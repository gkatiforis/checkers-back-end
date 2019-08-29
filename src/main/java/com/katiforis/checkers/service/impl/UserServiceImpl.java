package com.katiforis.checkers.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.katiforis.checkers.DTO.request.Reward;
import com.katiforis.checkers.model.PlayerDetails;
import com.katiforis.checkers.model.User;
import com.katiforis.checkers.repository.UserRepository;
import com.katiforis.checkers.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    public static final int GUEST_ID_LENGTH = 30;
    public static final String GUEST_ID_PREFIX = "guest_";

	@Autowired
	UserRepository userRepository;

    @Transactional
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
			playerDetails.setCoins(50);
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

    @Transactional
	@Override
	public User registerGuest(String userId) {
		log.debug("Start PlayerServiceImpl.registerGuest");
		User user = new User();
		user.setUserId(userId);
		user.setUsername("Guest"+ new Random().nextInt());

		PlayerDetails playerDetails = new PlayerDetails();
		playerDetails.setUser(user);
		playerDetails.setElo(0);
		playerDetails.setCoins(50);
		playerDetails.setLevel(1);
		playerDetails.setLevelPoints(0);
		user.setPlayerDetails(playerDetails);

		user = userRepository.save(user);
		log.debug("End PlayerServiceImpl.registerGuest");
		return user;
	}

	public List<User> getPlayers(int page, int size){
		Page<User> players = userRepository.findAllByOrderByPlayerDetails_EloDesc(new PageRequest(page, size));
		return players.getContent();
	}

    @Override
    public long getPlayerPosition(User user) {
	    return userRepository.countByPlayerDetailsElo(user.getPlayerDetails().getElo());
    }

    @Override
    public void addReward(Reward reward) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = getUser(principal.getName());
        int currentCoins = currentUser.getPlayerDetails().getCoins();
        currentUser.getPlayerDetails().setCoins(currentCoins + reward.getAmount());
        userRepository.save(currentUser);
    }

    @Override
    public User getUser(String userId){
		return userRepository.findByUserId(userId);
	}

    @Transactional
	@Override
    public long deleteUser(String userId){
		return userRepository.deleteByUserId(userId);
	}

    @Override
	public User getGuestUser(String userId){
		if(userId == null || userId.isEmpty() || !userId.startsWith(GUEST_ID_PREFIX)){
			return null;
		}
		return getUser(userId);
	}
}
