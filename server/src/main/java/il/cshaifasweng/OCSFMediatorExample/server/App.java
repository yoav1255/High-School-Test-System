package il.cshaifasweng.OCSFMediatorExample.server;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
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

//            generateObjects();

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
        courses.get(1).setSubject(subjects.get(0));
        courses.get(2).setSubject(subjects.get(1));
        courses.get(3).setSubject(subjects.get(1));
        courses.get(4).setSubject(subjects.get(2));
        courses.get(5).setSubject(subjects.get(2));
        subjects.get(0).addCourse(courses.get(0));
        subjects.get(0).addCourse(courses.get(1));
        subjects.get(1).addCourse(courses.get(2));
        subjects.get(1).addCourse(courses.get(3));
        subjects.get(2).addCourse(courses.get(4));
        subjects.get(2).addCourse(courses.get(5));

//Update ExamForms

        examForms.get(0).setSubject(subjects.get(0));
        examForms.get(0).setCourse(courses.get(0));
        subjects.get(0).addExamForm(examForms.get(0));
        courses.get(0).addExamForm(examForms.get(0));

        examForms.get(1).setSubject(subjects.get(1));
        examForms.get(1).setCourse(courses.get(3));
        subjects.get(1).addExamForm(examForms.get(1));
        courses.get(3).addExamForm(examForms.get(1));

//Update ScheduledTest

        for (int i = 0 ;i<scheduledTests.size()/3;i++){
            scheduledTests.get(i).setExamForm(examForms.get(0));
            scheduledTests.get(i).setTeacher(teachers.get(0));
            examForms.get(0).addScheduledTest(scheduledTests.get(i));
            teachers.get(0).addScheduledTest(scheduledTests.get(i));
        }
        for (int i = scheduledTests.size()/3 ;i<scheduledTests.size()*2/3;i++){
            scheduledTests.get(i).setExamForm(examForms.get(1));
            scheduledTests.get(i).setTeacher(teachers.get(1));
            examForms.get(1).addScheduledTest(scheduledTests.get(i));
            teachers.get(1).addScheduledTest(scheduledTests.get(i));
        }
        for (int i = scheduledTests.size()*2/3 ;i<scheduledTests.size();i++){
            scheduledTests.get(i).setExamForm(examForms.get(2));
            scheduledTests.get(i).setTeacher(teachers.get(2));
            examForms.get(2).addScheduledTest(scheduledTests.get(i));
            teachers.get(2).addScheduledTest(scheduledTests.get(i));
        }

//Update Teachers

        teachers.get(0).addCourses(courses.get(0));
        teachers.get(0).addSubject(subjects.get(0));
        courses.get(0).addTeacher(teachers.get(0));
        subjects.get(0).addTeacher(teachers.get(0));

        teachers.get(1).addCourses(courses.get(3));
        teachers.get(1).addSubject(subjects.get(1));
        courses.get(3).addTeacher(teachers.get(1));
        subjects.get(1).addTeacher(teachers.get(1));

        teachers.get(2).addCourses(courses.get(4));
        teachers.get(2).addSubject(subjects.get(2));
        courses.get(4).addTeacher(teachers.get(2));
        subjects.get(2).addTeacher(teachers.get(2));

//Update StudentTests

        for(int k = 0 ; k<2 ; k++) {
            for (int index = 0; index < 12; index++) {
                StudentTest s = studentTests.get(index+(k*12));
                s.setScheduledTest(scheduledTests.get(k*6));
                s.setStudent(students.get(index));
                scheduledTests.get(index).addStudentTest(s);
                students.get(index).addStudentTests(s);
            }
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

    public static List<String> getListExamFormCode(){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        // create a Criteria object for the Teacher class
        Criteria criteria = session.createCriteria(Teacher.class);
// set the first result to 0 (i.e., the first row)
        criteria.setFirstResult(0);
// set the maximum number of results to 1 (i.e., only one row)
        criteria.setMaxResults(1);
// execute the query and get the result

        Teacher firstTeacher = (Teacher) criteria.uniqueResult();

        org.hibernate.Query<String> query = session.createQuery("SELECT code FROM ExamForm WHERE subject IN (:subjects)", String.class);
        query.setParameterList("subjects", firstTeacher.getSubjects());
        List<String> codes = query.getResultList();
        session.close();
        return codes;
    }
    public static List<StudentTest> getStudentTests(Student student){
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
            studentTest.setStudent(student);
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
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE StudentTest SET grade = :newGrade WHERE id = :id");
        query.setParameter("newGrade", newGrade);
        query.setParameter("id", stud.getId());
        int updatedCount = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }


}
