package mobile.ufc.br.novosispu.entities;

public class Like {
    private String key;
    private String userKey;
    private String demandKey;
    private boolean notified;

    public Like() {
    }

    public Like(String userKey, String demandKey) {
        this.userKey = userKey;
        this.demandKey = demandKey;
        this.notified = false;
    }

    public Like(String key, String userKey, String demandKey) {
        this.key = key;
        this.userKey = userKey;
        this.demandKey = demandKey;
        this.notified = false;
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

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
