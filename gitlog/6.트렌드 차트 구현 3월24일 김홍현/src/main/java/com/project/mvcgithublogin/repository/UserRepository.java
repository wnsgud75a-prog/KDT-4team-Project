package com.project.mvcgithublogin.repository;

import com.project.mvcgithublogin.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserRepository{
   int save(User user);
   Optional<User> find(@Param("id") String id, @Param("pw") String pw);
   int delete(@Param("id") String id);
}
