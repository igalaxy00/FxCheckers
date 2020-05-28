import controller.CheckersApp;
import controller.MoveResult;
import controller.MoveType;
import controller.PieceType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class test1 {
        @Test
        void testBoard() {
            CheckersApp bardTest = new CheckersApp();
            bardTest.createContent();
            assertEquals(8,bardTest.board.length);
            assertTrue(bardTest.board[1][1].hasPiece());
            assertTrue(bardTest.board[6][6].hasPiece());
            assertSame(bardTest.board[6][6].getPiece().getType(), PieceType.WHITE);
            assertSame(bardTest.board[1][1].getPiece().getType(), PieceType.RED);
        }
    @Test
    void testMove() {
        CheckersApp moveTest = new CheckersApp();
        moveTest.createContent();
        assertFalse(moveTest.isNearPiece(0,5));
    }
}
