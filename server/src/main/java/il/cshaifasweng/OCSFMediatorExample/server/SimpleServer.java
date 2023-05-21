package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
					client.sendToClient(new CustomMessage("returnWarning",warning));
					System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case ("#showAllStudents"):
					List<Student> studentList = App.getAllStudents();
					client.sendToClient(new CustomMessage("returnStudentList",studentList));
					System.out.format("Sent Students to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case ("#getStudentTests"):
					List<StudentTest> studentTests =  App.getStudentTests((Student) message.getData());
					client.sendToClient(new CustomMessage("returnStudentTests" ,studentTests));
					System.out.format("Sent student tests to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case ("#getStudentTest"):
					client.sendToClient(new CustomMessage("returnStudentTest",message.getData()));
					System.out.format("Sent student test to client %s\n", client.getInetAddress().getHostAddress());
					break;
				case("#updateGrade"):
					StudentTest studentTest = (StudentTest) message.getData();
					App.updateStudentGrade(studentTest);
					client.sendToClient(new CustomMessage("updateSuccess",""));
					break;
				case ("#getSubjects"):
					List<Subject> subjects = App.getSubjectsFromTeacherId(message.getData().toString());
					client.sendToClient(new CustomMessage("returnSubjects",subjects));
					break;
				case ("#getCourses"):
					List<Course> courses = App.getCoursesFromSubjectName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnCourses",courses));
					break;
				case ("#getQuestions"):
					List<Question> questions = App.getQuestionsFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnQuestions",questions));
					break;
				case ("#addQuestion"):
					System.out.println("server - addQuestion");
					Question question = (Question)message.getData();
					App.addQuestion(question);
					client.sendToClient(new CustomMessage("addQuestionSuccess",""));
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
