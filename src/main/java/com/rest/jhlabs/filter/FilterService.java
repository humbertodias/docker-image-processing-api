package com.rest.jhlabs.filter;

import com.jhlabs.image.*;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

@Service
public class FilterService {

    // http://www.jhlabs.com/ip/filters/PointillizeFilter.html
    public BufferedImage pointillize(BufferedImage src){
        PointillizeFilter filter = new PointillizeFilter();
        filter.setEdgeThickness(0.45f);
        filter.setFadeEdges(false);
        filter.setEdgeColor(Color.GRAY.getRGB());
        filter.setFuzziness(1.0f);

        return filter.filter(src, null);
    }

    // http://www.jhlabs.com/ip/filters/BlurFilter.html
    public BufferedImage blur(BufferedImage src) {
        BlurFilter filter = new BlurFilter();
        Kernel kernel = new Kernel(3,3, new float[]{1,2,3,4,5,6,7,8,9});
        filter.setKernel(kernel);
        return filter.filter(src, null);
    }

    // http://www.jhlabs.com/ip/filters/RippleFilter.html
    public BufferedImage ripple(BufferedImage src) {
        RippleFilter filter = new RippleFilter();
        filter.setWaveType(RippleFilter.SINE);
        filter.setXAmplitude(2.6f);
        filter.setYAmplitude(1.7f);
        filter.setXWavelength(15);
        filter.setYWavelength(5);
        filter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);
        return filter.filter(src,null);
    }

    public BufferedImage shadow(BufferedImage src) {
        ShadowFilter filter = new ShadowFilter();
        filter.setRadius(10);
        filter.setDistance(5);
        return filter.filter(src, null);
    }

    public BufferedImage water(BufferedImage src) {
        WaterFilter filter = new WaterFilter();
        filter.setAmplitude(1.5f);
        filter.setPhase(10);
        filter.setWavelength(2);
        return filter.filter(src, null);
    }

    private Object invoke(String filterName, BufferedImage src) throws IllegalAccessException, InstantiationException {

        Class<?> clazz = getClass();
        Object instance = clazz.newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(filterName)) {
                try {
                    method.setAccessible(true);
                    return method.invoke(instance, src);
                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException | IllegalAccessException x) {
                    Throwable cause = x.getCause();
                    System.err.format("invocation of %s failed: %s%n",
                            method.getName(), cause.getMessage());
                }
            }

        }
        return null;
    }

    public Set<String> filters() {
        Set<String> filters = new HashSet<>();
        for (Method method : getClass().getDeclaredMethods()) {
            if(method.getGenericReturnType().getTypeName().contains("BufferedImage"))
                filters.add(method.getName());
        }
        return filters;
    }

    public byte[] apply(BufferedImage src, String filterName, String output) throws IOException, InstantiationException, IllegalAccessException {
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