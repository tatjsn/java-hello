package com.example.tj;

import com.example.tj.resources.HelloResource;
import com.example.tj.resources.SigninResource;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.template.soy.jbcsrc.api.SoySauceBuilder;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.io.ByteArrayInputStream;
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
    }

    @Override
    public void run(final HelloTjConfiguration configuration,
                    final Environment environment) throws IOException {
        final var json = System.getenv("GOOG_CREDS");
        final var jsonInputStream = new ByteArrayInputStream((json.getBytes()));
        final var options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(jsonInputStream))
                .build();
        FirebaseApp.initializeApp(options);

        final var factory = new JdbiFactory();
        final var jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final var create = DSL.using(SQLDialect.POSTGRES);
        final var templates = new SoySauceBuilder().build();
        environment.jersey().register(new HelloResource(jdbi, create, templates));
        environment.jersey().register(new SigninResource(templates));
    }

}
