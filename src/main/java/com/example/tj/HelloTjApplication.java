package com.example.tj;

import io.dropwizard.Application;
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
        // TODO: implement application
    }

}
