package com.oose2013.group7.roommates.server.games.contact;

import com.oose2013.group7.roommates.server.database.User;

public interface ContactGameInterface {

	/**A request to connect to the user whose hint is being broadcast*/
	public void setConnection(User u);
	/**Send a hint. This method will add the hint to the hint queue **/
	public void giveHint(String hint);
	
	/**This is sent to the server by both users who have established a connection.
	 * This method tallies if the connected users' guesses match**/
	public void guessWord(String guess);
	
	/**When two players who had established a connection, rightly guess the same word, this method is
	 * set by the defender to reveal the next letter */
	public void setNextLetter(char letter);
	
	/**This function generates the contact game*/
	public void playContact();
}
