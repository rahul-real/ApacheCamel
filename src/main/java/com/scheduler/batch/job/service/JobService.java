package com.scheduler.batch.job.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.camel.ProducerTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import com.common.artifact.PreferenceRequest;
import com.common.artifact.PreferenceResponse;
import com.common.artifact.RegistrationData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.batch.job.config.Queues;
import com.scheduler.batch.job.config.TransitiveConfig;
import com.scheduler.batch.job.dto.Account;
import com.scheduler.batch.job.dto.Customer;
import com.scheduler.batch.job.dto.Employee;
import com.scheduler.batch.job.repo.AccountRepo;
import com.scheduler.batch.job.repo.CustomerRepo;
import com.scheduler.batch.job.repo.JobSchedularRepository;
import com.scheduler.batch.job.utils.Constants;

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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class JobService {

	private final ProducerTemplate producerTemplate;

	private final JobSchedularRepository jobSchedularRepository;

	private final ObjectMapper mapper;

	private final JavaMailSender mailSender;
	
	private final CustomerRepo customerRepo;
	
	private final AccountRepo accountRepo;
	
	private final TransitiveConfig transitiveConfig;
	
	private final Queues queues;

	public PreferenceResponse getRegistrationData(String appTxnNum, PreferenceRequest request) throws JsonProcessingException {

		PreferenceResponse response = new PreferenceResponse();

		String parId = String.join(",", request.getParid());

		List<RegistrationData> registrationData = jobSchedularRepository.getRegistrationData(appTxnNum, parId);

		response.setRegistrationData(registrationData);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		LocalDateTime date = LocalDateTime.now();

		response.setResponseDate(date.format(formatter));

		String json = mapper.writeValueAsString(response);

		if(transitiveConfig.isKafkaEnabled()) {
			producerTemplate.sendBody("kafka:first-kafka-topic", json);
		} else {
			producerTemplate.sendBody(queues.getFirstQueue(),json);
		}

		return response;
	}

	public void addEmployeeDetails(String appTxnNum, List<Employee> employeesDetails) {

		jobSchedularRepository.addEmployee(appTxnNum, employeesDetails);

	}

	public List<Employee> getEmployeeData() {
		List<Employee> employee = new LinkedList<>();
		List<Object[]> response = jobSchedularRepository.getEmployeeData();
		if (response != null && !response.isEmpty()) {
			employee = response.stream().map(Employee::new).toList();
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
			Store store = session.getStore(Constants.IMAPS);
			store.connect("imap.gmail.com", "rahul.vodala@gmail.com", "qlaq hisd icda dajl");

			// Open the INBOX folder
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);

			// Get the latest message
			int messageCount = inbox.getMessageCount();
			if (messageCount > 0) {
				Message latestMessage = inbox.getMessage(messageCount);

				// Display the details of the latest email
				log.info("Subject: " + latestMessage.getSubject());
				log.info("From: " + latestMessage.getFrom()[0]);
				log.info("Sent Date: " + latestMessage.getSentDate());
				log.info("Content: " + latestMessage.getContent().toString());
			} else {
				log.info("No messages found in the inbox.");
			}

			// Close the inbox and store
			inbox.close(false);
			store.close();

		} catch (Exception e) {
			log.info("ApplicationTransactionNumber {} failed to read the latest mail due to {} ",UUID.randomUUID().toString(),e.getMessage());
		}
	}

	public String readLatestEmailV2() {
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				log.info("Sorry, unable to find config.properties");
				return "";
			}			
	            
			String downloadPath;
			Properties properties = new Properties();
            properties.load(input);
            downloadPath = properties.getProperty("download.path", "default/path");
			properties.put("mail.store.protocol", Constants.IMAPS);
			Session emailSession = Session.getDefaultInstance(properties);

			Store store = emailSession.getStore(Constants.IMAPS);
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
			log.info("ApplicationTransactionNumber {} failed to read the latest mail due to {} ",UUID.randomUUID().toString(),e.getMessage());
			return "Error reading email: " + e.getMessage();
		}
	}

	private String downloadAttachments(Message message, String downloadPath) throws IOException, MessagingException {
		StringBuilder attachmentInfo = new StringBuilder("Attachments:\n");

		if (message.getContent() instanceof Multipart multipart) {
			multipart = (Multipart) message.getContent();

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
			} else if (bodyPart.getContent() instanceof MimeMultipart mimeMultipartContent) {
				result.append(getTextFromMimeMultipart(mimeMultipartContent));
			}
		}
		return result.toString();
	}

	public void purgeCustomer(long id) {
		customerRepo.deleteById(id);
	}

	public void addCustomer(long id, String name) {
		
		Customer customer = new Customer();
		customer.setId(id);
		customer.setName(name);
		Account account = new Account();
		account.setCustomer(customer);
		account.setId(id);
		account.setNumber(name);
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);
 		customer.setAccounts(accounts);
		customerRepo.save(customer);
		
	}

	public void addAccount(long id, String name) {
		Account account = new Account();
		account.setNumber(name);
		Customer customer = new Customer();
		customer.setId(id);
		account.setCustomer(customer);
		accountRepo.save(account);
		
	}
}