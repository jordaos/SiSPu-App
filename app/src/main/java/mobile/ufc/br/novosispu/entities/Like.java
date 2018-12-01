package mobile.ufc.br.novosispu.entities;

public class Like {
    String key;
    String userKey;
    String demandKey;

    public Like() {
    }

    public Like(String userKey, String demandKey) {
        this.userKey = userKey;
        this.demandKey = demandKey;
    }

    public Like(String key, String userKey, String demandKey) {
        this.key = key;
        this.userKey = userKey;
        this.demandKey = demandKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getDemandKey() {
        return demandKey;
    }

    public void setDemandKey(String demandKey) {
        this.demandKey = demandKey;
    }
}
