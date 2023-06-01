package il.cshaifasweng.OCSFMediatorExample.server;

import java.util.ArrayList;
import java.util.List;

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
    private static final SessionFactory sessionFactory = getSessionFactory();
    private static List<ScheduledTest> scheduledTests;

    public static void main(String[] args) throws Exception {
        server = new SimpleServer(3028);
        System.out.println("server is listening");
        server.listen();
        try {
            //SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();

            //generateObjects();

            session.getTransaction().commit(); // Save Everything in the transaction area

        } catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        } finally {
            session.close();
        }
        //List<Integer> check = getTeacherExamStats("2");
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
        configuration.addAnnotatedClass(Question_Score.class);
        configuration.addAnnotatedClass(Question_Answer.class);


        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateObjects() throws Exception{
        List<Student> students = Student.GenerateStudents();
        List<Subject> subjects = Subject.GenerateSubjects();
        List<Course> courses = Course.GenerateCourses();
        List<Teacher> teachers = Teacher.GenerateTeachers();
        List<Question> questions = Question.GenerateQuestions();

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


// ------------ Add objects to DB --------//

        for(Subject subject:subjects)
            session.save(subject);
        for(Course course:courses)
            session.save(course);
        for (Question question: questions)
            session.save(question);
        for (Teacher teacher:teachers)
            session.save(teacher);
        for (Student student:students)
            session.save(student);


        session.flush();
    }

    public static List<ScheduledTest> getScheduledTests() throws Exception {

        List<ScheduledTest> scheduledTests;
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();

        String hql = "SELECT st FROM ScheduledTest st JOIN FETCH st.examForm et";
        Query query = session.createQuery(hql, ScheduledTest.class);
        scheduledTests = query.getResultList();

        for (ScheduledTest scheduledTest : scheduledTests) {
            int timeLimit = scheduledTest.getExamForm().getTimeLimit();
        }
        session.close();
            return scheduledTests;
    }

    static List<ScheduledTest> getScheduledTestsActive() throws Exception{
        List<ScheduledTest> scheduledTests;
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();

        String hql = "SELECT st FROM ScheduledTest st JOIN FETCH st.examForm et WHERE st.status IN(1,0)";
        Query query = session.createQuery(hql, ScheduledTest.class);
        scheduledTests = query.getResultList();

        for (ScheduledTest scheduledTest : scheduledTests) {
            int timeLimit = scheduledTest.getExamForm().getTimeLimit();
        }
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

    public static List<String> getListExamFormCode(String teacherId){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Teacher teacher = session.get(Teacher.class,teacherId);
        org.hibernate.Query<String> query = session.createQuery("SELECT code FROM ExamForm WHERE subject IN (:subjects)", String.class);
        query.setParameterList("subjects", teacher.getSubjects());
        List<String> codes = query.getResultList();
        session.getTransaction().commit();
        session.close();
        return codes;
    }
    public static ExamForm getExamForm(String examFormId) {
        SessionFactory sessionFactory = getSessionFactory();
        session=sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM ExamForm ef WHERE  ef.code = :examFormId",ExamForm.class);
        query.setParameter("examFormId",examFormId );
        ExamForm examForm=(ExamForm) query.getSingleResult();
        session.getTransaction().commit();
        session.close();
        return examForm;
    }
    public static void addScheduleTest(ScheduledTest scheduledTest) {
        SessionFactory sessionFactory = getSessionFactory();
        session=sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(scheduledTest);
        session.flush();
        session.getTransaction().commit();
        session.close();
    }
    public static void updateScheduleTests(List<ScheduledTest> scheduledTests, SessionFactory sessionFactory) throws Exception {
        session=sessionFactory.openSession();
        session.beginTransaction();
        for(ScheduledTest scheduledTest:scheduledTests) {
            System.out.println("update "+scheduledTest.getId() + " status "+ scheduledTest.getStatus());
            session.saveOrUpdate(scheduledTest);
        }
        session.flush();
        session.getTransaction().commit();
        session.close();
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
    public static void addQuestionScores(List<Question_Score> questionScores) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        for(Question_Score questionScore:questionScores){
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
    public static List<StudentTest> getStudentTestsFromScheduled(ScheduledTest scheduledTest){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("FROM StudentTest s WHERE s.scheduledTest = :scheduledTest", StudentTest.class);
        query.setParameter("scheduledTest", scheduledTest);
        List<StudentTest> studentTests = query.getResultList();
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
//            System.out.format("%s %s connecting to system", userType, username);
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


        session.flush();
        session.getTransaction().commit();
        session.close();

    }

    public static void updateScheduleTest(ScheduledTest scheduledTest) {
        try {
            SessionFactory sessionFactory = getSessionFactory();
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.update(scheduledTest);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static List<Question_Score> getQuestionScoresFromExamForm(ExamForm examForm) {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        String queryString = "SELECT qs FROM Question_Score qs WHERE qs.examForm =:examForm";
        Query query = session.createQuery(queryString, Question_Score.class);
        query.setParameter("examForm",examForm);
        List<Question_Score> questionScores = query.getResultList();
        session.close();
        return questionScores;
    }

    public static ScheduledTest getScheduleTestWithInfo(String id){
        ScheduledTest scheduledTest;
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();

        scheduledTest = session.get(ScheduledTest.class,id);
        String qString = "SELECT e FROM ExamForm e WHERE :scheduleTest in elements(e.scheduledTests) ";
        Query query = session.createQuery(qString, ExamForm.class);
        query.setParameter("scheduleTest",scheduledTest);
        ExamForm examForm = (ExamForm) query.getSingleResult();

        String hql = "SELECT qs FROM Question_Score qs " +
                "JOIN FETCH qs.question " +
                "WHERE qs.examForm = :examForm";

        List<Question_Score> questionScores = session.createQuery(hql)
                .setParameter("examForm", examForm)
                .getResultList();

        examForm.setQuestionScores(questionScores);
        scheduledTest.setExamForm(examForm);
        session.close();
        return scheduledTest;
    }

    public static Student getStudent(String id){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        Student student = session.get(Student.class,id);
        String queryString = "SELECT st from StudentTest st where st.student =:student";
        Query query = session.createQuery(queryString, StudentTest.class);
        query.setParameter("student",student);
        List<StudentTest> studentTests = query.getResultList();
        student.setStudentTests(studentTests);
        session.close();
        System.out.println(student.getEmail());
        return student;
    }
    public static void saveQuestionAnswers(List<Object> items){
        Student student = (Student) items.get(0);
        StudentTest studentTest = (StudentTest) items.get(1);

        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
//        session.saveOrUpdate(student);
        session.saveOrUpdate(studentTest);
        session.flush();
        for(int i=2;i<items.size();i++){
            Question_Answer item = (Question_Answer) items.get(i);
            System.out.println("saving question answer "+ item.getId());
            System.out.println("in question answer q.s id "+ item.getQuestionScore().getId());
            System.out.println("in question answer st id "+ item.getStudentTest().getId());
            session.save(item);
        }
        session.flush();
        session.getTransaction().commit(); // Save Everything in the transaction area
        session.close();
    }

    public static void saveQuestionScores(List<Question_Score> items){
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        for(Question_Score item:items){
            session.saveOrUpdate(item);
        }
        session.flush();
        session.getTransaction().commit(); // Save Everything in the transaction area
        session.close();
    }
    public static void saveStudentTest(List<Object> student_studentTest){
        Student student = (Student) student_studentTest.get(0);
        StudentTest studentTest = (StudentTest) student_studentTest.get(1);

        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        session.saveOrUpdate(student);
        session.saveOrUpdate(studentTest);
        session.flush();
        session.getTransaction().commit(); // Save Everything in the transaction area
        session.close();
    }

}
