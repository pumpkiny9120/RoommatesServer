package com.oose2013.group7.roommates.server.event.describe;

import java.util.Map;

import com.oose2013.group7.roommates.common.enums.DescribeState;
import com.oose2013.group7.roommates.server.games.describe.DescribeGameDataModel;

/***
 * A Describe Game Event updates the Describe Game Data Model and sends the
 * pushes the updated model to the client
 **/
public class DescribeGameEvent implements DescribeGameEventInterface {

	DescribeState state;
	private Integer currentRound;
	private String currentWord;
	private Map<String, Map<String, String>> userWordDescription;
	private Map<String, Map<String, Map<String, Integer>>> userScoreboard;

	DescribeGameDataModel describeGameDataModel;

	public DescribeGameEvent(DescribeState state, Integer currentRound,
			String currentWord, Map userDescriptions, Map userScores) {

		describeGameDataModel = new DescribeGameDataModel();
		describeGameDataModel.setCurrentRound(currentRound);
		describeGameDataModel.setCurrentWord(currentWord);
		describeGameDataModel.setState(state);
		describeGameDataModel.setUserScoreboard(userScores);
		describeGameDataModel.setUserWordDescription(userWordDescription);

	}

	public DescribeGameDataModel getChangedModel() {
		return this.describeGameDataModel;
	}

}
