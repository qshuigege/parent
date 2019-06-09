package com.fs.nothing.controller;

import com.fs.nothing.controller.redis.RedisOperator;
import com.fs.nothing.service.WXBusinessService;
import com.fs.nothing.utils.HttpClientUtils;
import com.fs.nothing.utils.JsonUtils;
import com.fs.nothing.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate defaultJdbcTemplate;

    @Autowired
    private RedisOperator redis;

    @Autowired
    private WXBusinessService service;

    @RequestMapping("test")
    @ResponseBody
    public Map<String, String> test(HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<>();
        /*Map<String, String[]> map = CommonUtils.mapKeyToLowerCase(request.getParameterMap());
        Iterator<Map.Entry<String, String[]>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String[]> en = iterator.next();
            System.out.println(en.getKey()+"-->"+en.getValue()[0]);
        }
        Map<String, String> req = CommonUtils.getAllParamsFromRequest(request.getParameterMap());
        System.out.println(req.get("ashe"));
        resultMap.put("test", request.getServletContext().getRealPath("/"));*/
        return resultMap;
    }

    @RequestMapping("testQueryForMap")
    @ResponseBody()
    public Map<String, Object> testQueryForMap(HttpServletRequest request){
        //Map<String, Object> resultMap = new HashMap<String, Object>();

        Map<String, Object> map = new HashMap<String, Object>();
        map = defaultJdbcTemplate.queryForMap("SELECT top 1 * FROM V_VIEW_THD_New WHERE PartnerID = 'C00001-A'");//queryForMap方法只能查一行数据，如果sql语句查出多行或0行，则抛出异常
        System.out.println("PartnerShortName-->"+map.get("PartnerShortName"));//查出来的map的key是不区分大小写的。(org.apache.commons.collections.map.CaseInsensitiveMap)
        System.out.println("partnershortname-->"+map.get("partnershortname"));
        return map;
        //resultMap.put("fsno3",maps.get(0).get("FSNO"));//+"-->"+maps.get(0).get("fsno"));
        //return map;
    }

    @RequestMapping("testQueryForList")
    @ResponseBody
    public List testQueryForList(){
        //List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("SELECT * FROM orguser WHERE ID = 'C00001-Aa' limit 10");//如果结果集为0条数据，list.size()=0
        System.out.println("maps.size()-->"+maps.size());
        return maps;
    }


    @RequestMapping("testUserContext")
    @ResponseBody
    public Map testUserContext(HttpServletRequest request){
        UserContext uc = (UserContext) request.getAttribute("userContext");
        Map<String,String> map = new HashMap<>();
        map.put("erpuseroid", uc.getErpuseroid());
        return map;
    }


    @RequestMapping("getQrcode")
    public void getQrcode(HttpServletRequest request, HttpServletResponse response)throws Exception{
//        service.getOrgidByUniqueid();
        String postData = "{\"expire_seconds\": 15, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"d6afe421-63a3-11e8-9d28-54ee75c23a17\"}}}";
        String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+redis.get("accesstoken");
        String ticketJson = null;
        try {
            //ticketJson = HttpRequestUtils.requestStringPost(ticket_qr_code_url, postData);
            ticketJson = HttpClientUtils.postStringEntity(ticket_qr_code_url, postData);
        } catch (Exception e) {

        }
        Map<String, Object> jo = JsonUtils.jsonToMap(ticketJson);
        if(jo.size()==0){

        }

        if(null==jo.get("ticket")){

        }
        String ticket = jo.get("ticket").toString();
        //String expire_seconds = jo.getString("expire_seconds");
        //String imgUrl = jo.getString("url");
        String erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
        PrintWriter out = response.getWriter();
        out.print("<html><body><img src=\""+erweimaurl+"\"></img></body></html>");
    }

    @RequestMapping("getQrcodeUnbinding")
    public void getQrcodeUnbinding(HttpServletRequest request, HttpServletResponse response)throws Exception{
        String postData = "{\"expire_seconds\": 15, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"0\"}}}";
        String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+redis.get("accesstoken");
        String ticketJson = null;
        try {
            //ticketJson = HttpRequestUtils.requestStringPost(ticket_qr_code_url, postData);
            ticketJson = HttpClientUtils.postStringEntity(ticket_qr_code_url, postData);
        } catch (Exception e) {

        }


        Map<String, Object> jo = JsonUtils.jsonToMap(ticketJson);
        if(jo.size()==0){

        }

        if(null==jo.get("ticket")){

        }
        String ticket = jo.get("ticket").toString();
        //String expire_seconds = jo.getString("expire_seconds");
        //String imgUrl = jo.getString("url");
        String erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
        PrintWriter out = response.getWriter();
        out.print("<html><body><img src=\""+erweimaurl+"\"></img></body></html>");
    }

}
