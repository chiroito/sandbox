package com.example.injecttest.ia;

import com.example.injecttest.ab.Interactor;
import com.example.injecttest.ab.InteractorMock;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;

@Dependent
public class InteractorMockProducer {

    @Produces
    public Interactor interactor = new InteractorMock();
}
