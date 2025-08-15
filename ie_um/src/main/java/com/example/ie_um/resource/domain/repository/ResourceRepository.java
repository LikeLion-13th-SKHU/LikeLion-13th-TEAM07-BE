package com.example.ie_um.resource.domain.repository;

import com.example.ie_um.resource.domain.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
