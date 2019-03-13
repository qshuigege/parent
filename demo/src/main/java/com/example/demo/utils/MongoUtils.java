package com.example.demo.utils;

import org.bson.types.ObjectId;

import java.util.List;
import java.util.Map;

public class MongoUtils {

    public static void extractObjectIdInfo(Map doc){
        if(null == doc.get("_id")){
            return;
        }
        ObjectId id = (ObjectId)doc.get("_id");
        doc.put("id", id.toString());
    }

    public static void extractObjectIdInfo(List<Map> docLst){
        for (int i = 0; i < docLst.size(); i++) {
            Map m = docLst.get(i);
            if(null == m.get("_id")){
                return;
            }
            ObjectId id = (ObjectId)m.get("_id");
            m.put("id", id.toString());
        }
    }
}
