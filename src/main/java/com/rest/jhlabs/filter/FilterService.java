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
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilterService {

    final String FILTER_PACKAGE = "com.jhlabs.image";

    Class<?> filterClass(String filterName) throws ClassNotFoundException {
        return Class.forName(FILTER_PACKAGE + "." + filterName);
    }

    private Object invoke(String filterName, Map<String,String> sets, BufferedImage src) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> clazz = filterClass(filterName);
        Object instance = clazz.getDeclaredConstructor().newInstance();
        sets.entrySet().stream().filter(entry -> entry.getKey().startsWith("set")).forEach(
                entry -> {
                    System.out.println(entry);
                }
        );
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

    public HashMap<String,Map> filterParams(String filterClass) throws ClassNotFoundException {
        HashMap<String,Map> params = new LinkedHashMap();
        Class<?> clazz = filterClass(filterClass);
        Set<Method> setMethods = Arrays.asList(clazz.getMethods()).stream().filter(method -> method.getName().startsWith("set")).collect(Collectors.toSet());
        setMethods.stream().forEach(method -> params.put(method.getName(), paramTypes(method)));
        return params;
    }

    private Map<String,String> paramTypes(Method method) {
        Map<String,String> params = new LinkedHashMap();
        for(Parameter parameter : method.getParameters()) {
            params.put(parameter.getName(), parameter.getParameterizedType().getTypeName());
        }
        return params;
    }

    public byte[] apply(BufferedImage src, String filterName, String output, Map<String, String> sets) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, NoSuchMethodException {
        Object ret = invoke(filterName, sets, src);
        return ret != null ? toBytes((BufferedImage) ret, output) : null;
    }

    public static byte[] toBytes(BufferedImage image, String type) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            ImageIO.write(image, type, out);
            return out.toByteArray();
        }
    }

}