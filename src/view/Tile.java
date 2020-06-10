package view;

import controller.CheckersApp;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * ячейка поля с размером и цветом
 */
public class Tile extends Rectangle {

    private Piece piece;

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Tile(boolean light, int x, int y) {
        setWidth(CheckersApp.TILE_SIZE);//указ размера тайла
        setHeight(CheckersApp.TILE_SIZE);

        relocate(x * CheckersApp.TILE_SIZE, y * CheckersApp.TILE_SIZE); //ставит узел на нужное место x*100 н*100

        setFill(light ? Color.valueOf("#805338") : Color.valueOf("#c9bb51"));//заполняется цвет красный или белый
    }
}
