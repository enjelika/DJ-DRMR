package edu.uco.sdd.spring15.dj_drmr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Song {
	private File song;
	private List<String> tags;
	private String name;
	
	public Song(File song, String name){
		this.song = song;
		this.setName(name);
	}
	
	public Song (File song, List<String> tags){
		
	}
	
	public void setTags(String tags){
		String[] tagsArray = tags.split("#");
		this.tags = new ArrayList<String>();
		for (String string : tagsArray) {
			string.trim();
			this.tags.add(string);
		}
	}
	
	public List<String> getTags(){
		return this.tags;
	}
	
	public File getSong(){
		return this.song;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
