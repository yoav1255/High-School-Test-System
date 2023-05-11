package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.Warning;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
		
	}
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String msgString = msg.toString();
		if (msgString.startsWith("#warning")) {
			Warning warning = new Warning("Warning from server!");
			try {
				client.sendToClient(warning);
				System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msgString.startsWith("#showAllStudents")) {
			try {
				List<Student> studentList = App.getAllStudents();
				//System.out.println(studentList.get(0).getEmail());
				//WORKS FINE TO THAT POINT
				// ----------//
				client.sendToClient(studentList);
				System.out.format("Sent Students to client %s\n", client.getInetAddress().getHostAddress());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
