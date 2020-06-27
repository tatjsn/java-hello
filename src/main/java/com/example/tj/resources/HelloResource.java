package com.example.tj.resources;

import org.jdbi.v3.core.Jdbi;
import org.jooq.DSLContext;
import org.jooq.conf.ParamType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class HelloResource {
    private final Jdbi jdbi;
    private final DSLContext create;

    public HelloResource(Jdbi jdbi, DSLContext create) {
        this.jdbi = jdbi;
        this.create = create;
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
        return "<body>Hello " + result.get(0).get("name");
    }
}
