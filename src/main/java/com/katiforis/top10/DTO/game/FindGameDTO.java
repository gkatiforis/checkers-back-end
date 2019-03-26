package com.katiforis.top10.DTO.game;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FindGameDTO {
	private String gameId;
	private String fromUserID;

	public FindGameDTO() {
		super();
	}
}
