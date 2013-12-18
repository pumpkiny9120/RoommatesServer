package com.oose2013.group7.roommates.server.network;

import java.io.IOException;

/***
 * A starting point for the application that starts the Network Manager and
 * subsequently the server to listen for user requests
 **/
public class Application {

	public static void main(String[] args) throws IOException {
		System.out.println("server starting...");
		NetworkManager.getNetworkManager().startServer();
		System.out.println("server stopped...");

	}

}
