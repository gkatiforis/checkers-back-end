package com.katiforis.top10.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PlayerDetails playerDetails;

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

    public Player(String username) {
        this.username = username;
    }

    public Player(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }
}
