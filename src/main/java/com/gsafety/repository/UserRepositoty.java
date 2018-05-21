package com.gsafety.repository;

import com.gsafety.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Liugan
 * @date 2017/2/15
 * User表操作接口
 */
@Repository
public interface UserRepositoty extends JpaRepository<User, String> {

	/**
	 * 根据邮箱查找用户
	 *
	 * @param userEmail 用户邮箱
	 * @return 用户对象
	 */
	@Query("select t from User t where t.userEmail = :userEmail")
	User findByUserEmail(@Param("userEmail") String userEmail);

	/**
	 * 根据姓名查找用户
	 *
	 * @param userName 用户姓名
	 * @return 用户对象
	 */
	@Query("select t from User t where t.userName = :userName")
	User findByUserName(@Param("userName") String userName);

	/**
	 * 根据id查找用户
	 *
	 * @param userId 用户Id
	 * @return 用户对象
	 */
	@Query("select t from User t where t.userId = :userId")
	User findByUserId(@Param("userId") String userId);


}
