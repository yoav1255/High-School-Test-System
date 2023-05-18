package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.client.Controllers.ShowUpdateStudentController;
import il.cshaifasweng.OCSFMediatorExample.client.Events.ShowAllStudentsEvent;
import il.cshaifasweng.OCSFMediatorExample.client.Events.ShowOneStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.client.Events.ShowUpdateStudentEvent;
import il.cshaifasweng.OCSFMediatorExample.client.Events.WarningEvent;
import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.entities.*;


import java.util.List;

public class SimpleClient extends AbstractClient {
	
	private static SimpleClient client = null;

	private SimpleClient(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		if (msg.getClass().equals(Warning.class)) {
			EventBus.getDefault().post(new WarningEvent((Warning) msg));
		}
		else if(msg instanceof List ){
			List list = (List) msg;
			if(list!=null)
			{
				Object firstObject = list.get(0);
				if(firstObject instanceof Student){
					List <Student> listStudent = (List<Student>) msg;
					EventBus.getDefault().post(new ShowAllStudentsEvent(listStudent));
				} else if (firstObject instanceof StudentTest) {
					List<StudentTest> studentTests = (List<StudentTest>) msg;
					EventBus.getDefault().post(new ShowOneStudentEvent(studentTests));
				}
			}//TODO if its null then we have to decide what to do!!
		} else if (msg instanceof StudentTest) {
			StudentTest studentTest = (StudentTest) msg;
			ShowUpdateStudentController showUpdateStudentController = new ShowUpdateStudentController();
			EventBus.getDefault().post(new ShowUpdateStudentEvent(studentTest));
		}
	}
	
	public static SimpleClient getClient() {
		if (client == null) {
			client = new SimpleClient("localhost", 3028);
		}
		return client;
	}
}
