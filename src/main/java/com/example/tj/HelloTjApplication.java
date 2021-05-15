package com.example.tj;

import com.auth0.client.auth.AuthAPI;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.tj.resources.HelloResource;
import com.example.tj.resources.SigninResource;
import com.google.template.soy.jbcsrc.api.SoySauceBuilder;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.IOException;

public class HelloTjApplication extends Application<HelloTjConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HelloTjApplication().run(args);
    }

    @Override
    public String getName() {
        return "HelloTj";
    }

    @Override
    public void initialize(final Bootstrap<HelloTjConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets"));
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
    }

    @Override
    public void run(final HelloTjConfiguration configuration,
                    final Environment environment) throws IOException {
        final var auth = new AuthAPI(configuration.getAuthDomain(), configuration.getAuthClientId(), configuration.getAuthClientSecret());
        final var algorithm = Algorithm.HMAC256(configuration.getAuthClientSecret());
        final var verifier = JWT.require(algorithm)
                .withIssuer("https://" + configuration.getAuthDomain() + "/")
                .build();
        final var factory = new JdbiFactory();
        final var jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final var create = DSL.using(SQLDialect.POSTGRES);
        final var templates = new SoySauceBuilder().build();
        environment.jersey().register(new HelloResource(jdbi, create, templates));
        environment.jersey().register(new SigninResource(auth, verifier, templates));
    }

}
