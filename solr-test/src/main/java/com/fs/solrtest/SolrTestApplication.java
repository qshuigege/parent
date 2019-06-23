package com.fs.solrtest;

import com.fs.diyutils.JsonResult;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
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
            UpdateResponse resp = solrClient.commit("testcore");
            return JsonResult.success("", resp.getStatus());
        }catch (Exception e){
            e.printStackTrace();
            return JsonResult.fail("", e.getMessage());
        }
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

    @RequestMapping("/delById")
    public JsonResult delById(HttpServletRequest request){
        String id = request.getParameter("id");
        try {
            solrClient.deleteById("testcore", id);
            UpdateResponse resp = solrClient.commit("testcore");
            return JsonResult.success("", resp.getRequestUrl());
        }catch (Exception e){
            return JsonResult.fail("fail", e.getMessage());
        }
    }

    @RequestMapping("/delAll")
    public JsonResult delAll(HttpServletRequest request){
        try {
            UpdateResponse resp = solrClient.deleteByQuery("testcore", "*:*");
            UpdateResponse resp2 = solrClient.commit("testcore");
            return JsonResult.success("", resp2.getRequestUrl());
        }catch (Exception e){
            return JsonResult.fail("fail", e.getMessage());
        }
    }

    @RequestMapping("/queryTest")
    public JsonResult queryTest(HttpServletRequest request){
        String queryparams = request.getParameter("queryparams");
        SolrQuery query = new SolrQuery();
        query.set("q", queryparams);

        //高亮
        query.setHighlight(true);//打开高亮
        query.addHighlightField("mobile_desc");//指定高亮域
        query.setHighlightSimplePre("<span style='color:red'>");//设置前缀
        query.setHighlightSimplePost("</span>");//设置后缀
        QueryResponse queryResponse = null;
        try {
            queryResponse = solrClient.query("testcore", query);
        }catch (Exception e){
            return JsonResult.fail("", e.getMessage());
        }
        SolrDocumentList results = queryResponse.getResults();

        long numFound = results.getNumFound();

        System.out.println(numFound);

        //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
        Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();

        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("mobile_desc"));
            //System.out.println(result.get("product_num"));
            System.out.println(result.get("content_ik"));
            //System.out.println(result.get("product_image"));

            Map<String, List<String>> map = highlight.get(result.get("id"));
            List<String> list = map.get("id");
            //System.out.println(list.get(0));
            System.out.println(map);

            System.out.println("------------------");
            System.out.println();
        }
        return JsonResult.success("", highlight);
    }

    //综合查询
    @RequestMapping("/search")
    public JsonResult search(HttpServletRequest request){
        SolrQuery query = new SolrQuery();

        //查询条件
        query.set("q", "手机");

        //过滤条件
        query.set("fq", "product_price:[100 TO 10000]");

        //排序
        query.addSort("product_price", SolrQuery.ORDER.asc);

        //分页
        query.setStart(0);
        query.setRows(10);

        //默认域
        query.set("df", "product_title");

        //只查询指定域
        query.set("fl", "id,product_title,product_price");

        //高亮
        query.setHighlight(true);//打开高亮
        query.addHighlightField("product_title");//指定高亮域
        query.setHighlightSimplePre("<span style='color:red'>");//设置前缀
        query.setHighlightSimplePost("</span>");//设置后缀

        QueryResponse queryResponse = null;
        try {
            queryResponse = solrClient.query(query);
        }catch (Exception e){
            return JsonResult.fail("", e.getMessage());
        }
        SolrDocumentList results = queryResponse.getResults();

        long numFound = results.getNumFound();

        System.out.println(numFound);

        //获取高亮显示的结果, 高亮显示的结果和查询结果是分开放的
        Map<String, Map<String, List<String>>> highlight = queryResponse.getHighlighting();

        for (SolrDocument result : results) {
            System.out.println(result.get("id"));
            System.out.println(result.get("product_title"));
            //System.out.println(result.get("product_num"));
            System.out.println(result.get("product_price"));
            //System.out.println(result.get("product_image"));

            Map<String, List<String>> map = highlight.get(result.get("id"));
            List<String> list = map.get("product_title");
            System.out.println(list.get(0));

            System.out.println("------------------");
            System.out.println();
        }
        return JsonResult.success("", highlight);

    }

}
