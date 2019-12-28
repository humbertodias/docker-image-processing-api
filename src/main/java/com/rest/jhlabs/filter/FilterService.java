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
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    // http://www.jhlabs.com/ip/filters/HighPassFilter.html
    public BufferedImage highPassFilter(BufferedImage src) {
        HighPassFilter filter = new HighPassFilter();
        return filter.filter(src, null);
    }

    public BufferedImage channelMixFilter(BufferedImage src) {
        ChannelMixFilter filter = new ChannelMixFilter();
        return filter.filter(src, null);
    }

    public BufferedImage curvesFilter(BufferedImage src) {
        CurvesFilter filter = new CurvesFilter();
        return filter.filter(src, null);
    }

    public BufferedImage diffuseFilter(BufferedImage src) {
        DiffuseFilter filter = new DiffuseFilter();
        return filter.filter(src, null);
    }

    public BufferedImage ditherFilter(BufferedImage src) {
        DitherFilter filter = new DitherFilter();
        return filter.filter(src, null);
    }

    public BufferedImage exposureFilter(BufferedImage src) {
        ExposureFilter filter = new ExposureFilter();
        return filter.filter(src, null);
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

    private Object invoke(String filterName, BufferedImage src) throws IllegalAccessException, InstantiationException, InvocationTargetException {

        Class<?> clazz = getClass();
        Object instance = clazz.newInstance();

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(filterName)) {
                method.setAccessible(true);
                return method.invoke(instance, src);
            }

        }
        return null;
    }

    public Set<String> filters() {
        Set<String> filters = new HashSet<>();
        for (Method method : getClass().getDeclaredMethods()) {
            if(method.getReturnType().equals(BufferedImage.class))
                filters.add(method.getName());
        }
        // Sort by name
        return filters.stream().collect(Collectors.toCollection(TreeSet::new));
    }

    public byte[] apply(BufferedImage src, String filterName, String output) throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
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