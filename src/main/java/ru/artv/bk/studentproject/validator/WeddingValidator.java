package ru.artv.bk.studentproject.validator;

import ru.artv.bk.studentproject.domain.wedding.AnswerWedding;
import ru.artv.bk.studentproject.domain.StudentOrder;

public class WeddingValidator {
    public AnswerWedding checkWedding(StudentOrder so) {
        System.out.println("WeddingValidator.checkWedding");
        return new AnswerWedding();
    }
}
