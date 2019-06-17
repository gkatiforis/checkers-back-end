package com.katiforis.checkers.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board implements Serializable {

    private Cell[][] board;
    private static int BOARD_SIZE = 8;

    public Board(){
        board = new Cell[Board.BOARD_SIZE][Board.BOARD_SIZE];
    }

    public void initialBoardSetup(){
        for(int i=0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                this.board[i][j] = new Cell(i, j);
            }
        }

        for(int column = 0; column < Board.BOARD_SIZE; column+= 2){
            this.board[0][column].placePiece(new Piece(Piece.LIGHT));
            this.board[2][column].placePiece(new Piece(Piece.LIGHT));
            this.board[6][column].placePiece(new Piece(Piece.DARK));
        }

        for(int column = 1; column< Board.BOARD_SIZE; column+=2){
            this.board[1][column].placePiece(new Piece(Piece.LIGHT));
            this.board[5][column].placePiece(new Piece(Piece.DARK));
            this.board[7][column].placePiece(new Piece(Piece.DARK));
        }
    }

    public Cell getCell(int x, int y)   {
        if((x<0 || x > 7) || (y<0 || y >7)){
            return null;
        }

        return this.board[x][y];
    }

    public List<Cell> movePiece(int fromX, int fromY, int toX, int toY){
        Cell srcCell = this.getCell(fromX, fromY);
        Cell dstCell = this.getCell(toX, toY);
        List<Cell> changedCells = new ArrayList<>();
        if(srcCell.getPlacedPiece() == null || dstCell.getPlacedPiece() != null){
            return Collections.emptyList();
        }

        if(isCaptureMove(srcCell, dstCell)){
            int capturedCellX = (fromX + toX)/ 2;
            int capturedCellY= (fromY + toY)/2;
            Piece capturedPiece = this.board[capturedCellX][capturedCellY].getPlacedPiece();
            removePiece(capturedPiece);
            changedCells.add(capturedPiece.getCell());
        }
        srcCell.movePiece(dstCell);
        changedCells.add(srcCell);
        changedCells.add(dstCell);
        return changedCells;
    }


    public void removePiece(Piece capturedPiece){
        if(capturedPiece.getColor().equals(Piece.LIGHT)){

            capturedPiece.getCell().placePiece(null);
        }
        else if(capturedPiece.getColor().equals(Piece.DARK)){
            capturedPiece.getCell().placePiece(null);
        }
    }

    public List<Move> possibleMoves(Cell cell)  {
        return possibleMoves(Arrays.asList(cell));
    }

    public List<Move> possibleMoves(List<Cell> cells)  {
        List<Move> nextMoves = new ArrayList<>();
        List<Move> requiredNextMoves = new ArrayList<>();
        if(cells == null){
            return Collections.emptyList();
        }
        for (Cell givenCell : cells) {

            Piece givenPiece = givenCell.getPlacedPiece();

            if(givenPiece == null){
                Cell c  = getCell(givenCell.getX(), givenCell.getY());

                if(c != null){
                    givenCell = c;
                    givenPiece = c.getPlacedPiece();
                }
                if(givenPiece == null){
                  continue;
                }
            }

            String playerColor = givenPiece.getColor();
            String opponentColor = Piece.getOpponentColor(playerColor);

            if(playerColor.equals(Piece.LIGHT)){
                int nextX = givenCell.getX()+1;

                if(nextX < 8){
                    int nextY = givenCell.getY()+1;

                    if(nextY < 8){

                        if(!this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }

                        else if(this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX + 1;
                            int yCoordAfterHoping = nextY + 1;
                            if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                    }
                    nextY = givenCell.getY() -1;
                    if(nextY >=0){
                        if(!this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }

                        else if(this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX + 1;
                            int yCoordAfterHoping = nextY - 1;
                            if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                    }
                }
                if(givenPiece.isKing()){
                    nextX = givenCell.getX() -1;
                    if(nextX >=0){
                        int nextY = givenCell.getY()+1;
                        if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }

                        else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX - 1;
                            int yCoordAfterHoping = nextY + 1;
                            if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                        nextY = givenCell.getY() -1;
                        if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }

                        else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX - 1;
                            int yCoordAfterHoping = nextY - 1;
                            if(xCoordAfterHoping >=0 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                    }
                }
            }
            else if(givenPiece.getColor().equals(Piece.DARK)){
                int nextX = givenCell.getX()-1;
                if(nextX >= 0){
                    //next move = (currentRow -1, currentColumn +1) which is a row ahead and a column to right
                    int nextY = givenCell.getY()+1;
                    if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }

                    else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX -1;
                        int yCoordAfterHoping = nextY +1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                    nextY = givenCell.getY()-1;
                    if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                        nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                    }
                    else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                        int xCoordAfterHoping = nextX -1;
                        int yCoordAfterHoping = nextY - 1;
                        if(xCoordAfterHoping >=0 && yCoordAfterHoping >=0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                            requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                        }
                    }
                }
                if(givenPiece.isKing()){
                    nextX = givenCell.getX()+1;
                    if(nextX < 8){
                        int nextY = givenCell.getY()+1;
                        if(nextY < 8 && !this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }

                        else if(nextY < 8 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX + 1;
                            int yCoordAfterHoping = nextY +  1;
                            if(xCoordAfterHoping < 8 && yCoordAfterHoping < 8 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                        nextY = givenCell.getY() -1;
                        if(nextY >=0 && !this.board[nextX][nextY].containsPiece()){
                            nextMoves.add(new Move(this.board[nextX][nextY], givenCell, givenCell.getPlacedPiece()));
                        }
                        else if(nextY >=0 && this.board[nextX][nextY].getPlacedPiece().getColor().equals(opponentColor)){
                            int xCoordAfterHoping = nextX + 1;
                            int yCoordAfterHoping = nextY -  1;
                            if(xCoordAfterHoping < 8 && yCoordAfterHoping >= 0 && !this.board[xCoordAfterHoping][yCoordAfterHoping].containsPiece()){
                                requiredNextMoves.add(new Move(this.board[xCoordAfterHoping][yCoordAfterHoping], givenCell, givenCell.getPlacedPiece(), true));
                            }
                        }
                    }
                }
            }
        }
        if(!requiredNextMoves.isEmpty()){
            return requiredNextMoves;
        }else{
            return nextMoves;
        }
    }

    public boolean isCaptureMove(Cell srcCell, Cell dstCell){
        if (srcCell == null || dstCell == null) {
           return false;
        }

        if (srcCell.getPlacedPiece() == null) {
            srcCell  = getCell(srcCell.getX(), srcCell.getY());
        }

        if ((Math.abs(srcCell.getX() - dstCell.getX()) == 2) && (Math.abs(srcCell.getY() - dstCell.getY()) == 2)) {
            return true;
        }
        return false;
    }

    public boolean hasMoves(String color){
        List<Cell> pieces = getCellPieces(color);
        if(!pieces.isEmpty()){
            if(!possibleMoves(pieces).isEmpty()){
                return true;
            }
        }
        return false;
    }

    public boolean hasGameEnd(){
        return !hasMoves(Piece.LIGHT) || !hasMoves(Piece.DARK);
    }

    public List<Cell> getCellPieces(String givenColor)  {
        List<Cell> pieces = new ArrayList<>(12);
        for(int i=0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if(this.board[i][j] != null && this.board[i][j].getPlacedPiece() != null && this.board[i][j].getPlacedPiece().getColor().equals(givenColor)){
                    pieces.add(this.board[i][j]);
                }
            }
        }
        return pieces;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] board) {
        this.board = board;
    }

    public boolean isValidMove(Cell srcCell, Cell dstCell){
        List<Move> moves = possibleMoves(srcCell);
        for(Move move:moves){
            if(move.getTo().equals(dstCell)){
                return true;
            }
        }
        return false;
    }
}
