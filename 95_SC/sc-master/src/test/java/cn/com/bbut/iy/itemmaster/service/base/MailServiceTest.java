package cn.com.bbut.iy.itemmaster.service.base;

import cn.com.bbut.iy.itemmaster.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = Application.class)
public class MailServiceTest extends AbstractTestNGSpringContextTests {

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private JavaMailSender javaMailSender;

	@Test
	public void sendSimpleMail() throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo("chengzhi9453@163.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");
		javaMailSender.send(message);
	}

}