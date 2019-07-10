package com.fs.myerp.controller;

import com.fs.diyutils.JsonResult;
import com.fs.myerp.dao.TestMybatisDao;
import com.fs.myerp.model.Emp;
import com.fs.myerp.model.Test;
import com.fs.myerp.utils.ReusableCodes;
import com.fs.myerp.vo.TestDozerVo;
import com.fs.myerp.vo.TestVo;
import com.github.dozermapper.core.Mapper;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Autowired
    private Mapper dozer;

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

    @RequestMapping("/testMybatisDataParam")
    public JsonResult testMybatisDataParam(@RequestBody TestVo vo){
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<Test> tests = dao.testMybatisDateParam(vo);
        PageInfo<Test> pageInfo = new PageInfo<>(tests);
        Map<String, Object> result = new HashMap<>();
        List<TestDozerVo> dlist = new ArrayList<>();
        for (int i = 0; i < tests.size(); i++) {
            dlist.add(dozer.map(tests.get(i), TestDozerVo.class));
        }
        result.put("test1", dlist);

        TestDozerVo dozerVo = dozer.map(vo, TestDozerVo.class);
        result.put("test2", dozerVo);

        List l = dozer.map(tests, List.class);
        result.put("test3", l);

        return JsonResult.success(pageInfo.getTotal()+"", result);
    }

    @RequestMapping("/testConcat")
    public JsonResult testConcat(@RequestBody List<Emp> empList){
        List<Emp> emps = dao.testConcat(empList);
        return JsonResult.success("", emps);
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

    @RequestMapping("/testRestful/{name}")
    public JsonResult testRestful(@PathVariable("name") String name){
        return JsonResult.success(name, name);
    }

    @RequestMapping("/testRequestBody")
    public JsonResult testRequestBody(@RequestBody List<Map<String, Object>> list){
        list.forEach(m -> m.forEach((key, value) -> {
            log.debug("key-->{}, value-->{}", key, value);
        }));
        return JsonResult.success("", list);
    }

    @RequestMapping("/testRequestParam")
    public JsonResult testRequestParam(@RequestBody TestVo vo, @RequestParam("username") String name){
        return JsonResult.success(name, vo);
    }
}
