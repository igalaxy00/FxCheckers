package controller;


import view.Piece;

public class MoveResult {

    private MoveType type;

    MoveType getType() {
        return type;
    }

    private Piece piece;

    Piece getPiece() {
        return piece;
    }

    MoveResult(MoveType type) {
        this(type, null);
    }

    MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }
}
