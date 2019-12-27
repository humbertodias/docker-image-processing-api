package com.rest.jhlabs.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class FilterController {

    @Autowired
    private FilterService filterService;

    @PostMapping("/filter")
    public ResponseEntity<InputStreamResource> filter(@ModelAttribute FilterForm filterForm) throws IOException {
        BufferedImage src = ImageIO.read(filterForm.getFile().getInputStream());
        byte[] bytes = filterService.apply(src, filterForm.getName(), filterForm.getOutput());

        String contentDisposition = "inline; filename=image." + filterForm.getOutput();
        MediaType mediaType = MediaType.parseMediaType("image/" + filterForm.getOutput());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", contentDisposition);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(mediaType)
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }

}
