package com.example.injecttest.ab;

public class InteractorImpl implements Interactor {
    @Override
    public String handle(String a) {
        return "Hello "+ a;
    }
}
