/**
 * Offensive Computer Player implementation:Implentation of the player interface that tries
 * to win, if it finds a connection somewhere.
 *
 * @author Nikhil Verma
 * @version 1.0: OffensivePlayer.java
 *          Revisions:
 *          Initial revision
 */
public class OffensivePlayer extends DefensivePlayer implements PlayerInterface {

    public OffensivePlayer(Connect4FieldInterface connect4Field, String name, char gamePiece) {
        super(connect4Field, name, gamePiece);
    }

    @Override
    public int nextMove() {

        //create a 2d char array for the game board using the toString
        String boardRepresentation = connect4Field.toString();
        char board[][]=new char[Connect4Field.BOARD_MAX_HEIGHT][Connect4Field.BOARD_MAX_WIDTH];

        //split the representation by newline charecters
        final String NEWLINE=System.getProperty("line.separator");
        String [] rows=boardRepresentation.split(NEWLINE);
        int height=rows.length;
        //assuming each row will be of equal width get the width of the first row
        int width=rows[0].length();

        char opponentGamePieces[]=new char[Connect4Field.MAX_SUPPORTED_PLAYER];//support for 10 players in the
        int totalKnownOpponents = fillBoardAndOpponentPieces(board, rows, height, width, opponentGamePieces);

        //try to attack by looking for a location where this player's piece could connect
        String attackMove = attackIfPossible(board, width, height);
        if(attackMove!=null){
            //split the location csv,return the column position which comes second
            return Integer.parseInt(attackMove.split(",")[1]);
        }else{
            //defend first if something is about to connect for the other player
            String preventOpponentConnection=defendIfNeeded(board,width,height,opponentGamePieces,totalKnownOpponents);

            if(preventOpponentConnection!=null){
                //split the location csv,return the column position which comes second
                return Integer.parseInt(preventOpponentConnection.split(",")[1]);
            }else{
                int column;
                //just return any valid random value
                boolean validColumn;
                do{
                    column=random.nextInt(Connect4Field.BOARD_MAX_WIDTH);

                    if(connect4Field.checkIfPiecedCanBeDroppedIn(column)){
                        validColumn=true;
                    }else{
                        validColumn=false;
                    }
                }while(!validColumn);//find another random value that can be dropped in board
                return column;
            }
        }


    }



    /**
     * Checks if a this player has a chance of connecting at a particular location
     * @param board the board configuration ,2d char array
     * @param width width of the board
     * @param height height of the board
     * @return if player has a chance at connecting, it returns the row,column value
     * as a string which is comma seperated which can be split and parsed.if not,
     * it returns null.
     */
    protected String attackIfPossible(char[][] board, int width, int height){

        for (int c = 0; c < width; c++) {//for each column

            //fall down from here until you hit something
            int r = 0;
            while (r < height && board[r][c] == Connect4Field.EMPTY_SLOT) {
                r++;
            }
            r -= 1;//reduce by 1 for the last iteration

            //-1 indicates that the current column is completely filled
            if (r != -1) {
                //if connection is being made, return the location as a csv
                boolean connecting = Connect4Field.willConnect(board, width, height, r, c, gamePiece);
                if (connecting) {
                    return r + "," + c; //return position in String csv then split in the caller
                }
            }

        }

        return null;//none found
    }



    public static void main(String[] args) {
        Connect4FieldInterface connect4FieldInterface=new Connect4Field(25,9);
        Player player1=new Player(connect4FieldInterface,"Nikhil",'+');
        OffensivePlayer computer=new OffensivePlayer(connect4FieldInterface,"Computer",'#');
        connect4FieldInterface.init(player1,computer);
        //it can also play computer against computer
//        DefensivePlayer computer1=new DefensivePlayer(connect4FieldInterface,"Computer 1",'@');
//        DefensivePlayer computer2=new DefensivePlayer(connect4FieldInterface,"Computer 2",'$');
//        connect4FieldInterface.init(computer1,computer2);
        connect4FieldInterface.playTheGame();
    }
}
