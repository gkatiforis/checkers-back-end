package com.katiforis.top10.DTO.game;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CurrentTime implements Serializable {
    private Date currentDate;
}
