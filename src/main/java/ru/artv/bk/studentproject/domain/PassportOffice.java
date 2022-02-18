package ru.artv.bk.studentproject.domain;

public class PassportOffice {
    private long officeId;
    private String areaId;
    private String officeName;

    public PassportOffice() {
    }

    public PassportOffice(long officeId, String areaId, String officeName) {
        this.officeId = officeId;
        this.areaId = areaId;
        this.officeName = officeName;
    }

    public long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(long officeId) {
        this.officeId = officeId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    @Override
    public String toString() {
        return "PassportOffice{" +
                "officeId=" + officeId +
                ", areaId='" + areaId + '\'' +
                ", officeName='" + officeName + '\'' +
                '}';
    }
}
