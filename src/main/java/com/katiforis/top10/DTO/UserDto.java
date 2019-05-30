package com.katiforis.top10.DTO;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDto implements Serializable {
    private long id;
    private String userId;
    private String username;
    private String email;
    private String imageUrl;
    private PlayerDetailsDto playerDetails;

    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}
