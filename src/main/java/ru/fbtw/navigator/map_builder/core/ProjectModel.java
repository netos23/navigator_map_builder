package ru.fbtw.navigator.map_builder.core;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Callback;

import java.util.Set;

public class ProjectModel {
    private Long id;
    private String name;
    private Set<Platform> platforms;
    private String telegramName;
    private String telegramApiKey;
    private String appName;
    private String userPackage;
    private String body;

    public ProjectModel(){

    }

    public ProjectModel(
            String name,
            Set<Platform> platforms,
            String telegramName,
            String telegramApiKey,
            String appName,
            String userPackage,
            String body) {
        this.name = name;
        this.platforms = platforms;
        this.telegramName = telegramName;
        this.telegramApiKey = telegramApiKey;
        this.appName = appName;
        this.userPackage = userPackage;
        this.body = body;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Platform> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(Set<Platform> platforms) {
        this.platforms = platforms;
    }

    public String getTelegramName() {
        return telegramName;
    }

    public void setTelegramName(String telegramName) {
        this.telegramName = telegramName;
    }

    public String getTelegramApiKey() {
        return telegramApiKey;
    }

    public void setTelegramApiKey(String telegramApiKey) {
        this.telegramApiKey = telegramApiKey;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserPackage() {
        return userPackage;
    }

    public void setUserPackage(String userPackage) {
        this.userPackage = userPackage;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
