package com.oose2013.group7.roommates.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/***
 * The Network Manager is a singleton class that handles all the sessions logged
 * on to the server
 * 
 * @author rujuta
 ***/
public class NetworkManager {

	private static NetworkManager networkManager = null;// Network Manager is
														// going to be a
														// singleton object
	private Map<NetworkHandler, Session> sessionMap; // List of Sessions this
														// manager is handling
	private ServerSocket serverSocket; // The main server socket listening to
										// requests
	Integer portNumber;
	String ipAddress;

	/***
	 * This method is called by all other methods needing to access the sessions
	 **/
	public static NetworkManager getNetworkManager() {
		if (networkManager == null) {
			networkManager = new NetworkManager();
		}
		return networkManager;

	}

	/*** Populates the port number using a network.properties file ***/
	public void setPortNumber() throws IOException {
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("network.properties");
		prop.load(in);
		String port = prop.getProperty("port");
		portNumber = Integer.parseInt(port);
		in.close();

	}

	public void setIPAddress() throws IOException {
		Properties prop = new Properties();
		InputStream in = getClass().getResourceAsStream("network.properties");
		prop.load(in);
		ipAddress = prop.getProperty("ipAddress");
		System.out.println(ipAddress);
		in.close();
	}

	/***
	 * Adds a session to the sessionMap which is a mapping between a network
	 * handler and its session This is needed, when a network handler receives
	 * data and needs to call its session's 'processData()' method
	 ***/
	public void addSession(Session session) {

		sessionMap.put(session.networkHandler, session);
	}

	/*** Retrieve a particular session object **/
	public Session getMySession(NetworkHandler networkHandler) {
		return sessionMap.get(networkHandler);
	}

	/***Starts the main server listening to requests**/
	public void startServer() throws IOException {

		boolean listening = true;
		setPortNumber();
		serverSocket = new ServerSocket(portNumber);
		sessionMap = new HashMap<NetworkHandler, Session>();
		System.out.println("server listening...");
		while (listening) {
			NetworkHandler networkHandler = new NetworkHandler(
					serverSocket.accept());
			Session session = new Session(networkHandler);

			this.addSession(session);

		}
	}

}
