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
    private String pictureUrl;
    private PlayerDetailsDto playerDetails;
    private String color;
    private Long secondsRemaining;
    private Boolean isCurrent;

    public UserDto(String username) {
        this.username = username;
    }

    public UserDto(long id, String username, String pictureUrl) {
        this.id = id;
        this.username = username;
        this.pictureUrl = pictureUrl;
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
