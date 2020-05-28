import controller.CheckersApp;
import controller.PieceType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class test1 {
        @Test
        void testBoard() {
            CheckersApp lol = new CheckersApp();
            lol.createContent();
            assertEquals(8,lol.board.length);
            assertTrue(lol.board[1][1].hasPiece());
            assertTrue(lol.board[6][6].hasPiece());
            assertSame(lol.board[6][6].getPiece().getType(), PieceType.WHITE);
            assertSame(lol.board[1][1].getPiece().getType(), PieceType.RED);
        }
}
