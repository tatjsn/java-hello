package com.example.tj;

import com.example.tj.resources.HelloResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

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
        // TODO: application initialization
    }

    @Override
    public void run(final HelloTjConfiguration configuration,
                    final Environment environment) {
        final var factory = new JdbiFactory();
        final var jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final var resource = new HelloResource(jdbi);
        environment.jersey().register(resource);
    }

}
