package il.cshaifasweng.OCSFMediatorExample.client;

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
					break;
				case ("returnLogin"):
					String login_auth = (String) message.getData();
					EventBus.getDefault().post(new loginEvent(login_auth));
					break;
				case ("studentHome"),("teacherHome"),("managerHome"):
					EventBus.getDefault().post(new UserHomeEvent((String) message.getData()));
					break;
				case ("returnSubjects"):
					List<Subject> subjects = (List<Subject>) message.getData();
					EventBus.getDefault().post(new ShowTeacherSubjectsEvent(subjects));
					break;
				case ("returnAllSubjects"):
					List<Subject> subjects1 = (List<Subject>) message.getData();
					EventBus.getDefault().post(new ShowAllSubjectsEvent(subjects1));
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
					EventBus.getDefault().post(new ShowSuccessEvent("Successfully added "+examForm.getExamFormCode()));
					break;
				case ("returnListCodes"):
					List <String> examFormCode=(List<String>) message.getData();
					EventBus.getDefault().post(new ExamFormEvent(examFormCode));
					break;
				case ("returnTeacher"):
					Teacher teacher=(Teacher) message.getData();
					EventBus.getDefault().post(new TeacherFromIdEvent(teacher));
					break;
				case("addScheduleTestSuccess"):
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
					String questId = (String) message.getData();
					EventBus.getDefault().post(new QuestionAddedEvent(questId));
					break;
				case("returnExamForms"):
					List<ExamForm> examForms = (List<ExamForm>) message.getData();
					EventBus.getDefault().post(new ShowExamFormsEvent(examForms));
					break;
				case ("returnQuestionScores"):
					List<Question_Score> questionScores = (List<Question_Score>) message.getData();
					EventBus.getDefault().post(new ShowExamFormQuestionScoresEvent(questionScores));
					break;
				case ("returnScheduleTestWithInfo"):
					ScheduledTest scheduledTest = (ScheduledTest)message.getData();
					EventBus.getDefault().post(new SelectedTestEvent(scheduledTest));
					break;
				case ("returnStudent"):
					Student student = (Student) message.getData();
					EventBus.getDefault().post(new SelectedStudentEvent(student));
					break;
				case ("savedQuestionAnswers"):
					EventBus.getDefault().post(new ShowSuccessEvent("Congratulations!"));
					break;
				case ("timerStarted"):
					System.out.println("in simple client!");
					ScheduledTest scheduledTest1 = (ScheduledTest) message.getData();
					System.out.println("in simple client! timer started for test "+scheduledTest1.getId());
					EventBus.getDefault().postSticky(new TimerStartEvent(scheduledTest1));
					break;
				case ("timerFinished"):
					System.out.println("in simple client!");
					ScheduledTest scheduledTest2 = (ScheduledTest) message.getData();
					System.out.println("in simple client! timer finished for test "+scheduledTest2.getId());
					EventBus.getDefault().postSticky(new TimerFinishedEvent(scheduledTest2));
					break;
				case ("timeLeft"):
					long timeLeft = (long)message.getData();
					EventBus.getDefault().postSticky(new TimeLeftEvent(timeLeft));
					break;
				case ("extraTimeRequest"):
					EventBus.getDefault().postSticky(new extraTimeRequestEvent((List<Object>) message.getData()));
					break;
				case ("extraTimeResponse"):
					EventBus.getDefault().post(new ManagerExtraTimeEvent((List<Object>) message.getData()));
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
