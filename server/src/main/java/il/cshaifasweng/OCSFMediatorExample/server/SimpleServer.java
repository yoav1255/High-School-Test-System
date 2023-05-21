package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

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
            switch (msgString) {
                case ("#warning"):
                    Warning warning = new Warning("Warning from server!");
                    client.sendToClient(new CustomMessage("returnWarning", warning));
                    System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
                    break;
                case ("#showAllStudents"):
                    List<Student> studentList = App.getAllStudents();
                    client.sendToClient(new CustomMessage("returnStudentList", studentList));
                    System.out.format("Sent Students to client %s\n", client.getInetAddress().getHostAddress());
                    break;
                case ("#getStudentTests"):
                    List<StudentTest> studentTests = App.getStudentTests((Student) message.getData());
                    client.sendToClient(new CustomMessage("returnStudentTests", studentTests));
                    System.out.format("Sent student tests to client %s\n", client.getInetAddress().getHostAddress());
                    break;
                case ("#getStudentTest"):
                    client.sendToClient(new CustomMessage("returnStudentTest", message.getData()));
                    System.out.format("Sent student test to client %s\n", client.getInetAddress().getHostAddress());
                    break;
                case ("#updateGrade"):
                    StudentTest studentTest = (StudentTest) message.getData();
                    App.updateStudentGrade(studentTest);
                    client.sendToClient(new CustomMessage("updateSuccess", ""));
                    break;
                case ("#getTeacher"):
                    Teacher teacher = App.getTeacherFromId(message.getData().toString());
                    client.sendToClient(new CustomMessage("returnTeacher", teacher));
                    break;
                case ("#fillComboBox"):
                    List<String> examFormCode = App.getListExamFormCode();
                    client.sendToClient(new CustomMessage("returnListCodes", examFormCode));
                    client.sendToClient(new CustomMessage("sentExamFormCodeSuccess", ""));
                    break;
                case ("#addScheduleTest"):
                    ScheduledTest scheduledTest = (ScheduledTest) message.getData();
                    App.addScheduleTest(scheduledTest);
                    client.sendToClient(new CustomMessage("addScheduleTestSuccess", ""));
                    break;
                case ("#sendExamFormId"):
                    ExamForm examForm = App.getExamForm((message.getData().toString()));
                    client.sendToClient(new CustomMessage("returnExamForm", examForm));
                    break;

                case ("#showScheduleTest"):
                    List<ScheduledTest> scheduledTests = App.getScheduledTests();
                    client.sendToClient(new CustomMessage("returnScheduledTestList", scheduledTests));
                    break;
                case ("#updateScheduleTest"):
                    System.out.println("i got here");
                    App.updateScheduleTest( (ScheduledTest) message.getData());
                    client.sendToClient(new CustomMessage("updateSuccess", ""));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

