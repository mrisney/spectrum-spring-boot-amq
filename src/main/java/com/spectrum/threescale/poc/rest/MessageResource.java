package com.spectrum.threescale.poc.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spectrum.threescale.poc.model.AdvancedMessageDTO;
import com.spectrum.threescale.poc.model.CountDTO;
import com.spectrum.threescale.poc.model.MessageDTO;
import com.spectrum.threescale.poc.service.MessagingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/messages/api")
@Api(value = "messages")
public class MessageResource {

	public static final Logger logger = LoggerFactory.getLogger(MessageResource.class);

	@Autowired
	private MessagingService messagingService;

	@GetMapping("/v1/{message}")
	@ApiOperation(value = "Send Message")
	public @ResponseBody ResponseEntity<String> send(@PathVariable("message") final String message) {

		messagingService.send(message);

		return new ResponseEntity<String>("message sent : " + message, HttpStatus.OK);

	}

	@GetMapping("/v1/count/")
	@ApiOperation(value = "Message Count")
	public @ResponseBody ResponseEntity<CountDTO> count() {

		return new ResponseEntity<CountDTO>(messagingService.count(), HttpStatus.OK);

	}

	@GetMapping("/v1/list/")
	@ApiOperation(value = "List Messages")
	public @ResponseBody ResponseEntity<List<MessageDTO>> listMessages() {

		List<MessageDTO> messages = messagingService.listMessages();

		return new ResponseEntity<List<MessageDTO>>(messages, HttpStatus.OK);
	}

	@GetMapping("/v2/list/")
	@ApiOperation(value = "List Advanced Messages")
	public @ResponseBody ResponseEntity<List<AdvancedMessageDTO>> listAdvancedMessages() {

		List<AdvancedMessageDTO> messages = messagingService.listAdvancedMessages();

		return new ResponseEntity<List<AdvancedMessageDTO>>(messages, HttpStatus.OK);
	}

	@DeleteMapping("/v1/delete/")
	@ApiOperation(value = "Delete Messages")
	public @ResponseBody ResponseEntity<String> deleteMessages() {

		int count = messagingService.purgeMessages();

		return new ResponseEntity<String>(count + " messages deleted", HttpStatus.OK);
	}

	@DeleteMapping("/v2/delete/{id}")
	@ApiOperation(value = "Delete Message by Id")
	public @ResponseBody ResponseEntity<String> deleteMessage(@PathVariable("id") final String messageId) {

		try {
			messagingService.deleteMessage(java.net.URLDecoder.decode(messageId, "UTF-8"));
		} catch (Exception e) {
			return new ResponseEntity<String>("messages not found", (HttpStatus.NOT_FOUND));
		}

		return new ResponseEntity<String>("messages deleted", HttpStatus.OK);
	}

}
