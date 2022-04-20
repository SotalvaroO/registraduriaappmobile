package soft.synergy.registraduriaapp.models;

public class StandModel {

    private String code;

    public StandModel(String code) {
        this.code = code;
    }

    public StandModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }
}
