package com.example.injecttest.ia;

import com.example.injecttest.ab.Interactor;
import com.example.injecttest.ab.InteractorImpl;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class InteractorProducer {

    @Produces
    public Interactor interactor = new InteractorImpl();
}
