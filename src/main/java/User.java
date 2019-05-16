public class User {
    private Long id;
    private Double latitude;
    private Double longtitude;
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + id +
            ", latitude=" + latitude +
            ", longtitude=" + longtitude +
            '}';
    }
}
