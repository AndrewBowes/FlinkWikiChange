package com.flutter.flunkwiki.model;

public class WikiChangeDetail {

    private final int id;
    private final String type;
    private final String user;

    private final boolean bot;

    private final String serverName;
    private final long timestamp;

    public WikiChangeDetail(int id, String type, String user, boolean bot, String serverName, long timestamp) {
        this.id = id;
        this.type = type;
        this.user = user;
        this.bot = bot;
        this.serverName = serverName;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    public boolean isBot() {
        return bot;
    }

    public String getServerName() {
        return serverName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "WikiChangeDetail{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", user='" + user + '\'' +
                ", bot=" + bot +
                ", serverName='" + serverName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
