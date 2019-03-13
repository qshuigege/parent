package com.fs.nothing.wx.apimodel;

/**
 * 公众平台公众号接口获取AccessToken
 *
 */
public class AccessTokenGZPT {
	private String access_token;
	private Integer expires_in;
	
	
	public AccessTokenGZPT() {
	}
	public AccessTokenGZPT(String access_token, Integer expires_in) {
		this.access_token = access_token;
		this.expires_in = expires_in;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	
	
}
