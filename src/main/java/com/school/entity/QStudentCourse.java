package com.school.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QStudentCourse is a Querydsl query type for StudentCourse
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudentCourse extends EntityPathBase<StudentCourse> {

    private static final long serialVersionUID = -418683240L;

    public static final QStudentCourse studentCourse = new QStudentCourse("studentCourse");

    public final NumberPath<Integer> courseId = createNumber("courseId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> studentId = createNumber("studentId", Integer.class);

    public QStudentCourse(String variable) {
        super(StudentCourse.class, forVariable(variable));
    }

    public QStudentCourse(Path<? extends StudentCourse> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStudentCourse(PathMetadata metadata) {
        super(StudentCourse.class, metadata);
    }

}

