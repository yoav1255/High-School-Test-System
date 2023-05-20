package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Controllers.ShowUpdateStudentController;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;


import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		try {
			CustomMessage message = (CustomMessage) msg;
			String msgString = message.getMessage();
			switch (msgString) {
				case ("returnWarning"):
					EventBus.getDefault().post(new WarningEvent((Warning) message.getData()));
					break;
				case ("returnStudentList"):
					List<Student> listStudent = (List<Student>) message.getData();
					EventBus.getDefault().post(new ShowAllStudentsEvent(listStudent));
					break;
				case ("returnStudentTests"):
					List<StudentTest> studentTests = (List<StudentTest>) message.getData();
					EventBus.getDefault().post(new ShowOneStudentEvent(studentTests));
					break;
				case ("returnStudentTest"):
					StudentTest studentTest = (StudentTest) message.getData();
					EventBus.getDefault().post(new ShowUpdateStudentEvent(studentTest));
					break;
				case ("updateSuccess"):
					System.out.println("updated grade successfully!");
					break;
				case ("returnSubjects"):
					List<Subject> subjects = (List<Subject>) message.getData();
					EventBus.getDefault().post(new ShowTeacherSubjectsEvent(subjects));
					break;
				case ("returnCourses"):
					List<Course> courses = (List<Course>) message.getData();
					EventBus.getDefault().post(new ShowSubjectCoursesEvent(courses));
					break;
				case ("returnQuestions"):
					List<Question> questions = (List<Question>) message.getData();
					EventBus.getDefault().post(new ShowCourseQuestionsEvent(questions));
					break;
				case ("addQuestionSuccess"):
					System.out.println("Question added successfully!");
					EventBus.getDefault().post(new QuestionAddedEvent(""));
					break;

			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3028);
		}
		return client;
	}
}
