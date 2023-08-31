package com.jd.dev.app.users.models.repository;

import com.jd.dev.app.user.commons.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Long> {
    @RestResource(path = "find-by-username")
    User findByUsername(String username);
}
