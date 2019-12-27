package com.rest.jhlabs.filter;

import com.jhlabs.image.*;
import com.rest.jhlabs.Util;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class FilterService {

    public BufferedImage pointillize(BufferedImage src){
        PointillizeFilter filter = new PointillizeFilter();
        filter.setEdgeThickness(0.45f);
        filter.setFadeEdges(false);
        filter.setEdgeColor(Color.GRAY.getRGB());
        filter.setFuzziness(1.0f);

        return filter.filter(src,dst(src));
    }


    public BufferedImage blur(BufferedImage src) {
        GaussianFilter filter = new GaussianFilter();
        return filter.filter(src, dst(src));
    }


    public BufferedImage shadow(BufferedImage src) {
        ShadowFilter filter = new ShadowFilter();
        filter.setRadius(10);
        filter.setDistance(5);
        filter.setOpacity(1);
        return filter.filter(src, dst(src));
    }

    public BufferedImage water(BufferedImage src) {
        WaterFilter filter = new WaterFilter();
        filter.setAmplitude(1.5f);
        filter.setPhase(10);
        filter.setWavelength(2);
        return filter.filter(src, dst(src));
    }

    public BufferedImage ripple(BufferedImage src) {
        RippleFilter filter = new RippleFilter();
        filter.setWaveType(RippleFilter.SINE);
        filter.setXAmplitude(2.6f);
        filter.setYAmplitude(1.7f);
        filter.setXWavelength(15);
        filter.setYWavelength(5);
        filter.setEdgeAction(TransformFilter.NEAREST_NEIGHBOUR);
        return filter.filter(src, dst(src));
    }



    public byte[] apply(BufferedImage src, String name, String output) throws IOException {
        BufferedImage dst = null;
        switch(Filters.valueOf(name.toUpperCase())){
            case POINTILLIZE:
                dst = pointillize(src);
                break;
            case BLUR:
                dst = blur(src);
                break;
            case WATER:
                dst = water(src);
                break;
            case RIPPLE:
                dst = ripple(src);
                break;
            case SHADOW:
                dst = shadow(src);
                break;
        }
        return dst != null ? Util.toBytes(dst, output) : null;
    }

    private BufferedImage dst(BufferedImage src){
        return new BufferedImage(src.getWidth(),src.getHeight(),BufferedImage.TYPE_INT_RGB);
    }

}