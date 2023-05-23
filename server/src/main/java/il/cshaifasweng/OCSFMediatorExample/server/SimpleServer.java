package il.cshaifasweng.OCSFMediatorExample.server;

import com.mysql.cj.xdevapi.Client;
import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.Events.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
//				case ("#SendIdToExamForms"):
//					System.out.println("In simple server send id to exam forms "+message.getData());
//					client.sendToClient(new CustomMessage("returnIdToPage",message.getData()));
//					break;
//				case ("#SendIdToScheduleTest"):
//					System.out.println("In simple server send id to scheduled Test form "+message.getData());
//					client.sendToClient(new CustomMessage("returnIdToPage",message.getData()));
//					break;
				case ("#getSubjects"):
					List<Subject> subjects = App.getSubjectsFromTeacherId(message.getData().toString());
					client.sendToClient(new CustomMessage("returnSubjects",subjects));
					break;
				case ("#getCourses"):
					List<Course> courses = App.getCoursesFromSubjectName(message.getData().toString());
					System.out.println(courses.get(0).getName());
					client.sendToClient(new CustomMessage("returnCourses",courses));
				case ("#getQuestions"):
					List<Question> questions = App.getQuestionsFromCourseName(message.getData().toString());
					client.sendToClient(new CustomMessage("returnQuestions",questions));
					break;
				case ("#addQuestion"):
					Question question = (Question)message.getData();
					App.addQuestion(question);
					client.sendToClient(new CustomMessage("addQuestionSuccess",""));
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
					System.out.println("WE GOT TO THE SIMPLE SERVER");
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
					System.out.println("return SChedule Test " + selectedTest);
					EventBus.getDefault().post(new SelectedTestEvent(selectedTest));
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
