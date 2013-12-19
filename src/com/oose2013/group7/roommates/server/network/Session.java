package com.oose2013.group7.roommates.server.network;

import com.oose2013.group7.roommates.common.commands.Context;
import com.oose2013.group7.roommates.common.enums.MessageUtils;
import com.oose2013.group7.roommates.common.interfaces.Command;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameProxy;
import com.oose2013.group7.roommates.common.interfaces.EventListener;
import com.oose2013.group7.roommates.common.interfaces.Game;
import com.oose2013.group7.roommates.common.interfaces.GameEvent;
import com.oose2013.group7.roommates.common.interfaces.UserEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.google.gson.Gson;
import com.oose2013.group7.roommates.server.DatabaseObject;
import com.oose2013.group7.roommates.server.database.User;
import com.oose2013.group7.roommates.server.event.BroadcastWordEvent;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEvent;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventInterface;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventListener;
import com.oose2013.group7.roommates.server.games.describe.DescribeGame;
import com.oose2013.group7.roommates.server.games.describe.DescribeGameDataModel;

/***
 * The Session class that will be unique to each user logged on to the system
 * 
 * @param userObject
 *            : The User object to whom this session belongs
 * @param sessionGame
 *            : The game that the user is currently playing
 * @param gameListener
 *            : An instance of the inner class GameListener that is a listener
 *            to the sessionGame and does event handling for events generated in
 *            the game
 * @param networkHandler
 *            : The network handler instance using which session communicates
 *            with its clients
 **/
public class Session implements Context {

	User userObj;
	Game<?> sessionGame;
	EventListener<GameEvent> gameListener;
	NetworkHandler networkHandler;
	private DatabaseObject dbHandler;

	private static Logger logger = LogManager.getLogger(Session.class);

	public Session(NetworkHandler networkHandler) {
		this.networkHandler = networkHandler;
		userObj = null;
		sessionGame = null;
		dbHandler = new DatabaseObject();
	}

	public User getUserObj() {
		return userObj;
	}

	public void setUserObj(User userObj) {
		this.userObj = userObj;
	}

	/*** Sets the session game user is playing **/
	public void setSessionGame(Game<?> sessionGame) {
		if (this.gameListener != null && this.sessionGame != null) {
			this.sessionGame.removeListener(this.gameListener);
		}
		this.sessionGame = sessionGame;
		this.gameListener = new GenericSessionGameEventListener();
		this.sessionGame.addListener(this.gameListener);
	}

	/*** Executes the command object sent by client **/
	public void processReceivedObject(Command commandObject) {
		commandObject.execute(this);
	}

	/***
	 * An inner class that listens to the session game and broadcasts the game's
	 * events to sessions
	 **/
	private class GenericSessionGameEventListener implements
			EventListener<GameEvent> {
		@Override
		public void eventReceived(GameEvent event) throws IOException {
//			if (event instanceof DescribeGameEvent) {
//
//				DescribeGameDataModel dataModel = ((DescribeGameEvent) event)
//						.getChangedModel();
//				Session.this.networkHandler.addToSendingQueue(dataModel);
//			}
			networkHandler.addToSendingQueue(event);
		}

	}

	@Override
	public Game<?> getGame() {
		return this.sessionGame;
	}

	@Override
	public void signIn(String username, String password) {
		// TODO Auto-generated method stub
		userObj = dbHandler.login(username, password);
		UserEvent e;
		if (userObj == null) {
			e = new UserEvent(MessageUtils.SIGNIN_FAIL);
			networkHandler.addToSendingQueue(e);
		}
		else {
			e = new UserEvent(MessageUtils.SIGNIN_SUCCESS);
			networkHandler.addToSendingQueue(e);
		}
	}
	
	@Override
	public void signUp(String username, String password, String email, String gender) {
		// TODO Auto-generated method stub
		userObj = dbHandler.register(username, password, email, gender);
		UserEvent e;
		if (userObj == null) {
			e = new UserEvent(MessageUtils.SIGNUP_FAIL);
			networkHandler.addToSendingQueue(e);
		}
		else {
			e = new UserEvent(MessageUtils.SIGNUP_SUCCESS);
			networkHandler.addToSendingQueue(e);
		}
	}

}
