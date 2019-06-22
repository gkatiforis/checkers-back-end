package com.katiforis.checkers.DTO.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FindGame extends BaseRequest {
	private String gameId;
	private boolean restart;

	public FindGame() {
		super();
	}
}
