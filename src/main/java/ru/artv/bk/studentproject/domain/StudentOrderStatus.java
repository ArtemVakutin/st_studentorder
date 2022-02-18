package ru.artv.bk.studentproject.domain;

public enum StudentOrderStatus {
    START, CHECKED;

    public static StudentOrderStatus fromValue(int value){
        for (StudentOrderStatus status : StudentOrderStatus.values()) {
            if(value == status.ordinal()) {
                return status;
            }
        }
        throw new RuntimeException("Value not found : " + value);
    }


}
