import view.CheckersApp;
import controller.Colour;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class test1 {
        @Test
        void testBoard() {
            CheckersApp bardTest = new CheckersApp();
            bardTest.createContent();
            assertEquals(8,bardTest.board.length);
            assertFalse(bardTest.board[0][0].hasPiece());
            assertFalse(bardTest.board[0][0].hasPiece());
            assertTrue(bardTest.board[1][1].hasPiece());
            assertTrue(bardTest.board[6][6].hasPiece());
            assertSame(bardTest.board[6][6].getPiece().getColour(), Colour.WHITE);
            assertSame(bardTest.board[1][1].getPiece().getColour(), Colour.RED);
        }
    @Test
    void testMove() {
        CheckersApp moveTest = new CheckersApp();
        moveTest.createContent();
        assertFalse(moveTest.isNearPiece(0,5));
    }
}
