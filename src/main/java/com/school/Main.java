package com.school;

import com.google.common.base.Optional;
import com.school.entity.Course;
import com.school.entity.Student;
import com.school.entity.StudentCourse;
import com.school.entity.Teacher;
import com.school.util.DBUtil;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class);

    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        BasicConfigurator.configure();

        logger.trace("Welcome to the application");

        DBUtil.addCourses();

        DBUtil.addTeachers();

        requestOption();
    }

    private static void requestOption() {
        logger.info("\n" +
                "Enter 1 to register a student\n" +
                "Enter 2 to register course for a student\n" +
                "Enter 3 to get the list of students in the school\n" +
                "Enter 4 to get the list of courses registered for a student\n" +
                "Enter 5 to get all the teachers in the school\n" +
                "Enter 6 to get the teacher assigned to a student\n" +
                "Enter any other key to exit");
        try {
            int option = sc.nextInt();
            if (option < 1 || option > 6) throw new InputMismatchException();

            doOption(option);
        }
        catch (InputMismatchException e) {
            logger.trace("Thank you for using the application");
        }

        sc.close();
    }

    private static void doOption(int option) {
        switch (option) {
            case 1:
                registerStudent();
                requestOption();
                break;
            case 2:
                registerCourse();
                requestOption();
                break;
            case 3:
                getStudents();
                requestOption();
                break;
            case 4:
                getCourses();
                requestOption();
                break;
            case 5:
                getTeachers();
                requestOption();
                break;
            case 6:
                getTeacher();
                requestOption();
                break;
            default:
                logger.warn("Option not supported");
        }
    }

    private static void registerStudent() {
        logger.info("Enter student name");
        String name = sc.next();

        Optional<Student> student = DBUtil.addStudent(name);

        if (student.isPresent()) logger.info(String.format("Student registered with id %s", student.get().getId()));
        else logger.warn("An internal error occurred");
    }

    private static void registerCourse() {
        logger.info("Enter student id");
        int stuId = sc.nextInt();

        if (DBUtil.getStudent(stuId).isPresent()) {
            logger.info("Enter course id [From 1 - 10]");
            int courseId = sc.nextInt();

            if (DBUtil.getCourse(courseId).isPresent()) {
                Optional<StudentCourse> course = DBUtil.addCourse(stuId, courseId);

                if (course.isPresent()) logger.info("Course registration successful");
                else logger.warn("An internal error occurred");
            }
            else logger.warn(String.format("Course with id %s does not exists", courseId));
        }
        else logger.warn(String.format("Student with id %s does not exists", stuId));
    }

    private static void getStudents() {
        Optional<List<Student>> students = DBUtil.getStudents();

        if (students.isPresent() && !students.get().isEmpty()) logger.info(String.format("%s", students.get()));
        else logger.warn("There are no students in the school");
    }

    private static void getCourses() {
        logger.info("Enter student id");
        int id = sc.nextInt();

        if(DBUtil.getStudent(id).isPresent()) {
            Optional<List<Course>> courses = DBUtil.getCourses(id);

            if (courses.isPresent() && !courses.get().isEmpty()) logger.info(String.format("%s", courses.get()));
            else logger.warn(String.format("Student with id %s has no registered courses", id));
        }
        else logger.warn(String.format("Student with id %s does not exists", id));
    }

    private static void getTeachers() {
        Optional<List<Teacher>> teachers = DBUtil.getTeachers();

        if (teachers.isPresent() && !teachers.get().isEmpty()) logger.info(String.format("%s", teachers.get()));
        else logger.warn("There are no teachers in the school");
    }

    private static void getTeacher() {
        logger.info("Enter student id");
        int id = sc.nextInt();

        if (DBUtil.getStudent(id).isPresent()) {
            Optional<Teacher> teacher = DBUtil.getTeacher(id);

            if (teacher.isPresent()) logger.info(String.format("%s", teacher.get()));
            logger.warn(String.format("Student with id %s has no assigned teacher", id));
        }
        else logger.warn(String.format("Student with id %s does not exists", id));
    }
}
