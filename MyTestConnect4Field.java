/**
 * Created by NikhilVerma on 15/09/15.
 */
public class MyTestConnect4Field {

    public static void main(String[] args) {
        Connect4FieldInterface connect4FieldInterface=new Connect4Field(25,9);
        Player player1=new Player(connect4FieldInterface,"Nikhil",'+');
        Player player2=new Player(connect4FieldInterface,"Swapnil",'*');
        WeakPlayer weakPlayer=new WeakPlayer(connect4FieldInterface,"Computer",'#');
        connect4FieldInterface.init(player1,weakPlayer);
        connect4FieldInterface.playTheGame();
//        connect4FieldInterface.toString();
//        connect4FieldInterface.dropPieces(4,'*');
//        connect4FieldInterface.dropPieces(5,'+');
//        connect4FieldInterface.dropPieces(4, '*');
//        System.out.println(connect4FieldInterface.toString());

    }
}