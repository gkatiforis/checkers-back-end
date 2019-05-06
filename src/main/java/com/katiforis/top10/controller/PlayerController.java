package com.katiforis.top10.controller;

import com.katiforis.top10.DTO.Notification;
import com.katiforis.top10.DTO.Player;
import com.katiforis.top10.DTO.request.FindGame;
import com.katiforis.top10.DTO.request.GetNotifications;
import com.katiforis.top10.DTO.request.GetRank;
import com.katiforis.top10.DTO.response.*;
import com.katiforis.top10.repository.PlayerRepository;
import com.katiforis.top10.services.GameService;
import com.katiforis.top10.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@MessageMapping("/menu")
public class PlayerController {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	GameService gameService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping("/login")
	ResponseEntity login(Player playerDTO) {
		log.debug("Start PlayerController.login");

		com.katiforis.top10.model.Player player = playerRepository.findByPlayerId(playerDTO.getPlayerId());

		if (player == null) {
			player = new com.katiforis.top10.model.Player( playerDTO.getPlayerId(), playerDTO.getUsername());
			playerRepository.save(player);
		}

		log.debug("End PlayerController.login");

		PlayerDetails playerDetailsDTO = new PlayerDetails();
		playerDetailsDTO.setUsername(player.getUsername());
		ResponseEntity<PlayerDetails> response = new ResponseEntity<>(playerDetailsDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(player.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/friend")
	ResponseEntity getFriendList(Player playerDTO) {
		log.debug("Start PlayerController.getFriendlist");

		com.katiforis.top10.model.Player player = playerRepository.findByPlayerId(playerDTO.getPlayerId());

		com.katiforis.top10.model.Player player1 = new com.katiforis.top10.model.Player( "id","gkatiforis");
		com.katiforis.top10.model.Player player2 = new com.katiforis.top10.model.Player( "id","gkatiforis");

		List<com.katiforis.top10.model.Player> players = new ArrayList<>();
		players.add(player1);players.add(player2);

		FriendList friendListDTO = new FriendList();
		friendListDTO.setPlayers(players);
		friendListDTO.setUserId(player.getPlayerId());
		ResponseEntity<FriendList> response = new ResponseEntity<>(friendListDTO, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(player.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/notification")
	ResponseEntity getNotificationList(GetNotifications get) {
		log.debug("Start PlayerController.getNotificationList");
		NotificationList notificationList = new NotificationList();
		notificationList.setNotifications((Arrays.asList(new Notification("test test", "22/02/2019"))));
		notificationList.setUserId(get.getPlayerId());
		ResponseEntity<NotificationList> response = new ResponseEntity<>(notificationList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(get.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/rank")
	ResponseEntity getRankList(GetRank get) {
		log.debug("Start PlayerController.getRankList");
		RankList rankList = new RankList();
		rankList.setPlayers(Arrays.asList( new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatdiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis")
				,new Player( "id","gkatiforis"),
				new Player( "id","gkatiforis"),
				new Player( "id","gkatiforis"),
				new Player( "id","gkatiforis"),
				new Player( "id","gkatiforis"),
				new Player( "id","gkatiforis")));
		rankList.setUserId(get.getPlayerId());
		ResponseEntity<RankList> response = new ResponseEntity<>(rankList, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(get.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/lobby")
	ResponseEntity getLobby(Player player) {
		log.debug("Start PlayerController.getLobby");

		Lobby lobby = new Lobby();
		com.katiforis.top10.model.Player player1 = new com.katiforis.top10.model.Player( "id","gkatiforis");
		com.katiforis.top10.model.Player player2 = new com.katiforis.top10.model.Player( "id","gkatiforis");

		List<com.katiforis.top10.model.Player> players = new ArrayList<>();
		players.add(player1);players.add(player2);
		lobby.setUserId(player.getPlayerId());
		lobby.setPlayers(players);
		ResponseEntity<Lobby> response = new ResponseEntity<>(lobby, HttpStatus.OK);
		simpMessagingTemplate.convertAndSendToUser(player.getPlayerId(), Constants.MAIN_TOPIC, response);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@MessageMapping("/game/find")
	public void findGame(FindGame findGame) {
		log.debug("Start GameController.findGame");
		gameService.findGame(findGame);
		log.debug("End GameController.findGame");
	}
}
