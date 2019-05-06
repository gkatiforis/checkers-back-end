package com.katiforis.top10.DTO.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class BaseResponse implements Serializable {
    protected String status;
    protected String userId;

    public BaseResponse(String status) {
        this.status = status;
    }

    public BaseResponse(String status, String userId) {
        this.status = status;
        this.userId = userId;
    }
}
