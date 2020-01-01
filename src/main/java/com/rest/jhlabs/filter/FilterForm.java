package com.rest.jhlabs.filter;

import org.springframework.http.MediaType;
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

    public String getContentDisposition(){
        return "inline; filename=image." + getOutput();
    }

    public MediaType getMediaType(){
        return MediaType.parseMediaType("image/" + getOutput());
    }

}
