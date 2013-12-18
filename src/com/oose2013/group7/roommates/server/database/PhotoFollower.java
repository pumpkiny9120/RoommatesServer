package com.oose2013.group7.roommates.server.database;

import javax.persistence.Entity;
import javax.persistence.Id;

/***
 * A PhotoFollower class that will map to the PhotoFollower table in the
 * database - a relationship table between a user and the photos he/she is
 * following
 **/
public class PhotoFollower {

	private Integer photoFollowerId;
	private Integer photoId;
	private Integer userId;

	private PhotoFollower() {

	}

	public Integer getPhotoFollowerId() {
		return photoFollowerId;
	}

	public void setPhotoFollowerId(Integer photoFollowerId) {
		this.photoFollowerId = photoFollowerId;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
