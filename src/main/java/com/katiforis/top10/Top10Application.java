package com.katiforis.top10;

import com.katiforis.top10.model.Player;
import com.katiforis.top10.model.ShareInvitation;
import com.katiforis.top10.repository.FriendInvitationRepository;
import com.katiforis.top10.repository.PlayerRepository;
import com.katiforis.top10.repository.ShareInvitationRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@SpringBootApplication
@EnableCaching
public class Top10Application implements InitializingBean {

	@Autowired
	PlayerRepository playerRepository;

	@Autowired
	FriendInvitationRepository friendInvitationRepository;

	@Autowired
	ShareInvitationRepository shareInvitationRepository;


	public static void main(String[] args) {
		SpringApplication.run(Top10Application.class, args);


	}


	@Override
	@Transactional
	public void afterPropertiesSet() throws Exception {
		Player player1= new Player();


		player1.setElo(344);
		player1.setBonusPoints(222);
		player1.setLevel(222);
		player1.setLevelPoints(222);
		player1.setPlayerId("dfg");
		player1.setUsername("sdfddss");
		playerRepository.save(player1);

//		player1 = playerRepository.findById(2L);
		Player player2= new Player();
//		player2 = playerRepository.findById(1L);
		player2.setElo(344);
		player2.setBonusPoints(222);
		player2.setLevel(222);
		player2.setLevelPoints(222);
		player2.setPlayerId("dffg");
		player2.setUsername("sdfss");
		playerRepository.save(player2);
//		player2 = playerRepository.findById(1L);
//		FriendInvitation fi = new FriendInvitation();
//		fi.setFrom(player1);
//		fi.setTo(player2);




		ShareInvitation shareInvitation = new ShareInvitation();
		shareInvitation.setCode("sdfgd");
		shareInvitation.setRequestDate(new Date());
		shareInvitation.setFrom(player1);
		shareInvitation.setTo(player2);

		//player1.setMyFriendInvitations(Arrays.asList(fi));
		shareInvitationRepository.save(shareInvitation);
		//friendInvitationRepository.save(fi);
//		playerRepository.save(player1);
	}
}

