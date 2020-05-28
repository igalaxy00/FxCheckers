package controller;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import view.*;
import view.Tile;


public class CheckersApp extends Application {

    public static final int TILE_SIZE = 100;//размер тайла
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;
    private boolean step = true;

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
                    piece = makePiece(PieceType.RED, x, y);
                }
                if (y== 6 || y==5) {
                    piece = makePiece(PieceType.WHITE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);//незнаю зачем
                    pieceGroup.getChildren().add(piece); //добавление ячейки в группу если ячейка есть
                }
            }
        }

        return root;
    }

    /**
     * Метод ниже обрабатывает 2 из  4 вариантов как ходит дамка
     * */
    private MoveResult cut1 (int x1, int y1, int y2, Piece piece){
        int killed = -1;
        for(int i = y1+1;i<y2 ; i++) {
            if (board[x1][i].hasPiece()){
                if (piece.getType() == PieceType.WHITEKING) {//если белай дамка
                    if ((board[x1][i].getPiece().getType() == PieceType.RED ||
                            board[x1][i].getPiece().getType() == PieceType.REDKING)) {
                        if (killed == -1)
                            killed = i;
                        else return new MoveResult(MoveType.NONE);
                    }else if ((board[x1][i].getPiece().getType() == PieceType.WHITE ||
                            board[x1][i].getPiece().getType() == PieceType.WHITEKING) ){//лево тру , право фолз
                        return new MoveResult(MoveType.NONE);
                    }
                }
                if (piece.getType() == PieceType.REDKING) {//если белай дамка
                    if ((board[x1][i].getPiece().getType() == PieceType.WHITE ||
                            board[x1][i].getPiece().getType() == PieceType.WHITEKING)) {
                        if (killed == -1)
                            killed = i;
                        else return new MoveResult(MoveType.NONE);
                    }else if ((board[x1][i].getPiece().getType() == PieceType.RED ||
                            board[x1][i].getPiece().getType() == PieceType.REDKING) ){//лево тру , право фолз
                        return new MoveResult(MoveType.NONE);
                    }
                }
            }
        }
        if (killed!= -1)
            return new MoveResult(MoveType.KILL, board[x1][killed].getPiece());
        return new MoveResult(MoveType.NORMAL);
    }


    /**
     * Метод ниже обрабатывает 2 из  4 вариантов как ходит дамка
     * */
    private MoveResult cut2 (int x1, int x2, int y1, Piece piece){
        int killed = -1;
        for(int i = x1+1;i<x2 ; i++) {
            if (board[i][y1].hasPiece()){
                if (piece.getType() == PieceType.WHITEKING) {//если белай дамка
                    if ((board[i][y1].getPiece().getType() == PieceType.RED ||
                            board[i][y1].getPiece().getType() == PieceType.REDKING)) {
                        if (killed == -1)
                            killed = i;
                        else return new MoveResult(MoveType.NONE);
                    } else if ((board[i][y1].getPiece().getType() == PieceType.WHITE ||
                            board[i][y1].getPiece().getType() == PieceType.WHITEKING) ){//лево тру , право фолз
                        return new MoveResult(MoveType.NONE);
                    }
                }
                if (piece.getType() == PieceType.REDKING) {//если белай дамка
                    if ((board[i][y1].getPiece().getType() == PieceType.WHITE ||
                            board[i][y1].getPiece().getType() == PieceType.WHITEKING)) {
                        if (killed == -1)
                            killed = i;
                        else return new MoveResult(MoveType.NONE);
                    }else if ((board[i][y1].getPiece().getType() == PieceType.RED ||
                            board[i][y1].getPiece().getType() == PieceType.REDKING) ){//лево тру , право фолз
                        return new MoveResult(MoveType.NONE);
                    }
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
        if (board[newX][newY].hasPiece() ||newX<1 || newY<1|| newX>WIDTH || newY>HEIGHT)
            return new MoveResult(MoveType.NONE);
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
        }
        return new MoveResult(MoveType.NONE);
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

        if (step) {
            if (piece.getType() == PieceType.RED || piece.getType() == PieceType.REDKING) {
                return new MoveResult(MoveType.NONE);
            }
        } else {
            if (piece.getType() == PieceType.WHITE || piece.getType() == PieceType.WHITEKING) {
                return new MoveResult(MoveType.NONE);
            }
        }
        boolean anyMoved = false;
        boolean tryKill = ((newY == y0 && (Math.abs(newX - x0) == 2) ||
                newX == x0 && (Math.abs(newY - y0) == 2))&& !board[newX][newY].hasPiece());
        for (int i = 0; i < 8; i++) {
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
                if (tryKill) {//если x отличается на 2 И y =2 или -2
                    if (!board[newX][newY].hasPiece()){
                        if (board[x1][y1].hasPiece() && piece.getType() == PieceType.WHITE && (board[x1][y1].getPiece().getType() == PieceType.RED
                                || board[x1][y1].getPiece().getType() == PieceType.REDKING) ||
                                piece.getType() == PieceType.RED &&
                                        (board[x1][y1].getPiece().getType() == PieceType.WHITE
                                                || board[x1][y1].getPiece().getType() == PieceType.WHITEKING)){
                            return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());}}
                } else
                    return new MoveResult(MoveType.NONE);
            }
        } else {
            if (piece.getType() == PieceType.WHITEKING || piece.getType() == PieceType.REDKING) {//ход дамки
                return kingKill(x0, newX, y0, newY, piece);
            } else {
                if (newX < 1 || newY < 1 || newX > WIDTH || newY > HEIGHT || board[newX][newY].hasPiece() ||
                        (piece.getType() == PieceType.WHITE && newY - y0 == 1 || ((newX - x0 == 1 && y0 - newY == 1) || (x0 - newX == 1 && y0 - newY == 1))) ||
                        (piece.getType() == PieceType.RED && newY - y0 == -1 || ((x0 - newX == 1 && newY - y0 == 1) || (newX - x0 == 1 && newY - y0 == 1)))
                ) {
                    return new MoveResult(MoveType.NONE);//возвращается none значит не полилось передвинуть шашку
                }
                if ((Math.abs(newX - x0) == 1 && newY == y0) ||
                        (Math.abs(newY - y0) == 1 && newX == x0)) {//если x отличается на 1 И направлеy
                    return new MoveResult(MoveType.NORMAL);//движение обычное
                } else if (tryKill) {//если x отличается на 2 И y =2 или -2
                    int x1 = x0 + (newX - x0) / 2;
                    int y1 = y0 + (newY - y0) / 2;
                    if (board[x1][y1].hasPiece())
                        if (piece.getType() == PieceType.WHITE &&
                                (board[x1][y1].getPiece().getType() == PieceType.RED
                                        || board[x1][y1].getPiece().getType() == PieceType.REDKING) ||
                                piece.getType() == PieceType.RED &&
                                        (board[x1][y1].getPiece().getType() == PieceType.WHITE
                                                || board[x1][y1].getPiece().getType() == PieceType.WHITEKING))
                            return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());

                }
            }
        }
        return new MoveResult(MoveType.NONE);//если ничего не прошло то резултьтат// нан
    }




    /**
     *Метод ниже проверяет одно направление относительно нашей шашки .
     *  Проверяет есть ли в этом направлении шашки котороые можно съесть
     *  @param x указывает на координату X где находится наша шашка
     * @param y указывает на координату Y где находится наша шашка
     * @param changeX указывает дельту/направление по оси X
     * @param changeY указывает дельту/направление по оси Y
     */
    private boolean nearCheck (int x, int y ,int changeX ,int changeY){
        try {
            return (board[x+changeX][x+changeX]!=null&&
                    board[x+changeX*2][y+changeY*2]!=null &&
                    board[x+changeX][y+changeY].hasPiece()&&
                    !board[x+changeX*2][y+changeY*2].hasPiece());
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
    }

    /**
     *Буду признателен если подскажете почему этот метод "isNearPiece" работает некорректно
     * и пишет что рядом есть шашки которые можно съесть а их нет при случае когда шашка ест по горизонтали что неверно
     * Метод ниже проверяет есть ли рядом шашки которые можно съесть
     * никак не могу решить 1 проблему а без неё очередность ходов ломается
     * @param x указывает на координату X где находится наша шашка
     * @param y указывает на координату Y где находится наша шашка
     * @return возвращает есть ли рядом шашки которые можно съесть
     */
    private boolean isNearPiece ( int x , int y) {

        boolean isWhite = board[x][y].getPiece().getType()==PieceType.WHITE;

        if (board[x][y].getPiece().getType()==PieceType.WHITEKING || isWhite)
        {
            return   (nearCheck(x,y,0,-1)&&(board[x][y-1].getPiece().getType() == PieceType.RED|| board[x][y-1].getPiece().getType() == PieceType.REDKING))||
                    (nearCheck(x,y,1,0)&& (board[x+1][y].getPiece().getType() == PieceType.RED|| board[x+1][y].getPiece().getType() == PieceType.REDKING ))||
                    (nearCheck(x,y,-1,0)&& (board[x-1][y].getPiece().getType() == PieceType.RED|| board[x-1][y].getPiece().getType() == PieceType.REDKING));
        }else
            return  (nearCheck(x,y,0,1)&&(board[x][y-1].getPiece().getType() == PieceType.WHITE|| board[x][y-1].getPiece().getType() == PieceType.WHITEKING))||
                    (nearCheck(x,y,1,0)&& (board[x+1][y].getPiece().getType() == PieceType.WHITE|| board[x+1][y].getPiece().getType() == PieceType.WHITEKING) )||
                    (nearCheck(x,y,-1,0)&&( board[x-1][y].getPiece().getType() == PieceType.WHITE|| board[x-1][y].getPiece().getType() == PieceType.WHITEKING));
    }


private boolean checkBoard (){
    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (board[i][j] != null &&
                    board[i][j].hasPiece() &&
                    isNearPiece(i,j) &&
                    board[i][j].getPiece().wasMoved()){
                return true;
            }
        }
    }
    return false;
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
    private Piece makePiece(PieceType type ,  int x, int y) {
        Piece piece = new Piece(type , false, x, y);//шашка с типом по координатам x y

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
            if (newY<8 && newY>0 ){
                switch (result.getType()) {
                    case NONE:
                        piece.abortMove();
                        break;
                    case NORMAL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в старом месте нал
                        board[newX][newY].setPiece(piece);//в новом месте шашка
                        step = !step;//переход ходя после обычного хода
                        break;
                    case KILL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в новом месте шашка
                        board[newX][newY].setPiece(piece);
                        //никак не могу решить 1 проблему а без неё очередность ходов ломается
                        // Буду признателен если подскажете почему метод "isNearPiece" работает некорректно и
                        // пишет что рядом есть шашки которые можно съесть а их нет (при случае когда шашка есть по горизонтали)
                        if (isNearPiece( newX, newY)){
                            piece.setMoved(true);
                        }else{
                            piece.setMoved(false);
                            step = !step;    //переход хода если больше некого есть
                        }
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroup.getChildren().remove(otherPiece);
                        break;
                }}
            if (newY==7 || newY==0){
                switch (result.getType()) {
                    case NONE:
                        piece.abortMove();
                        break;
                    case NORMAL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);//в старом месте нал
                        if(piece.getType()==PieceType.WHITE)
                            piece.setType(PieceType.WHITEKING);
                        if(piece.getType()==PieceType.RED)
                            piece.setType(PieceType.REDKING);
                        board[newX][newY].setPiece(piece);//в новом месте шашка
                        step = !step;//переход ходя после обычного хода
                        break;
                    case KILL:
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        if(piece.getType()==PieceType.WHITE)
                            piece.setType(PieceType.WHITEKING);
                        if(piece.getType()==PieceType.RED)
                            piece.setType(PieceType.REDKING);
                        board[newX][newY].setPiece(piece);//в новом месте шашка

                        if (isNearPiece( newX, newY)){
                            piece.setMoved(true);
                        }else {
                            piece.setMoved(false);
                            step = !step;    //переход хода если больше некого есть
                        }
                        Piece otherPiece = result.getPiece();
                        board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                        pieceGroup.getChildren().remove(otherPiece);
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
