package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece {

	public Queen(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "Q";
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] matrix = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0, 0);

		p.setValues(position.getRow() - 1, position.getColumn()); // above

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() - 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		p.setValues(position.getRow(), position.getColumn() - 1); // left

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		p.setValues(position.getRow(), position.getColumn() + 1); // right

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		p.setValues(position.getRow() + 1, position.getColumn()); // below

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1);
		}

		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;
		
		//NW
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		//NE
		p.setValues(position.getRow() - 1, position.getColumn() + 1); 
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() - 1, p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		//SW
		p.setValues(position.getRow() + 1, position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		//SE
		p.setValues(position.getRow() + 1, position.getColumn() + 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			matrix[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() + 1, p.getColumn() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p))
			matrix[p.getRow()][p.getColumn()] = true;

		return matrix;
	}
}
