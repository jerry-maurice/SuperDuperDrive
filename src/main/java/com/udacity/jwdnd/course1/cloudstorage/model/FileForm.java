package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileForm {
    private MultipartFile file;
    private String username;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) throws IOException {
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
