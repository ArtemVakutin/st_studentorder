package ru.artv.bk.studentproject.validator.register;

import ru.artv.bk.studentproject.domain.cityregister.CityRegisterResponse;
import ru.artv.bk.studentproject.domain.Person;
import ru.artv.bk.studentproject.exception.CityRegisterException;
import ru.artv.bk.studentproject.exception.TransportException;

public interface CityRegisterChecker {
    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException;
}
