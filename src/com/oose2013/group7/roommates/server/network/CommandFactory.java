package com.oose2013.group7.roommates.server.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.oose2013.group7.roommates.common.commands.SignInCommand;
import com.oose2013.group7.roommates.common.interfaces.Command;

public class CommandFactory {
	private static final String CLASSNAME = "CLASSNAME"; 
	private static final String SIGNIN = "SignInCommand"; 
	
	public static Command getCommand(String json) throws JsonParseException{
		Gson gson = new Gson();
//		Command cmd = null;
//		
//		String mClass = gson.fromJson(CLASSNAME, String.class); 
//		if (mClass.equals(SIGNIN)) {
//			String username = gson.fromJson("username", String.class); 
//			String password = gson.fromJson("password", String.class);
//			cmd = new SignInCommand(username, password);
//		}
//		return cmd;
		//Command commandObject = gson.fromJson(json, Command.class);
		 JsonObject jsonObject =  json.getAsJsonObject(); 
         JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME); 
         String className = prim.getAsString(); 

         Command cmd = null;
         Class<?> mClass = null; 
         try { 	
         		System.out.println("!!"+className);
         		mClass = Class.forName(className); 
         		cmd = (Command) gson.fromJson(json, mClass);
         } catch (ClassNotFoundException e) { 
                 throw new JsonParseException(e.getMessage());
         } 
         //return context.deserialize(jsonObject.get(INSTANCE), mClass); 
         return cmd;
	}

}
