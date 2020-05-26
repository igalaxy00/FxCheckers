package view;

import checkers.PieceType;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static checkers.CheckersApp.TILE_SIZE;


public class Piece extends StackPane {
    private PieceType type;
    private boolean moved;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {//моё
        this.type = type;
    }

    public void setMoved(boolean moved) {//моё
        this.moved = moved;
    }
    public boolean wasMoved() {
        return moved;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Piece(PieceType type,boolean moved , int x, int y) {
        this.type = type;
        this.moved = moved;
        move(x, y);

        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);//создание главного элипса
        ellipse.setFill(type == PieceType.RED //выбор как красить цвет
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));

        ellipse.setStroke(Color.BLACK);//цвет ободка
        ellipse.setStrokeWidth(TILE_SIZE * 0.03);//толищна ободка

       ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);//расположение черного элипса
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);//расположение черного элипса

        getChildren().addAll( ellipse);//добавление всех в группу

        setOnMousePressed(e -> {//запоминается x y когда нажимаешь на шашку
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);//перемещение в новый тайл
        });
    }

    public void move(int x, int y) {//?
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    public void abortMove() {
        relocate(oldX, oldY);//отбой перемещения
    }
}
