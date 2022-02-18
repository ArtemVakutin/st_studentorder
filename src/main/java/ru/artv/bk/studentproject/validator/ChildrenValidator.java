package ru.artv.bk.studentproject.validator;

import ru.artv.bk.studentproject.domain.children.AnswerChildren;
import ru.artv.bk.studentproject.domain.StudentOrder;

public class ChildrenValidator {
    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("ChildrenValidator.checkChildren");
        return new AnswerChildren();
    }
}
