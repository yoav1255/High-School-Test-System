package il.cshaifasweng.OCSFMediatorExample.server;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleServer extends AbstractServer {
	private static List<ScheduledTest> scheduledTests;
	private List<ConnectionToClient> clients;


	public SimpleServer(int port) {
		super(port);
		clients = new ArrayList<>();
		scheduleTestTimerHandler();

	}
	@Override
	protected void clientConnected(ConnectionToClient client) {
		clients.add(client);
	}

	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		clients.remove(client);
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
					break;
				case ("#showAllStudents"):
					List<Student> studentList = App.getAllStudents();
					client.sendToClient(new CustomMessage("returnStudentList",studentList));
					break;
				case ("#getStudentTests"):
					List<StudentTest> studentTests =  App.getStudentTests((Student) message.getData());
					client.sendToClient(new CustomMessage("returnStudentTests" ,studentTests));
					break;
				case ("#getStudentTestsFromSchedule"):
					List<StudentTest> studentTests1 =  App.getStudentTestsFromScheduled((ScheduledTest) message.getData());
					client.sendToClient(new CustomMessage("returnStudentTests" ,studentTests1));
					break;
				case ("#getStudentTest"):
					client.sendToClient(new CustomMessage("returnStudentTest",message.getData()));
					break;
				case("#updateGrade"):
					StudentTest studentTest = (StudentTest) message.getData();
					App.updateStudentGrade(studentTest);
					client.sendToClient(new CustomMessage("updateSuccess",""));
					break;
				case ("#login"):
					ArrayList<String> auth = (ArrayList<String>) message.getData();
					String user_type = App.login_auth(auth.get(0), auth.get(1));
					client.sendToClient(new CustomMessage("returnLogin", user_type));
					break;
				case ("#studentHome"):
					client.sendToClient(new CustomMessage("studentHome", message.getData()));
					break;
				case ("#teacherHome"):
					client.sendToClient(new CustomMessage("teacherHome", message.getData()));
					break;
				case ("#managerHome"):
					client.sendToClient(new CustomMessage("managerHome", message.getData()));
					break;
				case ("#getSubjects"):
					List<Subject> subjects = App.getSubjectsFromTeacherId(message.getData().toString());
					client.sendToClient(new CustomMessage("returnSubjects",subjects));
					break;
				case ("#getCourses"):
					List<Course> courses = App.getCoursesFromSubjectName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnCourses",courses));
				case ("#getQuestions"):
					List<Question> questions = App.getQuestionsFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnQuestions",questions));
					break;
				case ("#addQuestion"):
					Question question = (Question)message.getData();
					App.addQuestion(question);
					String questId = String.valueOf(question.getId());
					client.sendToClient(new CustomMessage("addQuestionSuccess",questId));
					break;
				case ("#getCourseFromName"):
					Course course =App.getCourseFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnCourse",course));
					break;
				case ("#addExamForm"):
					ExamForm examForm = (ExamForm) message.getData();
					App.addExamForm(examForm);
					break;
				case ("#addQuestionScores"):
					List<QuestionScore> questionScores = (List<QuestionScore>) message.getData();
					App.addQuestionScores(questionScores);
					break;
				case ("#getTeacher"):
					Teacher teacher = App.getTeacherFromId((String) message.getData().toString());
					client.sendToClient(new CustomMessage("returnTeacher", teacher));
					break;
				case ("#fillComboBox"):
					List<String> examFormCode = App.getListExamFormCode((String) message.getData().toString());
					client.sendToClient(new CustomMessage("returnListCodes", examFormCode));
					break;
				case ("#addScheduleTest"):
					ScheduledTest scheduledTest = (ScheduledTest) message.getData();
					App.addScheduleTest(scheduledTest);
					client.sendToClient(new CustomMessage("addScheduleTestSuccess", ""));
					break;
				case ("#sendExamFormId"):
					ExamForm examForm2 = App.getExamForm((message.getData().toString()));
					client.sendToClient(new CustomMessage("returnExamForm", examForm2));
					break;

				case ("#showScheduleTest"):
					List<ScheduledTest> scheduledTests = App.getScheduledTests();
					client.sendToClient(new CustomMessage("returnScheduledTestList", scheduledTests));
					break;
				case ("#updateScheduleTest"):
					App.updateScheduleTest( (ScheduledTest) message.getData());
					client.sendToClient(new CustomMessage("updateSuccess", ""));
					break;
				case ("#getCourseExamForms"):
					List<ExamForm> examForms = App.getCourseExamForms(message.getData().toString());
					client.sendToClient(new CustomMessage("returnExamForms",examForms));
					break;
				case ("#getQuestionScores"):
					ExamForm examForm1 = (ExamForm) message.getData();
					List<QuestionScore> questionScoreList = App.getQuestionScoresFromExamForm(examForm1);
					client.sendToClient(new CustomMessage("returnQuestionScores",questionScoreList));
					break;
				case ("SendSelectedTest"):
					ScheduledTest selectedTest =(ScheduledTest) message.getData();
					EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
					break;
				case("#getScheduleTestWithInfo"):
					ScheduledTest scheduledTest1 = App.getScheduleTestWithInfo(message.getData().toString());
					client.sendToClient(new CustomMessage("returnScheduleTestWithInfo",scheduledTest1));
					break;
				case ("#getStudent"):
					Student student = App.getStudent(message.getData().toString());
					client.sendToClient(new CustomMessage("returnStudent",student));
					break;
				case ("#saveQuestionAnswers"):
					App.saveQuestionAnswers((List<Object>) message.getData());
					client.sendToClient(new CustomMessage("savedQuestionAnswers","Success"));
					break;
				case ("#saveQuestionScores"):
					App.saveQuestionScores((List<QuestionScore>) message.getData());
					//client.sendToClient(new CustomMessage("savedStudentTest_QuestionAnswers","Success"));
					break;
				case ("#saveStudentTest"):
					App.saveStudentTest((List<Object>) message.getData());
					//client.sendToClient(new CustomMessage("savedStudentTest","Success"));
					break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void scheduleTestTimerHandler(){
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		executorService.scheduleAtFixedRate(new Runnable() { // do this code every 20 seconds
			@Override
			public void run() {
				try {
					scheduledTests = App.getScheduledTestsActive();
				} catch (Exception e) {
					e.printStackTrace();
				}
				LocalDateTime currentDateTime = LocalDateTime.now();
				assert scheduledTests != null;
				for (ScheduledTest scheduledTest : scheduledTests) {

					if(scheduledTest.getStatus()==0) { // before test
						LocalDateTime scheduledDateTime = LocalDateTime.of(scheduledTest.getDate(), scheduledTest.getTime());

						if (currentDateTime.isAfter(scheduledDateTime)) {
							long timeLimitMinutes = scheduledTest.getExamForm().getTimeLimit();
							scheduledTest.setStatus(1); // set as during test
							App.addScheduleTest(scheduledTest);
							LocalDateTime startTime = scheduledDateTime;
							LocalDateTime endTime = startTime.plusMinutes(timeLimitMinutes);

							Timer timer = new Timer();
							try {
								sendToAllClients(new CustomMessage("timerStarted", scheduledTest));
							}catch (Exception e){
								e.printStackTrace();
							}
							System.out.println("timer started for test : "+ scheduledTest.getId());
							// timer started
							// now we apply what the timer will do through its whole lifecycle
							TimerTask task = new TimerTask() {
								@Override
								public void run() {
									LocalDateTime currentDateTime = LocalDateTime.now();
									if (currentDateTime.isAfter(endTime)) {
										System.out.println("current date time " + currentDateTime);
										System.out.println("end time " + endTime);

										System.out.println("checking the time left " + Duration.between(currentDateTime,endTime).toMinutes());
										try {
												sendToAllClients(new CustomMessage("timerFinished",scheduledTest));
										}catch (Exception e){
											e.printStackTrace();
										}
										timer.cancel(); // Stop the timer when the time limit is reached
										scheduledTest.setStatus(2);
										App.addScheduleTest(scheduledTest);
										System.out.println("Status "+ scheduledTest.getStatus());
									}
								}
							};


							timer.schedule(task, 0, 3000); // Check every 3 seconds (adjust the delay as needed)
						}
					}
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}

}
