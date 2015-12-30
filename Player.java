import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Human Player Implementation: an implementation of PlayerInterface which asks the user for input for the next move
 *
 * @author Nikhil Verma
 * @version 1.0: Player.java
 *          Revisions:
 *          Initial revision
 */
public class Player implements PlayerInterface {

    protected Connect4FieldInterface connect4Field;
    protected String name;
    protected char gamePiece;

    public Player(Connect4FieldInterface connect4Field, String name, char gamePiece) {
        this.connect4Field = connect4Field;
        this.name = name;
        this.gamePiece = gamePiece;
    }

    @Override
    public char getGamePiece() {
        return gamePiece;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int nextMove() {

        int input= 0;
        boolean validNumber;
        do {
            Scanner scanner=new Scanner(System.in);
            validNumber=true;
            try {
                input = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Please enter an integer number");
                validNumber=false;
            }
//            scanner.close();//doing this will close System.in too
        }while(!validNumber);
        return input;
    }

    public static void main(String[] args) {
        Connect4FieldInterface connect4FieldInterface=new Connect4Field();
        Player player1=new Player(connect4FieldInterface,"Nikhil",'+');
        Player player2=new Player(connect4FieldInterface,"Swapnil",'*');
        connect4FieldInterface.init(player1,player2);
        connect4FieldInterface.playTheGame();
    }
}
