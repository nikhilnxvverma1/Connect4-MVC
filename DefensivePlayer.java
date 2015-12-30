import java.util.Random;

/**
 * Defensive Computer player Implementation: an implementation of the computer player that plays defensively
 *
 * @author Nikhil Verma
 * @version 1.1: DefensivePlayer.java
 *          Revisions:
 *          changed access modifiers of certain methods 9th sep,2015 11:56 PM Nikhil Verma
 *          Initial revision
 */
public class DefensivePlayer extends Player {

    protected static final Random random=new Random();

    public DefensivePlayer(Connect4FieldInterface connect4Field, String name, char gamePiece) {
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

    /**
     * fills a 2d char array for board configuration using a array of string rows.Also in doing so, keeps
     * track of the opponent game pieces in that board
     * @param board 2d char array of board that needs to be filled
     * @param rows array of string denoting the string representation of a each row
     * @param height known height of the board
     * @param width known width of the board
     * @param opponentGamePieces char array of opponent game pieces that needs to be filled
     * @return total known opponent pieces scanned so far
     */
    protected int fillBoardAndOpponentPieces(char[][] board, String[] rows, int height, int width, char[] opponentGamePieces) {
        int totalKnownOpponents=0;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                //add the piece to the board
                char piece=rows[i].charAt(j);
                board[i][j]=piece;
                //check if the piece at current position is foreign
                //(which means it belongs to an opponent)
                if(piece!= Connect4Field.EMPTY_SLOT&&
                        piece!= Connect4Field.INVALID_SLOT&&
                        piece!=gamePiece){
                        //first check to see that the piece already doesn't exist in the

                    //opponent pieces array
                    boolean opponentAlreadyDoesntExist=true;
                    for (int k = 0; k < totalKnownOpponents; k++) {
                        if(opponentGamePieces[k]==piece){
                            opponentAlreadyDoesntExist=false;
                        }
                    }
                    if (opponentAlreadyDoesntExist) {
                        opponentGamePieces[totalKnownOpponents++]=piece;
                    }
                }
            }

        }
        return totalKnownOpponents;
    }

    /**
     * Checks if a any opponent has a chance of connecting at a particular location
     * @param board the board configuration ,2d char array
     * @param width width of the board
     * @param height height of the board
     * @param opponents opponent game pieces in a 1d char array
     * @param totalOpponent total opponents in that 1d char array
     * @return if any opponent has a chance at connecting, it returns the row,column value
     * as a string which is comma seperated which can be split and parsed.if no opponent has a chance
     * of connecting, it returns null.
     */
    protected String defendIfNeeded(char [][] board, int width,int height,char []opponents,int totalOpponent){

        //for each opponenet, check to see if the board provides connection for the opponnet
        for (int i = 0; i < totalOpponent; i++) {
            for (int c = 0; c <width; c++) {//for each column

                //fall down from here until you hit something
                int r=0;
                while(r<height&&board[r][c]== Connect4Field.EMPTY_SLOT){
                    r++;
                }
                r-=1;//reduce by 1 for the last iteration

                //-1 indicates that the current column is completely filled
                if(r!=-1){
                    //if connection is being made, return the location as a csv
                    boolean connecting = Connect4Field.willConnect(board, width, height, r, c, opponents[i]);
                    if (connecting) {
                        return r + "," + c; //return position in String csv then split in the caller
                    }
                }

            }
        }

        return null;//none found
    }

    public static void main(String[] args) {
        Connect4FieldInterface connect4FieldInterface=new Connect4Field(25,9);
        Player player1=new Player(connect4FieldInterface,"Nikhil",'+');
        DefensivePlayer computer=new DefensivePlayer(connect4FieldInterface,"Computer",'#');
        connect4FieldInterface.init(player1,computer);
        //it can also play computer against computer
//        DefensivePlayer computer1=new DefensivePlayer(connect4FieldInterface,"Computer 1",'@');
//        DefensivePlayer computer2=new DefensivePlayer(connect4FieldInterface,"Computer 2",'$');
//        connect4FieldInterface.init(computer1,computer2);
        connect4FieldInterface.playTheGame();
    }
}
