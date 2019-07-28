package com.katiforis.checkers.DTO.request;

import com.katiforis.checkers.DTO.GameType;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FindGame extends BaseRequest {
	private String gameId;
	private boolean restart;
    private GameType gameType;

	public FindGame() {
		super();
	}
}
