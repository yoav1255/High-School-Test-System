package il.cshaifasweng.OCSFMediatorExample.server;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleServer extends AbstractServer {
	private static List<ScheduledTest> scheduledTests;
	private static List<ConnectionToClient> clients;
	private static int iterations = 0;
	private Timer timer;
	private static List<CustomMessage> allMessages;


	public SimpleServer(int port) {
		super(port);
		clients = new ArrayList<>();
		scheduleTestTimerHandler();
		EventBus.getDefault().register(this);
		allMessages = new ArrayList<>();
	}

	public static List<CustomMessage> getAllMessages() {
		return allMessages;
	}
	public static void setAllMessages(List<CustomMessage> allMessages) {
		SimpleServer.allMessages = allMessages;
	}

	public static List<ScheduledTest> getScheduledTests() {
		return scheduledTests;
	}

	public static void setScheduledTests(List<ScheduledTest> scheduledTests) {
		SimpleServer.scheduledTests = scheduledTests;
	}

	public List<ConnectionToClient> getClients() {
		return clients;
	}

	public void setClients(List<ConnectionToClient> clients) {
		this.clients = clients;
	}

	@Override
	protected void clientConnected(ConnectionToClient client) {
		try {
			clients.add(client);
			EventBus.getDefault().post(new NewClientEvent(client));
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	@Override
	protected synchronized void clientDisconnected(ConnectionToClient client) {
		try {
			clients.remove(client);
			EventBus.getDefault().post(new DeleteClientEvent(client));
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {

			CustomMessage message = (CustomMessage) msg;
			allMessages.add(message);
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
					client.sendToClient(new CustomMessage("returnStudentTestsFromStudent" ,studentTests));
					break;
				case ("#getStudentTestsFromSchedule"):
					List<StudentTest> studentTests1 =  App.getStudentTestsFromScheduled((ScheduledTest) message.getData());
					client.sendToClient(new CustomMessage("returnStudentTestsFromSchedule" ,studentTests1));
					break;
				case ("#getStudentTestWithInfo"):
					StudentTest studentTest1 = App.getStudentTest((StudentTest) message.getData());
					client.sendToClient(new CustomMessage("returnStudentTest",studentTest1));
					break;
				case("#updateStudentTest"):
					StudentTest studentTest = (StudentTest) message.getData();
					App.updateStudentTest(studentTest);
					client.sendToClient(new CustomMessage("updateSuccess",studentTest.getGrade()));
					break;
				case ("#login"):
					ArrayList<String> auth = (ArrayList<String>) message.getData();
					String user_type = App.login_auth(auth.get(0), auth.get(1));
					client.sendToClient(new CustomMessage("returnLogin", user_type));
					break;
				case ("#logout"):
					ArrayList<String> info = (ArrayList<String>) message.getData();
					App.logout(info.get(0), info.get(1));
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
					break;
				case ("#getQuestions"):
					List<Question> questions = App.getQuestionsFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnQuestions",questions));
					break;
				case ("#addQuestion"):
					Question question = (Question)message.getData();
					boolean check = App.addQuestion(question);
					String questId = String.valueOf(question.getId());
					List<Object> objectList = new ArrayList<>();
					objectList.add(check);
					objectList.add(questId);
					client.sendToClient(new CustomMessage("addQuestionSuccess",objectList));
					break;
				case ("#getCourseFromName"):
					Course course =App.getCourseFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnCourse",course));
					break;
				case ("#addExamForm"):
					ExamForm examForm = (ExamForm) message.getData();
					boolean check1 = App.addExamForm(examForm);
					client.sendToClient(new CustomMessage("addExamForm",check1));
					break;
				case ("#addQuestionScores"):
					List<Question_Score> questionScores = (List<Question_Score>) message.getData();
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
					boolean check3 = App.addScheduleTest(scheduledTest);
					client.sendToClient(new CustomMessage("addScheduleTestSuccess", check3));
					break;
				case ("#deleteRow"):
					ScheduledTest deleteScheduledTest = (ScheduledTest) message.getData();
					App.deleteScheduleTest(deleteScheduledTest);
					client.sendToClient(new CustomMessage("deleteScheduleTestSuccess", ""));
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
					ScheduledTest scheduledTest1 = (ScheduledTest) message.getData();
					boolean check2 = App.updateScheduleTest( scheduledTest1);
					client.sendToClient(new CustomMessage("updateScheduleTest", check2));
					break;
				case ("#getCourseExamForms"):
					List<ExamForm> examForms = App.getCourseExamForms(message.getData().toString());
					client.sendToClient(new CustomMessage("returnExamForms",examForms));
					break;
				case ("#getQuestionScores"):
					ExamForm examForm1 = (ExamForm) message.getData();
					List<Question_Score> questionScoreList = App.getQuestionScoresFromExamForm(examForm1);
					client.sendToClient(new CustomMessage("returnQuestionScores",questionScoreList));
					break;
				case ("SendSelectedTest"):
					ScheduledTest selectedTest =(ScheduledTest) message.getData();
					EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
					break;
				case("#getScheduleTestWithInfo"):
					ScheduledTest scheduledTest2 = App.getScheduleTestWithInfo(message.getData().toString());
					client.sendToClient(new CustomMessage("returnScheduleTestWithInfo",scheduledTest2));
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
					App.saveQuestionScores((List<Question_Score>) message.getData());
					//client.sendToClient(new CustomMessage("savedStudentTest_QuestionAnswers","Success"));
					break;
				case ("#saveStudentTest"):
					App.saveStudentTest((List<Object>) message.getData());
					//client.sendToClient(new CustomMessage("savedStudentTest","Success"));
					break;
				case ("#getAllSubjects"):
					List<Subject> subjects1 = App.getAllSubjects();
					client.sendToClient(new CustomMessage("returnAllSubjects",subjects1));
					break;
				case ("#endLocalTest"):
					TestFile file = (TestFile) message.getData();
					App.saveFileToDatabase(file);
					client.sendToClient(new CustomMessage("successEvent", ""));
					break;
				case ("#extraTimeResponse"):
					sendToAllClients(new CustomMessage("extraTimeResponse", (List<Object>) message.getData()));
					break;
				case ("#getExtraTimeRequests"):
					List<ExtraTime> extraTimeList = App.getAllExtraTimes();
					client.sendToClient(new CustomMessage("extraTimeRequests", extraTimeList));
					break;
				case ("#addExtraTimeRequest"):
					App.saveExtraTimeRequest((ExtraTime) message.getData());
					break;
				case ("#clearExtraTimeRequests"):
					App.clearExtraTimeTable();
					break;
				case ("#checkStudentTest"):
					List<Object> studentId_scheduleTestId = (List<Object>) message.getData();
					String studentId = (String) studentId_scheduleTestId.get(0);
					String scheduleTestId = (String) studentId_scheduleTestId.get(1);
					boolean firstTime = App.getFirstTestEntryCheck(studentId,scheduleTestId);
					System.out.println("sending from s.s to client");
					client.sendToClient(new CustomMessage("getIsFirstEntry",firstTime));
					break;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void scheduleTestTimerHandler(){
		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		// do this code every 20 seconds
		executorService.scheduleAtFixedRate(() -> {
			try {
				iterations++;
				if(iterations==1){
					App.logOffAllUsers();
				}
				scheduledTests = App.getScheduledTestsActive();
			} catch (Exception e) {
				e.printStackTrace();
			}
			LocalDateTime currentDateTime = LocalDateTime.now();
			assert scheduledTests != null;
			for (ScheduledTest scheduledTest : scheduledTests) {
				LocalDateTime scheduledDateTime = LocalDateTime.of(scheduledTest.getDate(), scheduledTest.getTime());
				long timeLimitMinutes = scheduledTest.getTimeLimit();

				LocalDateTime endTime = scheduledDateTime.plusMinutes(timeLimitMinutes);

				if(scheduledTest.getStatus()==1 && currentDateTime.isAfter(endTime)) // test is done but not yet updated in the db
				{
					scheduledTest.setStatus(2);
					App.addScheduleTest(scheduledTest);
				}

				else if((scheduledTest.getStatus()==0) || (iterations==1 && scheduledTest.getStatus()==1)) { // before test
					// or if server is up now, we need to check if there is a test that should continue its task

					if (currentDateTime.isAfter(scheduledDateTime)) {
						scheduledTest.setStatus(1); // set as during test
						App.addScheduleTest(scheduledTest);
						timer = new Timer();

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
								ScheduledTest st = App.getScheduleTest(scheduledTest.getId());
								long timeLimitMinutes = st.getTimeLimit();
								LocalDateTime scheduledDateTime = LocalDateTime.of(st.getDate(), st.getTime());
								LocalDateTime endTime = scheduledDateTime.plusMinutes(timeLimitMinutes);
								LocalDateTime currentDateTime = LocalDateTime.now();
								long timeLeft = Duration.between(currentDateTime,endTime).toMinutes();
								System.out.println(timeLeft + " time left");

								try {
									List<Object> scheduleTestId_timeLeft = new ArrayList<>();
									scheduleTestId_timeLeft.add(st.getId());
									System.out.println("Time Left: " + timeLeft);
									scheduleTestId_timeLeft.add(timeLeft);
									sendToAllClients(new CustomMessage("timeLeft",scheduleTestId_timeLeft));
								}catch (Exception e){
									e.printStackTrace();
								}

								if (currentDateTime.isAfter(endTime)) {

									try {
										sendToAllClients(new CustomMessage("timerFinished",st));
									}catch (Exception e){
										e.printStackTrace();
									}

									timer.cancel(); // Stop the timer when the time limit is reached
									scheduledTest.setStatus(2);
									App.addScheduleTest(scheduledTest);
								}
							}
						};


						timer.schedule(task, 0, 10000); // Check every 10 seconds (adjust the delay as needed)
					}
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}

	@Subscribe
	public void onTerminateAllClientsEvent(TerminateAllClientsEvent event){
		sendToAllClients(new CustomMessage("Terminate",""));
	}
}
