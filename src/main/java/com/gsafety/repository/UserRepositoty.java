package com.gsafety.repository;

import com.gsafety.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Liugan
 * @date 2017/2/15
 * User表操作接口
 */
@Repository
public interface UserRepositoty extends JpaRepository<User,Long>{

    /**
     * 根据姓名查找用户
     * @param name
     * @return
     */
    @Query("select t from User t where t.name = :name")
    User findByUserName(@Param("name") String name);
}
