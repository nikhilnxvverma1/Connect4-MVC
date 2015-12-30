import java.util.Random;

/**
 * Weak Computer Implementation: A computer implementation of PlayerInterface.
 * Weak as in it uses random values to compete against the player
 *
 * @author Nikhil Verma
 * @version 1.0: WeakPlayer.java
 *          Revisions:
 *          Initial revision
 */
public class WeakPlayer extends Player {

    public static final Random random=new Random();

    public WeakPlayer(Connect4FieldInterface connect4Field, String name, char gamePiece) {
        super(connect4Field, name, gamePiece);
    }

    @Override
    public int nextMove() {

        //just return any valid random value
        int column;
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

    public static void main(String[] args) {
        Connect4FieldInterface connect4FieldInterface=new Connect4Field(25,9);
        Player player1=new Player(connect4FieldInterface,"Nikhil",'+');
        WeakPlayer computer=new WeakPlayer(connect4FieldInterface,"Computer",'#');
        connect4FieldInterface.init(player1,computer);
        connect4FieldInterface.playTheGame();
    }
}
