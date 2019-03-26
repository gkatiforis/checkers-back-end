package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name= "player")
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "player_id")
    private String playerId;

    @Column(name = "username")
    private String username;

    @Column(name = "elo")
    private Integer elo;

    @Column(name = "level")
    private Integer level;

    @Column(name = "level_points")
    private Integer levelPoints;

    @Column(name = "bonus_points")
    private Integer bonusPoints;

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    private List<FriendInvitation> myFriendInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    private List<FriendInvitation> friendInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    private List<GameInvitation> myGameInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "to", fetch = FetchType.LAZY)
    private List<GameInvitation> gameInvitations = new ArrayList<>();

    @OneToMany(mappedBy = "from", fetch = FetchType.LAZY)
    private List<ShareInvitation> shareInvitations = new ArrayList<>();

    public Player() {
    }

    public Player(String username) {
        this.username = username;
    }

    public Player(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getElo() {
        return elo;
    }

    public void setElo(Integer elo) {
        this.elo = elo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevelPoints() {
        return levelPoints;
    }

    public void setLevelPoints(Integer levelPoints) {
        this.levelPoints = levelPoints;
    }

    public Integer getBonusPoints() {
        return bonusPoints;
    }

    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    public List<FriendInvitation> getMyFriendInvitations() {
        return myFriendInvitations;
    }

    public void setMyFriendInvitations(List<FriendInvitation> myFriendInvitations) {
        this.myFriendInvitations = myFriendInvitations;
    }

    public List<FriendInvitation> getFriendInvitations() {
        return friendInvitations;
    }

    public void setFriendInvitations(List<FriendInvitation> friendInvitations) {
        this.friendInvitations = friendInvitations;
    }

    public List<GameInvitation> getMyGameInvitations() {
        return myGameInvitations;
    }

    public void setMyGameInvitations(List<GameInvitation> myGameInvitations) {
        this.myGameInvitations = myGameInvitations;
    }

    public List<GameInvitation> getGameInvitations() {
        return gameInvitations;
    }

    public void setGameInvitations(List<GameInvitation> gameInvitations) {
        this.gameInvitations = gameInvitations;
    }

    public List<ShareInvitation> getShareInvitations() {
        return shareInvitations;
    }

    public void setShareInvitations(List<ShareInvitation> shareInvitations) {
        this.shareInvitations = shareInvitations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (id != player.id) return false;
        if (playerId != null ? !playerId.equals(player.playerId) : player.playerId != null) return false;
        return username != null ? username.equals(player.username) : player.username == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (playerId != null ? playerId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
