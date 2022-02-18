package ru.artv.bk.studentproject.validator.register;

import ru.artv.bk.studentproject.domain.*;
import ru.artv.bk.studentproject.domain.cityregister.CityRegisterResponse;
import ru.artv.bk.studentproject.exception.CityRegisterException;
import ru.artv.bk.studentproject.exception.TransportException;

public class FakeCityRegisterChecker implements CityRegisterChecker {

    final static String GOOD_1 = "100000";
    final static String GOOD_2 = "200000";
    final static String BAD_1 = "100001";
    final static String BAD_2 = "200001";
    final static String ERROR_1 = "100002";
    final static String ERROR_2 = "200002";
    final static String ERROR_T_1 = "100003";
    final static String ERROR_T_2 = "200003";



    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {
        CityRegisterResponse cityRegResp = new CityRegisterResponse();
        if (person instanceof Adult) {

            Adult t = (Adult) person;
            String passNumber = ((Adult) person).getPassportNumber();
            if (passNumber.equals(GOOD_1)||passNumber.equals(GOOD_2)){

                cityRegResp.setExisting(true);
                cityRegResp.setTemporal(false);

            }
            if (passNumber.equals(BAD_1)||passNumber.equals(BAD_2)){
                cityRegResp.setExisting(false);
                cityRegResp.setTemporal(false);
            }
            if (passNumber.equals(ERROR_1)||passNumber.equals(ERROR_2)){
                throw new CityRegisterException("1", "СityRegisterException " + t.getPassportSeria());
            }
            if (passNumber.equals(ERROR_T_1)||passNumber.equals(ERROR_T_2)){
                throw new TransportException("Transport ERROR "  +  t.getPassportSeria());
            } else {
                cityRegResp.setExisting(true);
                cityRegResp.setTemporal(false);
            }


        } else if (person instanceof Child) {
            cityRegResp.setExisting(true);
            cityRegResp.setTemporal(true);
        }
        return cityRegResp;
    }
}
