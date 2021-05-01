package com.example.tj.resources;

import com.google.template.soy.jbcsrc.api.SoySauce;
import org.jdbi.v3.core.Jdbi;
import org.jooq.DSLContext;
import org.jooq.conf.ParamType;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HelloResource {
    private final Jdbi jdbi;
    private final DSLContext create;
    private final SoySauce templates;

    public HelloResource(Jdbi jdbi, DSLContext create, SoySauce templates) {
        this.jdbi = jdbi;
        this.create = create;
        this.templates = templates;
    }

    private boolean isNotAdmin(String token) {
        var sql = create.select(field("id"))
                .from(table("admin"))
                .where(field("hash").eq(token))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list());
        return result.isEmpty();
    }

    @GET
    public String index(@CookieParam("token") String token) {
        var sql = create.select(field("name"), field("image"))
                .from(table("foo"))
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list())
                .get(0);
        result.put("signed", token != null);
        return templates.renderTemplate("examples.simple.index")
                .setData(result)
                .renderHtml()
                .get()
                .toString();
    }

    @Path("/admin")
    @GET
    public String admin(@CookieParam("token") String token) {
        if (token == null) {
            throw new NotAuthorizedException("No token");
        }
        if (isNotAdmin(token)) {
            throw new NotAuthorizedException("You don't have access right");
        }
        var sql = create.select(field("name"), field("image"))
                .from(table("foo"))
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list());
        return templates.renderTemplate("examples.simple.admin")
                .setData(Map.of(
                        "name", result.get(0).get("name"),
                        "image", result.get(0).get("image"),
                        "secret", token
                ))
                .renderHtml()
                .get()
                .toString();
    }

    @Path("/update")
    @POST
    public Response update(
            @FormParam("name") String name,
            @FormParam("image") String image,
            @FormParam("secret") String secret,
            @CookieParam("token") String token,
            @Context UriInfo uriInfo) {
        if (token == null) {
            throw new WebApplicationException("Corrupt input 1");
        }
        if (!token.equals(secret)) {
            throw new WebApplicationException("Corrupt input 2");
        }
        if (isNotAdmin(token)) {
            throw new WebApplicationException("Corrupt input 3");
        }
        var sql = create.update(table("foo"))
                .set(field("name"), name)
                .set(field("image"), image)
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        jdbi.withHandle(handle -> {
            handle.execute(sql);
            return null;
        });
        return Response.seeOther(uriInfo.getBaseUriBuilder().path("/").build())
                .build();
    }
}
