package boardgame;

public abstract class Piece {
	
	protected Position position;
	private Board board;
	
	public Piece(Board board) {
		this.board = board;
		position = null;
	}

	protected Board getBoard() {
		return board;
	}
	
	public abstract boolean [][] possibleMoves(); //by default, all empty matrix positions start with false
	
	public boolean possibleMove(Position position) { //hook methods
		return possibleMoves()[position.getRow()][position.getColumn()]; //This method will return true if there is any possible move to the piece
	}
	public boolean isThereAnyPossibleMove() { // check if there is any possible move in the matrix.
		boolean[][]matrix = possibleMoves(); 
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix.length; j++)
				if (matrix[i][j])
					return true;
		return false;
	}
}
