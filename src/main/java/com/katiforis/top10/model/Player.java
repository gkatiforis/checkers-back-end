package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;

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
