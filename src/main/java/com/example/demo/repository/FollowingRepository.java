package com.example.demo.repository;

import com.example.demo.entity.Following;
import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowingRepository extends JpaRepository<Following, Long> {

    List<Following> findByFollow(Users users);

    boolean existsByFollowerAndFollow(Users follower, Users follow);
}
