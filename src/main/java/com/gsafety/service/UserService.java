package com.gsafety.service;

import com.gsafety.entity.User;
import com.gsafety.repository.UserRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Liugan
 * @date 2017/2/15
 * User业务逻辑
 */
@Service
public class UserService {
	@Autowired
	private UserRepositoty userRepositoty;

	/**
	 * 根据用户姓名查找用户
	 *
	 * @param name
	 * @return User对象
	 */
	public User findUserByName(String name) {
		User user = null;
		try {
			user = userRepositoty.findByUserName(name);
		} catch (Exception e) {
		}
		return user;
	}

	/**
	 * 根据用户邮箱查询用户
	 *
	 * @param email
	 * @return User对象
	 */
	public User findUserByEmail(String email) {
		User user = null;
		try {
			user = userRepositoty.findByUserEmail(email);
		} catch (Exception e) {
		}
		return user;
	}

	/**
	 * 根据用户id查询用户
	 * @param userId 用户id
	 * @return User对象
	 */
	public User	findByUserId(String userId){
		User user = null;
		try {
			user = userRepositoty.findByUserId(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}


	/**
	 * 保存用户
	 *
	 * @param user
	 */
	public void saveUser(User user) {
		userRepositoty.save(user);
	}
}
