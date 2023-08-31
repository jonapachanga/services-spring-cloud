package com.jd.dev.app.auth.clients;

import com.jd.dev.app.user.commons.models.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserFeignClient {
    @GetMapping("/users/search/find-by-username")
    User findByUsername(@RequestParam String username);

    @PutMapping("/users/{id}")
    User update(@RequestBody User user, @PathVariable Long id);
}
