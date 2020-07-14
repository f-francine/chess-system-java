package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	private ChessMatch chessMatch;
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
		}
	
	@Override
	public String toString() {
		return "K";
	}
	
	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p == null || p.getColor() != getColor(); //if the position is empty or there is an enemy piece, then the king acn move.
	}
	
	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean [][]matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//above
		p.setValues(position.getRow() -1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//below
		p.setValues(position.getRow() +1, position.getColumn());
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//right
		p.setValues(position.getRow(), position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//left
		p.setValues(position.getRow(), position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//NW
		p.setValues(position.getRow() -1, position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//SW
		p.setValues(position.getRow() +1, position.getColumn() - 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//NE
		p.setValues(position.getRow() -1, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//SE
		p.setValues(position.getRow() +1, position.getColumn() + 1);
		if(getBoard().positionExists(p) && canMove(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//special move CASTLING - king side rook
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			Position positionR1 = new Position(position.getRow(),  position.getColumn() + 3);
			if (testRookCastling(positionR1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					matrix[position.getRow()][position.getColumn() + 2] = true;
				}
			}
			//special move CASTLING - queen side rook
			Position positionR2 = new Position(position.getRow(),  position.getColumn() - 4);
			if (testRookCastling(positionR2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					matrix[position.getRow()][position.getColumn() - 2] = true;
				}
			}
		}
		return matrix;
	}
}
