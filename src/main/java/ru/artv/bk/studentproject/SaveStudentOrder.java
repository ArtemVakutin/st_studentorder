package ru.artv.bk.studentproject;

import ru.artv.bk.studentproject.dao.DictionaryDaoImpl;
import ru.artv.bk.studentproject.dao.StudentOrderImpl;
import ru.artv.bk.studentproject.dao.StudentOrderDao;
import ru.artv.bk.studentproject.dao.StudentOrderImplTwoSelect;
import ru.artv.bk.studentproject.domain.*;
import ru.artv.bk.studentproject.exception.DaoException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SaveStudentOrder {
    public static void main(String[] args) throws DaoException, SQLException {
        String s = "Траляля";
        DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();
        List<Street> streets = dictionaryDao.findStreets("прос");
//        for (Street street : streets) {
//            System.out.println(street);
//        }
//
//        List<PassportOffice> passport = dictionaryDao.findPassportOffice("010010000000");
//        for (PassportOffice passportOffice : passport) {
//            System.out.println(passportOffice);
//        }
//
//        List<RegisterOffice> register = dictionaryDao.findRegisterOffice("010020000000");
//        for (RegisterOffice registerOffice : register) {
//            System.out.println(registerOffice);
//        }
//        List<CountryArea> countryAreas = dictionaryDao.findArea(null);
//        for (CountryArea countryArea : countryAreas) {
//            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
//        }
//        List<CountryArea> countryAreas1 = dictionaryDao.findArea("020000000000");
//        for (CountryArea countryArea : countryAreas1) {
//            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
//        }
//        List<CountryArea> countryAreas2 = dictionaryDao.findArea("020020000000");
//        for (CountryArea countryArea : countryAreas2) {
//            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
//        }
//        List<CountryArea> countryAreas3 = dictionaryDao.findArea("020020020000");
//        for (CountryArea countryArea : countryAreas3) {
//            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
//        }
//        List<CountryArea> countryAreas4 = dictionaryDao.findArea("020020020002");
//        for (CountryArea countryArea : countryAreas4) {
//            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
//        }
    StudentOrder so = buildStudentOrder(10);
        StudentOrderDao soDao = new StudentOrderImpl();
    soDao.saveStudentOrder(so);
        List<StudentOrder> soList = soDao.getStudentOrders();
        for (StudentOrder so1 : soList) {
            System.out.println(so1);
        }
    }

//
    public static StudentOrder buildStudentOrder(int id) {
        StudentOrder so = new StudentOrder();

        so.setStudentOrderId(id);
        so.setMarriageCertificateId("" + (123456000 + id));
        so.setMarriageDate(LocalDate.of(2016, 7, 4));

        RegisterOffice ro = new RegisterOffice(1L, "", "");
        so.setMarriageOffice(ro);

        Street street = new Street(1L, "First street");

        Address address = new Address("195000", street, "12", "", "142");

        // Муж
        Adult husband = new Adult("Петров", "Виктор", "Сергеевич", LocalDate.of(1997, 8, 24));
        husband.setPassportSeria("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setIssueDate(LocalDate.of(2017, 9, 15));
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husband.setIssueDepartment(po1);
        husband.setStudentId("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentId("HH12345");

        // Жена
        Adult wife = new Adult("Петрова", "Вероника", "Алекссевна", LocalDate.of(1998, 3, 12));
        wife.setPassportSeria("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setIssueDate(LocalDate.of(2018, 4, 5));
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setIssueDepartment(po2);
        wife.setStudentId("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(1L, ""));
        wife.setStudentId("WW12345");

        // Ребенок
        Child child1 = new Child("Петрова", "Ирина", "Викторовна", LocalDate.of(2018, 6, 29));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setIssueDate(LocalDate.of(2018, 6, 11));
        RegisterOffice ro2 = new RegisterOffice(2L, "", "");
        child1.setIssueDepartment(ro2);
        child1.setAddress(address);
        // Ребенок
        Child child2 = new Child("Петров", "Евгений", "Викторович", LocalDate.of(2018, 6, 29));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setIssueDate(LocalDate.of(2018, 7, 19));
        RegisterOffice ro3 = new RegisterOffice(3L, "", "");
        child2.setIssueDepartment(ro3);
        child2.setAddress(address);

        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);
        return so;
    }
}
