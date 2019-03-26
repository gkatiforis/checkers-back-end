package com.katiforis.top10.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name= "share_invitation")
public class ShareInvitation implements Serializable {
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

    @Column(name = "code")
    private String code;

    @Column(name = "request_date")
    private Date requestDate;
}
