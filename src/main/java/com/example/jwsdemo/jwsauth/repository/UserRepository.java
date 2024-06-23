package com.example.jwsdemo.jwsauth.repository;

import com.example.jwsdemo.jwsauth.model.UserInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserInfo , Long> {
    public UserInfo findByUserName(String userName);
}
