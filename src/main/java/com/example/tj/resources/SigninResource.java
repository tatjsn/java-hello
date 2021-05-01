package com.example.tj.resources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.template.soy.jbcsrc.api.SoySauce;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.time.Duration;

@Path("/signin")
@Produces(MediaType.TEXT_HTML)
public class SigninResource {
    private final SoySauce templates;

    public SigninResource(SoySauce templates) {
        this.templates = templates;
    }

    @GET
    public String index() {
        return templates.renderTemplate("examples.simple.signin")
                .renderHtml()
                .get()
                .toString();
    }

    @Path("/callback")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response callback(@FormParam("token") String idToken, @Context UriInfo uriInfo) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new WebApplicationException("Id token verification failed.");
        }
        return Response.seeOther(uriInfo.getBaseUriBuilder().path("/").build())
                .cookie(new NewCookie("token", decodedToken.getUid(),
                        "/", null, null,
                        (int) Duration.ofDays(365).toSeconds(), true, true))
                .build();
    }

    @Path("/signout")
    @GET
    public Response signout(@Context UriInfo uriInfo) {
        return Response.ok()
                .entity(templates.renderTemplate("examples.simple.signout")
                        .renderHtml()
                        .get()
                        .toString())
                .cookie(new NewCookie("token", null,
                        "/", null, null,
                        0, true, true))
                .build();
    }
}
