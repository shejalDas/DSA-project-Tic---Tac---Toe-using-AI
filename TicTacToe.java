import java.util.Scanner;
import java.util.Stack;

public class TicTacToe {

    static char[][] board = new char[3][3];       // 2D array for the board
    static Stack<Integer> moves = new Stack<>();  // stack to store moves (row * 3 + col)

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // make the board empty
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c] = ' ';
            }
        }

        System.out.println("TIC-TAC-TOE: You = X, AI = O");
        System.out.println("Enter row (0-2) and then column (0-2).");
        System.out.println("Enter 9 as the row to undo your last move.");

        while (true) {
            printBoard();

            if (checkWin('X')) {
                System.out.println("You win!");
                break;
            }
            if (checkWin('O')) {
                System.out.println("AI wins!");
                break;
            }
            if (isFull()) {
                System.out.println("Game draw.");
                break;
            }

            // human turn (X plays when the stack size is even)
            if (moves.size() % 2 == 0) {
                System.out.print("Enter row: ");
                int row = sc.nextInt();

                if (row == 9) {
                    undo();
                    continue;
                }

                System.out.print("Enter column: ");
                int col = sc.nextInt();

                if (row < 0 || row > 2 || col < 0 || col > 2) {
                    System.out.println("Invalid move. Use numbers 0 to 2.");
                } else if (board[row][col] != ' ') {
                    System.out.println("Cell occupied. Choose another cell.");
                } else {
                    board[row][col] = 'X';
                    moves.push(row * 3 + col);   // push move on the stack
                    System.out.println("Move accepted. Board updated.");
                }
            }
            // AI turn
            else {
                aiMove();
            }
        }
    }

    // show the board
    static void printBoard() {
        System.out.println();
        System.out.println("    0   1   2");
        for (int r = 0; r < 3; r++) {
            System.out.println(r + "   " + board[r][0] + " | " + board[r][1] + " | " + board[r][2]);
            if (r < 2) {
                System.out.println("   ---+---+---");
            }
        }
        System.out.println();
    }

    // check if the given player has won
    static boolean checkWin(char p) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == p && board[i][1] == p && board[i][2] == p) return true;  // row
            if (board[0][i] == p && board[1][i] == p && board[2][i] == p) return true;  // column
        }
        if (board[0][0] == p && board[1][1] == p && board[2][2] == p) return true;      // diagonal
        if (board[0][2] == p && board[1][1] == p && board[2][0] == p) return true;      // diagonal
        return false;
    }

    // check if the board is full
    static boolean isFull() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') return false;
            }
        }
        return true;
    }

    // undo: remove the AI's last move and the player's last move
    static void undo() {
        if (moves.size() < 2) {
            System.out.println("Nothing to undo.");
            return;
        }
        for (int i = 0; i < 2; i++) {
            int m = moves.pop();             // pop move from the stack
            board[m / 3][m % 3] = ' ';
        }
        System.out.println("Last moves undone.");
    }

    // AI chooses the best move using backtracking (minimax)
    static void aiMove() {
        int bestScore = -2;
        int bestRow = 0;
        int bestCol = 0;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    board[r][c] = 'O';                // try the move
                    int score = minimax(false);
                    board[r][c] = ' ';                // backtrack (remove the try)
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = r;
                        bestCol = c;
                    }
                }
            }
        }

        board[bestRow][bestCol] = 'O';
        moves.push(bestRow * 3 + bestCol);
        System.out.println("AI chooses row " + bestRow + ", column " + bestCol + ".");
    }

    // returns +1 if AI wins, -1 if human wins, 0 if draw
    static int minimax(boolean aiTurn) {
        if (checkWin('O')) return 1;
        if (checkWin('X')) return -1;
        if (isFull()) return 0;

        int best;
        if (aiTurn) best = -2;
        else best = 2;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r][c] == ' ') {
                    if (aiTurn) board[r][c] = 'O';
                    else board[r][c] = 'X';

                    int score = minimax(!aiTurn);

                    board[r][c] = ' ';               // backtrack

                    if (aiTurn && score > best) best = score;
                    if (!aiTurn && score < best) best = score;
                }
            }
        }
        return best;
    }
}