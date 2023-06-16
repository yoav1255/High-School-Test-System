package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import il.cshaifasweng.OCSFMediatorExample.entities.*;


import java.util.ArrayList;
import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
		System.out.println("host: "+host + " port: "+ port);
	}

	@Override
	protected void connectionEstablished(){
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
				case ("returnStudentTestsFromStudent"):
					List<StudentTest> studentTests = (List<StudentTest>) message.getData();
					EventBus.getDefault().post(new ShowOneStudentEvent(studentTests));
					break;
				case ("returnStudentTestsFromSchedule"):
					List<StudentTest> studentTests1 = (List<StudentTest>) message.getData();
					EventBus.getDefault().post(new ShowStudentFromScheduleEvent(studentTests1));
					break;
				case ("returnStudentTest"):
					StudentTest studentTest = (StudentTest) message.getData();
					EventBus.getDefault().post(new ShowUpdateStudentEvent(studentTest));
					break;
				case ("updateSuccess"):
					EventBus.getDefault().post(new ShowSuccessEvent("Student updated successfully with the grade "+message.getData()));
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
					EventBus.getDefault().post(new UpdateScheduleTestEvent((boolean) message.getData()));
					break;
				case ("deleteScheduleTestSuccess"):
					EventBus.getDefault().post(new ShowSuccessEvent("Successfully deleted"));
				case ("returnExamForm"):
					ExamForm examForm1=(ExamForm) message.getData();
					EventBus.getDefault().post(new ScheduledTestEvent(examForm1));
					break;
				case ("returnScheduledTestList"):
					List<ScheduledTest> scheduledTests = (List<ScheduledTest>) message.getData();
					EventBus.getDefault().post(new ShowScheduleTestEvent(scheduledTests));
					break;
				case ("addQuestionSuccess"):
					List<Object> objectList = new ArrayList<>();
					objectList = (List<Object>) message.getData();
					EventBus.getDefault().post(new QuestionAddedEvent(objectList));
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
					ScheduledTest scheduledTest1 = (ScheduledTest) message.getData();
					EventBus.getDefault().postSticky(new TimerStartEvent(scheduledTest1));
					break;
				case ("timerFinished"):
					ScheduledTest scheduledTest2 = (ScheduledTest) message.getData();
					EventBus.getDefault().postSticky(new TimerFinishedEvent(scheduledTest2));
					break;
				case ("timeLeft"):
					List<Object> scheduleTestId_timeLeft =(List<Object>) message.getData();
					EventBus.getDefault().postSticky(new TimeLeftEvent(scheduleTestId_timeLeft));
					break;
				case ("extraTimeRequests"):
					EventBus.getDefault().post(new extraTimeRequestEvent((List<ExtraTime>) message.getData()));
					break;
				case ("extraTimeResponse"):
					EventBus.getDefault().post(new ManagerExtraTimeEvent((List<Object>) message.getData()));
					break;
				case ("successEvent"):
					EventBus.getDefault().post(new ShowSuccessEvent("success"));
					break;
				case ("getIsFirstEntry"):
					System.out.println("int s.c posting event");
					EventBus.getDefault().post(new CheckFirstEntryEvent((Boolean)message.getData()));
					break;
				case ("Terminate"):
					System.exit(0);
					break;
				case ("updateScheduleTest"):
					EventBus.getDefault().post(new UpdateScheduleTestEvent((Boolean)message.getData()));
					break;
				case ("returnAllTeachersNames"):
					List<Teacher> teacherName = (List<Teacher>) message.getData();
					EventBus.getDefault().post(new ShowAllTeachersNamesEvent(teacherName));
					break;
				case ("returnAllCoursesNames"):
					List<Course> courseName =(List<Course>) message.getData();
					EventBus.getDefault().post(new ShowAllCoursesNamesEvent(courseName));
					break;
				case ("returnAllStudentsNames"):
					List<Student> studentName =(List<Student>) message.getData();
					EventBus.getDefault().post(new ShowAllStudentsNamesEvent(studentName));
					break;
				case ("returnTeacherStat"):
					ArrayList<Statistics> teacherStat = (ArrayList<Statistics>) message.getData();
					EventBus.getDefault().post(new ShowTeacherStatEvent(teacherStat));
					break;
				case ("returnCourseStat"):
					List<Statistics> courseStat = (List<Statistics>) message.getData();
					EventBus.getDefault().post(new ShowCourseStatEvent(courseStat));
					break;
				case ("returnStudentStat"):
					Statistics studentStat = (Statistics) message.getData();
					EventBus.getDefault().post(new ShowStudentStatEvent(studentStat));
					break;
				case ("generateUniqueExamCode"):
					EventBus.getDefault().post(new GetUniqueExamCode((String)message.getData()));
					break;
				case ("returnQuestionToUpdate"):
					EventBus.getDefault().post(new ShowUpdateQuestFormEvent((List<Object>) message.getData()));
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
