package com.gpstracker.server.db.entities;

public class TrackPoint {
    private int id;
    private int trackId;
    private double latitude;
    private double longitude;
    private long gpsTime;

    public TrackPoint(int trackId, double latitude, double longitude, long gpsTime) {
        this.trackId = trackId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsTime = gpsTime;
    }

    public TrackPoint(int id, int trackId, double latitude, double longitude, long gpsTime) {
        this.id = id;
        this.trackId = trackId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gpsTime = gpsTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTrackId() {
        return trackId;
    }

    public void setTrackId(int trackId) {
        this.trackId = trackId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public long getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(long gpsTime) {
        this.gpsTime = gpsTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TrackPoint{");
        sb.append("id=").append(id);
        sb.append(", trackId=").append(trackId);
        sb.append(", latitude=").append(latitude);
        sb.append(", longitude=").append(longitude);
        sb.append(", gpsTime=").append(gpsTime);
        sb.append('}');
        return sb.toString();
    }
}
