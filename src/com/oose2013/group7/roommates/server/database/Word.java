package com.oose2013.group7.roommates.server.database;

/****
 * Word class that maps to word table in the database. It is used in Describe to
 * populate Describe queries
 ***/
public class Word {

	private Integer wordId;

	public Word() {

	}

	public Integer getWordId() {
		return wordId;
	}

	public void setWordId(Integer wordId) {
		this.wordId = wordId;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	private String word;
}
