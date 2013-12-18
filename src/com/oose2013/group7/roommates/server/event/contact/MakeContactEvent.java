package com.oose2013.group7.roommates.server.event.contact;

import com.oose2013.group7.roommates.server.database.User;

public class MakeContactEvent implements ContactGameEvent {
	
	User connector;
	
	public MakeContactEvent(User connector){
		this.connector = connector;
	}

	public User getConnector() {
		return connector;
	}

	
 
	

}
