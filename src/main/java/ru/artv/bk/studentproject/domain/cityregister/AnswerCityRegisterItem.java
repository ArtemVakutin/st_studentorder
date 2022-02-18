package ru.artv.bk.studentproject.domain.cityregister;

import ru.artv.bk.studentproject.domain.Person;

public class AnswerCityRegisterItem {
    public enum CityStatus{
        YES, NO, ERROR;
    }
    public static class CityError{
        private String error;
        private String textError;

        public CityError(String error, String textError) {
            this.error = error;
            this.textError = textError;
        }

        public String getError() {
            return error;
        }

        public String getTextError() {
            return textError;
        }

        @Override
        public String toString() {
            return "CityError{" +
                    "error='" + error + '\'' +
                    ", textError='" + textError + '\'' +
                    '}';
        }
    }
    private Person person;
    private CityStatus cityStatus;
    private CityError cityError;

    public AnswerCityRegisterItem(Person person, CityStatus cityStatus) {
        this.person = person;
        this.cityStatus = cityStatus;
    }

    public AnswerCityRegisterItem(Person person, CityStatus cityStatus, CityError cityError) {
        this.person = person;
        this.cityStatus = cityStatus;
        this.cityError = cityError;
    }

    public Person getPerson() {
        return person;
    }

    public CityStatus getCityStatus() {
        return cityStatus;
    }

    public CityError getCityError() {
        return cityError;
    }

    @Override
    public String toString() {
        return "AnswerCityRegisterItem{" +
                "person=" + person +
                ", cityStatus=" + cityStatus +
                ", cityError=" + cityError +
                '}';
    }
}
