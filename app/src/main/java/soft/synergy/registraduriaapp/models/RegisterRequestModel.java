package soft.synergy.registraduriaapp.models;

public class RegisterRequestModel {

    private String pollingStationCode;

    private String standCode;

    private long totalPolls;

    public RegisterRequestModel(String pollingStationCode, String standCode, long totalPolls) {
        this.pollingStationCode = pollingStationCode;
        this.standCode = standCode;
        this.totalPolls = totalPolls;
    }

    public RegisterRequestModel() {
    }

    public String getPollingStationCode() {
        return pollingStationCode;
    }

    public void setPollingStationCode(String pollingStationCode) {
        this.pollingStationCode = pollingStationCode;
    }

    public String getStandCode() {
        return standCode;
    }

    public void setStandCode(String standCode) {
        this.standCode = standCode;
    }

    public long getTotalPolls() {
        return totalPolls;
    }

    public void setTotalPolls(long totalPolls) {
        this.totalPolls = totalPolls;
    }
}
