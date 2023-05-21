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
					System.out.println("updated successfully!");
					break;
				case ("returnLogin"):
					String login_auth = (String) message.getData();
					EventBus.getDefault().post(new loginEvent(login_auth));
					break;
				case ("studentHome"),("teacherHome"),("managerHome"):
					System.out.println("in simple client "+ (String)message.getData());
					EventBus.getDefault().post(new UserHomeEvent((String) message.getData()));
					break;
				case ("returnIdToPage"):
					String id = message.getData().toString();
					System.out.println("in s.c return id to page " + id);
					EventBus.getDefault().post(new MoveIdToNextPageEvent(id));
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
				case ("returnCourse"):
					Course course =(Course) message.getData();
					EventBus.getDefault().post(new ShowCourseEvent(course));
					break;
				case ("addedExamForm"):
					ExamForm examForm = (ExamForm) message.getData();
					System.out.println(examForm.getTimeLimit());
					EventBus.getDefault().post(new ShowSuccessEvent("Successfully added "+examForm.getExamFormCode()));
					break;
				case ("returnListCodes"):
					List <String> examFormCode=(List<String>) message.getData();
					EventBus.getDefault().post(new ExamFormEvent(examFormCode));
					break;
				case("addScheduleTestSuccess"):
					System.out.println("added new schedule test successfuly!");
					break;
				case ("returnExamForm"):
					ExamForm examForm1=(ExamForm) message.getData();
					EventBus.getDefault().post(new ScheduledTestEvent(examForm1));
					break;
				case ("returnScheduledTestList"):
					List<ScheduledTest> scheduledTests = (List<ScheduledTest>) message.getData();
					EventBus.getDefault().post(new ShowScheduleTestEvent(scheduledTests));
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
