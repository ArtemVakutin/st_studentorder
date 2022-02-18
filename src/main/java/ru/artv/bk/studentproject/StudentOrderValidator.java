package ru.artv.bk.studentproject;

import ru.artv.bk.studentproject.dao.StudentOrderDao;
import ru.artv.bk.studentproject.dao.StudentOrderImpl;
import ru.artv.bk.studentproject.domain.*;
import ru.artv.bk.studentproject.domain.children.AnswerChildren;
import ru.artv.bk.studentproject.domain.cityregister.AnswerCityRegister;
import ru.artv.bk.studentproject.domain.cityregister.AnswerCityRegisterItem;
import ru.artv.bk.studentproject.domain.student.AnswerStudent;
import ru.artv.bk.studentproject.domain.wedding.AnswerWedding;
import ru.artv.bk.studentproject.exception.DaoException;
import ru.artv.bk.studentproject.mail.MailSender;
import ru.artv.bk.studentproject.validator.ChildrenValidator;
import ru.artv.bk.studentproject.validator.CityRegisterValidator;
import ru.artv.bk.studentproject.validator.StudentValidator;
import ru.artv.bk.studentproject.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private ChildrenValidator childrenValidator = new ChildrenValidator();
    private CityRegisterValidator cityRegisterValidator = new CityRegisterValidator();
    private StudentValidator studentValidator = new StudentValidator();
    private WeddingValidator weddingValidator = new WeddingValidator();
    private MailSender mailSender = new MailSender();

    public static void main(String[] args) {
        new StudentOrderValidator().checkAll();
    }

    private void checkAll() {
        List<StudentOrder> studentOrderList = null;
        try {
            studentOrderList = readStudentOrder();
        } catch (DaoException e) {
            e.printStackTrace();
        }
        for (StudentOrder studentOrder : studentOrderList) {
            checkOneOrder(studentOrder);
        }
    }

    private void checkOneOrder(StudentOrder so) {
        AnswerCityRegister answerCityRegister = checkCityRegister(so);
//        AnswerStudent answerStudent = checkStudent(so);
//        AnswerChildren answerChildren = checkChildren(so);
//        AnswerWedding answerWedding = checkWedding(so);
//        sendMail(so);
        List<AnswerCityRegisterItem> listItem = answerCityRegister.getAnswerCityRegisterItems();
        for (AnswerCityRegisterItem answerCityRegisterItem : listItem) {
            System.out.println(answerCityRegisterItem.toString());
        }
    }


    private static List<StudentOrder> readStudentOrder() throws DaoException {
        return new StudentOrderImpl().getStudentOrders();
    }

    private AnswerCityRegister checkCityRegister(StudentOrder so) {
        return cityRegisterValidator.checkCityRegister(so);
    }

    private AnswerStudent checkStudent(StudentOrder so) {
        return studentValidator.checkStudent(so);
    }

    private AnswerChildren checkChildren(StudentOrder so) {
        return childrenValidator.checkChildren(so);
    }

    private AnswerWedding checkWedding(StudentOrder so) {
        return weddingValidator.checkWedding(so);
    }

    private MailSender sendMail(StudentOrder so) {
        System.out.println("StudentOrderValidator.sendMail");
        return mailSender.sendMail(so);
    }
}
