package com.example.ie_um.community.domain.repository;

import com.example.ie_um.community.domain.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
}