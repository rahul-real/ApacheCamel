package com.scheduler.batch.job.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.common.artifact.PreferenceRequest;
import com.common.artifact.PreferenceResponse;
import com.common.artifact.RegistrationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.repo.CustomerRepo;
import com.scheduler.batch.job.repo.JobSchedularRepository;

import jakarta.mail.BodyPart;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

@Service
public class JobService {

	@Autowired
	ProducerTemplate producerTemplate;

	@Autowired
	JobSchedularRepository jobSchedularRepository;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	CustomerRepo customerRepo;

	public PreferenceResponse getRegistrationData(String appTxnNum, PreferenceRequest request) throws Exception {

		PreferenceResponse response = new PreferenceResponse();

		String parId = String.join(",", request.getParid());

		List<RegistrationData> registrationData = jobSchedularRepository.getRegistrationData(appTxnNum, parId);

		response.setRegistrationData(registrationData);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime date = LocalDateTime.now();

		response.setResponseDate(date.format(formatter));

		String json = mapper.writeValueAsString(response);

		producerTemplate.sendBody("kafka:first-kafka-topic", json);

		return response;
	}

	public void addEmployeeDetails(String appTxnNum, List<Employee> employeesDetails) {

		jobSchedularRepository.addEmployee(appTxnNum, employeesDetails);

	}

	public List<Employee> getEmployeeData() {
		// TODO Auto-generated method stub
		List<Employee> employee = new LinkedList<Employee>();
		List<Object[]> response = jobSchedularRepository.getEmployeeData();
		if (response != null && !response.isEmpty()) {
			employee = response.stream().map(Employee::new).collect(Collectors.toList());
		}

		return employee;
	}

	public void sendMail(String to, String subject, String body) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setSubject(subject);
		mailMessage.setText(body);
		mailSender.send(mailMessage);
	}

	public void readLatestEmail() {
		try {
			JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
			Properties properties = mailSenderImpl.getJavaMailProperties();
			Session session = Session.getDefaultInstance(properties);

			// Connect to the mail store
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", "rahul.vodala@gmail.com", "qlaq hisd icda dajl");

			// Open the INBOX folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Get the latest message
			int messageCount = inbox.getMessageCount();
			if (messageCount > 0) {
				Message latestMessage = inbox.getMessage(messageCount);

				// Display the details of the latest email
				System.out.println("Subject: " + latestMessage.getSubject());
				System.out.println("From: " + latestMessage.getFrom()[0]);
				System.out.println("Sent Date: " + latestMessage.getSentDate());
				System.out.println("Content: " + latestMessage.getContent().toString());
			} else {
				System.out.println("No messages found in the inbox.");
			}

			// Close the inbox and store
			inbox.close(false);
			store.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readLatestEmail(String host, String user, String password) {
		try {
			String downloadPath = "C:\\Users\\syste\\Downloads\\Docs";
			Properties properties = new Properties();
			properties.put("mail.store.protocol", "imaps");
			Session emailSession = Session.getDefaultInstance(properties);

			Store store = emailSession.getStore("imaps");
			store.connect("imap.gmail.com", "rahul.vodala@gmail.com", "qlaq hisd icda dajl");

			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			Message[] messages = inbox.getMessages();
			int latestMessageIndex = messages.length - 1;
			Message latestMessage = messages[latestMessageIndex];

			String subject = latestMessage.getSubject();
			String content = getTextFromMessage(latestMessage);
			String attachmentInfo = downloadAttachments(latestMessage, downloadPath);

			inbox.close(false);
			store.close();

			return "Subject: " + subject + "\nContent: " + content + "\n" + attachmentInfo;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error reading email: " + e.getMessage();
		}
	}

	private String downloadAttachments(Message message, String downloadPath) throws IOException, MessagingException {
		StringBuilder attachmentInfo = new StringBuilder("Attachments:\n");

		if (message.getContent() instanceof Multipart) {
			Multipart multipart = (Multipart) message.getContent();

			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
					String fileName = bodyPart.getFileName();
					String filePath = downloadPath + File.separator + fileName;

					((MimeBodyPart) bodyPart).saveFile(filePath);
					attachmentInfo.append("- ").append(fileName).append(" (saved to: ").append(filePath).append(")\n");
				}
			}
		}

		return attachmentInfo.toString();
	}

	private String getTextFromMessage(Message message) throws Exception {
		if (message.isMimeType("text/plain")) {
			return message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			return getTextFromMimeMultipart(mimeMultipart);
		}
		return "";

	}

	private String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
		StringBuilder result = new StringBuilder();
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result.append("\n").append(bodyPart.getContent());
				break;
			} else if (bodyPart.isMimeType("text/html")) {
				String html = (String) bodyPart.getContent();
				result.append("\n").append(org.jsoup.Jsoup.parse(html).text());
			} else if (bodyPart.getContent() instanceof MimeMultipart) {
				result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
			}
		}
		return result.toString();
	}

	public void purgeCustomer(long id) {
		Long idLong = (long) 1;
		customerRepo.deleteById(idLong);
	}
}