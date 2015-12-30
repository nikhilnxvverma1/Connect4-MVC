/**
 * Connect4Field Implementation: An implementation of the Connect4FieldInterface for the game
 *
 * @author Nikhil Verma
 * @version 1.1: Connect4Field.java
 *          Revisions:
 *          Revision Added a method for retrieving landing row for dropping in a column 10/02/2015 11:47 PM Nikhil Verma
 *          Initial revision
 */
public class Connect4Field implements Connect4FieldInterface {

    public static final int BOARD_MAX_WIDTH = 100;
    public static final int BOARD_MAX_HEIGHT = 100;
    public static final char INVALID_SLOT = ' ';
    public static final char EMPTY_SLOT = 'o';
    public static final int DEFAULT_WIDTH = 25;
    public static final int DEFAULT_HEIGHT = 9;

    public static final int MAX_SUPPORTED_PLAYER = 10;

    //a 2d char array will store the game state
    private int width;
    private int height;
    /**
     * 2d char array that stores the board
     * <p>
     * ooooooooooooooooooooooooo
     * ooooooooooooooooooooooo
     * ooooooooooooooooooooo
     * ooooooooooooooooooo
     * ooooooooooooooooo
     * ooooooooooooooo
     * ooooooooooooo
     * ooooooooooo
     * ooooooooo
     */
    private char board[][] = new char[BOARD_MAX_HEIGHT][BOARD_MAX_WIDTH];

    private PlayerInterface[] players = new PlayerInterface[MAX_SUPPORTED_PLAYER];//support for upto 10 players
    private int totalPlayers;

    public Connect4Field() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * checks if the given piece has a chance to connect at the specified location.
     *
     * @param board  the 2d board char array
     * @param width  width of the board
     * @param height height of the board
     * @param r      row location
     * @param c      column location
     * @param piece  the piece that might connect
     * @return true if it connects,false otherwise
     */
    public static boolean willConnect(char[][] board, int width, int height, int r, int c, char piece) {
        //check all 8 directions for figuring out if this element is a winner
        boolean connects = false;

        int piecesBefore;
        int piecesAfter;
        int i;

        //vertical
        i = 1;
        piecesBefore = 0;
        while (((r - i) >= 0) &&
                (i < 4) &&
                (board[r - i][c] == piece)) {
            piecesBefore++;
            i++;
        }

        i = 1;
        piecesAfter = 0;
        while (((r + i) < width) &&
                (i < 4) &&
                (board[r + i][c] == piece)) {
            piecesAfter++;
            i++;
        }
        if ((board[r][c] == EMPTY_SLOT) &&
                ((piecesBefore + piecesAfter) >= (4 - 1))
                ) {
            connects = true;
        }


        //horizontal
        i = 1;
        piecesBefore = 0;
        while (((c - i) >= 0) &&
                (i < 4) &&
                (board[r][c - i] == piece)) {
            piecesBefore++;
            i++;
        }

        i = 1;
        piecesAfter = 0;
        while (((c + i) < width) &&
                (i < 4) &&
                (board[r][c + i] == piece)) {
            piecesAfter++;
            i++;
        }
        if ((board[r][c] == EMPTY_SLOT) &&
                ((piecesBefore + piecesAfter) >= (4 - 1))
                ) {
            connects = true;
        }

        //left diagonal
        i = 1;
        piecesBefore = 0;
        while (((r - i) >= 0) &&
                ((c - i) >= 0) &&
                (i < 4) &&
                (board[r - i][c - i] == piece)) {
            piecesBefore++;
            i++;
        }

        i = 1;
        piecesAfter = 0;
        while (((c + i) < width) &&
                ((r + i) < height) &&
                (i < 4) &&
                (board[r+i][c + i] == piece)) {
            piecesAfter++;
            i++;
        }
        if ((board[r][c] == EMPTY_SLOT) &&
                ((piecesBefore + piecesAfter) >= (4 - 1))
                ) {
            connects = true;
        }

        //right diagonal
        i = 1;
        piecesBefore = 0;
        while (((r - i) >= 0) &&
                ((c + i) < width) &&
                (i < 4) &&
                (board[r - i][c + i] == piece)) {
            piecesBefore++;
            i++;
        }

        i = 1;
        piecesAfter = 0;
        while (((c - i) >=0) &&
                ((r + i) < height) &&
                (i < 4) &&
                (board[r+i][c - i] == piece)) {
            piecesAfter++;
            i++;
        }
        if ((board[r][c] == EMPTY_SLOT) &&
                ((piecesBefore + piecesAfter) >= (4 - 1))
                ) {
            connects = true;
        }

        return connects;
    }

    public Connect4Field(int width, int height) {
        this.width = width;
        this.height = height;
        initializeBoard(width, height);
    }

    private void initializeBoard(int width, int height) {

        //initialize the whole board with invalid slots
        for (int i = 0; i < BOARD_MAX_HEIGHT; i++) {
            for (int j = 0; j < BOARD_MAX_WIDTH; j++) {
                board[i][j] = INVALID_SLOT;
            }
        }

        //now make the board the whole board with invalid slots

        //keeps track of spaces before and after a row
        int k = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 2 * k; j++) {
                board[i][j + k] = EMPTY_SLOT;
            }
            k++;
        }
    }

    @Override
    public String toString() {
        final String NEWLINE = System.getProperty("line.separator");
        StringBuilder boardInString = new StringBuilder();
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {
                boardInString.append(board[i][j]);
            }
            boardInString.append(NEWLINE);
        }
        return boardInString.toString();
    }

    @Override
    public boolean checkIfPiecedCanBeDroppedIn(int column) {
        if (column < 0 || column > width) {
            return false;
        }
        int j = 0;
        while (board[j][column] == EMPTY_SLOT) {
            j++;
        }
        return j > 0;
    }

    @Override
    public void dropPieces(int column, char gamePiece) {
        int j = 0;
        while (board[j][column] == EMPTY_SLOT) {
            j++;
        }
        if (j > 0) {
            j -= 1;
            board[j][column] = gamePiece;
        }
    }

    @Override
    public boolean didLastMoveWin() {
        //check the entire board, to see if there is a winner
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char slot = board[i][j];
                if (slot != INVALID_SLOT && slot != EMPTY_SLOT) {
                    if (connectsWith4Others(slot, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * using the element at specified position,it tries to figure out if,
     * as a terminal it connects with 3 other pieces in 8 directions around it
     *
     * @param piece the piece it should be, to connect with
     * @param r     row
     * @param c     column
     * @return true if connects with 3 others(3+1=4), false otherwise
     */
    private boolean connectsWith4Others(char piece, int r, int c) {

        //check all 8 directions for figuring out if this element is a winner
        boolean connects = false;

        //up
        if ((r - 3 >= 0) &&
                board[r][c] == piece &&
                board[r - 1][c] == piece &&
                board[r - 2][c] == piece &&
                board[r - 3][c] == piece
                ) {
            connects = true;
        }

        //top right diagonal
        if ((r - 3 >= 0 && c + 3 < width) &&
                board[r][c] == piece &&
                board[r - 1][c + 1] == piece &&
                board[r - 2][c + 2] == piece &&
                board[r - 3][c + 3] == piece) {
            connects = true;
        }

        //right
        if ((c + 3 < width) &&
                board[r][c] == piece &&
                board[r][c + 1] == piece &&
                board[r][c + 2] == piece &&
                board[r][c + 3] == piece
                ) {
            connects = true;
        }

        //bottom right
        if ((r + 3 < height && c + 3 < width) &&
                board[r][c] == piece &&
                board[r + 1][c + 1] == piece &&
                board[r + 2][c + 2] == piece &&
                board[r + 3][c + 3] == piece) {
            connects = true;
        }

        //bottom
        if ((r + 3 < height) &&
                board[r][c] == piece &&
                board[r + 1][c] == piece &&
                board[r + 2][c] == piece &&
                board[r + 3][c] == piece
                ) {
            connects = true;
        }

        //bottom left diagonal
        if ((r + 3 < height && c - 3 >= 0) &&
                board[r][c] == piece &&
                board[r + 1][c - 1] == piece &&
                board[r + 2][c - 2] == piece &&
                board[r + 3][c - 3] == piece) {
            connects = true;
        }

        //left
        if ((c - 3 >= 0) &&
                board[r][c] == piece &&
                board[r][c - 1] == piece &&
                board[r][c - 2] == piece &&
                board[r][c - 3] == piece
                ) {
            connects = true;
        }

        //top left diagonal
        if ((r - 3 >= 0 && c - 3 < width) &&
                board[r][c] == piece &&
                board[r - 1][c - 1] == piece &&
                board[r - 2][c - 2] == piece &&
                board[r - 3][c - 3] == piece) {
            connects = true;
        }

        return connects;

    }

    @Override
    public boolean isItaDraw() {
        //if connection of 4 is not possible,in the entire board,
        //its a draw
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char slot = board[i][j];
                if (slot != INVALID_SLOT) {//we even allow the empty slot connection
                    if (connectsWith4Others(slot, i, j)) {
                        //so long as one connection exists
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void init(PlayerInterface playerA, PlayerInterface playerB) {
        this.players[0] = playerA;
        this.players[1] = playerB;
        totalPlayers = 2;
    }

    @Override
    public void playTheGame() {

        boolean gameOver = false;
        PlayerInterface winner = null;
        int playerTurn = 0;//index to the player ,whos turn is up
        totalPlayers = 2;
        int column = 0;
        do {

            //show the game board
            System.out.println(this);//calls toString
            System.out.println(availableInputLine());

            //Get the input from the current player
            System.out.print(players[playerTurn].getName() + "'s Turn:");
            boolean validMove = false;
            do {
                column = players[playerTurn].nextMove();
                validMove = checkIfPiecedCanBeDroppedIn(column);
                if (!validMove) {
                    if ((column < 0) || (column > width)) {
                        System.out.println("Please enter values within the specified range");
                    } else {
                        System.out.println("Piece cannot be dropped in column number" + column + ".Please try again");
                    }
                }
            } while (!validMove);

            //print the input column,and drop piece
            System.out.println(column);
            dropPieces(column, players[playerTurn].getGamePiece());

            //check for winner
            if (didLastMoveWin()) {
                winner = players[playerTurn];
                gameOver = true;
            }
            //check for draw
            else if (isItaDraw()) {
                gameOver = true;//no winner in this case
            }

            //set the next player in turn,circle back if needed
            playerTurn++;
            if (playerTurn >= totalPlayers) {
                playerTurn = 0;
            }
        } while (!gameOver);

        //print the ending game board state
        System.out.println(this);

        //if there is a winner show him
        if (winner != null) {
            System.out.println("The winner is " + winner.getName() + "!"+" ---> "+winner.getGamePiece());
            System.out.println("(Last piece was dropped at column : "+column+")");
        } else {
            System.out.println("Its a draw!");
        }
    }

    public int getLandingRow(int column){
        int j = 0;
        while (board[j][column] == EMPTY_SLOT) {
            j++;
        }
        j -= 1;
        return j;
    }

    private String availableInputLine() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            stringBuilder.append(i);

            //if not the last element ,append a comma
            if (i < width - 1) {
                stringBuilder.append(',');
            }
        }
        return stringBuilder.toString();
    }
}