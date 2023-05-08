package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private SimpleClient client;
    private static Session session;

    @Override
    public void start(Stage stage) throws IOException {
    	EventBus.getDefault().register(this);
    	client = SimpleClient.getClient();
    	client.openConnection();
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();

    // -------------- //

        try{
            SessionFactory sessionFactory = getSessionFactory();
            session=sessionFactory.openSession();
            session.beginTransaction();
            //
            generateObjects();
            //
            session.getTransaction().commit(); // Save Everything in the transaction area
        } catch (Exception exception){
            if(session!=null){
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static SessionFactory getSessionFactory()throws HibernateException{
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Course.class);
        configuration.addAnnotatedClass(ExamForm.class);
        configuration.addAnnotatedClass(Principal.class);
        configuration.addAnnotatedClass(Question.class);
        configuration.addAnnotatedClass(Student.class);
        configuration.addAnnotatedClass(ScheduledTest.class);
        configuration.addAnnotatedClass(StudentTest.class);
        configuration.addAnnotatedClass(Subject.class);
        configuration.addAnnotatedClass(Teacher.class);


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateObjects() throws Exception{
        List<Student> students = Student.GenerateStudents();
        List<Subject> subjects = Subject.GenerateSubjects();
        List<Course> courses = Course.GenerateCourses();
        List<ExamForm> examForms = ExamForm.GenerateExamForms();
        List<Teacher> teachers = Teacher.GenerateTeachers();
        List<ScheduledTest> scheduledTests = ScheduledTest.GenerateScheduledTests();
        List<StudentTest> studentTests = StudentTest.GenerateStudentTests();

// Update Courses
        courses.get(0).setSubject(subjects.get(0));
        subjects.get(0).addCourse(courses.get(0));

//Update ExamForms

        examForms.get(0).setSubject(subjects.get(0));
        examForms.get(0).setCourse(courses.get(0));
        subjects.get(0).addExamForm(examForms.get(0));
        courses.get(0).addExamForm(examForms.get(0));

//Update ScheduledTest

        for (ScheduledTest s : scheduledTests){
            s.setExamForm(examForms.get(0));
            s.setTeacher(teachers.get(0));
            examForms.get(0).addScheduledTest(s);
            teachers.get(0).addScheduledTest(s);
        }

//Update Teachers

        teachers.get(0).addCourses(courses.get(0));
        teachers.get(0).addSubject(subjects.get(0));
        courses.get(0).addTeacher(teachers.get(0));
        subjects.get(0).addTeacher(teachers.get(0));

//Update StudentTests

        for(StudentTest s : studentTests){
            Random rand = new Random();
            int randTest = rand.nextInt(4);
            int randStudent = rand.nextInt(12);
            s.setScheduledTest(scheduledTests.get(randTest));
            s.setStudent(students.get(randStudent));
            scheduledTests.get(randTest).addStudentTest(s);
            students.get(randStudent).addStudentTests(s);
        }

// ------------ Add objects to DB --------//

        for(Subject subject:subjects)
            session.save(subject);
        for(Course course:courses)
            session.save(course);
        for (ExamForm examForm:examForms)
            session.save(examForm);
        for (ScheduledTest scheduledTest:scheduledTests)
            session.save(scheduledTest);
        for (Teacher teacher:teachers)
            session.save(teacher);
        for (Student student:students)
            session.save(student);
        for (StudentTest studentTest:studentTests)
            session.save(studentTest);
        session.flush();
    }

    private static List<Student> getAllStudents() throws Exception{
        String queryString = "FROM Student";
        Query query = session.createQuery(queryString,Student.class);
        List<Student> students = query.getResultList();
        return students;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
    

    @Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
    	EventBus.getDefault().unregister(this);
		super.stop();
	}
    
    @Subscribe
    public void onWarningEvent(WarningEvent event) {
    	Platform.runLater(() -> {
    		Alert alert = new Alert(AlertType.WARNING,
        			String.format("Message: %s\nTimestamp: %s\n",
        					event.getWarning().getMessage(),
        					event.getWarning().getTime().toString())
        	);
        	alert.show();
    	});
    	
    }

	public static void main(String[] args) {
        launch();
    }

}