package com.tapioca.BE.domain.model.project;

public class GitHub {
    private final String teamCode;
    private final String repoUrl;
    private final boolean isPrivate;
    private final String accessToken;
    private final String defaultBranch;

    public GitHub(
            String teamCode, String repoUrl, boolean isPrivate,
            String accessToken, String defaultBranch
    ) {
        this.teamCode = teamCode;
        this.repoUrl = repoUrl;
        this.isPrivate = isPrivate;
        this.accessToken = accessToken;
        this.defaultBranch = defaultBranch;
    }

    public String getTeamCode() { return teamCode; }
    public String getRepoUrl() { return repoUrl; }
    public boolean isPrivate() { return isPrivate; }
    public String getAccessToken() { return accessToken; }
    public String getDefaultBranch() { return defaultBranch; }
}
