package edu.uco.sdd.spring15.dj_drmr;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = -6505998984941341983L;
	private String name;
	private String password;
	
	public User(String name, String password){
		this.name = name;
		this.password = password;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getPassword(){
		return this.password;
	}
}
