package com.oose2013.group7.roommates.server.games.describe;

import java.util.Map;

import com.oose2013.group7.roommates.common.enums.DescribeState;
import com.oose2013.group7.roommates.common.interfaces.DescribeGameAbstract;

/***
 * A data model for the Describe Game which consists of the data fields and
 * their getter and setter methods
 **/
public class DescribeGameDataModel extends DescribeGameAbstract {

	public DescribeState getState() {
		return state;
	}

	public void setState(DescribeState state) {
		this.state = state;
	}

	public Integer getCurrentRound() {
		return currentRound;
	}

	public void setCurrentRound(Integer currentRound) {
		this.currentRound = currentRound;
	}

	public String getCurrentWord() {
		return currentWord;
	}

	public void setCurrentWord(String currentWord) {
		this.currentWord = currentWord;
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

	DescribeState state;
	private Integer currentRound;
	private String currentWord;
	private Map<String, Map<String, String>> userWordDescription;
	private Map<String, Map<String, Map<String, Integer>>> userScoreboard;

}
