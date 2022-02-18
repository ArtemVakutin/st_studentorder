package ru.artv.bk.studentproject.validator.register;

import ru.artv.bk.studentproject.domain.cityregister.CityRegisterResponse;
import ru.artv.bk.studentproject.domain.Person;
import ru.artv.bk.studentproject.exception.CityRegisterException;
import ru.artv.bk.studentproject.exception.TransportException;

public class RealCityRegisterChecker implements CityRegisterChecker{
    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {
        throw new CityRegisterException("1","Ashipka");
//        return null;
    }
}
