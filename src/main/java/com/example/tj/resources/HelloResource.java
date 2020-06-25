package com.example.tj.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
@Produces(MediaType.TEXT_HTML)
public class HelloResource {
    @GET
    public String getHello() {
        return "<body>Hello";
    }
}
