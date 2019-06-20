package com.fs.myerp.controller;

import com.fs.diyutils.JsonResult;
import com.fs.myerp.dao.TestMybatisDao;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

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
    public JsonResult testMybatis(){
        /*SqlSession sqlSession = sqlSessionFactory.openSession(false);//取消自动提交事务
        List<Object> list = sqlSession.selectList("");*/

        Map<String, String> map = new HashMap<>();
        List<Object> list = dao.getList(map);

        return JsonResult.success("", list);
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
