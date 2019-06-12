package com.katiforis.checkers.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name= "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "username")
    private String username;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "picture_url")
    private String pictureUrl;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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

    public User(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
