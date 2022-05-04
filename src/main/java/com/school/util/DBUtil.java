package com.school.util;

import com.google.common.base.Optional;
import com.querydsl.jpa.impl.JPAQuery;
import com.school.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DBUtil {
    private static final EntityManager em = HibernateUtil.getSessionFactory().openSession().getEntityManagerFactory().createEntityManager();

    public static void addCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("MATHS"));
        courses.add(new Course("ENGLISH"));
        courses.add(new Course("FINE ART"));
        courses.add(new Course("FURTHER MATHS"));
        courses.add(new Course("AGRIC"));
        courses.add(new Course("BIOLOGY"));
        courses.add(new Course("CHEMISTRY"));
        courses.add(new Course("PHYSICS"));
        courses.add(new Course("CIVIC"));
        courses.add(new Course("GEOGRAPHY"));

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            courses.forEach(session::save);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void addTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(new Teacher("teacher1"));
        teachers.add(new Teacher("teacher2"));
        teachers.add(new Teacher("teacher3"));
        teachers.add(new Teacher("teacher4"));
        teachers.add(new Teacher("teacher5"));
        teachers.add(new Teacher("teacher6"));
        teachers.add(new Teacher("teacher7"));
        teachers.add(new Teacher("teacher8"));
        teachers.add(new Teacher("teacher9"));
        teachers.add(new Teacher("teacher10"));

        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            teachers.forEach(session::save);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Optional<Student> addStudent(String name) {
        List<Teacher> teachers = getTeachers().get();
        int randomIndex = new Random().nextInt(teachers.size());
        int randomTeacherId = teachers.get(randomIndex).getId();

        Student student = new Student(name, randomTeacherId);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int savedId = (int) session.save(student);
            student = session.get(Student.class, savedId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return Optional.fromNullable(student);
    }

    public static Optional<StudentCourse> addCourse(int stuId, int courseId) {
        if (!getStudent(stuId).isPresent() || !getCourse(courseId).isPresent()) return Optional.fromNullable(null);

        StudentCourse studentCourse = new StudentCourse(stuId, courseId);
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            int savedId = (int) session.save(studentCourse);
            studentCourse = session.get(StudentCourse.class, savedId);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return Optional.fromNullable(studentCourse);
    }

    public static Optional<Student> getStudent(int id) {
        JPAQuery<Student> query = new JPAQuery<>(em);

        QStudent qStudent = QStudent.student;

        return Optional.fromNullable(query.from(qStudent)
                .where(qStudent.id.eq(id))
                .fetchOne());
    }

    public static Optional<Course> getCourse(int id) {
        JPAQuery<Course> query = new JPAQuery<>(em);

        QCourse qCourse = QCourse.course;

        return Optional.fromNullable(query.from(qCourse)
                .where(qCourse.id.eq(id))
                .fetchOne());
    }

    public static Optional<List<Student>> getStudents() {
        JPAQuery<Student> query = new JPAQuery<>(em);

        QStudent qStudent = QStudent.student;

        return Optional.fromNullable(query.from(qStudent)
                .fetch());
    }

    public static Optional<List<Course>> getCourses(int id) {
        JPAQuery<StudentCourse> queryStudentCourse = new JPAQuery<>(em);
        JPAQuery<Course> queryCourse = new JPAQuery<>(em);

        QStudentCourse qStudentCourse = QStudentCourse.studentCourse;
        QCourse qCourse = QCourse.course;

        return Optional.fromNullable(queryCourse.from(qCourse)
                .where(qCourse.id.in(
                        queryStudentCourse.select(qStudentCourse.courseId)
                                .from(qStudentCourse)
                                .where(qStudentCourse.studentId.eq(id))
                                .fetchAll())
                ).fetch());
    }

    public static Optional<List<Teacher>> getTeachers() {
        JPAQuery<Teacher> query = new JPAQuery<>(em);

        QTeacher qTeacher = QTeacher.teacher;

        return Optional.fromNullable(query.from(qTeacher)
                .fetch());
    }

    public static Optional<Teacher> getTeacher(int id) {
        JPAQuery<Student> queryStudent = new JPAQuery<>(em);
        JPAQuery<Teacher> queryTeacher = new JPAQuery<>(em);

        QStudent qStudent = QStudent.student;
        QTeacher qTeacher = QTeacher.teacher;

        Optional<Student> student = Optional.fromNullable(queryStudent.from(qStudent)
                .where(qStudent.id.eq(id))
                .fetchOne());
        if (!student.isPresent()) return Optional.absent();

        Optional<Teacher> result = Optional.fromNullable(queryTeacher.from(qTeacher)
                .where(qTeacher.id.eq(student.get().getTeacherId()))
                .fetchOne());

        return result;
    }
}
