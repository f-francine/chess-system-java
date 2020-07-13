package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	
	private Color color;
	protected int moveCount;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public void increaseCount() {
		moveCount++;
	}
	
	public void decreaseCount() {
		moveCount--;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPostion(position);
	}
	
	protected boolean isThereOpponentPiece(Position position) { // Método geral que serve para todas peças. A implemação específica de cada peça está na classe da própia peça.
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColor() != color;
	}
	
	
}
