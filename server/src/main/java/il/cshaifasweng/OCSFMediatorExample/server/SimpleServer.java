package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.StudentTest;
import il.cshaifasweng.OCSFMediatorExample.entities.Warning;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;
import java.util.List;

public class SimpleServer extends AbstractServer {

	public SimpleServer(int port) {
		super(port);
		
	}
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			String msgString = msg.toString();
			switch (msgString){
				case ("#warning"):
					Warning warning = new Warning("Warning from server!");
					client.sendToClient(warning);
					System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case ("#showAllStudents"):
					List<Student> studentList = App.getAllStudents();
					client.sendToClient(studentList);
					System.out.format("Sent Students to client %s\n", client.getInetAddress().getHostAddress());
					break;
				//case ("#createTest"):


					// continue here if message is a string ....
			}


			if (msg.getClass().equals(Student.class)) {
				List<StudentTest> studentTests =  App.getStudentTests((Student) msg);
				client.sendToClient(studentTests);
				System.out.format("Sent student tests to client %s\n", client.getInetAddress().getHostAddress());
			} else if (msg.getClass().equals(StudentTest.class)) {
				client.sendToClient((StudentTest) msg);
				System.out.format("Sent student test to client %s\n", client.getInetAddress().getHostAddress());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
