package ru.artv.bk.studentproject.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.artv.bk.studentproject.config.Config;
import ru.artv.bk.studentproject.domain.*;
import ru.artv.bk.studentproject.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentOrderImpl implements StudentOrderDao {

    private static final Logger logger = LoggerFactory.getLogger(StudentOrderImpl.class);

    public static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            " student_order_status, student_order_date, h_sur_name, " +
            " h_given_name, h_patronymic, h_date_of_birth, h_passport_seria, " +
            " h_passport_number, h_passport_date, h_passport_office_id, h_post_index, " +
            " h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
            " w_sur_name, w_given_name, w_patronymic, w_date_of_birth, w_passport_seria, " +
            " w_passport_number, w_passport_date, w_passport_office_id, w_post_index, " +
            " w_street_code, w_building, w_extension, w_apartment, w_university_id, w_student_number, " +
            " certificate_id, register_office_id, marriage_date)" +
            " VALUES (?, ?, ?, " +
            " ?, ?, ?, ?, " +
            " ?, ?, ?, ?, " +
            " ?, ?, ?, ?, ?, ?, " +
            " ?, ?, ?, ?, ?, " +
            " ?, ?, ?, ?, " +
            " ?, ?, ?, ?, ?, ?, " +
            " ?, ?, ?);";

    public static final String INSERT_CHILD = "INSERT INTO public.jc_student_child(" +
            "student_order_id, c_sur_name, c_given_name," +
            " c_patronymic, c_date_of_birth, c_certificate_number," +
            " c_certificate_date, c_register_office_id, c_post_index," +
            " c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public static final String SELECT_SO =
            "SELECT so.*, ro.r_office_area_id, ro.r_office_name, " +
                    "po_h.p_office_area_id as h_p_office_area_id, " +
                    "po_h.p_office_name as h_p_office_name, " +
                    "po_w.p_office_area_id as w_p_office_area_id, " +
                    "po_w.p_office_name as w_p_office_name " +
                    "FROM jc_student_order so " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = so.register_office_id " +
                    "INNER JOIN jc_passport_office po_h ON po_h.p_office_id = so.h_passport_office_id " +
                    "INNER JOIN jc_passport_office po_w ON po_w.p_office_id = so.w_passport_office_id " +
                    "WHERE student_order_status = ? ORDER BY student_order_date LIMIT ?";

    private static final String SELECT_CHILD =
            "SELECT soc.*, ro.r_office_area_id, ro.r_office_name " +
                    "FROM jc_student_child soc " +
                    "INNER JOIN jc_register_office ro ON ro.r_office_id = soc.c_register_office_id " +
                    "WHERE soc.student_order_id IN ";

    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {
        logger.info("Student Order : {}", so);
        Long id = -1l;
        try (Connection con = ConnectionBuilder.getConnection();
             PreparedStatement stm = con.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"})) {
            con.setAutoCommit(false);
            try {
                // Header
                stm.setInt(1, StudentOrderStatus.START.ordinal());
                stm.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

                // Husband and Wife
                setAdultParameters(stm, 3, so.getHusband());
                setAdultParameters(stm, 18, so.getWife());

                // Marriage
                stm.setString(33, so.getMarriageCertificateId());
                stm.setLong(34, so.getMarriageOffice().getOfficeId());
                stm.setDate(35, Date.valueOf(so.getMarriageDate()));

                stm.executeUpdate();
                ResultSet generatedKeys = stm.getGeneratedKeys();
                while (generatedKeys.next()) {
                    System.out.println(generatedKeys.getLong(1));
                    id = generatedKeys.getLong(1);
                }
                saveChildren(con, so, id);
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                logger.error(e.getMessage(), e);
                throw e;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }

        return id;
    }

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();

        try (Connection con = ConnectionBuilder.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_SO)) {
            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            stmt.setInt(2, Integer.parseInt(Config.getProperty(Config.DB_LIMIT)));
            ResultSet rs = stmt.executeQuery();
            List<Long> ids= new LinkedList<>();

            while(rs.next()) {
                StudentOrder so = new StudentOrder();

                fillStudentOrder(rs, so);
                fillMarriage(rs, so);

                Adult husband = fillAdult(rs, "h_");
                Adult wife = fillAdult(rs, "w_");
                so.setHusband(husband);
                so.setWife(wife);

                result.add(so);
                ids.add(so.getStudentOrderId());
            }

            if(ids.isEmpty() != true) {
                StringBuilder sb = new StringBuilder("(");
                for (Long id : ids) {
                    sb.append(id).append(",");
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append(")");
                String m = sb.toString();
                }
            findChildren(con, result);
            rs.close();


        } catch(SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DaoException(e);
        }

        return result;
    }

    private void findChildren(Connection con, List<StudentOrder> result) throws SQLException {
        String collect = "(" + result.stream().map(so -> String.valueOf(so.getStudentOrderId()))
                .collect(Collectors.joining(",")) + ")";
        try (PreparedStatement stmt = con.prepareStatement(SELECT_CHILD + collect)){
            ResultSet rs = stmt.executeQuery();
            Map<Long, StudentOrder> mapSo = result.stream().collect(Collectors.toMap(so -> so.getStudentOrderId(), so -> so));
            while(rs.next()){
                Child child = fillChild(rs);
                mapSo.get(rs.getLong("student_order_id")).addChild(child);
            }
        }

    }

    private Child fillChild(ResultSet rs) throws SQLException {

        Child child = new Child();
        child.setSurName(rs.getString("c_sur_name"));
        child.setGivenName(rs.getString("c_given_name"));
        child.setPatronymic(rs.getString("c_patronymic"));
        child.setDateOfBirth(rs.getDate("c_date_of_birth").toLocalDate());
        child.setCertificateNumber(rs.getString("c_certificate_number"));
        child.setIssueDate(rs.getDate("c_certificate_date").toLocalDate());

        Long roId = rs.getLong("c_register_office_id");
        String roArea = rs.getString("r_office_area_id");
        String roName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roArea, roName);
        child.setIssueDepartment(ro);

        Address adr = new Address();
        Street st = new Street(rs.getLong("c_street_code"), "");
        adr.setStreet(st);
        adr.setPostCode(rs.getString("c_post_index"));
        adr.setBuilding(rs.getString("c_building"));
        adr.setExtension(rs.getString("c_extension"));
        adr.setApartment(rs.getString("c_apartment"));
        child.setAddress(adr);

        return child;
    }

    private Adult fillAdult(ResultSet rs, String pref) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(pref + "sur_name"));
        adult.setGivenName(rs.getString(pref + "given_name"));
        adult.setPatronymic(rs.getString(pref + "patronymic"));
        adult.setDateOfBirth(rs.getDate(pref + "date_of_birth").toLocalDate());
        adult.setPassportSeria(rs.getString(pref + "passport_seria"));
        adult.setPassportNumber(rs.getString(pref + "passport_number"));
        adult.setIssueDate(rs.getDate(pref + "passport_date").toLocalDate());

        PassportOffice po = new PassportOffice();
        po.setOfficeId(rs.getLong(pref + "passport_office_id"));
        po.setAreaId(rs.getString(pref + "p_office_area_id"));
        po.setOfficeName(rs.getString(pref+"p_office_name"));
        adult.setIssueDepartment(po);
        Address adr = new Address();
        Street st = new Street(rs.getLong(pref + "street_code"), "");
        adr.setStreet(st);
        adr.setPostCode(rs.getString(pref + "post_index"));
        adr.setBuilding(rs.getString(pref + "building"));
        adr.setExtension(rs.getString(pref + "extension"));
        adr.setApartment(rs.getString(pref + "apartment"));
        adult.setAddress(adr);

        University uni = new University(rs.getLong(pref + "university_id"), "");
        adult.setUniversity(uni);
        adult.setStudentId(rs.getString(pref + "student_number"));

        return adult;
    }

    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderId(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        so.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));
    }

    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());
        Long roId = rs.getLong("register_office_id");
        RegisterOffice ro = new RegisterOffice();
        ro.setOfficeId(rs.getLong("register_office_id"));
        ro.setAreaId(rs.getString("r_office_area_id"));
        ro.setAreaId(rs.getString("r_office_name"));
        so.setMarriageOffice(ro);
    }

    private void saveChildren(Connection con, StudentOrder so, Long id) {

        try (PreparedStatement stm = con.prepareStatement(INSERT_CHILD)) {
            for (Child children : so.getChild()) {
                setChildrenParameters(children, stm, so, id);
                stm.addBatch(); //Накапливает запросы
            }
            stm.executeBatch(); //Выполняет накопленные запросы
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }


    }

    private void setChildrenParameters(Child child, PreparedStatement stm, StudentOrder so, Long id) throws SQLException {
        int i = 1;
        stm.setLong(i++, id);
        stm.setString(i++, child.getSurName());
        stm.setString(i++, child.getGivenName());
        stm.setString(i++, child.getPatronymic());
        stm.setDate(i++, Date.valueOf(child.getDateOfBirth()));
        stm.setString(i++, child.getCertificateNumber());
        stm.setDate(i++, Date.valueOf(child.getIssueDate()));
        stm.setLong(i++, child.getIssueDepartment().getOfficeId());
        Address address = child.getAddress();
        stm.setString(i++, address.getPostCode());
        stm.setLong(i++, address.getStreet().getStreetCode());
        stm.setString(i++, address.getBuilding());
        stm.setString(i++, address.getExtension());
        stm.setString(i++, address.getApartment());
    }

    private void setAdultParameters(PreparedStatement stm, int i, Adult adult) throws SQLException {
        stm.setString(i++, adult.getSurName());
        stm.setString(i++, adult.getGivenName());
        stm.setString(i++, adult.getPatronymic());
        stm.setDate(i++, Date.valueOf(adult.getDateOfBirth()));
        stm.setString(i++, adult.getPassportSeria());
        stm.setString(i++, adult.getPassportNumber());
        stm.setDate(i++, Date.valueOf(adult.getIssueDate()));
        stm.setLong(i++, adult.getIssueDepartment().getOfficeId());
        Address address = adult.getAddress();
        stm.setString(i++, address.getPostCode());
        stm.setLong(i++, address.getStreet().getStreetCode());
        stm.setString(i++, address.getBuilding());
        stm.setString(i++, address.getExtension());
        stm.setString(i++, address.getApartment());
        University university = adult.getUniversity();
        stm.setLong(i++, university.getUniversityId());
        stm.setString(i++, adult.getStudentId());
    }
}
