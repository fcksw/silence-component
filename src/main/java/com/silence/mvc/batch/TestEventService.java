package com.silence.mvc.batch;

public class TestEventService implements EventService{
    @Override
    public void onEvent() {
        System.out.println("test");
    }
}
