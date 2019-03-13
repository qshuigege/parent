package com.example.demo.controller;

import com.example.demo.utils.DateUtils;
import com.example.demo.utils.JsonResult;
import com.example.demo.utils.MongoUtils;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/demo/mongodb")
public class MongoDBTestController {

    private static Logger log = LoggerFactory.getLogger(MongoDBTestController.class);

    @Autowired
    @Qualifier("mongoTemplate1")
    private MongoTemplate mongoTemplate1;

    @Autowired
    @Qualifier("mongoTemplate2")
    private MongoTemplate mongoTemplate2;

    @RequestMapping("helloMongoDB")
    public JsonResult helloMongoDB(){
        Map<String, Object> ret = new HashMap<>();
        Query query = new Query();
        long count = mongoTemplate1.count(query, "testcollection");
        List<Map> lst = mongoTemplate1.findAll(Map.class, "testcollection");
        ret.put("count", count);
        ret.put("docs", lst);
        return JsonResult.success("", ret);
    }

    @RequestMapping("mongoInsert")
    public JsonResult mongoInsert(HttpServletRequest request){

        String name = request.getParameter("name");
        String ageStr = request.getParameter("age");
        int age;
        try {
            age = Integer.parseInt(ageStr);
        }catch (Exception e){
            age = -1;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("age", age);
        mongoTemplate1.insert(map, "testcollection");
        Query query = new Query();
        long count = mongoTemplate1.count(query, "testcollection");
        List<Map> lst = mongoTemplate1.findAll(Map.class, "testcollection");
        MongoUtils.extractObjectIdInfo(lst);
        map.clear();
        map.put("count", count);
        map.put("docs", lst);
        return JsonResult.success("", map);
    }

    @RequestMapping("mongoInsertAll")
    public JsonResult mongoInsertAll(){
        List<Map<String, Object>> lst = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name111");
        map.put("age", 11);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "name222");
        map2.put("age", 12);
        lst.add(map);
        lst.add(map2);
        /*Collection<Map<String, Object>> insertResult = */mongoTemplate1.insert(lst, "testcollection");
        Query query = new Query();
        long count = mongoTemplate1.count(query, "testcollection");
        List<Map> result = mongoTemplate1.findAll(Map.class, "testcollection");
        MongoUtils.extractObjectIdInfo(result);
        map.clear();
        map.put("count", count);
        map.put("docs", result);
        /*map.put("insertResult", insertResult);*/
        return JsonResult.success("", map);
    }

    @RequestMapping("mongoFindById")
    public JsonResult mongoFindById(HttpServletRequest request){
        String id = request.getParameter("id");
        Query query = new Query();
        long count = mongoTemplate1.count(query, "testcollection");
        Map result = mongoTemplate1.findById(id, Map.class, "testcollection");
        return JsonResult.success("", result);
    }

    @RequestMapping("mongoFindViaCriteria")
    public JsonResult mongoFindViaCriteria(HttpServletRequest request){
        String id = request.getParameter("id");
        Document queryObj = new Document();
        Document fieldObj = new Document();
        fieldObj.put("_id", false);
        //Query query = new BasicQuery(queryObj, fieldObj);
        Query query = new BasicQuery("{}", "{_id:1}");
        long count = mongoTemplate1.count(query, "testcollection");
        List<Map> lst = mongoTemplate1.find(query, Map.class, "testcollection");
        MongoUtils.extractObjectIdInfo(lst);
        return JsonResult.success("", lst);
    }

    @RequestMapping("mongoFindPeriodDataByObjectId")
    public JsonResult mongoFindPeroidDateByObjectId(HttpServletRequest request){
        String dateStr1 = request.getParameter("date_str1");
        String dateStr2 = request.getParameter("date_str2");
        Date d1;
        Date d2;
        try {
            d1 = DateUtils.parse(dateStr1);
            d2 = DateUtils.parse(dateStr2);
        }catch (Exception e){
            return JsonResult.fail("日期格式错误！", "");
        }
        long time1 = d1.getTime()/1000;
        long time2 = d2.getTime()/1000;
        //double floor = Math.floor(time / 1000);
        String objectidtime1 = Long.toHexString(time1);
        String objectidtime2 = Long.toHexString(time2);
        objectidtime1 += "0000000000000000";
        objectidtime2 += "0000000000000000";
        /*Document queryObj = new Document();
        Document fieldObj = new Document();
        fieldObj.put("_id", false);*/
        //Query query = new BasicQuery(queryObj, fieldObj);

        String pattern = "{_id:{$gt:ObjectId('"+objectidtime1+"'), $lt:ObjectId('"+objectidtime2+"')}}";
        log.info(pattern);
        Query query = new BasicQuery(pattern);

        long count = mongoTemplate1.count(query, "t_history_location");
        List<Map> lst = mongoTemplate1.find(query, Map.class, "t_history_location");
        Map m = new HashMap();
        m.put("count", count);
        lst.add(m);
        MongoUtils.extractObjectIdInfo(lst);
        return JsonResult.success("", lst);
    }

    @RequestMapping("mongoFindPeriodDataByObjectId_prod")
    public JsonResult mongoFindPeroidDateByObjectId_prod(HttpServletRequest request){
        String mailno = request.getParameter("mailno");
        String limit = request.getParameter("limit");
        String dateStr1 = request.getParameter("date_str1");
        String dateStr2 = request.getParameter("date_str2");
        Date d1;
        Date d2;
        try {
            d1 = DateUtils.parse(dateStr1);
            d2 = DateUtils.parse(dateStr2);
        }catch (Exception e){
            return JsonResult.fail("日期格式错误！", "");
        }
        long time1 = d1.getTime()/1000;
        long time2 = d2.getTime()/1000;
        //double floor = Math.floor(time / 1000);
        String objectidtime1 = Long.toHexString(time1);
        String objectidtime2 = Long.toHexString(time2);
        objectidtime1 += "0000000000000000";
        objectidtime2 += "0000000000000000";
        /*Document queryObj = new Document();
        Document fieldObj = new Document();
        fieldObj.put("_id", false);*/
        //Query query = new BasicQuery(queryObj, fieldObj);

        String pattern = "{_id:{$gt:ObjectId('"+objectidtime1+"'), $lt:ObjectId('"+objectidtime2+"')}}";
        pattern = "{acceptTime:{$gt:'"+dateStr1+"', $lt:'"+dateStr2+"'},mailno:'"+mailno+"'}";
        log.info(pattern);
        Query query = new BasicQuery(pattern);
        query.with(new Sort(Sort.Direction.DESC, "acceptTime")).limit(Integer.parseInt(limit));
        long count = mongoTemplate2.count(query, "t_shunfeng_node");
        List<Map> lst = mongoTemplate2.find(query, Map.class, "t_shunfeng_node");
        Map m = new HashMap();
        m.put("count", count);
        lst.add(m);
        //MongoUtils.extractObjectIdInfo(lst);
        return JsonResult.success("", lst);
    }



    public static void main(String[] args)throws Exception{
        /*Date d = DateUtils.parse("2018-12-27 17:00:00");
        long time = d.getTime()/1000;
        //double floor = Math.floor(time / 1000);
        String s = Long.toHexString(time);
        System.out.println(s);
        ObjectId objectId = ObjectId.createFromLegacyFormat(Integer.parseInt(time+""),0,0);
        System.out.println(objectId.toString());*/

        String str = "快件到达 深圳宝安易利科技园营业部 ";
        System.out.println(str.indexOf("【"));
        System.out.println(str.indexOf("*"));
        System.out.println(str.substring(str.indexOf("【")+1, str.indexOf("】")));
    }

}
