package view;

import controller.Colour;
import controller.MoveResult;
import controller.MoveType;
import controller.Type;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;



public class CheckersApp extends Application {

    static final int TILE_SIZE = 100;//размер тайла
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;
    private boolean step = true; //тру белые фолз красные
    public Tile[][] board = new Tile[WIDTH][HEIGHT];//массив клеток

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    /**
     *Метод ниже создаёт поле шашек и расставляет шашки на изначальные месте
     * @return корневой узел
     */
    public Parent createContent() {
        AnchorPane  root = new AnchorPane ();
        root.setPrefSize((WIDTH) * TILE_SIZE, (HEIGHT) * TILE_SIZE); //размер поля
        root.getChildren().addAll(tileGroup, pieceGroup);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);// (выбор цвета маркер , x,  y)
                board[x][y] = tile;//добавление клетки на поле

                tileGroup.getChildren().add(tile);//добавление тайла в группу
                Piece piece = null;
                if (y ==2 || y==1) {
                    piece = makePiece(Colour.RED, x, y, Type.PAWN);
                }
                if (y== 6 || y==5) {
                    piece = makePiece(Colour.WHITE, x, y,Type.PAWN);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece); //добавление ячейки в группу если ячейка есть
                }
            }
        }
        return root;
    }


    /**
     * Я не смог обьеденить cut1 и cut2 так как там циклы по разым осям
     */
    private MoveResult cut1(int x1, int y1, int y2, Piece piece){
        int killed = -1;
        for(int i = y1+1;i<y2 ; i++) {
            if (board[x1][i].hasPiece()){
                if (board[x1][i].getPiece().getColour()!=piece.getColour()){//если цвет это противник то
                    if (killed == -1)
                        killed = i;
                    else {
                        return new MoveResult(MoveType.NONE);}//если на пути 2 шашки
                }
            }
        }
        if (killed!= -1)
            return new MoveResult(MoveType.KILL, board[x1][killed].getPiece());
        return new MoveResult(MoveType.NORMAL);
    }


    private MoveResult cut2(int x1, int x2, int y1, Piece piece){
        int killed = -1;
        for(int i = x1+1;i<x2 ; i++) {
            if (board[i][y1].hasPiece()){
                if (board[i][y1].getPiece().getColour()!=piece.getColour()){//если цвет это противник то
                    if (killed == -1)
                        killed = i;
                    else {

                        return new MoveResult(MoveType.NONE);}//если на пути 2 шашки
                }
            }
        }
        if (killed!= -1)
            return new MoveResult(MoveType.KILL, board[killed][y1].getPiece());
        return new MoveResult(MoveType.NORMAL);
    }



    /**
     * Метод ниже обрабатывает то как ходит дамка
     * @param piece передаём саму шашку
     * @param x0 начальная кордината X шашки
     * @param y0 начальная кордината Y шашки
     * @param newX указывает на координату X куда мы пытаемся подвинуть шашку
     * @param newY указывает на координату Y куда мы пытаемся подвинуть шашку
     * @return возвращает результат хода.
     */
    private MoveResult kingKill(int x0 , int newX , int y0 , int newY , Piece piece ) {
        if (newX<0 || newY<0 || newX>WIDTH-1 || newY>HEIGHT-1 || board[newX][newY].hasPiece() ){
            return new MoveResult(MoveType.NONE);}
        if (x0 == newX) {
            if (y0 < newY)
                return cut1(x0, y0, newY, piece);
            else if (y0 > newY)
                return cut1(x0, newY, y0, piece);
        }
        if (y0 == newY) {
            if (x0 < newX)
                return cut2(x0, newX, y0, piece);
            else if (x0 > newX)
                return cut2(newX, x0, y0, piece);
        }{

            return new MoveResult(MoveType.NONE);}
    }

    private void checkColour(boolean step){//расставляет wasmoved тем которые могут етсь
        // System.out.println("нначало метода");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (step && board[i][j] != null && board[i][j].hasPiece() && board[i][j].getPiece().getColour()==Colour.WHITE ){
                    if (isNearPiece(i,j)){//есть ли рядом красные которые можно есть
                        System.out.println("белые где то могут есть x "+i+" y "+j);
                        board[i][j].getPiece().setMoved(true);
                    }
                }
                if (!step && board[i][j] != null && board[i][j].hasPiece() && board[i][j].getPiece().getColour()==Colour.RED ){
                    if (isNearPiece(i,j)){//есть ли рядом белые которые можно есть
                        board[i][j].getPiece().setMoved(true);
                    }
                }
            }
        }
    }


    private void changeStep(int newY , Piece piece){//чтобы не давать до ход дамкам
        if (newY==0|| newY==7){//если шашка перешла в дамку то меняем ход
            if (piece.getColour()==Colour.WHITE){
                step = false;
            }
            if(piece.getColour()==Colour.RED)
                step=true;
        }
    }

    /**
     * Метод ниже проверяет что произойдёт если попробовать переместить шашку на новую клетку
     * @param piece сама шашка
     * @param newX указывает на координату X куда мы пытаемся подвинуть шашку
     * @param newY указывает на координату Y куда мы пытаемся подвинуть шашку
     * @return возвращает результат хода.
     */
    private MoveResult tryMove(Piece piece, int newX, int newY) {//возможно ли сюда перейти
        int x0 = toBoard(piece.getOldX());//начальный x
        int y0 = toBoard(piece.getOldY());//начальный y


        if (newX<0 || newY<0 || newX>WIDTH-1 || newY>HEIGHT-1 || board[newX][newY].hasPiece() ){
            return new MoveResult(MoveType.NONE);}


        if (step) {
            if (piece.getColour() == Colour.RED) {
                return new MoveResult(MoveType.NONE);
            }
        } else {
            if (piece.getColour() == Colour.WHITE ) {
                return new MoveResult(MoveType.NONE);
            }
        }
        boolean anyMoved = false;
        boolean tryKill = (newY == y0 && Math.abs(newX - x0) == 2 || newX == x0 && Math.abs(newY - y0) == 2) && !board[newX][newY].hasPiece();
        for (int i = 0; i < 8; i++) {//есть ли хоть одна возмувд
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null && board[i][j].hasPiece() && board[i][j].getPiece().wasMoved()){
                    anyMoved = true;
                }
            }
        }
        if (anyMoved) {
            if (piece.wasMoved()) {
                int x1 = x0 + (newX - x0) / 2;
                int y1 = y0 + (newY - y0) / 2;

                if (piece.getType()==Type.PAWN && board[x1][y1].hasPiece()&& piece.getColour()!=board[x1][y1].getPiece().getColour()){
                    if (tryKill) {//если x отличается на 2 И y =2 или -2 ест не дамка
                        if (!board[newX][newY].hasPiece()){
                            if (board[x1][y1].hasPiece()&& piece.getColour()!=board[x1][y1].getPiece().getColour()){
                                changeStep(newY,piece);
                                /* if (newY==0|| newY==7){//если шашка перешла в дамку то меняем ход
                                if (piece.getColour()==Colour.WHITE){
                                    step = false;
                                }
                                if(piece.getColour()==Colour.RED)
                                    step=true;
                            }*/
                                return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());}
                        }
                    } else
                        return new MoveResult(MoveType.NONE);}

                if (piece.getType()==Type.KING){
                    if (kingKill(x0, newX, y0, newY, piece).getType()==MoveType.NORMAL|| kingKill(x0, newX, y0, newY, piece).getType()==MoveType.NONE){
                        return new MoveResult(MoveType.NONE);}
                    if (kingKill(x0, newX, y0, newY, piece).getType()==MoveType.KILL)
                        return kingKill(x0, newX, y0, newY, piece);
                }
            }
        } else {
            if (piece.getType() == Type.KING ) {//ход дамки
                if (board[newX][newY].hasPiece()){
                    return new MoveResult(MoveType.NONE);}
                return kingKill(x0, newX, y0, newY, piece);
            } else {//если pawn
                if (board[newX][newY].hasPiece() || piece.getColour() == Colour.WHITE && newY - y0 == 1 || newX - x0 == 1 && y0 - newY == 1 || x0 - newX == 1 && y0 - newY == 1 || piece.getColour() == Colour.RED && newY - y0 == -1 || x0 - newX == 1 && newY - y0 == 1 || newX - x0 == 1 && newY - y0 == 1
                ) {
                    return new MoveResult(MoveType.NONE);//возвращается none значит не полилось передвинуть шашку
                }
                if ((Math.abs(newX - x0) == 1 && newY == y0) ||
                        (Math.abs(newY - y0) == 1 && newX == x0)) {//если x отличается на 1 И направлеy
                    //changeStep(newY,piece);
                    return new MoveResult(MoveType.NORMAL);//движение обычное
                } else if (tryKill) {//если x отличается на 2 И y =2 или -2
                    int x1 = x0 + (newX - x0) / 2;
                    int y1 = y0 + (newY - y0) / 2;
                    if (board[x1][y1].hasPiece())
                        if (piece.getColour()!=board[x1][y1].getPiece().getColour()){
                            changeStep(newY,piece);
                            return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());}

                }
            }
        }
        return new MoveResult(MoveType.NONE);//если ничего не прошло то резултьтат// нан
    }




    /**
     *  Метод ниже проверяет одно направление относительно нашей шашки .
     *  Проверяет есть ли в этом направлении шашки котороые можно съесть
     *  @param x указывает на координату X где находится наша шашка
     * @param y указывает на координату Y где находится наша шашка
     * @param changeX указывает дельту/направление по оси X
     * @param changeY указывает дельту/направление по оси Y
     */
    private boolean nearCheck (int x, int y ,int changeX ,int changeY){
        if (x+changeX>7 || y+changeY>7 || x+changeX*2>7 || y+changeY*2>7 || x+changeX<0 || y+changeY<0 || x+changeX*2<0 || y+changeY*2<0)
            return false;
        return (board[x+changeX][y+changeY]!=null&&//в указанной клетке не пустота
                board[x+changeX*2][y+changeY*2]!=null &&// через клетку не пустота
                board[x+changeX][y+changeY].hasPiece()&&//в указанной клетке есть шашка
                !board[x+changeX*2][y+changeY*2].hasPiece());//через клетку нет шашки
    }

    /**
     * @param x указывает на координату X где находится наша шашка
     * @param y указывает на координату Y где находится наша шашка
     * @return возвращает есть ли рядом шашки которые можно съесть
     */
    public boolean isNearPiece ( int x , int y) {
        boolean isWhite = board[x][y].getPiece().getColour()== Colour.WHITE ;
        if (board[x][y].getPiece().getType()==Type.PAWN) {
            if (isWhite) {
                return (nearCheck(x, y, 0, -1) && (board[x][y - 1].getPiece().getColour() == Colour.RED)) ||
                        (nearCheck(x, y, 1, 0) && (board[x + 1][y].getPiece().getColour() == Colour.RED)) ||
                        (nearCheck(x, y, -1, 0) && (board[x - 1][y].getPiece().getColour() == Colour.RED));
            } else {
                return (nearCheck(x, y, 0, 1) && (board[x][y + 1].getPiece().getColour() == Colour.WHITE)) ||
                        (nearCheck(x, y, 1, 0) && (board[x + 1][y].getPiece().getColour() == Colour.WHITE)) ||
                        (nearCheck(x, y, -1, 0) && (board[x - 1][y].getPiece().getColour() == Colour.WHITE));
            }
        }else if(board[x][y].getPiece().getType()==Type.KING){
            if (isWhite){
                return gorizontalKingPlus(x,y,Colour.WHITE)||gorizontalKingMinus(x,y,Colour.WHITE)
                        ||verticalKingPlus(x,y,Colour.WHITE)||verticalKingMinus(x,y,Colour.WHITE)
                        /*(nearCheck(x, y, 0, -1) && (board[x][y - 1].getPiece().getColour() == Colour.RED)) ||
                                (nearCheck(x, y, 0, 1) && (board[x][y + 1].getPiece().getColour() == Colour.RED)) ||
                                (nearCheck(x, y, 1, 0) && (board[x + 1][y].getPiece().getColour() == Colour.RED)) ||
                                (nearCheck(x, y, -1, 0) && (board[x - 1][y].getPiece().getColour() == Colour.RED))*/;}
        }else
            return gorizontalKingPlus(x,y,Colour.RED)||gorizontalKingMinus(x,y,Colour.RED)
                    || verticalKingPlus(x,y,Colour.RED)||verticalKingMinus(x,y,Colour.RED)
                    /*(nearCheck(x, y, 0, -1) && (board[x][y - 1].getPiece().getColour() == Colour.WHITE)) ||
                            (nearCheck(x, y, 0, 1) && (board[x][y + 1].getPiece().getColour() == Colour.WHITE)) ||
                            (nearCheck(x, y, 1, 0) && (board[x + 1][y].getPiece().getColour() == Colour.WHITE)) ||
                            (nearCheck(x, y, -1, 0) && (board[x - 1][y].getPiece().getColour() == Colour.WHITE))*/;
        return false;
    }
    private boolean gorizontalKingPlus(int x0 , int y0 , Colour colour){
        // System.out.println("начало горизонтал");
        for (int i = x0 ; i<8 ; i++){
            if (i!=x0)
                if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()== colour ){//если на пути стоит такой же цвет
                    return false;}
            if (board[i][y0].hasPiece() && i<7 && board[i+1][y0].hasPiece())
                return false;
            if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()!= colour && i<7 && !board[i+1][y0].hasPiece()){
                return true;}// ЕСТЬ ШАШКА ОНА ДРУГОГО ЦВЕТА

        }
        /* for (int i = x0 ; i>-1 ; i--){
            if (i!=x0)
                if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()== colour ){//если на пути стоит такой же цвет
                    return false;}
            if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()!= colour && i>0 && !board[i+1][y0].hasPiece()){
                return true;}
        }*/
        return false;
    }
    private boolean gorizontalKingMinus(int x0 , int y0 , Colour colour){
        for (int i = x0 ; i>-1 ; i--){
            if (i!=x0)
                if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()== colour ){//если на пути стоит такой же цвет
                    return false;}
            if (board[i][y0].hasPiece() && i>0 && board[i-1][y0].hasPiece())
                return false;
            if (board[i][y0].hasPiece() && board[i][y0].getPiece().getColour()!= colour && i>0 && !board[i-1][y0].hasPiece()){
               // System.out.println("горизонталь-");
                return true;}

        }
        return false;
    }





private boolean verticalKingPlus(int x0, int y0 ,Colour colour){
    for (int i = y0 ; i<8 ; i++){
        if (i!=y0)
            if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()==colour)
                return false;
        if(board[x0][i].hasPiece() && i<7 && board[x0][i+1].hasPiece())
            return false;
        if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()!=colour&& i<7 && !board[x0][i+1].hasPiece()){
           // System.out.println("x0  "+x0+" y "+i);
            return true;}
    }
    /*for (int i = y0 ; i>-1 ; i--){
        if (i!=y0)
            if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()==colour)
                return false;
        if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()!=colour&& i>0  && !board[x0][i-1].hasPiece())
            return true;
    }*/
    return false;
}
    private boolean verticalKingMinus(int x0, int y0 ,Colour colour){
        for (int i = y0 ; i>-1 ; i--){
            if (i!=y0)
                if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()==colour){
                    return false;}
            if(board[x0][i].hasPiece() && i>0 && board[x0][i-1].hasPiece())
                return false;
            if(board[x0][i].hasPiece()&& board[x0][i].getPiece().getColour()!=colour&& i>0  && !board[x0][i-1].hasPiece()){
           //     System.out.println("вертикаль-");
                return true;}
        }
        return false;
    }



private void clearMoved(boolean step){
    for (int i=0 ; i<8 ; i++)
        for (int j=0 ; j<8 ; j++){
            if(step && board[i][j].hasPiece() && board[i][j].getPiece().getColour()==Colour.RED)
                board[i][j].getPiece().setMoved(false);

            if(!step && board[i][j].hasPiece() && board[i][j].getPiece().getColour()==Colour.WHITE)
                board[i][j].getPiece().setMoved(false);
        }
}

    private int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Шашки");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * Метод перемещает саму шашку на новое место после того как получил результат хода и удаляет её с предыдущего места
     * @param type указывает на тип шашки которую двигаем
     * @param x указывает на координату X где была наша шашка
     * @param y указывает на координату Y где была наша шашка
     * @return возвращает есть ли рядом шашки которые можно съесть
     */
    private Piece makePiece(Colour colour, int x, int y, Type type) {
        Piece piece = new Piece(type, colour, false, x, y);//шашка с типом по координатам x y

        piece.setOnMouseReleased(e -> {
            int newX = toBoard(piece.getLayoutX());//присваевает x шашки в newX
            int newY = toBoard(piece.getLayoutY());
            MoveResult result;
            if (newX < 0 || newY < 0 || newX >= WIDTH+1 || newY >= HEIGHT+1) {
                result = new MoveResult(MoveType.NONE);//если мы хотим переместиться вне поля то NONE
            } else {
                result = tryMove(piece, newX, newY);//иначе попробоавть подвинуть в новый x y шашку
            }
            int x0 = toBoard(piece.getOldX());//присваивает старый X
            int y0 = toBoard(piece.getOldY());

            if (newY!=7 && newY!=0 ){//подумать
                switch (result.getType()) {
                    case NONE:
                        piece.abortMove();
                        break;
                    case NORMAL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в старом месте нал
                        board[newX][newY].setPiece(piece);//в новом месте шашка
                        step = !step;//переход ходя после обычного хода
                        checkColour(step);
                        clearMoved(step);
                        break;
                    case KILL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в старом месте нал
                        board[newX][newY].setPiece(piece);
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroup.getChildren().remove(otherPiece);

                        if (isNearPiece(newX,newY)){
                            piece.setMoved(true);
                        }else{
                            piece.setMoved(false);
                            step = !step;    //переход хода если больше некого есть
                            checkColour(step);
                            clearMoved(step);
                        }
                        break;
                }}
            if (newY==7 || newY==0){
                switch (result.getType()) {
                    case NONE:
                        piece.abortMove();
                        break;
                    case NORMAL:
                        // System.out.println("ход чей" + step);
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в старом месте нал
                        Piece piece1 = makePiece(piece.getColour(), newX, newY,Type.PAWN);
                        piece1.setMoved(false);
                        board[x0][y0].setPiece(null);
                        piece1.setType(Type.KING);
                        piece1.move(newX,newY);
                        board[newX][newY].setPiece(piece1);
                        pieceGroup.getChildren().add(piece1);
                        pieceGroup.getChildren().remove(piece);
                       // if (board[newX][newY].getPiece().getColour()==Colour.WHITE){
                        //    step=!step;}
                        step = !step;//переход ходя после обычного хода
                        //step = !step;
                        checkColour(step);
                        clearMoved(step);
                        break;
                    case KILL:
                        // board[newX][newY].setPiece(null);
                        Piece piece2 = makePiece(piece.getColour(), newX, newY,Type.KING);
                        board[x0][y0].setPiece(null);
                        // piece2.setType(Type.KING);
                        piece2.move(newX,newY);
                        board[newX][newY].setPiece(piece2);
                        pieceGroup.getChildren().add(piece2);
                        pieceGroup.getChildren().remove(piece);
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroup.getChildren().remove(otherPiece);
                        if (isNearPiece( newX, newY)){
                            piece.setMoved(true);
                        }else {
                            piece.setMoved(false);
                            if (board[newX][newY].getPiece().getColour()==Colour.WHITE){
                                step=!step;}
                            step = !step;    //переход хода если больше некого есть
                            checkColour(step);
                            clearMoved(step);
                        }
                        break;
                }
            }
        });

        return piece;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
