package com.oose2013.group7.roommates.server.event;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.oose2013.group7.roommates.server.event.contact.ContactGameEvent;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventInterface;
import com.oose2013.group7.roommates.server.games.describe.DescribeGame;

/***
 * A Broadcast word event that will push a word to all the clients listening on
 * a game
 ***/
public class BroadcastWordEvent implements DescribeGameEventInterface,
		ContactGameEvent {

	String word;
	private static final Logger logger = LogManager
			.getLogger(BroadcastWordEvent.class);

	public BroadcastWordEvent(String newWord) {
		word = newWord;
	}

	public String getWord() {
		return word;
	}

}
