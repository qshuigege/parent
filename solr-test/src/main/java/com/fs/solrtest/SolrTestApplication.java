package com.fs.solrtest;

import com.fs.diyutils.JsonResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@SpringBootApplication
@RestController
public class SolrTestApplication {

    @Autowired
    private SolrClient solrClient;

    public static void main(String[] args) {
        SpringApplication.run(SolrTestApplication.class, args);
    }

    @RequestMapping("/helloSolr")
    public JsonResult helloSolr() throws Exception{
        return JsonResult.success("", solrClient);
    }

    @RequestMapping("/add")
    public JsonResult add(){
        SolrInputDocument doc = new SolrInputDocument();
        doc.setField("id", UUID.randomUUID().toString().toLowerCase());
        doc.setField("content_ik", "我是中国人，我爱中国！");
        try {
            solrClient.add("testcore", doc);
            solrClient.commit("testcore");
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail("", e.getMessage());
        }
        return JsonResult.success("", "success");
    }

    @RequestMapping("/getById")
    public JsonResult getById(HttpServletRequest request){
        String id = request.getParameter("id");
        try {
            SolrDocument doc = solrClient.getById("testcore", id);
            return JsonResult.success("", doc);
        }catch (Exception e){
            return JsonResult.fail("fail", e.getMessage());
        }
    }

}
