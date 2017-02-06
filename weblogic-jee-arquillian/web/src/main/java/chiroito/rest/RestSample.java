package chiroito.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/sample")
public class RestSample {

    @Path("/id")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getId() {
        return "abcdefg";
    }
}
