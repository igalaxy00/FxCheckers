package checkers;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.Piece;
import view.Tile;

public class CheckersApp extends Application {

    public static final int TILE_SIZE = 100;//размер тайла
    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;
    private boolean step = true;

    private Tile[][] board = new Tile[WIDTH+2][HEIGHT+2];//массив клеток

    private Group tileGroup = new Group();
    private Group pieceGroup = new Group();

    private Parent createContent() {
        GridPane root = new GridPane();
        root.setPrefSize((WIDTH) * TILE_SIZE, (HEIGHT) * TILE_SIZE); //размер поля
        root.getChildren().addAll(tileGroup, pieceGroup);

        for (int y = 1; y < HEIGHT+1; y++) {
            for (int x = 1; x < WIDTH+1; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);// (выбор цвета маркер , x,  y)
                board[x][y] = tile;//добавление клетки на поле

                tileGroup.getChildren().add(tile);//добавление тайла в группу
                Piece piece = null;
                if (y < 3) {
                    piece = makePiece(PieceType.RED, x, y);
                }
                if (y > 6) {
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

    private MoveResult tryMove(Piece piece, int newX, int newY) {//возможно ли сюда перейти
        int x0 = toBoard(piece.getOldX());//начальный x
        int y0 = toBoard(piece.getOldY());//начальный y

        if (step){
            if (piece.getType()==PieceType.RED||piece.getType()==PieceType.REDKING){
                return new MoveResult(MoveType.NONE);
            }
        }else {
            if (piece.getType()==PieceType.WHITE||piece.getType()==PieceType.WHITEKING){
                return new MoveResult(MoveType.NONE);
            }
        }

        boolean anyMoved = false;
         boolean tryKill = Math.abs(newX - x0) == 2 ||
                Math.abs(newY - y0) == 2;
         for (int i = 1 ; i<9;i++){
             for (int j = 1 ; j<9;j++){
                if(board[i][j]!=null && board[i][j].hasPiece()&& board[i][j].getPiece().wasMoved())
                    anyMoved = true;
             }
         }
         if (anyMoved){
        if(piece.wasMoved()){
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;
            if (tryKill) {//если x отличается на 2 И y =2 или -2
                if (board[x1][y1].hasPiece()&& !board[newX][newX].hasPiece())
                    if (piece.getType()==PieceType.WHITE && (board[x1][y1].getPiece().getType() == PieceType.RED
                            || board[x1][y1].getPiece().getType() == PieceType.REDKING )||
                            piece.getType()==PieceType.RED &&
                                    (board[x1][y1].getPiece().getType() == PieceType.WHITE
                                            || board[x1][y1].getPiece().getType() == PieceType.WHITEKING ))
                        return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());
            }else
                return new MoveResult(MoveType.NONE);
        }
         }else{
        if (piece.getType()==PieceType.WHITEKING ||piece.getType()==PieceType.REDKING){//ход дамки
            return kingKill(x0,newX , y0, newY , piece);
        }else {
            if ( newX<1 || newY<1|| newX>WIDTH || newY>HEIGHT || board[newX][newY].hasPiece() ||//если там есть уже шашка или если это невозможна клетка
                    (piece.getType()==PieceType.WHITE && newY-y0==1 || ((newX-x0==1 && y0-newY==1)|| (x0-newX==1 && y0-newY==1))) ||
                    (piece.getType()==PieceType.RED && newY-y0==-1 || ((x0-newX==1 && newY-y0==1)|| (newX-x0==1 && newY-y0==1)))
                   )
            {
            return new MoveResult(MoveType.NONE);//возвращается none значит не полилось передвинуть шашку
        }
        if ((Math.abs(newX - x0) == 1 && newY==y0 )||
                ( Math.abs(newY - y0) == 1 && newX==x0)) {//если x отличается на 1 И направлеy
            return new MoveResult(MoveType.NORMAL);//движение обычное
        }
        else if (tryKill) {//если x отличается на 2 И y =2 или -2
            int x1 = x0 + (newX - x0) / 2;
            int y1 = y0 + (newY - y0) / 2;
            if (board[x1][y1].hasPiece())
                if (    piece.getType()==PieceType.WHITE &&
                        (board[x1][y1].getPiece().getType() == PieceType.RED
                        || board[x1][y1].getPiece().getType() == PieceType.REDKING )||
                        piece.getType()==PieceType.RED &&
                                (board[x1][y1].getPiece().getType() == PieceType.WHITE
                                || board[x1][y1].getPiece().getType() == PieceType.WHITEKING ))
                    return new MoveResult(MoveType.KILL, board[x1][y1].getPiece());

        }
        }  }
        return new MoveResult(MoveType.NONE);//если ничего не прошло то резултьтат// нан
    }


    //проверка есть ли рядом шашки которые можно съесть
    private boolean isNearPiece (Piece piece, int newX , int newY) {//так же обработаны крайние случаи

        boolean isWhite = board[newX][newY].getPiece().getType()==PieceType.WHITE;

        if (board[newX][newY].getPiece().getType()==PieceType.WHITEKING || isWhite)//попробовать оставить проверку по всем направлениям и просто ловить налы
        {
            return (board[newX][newY-1]!=null&& board[newX][newY-2]!=null && board[newX][newY-1].hasPiece() && board[newX][newY-1].getPiece().getType() == PieceType.RED && !board[newX][newY-2].hasPiece()) ||
                    (board[newX][newY-1]!=null&& board[newX][newY-2]!=null &&board[newX][newY-1].hasPiece() && board[newX][newY-1].getPiece().getType() == PieceType.REDKING && !board[newX][newY-2].hasPiece()) ||
                    (board[newX+1][newY]!=null&& board[newX+2][newY]!=null &&board[newX+1][newY].hasPiece() && board[newX+1][newY].getPiece().getType() == PieceType.RED && !board[newX+2][newY].hasPiece()) ||
                    (board[newX+1][newY]!=null&& board[newX+2][newY]!=null &&board[newX+1][newY].hasPiece() && board[newX+1][newY].getPiece().getType() == PieceType.REDKING && !board[newX+2][newY].hasPiece()) ||
                    (board[newX-1][newY]!=null&& board[newX-2][newY]!=null &&board[newX-1][newY].hasPiece() && board[newX-1][newY].getPiece().getType() == PieceType.RED && !board[newX-2][newY].hasPiece()) ||
                    (board[newX-1][newY]!=null&& board[newX-2][newY]!=null &&board[newX-1][newY].hasPiece() && board[newX-1][newY].getPiece().getType() == PieceType.REDKING && !board[newX-2][newY].hasPiece());
        }else
            return (board[newX][newY+1]!=null&& board[newX][newY+2]!=null && board[newX][newY+1].hasPiece() && board[newX][newY+1].getPiece().getType() == PieceType.WHITE&& !board[newX][newY+2].hasPiece()) ||
                    (board[newX][newY+1]!=null&& board[newX][newY+2]!=null && board[newX][newY+1].hasPiece() && board[newX][newY+1].getPiece().getType() == PieceType.WHITEKING&& !board[newX][newY+2].hasPiece()) ||
                    (board[newX+1][newY]!=null&& board[newX+2][newY]!=null && board[newX+1][newY].hasPiece() && board[newX+1][newY].getPiece().getType() == PieceType.WHITE&& !board[newX+2][newY].hasPiece()) ||
                    (board[newX+1][newY]!=null&& board[newX+2][newY]!=null && board[newX+1][newY].hasPiece() && board[newX+1][newY].getPiece().getType() == PieceType.WHITEKING&& !board[newX+2][newY].hasPiece()) ||
                    (board[newX-1][newY]!=null&& board[newX-2][newY]!=null && board[newX-1][newY].hasPiece() && board[newX-1][newY].getPiece().getType() == PieceType.WHITE&& !board[newX-2][newY].hasPiece()) ||
                    (board[newX-1][newY]!=null&& board[newX-2][newY]!=null && board[newX-1][newY].hasPiece() && board[newX-1][newY].getPiece().getType() == PieceType.WHITEKING&& !board[newX-2][newY].hasPiece());//false когда рядом нет шашек которые можно съесть
    }




    private int toBoard(double pixel) {
        return (int)(pixel + TILE_SIZE / 2) / TILE_SIZE;//добавление на доску пиксель + размер тайла...
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Шашки");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
// добавить метку которая ходила(если рядом можно есть).Try move  Проверить все шашки и если нет шашек с меткой то обычный
// ход если шашка с меткой то может ходить только онаи можэет только есть
private Piece makePiece(PieceType type ,  int x, int y) {
    Piece piece = new Piece(type , false, x, y);//шашка с типом по координатам x y

    piece.setOnMouseReleased(e -> {
        int newX = toBoard(piece.getLayoutX());//присваевает x шашки в newX
        int newY = toBoard(piece.getLayoutY());
        MoveResult result;
        if (newX < 0 || newY < 0 || newX >= WIDTH+1 || newY >= HEIGHT+1) {
            //veefv
            result = new MoveResult(MoveType.NONE);//если мы хотим переместиться вне поля то NONE
        } else {
            result = tryMove(piece, newX, newY);//иначе попробоавть подвинуть в новый x y шашку
        }
        int x0 = toBoard(piece.getOldX());//присваивает старый X
        int y0 = toBoard(piece.getOldY());
        if (newY<8 && newY>0 ){
            System.out.println(newX);
            System.out.println(newY);
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
                    board[x0][y0].setPiece(null);
                    board[newX][newY].setPiece(piece);//в новом месте шашка
                    if (isNearPiece(piece, newX, newY)){
                        piece.setMoved(true);
                    }
                    if (!isNearPiece(piece, newX, newY)){
                        piece.setMoved(false);
                        step = !step;    //переход хода если больше некого есть
                    }
                    Piece otherPiece = result.getPiece();
                    board[toBoard(otherPiece.getOldX())][toBoard(otherPiece.getOldY())].setPiece(null);
                    pieceGroup.getChildren().remove(otherPiece);

                    System.out.println(isNearPiece(piece, newX, newY));
                    break;
            }}
        if (newY==8 || newY==1){
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
