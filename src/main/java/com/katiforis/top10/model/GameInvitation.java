package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name= "game_invitation")
public class GameInvitation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_player_id")
    private Player from;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_player_id")
    private Player to;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private GameInvitationStatus gameInvitationStatus;

    @Column(name = "is_private")
    private boolean isPrivate;

    @Column(name = "request_date")
    private Date requestDate;

    public GameInvitation() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getFrom() {
        return from;
    }

    public void setFrom(Player from) {
        this.from = from;
    }

    public Player getTo() {
        return to;
    }

    public void setTo(Player to) {
        this.to = to;
    }

    public GameInvitationStatus getGameInvitationStatus() {
        return gameInvitationStatus;
    }

    public void setGameInvitationStatus(GameInvitationStatus gameInvitationStatus) {
        this.gameInvitationStatus = gameInvitationStatus;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }
}
