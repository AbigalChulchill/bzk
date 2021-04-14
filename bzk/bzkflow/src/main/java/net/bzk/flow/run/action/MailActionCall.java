package net.bzk.flow.run.action;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.MailAction;
import net.bzk.flow.model.var.VarValSet;

@Service("net.bzk.flow.model.Action$MailAction")
@Scope("prototype")
@Slf4j
public class MailActionCall extends ActionCall<MailAction> {



	
	public MailActionCall() {
		super(MailAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		sendMail(getModel().getToMail(), getModel().getSubject(), getModel().getBody());
		return new VarValSet();
	}
	
	
	public void sendMail(String tos, String sub, String text) {

		Properties props = new Properties();
		props.put("mail.smtp.host", getModel().getSmtpHost());
		props.put("mail.smtp.socketFactory.port", getModel().getSmtpPort()+"");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", getModel().getSmtpPort()+"");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.debug", "true");
		props.put("mail.mime.charset", "UTF-8");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getModel().getUsername(), getModel().getPassword());
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(getModel().getUsername()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(tos));
			message.setSubject(sub, "UTF-8");
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();

			// 設定郵件內容的型態為 text/html
			mbp.setContent(text, "text/html;charset=UTF-8");
			mp.addBodyPart(mbp);
			message.setContent(mp);

			Transport.send(message);
			System.out.println("Done");

			logUtils.logActionCall(getUids(), "Send Mail subject:"+sub+" content:"+ text);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}	

}
