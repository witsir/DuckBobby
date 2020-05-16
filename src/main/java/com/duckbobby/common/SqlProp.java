package com.duckbobby.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix ="mysqlconfig")
public class SqlProp {
    String sqlUsername;
    String sqlUserPassword;

    public String getSqlUsername() {
        return sqlUsername;
    }

    public void setSqlUsername(String sqlUsername) {
        this.sqlUsername = sqlUsername;
    }

    public String getSqlUserPassword() {
        return sqlUserPassword;
    }

    public void setSqlUserPassword(String sqlUserPassword) {
        this.sqlUserPassword = sqlUserPassword;
    }
}
