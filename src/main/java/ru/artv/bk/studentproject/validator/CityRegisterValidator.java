package ru.artv.bk.studentproject.validator;

import ru.artv.bk.studentproject.domain.Person;
import ru.artv.bk.studentproject.domain.cityregister.AnswerCityRegister;
import ru.artv.bk.studentproject.domain.Child;
import ru.artv.bk.studentproject.domain.cityregister.AnswerCityRegisterItem;
import ru.artv.bk.studentproject.domain.StudentOrder;
import ru.artv.bk.studentproject.domain.cityregister.CityRegisterResponse;
import ru.artv.bk.studentproject.exception.CityRegisterException;
import ru.artv.bk.studentproject.exception.TransportException;
import ru.artv.bk.studentproject.validator.register.CityRegisterChecker;
import ru.artv.bk.studentproject.validator.register.FakeCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator {

    private CityRegisterChecker registerChecker;
    public final static String NO_GRN = "No_GRN";

    public CityRegisterValidator() {
        registerChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister ans = new AnswerCityRegister();


        ans.addAnswerCityRegisterItems(personChecker(so.getHusband()));
        ans.addAnswerCityRegisterItems(personChecker(so.getWife()));
        List<Child> childList = so.getChild();
        for (Child ch : childList) {
            ans.addAnswerCityRegisterItems(personChecker(ch));
        }
        return ans;
    }

    private AnswerCityRegisterItem personChecker(Person person) {
        AnswerCityRegisterItem.CityStatus status = null;
        AnswerCityRegisterItem.CityError error = null;
        try {
            CityRegisterResponse response = registerChecker.checkPerson(person);
            status = response.isExisting() ?
                    AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;
                  }
        catch (CityRegisterException e) {
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(e.getCode(), e.getMessage());
            e.printStackTrace(System.out);
                 }
        catch (TransportException e) {
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(NO_GRN, e.getMessage());
            e.printStackTrace(System.out);

        }
        System.out.println("Make ITEM");
        return new AnswerCityRegisterItem(person, status, error);
    }
}
