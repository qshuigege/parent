package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.dao.ParkingSettingsDao;
import com.drzk.parkingguide.po.ParkingSettingsPo;
import com.drzk.parkingguide.service.ParkingSettingsService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.JsonUtils;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.vo.ParkingSettingsVo;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ParkingSettingsServiceImpl implements ParkingSettingsService {

    private Logger log = LoggerFactory.getLogger(ParkingSettingsServiceImpl.class);

    @Autowired
    private ParkingSettingsDao dao;

    @Override
    public JsonResponse permissionAddParkingSettings(ParkingSettingsPo po, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"optionKey", "optionValue", "comment"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, po);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        int i = dao.insert(po);
        if (i > 0){
            return JsonResponse.success("成功新增"+i+"条数据！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "新增数据失败！");
        }
    }

    @Override
    public List<ParkingSettingsVo> queryParkingSettingsPage(ParkingSettingsVo vo) {
        log.debug("请求参数-->{}", JsonUtils.objectToJson(vo));
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
         return dao.queryAllWithPage(vo);
    }

    @Override
    public JsonResponse permissionDeleteById(List<ParkingSettingsPo> list, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(list));
        int rows = dao.deleteByIdBatch(list);
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse permissionUpdateById(ParkingSettingsPo po, HttpServletRequest request) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        int rows = dao.updateById(po);
        /*if (rows > 0){
            return JsonResponse.success("成功更新"+rows+"条数据！");
        }else {
            return JsonResponse.fail("更新失败！");
        }*/
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse permissionUpdateByOptionKey(ParkingSettingsVo vo, HttpServletRequest request) throws Exception{
        log.info("请求参数-->{}", JsonUtils.objectToJson(vo));
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(new String[]{"id", "optionKey", "optionValue"}, vo);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段值不能为空！");
        }
        int rows = dao.updateByOptionKey(vo);
        if (rows>0){
            return JsonResponse.success("更新成功！");
        }else {
            return JsonResponse.fail(JsonResponse.SYS_ERR, "更新失败！");
        }
    }

    @Override
    public JsonResponse queryById(ParkingSettingsPo po) {
        log.info("请求参数-->{}", JsonUtils.objectToJson(po));
        return JsonResponse.success(dao.queryById(po));
    }
}
