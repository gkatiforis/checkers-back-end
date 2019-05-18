package com.katiforis.top10.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements Serializable {
    private long id;
    private String username;
    private String imageUrl;

    public User(String username) {
        this.username = username;
    }


    public User(long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}
