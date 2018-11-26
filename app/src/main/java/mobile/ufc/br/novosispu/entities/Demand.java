package mobile.ufc.br.novosispu.entities;

public class Demand {
    private String key;
    private long lat;
    private long lng;
    private long time;
    private String description;
    private String title;
    private String userKey;

    public Demand(String key, long lat, long lng, long time, String description, String title, String userKey) {
        this.key = key;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.description = description;
        this.title = title;
        this.userKey = userKey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
}
