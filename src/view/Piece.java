package view;

import controller.Colour;
import controller.Type;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import static view.CheckersApp.TILE_SIZE;

/**
 * Шашка . Её размеры свойства и выставление на поле .
 */
public class Piece extends StackPane {
    private Colour colour;
    private Type type;
    private boolean moved;

    private double mouseX, mouseY;
    private double oldX, oldY;

    void setType(Type type) {
        this.type = type;
    }

    Type getType() {
        return type;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    void setMoved(boolean moved) {
        this.moved = moved;
    }
    boolean wasMoved() {
        return moved;
    }

    double getOldX() {
        return oldX;
    }

    double getOldY() {
        return oldY;
    }

    Piece(Type type, Colour colour, boolean moved, int x, int y) {
        this.type = type;
        this.colour = colour;
        this.moved = moved;
        move(x, y);

        Ellipse ellipse = new Ellipse(TILE_SIZE * 0.3125, TILE_SIZE * 0.26);//создание главного элипса
        ellipse.setFill(colour == Colour.RED //выбор как красить цвет
                ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));
        if (y==7 || y==0)
            ellipse.setStroke(Color.AQUAMARINE);

        if (y==7 || y==0){
            ellipse.setStroke(Color.AQUAMARINE);
            ellipse.setStrokeWidth(TILE_SIZE * 0.03);
        }else {
            ellipse.setStroke(Color.BLACK);//цвет ободка
            ellipse.setStrokeWidth(TILE_SIZE * 0.03);//толищна ободка
        }
        ellipse.setTranslateX((TILE_SIZE - TILE_SIZE * 0.3125 * 2) / 2);//расположение черного элипса
        ellipse.setTranslateY((TILE_SIZE - TILE_SIZE * 0.26 * 2) / 2);//расположение черного элипса


        getChildren().addAll( ellipse);//добавление всех в группу
        setOnMousePressed(e -> {//запоминается x y когда нажимаешь на шашку
                System.out.println("мауз прессед");
                mouseX = e.getSceneX();
                mouseY = e.getSceneY();
            });

            setOnMouseDragged(e -> {
                relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);//перемещение в новый тайл
            });


    }

    void move(int x, int y) {
        oldX = x * TILE_SIZE;
        oldY = y * TILE_SIZE;
        relocate(oldX, oldY);
    }

    void abortMove() {
        relocate(oldX, oldY);//отбой перемещения
    }
}
