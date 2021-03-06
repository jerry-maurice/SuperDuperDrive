package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    private FileService fileService;
    private UserService userService;

    public HomeController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, @ModelAttribute("newFile") FileForm newFile, Model model){
        Integer userId = getUserId(authentication);
        model.addAttribute("files", this.fileService.getAllUserFilesName(userId));
        return "home";
    }

    @PostMapping
    public String postFile(Authentication authentication, @ModelAttribute("newFile") FileForm newFile, Model model) throws IOException {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        newFile.setUsername(userName);

        List<String> fileList = fileService.getAllUserFilesName(userId);

        if(verifyIfDuplication(fileList,newFile.getFile().getOriginalFilename())){
            model.addAttribute("result","error");
            model.addAttribute("message","File is already uploaded. Please try again");
        }
        else {
            fileService.addFile(newFile);
            model.addAttribute("result", "success");
        }

        return "result";
    }

    public Boolean verifyIfDuplication(List<String> fileList, String fileName){
        for(String name:fileList){
            if(name.equals(fileName)){
                return true;
            }
        }
        return false;
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserId();
    }

    @GetMapping(
            value = "/get-file/{fileName}",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    public @ResponseBody byte[] getFile(@PathVariable String fileName) throws IOException
    {
        return fileService.getFile(fileName).getFileData();
    }

    @GetMapping(
            value = "/delete-file/{fileName}"
    )
    public String deleteFile(Authentication authentication,@PathVariable String fileName, @ModelAttribute("newFile") FileForm newFile, Model model){
        fileService.deleteFile(fileName);
        model.addAttribute("result", "success");
        return "result";
    }
}
