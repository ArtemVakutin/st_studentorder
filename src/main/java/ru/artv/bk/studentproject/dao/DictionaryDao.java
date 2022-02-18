package ru.artv.bk.studentproject.dao;

import ru.artv.bk.studentproject.domain.CountryArea;
import ru.artv.bk.studentproject.domain.PassportOffice;
import ru.artv.bk.studentproject.domain.RegisterOffice;
import ru.artv.bk.studentproject.domain.Street;
import ru.artv.bk.studentproject.exception.DaoException;

import java.sql.SQLException;
import java.util.List;

public interface DictionaryDao {
    public List<Street> findStreets(String pattern) throws DaoException;
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException;
    public List<PassportOffice> findPassportOffice(String areaId) throws DaoException;
    public List<CountryArea> findArea(String areaId) throws DaoException, SQLException;
}
