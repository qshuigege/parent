package com.fs.myerp.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.diyutils.JsonResult;
import com.fs.myerp.dao.TestMybatisDao;
import com.fs.myerp.model.Emp;
import com.fs.myerp.po.LoginPo;
import com.fs.myerp.service.AopTestService;
import com.fs.myerp.utils.ReusableCodes;
import com.fs.myerp.utils.ValidationUtils;
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
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.util.*;

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
    private AopTestService aopTestService;

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

    @RequestMapping("/testMybatisConcat")
    public JsonResponse testMybatisConcat(@RequestBody List<Emp> empList){
        List<Emp> emps = dao.testConcat(empList);
        return JsonResponse.success(emps);
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

    @RequestMapping(value = "/testAop", method = RequestMethod.POST)
    public JsonResponse testAop(HttpServletRequest request){
        return aopTestService.testAop(request);
    }

    @RequestMapping(value = "/testFileUpload", method = RequestMethod.POST)
    public JsonResponse testFileUpload(HttpServletRequest request){
        return aopTestService.testAop(request);
    }

    @RequestMapping(value = "/testMultipartFile")
    public JsonResponse testMultipartFile(@RequestParam(value = "file")MultipartFile mpf, HttpServletRequest request){
        try {
            String classpath = ResourceUtils.getURL("classpath:").getPath();
            log.debug("classpath-->{}", classpath);
            File uploadPathFile = new File(classpath + File.separator + "upload");
            if (!uploadPathFile.exists()) {
                uploadPathFile.mkdir();
            }
            log.debug("path-->{}", uploadPathFile.getPath());
            log.debug("name-->{}", uploadPathFile.getName());
            log.debug("absolutePath-->{}", uploadPathFile.getAbsolutePath());
            log.debug("canonicalPath-->{}", uploadPathFile.getCanonicalPath());
            log.debug("parentPath-->{}", uploadPathFile.getParent());
            File file = new File(uploadPathFile.getPath() + File.separator + UUID.randomUUID()+mpf.getOriginalFilename());
            FileCopyUtils.copy(mpf.getBytes(), file);
            System.out.println("aaa-->"+classpath);
            System.out.println("bbb-->"+request.getServletPath());
            System.out.println("ccc-->"+request.getServletContext().getContextPath());
            System.out.println("ccc22-->"+request.getServletContext().getRealPath("/"));
            System.out.println("ddd-->"+request.getParameter("username"));
            System.out.println("eee-->"+mpf.getOriginalFilename());
            MultipartHttpServletRequest mReq = (MultipartHttpServletRequest)request;
            MultipartFile mpf2 = mReq.getFile("file");
            System.out.println(mpf2.getOriginalFilename());
            if(mpf == null){

                System.out.println("mpf is nullaaa");
            }else {
                System.out.println("oooook");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JsonResponse.success("ok");
    }

    @RequestMapping(value = "/testValidation", method = RequestMethod.POST)
    public JsonResponse testValidation(HttpServletRequest request, @Validated @RequestBody LoginPo loginPo, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return JsonResponse.fail(ValidationUtils.getFailMsg(bindingResult));
        }else{
            return JsonResponse.success("ok");
        }
    }

    @RequestMapping(value = "/testUploadFile", method = RequestMethod.POST)
    public JsonResponse testUploadFile(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {

        try {
            String fileName = UUID.randomUUID().toString().toLowerCase()+"_"+uploadFile.getOriginalFilename();
            File uploadDir = new File(new File("upload").getAbsolutePath());
            if(!uploadDir.exists()) {//如果不存在则创建目录
                uploadDir.mkdirs();
            }
            File file = new File(uploadDir.getPath() + File.separator + fileName);
            FileCopyUtils.copy(uploadFile.getBytes(), file);
            return JsonResponse.success("上传文件成功！");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("上传文件异常！-->{}", e.getMessage());
            return JsonResponse.fail("上传文件异常！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/testDownloadFile")
    public void testDownloadFile(@RequestParam("file") String fileName, HttpServletRequest request, HttpServletResponse response) {
        File uploadDir = new File(new File("upload").getAbsolutePath());
        File file = new File(uploadDir.getPath() + File.separator + fileName);
        response.setHeader("Content-Disposition", "attachment;fileName="+fileName);
        if (!file.exists()) return;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] buff = new byte[1024];
            int len = -1;
            while ((len = fileInputStream.read(buff))>0){
                outputStream.write(buff, 0, len);
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/test/testSameUrl")
    public JsonResponse testSameUrlGet(HttpServletRequest request){
        return JsonResponse.success(request.getMethod());
    }

    @PostMapping("/test/testSameUrl")
    public JsonResponse testSameUrlPost(HttpServletRequest request){
        return JsonResponse.success(request.getMethod());
    }

}
