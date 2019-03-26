package com.katiforis.top10.model;//package com.katiforis.top10.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name= "game_invitation_status")
public class GameInvitationStatus implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    public GameInvitationStatus() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
