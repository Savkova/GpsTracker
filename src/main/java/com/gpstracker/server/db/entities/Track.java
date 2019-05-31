package com.gpstracker.server.db.entities;

public class Track {

    private Integer id;
    private String name;
    private long starttimeServer;
    private long endtimeServer;
    private Integer userId;

    public Track(String name, long starttimeServer, long endtimeServer, Integer userId) {
        this.name = name;
        this.userId = userId;
        this.starttimeServer = starttimeServer;
        this.endtimeServer = endtimeServer;
    }

    public Track(Integer id, String name, long starttimeServer, long endtimeServer, Integer userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.starttimeServer = starttimeServer;
        this.endtimeServer = endtimeServer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public long getStarttimeServer() {
        return starttimeServer;
    }

    public void setStarttimeServer(long starttimeServer) {
        this.starttimeServer = starttimeServer;
    }

    public long getEndtimeServer() {
        return endtimeServer;
    }

    public void setEndtimeServer(long endtimeServer) {
        this.endtimeServer = endtimeServer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Track{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", starttimeServer=").append(starttimeServer);
        sb.append(", endtimeServer=").append(endtimeServer);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
