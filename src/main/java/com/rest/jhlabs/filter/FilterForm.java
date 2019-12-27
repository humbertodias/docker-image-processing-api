package com.rest.jhlabs.filter;

import org.springframework.web.multipart.MultipartFile;

public class FilterForm {

    private MultipartFile file;
    private String name;
    private String output;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public byte[] toBytes(){
        return new byte[0];
    }    
    
}
