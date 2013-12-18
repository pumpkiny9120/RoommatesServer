package com.oose2013.group7.roommates.server.games.describe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.oose2013.group7.roommates.common.enums.DescribeState;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameAbstract;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameProxy;
import com.oose2013.group7.roommates.common.interfaces.EventListener;
import com.oose2013.group7.roommates.common.interfaces.Game;
import com.oose2013.group7.roommates.common.interfaces.GameEvent;
import com.oose2013.group7.roommates.server.DatabaseObject;
import com.oose2013.group7.roommates.server.database.User;
import com.oose2013.group7.roommates.server.database.Word;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEvent;
import com.oose2013.group7.roommates.server.event.describe.DescribeGameEventInterface;

/**
 * @author rujuta This class implements the common functions in the Describe
 *         Game interface as well as server side computation of the Describe
 *         Game
 **/
public class DescribeGame implements Game, DescribeGameProxy {

	List<Word> wordList;
	Map<String, Map<String, String>> userWordDescription;
	Map<String, Map<String, Map<String, Integer>>> userScoreboard;
	String currentQuery;
	Integer currentRound;
	DescribeGameDataModel gameModel;
	List<EventListener<? super DescribeGameEventInterface>> describeListeners;
	private static final Logger logger = LogManager
			.getLogger(DescribeGame.class);

	/**
	 * A constructor that creates the following data structures :
	 * 
	 * 
	 * 1) wordList["apple","pear","banana"] etc which is the set of words
	 * players describe. These are the words the players will describe
	 * 
	 * 2) userWordDescription is a map with structure: {word1: {UserName:
	 * <Description>, UserName:<Description>..} word2: {User:<Description>,
	 * UserName:<Description> .. } } This structure stores the description each
	 * player gave for a word
	 * 
	 * 3) userScoreboard is a map with structure: {word1: {UserName: {User1:
	 * <Count>, User2:<Count>}} word2: {UserName: {User1: <Count>,
	 * User2:<Count>}} } This structure stores the count of similar words used
	 * by two players to describe the same word.
	 **/
	public DescribeGame() {

		DatabaseObject dObj = new DatabaseObject(); // this needs to be changed
													// and made into a singleton
													// object
		wordList = dObj.populateWords();
		currentRound = 1;
		userWordDescription = new HashMap<String, Map<String, String>>();

		// creates a map with an empty Hashmap indexed by the word
		for (Word word : wordList) {
			userWordDescription.put((String) word.getWord(),
					new HashMap<String, String>());
		}
		userScoreboard = new HashMap<String, Map<String, Map<String, Integer>>>();
		currentQuery = null; // the current Query that is in progress
		describeListeners = new ArrayList<EventListener<? super DescribeGameEventInterface>>(); // the
		// list
		// of
		// listeners
	}

	/** Sets word that needs to be described */
	public void setCurrentWord(String word) {
		currentQuery = word;
	}

	/** Returns the word currently being described */
	public String getCurrentWord() {
		return currentQuery;
	}

	public Integer getCurrentRound() {
		return this.currentRound;
	}

	public void setCurrentRound(Integer round) {
		this.currentRound = round;
	}

	public Map<String, Map<String, String>> getUserWordDescription() {
		return userWordDescription;
	}

	public void setUserWordDescription(
			Map<String, Map<String, String>> userWordDescription) {
		this.userWordDescription = userWordDescription;
	}

	public Map<String, Map<String, Map<String, Integer>>> getUserScoreboard() {
		return userScoreboard;
	}

	public void setUserScoreboard(
			Map<String, Map<String, Map<String, Integer>>> userScoreboard) {
		this.userScoreboard = userScoreboard;
	}

	/**
	 * Calculates a score between a pair of users to see how many used same
	 * words to describe the current word
	 **/
	public void populateScores() {
		// first take text for each query

		userScoreboard.put(currentQuery,
				new HashMap<String, Map<String, Integer>>());
		Map<String, String> map = userWordDescription.get(currentQuery);

		// word descriptions for query obtained, now find common words.

		for (String userName : map.keySet()) {

			HashMap userMapping = new HashMap<String, Integer>();
			String[] textDescription1 = map.get(userName).split("\\s+");
			List<String> describeList = Arrays.asList(textDescription1);

			for (String u1 : map.keySet()) {
				if (userName != u1) { // for every pair of users with user1
					// find common words
					String[] textDescription2 = map.get(u1).split("\\s+");
					int cnt = 0;
					for (String word : textDescription2) {
						if (describeList.contains(word)) {
							cnt++; // counts number of matches between the
									// two descriptions
						}
					}
					userMapping.put(u1, cnt);
				}

			}
			userScoreboard.get(currentQuery).put(userName, userMapping);
		}

	}

	/**
	 * This function notifies the listeners that the Broadcast word event has
	 * occurred.
	 **/
	public void playDescribe() {
		Word currentWord = wordList.remove(0);
		this.setCurrentWord(currentWord.getWord());

		GameEvent describeGameEvent = new DescribeGameEvent(DescribeState.PLAY,
				this.getCurrentRound(), this.getCurrentWord(),
				this.getUserWordDescription(), this.getUserScoreboard());

		notifyListeners(describeGameEvent);
	}

	/**
	 * This function notifies the listeners that the Publish score event has
	 * occurred.
	 **/
	public void publishScores(String query) {

		populateScores();
		GameEvent describeGameEvent = new DescribeGameEvent(
				DescribeState.SHOWSCORE, this.getCurrentRound(),
				this.getCurrentWord(), this.getUserWordDescription(),
				this.getUserScoreboard());

		notifyListeners(describeGameEvent);

	}

	@Override
	public void notifyListeners(GameEvent e) {
		for (EventListener<? super DescribeGameEventInterface> el : describeListeners) {
			try {
				el.eventReceived((DescribeGameEventInterface) e);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	@Override
	public void addListener(EventListener listener) {
		this.describeListeners.add(listener);

	}

	@Override
	public void removeListener(EventListener listener) {
		this.describeListeners.remove(listener);

	}

	/***
	 * This is a common function between client and server. It maps a user's
	 * description to the current query
	 ***/
	@Override
	public void setDescription(String text, String userName) {

		((HashMap) userWordDescription.get(currentQuery)).put(userName, text);
	}

}
