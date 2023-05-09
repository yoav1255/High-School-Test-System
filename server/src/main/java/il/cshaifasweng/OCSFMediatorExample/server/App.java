package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;
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

    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3028);
        System.out.println("server is listening");
        server.listen();
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

    private static List<Student> getAllStudents() throws Exception{
        String queryString = "FROM Student";
        Query query = session.createQuery(queryString,Student.class);
        List<Student> students = query.getResultList();
        return students;
    }

//    private Student getStudentWithTests(String student_id){
//        String queryString = "SELECT s.id,s.first_name,s.last_name " +
//                "FROM Student s LEFT JOIN FETCH s.studentTests st " +
//                "WHERE s.id = :student_id";
//        Query query = session.createQuery(queryString);
//        query.setParameter("student_id",student_id);
//        return (Student) query.getSingleResult();
//    }


    private static void updateStudentGrade(String student_id, int studentTest_id, int newGrade){
        Student student = session.get(Student.class,student_id);
        StudentTest studentTest = session.get(StudentTest.class,studentTest_id);
        studentTest.setGrade(newGrade);
        studentTest.setStudent(student);
        session.saveOrUpdate(studentTest);
        session.saveOrUpdate(student);
        session.flush();
    }


}
