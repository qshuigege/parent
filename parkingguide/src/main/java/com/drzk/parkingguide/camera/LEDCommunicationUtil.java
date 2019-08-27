package com.drzk.parkingguide.camera;

import com.drzk.parkingguide.util.BinaryStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class LEDCommunicationUtil {

    private static Logger log = LoggerFactory.getLogger(LEDCommunicationUtil.class);

    private static DatagramSocket udpSender;

    public static int ledConnTimeout;

    static {
        try {
            udpSender = new DatagramSocket();
        } catch (SocketException e) {
            log.error("初始化UDP发送器错误！-->{}", e);
        }
    }

    private static final byte[] part = new byte[]{
            0x44,0x52,//标识，2字节，“DR"=0x4452
            0x00,0x00,//机号，2字节，00 00
            0x2b,//设备类型，1字节，2B-车位引导屏控制板
            0x03,//命令码，1字节，03-车位数更新
            0x00,0x00,//状态码，2字节，0表示成功，非0值表示错误
            0x00,0x00//数据长度，2字节
    };


    /***
     *发送UDP数据
     * @param dstHost 目标主机IP地址
     * @param dstPort 目标主机端口号
     * @param data 发送的数据
     * @throws IOException
     */
    public static void sendUDPData(String dstHost, int dstPort, byte[] data) throws IOException {
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(dstHost), dstPort);
        udpSender.send(packet);
    }

    private static byte[] getSocketInputStreamData(InputStream in) throws IOException {
        List<Object> content = new ArrayList<>();
        byte[] buff = new byte[1024];
        int totalReadBytes = 0;
        int curReadBytes;
        while (true) {
            try {
                curReadBytes = in.read(buff);
                log.debug("以每次1024字节大小读取返回的数据，直到读取完毕。本次读取字节数-->{}", curReadBytes);
                if (curReadBytes == -1) {
                    if (content.size() == 0){
                        log.debug("没有读到任何数据");
                        return new byte[0];
                    }
                    break;
                }
            }catch (SocketTimeoutException e){
                log.warn("读取输入流超时！");
                if (content.size() == 0){
                    log.debug("超时且没有读到任何数据");
                    return new byte[0];
                }
                break;
            }

            totalReadBytes += curReadBytes;
            byte[] temp = new byte[curReadBytes];
            System.arraycopy(buff, 0, temp, 0, curReadBytes);
            content.add(temp);
        }
        log.debug("返回的数据总字节长度-->{}", totalReadBytes);

        byte[] retVal = new byte[totalReadBytes];

        int pos = 0;
        for (Object aContent : content) {
            byte[] t = (byte[]) aContent;
            System.arraycopy(t, 0, retVal, pos, t.length);
            pos += t.length;
        }
        return retVal;
    }

    public static byte[] sendTCPData(String dstHost, int dstPort, byte[] data) throws IOException {
        Socket socket = new Socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(dstHost, dstPort);
        socket.connect(inetSocketAddress, ledConnTimeout);//设置创建连接超时时间为1.5秒
        socket.setSoTimeout(10);//设置输入流读取超时时间为10毫秒
        OutputStream out = socket.getOutputStream();
        out.write(data);
        out.flush();
        //socket.shutdownOutput();
        log.debug(BinaryStringUtils.byte2hexAndPretty(data).toUpperCase());
        InputStream in = socket.getInputStream();
        byte[] buff = getSocketInputStreamData(in);
        //byte[] buff = new byte[0];
        socket.shutdownInput();

        out.close();
        in.close();
        socket.close();
        return buff;
    }

    /**
     * 设置LED显示的车位数据
     * @param ledRowNum LED行号
     * @param parkingPlaceNum 车位数
     * @param dstHost LED ip地址
     * @param dstPort LED 端口号
     * @throws IOException
     */
    public static byte[] setLedParkingPlaceNum(int ledRowNum, int parkingPlaceNum, String dstHost, int dstPort) throws IOException {
        byte[] senddata = new byte[14];
        System.arraycopy(part, 0, senddata, 0, part.length);
        senddata[4] = 0x2b;
        senddata[5] = 0x03;
        senddata[8] = 0x00;//数据内容有3个字节
        senddata[9] = 0x03;//数据内容有3个字节
        senddata[10] = (byte)ledRowNum;
        senddata[11] = (byte)(parkingPlaceNum>>8);
        senddata[12] = (byte)parkingPlaceNum;
        senddata[13] = verify(senddata);
        //senddata = new byte[]{0x44,0x52,0x00,0x00,0x2b,0x03,0x00,0x00,0x00,0x03,0x01,0x00,0x17,(byte)0xdf};
        //sendUDPData(dstHost, dstPort, senddata);
        return sendTCPData(dstHost, dstPort, senddata);
    }

    /**
     * 设置LED显示的车位数据
     * @param color 内容显示颜色，0红色，1绿色，2黄色
     * @param ledRowNum LED行号
     * @param parkingPlaceNum 车位数
     * @param dstHost LED ip地址
     * @param dstPort LED 端口号
     * @throws IOException
     */
    public static byte[] setLedParkingPlaceNum17Bytes(int color, int ledRowNum, int parkingPlaceNum, String dstHost, int dstPort) throws Exception {
        byte[] senddata = new byte[17];
        System.arraycopy(part, 0, senddata, 0, part.length);
        senddata[4] = 0x2b;
        senddata[5] = 0x0d;
        senddata[8] = 0x00;//数据内容有6个字节
        senddata[9] = 0x06;//数据内容有6个字节
        String byteBitPos_1_2;//内容显示颜色，00红色，01绿色，10黄色
        switch (color){
            case 0:
                byteBitPos_1_2 = "00";
                break;
            case 1:
                byteBitPos_1_2 = "01";
                break;
            case 2:
                byteBitPos_1_2 = "10";
                break;
            default: byteBitPos_1_2 = "00";
        }
        String byteBitPos_3 = "0";//1-全显（亮），测试
        String byteBitPos_4 = "0";//预留，0
        String byteBitPos_5_6_7_8;//楼层屏行号
        switch (ledRowNum){
            case 1:
                byteBitPos_5_6_7_8 = "0000";
                break;
            case 2:
                byteBitPos_5_6_7_8 = "0001";
                break;
            case 3:
                byteBitPos_5_6_7_8 = "0010";
                break;
            case 4:
                byteBitPos_5_6_7_8 = "0011";
                break;
            default:
                throw new Exception("楼层屏行号只能是1-4，实际传入值："+ledRowNum);
        }
        String byteStr = byteBitPos_1_2+byteBitPos_3+byteBitPos_4+byteBitPos_5_6_7_8;
        senddata[10] = (byte)Short.parseShort(byteStr, 2);
        senddata[11] = 0x00;//0-一直显示；1~255-表示显示秒数
        String parkingPlaceNumStr = parkingPlaceNum+"";
        switch (parkingPlaceNumStr.length()){
            case 0:
                parkingPlaceNumStr = "0000";
                break;
            case 1:
                parkingPlaceNumStr = "000" + parkingPlaceNumStr;
                break;
            case 2:
                parkingPlaceNumStr = "00" + parkingPlaceNumStr;
                break;
            case 3:
                parkingPlaceNumStr = "0" + parkingPlaceNumStr;
                break;
        }
        char[] chars = parkingPlaceNumStr.toCharArray();
        senddata[12] = (byte)chars[0];
        senddata[13] = (byte)chars[1];
        senddata[14] = (byte)chars[2];
        senddata[15] = (byte)chars[3];
        senddata[16] = verify(senddata);
        //senddata = new byte[]{0x44,0x52,0x00,0x00,0x2b,0x03,0x00,0x00,0x00,0x03,0x01,0x00,0x17,(byte)0xdf};
        //sendUDPData(dstHost, dstPort, senddata);
        return sendTCPData(dstHost, dstPort, senddata);
    }

    /**
     * 配置LED参数，包括箭头方向，车位预警数量，屏幕亮度
     * @param arrowDirection 设置箭头方向的字节
     * @param warningNum 车位预警数（0-255，0为不预警）
     * @param brightness 屏幕亮度（0-9,建议设置为4）
     * @param dstHost led ip地址
     * @param dstPort led 端口号
     * @throws IOException
     */
    public static void configLED(int arrowDirection, int warningNum, int brightness, String dstHost, int dstPort) throws IOException {
        byte[] senddata = new byte[15];
        System.arraycopy(part, 0, senddata, 0, part.length);
        senddata[4] = 0x00;
        senddata[5] = 0x05;
        senddata[9] = 0x04;
        senddata[10] = (byte)arrowDirection;

        if (warningNum<0){
            warningNum = 0;
        }else if (warningNum>255){
            warningNum = 0;
        }
        senddata[11] = (byte)warningNum;

        if (brightness<0){
            brightness = 4;
        }else if (brightness>9){
            brightness = 4;
        }
        senddata[12] = (byte)brightness;

        senddata[14] = verify(senddata);

        //sendUDPData(dstHost, dstPort, senddata);
        sendTCPData(dstHost, dstPort, senddata);
    }

    /***
     * 校验
     * @param senddata
     */
    private static byte verify(byte[] senddata){
        byte temp = 0;
        for (int i = 0; i < senddata.length-1; i++) {
            temp += senddata[i];
        }
        return temp;
    }

    /**
     * 四行LED屏显示的箭头信息由一个字节控制，组装出这个字节
     * @param row1Dir 第一行屏的显示由字节的第1,2位控制，以此类推。（00无方向，01向左，10向上，11向右）
     * @param row2Dir bit3-bit4
     * @param row3Dir bit5-bit6
     * @param row4Dir bit7-bit8
     * @return
     */
    public static int assembleLedArrowDirectionByte(String row1Dir, String row2Dir, String row3Dir, String row4Dir) {
        String direction = row1Dir + row2Dir + row3Dir + row4Dir;
        if (direction.length()>8){
            return 0b00000000;//如果不按规定设置方向，则将4行led都设置为无箭头
        }
        try {
            int i = Integer.parseInt(direction, 2);
            return i;
        }catch (Exception e){
            log.error("设置方向的byte内容不正确！({})-->{}", direction, e);
            return 0b00000000;//如果不按规定设置方向，则将4行led都设置为无箭头
        }
    }

    public static void main(String[] args) throws IOException {

        //LEDCommunicationUtil.configLED(LEDCommunicationUtil.assembleLedArrowDirectionByte("01", "11", "11", "11"), 0, 4, "192.168.50.232", 2000);

        //区域屏
        //byte[] bytes = LEDCommunicationUtil.setLedParkingPlaceNum(1, 123, "192.168.50.221", 2000);
        //System.out.println(Arrays.toString(bytes));
        //LEDCommunicationUtil.setLedParkingPlaceNum(2, 998, "192.168.50.232", 2000);
        /*byte[] bytes = LEDCommunicationUtil.setLedParkingPlaceNum(1, 128, "192.168.50.221", 2000);
        System.out.println(StringUtils.byte2hexAndPretty(bytes));*/
        long count = 1;
        try {
            ledConnTimeout = 15000;
            for (int i = 1; i < 1000; i++) {
                if (i == 999) {
                    i = 1;
                }
                byte[] bytes = LEDCommunicationUtil.setLedParkingPlaceNum(1, i, "192.168.50.221", 2000);
                log.debug(BinaryStringUtils.byte2hexAndPretty(bytes));
                byte[] bytes2 = LEDCommunicationUtil.setLedParkingPlaceNum(1, i, "192.168.50.220", 2000);
                log.debug(BinaryStringUtils.byte2hexAndPretty(bytes2));
                count++;
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            log.debug("第" + count + "次连接时程序异常！");
            e.printStackTrace();
        }

        //String hex = "445200002b030000000301007b43";
        //System.out.println(StringUtils.hexPretty(hex));

        //byte[] data = new byte[]{0x44, 0x52, 0x00, 0x00, 0x2b, 0x03, 0x00, 0x00, 0x00, 0x03, 0x01, 0x00, 0x7b, 0x43};
        //byte[] data = new byte[]{0x44, 0x52, 0x00, 0x00, 0x2b, 0x03, 0x00, 0x00, 0x00, 0x03, 0x01, 0x00, 0x7c, 0x44};
        //byte[] bytes = sendTCPData("192.168.50.221", 2000, data);
        //System.out.println(StringUtils.byte2hexAndPretty(bytes));
    }
}
