package com.drzk.parkingguide.camera;

import com.drzk.parkingguide.dao.ParkingDeviceDao;
import com.drzk.parkingguide.util.SpringApplicationContextUtil;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class fDisConnectImpl implements NetSDKLib.fDisConnect{

    private Logger log = LoggerFactory.getLogger(fDisConnectImpl.class);

    @Override
    public void invoke(NetSDKLib.LLong lLoginID, String pchDVRIP, int nDVRPort, Pointer dwUser) {
        log.info("与相机断开连接，lLoginED:{}, pchDVRIP:{}, nDVRPort:{}, dwUser:{}", lLoginID, pchDVRIP, nDVRPort, dwUser);
        ParkingDeviceDao deviceDao = SpringApplicationContextUtil.getApplicationContext().getBean(ParkingDeviceDao.class);
        ParkingDeviceVo deviceVo = new ParkingDeviceVo();
        deviceVo.setDeviceIp(pchDVRIP);
        deviceVo.setStatus("0");
        int rows = deviceDao.updateCameraConnStatusByIp(deviceVo);
        if (rows < 1){
            log.error("与相机断开连接后更新相机状态失败！");
        }
    }
}
