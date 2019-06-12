package com.katiforis.checkers.DTO.response;

import com.katiforis.checkers.model.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FriendList extends BaseResponse {
    List<User> users = new ArrayList<>();
    public FriendList(String status) {
        super(status);
    }
    public FriendList() {
        super(ResponseState.FRIEND_LIST.getState());
    }
}
