package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.*;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hello world!
 *
 */
public class App
{
	
	private static SimpleServer server;
    private static Session session;

    public static void main( String[] args ) throws Exception
    {
        server = new SimpleServer(3028);
        System.out.println("server is listening");
        server.listen();
        try{
            SessionFactory sessionFactory = getSessionFactory();
            session=sessionFactory.openSession();
            session.beginTransaction();
            //
            //generateObjects();
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
        //
//        List<Student> students = getAllStudents();
//        for(Student s : students)
//            System.out.println(s.getEmail());
//        //

//        Student student = getAllStudents().get(0);
//        List<StudentTest> studentTests = getStudentTestsById(student);
//        StudentTest studentTest1 = studentTests.get(0);
//        System.out.println(studentTest1.getStudent().getId());
//        System.out.println(studentTest1.getGrade());
//        System.out.println(studentTest1.getTeacherId(studentTest1.getScheduledTest()));
//        System.out.println(studentTest1.getSubjectCode(studentTest1.getScheduledTest()));
//        System.out.println(studentTest1.getCourseCode(studentTest1.getScheduledTest()));
    }

    public static Session getSession() {
        return session;
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

        int i = 0; int j = 6;
        for(int index = 0; index<studentTests.size();index++){
            StudentTest s = studentTests.get(index);
            s.setScheduledTest(scheduledTests.get(i));
            s.setStudent(students.get(j));
            scheduledTests.get(i).addStudentTest(s);
            students.get(j).addStudentTests(s);
            i++; j++;
            if(i>=4) i=0;
            if(j>=12) j=0;
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

    public static List<Student> getAllStudents() throws Exception{

        List<Student> students = new ArrayList<Student>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        //
        String queryString = "SELECT s FROM Student s";
        Query query = session.createQuery(queryString,Student.class);
        students = query.getResultList();
        //
        session.close();
        return students;
    }

    public static List<StudentTest> getStudentTestsById(Student student){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery("FROM StudentTest s WHERE s.student = :studentToTake", StudentTest.class);
        query.setParameter("studentToTake", student);
        List<StudentTest> studentTests = query.getResultList();
        student.setStudentTests(studentTests);
        for (StudentTest studentTest : studentTests) {
            Query query2 = session.createQuery("FROM ScheduledTest s WHERE :studentTest IN elements(s.studentTests)", ScheduledTest.class);
            query2.setParameter("studentTest", studentTest);
            ScheduledTest scheduledTest = (ScheduledTest) query2.getSingleResult();

            studentTest.setScheduledTest(scheduledTest);
            ExamForm examForm = scheduledTest.getExamForm();
            studentTest.setSubject(scheduledTest,examForm.getSubject());
            studentTest.setCourse(scheduledTest,examForm.getCourse());

            Teacher teacher = scheduledTest.getTeacher();
            studentTest.setTeacher(scheduledTest,teacher);
        }

        session.getTransaction().commit();
        session.close();

        return studentTests;
    }

    public static StudentTest getStudentTest(StudentTest studentTest){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        StudentTest studentTestToReturn = session.get(StudentTest.class,studentTest);
        session.getTransaction().commit();
        session.close();
        return studentTestToReturn;
    }

    public static void updateStudentGrade(StudentTest stud, int newGrade){ //Method checked
        System.out.println("In update Student Grade!");
        System.out.println(stud.getGrade()+1);

        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE StudentTest SET grade = :newGrade WHERE id = :id");
        query.setParameter("newGrade", newGrade);
        query.setParameter("id", stud.getId());
        int updatedCount = query.executeUpdate();
        System.out.println(updatedCount);
        session.getTransaction().commit();
        session.close();
    }


}
