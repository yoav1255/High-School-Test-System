package il.cshaifasweng.OCSFMediatorExample.server;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.persistence.Query;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.Criteria;
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

            //generateObjects();

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
        //List<QuestionScore> questionScores = QuestionScore.generateQuestionScores();

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

//        questionScores.get(0).setQuestion(questions.get(0));
//        questionScores.get(1).setQuestion(questions.get(1));
//        questionScores.get(2).setQuestion(questions.get(2));
//        questionScores.get(6).setQuestion(questions.get(6));
//
//        questionScores.get(3).setQuestion(questions.get(3));
//        questionScores.get(4).setQuestion(questions.get(4));
//        questionScores.get(5).setQuestion(questions.get(5));
//        questionScores.get(7).setQuestion(questions.get(7));
//
//        questions.get(0).addQuestionScore(questionScores.get(0));
//        questions.get(1).addQuestionScore(questionScores.get(1));
//        questions.get(2).addQuestionScore(questionScores.get(2));
//        questions.get(6).addQuestionScore(questionScores.get(6));
//
//        questions.get(3).addQuestionScore(questionScores.get(3));
//        questions.get(4).addQuestionScore(questionScores.get(4));
//        questions.get(5).addQuestionScore(questionScores.get(5));
//        questions.get(7).addQuestionScore(questionScores.get(7));

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
//        for(QuestionScore questionScore:questionScores)
//            session.save(questionScore);
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

    public static List<ScheduledTest> getScheduledTests() throws Exception{

        List<ScheduledTest> scheduledTests = new ArrayList<ScheduledTest>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        //
        String queryString = "SELECT s FROM ScheduledTest s";
        Query query = session.createQuery(queryString,ScheduledTest.class);
        scheduledTests = query.getResultList();
        //
        session.close();
        return scheduledTests;
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


   public static Teacher getTeacher(){
       // create a Criteria object for the Teacher class
       Criteria criteria = session.createCriteria(Teacher.class);
// set the first result to 0 (i.e., the first row)
       criteria.setFirstResult(0);
// set the maximum number of results to 1 (i.e., only one row)
       criteria.setMaxResults(1);
// execute the query and get the result
       Teacher firstTeacher = (Teacher) criteria.uniqueResult();

       return firstTeacher;
   }

    public static List<String> getListExamFormCode(){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Teacher firstTeacher = getTeacher();
        org.hibernate.Query<String> query = session.createQuery("SELECT code FROM ExamForm WHERE subject IN (:subjects)", String.class);
        query.setParameterList("subjects", firstTeacher.getSubjects());
        List<String> codes = query.getResultList();
        session.getTransaction().commit();
        session.close();
        System.out.println(codes);
        return codes;
    }
    public static ExamForm getExamForm(String examFormId) {
        SessionFactory sessionFactory = getSessionFactory();
        session=sessionFactory.openSession();
        session.beginTransaction();
        System.out.println("enter");
        Query query = session.createQuery("FROM ExamForm ef WHERE  ef.code = :examFormId",ExamForm.class);
        query.setParameter("examFormId",examFormId );
        ExamForm examForm=(ExamForm) query.getSingleResult();
        System.out.println(examForm.getExamFormCode());
        session.getTransaction().commit();
        session.close();
        return examForm;
    }
    public static void addScheduleTest(ScheduledTest scheduledTest) throws Exception {
            SessionFactory sessionFactory = getSessionFactory();
            session=sessionFactory.openSession();
            session.beginTransaction();
            System.out.println(scheduledTest);
            Teacher teacher=getTeacher();
            scheduledTest.setTeacher(teacher);
            session.save(scheduledTest);
            session.getTransaction().commit();
            session.close();
            System.out.println(scheduledTest);
    }
    public static List<Subject> getSubjectsFromTeacherId(String id){
        List<Subject> subjects = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        Teacher teacher = session.get(Teacher.class,id);
        String queryString = "SELECT s FROM Subject s WHERE :teacher IN elements(s.teachers)";
        Query query = session.createQuery(queryString, Subject.class);
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

    public static List<Course> getCoursesFromSubjectName(String subjectName){
        List<Course> courses = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String querySub = "SELECT s FROM Subject s WHERE s.name =:subjectName";
        Query q = session.createQuery(querySub, Subject.class);
        q.setParameter("subjectName",subjectName);
        Subject subject = (Subject) q.getSingleResult();
        String queryString = "SELECT c FROM Course c JOIN c.subject s WHERE s = :subject ";
        courses = session.createQuery(queryString, Course.class)
                        .setParameter("subject",subject)
                                .getResultList();
        for(Course course:courses)
            course.setSubject(subject);
        session.close();
        return courses;
    }
    public static Course getCourseFromCourseName(String courseName){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String querySub = "SELECT c FROM Course c WHERE c.name =:courseName";
        Query q = session.createQuery(querySub, Course.class);
        q.setParameter("courseName",courseName);
        Course course = (Course) q.getSingleResult();
        session.close();
        return course;
    }

    public static List<Question> getQuestionsFromCourseName(String courseName){
        List<Question> questions = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String querySub = "SELECT c FROM Course c WHERE c.name =:courseName";
        Query q = session.createQuery(querySub, Course.class);
        q.setParameter("courseName",courseName);
        Course course = (Course) q.getSingleResult();
        String queryString = "SELECT DISTINCT q FROM Course c JOIN c.questions q WHERE c = :course";
        Query query = session.createQuery(queryString, Question.class);
        query.setParameter("course",course);
        questions = query.getResultList();
        session.close();
        return questions;
    }
    public static void addExamForm(ExamForm examForm){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(examForm);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
    public static void addQuestionScores(List<QuestionScore> questionScores) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        for(QuestionScore questionScore:questionScores){
            session.save(questionScore);
        }
        session.flush();
        session.getTransaction().commit();
        session.close();
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

    public static String login_auth(String username, String password){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

// Check in the student table
        String studentQuery = "SELECT 'student' as type FROM Student WHERE id = :username AND password = :password";
        List<String> studentResults = session.createNativeQuery(studentQuery)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

// Check in the manager table
        String managerQuery = "SELECT 'manager' as type FROM principal WHERE id = :username AND password = :password";
        List<String> managerResults = session.createNativeQuery(managerQuery)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

// Check in the teacher table
        String teacherQuery = "SELECT 'teacher' as type FROM Teacher WHERE id = :username AND password = :password";
        List<String> teacherResults = session.createNativeQuery(teacherQuery)
                .setParameter("username", username)
                .setParameter("password", password)
                .getResultList();

// Combine the results and determine the user type
        String userType = null;

        if (!studentResults.isEmpty()) {
            userType = studentResults.get(0);
        } else if (!managerResults.isEmpty()) {
            userType = managerResults.get(0);
        } else if (!teacherResults.isEmpty()) {
            userType = teacherResults.get(0);
        }

        if (userType != null) {
            // User exists, userType contains the user type
            System.out.format("%s %s connecting to system", userType, username);
        }
        if(userType == null){userType = "wrong";}
        session.getTransaction().commit();
        session.close();
        return userType;
    }

    public static void addQuestion(Question question){

        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

        session.save(question);
/*        Subject subject = question.getSubject();
        subject.addQuestion(question);*//*
        List<Course> courses= question.getCourses();
        for(Course course:courses){
            course.addQuestion(question);
            session.saveOrUpdate(course);
        }
        session.saveOrUpdate(subject);*/
        session.flush();
        session.getTransaction().commit();
        session.close();

    }

    public static void updateScheduleTest(ScheduledTest scheduledTest) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("UPDATE ScheduledTest SET date = :newDate, time=:newTime,submissions=:newSubmission WHERE id = :newId");
        query.setParameter("newDate", scheduledTest.getDate());
        query.setParameter("newTime", scheduledTest.getTime());
        query.setParameter("newSubmission", scheduledTest.getSubmissions());
        query.setParameter("newId", scheduledTest.getId());
        session.getTransaction().commit();
        session.close();
        System.out.println(scheduledTest);
    }

    public static List<ExamForm> getCourseExamForms(String courseName) {
        List<ExamForm> examForms = new ArrayList<>();
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String querySub = "SELECT c FROM Course c WHERE c.name =:courseName";
        Query q = session.createQuery(querySub, Course.class);
        q.setParameter("courseName",courseName);
        Course course = (Course) q.getSingleResult();

        String queryString = "SELECT DISTINCT e FROM Course c JOIN c.examForms e WHERE c = :course";
        Query query = session.createQuery(queryString, ExamForm.class);
        query.setParameter("course",course);
        examForms = query.getResultList();
        session.close();
        return examForms;
    }

    public static List<QuestionScore> getQuestionScoresFromExamForm(ExamForm examForm) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String queryString = "SELECT qs FROM QuestionScore qs WHERE qs.examForm =:examForm";
        Query query = session.createQuery(queryString,QuestionScore.class);
        query.setParameter("examForm",examForm);
        List<QuestionScore> questionScores = query.getResultList();
        session.close();
        return questionScores;
    }
}
