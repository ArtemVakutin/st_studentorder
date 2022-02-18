package ru.artv.bk.studentproject.validator;

import ru.artv.bk.studentproject.domain.student.AnswerStudent;
import ru.artv.bk.studentproject.domain.StudentOrder;

public class StudentValidator {
    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("StudentValidator.checkStudent");
        return new AnswerStudent();
    }
}
