package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check; // by pattern, it starts false.
	private boolean checkMate;
	private ChessPiece enPassantVunerable; // by pattern, it starts null.
	private ChessPiece promoted;
	
	private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();

	public ChessMatch() {
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVunerable() {
		return enPassantVunerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] matrix = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				matrix[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return matrix;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) { // No programa principal será imprimido as cores,
																		// de acordo com a lógica implementada aqui.
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validadeTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check.\nPress [ENTER] to continue.");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		//special move PROMOTION
		promoted = null;
		
		if (movedPiece instanceof Pawn) {
			if (movedPiece.getColor() == Color.WHITE && target.getRow() == 0 || movedPiece.getColor() == Color.BLACK && target.getRow() == 7 ) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");
				
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		if (testCheckMate(opponent(currentPlayer)))
			checkMate = true;
		
		else
			nextTurn();
		
		//special move EN PASSANT
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2))
			enPassantVunerable = movedPiece;
		else
			enPassantVunerable = null;
			
		return (ChessPiece) capturedPiece;
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null)
			throw new IllegalStateException("There is no piece to be promoted.");
		
		if (!type.equals("B") && !type.equals("R") && !type.equals("N") && !type.equals("Q"))
			return promoted;
		System.out.println("Piece " + promoted.getChessPosition() + " has been promoted.");
		System.out.println("B = Bishop\n"
				+ "R = Rook\n"
				+ "N = Knight\n"
				+ "Q = Queen");
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board, color); //is used "equals()" because String is not a primitive type. It is a Class type, so we can't compare using "==".
		if (type.equals("R")) return new Rook(board, color);
		if (type.equals("N")) return new Knight(board, color);
		return new Queen(board, color);
	}
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source);
		p.increaseCount();
		Piece capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}
		//special move CASTLING king side rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseCount();
		}
		//special move CASTLING queen side rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseCount();
		}
		//special move EN PASSANT
		if (p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) 
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				else
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add((ChessPiece) capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
			
		}
		return capturedPiece;
	}
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target);
		p.decreaseCount();
		board.placePiece(p, source);
		
		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add((ChessPiece) capturedPiece);
		}
		
		//special move CASTLING king side rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseCount();
		}
		
		//special move CASTLING queen side rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece)board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseCount();
		}
		//special move EN PASSANT
		if (p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVunerable) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) 
					pawnPosition = new Position(3, target.getColumn());
				else
					pawnPosition = new Position(4, target.getColumn());
			board.placePiece(p, pawnPosition);
			capturedPiece = board.removePiece(pawnPosition);
			capturedPieces.add((ChessPiece) capturedPiece);
			piecesOnTheBoard.remove(capturedPiece);
			}
		}
	}
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position))
			throw new ChessException("\nThere is no piece on source position.\nPress [ENTER] to continue.");

		if (!board.piece(position).isThereAnyPossibleMove())
			throw new ChessException("\nThere is no possible moves for the chosen piece.\nPress [ENTER] to continue.");

		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor())
			throw new ChessException("\nThe chosen piece is not yours.\nPress [ENTER] to continue.");
	}

	private void validadeTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target))
			throw new ChessException("The chosen piece can't move to the target position.\nPress [ENTER] to continue.");
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list)
			if (p instanceof King)
				return (ChessPiece)p;
		throw new IllegalStateException("There is no " + color + "king on the boar.");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition(); //pega a posição do rei em formato de matriz.
		List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList()); //cria uma lista com as peças adversárias.
		for (Piece p : opponentPieces) {
			boolean[][] matrix = p.possibleMoves();
			if (matrix[kingPosition.getRow()][kingPosition.getColumn()])
				return true;
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
		if(!testCheck(color))
			return false;
		List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] matrix = p.possibleMoves(); //matrix receives possible moves of the piece p.
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (matrix[i][j]) { //if there is possible moves.
						Position source = ((ChessPiece)p).getChessPosition().toPosition(); //convert the p position(matrix form) to chess position.
						Position target = new Position(i, j); //move the piece to the possible move and test if when we move the check piece to a target possible move, this piece is still in check.
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck)
							return false;
					}
				}
			}
		}
		return true;
	}
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
        placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
        placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
        placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
        placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}
