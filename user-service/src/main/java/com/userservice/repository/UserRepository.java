package com.userservice.repository;

import com.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * Created by Achim Timis on 7/6/2016.
 */
public interface UserRepository extends JpaRepository<User,Long> {
//
//    @RestResource(path="by-name")
//    List<User> findByName(@Param("name") String name);

}
