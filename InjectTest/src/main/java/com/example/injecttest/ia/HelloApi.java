package com.example.injecttest.ia;

import com.example.injecttest.ab.Interactor;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class HelloApi{

    @Inject
    Interactor interactor;

    @GET
    public String handle() {

        return interactor.handle("API");
    }

}