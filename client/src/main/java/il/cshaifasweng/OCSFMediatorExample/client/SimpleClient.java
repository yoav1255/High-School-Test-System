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
					System.out.println("updated grade successfully!");
					break;
				case ("returnTeacher"):
					break;
				case ("returnListCodes"):
					List <String> examFormCode=(List<String>) message.getData();
					EventBus.getDefault().post(new ExamFormEvent(examFormCode));
					break;
				case("addScheduleTestSuccess"):
					System.out.println("added new schedule test successfuly!");
					break;
				case ("returnExamForm"):
					ExamForm examForm=(ExamForm) message.getData();
					EventBus.getDefault().post(new ScheduledTestEvent(examForm));
					break;
				case ("returnScheduledTestList"):
					List<ScheduledTest> scheduledTests = (List<ScheduledTest>) message.getData();
					EventBus.getDefault().post(new ShowScheduleTestEvent(scheduledTests));
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
