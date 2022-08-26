package com.parkingsolutions.parkify.mongo.converter;


import com.parkingsolutions.parkify.document.Point;

import org.springframework.core.convert.converter.Converter;
import org.bson.Document;


public class PointWriteConverter implements Converter<Point, Document> {


    @Override
    public Document convert(Point source) {
        Document document = new Document();
        document.put("x", source.getX());
        document.put("y", source.getY());
        return document;
    }
}
