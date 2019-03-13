package com.fs.nothing.wx.apimodel;

public class AccessTokenGZPT_WY {

	private String access_token;
	private Integer expires_in;
	private String refresh_token;
	private String openid;
	private String scope;
	
	public AccessTokenGZPT_WY(){}
	
	public AccessTokenGZPT_WY(String access_token, Integer expires_in,
			String refresh_token, String openid, String scope) {
		this.access_token = access_token;
		this.expires_in = expires_in;
		this.refresh_token = refresh_token;
		this.openid = openid;
		this.scope = scope;
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
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	@Override
	public String toString() {
		return "AccessTokenGZPT_WY [access_token=" + access_token
				+ ", expires_in=" + expires_in + ", refresh_token="
				+ refresh_token + ", openid=" + openid + ", scope=" + scope
				+ "]";
	}
	
	
}
