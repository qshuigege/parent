package com.fs.nothing.wx.apimodel;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;

public class WXUserInfo {
	private String openid;
	private String unionid;
	private String nickname;
	private char sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;
	private Date createdate;
	private String appid;
	private char subscribe;
	private String language;
	private BigDecimal subscribe_time;
	private String remark;
	private String groupid;
	private int[] tagid_list;
	private String subscribe_scene;
	private String qr_scene;
	private String qr_scene_str;
	//private String privilege;
	public WXUserInfo(){}
	public WXUserInfo(String openid, String unionid, String nickname, char sex,
			String province, String city, String country, String headimgurl,
			Date createdate, String appid, char subscribe, String language,
			BigDecimal subscribe_time, String remark, String groupid,
			int[] tagid_list, String subscribe_scene, String qr_scene, String qr_scene_str) {
		super();
		this.openid = openid;
		this.unionid = unionid;
		this.nickname = nickname;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.country = country;
		this.headimgurl = headimgurl;
		this.createdate = createdate;
		this.appid = appid;
		this.subscribe = subscribe;
		this.language = language;
		this.subscribe_time = subscribe_time;
		this.remark = remark;
		this.groupid = groupid;
		this.tagid_list = tagid_list;
		this.subscribe_scene = subscribe_scene;
		this.qr_scene = qr_scene;
		this.qr_scene_str = qr_scene_str;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public char getSex() {
		return sex;
	}
	public void setSex(char sex) {
		this.sex = sex;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHeadimgurl() {
		return headimgurl;
	}
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public char getSubscribe() {
		return subscribe;
	}
	public void setSubscribe(char subscribe) {
		this.subscribe = subscribe;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public BigDecimal getSubscribe_time() {
		return subscribe_time;
	}
	public void setSubscribe_time(BigDecimal subscribe_time) {
		this.subscribe_time = subscribe_time;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public int[] getTagid_list() {
		return tagid_list;
	}
	public void setTagid_list(int[] tagid_list) {
		this.tagid_list = tagid_list;
	}

	public String getSubscribe_scene() {
		return subscribe_scene;
	}

	public void setSubscribe_scene(String subscribe_scene) {
		this.subscribe_scene = subscribe_scene;
	}

	public String getQr_scene() {
		return qr_scene;
	}

	public void setQr_scene(String qr_scene) {
		this.qr_scene = qr_scene;
	}

	public String getQr_scene_str() {
		return qr_scene_str;
	}

	public void setQr_scene_str(String qr_scene_str) {
		this.qr_scene_str = qr_scene_str;
	}

	@Override
	public String toString() {
		return "WXUserInfo{" +
				"openid='" + openid + '\'' +
				", unionid='" + unionid + '\'' +
				", nickname='" + nickname + '\'' +
				", sex=" + sex +
				", province='" + province + '\'' +
				", city='" + city + '\'' +
				", country='" + country + '\'' +
				", headimgurl='" + headimgurl + '\'' +
				", createdate=" + createdate +
				", appid='" + appid + '\'' +
				", subscribe=" + subscribe +
				", language='" + language + '\'' +
				", subscribe_time=" + subscribe_time +
				", remark='" + remark + '\'' +
				", groupid='" + groupid + '\'' +
				", tagid_list=" + Arrays.toString(tagid_list) +
				", subscribe_scene='" + subscribe_scene + '\'' +
				", qr_scene='" + qr_scene + '\'' +
				", qr_scene_str='" + qr_scene_str + '\'' +
				'}';
	}
}
