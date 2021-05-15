package com.example.tj;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;

import javax.validation.Valid;
import javax.validation.constraints.*;

public class HelloTjConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private String authDomain;

    @Valid
    @NotNull
    private String authClientId;

    @Valid
    @NotNull
    private String authClientSecret;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("authDomain")
    public String getAuthDomain() {
        return authDomain;
    }

    @JsonProperty("authDomain")
    public void setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
    }

    @JsonProperty("authClientId")
    public String getAuthClientId() {
        return authClientId;
    }

    @JsonProperty("authClientId")
    public void setAuthClientId(String authClientId) {
        this.authClientId = authClientId;
    }

    @JsonProperty("authClientSecret")
    public String getAuthClientSecret() {
        return authClientSecret;
    }

    @JsonProperty("authClientSecret")
    public void setAuthClientSecret(String authClientSecret) {
        this.authClientSecret = authClientSecret;
    }
}
