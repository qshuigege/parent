package com.example.demo.test.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangbin
 * @Title: ${file_name}
 * @Package ${package_name}
 * @Description: UDP数据包格式组装
 * @date 2019/4/18 9:46
 */
public class UDPPacket {

    private Logger log=LoggerFactory.getLogger(UDPPacket.class);            //日志包

    private byte[] head = new byte[2];                      //包头标识
    public byte[] addCode = new byte[2];                    //机号
    public byte deviceType = 43;                              //设备类型
    public byte cmdNumber = 0;                                //命令码
    public byte[] statusCode = new byte[2];                   // 状态码
    public byte[] data;                                        //数据包
    public byte[] lenByte = new byte[2];                    //数据包长度
    public byte packgeYZ = 0;                               //校验以上字节的总和
    public byte[] packgeData;                               //返回的组装数据包

    private int OtherLen = 11;                                //其他字节长度
    public int dataLen = 0;                                 //数据长度

    public UDPPacket(){
        head[0] = 0x44;
        head[1] = 0x52;
    }

    /**
     * 构建发送的数据包
     * @param cmd 发送的命令
     * @param data 数据包
     * @return
     */
    public byte[] getSendData(byte cmd, byte[] data){
        if (data == null) {
            data = new byte[0];
        }
        this.cmdNumber = cmd;
        this.data = data;
        lenByte[0] = (byte)(data.length / 256);
        lenByte[1] = (byte)data.length;

        packgeData = new byte[data.length + OtherLen];              //报文的配置
        //包头
        packgeData[0] = head[0];
        packgeData[1] = head[1];
        //机号
        packgeData[2] = addCode[0];
        packgeData[3] = addCode[1];
        //设备类型
        packgeData[4] = this.deviceType;
        //命令码
        packgeData[5] = cmdNumber;
        //状态码
        packgeData[6] = statusCode[0];
        packgeData[7] = statusCode[1];
        //数据包长度
        packgeData[8] = lenByte[0];
        packgeData[9] = lenByte[1];
        //数据包
        System.arraycopy(data, 0, packgeData, 10, data.length);
        //数据字节长度
        byte tmp = sum(packgeData);
        packgeData[packgeData.length - 1] = tmp;
        return packgeData;
    }

    /**
     * 读取数据报文返回
     * @param data
     * @return
     */
    public boolean getPackage(byte[] data) {
        //长度不对
        if (data == null || data.length < OtherLen){
            return false;
        }
        //长度
        lenByte[0] = data[8];
        lenByte[1] = data[9];
        dataLen = lenByte[1];
        dataLen += lenByte[0] * 256;
        //包头不对
        if (data[0] != head[0] || data[1] != head[1]){
            log.debug("读取的报文包头不对");
            return false;
        }

        //校验不对
        byte sum = sum(data, 11 + dataLen);
        byte sumbit=data[10 + dataLen];
        if (sum != sumbit) {
            return false;
        }

        //地址码
        addCode[0] = data[2];
        addCode[1] = data[3];
        //设备类型
        this.deviceType = data[4];
        //命令
        cmdNumber = data[5];
        //状态码
        statusCode[0] = data[6];
        statusCode[1] = data[7];

        //包解析
        this.data = new byte[dataLen];
        if(data.length > 0) {
            System.arraycopy(data, 10, data, 0, dataLen);
        }
        return true;
    }

    /**
     * 发送时，校验数据报文的长度
     * @param sourceData
     * @return
     */
    public byte sum(byte[] sourceData) {
        packgeYZ = 0;
        //所有数据之和不包含最后一个字节
        for (int i = 0; i < sourceData.length - 1; i++) {
            packgeYZ += sourceData[i];
        }
        return packgeYZ;
    }

    /**
     * 接收时，校验数据报文长度
     * @param sourceData
     * @param len
     * @return
     */
    public byte sum(byte[] sourceData,int len){
        packgeYZ = 0;
        for (int i = 0; i < len - 1; i++) {
            packgeYZ += sourceData[i];
        }
        return packgeYZ;
    }
}