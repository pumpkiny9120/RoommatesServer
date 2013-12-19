package com.oose2013.group7.roommates.server.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oose2013.group7.roommates.common.commands.InterfaceAdapter;
import com.oose2013.group7.roommates.common.interfaces.Command;

/***
 * The Network Handler handles communication with a client. There is one network
 * handler per session. It is the end point of a server.
 * 
 * @author rujuta
 **/
public class NetworkHandler {

	private Socket socket = null;
	BufferedReader reader;
	PrintWriter out;
	List<String> sendingQueue;

	/***
	 * A network handler constructor initializes its member variables:
	 * 
	 * @param socket
	 *            - The socket with which it communicates with the client
	 * @param sendingQueue
	 *            - The sendingThread waits on the sending queue and sends
	 *            objects to client as and when the queue is filled
	 * @param reader
	 *            - The buffered reader to read the input stream
	 * @param out
	 *            - The Printwriter to write to the output stream
	 * 
	 *            The Sending/Receiving functions are handled by two separate
	 *            threads that are inner classes of this class
	 **/
	public NetworkHandler(Socket socket) throws IOException {

		this.socket = socket;
		sendingQueue = new ArrayList<String>();
		System.out.println("Connection established. Client's socket:"
				+ socket.getPort());
		reader = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream());
		Thread readerThread = new Thread(new ReceivingThread());
		readerThread.start();
		Thread writerThread = new Thread(new SendingThread());
		writerThread.start();

	}

	/*** Writes data to socket **/
	public void sendObject(String toSend) throws IOException {

		out.print(toSend);

	}

	/*** Serializes an object to be sent **/
	public String createJson(Object objectToSend) {
		Gson gson = new Gson();
		String json = gson.toJson(objectToSend);
		return json;

	}

	/*** Deserializes an object **/
	public Command getObjectFromJson(String json) {
		//Gson gson = new Gson();
//		GsonBuilder builder = new GsonBuilder(); 
//        builder.registerTypeAdapter(Command.class, new InterfaceAdapter<Command>()); 
//        Gson gson = builder.create(); 
//		Command commandObject = gson.fromJson(json, Command.class);
//		return commandObject;
		return CommandFactory.getCommand(json);
	}

	/** Adds an object to the sending queue **/
	public void addToSendingQueue(Object sendObject) {
		String toSend = createJson(sendObject);
		this.sendingQueue.add(toSend);
	}

	/*
	 * public String getData(){ BufferedReader in; String data ="hhh"; try { in
	 * = new BufferedReader(new InputStreamReader( socket.getInputStream()));
	 * data=in.readLine();
	 * 
	 * if (data ==null){ System.out.print("Nothing to read"); } }
	 * catch(Exception e){ e.printStackTrace(); } return data; }
	 */

	public Socket getSocket() {
		return socket;
	}

	/***
	 * On receiving an object, this method calls the session's process object as
	 * all higher level processing is left to the session to handle
	 **/
	public void receiveObject(String jsonString) {

		Session mySession = NetworkManager.getNetworkManager().getMySession(
				this);
		Command commandObject = getObjectFromJson(jsonString);
		mySession.processReceivedObject(commandObject); // Why
														// does
														// it
														// give
														// me
														// an
														// error
														// without
														// the
														// fully
														// qualified
														// name
														// ???
// FIXME!!!!
	}

	public void closeSocket() throws IOException {
		socket.close();
	}

	/***
	 * The Sending thread an instance of which will wait on sending queue to be
	 * filled
	 **/
	class SendingThread implements Runnable {

		@Override
		public void run() {
			while (true) {
				if (!sendingQueue.isEmpty()) {
					try {
						NetworkHandler.this.sendObject(sendingQueue.get(0));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

	}

	/***
	 * Class Receiving Thread as instance of which will constantly wait for data
	 * from client
	 **/
	class ReceivingThread implements Runnable {

		String message;

		@Override
		public void run() {
			try {
				while ((message = (reader.readLine())) != null) {
					System.out.println("Client says: " + message);
					// TODO comment out this to test first
					NetworkHandler.this.receiveObject(message);

				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

	}
}
