package ru.artv.bk.studentproject.dao;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.artv.bk.studentproject.domain.CountryArea;
import ru.artv.bk.studentproject.domain.PassportOffice;
import ru.artv.bk.studentproject.domain.RegisterOffice;
import ru.artv.bk.studentproject.domain.Street;
import ru.artv.bk.studentproject.exception.DaoException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DictionaryDaoImplTest {
    private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImplTest.class);

    @BeforeClass
    public static void startUp() throws IOException, URISyntaxException {
        DBInit.initDB();
    }
//    @Before //starting before every test
//    @After //after ending every test
//    @AfterClass //after ending all tests

    @Test
    public void findStreets() throws DaoException {
        LocalDateTime dt = LocalDateTime.now();
        logger.info("TesT {}", dt); //doing without concatenation

        DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();
        List<Street> streets = dictionaryDao.findStreets("прос");
        Assert.assertTrue(streets.size() == 2);
        Assert.assertEquals(streets.size(), 2);
    }

    @Test
    public void findRegisterOffice() throws DaoException {
        DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();
        List<RegisterOffice> register = dictionaryDao.findRegisterOffice("010020000000");
        assertTrue(register.size()==1);
    }

    @Test
    public void findPassportOffice() throws DaoException {
        DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();
        List<PassportOffice> passport = dictionaryDao.findPassportOffice("010010000000");
        assertTrue(passport.size()==2);
    }

    @Test(expected = DaoException.class)
    public void findArea() throws SQLException, DaoException {
        DictionaryDaoImpl dictionaryDao = new DictionaryDaoImpl();

        List<CountryArea> countryAreas1 = dictionaryDao.findArea("020000000000");
        for (CountryArea countryArea : countryAreas1) {
            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
        }
        assertTrue(countryAreas1.size()==2);
        List<CountryArea> countryAreas2 = dictionaryDao.findArea("020020000000");
        for (CountryArea countryArea : countryAreas2) {
            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
        }
        assertTrue(countryAreas2.size()==2);

        List<CountryArea> countryAreas3 = dictionaryDao.findArea("020020020000");
        for (CountryArea countryArea : countryAreas3) {
            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
        }
        assertTrue(countryAreas2.size()==2);

        List<CountryArea> countryAreas4 = dictionaryDao.findArea("020020020002");
        for (CountryArea countryArea : countryAreas4) {
            System.out.println(countryArea.getAreaId() + " : " +countryArea.getAreaName());
        }

    }
}