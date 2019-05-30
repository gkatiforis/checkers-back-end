package com.katiforis.top10.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.katiforis.top10.model.PlayerDetails;
import com.katiforis.top10.model.User;
import com.katiforis.top10.repository.UserRepository;
import com.katiforis.top10.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	public static final int GUEST_ID_LENGTH = 30;
	public static final String GUEST_ID_PREFIX = "guest_";

	@Autowired
	UserRepository userRepository;

	@Override
	public User registerWithGoogle(String userId, GoogleIdToken idToken) {
		log.debug("Start PlayerServiceImpl.registerWithGoogle");
		User user = null;
		if (userId != null) {
			user = userRepository.findByUserId(userId);
		}
		if(user == null){
			 user = new User();
			PlayerDetails playerDetails = new PlayerDetails();
			playerDetails.setUser(user);
			playerDetails.setElo(0);
			playerDetails.setCoins(0);
			playerDetails.setLevel(1);
			playerDetails.setLevelPoints(0);
			user.setPlayerDetails(playerDetails);
		}
		GoogleIdToken.Payload payload = idToken.getPayload();
		user.setUserId(payload.getSubject());
		user.setUsername("User" + new Random().nextInt());
		user.setEmail(payload.getEmail());
		user.setName((String) payload.get("name"));
		user.setPictureUrl((String) payload.get("picture"));

		return userRepository.save(user);
	}

	@Override
	public User registerGuest(String userId) {
		log.debug("Start PlayerServiceImpl.registerGuest");
		User user = new User();
		user.setUserId(userId);
		user.setUsername("Guest"+ new Random().nextInt());

		PlayerDetails playerDetails = new PlayerDetails();
		playerDetails.setUser(user);
		playerDetails.setElo(0);
		playerDetails.setCoins(0);
		playerDetails.setLevel(1);
		playerDetails.setLevelPoints(0);
		user.setPlayerDetails(playerDetails);

		user = userRepository.save(user);
		log.debug("End PlayerServiceImpl.registerGuest");
		return user;
	}

	public List<User> getPlayers(int page, int size){
		Page<User> players = userRepository.findAllByOrderByPlayerDetails_EloDesc(new PageRequest(0, 10));
		return players.getContent();
	}

	public User getUser(String userId){
		return userRepository.findByUserId(userId);
	}

	public User getGuestUser(String userId){
		if(userId == null || userId.isEmpty() || !userId.startsWith(GUEST_ID_PREFIX)){
			return null;
		}
		return getUser(userId);
	}
}
