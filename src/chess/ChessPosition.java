package chess;

import boardgame.Position;

public class ChessPosition {

	private int row;
	private char column;
	
	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8)
			throw new ChessException("Error instantiating postion: Valid values are from A1 to H8\nPress [ENTER] to continue.");
		this.column = column;
		this.row = row;
	}
	
	public char getColumn() {
		return column;
	}
	
	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition fromPostion(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	@Override
	public String toString(){
		 return "" + column + row;
	}
	
	
	
	
	
	
}
