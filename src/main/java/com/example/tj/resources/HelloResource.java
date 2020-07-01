package com.example.tj.resources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.template.soy.jbcsrc.api.SoySauce;
import org.jdbi.v3.core.Jdbi;
import org.jooq.DSLContext;
import org.jooq.conf.ParamType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Duration;
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

    private boolean isAdmin(String token) {
        var sql = create.select(field("id"))
                .from(table("admin"))
                .where(field("hash").eq(token))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list());
        return !result.isEmpty();
    }

    @GET
    public String index() {
        var sql = create.select(field("name"))
                .from(table("foo"))
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list());
        return templates.renderTemplate("examples.simple.index")
                .setData(Map.of("name", result.get(0).get("name")))
                .renderHtml()
                .get()
                .toString();
    }

    @POST
    public String postIndex(
            @FormParam("name") String newName,
            @FormParam("secret") String secret,
            @CookieParam("token") String token) {
        if (token == null) {
            throw new WebApplicationException("Corrupt input 1");
        }
        if (!token.equals(secret)) {
            throw new WebApplicationException("Corrupt input 2");
        }
        if (isAdmin(token)) {
            throw new WebApplicationException("Corrupt input 3");
        }
        var sql = create.update(table("name"))
                .set(field("name"), newName)
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        jdbi.withHandle(handle -> {
            handle.execute(sql);
            return null;
        });
        return index();
    }

    @Path("/signin")
    @GET
    public String signin() {
        return templates.renderTemplate("examples.simple.signin")
                .renderHtml()
                .get()
                .toString();
    }

    @Path("/callback")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response callback(@FormParam("token") String idToken) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new WebApplicationException("Id token verification failed.");
        }
        return Response.seeOther(URI.create("https://tj-hello.herokuapp.com/"))
                .cookie(new NewCookie("token", decodedToken.getUid(),
                        null, null, null,
                        (int) Duration.ofDays(365).toSeconds(), true, true))
                .build();
    }

    @Path("/admin")
    @GET
    public String admin(@CookieParam("token") String token) {
        if (token == null) {
            throw new NotAuthorizedException("Please sign in first");
        }
        if (isAdmin(token)) {
            throw new NotAuthorizedException("Requires admin right");
        }
        var sql = create.select(field("name"))
                .from(table("foo"))
                .where(field("id").eq(1))
                .getSQL(ParamType.INLINED);
        var result = jdbi.withHandle(handle -> handle.createQuery(sql)
                .mapToMap()
                .list());
        return templates.renderTemplate("examples.simple.admin")
                .setData(Map.of("name", result.get(0).get("name")))
                .renderHtml()
                .get()
                .toString();
    }
}
