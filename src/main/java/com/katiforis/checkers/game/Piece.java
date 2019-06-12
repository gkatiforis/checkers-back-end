
package com.katiforis.checkers.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

public class Piece implements Serializable {

	private String color;
	private boolean isKing;
	@JsonIgnore
	private Cell cell;

	public Piece(){}
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

	public Piece(String color) throws IllegalArgumentException {
		this.color = color;
		this.isKing = false;
		this.cell = null;
	}

	public String getColor(){
		return this.color;
	}

	public boolean isKing(){
		return this.isKing;
	}

	public void makeKing(){
		this.isKing = true;
	}

	public static String getOpponentColor(String givenColor){
		if(givenColor.equals(Piece.DARK)){
			return Piece.LIGHT;
		}
		else if(givenColor.equals(Piece.LIGHT)){
			return Piece.DARK;
		}
		else{
			return null;
		}
	}

	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Piece)){
			return false;
		}

		Piece givenPiece =  (Piece) obj;

		if(givenPiece.getColor().equals(this.color) && givenPiece.isKing() == this.isKing &&
				givenPiece.getCell().getX() == this.cell.getX() && givenPiece.getCell().getY() == this.cell.getY()){
			return true;
		}
		return false;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public void setKing(boolean king) {
		isKing = king;
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}


}