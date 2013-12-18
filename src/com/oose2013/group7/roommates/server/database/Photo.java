package com.oose2013.group7.roommates.server.database;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;

/**The Photo class used to connect to Photo Table in database that stores 
 * user photos**/
public class Photo {

	private Integer photoId;
	private String photoPath;
	private Integer userId;
	private Date uploadedAt;
	public Photo(){
		
	}
	
	public Integer getPhotoId() {
		return photoId;
	}
	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Date getUploadedAt() {
		return uploadedAt;
	}
	public void setUploadedAt(Date uploadedAt) {
		this.uploadedAt = uploadedAt;
	}
}
