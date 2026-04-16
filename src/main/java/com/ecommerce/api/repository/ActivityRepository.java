package com.ecommerce.api.repository;

import com.ecommerce.api.model.UserActivity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends ReactiveCassandraRepository<UserActivity, UUID> {
}
