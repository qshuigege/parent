package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.dao.ParkingChannelDao;
import com.drzk.parkingguide.dao.ParkingFileUploadDao;
import com.drzk.parkingguide.dao.ParkingRegionDao;
import com.drzk.parkingguide.po.ParkingChannelPo;
import com.drzk.parkingguide.po.ParkingFileUploadPo;
import com.drzk.parkingguide.po.ParkingRegionPo;
import com.drzk.parkingguide.service.ParkingRegionService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.vo.ParkingChannelVo;
import com.drzk.parkingguide.vo.ParkingFileUploadVo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class ParkingRegionServiceImpl implements ParkingRegionService {

    private Logger log = LoggerFactory.getLogger(ParkingRegionServiceImpl.class);

    @Autowired
    private ParkingRegionDao dao;

    @Autowired
    private ParkingFileUploadDao fileUploadDao;

    @Autowired
    private ParkingChannelDao channelDao;

    @Override
    public JsonResponse permissionAddParkingRegion(ParkingRegionPo po, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"regionName", "floorId", "totalPlaces", "specificPlaces", "tempPlaces"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, po);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        if (po.getTotalPlaces() <= 0){
            return JsonResponse.fail("002", "总车位数必须大于0");
        }
        if (po.getSpecificPlaces() < 0){
            return JsonResponse.fail("003", "固定车位数必须大于或等于0");
        }
        if (po.getTempPlaces() < 0){
            return JsonResponse.fail("004", "临时车位数必须大于或等于0");
        }
        if ((po.getSpecificPlaces()+po.getTempPlaces())>po.getTotalPlaces()){
            return JsonResponse.fail("005", "固定车位数与临时车位数之和不能大于总车位数！");
        }
        int usable = dao.validateAddRegionTotalPlaces(po);
        if (usable < po.getTotalPlaces()){
            return JsonResponse.fail("006", "参数中totalPlaces字段值无效！所有区域总车位数之和不能大于楼层总车位数！当前楼层剩余最大可分配车位数为"+usable);
        }
        po.setRemainPlaces(po.getTotalPlaces());//添加一个新区域后设置默认剩余车位数等于区域总车位数
        int i = dao.addParkingRegion(po);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse permissionBatchInsert(List<ParkingRegionPo> list, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        int i = dao.batchInsert(list);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public JsonResponse queryParkingRegionPage(ParkingRegionVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        List<ParkingRegionVo> lst = dao.queryParkingRegionPage(vo);
        PageInfo<ParkingRegionVo> pageInfo = new PageInfo<>(lst);
        return JsonResponse.success(pageInfo);
    }

    @Override
    public JsonResponse permissionDeleteById(List<ParkingRegionPo> list, HttpServletRequest request) {
        for (ParkingRegionPo regionPo : list) {
            ParkingChannelVo channelVo = new ParkingChannelVo();
            channelVo.setRegionId(regionPo.getId());
            int count = channelDao.getCountByRegionId(channelVo);
            if (count > 0){
                return JsonResponse.fail("008", "该区域下存在通道信息，请先删除通道信息！");
            }
        }
        int rows = dao.deleteByIdBatch(list);
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse permissionUpdateById(ParkingRegionPo po, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"id", "regionName", "floorId", "totalPlaces", "specificPlaces", "tempPlaces"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, po);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        if (po.getTotalPlaces() <= 0){
            return JsonResponse.fail("002", "总车位数必须大于0");
        }
        if (po.getSpecificPlaces() < 0){
            return JsonResponse.fail("003", "固定车位数必须大于或等于0");
        }
        if (po.getTempPlaces() < 0){
            return JsonResponse.fail("004", "临时车位数必须大于或等于0");
        }
        if ((po.getSpecificPlaces()+po.getTempPlaces())>po.getTotalPlaces()){
            return JsonResponse.fail("005", "固定车位数与临时车位数之和不能大于总车位数！");
        }
        ParkingRegionVo dbVal = dao.queryById(po);
        int usable = dao.validateAddRegionTotalPlaces(po);
        if (usable < (po.getTotalPlaces()- dbVal.getTotalPlaces())){
            return JsonResponse.fail("006", "参数中totalPlaces字段值无效！所有区域总车位数之和不能大于楼层总车位数！当前楼层剩余最大可分配车位数为"+usable);
        }
        if (!po.getFloorId().equals(dbVal.getFloorId())){
            return JsonResponse.fail("007", "不支持更改区域的所属楼层信息，可以先删除再添加！");
        }
        int rows = dao.updateById(po);
        /*if (rows > 0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail("更新失败！");
        }*/
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse queryById(ParkingRegionPo po, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        return JsonResponse.success(dao.queryById(po));
    }

    @Override
    public JsonResponse queryRegionsByBelongToFloorId(ParkingRegionVo regionVo){
        log.info("请求参数-->{}", JsonUtils.objectToJson(regionVo));
        return JsonResponse.success(dao.queryRegionsByBelongToFloorId(regionVo));
    }

    @Override
    public JsonResponse permissionUploadRegionMap(MultipartFile uploadFile, HttpServletRequest request) throws IOException {
        log.info("请求参数-->文件名称：{}", uploadFile.getOriginalFilename());
        String fileName = UUID.randomUUID().toString().toLowerCase()+"_"+uploadFile.getOriginalFilename();
        File uploadDir = new File(new File("upload").getAbsolutePath());
        if(!uploadDir.exists()) {//如果不存在则创建目录
            uploadDir.mkdirs();
        }
        File file = new File(uploadDir.getPath() + File.separator + fileName);
        FileCopyUtils.copy(uploadFile.getBytes(), file);
        ParkingFileUploadPo po = new ParkingFileUploadPo();
        String id = UUID.randomUUID().toString().toLowerCase();
        po.setId(id);
        po.setFileName(fileName);
        //po.setRegionId(regionId);
        fileUploadDao.insert(po);
        return JsonResponse.success(id);
        //return JsonResponse.success("上传文件成功！");
        //return JsonResponse.fail("上传文件异常！-->"+e.getMessage());
        /*String classpath = ResourceUtils.getURL("classpath:").getPath();
        File uploadPathFile = new File(classpath + File.separator + "upload");
        if (!uploadPathFile.exists()) {
            uploadPathFile.mkdir();
        }
        String fileName = UUID.randomUUID()+"_"+uploadFile.getOriginalFilename();
        File file = new File(uploadPathFile.getPath() + File.separator + fileName);
        FileCopyUtils.copy(uploadFile.getBytes(), file);
        ParkingFileUploadPo po = new ParkingFileUploadPo();
        po.setId(UUID.randomUUID().toString().toLowerCase());
        po.setFileName(fileName);
        po.setRegionId(regionId);
        fileUploadDao.insert(po);*/
    }

    @Override
    public JsonResponse permissionUpdateRegionMapByRegionId(ParkingRegionVo regionVo, HttpServletRequest request) throws Exception {
        log.info("请求参数-->{}", JsonUtils.objectToJson(regionVo));
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "regionMapId"}, regionVo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        int rows = dao.updateRegionMapById(regionVo);
        if (rows > 0) {
            return JsonResponse.success("更新成功！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新区域地图失败！");
        }
    }

    @Override
    public void permissionDownloadRegionMap(String fileId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        log.info("请求参数-->{}", fileId);
        if (null==fileId||"".equals(fileId)){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();//该行代码不能放在全局，否则执行到下载代码时会报异常！（getWriter() has already been called for this response）
            out.print("{\"result\": 0, \"data\":\"参数fileId为空！\"}");
            out.flush();
            return;
            //return JsonResponse.fail("参数fileId为空！");
        }
        ParkingFileUploadVo fileUploadVo = new ParkingFileUploadVo();
        fileUploadVo.setId(fileId);
        //fileUploadVo = fileUploadDao.getUploadFileByRegionId(fileUploadVo);
        fileUploadVo = fileUploadDao.queryById(fileUploadVo);
        if (null == fileUploadVo){
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"result\": 0, \"data\":\"fileId错误！\"}");
            out.flush();
            return;
        }

        File uploadDir = new File(new File("upload").getAbsolutePath());
        File file = new File(uploadDir.getPath() + File.separator + fileUploadVo.getFileName());
        if (!file.exists()) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{\"result\": 0, \"data\":\"地图文件不存在，请确认是否已上传！\"}");
            out.flush();
            return;
            //return JsonResponse.fail("地图文件不存在，请确认是否已上传！");
        }
        response.setHeader("Content-Disposition", "attachment;fileName="+URLEncoder.encode(fileUploadVo.getFileName().split("_")[1], "utf-8"));
        response.setContentType("image/jpeg");
        FileInputStream fileInputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] buff = new byte[1024];
        int len;
        while ((len = fileInputStream.read(buff))>0){
            outputStream.write(buff, 0, len);
        }
        outputStream.flush();
        outputStream.close();
        fileInputStream.close();
        //return JsonResponse.success("success");
    }
}
