package com.jd.dev.app.auth.services;

import com.jd.dev.app.user.commons.models.entity.User;

public interface UserService {
    User findByUsername(String username);

    User update(User user, Long id);
}
