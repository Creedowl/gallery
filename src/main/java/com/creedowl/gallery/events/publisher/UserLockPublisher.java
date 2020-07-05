package com.creedowl.gallery.events.publisher;

import com.creedowl.gallery.events.event.UserLockEvent;
import com.creedowl.gallery.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class UserLockPublisher {
    private final ApplicationContext applicationContext;

    public UserLockPublisher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void publish(User user) {
        this.applicationContext.publishEvent(new UserLockEvent(this, user));
    }
}
