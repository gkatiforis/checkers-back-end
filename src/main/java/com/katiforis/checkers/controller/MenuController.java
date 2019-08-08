package com.katiforis.checkers.controller;

import com.katiforis.checkers.DTO.*;
import com.katiforis.checkers.DTO.request.FindGame;
import com.katiforis.checkers.DTO.request.GetRank;
import com.katiforis.checkers.DTO.request.Reward;
import com.katiforis.checkers.DTO.response.*;
import com.katiforis.checkers.exception.GameException;
import com.katiforis.checkers.model.User;
import com.katiforis.checkers.service.GameHandlerService;
import com.katiforis.checkers.service.UserService;
import com.katiforis.checkers.util.Constants;
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
		log.debug("Start MenuController.getRankList");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.getUser(principal.getName());
		List<User> users = userService.getPlayers(0, 20);
        long currentPlayerPosition = userService.getPlayerPosition(currentUser);
		RankList rankList = new RankList();
		rankList.setCurrentPlayerPosition(currentPlayerPosition + 1);
        rankList.setCurrentPlayer(modelMapper.map(currentUser,  UserDto.class));
		rankList.setPlayers(modelMapper.map(users,  new TypeToken<List<UserDto>>(){}.getType()));
		ResponseEntity<RankList> response = new ResponseEntity<>(rankList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(principal.getName(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

    @MessageMapping("/reward")
    ResponseEntity reward(Reward reward) {
        log.debug("Start MenuController.reward");
        userService.addReward(reward);
        return new ResponseEntity<>(HttpStatus.OK);
    }

	@MessageMapping("/game/find")
	public ResponseEntity findGame(FindGame findGame) throws GameException {
		log.debug("Start MenuController.findGame");

            gameHandlerService.findGame(findGame);

        log.debug("End MenuController.findGame");
        return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/details")
	public void getPlayerDetails() {
		log.debug("Start MenuController.getPlayerDetails");
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.getUser(principal.getName());
		UserDto userDto =  modelMapper.map(user,  UserDto.class);
		UserStats userStats = new UserStats();
		userStats.setUserDto(userDto);
		ResponseEntity<UserStats> response = new ResponseEntity<>(userStats, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(principal.getName(), Constants.MAIN_TOPIC, response);
		log.debug("End MenuController.getPlayerDetails");
	}

    @MessageMapping("/keepConnection")
    public void keepConnection() {
    }
}

