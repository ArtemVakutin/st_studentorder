package ru.artv.bk.studentproject.domain;

public class Street {
    private long streetCode;
    private String streetName;

    public Street() {
    }

    public Street(long code, String streetName) {
        this.streetCode = code;
        this.streetName = streetName;
    }

    public long getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(long streetCode) {
        this.streetCode = streetCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @Override
    public String toString() {
        return "Street{" +
                "code=" + streetCode +
                ", streetName='" + streetName + '\'' +
                '}';
    }
}
