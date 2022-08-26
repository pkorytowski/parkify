package com.parkingsolutions.parkify.mongo.converter;


import com.parkingsolutions.parkify.document.Point;

import org.springframework.core.convert.converter.Converter;
import org.bson.Document;


public class PointReadConverter implements Converter<Document, Point> {

    @Override
    public Point convert(Document source) {
        return new Point(Double.parseDouble(source.get("x").toString()),Double.parseDouble(source.get("y").toString()));
    }
}
