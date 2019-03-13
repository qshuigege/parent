package com.fs.nothing.wx.testacc;

import com.fs.nothing.wx.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

/**
 * 核心服务类
 * @author 简爱微萌
 * @Email zyw205@gmial.com
 *Administrator
 */
public class WXTestAccHandler {

    public static Logger log = LoggerFactory.getLogger(WXTestAccHandler.class);
    public static String keyWord;


    public static final String MENU2="技术支持群:347245650 345531810 欢迎关注：Javen_Crazy";

    public static final String MENU="谢谢关注！！！";
    public static final String SERVER_EXCEPTION = "服务器异常！";
    /**
     * 处理微信发来的请求
     *
     * @return
     */
    public static String processRequest(Map<String, String> requestMap) {

        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");
        // 消息类型
        String msgType = requestMap.get("MsgType");
        // 回复文本消息
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        // 默认返回的文本消息内容
        String respContent = "您输入的内容无效！";


        // 文本消息
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

            String msgCont = requestMap.get("Content");

            log.info("您输入的内容-->" + msgCont);
            respContent = "你输入的是文本消息，内容为：\n" + msgCont;

        }



        // 图片消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            String picUrl = requestMap.get("PicUrl");
            respContent = FaceService.detect(picUrl);
        }



        // 地理位置消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
            respContent = "您发送的是地理位置消息！";
            String Location_X = requestMap.get("Location_X");
            String Location_Y = requestMap.get("Location_Y");
            log.info("Location_X:" + Location_X + " Location_Y:" + Location_Y);
        }



        // 链接消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
            respContent = "您发送的是链接消息！";
        }



        // 音频消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {

            String recognition = requestMap.get("Recognition");
            if (recognition == null || recognition.trim().equals("")) {
                respContent = "您发送的是音频消息！";
            } else {
                respContent = "语音识别结果:" + recognition;
            }

        }



        // 事件推送
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {

            // 事件类型
            String eventType = requestMap.get("Event");
            // 订阅(用户关注公众号)
            if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {

            }
            //用户已关注时的事件推送
            //扫带参数的二维码
            else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {


            }
            // 取消订阅
            else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

            }
            //点击带链接的菜单
            else if(eventType.equals("VIEW")){

            }
            // 点击按钮类型的菜单
            else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                //自定义菜单权没有开放，暂不处理该类消息
                if (requestMap.get("EventKey").equals("wuliugenzong")) {

                } else if (requestMap.get("EventKey").equals("test_click")) {
                    //回复图文消息
				/*NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				newsMessage.setArticleCount(1);
				List<Article> articles = new ArrayList<Article>();
				Article article = new Article();
				article.setDescription("富森十五周年征文投票开始啦！每人有 25 次点赞机会，每篇文章只能点赞 1 次，选出你心目中最优的25 篇作品吧！");
				article.setPicUrl("http://b2b.fusen.net.cn/wei/images/dianzanFengmian.jpg");
				article.setTitle("富森十五周年征文投票25强入围赛");
				//article.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf48eadf1ba6a4e64&redirect_uri=http%3A%2F%2Fb2b.fusen.net.cn%2Fwei%2Fcontroller%2FQuestionnaireController%2FgetPage.action&response_type=code&scope=snsapi_userinfo&state=1234567#wechat_redirect");
				article.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf48eadf1ba6a4e64&redirect_uri=http%3A%2F%2Fb2b.fusen.net.cn%2Fwei%2Fcontroller%2FDianzanController%2FgetPage.action&response_type=code&scope=snsapi_userinfo&state=1234567#wechat_redirect");
				articles.add(article);
				newsMessage.setArticles(articles);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);
				return respMessage;*/
                }
            }

        }

        textMessage.setContent(respContent);
        return MessageUtil.textMessageToXml(textMessage);

    }


}
