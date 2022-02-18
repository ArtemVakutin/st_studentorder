package ru.artv.bk.studentproject.test;

import ru.artv.bk.studentproject.domain.children.AnswerChildren;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalField;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        long l = System.currentTimeMillis();
        Thread.sleep(1111);
        System.out.println(System.currentTimeMillis()-l);
        System.out.println(System.currentTimeMillis());
    }
}
