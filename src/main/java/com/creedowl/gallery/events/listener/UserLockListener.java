package com.creedowl.gallery.events.listener;

import com.creedowl.gallery.events.event.UserLockEvent;
import com.creedowl.gallery.mapper.UploadFIleMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserLockListener {
    private final UploadFIleMapper uploadFIleMapper;

    public UserLockListener(UploadFIleMapper uploadFIleMapper) {
        this.uploadFIleMapper = uploadFIleMapper;
    }

    @EventListener
    public void onApplicationEvent(UserLockEvent event) {
        this.uploadFIleMapper.updateByUserId(event.getUser().getId(), event.getUser().getLocked());
    }
}
