package com.service.user.dto;



public class UserDto {

    private long id;
    private String login;
    private String access_token;
    private String refresh_token;

    
    
	
	public UserDto(long id, String login, String access_token, String refresh_token) {
		super();
		this.id = id;
		this.login = login;
		this.access_token = access_token;
		this.refresh_token = refresh_token;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	
    
    
}
