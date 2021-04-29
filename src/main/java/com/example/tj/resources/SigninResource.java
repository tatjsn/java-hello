package com.example.tj.resources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.template.soy.jbcsrc.api.SoySauce;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.time.Duration;

@Path("/signin")
@Produces(MediaType.TEXT_HTML)
public class SigninResource {
    private static final URI ENTRY = URI.create("https://tj-hello.herokuapp.com/");
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
    public Response callback(@FormParam("token") String idToken) {
        FirebaseToken decodedToken;
        try {
            decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException e) {
            throw new WebApplicationException("Id token verification failed.");
        }
        return Response.seeOther(ENTRY)
                .cookie(new NewCookie("token", decodedToken.getUid(),
                        "/", null, null,
                        (int) Duration.ofDays(365).toSeconds(), true, true))
                .build();
    }
}
