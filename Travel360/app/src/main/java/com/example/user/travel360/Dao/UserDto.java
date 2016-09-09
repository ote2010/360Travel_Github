package com.example.user.travel360.Dao;

public class UserDto {
	int seq;
	String id;
	int password;
	boolean permission_check;
	
	String profile_image;
	String email;
	String name;

	public UserDto(int seq, String id, int password, boolean permission_check)
	{
		this.seq = seq;
		this.id = id;
		this.password = password;
		this.permission_check = permission_check;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfile_image() {
		return profile_image;
	}
	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getPassword() {
		return password;
	}
	public void setPassword(int password) {
		this.password = password;
	}
	public boolean isPermission_check() {
		return permission_check;
	}
	public void setPermission_check(boolean permission_check) {
		this.permission_check = permission_check;
	}
	@Override
	public String toString() {
		return "UserDto [seq=" + seq + ", id=" + id + ", password=" + password + ", permission_check="
				+ permission_check + "]";
	}
	
	
}
