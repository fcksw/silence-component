package com.silence.mvc.batch;

public class DefaultEventService implements EventService {
    @Override
    public void onEvent() {
        System.out.println("default");
    }
}
