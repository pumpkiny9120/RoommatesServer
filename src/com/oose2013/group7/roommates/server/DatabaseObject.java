package com.oose2013.group7.roommates.server;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oose2013.group7.roommates.server.database.User;
import com.oose2013.group7.roommates.server.database.Word;

/** Class that communicates with the database **/
public class DatabaseObject {

	final static Logger logger = LoggerFactory.getLogger(DatabaseObject.class);

	/**
	 * Constructor that receives a message from the client and stores it to the
	 * database
	 **/
	public DatabaseObject() {

	}

	public static void main(String[] args) {

		DatabaseObject r = new DatabaseObject();

	}

	/** Creates a new user in the system */
	public User register(String username, String password, String email, String gender) {

		User u = new User();
		u.setUserName(username);
		u.setUserGender(gender);
		u.setUserPassword(password);
		u.setUserStatus("Waiting");
		u.setUserEmail(email);
		createUser(u);
		return u;

	}

	/** Retrieves data of user having this username and password **/
	public User login(String userName, String password) {
		User loggedInUser = null;
		loggedInUser = getUser(userName, password);
		if (loggedInUser != null) {
			System.out.print("Welcome " + loggedInUser.getUserName());
		} else {
			System.out
					.println("Sorry, we couldn't find you, you should register");
		}
		return loggedInUser;

	}

	public List populateWords() {
		List<Word> wordList =  new ArrayList<Word>();
		wordList = getWords();
		if (wordList != null)
			return wordList;
		return null;
	}

	/** Inserts user object to database */
	private static void createUser(User newUser) {
		Transaction tx = null;
		Session session = SessionFactoryUtility.getSessionFactory()
				.getCurrentSession();
		try {
			tx = session.beginTransaction();
			session.save(newUser);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.debug("Error rolling back transaction");
				}
				e.printStackTrace();
			}
		}
	}

	/** Retrieves user object based on userName and password **/
	private static User getUser(String userName, String password) {
		Transaction tx = null;
		Session session = SessionFactoryUtility.getSessionFactory()
				.getCurrentSession();
		try {
			tx = session.beginTransaction();
			String queryString = "from User where userName=:userName and userPassword=:password";
			Query query = session.createQuery(queryString);
			query.setString("userName", userName);
			query.setString("password", password);
			Object queryResult = query.uniqueResult();
			User user = (User) queryResult;
			session.getTransaction().commit();
			return user;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.debug("Error rolling back transaction");
				}
				e.printStackTrace();
			}
		}
		return null;
	}

	/** Retrieves 5 word objects **/
	private static List<Word> getWords() {
		Transaction tx = null;
		Session session = SessionFactoryUtility.getSessionFactory()
				.getCurrentSession();
		try {
			tx = session.beginTransaction();
			String queryString = "from Word";
			List<Word> wordList = session.createQuery(queryString).list();
			session.getTransaction().commit();
			return wordList;
		} catch (RuntimeException e) {
			if (tx != null && tx.isActive()) {
				try {
					tx.rollback();
				} catch (HibernateException e1) {
					logger.debug("Error rolling back transaction");
				}
				e.printStackTrace();
			}
		}
		return null;
	}

}
