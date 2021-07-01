package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;
    private UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    @PostConstruct
    public  void postConstruct(){
        System.out.println("Creating FileService bean");
    }

    public File getFile(String fileName){
        return fileMapper.getFile(fileName);
    }

    public List<String> getAllUserFilesName(Integer userId){
        return fileMapper.getAllUserFilesName(userId);
    }

    public void addFile(FileForm fileForm) throws IOException {
        File newFile = new File();

        InputStream fis = fileForm.getFile().getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = fis.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        byte[] fileData = buffer.toByteArray();

        newFile.setFileName(fileForm.getFile().getOriginalFilename());
        newFile.setContentType(fileForm.getFile().getContentType());
        newFile.setFileSize(String.valueOf(fileForm.getFile().getSize()));
        newFile.setUserId(userMapper.getUser(fileForm.getUsername()).getUserId());
        newFile.setFileData(fileData);

        fileMapper.insert(newFile);
    }

    public void deleteFile(String fileName) {
        fileMapper.deleteFile(fileName);
    }
}
