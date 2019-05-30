package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.*;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.request.GetRank;
import com.katiforis.top10.DTO.response.*;
import com.katiforis.top10.model.User;
import com.katiforis.top10.service.GameHandlerService;
import com.katiforis.top10.service.UserService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@MessageMapping("/menu")
public class MenuController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
    GameHandlerService gameHandlerService;

	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/rank")
	ResponseEntity getRankList(GetRank get) {
		log.debug("Start PlayerController.getRankList");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		List<User> users = userService.getPlayers(0, 10);
		RankList rankList = new RankList();
		rankList.setPlayers(modelMapper.map(users,  new TypeToken<List<UserDto>>(){}.getType()));
		ResponseEntity<RankList> response = new ResponseEntity<>(rankList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(principal.getName(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/game/find")
	public void findGame(FindGame findGame) {
		log.debug("Start GameController.findGame");
		gameHandlerService.findGame(findGame);
		log.debug("End GameController.findGame");
	}

	@MessageMapping("/details")
	public void getPlayerDetails() {
		log.debug("Start GameController.getPlayerDetails");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUser(principal.getName());
		UserDto userDto =  modelMapper.map(user,  UserDto.class);
		UserStats userStats = new UserStats();
		userStats.setUserDto(userDto);
		ResponseEntity<UserStats> response = new ResponseEntity<>(userStats, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(principal.getName(), Constants.MAIN_TOPIC, response);
		log.debug("End GameController.getPlayerDetails");
	}
}

