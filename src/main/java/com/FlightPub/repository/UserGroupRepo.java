package com.FlightPub.repository;


import com.FlightPub.model.UserGroup;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserGroupRepo extends MongoRepository<UserGroup, String> {
    @Query(value="{ 'UserIDs' : ?0 }")
    List<UserGroup> findAllByUserIDs(String userIDs);

    @Query(value="{ 'InvitedIds' : ?0 }")
    List<UserGroup> findAllByInvitedIds(String userIDs);
}
