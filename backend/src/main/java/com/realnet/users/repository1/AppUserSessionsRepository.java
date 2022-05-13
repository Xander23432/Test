package com.realnet.users.repository1;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.realnet.users.entity1.AppUserSessions;
import com.realnet.users.entity1.AppUserSessionsCompositeKey;

@Repository
public interface AppUserSessionsRepository extends JpaRepository<AppUserSessions,AppUserSessionsCompositeKey>{

}
