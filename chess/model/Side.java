package ws.aperture.chess.model;

public enum Side {
    WHITE,
    BLACK;

    public static Side otherSide(Side side) {
        return ( side == WHITE ) ? BLACK : WHITE;
    }
}
