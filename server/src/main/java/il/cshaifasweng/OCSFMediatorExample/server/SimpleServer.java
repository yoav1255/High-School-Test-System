package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
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
			CustomMessage message = (CustomMessage) msg;
			String msgString = message.getMessage();
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
				case ("#getTeacher"):
					Teacher teacher = App.getTeacherFromId(message.getData().toString());
					client.sendToClient(new CustomMessage("returnTeacher",teacher));
					break;
				case ("#getStudentTests"):
					List<StudentTest> studentTests =  App.getStudentTests((Student) message.getData());
					client.sendToClient(studentTests);
					System.out.format("Sent student tests to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case ("#getStudentTest"):
					client.sendToClient((StudentTest) message.getData());
					System.out.format("Sent student test to client %s\n", client.getInetAddress().getHostAddress());
					break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
