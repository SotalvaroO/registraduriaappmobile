package soft.synergy.registraduriaapp.models;

import java.util.Date;

public class RegisterResponseModel {

    private String stationName;
    private String stationAddress;
    private String standNumber;
    private long totalPolls;
    private Date dateTime;

    public RegisterResponseModel(String stationName, String stationAddress, String standNumber, long totalPolls, Date dateTime) {
        this.stationName = stationName;
        this.stationAddress = stationAddress;
        this.standNumber = standNumber;
        this.totalPolls = totalPolls;
        this.dateTime = dateTime;
    }

    public RegisterResponseModel() {
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationAddress() {
        return stationAddress;
    }

    public void setStationAddress(String stationAddress) {
        this.stationAddress = stationAddress;
    }

    public String getStandNumber() {
        return standNumber;
    }

    public void setStandNumber(String standNumber) {
        this.standNumber = standNumber;
    }

    public long getTotalPolls() {
        return totalPolls;
    }

    public void setTotalPolls(long totalPolls) {
        this.totalPolls = totalPolls;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
