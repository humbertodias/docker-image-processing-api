package com.rest.jhlabs.filter;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilterService {

    final String FILTER_PACKAGE = "com.jhlabs.image";

    private Object invoke(String filterName, BufferedImage src) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
        String clazzFullName = FILTER_PACKAGE + "." + filterName;
        Class<?> clazz = Class.forName(clazzFullName);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        Method method = clazz.getMethod("filter", BufferedImage.class, BufferedImage.class);
        method.setAccessible(true);
        return method.invoke(instance, src, null);
    }

    public Set<String> filters() {
        Reflections reflections =
                new Reflections(new ConfigurationBuilder()
                        .filterInputsBy(new FilterBuilder().includePackage(FILTER_PACKAGE))
                        .setUrls(ClasspathHelper.forPackage(FILTER_PACKAGE))
                        .setScanners(new SubTypesScanner(false)));

        Set<String> filters = reflections.getAllTypes();
        // Sort by name
        return filters.stream().filter(name -> name.endsWith("Filter")).map(s -> s.substring(FILTER_PACKAGE.length()+1)).collect(Collectors.toCollection(TreeSet::new));
    }

    public byte[] apply(BufferedImage src, String filterName, String output) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
        Object ret = invoke(filterName, src);
        return ret != null ? toBytes((BufferedImage) ret, output) : null;
    }

    public static byte[] toBytes(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }

}