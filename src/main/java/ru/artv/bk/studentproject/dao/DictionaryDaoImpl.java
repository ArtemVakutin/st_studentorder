package ru.artv.bk.studentproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.artv.bk.studentproject.config.Config;
import ru.artv.bk.studentproject.domain.CountryArea;
import ru.artv.bk.studentproject.domain.PassportOffice;
import ru.artv.bk.studentproject.domain.RegisterOffice;
import ru.artv.bk.studentproject.domain.Street;
import ru.artv.bk.studentproject.exception.DaoException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class DictionaryDaoImpl implements DictionaryDao{
    private static final String GET_STREET ="SELECT street_code, street_name" +
            " FROM jc_street WHERE UPPER(street_name) LIKE UPPER (?);";

    public static final String GET_PASSPORT_OFFICE = "SELECT *" +
            " FROM jc_passport_office WHERE p_office_area_id = ?;";

    public static final String GET_REGISTER_OFFICE = "SELECT *" +
            " FROM jc_register_office WHERE r_office_area_id = ?;";

    private static final String GET_AREA = "SELECT *" +
            "FROM jc_country_struct WHERE area_id LIKE ? AND area_id <> ?;";

    private static final Logger logger = LoggerFactory.getLogger(DictionaryDaoImpl.class);

    public List<Street> findStreets(String pattern) throws DaoException {

        List<Street> result = new LinkedList<>();
        try (Connection con = ConnectionBuilder.getConnection();
            PreparedStatement stm = con.prepareStatement(GET_STREET)) {
            stm.setString(1, "%" + pattern + "%");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(new Street(rs.getLong(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<RegisterOffice> findRegisterOffice(String areaId) throws DaoException {
        logger.info("RegisterOffice : {}", areaId);
        List<RegisterOffice> result = new LinkedList<>();
        try (Connection con = ConnectionBuilder.getConnection();
             PreparedStatement stm = con.prepareStatement(GET_REGISTER_OFFICE)) {
            stm.setString(1, areaId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(new RegisterOffice(rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<PassportOffice> findPassportOffice(String areaId) throws DaoException {
        logger.info("PassportOffice : {}", areaId);
        List<PassportOffice> result = new LinkedList<>();
        try (Connection con = ConnectionBuilder.getConnection();
             PreparedStatement stm = con.prepareStatement(GET_PASSPORT_OFFICE)) {
            stm.setString(1, areaId);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(new PassportOffice(rs.getLong(1),
                        rs.getString(2),
                        rs.getString(3)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
        return result;
    }

    @Override
    public List<CountryArea> findArea(String areaId) throws DaoException, SQLException {
        logger.info("FindArea : {}", areaId);
        List<CountryArea> result = new LinkedList<>();

        try (Connection con = ConnectionBuilder.getConnection();
             PreparedStatement stm = con.prepareStatement(GET_AREA)) {
            String param1 = buildParam(areaId);
            String param2 = areaId;
            stm.setString(1, param1);
            stm.setString(2, param2);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                result.add(new CountryArea(rs.getString(1), rs.getString(2)));
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }
        return result;
    }

    private String buildParam(String areaId) throws SQLException {
        if (areaId == null || areaId.trim().isEmpty()) {
            return "__0000000000";
        } else if(areaId.endsWith("0000000000")){
            return (areaId.substring(0,2) + "___0000000");
        } else if(areaId.endsWith("0000000")) {
            return (areaId.substring(0,5)+"___0000");
        } else if((areaId.endsWith("0000"))){
            return (areaId.substring(0,8)+"____");
        }
        throw new SQLException("Invalid parameter areaId : " + areaId);
    }
}
