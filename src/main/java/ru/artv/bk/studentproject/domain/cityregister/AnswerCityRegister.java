package ru.artv.bk.studentproject.domain.cityregister;

import java.util.LinkedList;
import java.util.List;

public class AnswerCityRegister {

    private List<AnswerCityRegisterItem> answerCityRegisterItems;

    public List<AnswerCityRegisterItem> getAnswerCityRegisterItems() {
        return answerCityRegisterItems;
    }

    public void addAnswerCityRegisterItems(AnswerCityRegisterItem item) {
        if (getAnswerCityRegisterItems() == null){
            answerCityRegisterItems = new LinkedList<>();
        }
        answerCityRegisterItems.add(item);
    }

}
