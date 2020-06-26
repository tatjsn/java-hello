package com.example.tj.resources;

import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HelloResource {
    private final Jdbi jdbi;

    public HelloResource(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @GET
    public String getHello() {
        var result = jdbi.withHandle(handle -> handle.createQuery("select * from foo where id = 1")
                .mapToMap()
                .list());
        return "<body>Hello " + result.get(0).get("name");
    }
}
