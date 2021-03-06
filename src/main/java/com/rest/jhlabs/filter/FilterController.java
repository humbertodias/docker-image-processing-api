package com.rest.jhlabs.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class FilterController {

    @Autowired
    private FilterService filterService;

    @GetMapping("/filters")
    public Set<String> filters(){
        return filterService.filters();
    }

    @GetMapping("/filterParams")
    public HashMap<String, Map> filterParams(@RequestParam("filterClass") String filterClass) throws ClassNotFoundException {
        return filterService.filterParams(filterClass);
    }

    @PostMapping("/filter")
    public ResponseEntity<InputStreamResource> filter(@ModelAttribute FilterForm filterForm, @RequestParam Map<String,String> allParams) throws IOException, IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
        BufferedImage src = ImageIO.read(filterForm.getFile().getInputStream());
        byte[] bytes = filterService.apply(src, filterForm.getName(), filterForm.getOutput(), allParams);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", filterForm.getContentDisposition());

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(filterForm.getMediaType())
                .body(new InputStreamResource(new ByteArrayInputStream(bytes)));
    }


}
