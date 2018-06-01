package com.spectrum.threescale.poc.service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.spectrum.threescale.poc.model.AdvancedMessageDTO;
import com.spectrum.threescale.poc.model.CountDTO;
import com.spectrum.threescale.poc.model.MessageDTO;

@Service
public class MessagingService {

	public static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Queue queue;

	public void send(@PathVariable("message") final String message) {

		jmsTemplate.convertAndSend(queue, message);

	}

	public CountDTO count() {
		CountDTO countDTO = new CountDTO();
		countDTO.setCount(getMessageCount());

		return countDTO;
	}

	public List<MessageDTO> listMessages() {
		List<MessageDTO> messageDTOs = new LinkedList<MessageDTO>();

		jmsTemplate.browse(queue, new BrowserCallback<Integer>() {
			public Integer doInJms(final Session session, final QueueBrowser browser) throws JMSException {
				Enumeration<?> enumeration = browser.getEnumeration();
				int counter = 0;
				while (enumeration.hasMoreElements()) {

					Message msg = (Message) enumeration.nextElement();
					ActiveMQTextMessage message = (ActiveMQTextMessage) msg;

					MessageDTO messageDTO = new MessageDTO();
					counter += 1;

					messageDTO.setId(message.getJMSMessageID());
					messageDTO.setText((String) message.getText());
					messageDTOs.add(messageDTO);

				}
				return counter;
			}
		});

		return messageDTOs;
	}

	public List<AdvancedMessageDTO> listAdvancedMessages() {
		List<AdvancedMessageDTO> advancedMessageDTOs = new LinkedList<AdvancedMessageDTO>();
		jmsTemplate.browse(queue, new BrowserCallback<Integer>() {
			public Integer doInJms(final Session session, final QueueBrowser browser) throws JMSException {
				Enumeration<?> enumeration = browser.getEnumeration();
				int counter = 0;
				while (enumeration.hasMoreElements()) {

					Message msg = (Message) enumeration.nextElement();
					ActiveMQTextMessage message = (ActiveMQTextMessage) msg;

					AdvancedMessageDTO messageDTO = new AdvancedMessageDTO();
					counter += 1;

					messageDTO.setId(message.getJMSMessageID());
					messageDTO.setText((String) message.getText());
					messageDTO.setTimestamp(convertTime(message.getJMSTimestamp()));
					advancedMessageDTOs.add(messageDTO);

				}
				return counter;
			}
		});
		return advancedMessageDTOs;
	}

	public int purgeMessages() {

		int messageCount = getMessageCount();

		jmsTemplate.browse(queue, new BrowserCallback<Integer>() {
			public Integer doInJms(final Session session, final QueueBrowser browser) throws JMSException {
				Enumeration<?> enumeration = browser.getEnumeration();
				while (enumeration.hasMoreElements()) {
					Message msg = (Message) enumeration.nextElement();
					ActiveMQTextMessage message = (ActiveMQTextMessage) msg;
					deleteMessage(message.getJMSMessageID());
				}
				return null;
			}
		});

		return messageCount;
	}

	public void deleteMessage(String messageId) {
		try {
			jmsTemplate.receiveSelected(queue.getQueueName(), "JMSMessageID='" + messageId + "'");
		} catch (Exception e) {
			logger.warn("unable to remove msg {}", messageId);
		}
	}

	private int getMessageCount() {
		return jmsTemplate.browse(queue, new BrowserCallback<Integer>() {
			@Override
			public Integer doInJms(final Session session, final QueueBrowser browser) throws JMSException {
				{
					return Collections.list(browser.getEnumeration()).size();
				}
			};
		});
	}

	private String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
		return format.format(date);
	}

}
