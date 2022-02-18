package ru.artv.bk.studentproject.dao;

import ru.artv.bk.studentproject.domain.StudentOrder;
import ru.artv.bk.studentproject.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {
    public Long saveStudentOrder(StudentOrder so) throws DaoException;
    public List<StudentOrder> getStudentOrders() throws DaoException;

}
