package com.katiforis.checkers.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@Entity
@Table(name= "player_details")
public class PlayerDetails implements Serializable {

    @Id
    private long id;

    @Column(name = "elo")
    private int elo;

    @Column(name = "level")
    private int level;

    @Column(name = "level_points")
    private int levelPoints;

    @Column(name = "coins")
    private int coins;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
}
