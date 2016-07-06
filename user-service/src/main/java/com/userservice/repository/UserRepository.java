package com.userservice.repository;

import com.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Achim Timis on 7/6/2016.
 */
public interface UserRepository extends JpaRepository<User,Long> {
}
