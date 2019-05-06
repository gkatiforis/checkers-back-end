package com.katiforis.top10.DTO.request;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class FindGame extends BaseRequest {
	private String gameId;

	public FindGame() {
		super();
	}
}
