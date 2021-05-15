package com.example.tj.resources;

import com.auth0.client.auth.AuthAPI;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.template.soy.jbcsrc.api.SoySauce;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Duration;
import java.util.Map;

@Path("/signin")
@Produces(MediaType.TEXT_HTML)
public class SigninResource {
    private final AuthAPI auth;
    private final JWTVerifier verifier;
    private final SoySauce templates;

    public SigninResource(AuthAPI auth, JWTVerifier verifier, SoySauce templates) {
        this.auth = auth;
        this.verifier = verifier;
        this.templates = templates;
    }

    @GET
    public String index(@Context UriInfo uriInfo) {
        final var authUrl = auth.authorizeUrl(uriInfo.getBaseUriBuilder().path("/signin/callback").build().toString())
                .withScope("openid")
                .withResponseType("id_token")
                .build() + "&response_mode=form_post&nonce=deadbeef";
        return templates.renderTemplate("examples.simple.signin")
                .setData(Map.of(
                        "authUrl", authUrl
                ))
                .renderHtml()
                .get()
                .toString();
    }

    @Path("/callback")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    public Response callback(@FormParam("id_token") String idToken, @Context UriInfo uriInfo) {
        final DecodedJWT jwt;
        try {
            jwt = verifier.verify(idToken);
        } catch (JWTVerificationException exception) {
            throw new WebApplicationException(exception);
        }
        return Response.seeOther(uriInfo.getBaseUriBuilder().path("/").build())
                .cookie(new NewCookie("token", jwt.getSubject(),
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
