package ru.fbtw.navigator.map_builder.core;

import java.util.HashSet;
import java.util.Set;

public class ProjectModelBuilder {
    private String name;
    private Set<Platform> platformSet;
    private String telegramName;
    private String telegramApiKey;
    private String appName;
    private String userPackage;
    private String body;

    public ProjectModelBuilder() {
        name = "";
        platformSet = new HashSet<>();
        telegramName = "";
        telegramApiKey = "";
        appName = "";
        userPackage = "";
        body = "";
    }

    public ProjectModelBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ProjectModelBuilder setPlatformsSet(Set<Platform> platformSet) {
        this.platformSet = platformSet;
        return this;
    }

    public ProjectModelBuilder setTelegramName(String telegramName) {
        this.telegramName = telegramName;
        return this;
    }

    public ProjectModelBuilder setTelegramApiKey(String telegramApiKey) {
        this.telegramApiKey = telegramApiKey;
        return this;
    }

    public ProjectModelBuilder addPlatform(Platform platform) {
        platformSet.add(platform);
        return this;
    }

    public ProjectModelBuilder setAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public ProjectModelBuilder setUserPackage(String userPackage) {
        this.userPackage = userPackage;
        return this;
    }

    public ProjectModelBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public ProjectModel createProjectModel() {
        return new ProjectModel(
                name,
                platformSet,
                telegramName,
                telegramApiKey,
                appName,
                userPackage,
                body
        );
    }
}