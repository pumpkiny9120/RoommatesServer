package com.oose2013.group7.roommates.server.games.contact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oose2013.group7.roommates.common.interfaces.EventListener;
import com.oose2013.group7.roommates.common.interfaces.Game;
import com.oose2013.group7.roommates.common.interfaces.GameEvent;
import com.oose2013.group7.roommates.server.database.User;
import com.oose2013.group7.roommates.server.event.BroadcastWordEvent;
import com.oose2013.group7.roommates.server.event.contact.ContactGameEvent;
import com.oose2013.group7.roommates.server.event.contact.ContactGameEventListener;
import com.oose2013.group7.roommates.server.event.contact.ContactHintEvent;
import com.oose2013.group7.roommates.server.event.contact.ContactQueryEvent;
import com.oose2013.group7.roommates.server.event.contact.MakeContactEvent;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventInterface;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventListener;

public class ContactGame implements ContactGameInterface,Game {

	String currentQuery;
	String queryWord;
	User currentDefender;
	String currentHint;
	String currentHintGiver;
	ArrayList<String> hintQueue;
	ArrayList<User> connectorQueue;
	List<EventListener<? super ContactGameEvent>> contactListeners;
	
	public String getCurrentHintGiver() {
		return currentHintGiver;
	}
	public void setCurrentHintGiver(String currentHintGiver) {
		this.currentHintGiver = currentHintGiver;
	}
	
	public String getCurrentQuery() {
		return currentQuery;
	}
	public void setCurrentQuery(String currentQuery) {
		this.currentQuery = currentQuery;
	}
	public User getCurrentDefender() {
		return currentDefender;
	}
	public void setCurrentDefender(User currentDefender) {
		this.currentDefender = currentDefender;
	}
	public String getCurrentHint() {
		return currentHint;
	}
	public void setCurrentHint(String currentHint) {
		this.currentHint = currentHint;
	}
	
	@Override
	public void setConnection(User u) {
		
		connectorQueue.add(u);
		MakeContactEvent e = new MakeContactEvent(u);
		notifyListeners(e);
		
	}
	@Override
	public void giveHint(String hint) {
		BroadcastWordEvent e = new ContactHintEvent(hint);
		notifyListeners(e);
		
	}
	@Override
	public void guessWord(String guess) {
		BroadcastWordEvent e = new ContactHintEvent(guess);
		notifyListeners(e);
		
	}
	@Override
	public void setNextLetter(char letter) {
		BroadcastWordEvent e = new ContactQueryEvent(this.currentQuery+letter);
		notifyListeners(e);
	}
	
	public void playContact(){
		//code to generate a word
		
	}
	@Override
	public void addListener(EventListener listener) {
		this.contactListeners.add(listener);
		
	}
	@Override
	public void removeListener(EventListener listener) {
		this.contactListeners.remove(listener);
		
	}
	@Override
	public void notifyListeners(GameEvent e) {
		for (EventListener<? super ContactGameEvent> el : contactListeners) {
			try {
				el.eventReceived((ContactGameEvent) e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
	
	
}
