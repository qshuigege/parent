package com.fs.myerp.controller;

import com.fs.diyutils.JsonResult;
import com.fs.myerp.dao.TestMybatisDao;
import com.fs.myerp.utils.ReusableCodes;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    private Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private TestMybatisDao dao;

    @RequestMapping("/testRedis")
    public JsonResult testRedis(){
        String aaa = redis.opsForValue().get("aaa");
        return JsonResult.success("", aaa);
    }

    @RequestMapping("/testMysql")
    public JsonResult testMysql(){
        Map<String, Object> map = template.queryForMap("select * from emp limit 1");
        return JsonResult.success("", map);
    }

    @RequestMapping("/testMybatis")
    @Transactional
    public JsonResult testMybatis(HttpServletRequest request){
        /*SqlSession sqlSession = sqlSessionFactory.openSession(false);//取消自动提交事务
        List<Object> list = sqlSession.selectList("");*/

        String userid = request.getParameter("userid");
        String pageNumber = request.getParameter("pageNumber");
        String pageSize = request.getParameter("pageSize");
        log.debug("userid-->{}, pageNumber-->{}, pageSize-->{}", userid, pageNumber, pageSize);
        Map<String, Object> map = new HashMap<>();
        try {
            int rowindex = ReusableCodes.calcRowindex(pageNumber, pageSize);
            map.put("userid", userid);
            //List<Map<String, Object>> list = dao.getList(map);
            map.put("begin", "2019-7-7");
            map.put("end", "2019-7-9");
            int count = dao.getCount(map);
            map.put("rowindex", rowindex);
            map.put("pagesize", Integer.parseInt(pageSize));
            List<Map<String, Object>> maps = dao.testIf(map);

            return JsonResult.success(count + "", maps);
        }catch (Exception e){
            map.put("error", e);
            return JsonResult.fail(e.getMessage(), map);
        }
    }

    @RequestMapping("/testPagehelper")
    public JsonResult testPagehelper(@RequestParam Map<String, Object> params){
        PageHelper.startPage(Integer.parseInt(params.get("pageNum").toString()), Integer.parseInt(params.get("pageSize").toString()));
        Map<String, Object> map = new HashMap<>();
        map.put("begin", null);
        map.put("end", null);
        List<Map<String, Object>> maps = dao.testPagehelper(map);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(maps);
        return JsonResult.success(pageInfo.getTotal()+"", pageInfo);
    }

    @RequestMapping("/testMybatisSqlSession")
    public JsonResult testMybatisSqlSession(){
        SqlSession sqlSession = sqlSessionFactory.openSession(false);
        try {
            List<Object> list = sqlSession.selectList("com.fs.myerp.dao.TestMybatisDao.getList");
            sqlSession.commit();
            return JsonResult.success("", list);
        }catch (Exception e){
            sqlSession.rollback();
            return JsonResult.fail(e.getMessage(), e);
        }
    }
}
