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

//           generateObjects();

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
        configuration.addAnnotatedClass(QuestionScore.class);

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
        List<Question> questions = Question.GenerateQuestions();
        List<QuestionScore> questionScores = QuestionScore.generateQuestionScores();

// Update Courses
        courses.get(0).setSubject(subjects.get(0));
        courses.get(1).setSubject(subjects.get(0));
        courses.get(2).setSubject(subjects.get(1));
        courses.get(3).setSubject(subjects.get(1));
        courses.get(4).setSubject(subjects.get(2));
        courses.get(5).setSubject(subjects.get(2));

//Update Subjects

        subjects.get(0).addCourse(courses.get(0));
        subjects.get(0).addCourse(courses.get(1));
        subjects.get(1).addCourse(courses.get(2));
        subjects.get(1).addCourse(courses.get(3));
        subjects.get(2).addCourse(courses.get(4));
        subjects.get(2).addCourse(courses.get(5));

//Update Questions

        questions.get(0).setSubject(subjects.get(0));
        questions.get(0).setCourses(subjects.get(0).getCourses());
        questions.get(1).setSubject(subjects.get(0));
        questions.get(1).setCourses(subjects.get(0).getCourses());
        questions.get(2).setSubject(subjects.get(0));
        questions.get(2).setCourses(subjects.get(0).getCourses());
        questions.get(6).setSubject(subjects.get(0));
        questions.get(6).setCourses(subjects.get(0).getCourses());

        questions.get(3).setSubject(subjects.get(1));
        questions.get(3).setCourses(subjects.get(1).getCourses());
        questions.get(4).setSubject(subjects.get(1));
        questions.get(4).setCourses(subjects.get(1).getCourses());
        questions.get(5).setSubject(subjects.get(1));
        questions.get(5).setCourses(subjects.get(1).getCourses());
        questions.get(7).setSubject(subjects.get(1));
        questions.get(7).setCourses(subjects.get(1).getCourses());

        questionScores.get(0).setQuestion(questions.get(0));
        questionScores.get(1).setQuestion(questions.get(1));
        questionScores.get(2).setQuestion(questions.get(2));
        questionScores.get(6).setQuestion(questions.get(6));

        questionScores.get(3).setQuestion(questions.get(3));
        questionScores.get(4).setQuestion(questions.get(4));
        questionScores.get(5).setQuestion(questions.get(5));
        questionScores.get(7).setQuestion(questions.get(7));

        questions.get(0).addQuestionScore(questionScores.get(0));
        questions.get(1).addQuestionScore(questionScores.get(1));
        questions.get(2).addQuestionScore(questionScores.get(2));
        questions.get(6).addQuestionScore(questionScores.get(6));

        questions.get(3).addQuestionScore(questionScores.get(3));
        questions.get(4).addQuestionScore(questionScores.get(4));
        questions.get(5).addQuestionScore(questionScores.get(5));
        questions.get(7).addQuestionScore(questionScores.get(7));

        subjects.get(0).addQuestion(questions.get(0));
        subjects.get(0).addQuestion(questions.get(1));
        subjects.get(0).addQuestion(questions.get(2));
        subjects.get(0).addQuestion(questions.get(6));

        subjects.get(1).addQuestion(questions.get(3));
        subjects.get(1).addQuestion(questions.get(4));
        subjects.get(1).addQuestion(questions.get(5));
        subjects.get(1).addQuestion(questions.get(7));

        for (Course course : subjects.get(0).getCourses()){
            course.addQuestion(questions.get(0));
            course.addQuestion(questions.get(1));
            course.addQuestion(questions.get(2));
            course.addQuestion(questions.get(6));
        }

        for (Course course : subjects.get(1).getCourses()){
            course.addQuestion(questions.get(3));
            course.addQuestion(questions.get(4));
            course.addQuestion(questions.get(5));
            course.addQuestion(questions.get(7));
        }


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
        for (Question question: questions)
            session.save(question);
        for(QuestionScore questionScore:questionScores)
            session.save(questionScore);
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

    public static List<Subject> getSubjectsFromTeacher(Teacher teacher){
        List<Subject> subjects = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String queryString = "SELECT s FROM Subject s WHERE :teacher IN elements(s.teachers)";
        Query query = session.createQuery(queryString,Subject.class);
        query.setParameter("teacher",teacher);
        subjects = query.getResultList();
        session.close();
        return subjects;
    }
    public static Teacher getTeacherFromId(String id){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        Teacher teacher = session.get(Teacher.class,id);
        session.close();
        return teacher;
    }

    public static List<Course> getCoursesFromSubject(Subject subject){
        List<Course> courses = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String queryString = "SELECT c FROM Course c WHERE c.subject =:subject";
        courses = session.createQuery(queryString, Course.class)
                        .setParameter("subject",subject)
                                .getResultList();
        session.close();
        return courses;
    }

    public static List<Question> getQuestionsFromCourse(Course course){
        List<Question> questions = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String queryString = "SELECT q FROM Question q WHERE :course IN elements(q.courses)";
        Query query = session.createQuery(queryString, Question.class);
        query.setParameter("course",course);
        questions = query.getResultList();
        session.close();
        return questions;
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


    public static void updateStudentGrade(StudentTest stud){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE StudentTest SET grade = :newGrade WHERE id = :id");
        query.setParameter("newGrade", stud.getGrade());
        query.setParameter("id", stud.getId());
        int updatedCount = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }




}
