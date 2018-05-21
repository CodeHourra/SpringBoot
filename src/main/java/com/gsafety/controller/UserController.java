package com.gsafety.controller;

import com.google.gson.Gson;
import com.gsafety.entity.RequestData;
import com.gsafety.entity.User;
import com.gsafety.repository.UserRepositoty;
import com.gsafety.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Liugan
 * @date 2017/2/15
 * User控制层
 * 注意:
 * 在controller上加注解@Controller 和@RestController都可以在前端调通接口，
 * 但是二者的区别在于，当用前者的时候在方法上必须添加注解@ResponseBody，
 * 如果不添加@ResponseBody，就会报上面错误，因为当使用@Controller 注解时，
 * spring默认方法返回的是view对象（页面）。而加上@ResponseBody，则方法返回的就是具体对象了。
 * @RestController的作用就相当于@Controller+@ResponseBody的结合体
 */

@Controller
@ResponseBody
@RequestMapping(value = "user")
public class UserController {
	/**
	 * 自动装配
	 */
	@Autowired
	private UserService userService;
	/**
	 * 邮件发送
	 */
	@Autowired
	private JavaMailSender javaMailSender;
	/**
	 * 邮件模版引擎
	 */
	@Autowired
	private TemplateEngine templateEngine;

	/**
	 * 从配置文件中获取用户名
	 */
	@Value("${spring.mail.username}")
	private String sender;

	/**
	 * 创建返回值对象
	 */
	private RequestData requestData = new RequestData();


	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(User user) {
		User u = userService.findUserByEmail(user.getUserEmail());
		if (u != null && u.getUserPass().equals(user.getUserPass()) && u.getUserEmail().equals(user.getUserEmail())) {
			if (u.getState() == 0) {
				requestData.setCode("9999");
				requestData.setState("500");
				requestData.setMessage("请前往" + user.getUserEmail() + "邮箱激活");
				return new Gson().toJson(requestData);
			}
			requestData.setMessage("/static/success.html");
			return new Gson().toJson(requestData);
		}
		requestData.setCode("9999");
		requestData.setState("500");
		requestData.setMessage("邮箱或者密码错误");
		return new Gson().toJson(requestData);
	}

	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(User user) {
		if (userService.findUserByEmail(user.getUserEmail()) != null) {
			requestData.setMessage("该邮箱已经注册!");
		} else {
			// 发送注册邮件
			sendTemplateMail(user.getUserEmail(), user.getUserId());
			// 保存User
			userService.saveUser(user);
			requestData.setMessage("注册成功, 快去激活");
		}
		return new Gson().toJson(requestData);
	}

	@RequestMapping(value = "forget", method = RequestMethod.POST)
	public String forget(User user) {
		User u = userService.findUserByEmail(user.getUserEmail());
		if (u != null || u.getUserEmail() == user.getUserEmail()) {
			u.setUserPass("6666");
			userService.saveUser(u);
			requestData.setMessage("密码已经重置，快去查看你的邮箱");
			sendSimpleEmail(u.getUserEmail(), "您好，您密码已重置，初始密码：6666，" +
					"为了你的安全请尽快修改密码。");
			return new Gson().toJson(requestData);
		}
		requestData.setCode("9999");
		requestData.setState("500");
		requestData.setMessage("无效邮箱");
		return new Gson().toJson(requestData);
	}

	@RequestMapping(value = "activation/{userId}", method = RequestMethod.GET)
	public void activation(@PathVariable String userId, HttpServletResponse response) throws IOException {
		User user = userService.findByUserId(userId);
		if (user != null) {
			user.setState(1);
			//将用户状态修改为激活
			userService.saveUser(user);
		}
		response.sendRedirect("/static/login.html");
	}

	public void sendSimpleEmail(String recipient, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		// 发送者
		message.setFrom(sender);
		// 接收者
		message.setTo(recipient);
		//邮件主题
		message.setSubject("密码重置邮件");
		// 邮件内容
		message.setText(text);
		javaMailSender.send(message);
	}

	public void sendTemplateMail(String recipient, String userId) {
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(recipient);
			helper.setSubject("验证邮件");
			Context context = new Context();
			context.setVariable("id", userId);
			String emailContent = templateEngine.process("emailTemplate", context);
			helper.setText(emailContent, true);
		} catch (MessagingException e) {
			throw new RuntimeException("Messaging  Exception !", e);
		}
		javaMailSender.send(message);
	}


}
