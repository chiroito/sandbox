package com.example.injecttest.ab;

public class InteractorMock implements Interactor {
    @Override
    public String handle(String a) {
        return "Hello " + a + " from Mock";
    }
}
