package com.katiforis.checkers.DTO;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

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
    private String color;
    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(long id, String username, String imageUrl) {
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return userId.equals(userDto.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
