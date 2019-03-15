package com.katiforis.top10.DTO.game;

public class FindGameDTO {
	private String gameId;
	private String fromUserID;

	public FindGameDTO() {
		super();
	}

	public FindGameDTO(String fromUserID) {
		this.fromUserID = fromUserID;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getFromUserID() {
		return fromUserID;
	}

	public void setFromUserID(String fromUserID) {
		this.fromUserID = fromUserID;
	}



}
