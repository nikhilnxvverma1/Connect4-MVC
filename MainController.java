import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for Connect 4 game
 *
 * @author Nikhil Verma
 * @version 1.0: MainController.java
 *          Revisions:
 *          Initial revision
 */
public class MainController implements GameboardListener{

    @FXML
    private Pane gameplayPane;

    @FXML
    private Label playerTurn;

    @FXML
    private Button back;

    @FXML
    private VBox menu;

    @FXML
    private Label title;

    @FXML
    void  backgroundPressed(MouseEvent mouseEvent) {
        initialX = mouseEvent.getSceneX();
        initialY = mouseEvent.getSceneY();
    }

    @FXML
    void backgroundDragged(MouseEvent mouseEvent) {
        gameplayPane.getScene().getWindow().setX(mouseEvent.getScreenX() - initialX);
        gameplayPane.getScene().getWindow().setY(mouseEvent.getScreenY() - initialY);
    }

    @FXML
    void playVsHuman(ActionEvent event) {
        bringGameboardInFocus.playFromStart();
        gameboard.enableDropIndicator(true);

        connect4Field=new Connect4Field();
        player[0]=new Player(connect4Field,"Nikhil",'r');
        player[1]=new OffensivePlayer(connect4Field,"Computer",'y');
        connect4Field.init(player[0],player[1]);
        currentPlayer=0;
        playerTurn.setText(player[currentPlayer].getName());
    }

    @FXML
    void playVsComputer(ActionEvent event) {
        bringGameboardInFocus.playFromStart();
        gameboard.enableDropIndicator(true);

        connect4Field=new Connect4Field();
        player[0]=new Player(connect4Field,"Nikhil",'r');
        player[1]=new Player(connect4Field,"Swapnil",'y');
        connect4Field.init(player[0],player[1]);
        currentPlayer=0;
        playerTurn.setText(player[currentPlayer].getName());
    }

    @FXML
    void quit(ActionEvent actionEvent) {
        //close the window
        Stage stage = (Stage) menu.getScene().getWindow();
        stage.close();

    }

    @FXML
    void backToMainMenu(ActionEvent event) {
        System.out.println("Playing animation in reverse");
        gameboard.enableDropIndicator(false);
        gameboard.removeAllDisks();
        takeGameboardOutOfFocus.playFromStart();
    }

    private Gameboard gameboard;
    private double initialX;
    private double initialY;

    private Timeline bringGameboardInFocus;
    private Timeline takeGameboardOutOfFocus;

    //these are not final because their values are retrieved from the view on init
    private static double MENU_START_X=100;
    private static double MENU_END_X=-200;
    private static double TITLE_START_Y=200;
    private static double TITLE_END_Y=-200;
    private static double BACK_START_Y=-200;
    private static double BACK_END_Y=20;
    private static double PLAYER_TURN_START_Y=-200;
    private static double PLAYER_TURN_END_Y=BACK_END_Y;
    private static double PLAYER_TURN_WON_Y=100;
    private static double PLAYER_TURN_START_SCALE=1;
    private static double PLAYER_TURN_WON_SCALE=3;
    private static double BOARD_START_X=150;
    private static double BOARD_END_X=60;
    private static double BOARD_START_Y=300;
    private static double BOARD_END_Y=200;
    private static double BOARD_SCALE_START_X=0.7;
    private static double BOARD_SCALE_END_X=1;
    private static double BOARD_SCALE_START_Y=0.7;
    private static double BOARD_SCALE_END_Y=1;


    //=============================================================================================
    //Business logic
    //=============================================================================================

    private Connect4Field connect4Field;
    private PlayerInterface player[]=new PlayerInterface[2];
    private int totalPlayers=2;
    private int currentPlayer;

    public void init(){
        gameboard = new TrapeziumGameboard(Connect4Field.DEFAULT_HEIGHT,Connect4Field.DEFAULT_WIDTH,this);
        gameboard.setScale(0.7);
        gameboard.setLayoutX(200);
        gameboard.setLayoutY(250);
        gameplayPane.getChildren().addAll(gameboard);
        bringGameboardInFocus = createPlayAnimation();
        initStartValues();
        bringGameboardInFocus.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bringGameboardInFocus.pause();
            }
        });
        takeGameboardOutOfFocus=createBackToMenuAnimation();
        takeGameboardOutOfFocus.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                takeGameboardOutOfFocus.pause();
            }
        });
    }

    private void initStartValues(){
        MENU_START_X=menu.getLayoutX();
        TITLE_START_Y=title.getLayoutY();
        BACK_START_Y=back.getLayoutY();
        PLAYER_TURN_START_Y=playerTurn.getLayoutY();
        PLAYER_TURN_START_SCALE=playerTurn.getScaleX();
        BOARD_START_X=gameboard.getLayoutX();
        BOARD_START_Y=gameboard.getLayoutY();
        BOARD_SCALE_START_X=gameboard.getScaleX();
        BOARD_SCALE_START_Y=gameboard.getScaleY();
    }

    public Timeline createPlayAnimation(){
        Timeline timeline=new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);

        KeyFrame keyFrame = getForwardKeyFrameForPlay();

        timeline.getKeyFrames().addAll(keyFrame);
        return timeline;
    }

    private KeyFrame getForwardKeyFrameForPlay() {
        KeyValue menuGoesLeft=new KeyValue(menu.layoutXProperty(),MENU_END_X, Interpolator.EASE_OUT);
        KeyValue titleGoesUp=new KeyValue(title.layoutYProperty(),TITLE_END_Y,Interpolator.EASE_OUT);
        KeyValue backComesDown=new KeyValue(back.layoutYProperty(),BACK_END_Y,Interpolator.EASE_OUT);
        KeyValue playerTurnComesDown=new KeyValue(playerTurn.layoutYProperty(),PLAYER_TURN_END_Y,Interpolator.EASE_OUT);
        KeyValue boardGoesLeft=new KeyValue(gameboard.layoutXProperty(),BOARD_END_X,Interpolator.EASE_OUT);
        KeyValue boardGoesUp=new KeyValue(gameboard.layoutYProperty(),BOARD_END_Y,Interpolator.EASE_OUT);
        KeyValue boardScalesUpForX=new KeyValue(gameboard.scaleXProperty(),BOARD_SCALE_END_X,Interpolator.EASE_OUT);
        KeyValue boardScalesUpForY=new KeyValue(gameboard.scaleYProperty(),BOARD_SCALE_END_Y,Interpolator.EASE_OUT);

        EventHandler<ActionEvent> onFinished=new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                bringGameboardInFocus.pause();
            }
        };

        return new KeyFrame(Duration.millis(200),menuGoesLeft,titleGoesUp,backComesDown,
                playerTurnComesDown,boardGoesLeft,boardGoesUp,boardScalesUpForX,boardScalesUpForY);
    }

    private KeyFrame getBackwardKeyFrameForPlay() {
        //reverse of all keyvalues from play animation
        KeyValue menuGoesLeft=new KeyValue(menu.layoutXProperty(),MENU_START_X, Interpolator.EASE_OUT);
        KeyValue titleGoesUp=new KeyValue(title.layoutYProperty(),TITLE_START_Y,Interpolator.EASE_OUT);
        KeyValue backComesDown=new KeyValue(back.layoutYProperty(),BACK_START_Y,Interpolator.EASE_OUT);
        KeyValue playerTurnComesDown=new KeyValue(playerTurn.layoutYProperty(),PLAYER_TURN_START_Y,Interpolator.EASE_OUT);
        KeyValue playerTurnBecomesSmallX=new KeyValue(playerTurn.scaleXProperty(),PLAYER_TURN_START_SCALE,Interpolator.EASE_OUT);
        KeyValue playerTurnBecomesSmallY=new KeyValue(playerTurn.scaleYProperty(),PLAYER_TURN_START_SCALE,Interpolator.EASE_OUT);
        KeyValue boardGoesLeft=new KeyValue(gameboard.layoutXProperty(),BOARD_START_X,Interpolator.EASE_OUT);
        KeyValue boardGoesUp=new KeyValue(gameboard.layoutYProperty(),BOARD_START_Y,Interpolator.EASE_OUT);
        KeyValue boardScalesUpForX=new KeyValue(gameboard.scaleXProperty(),BOARD_SCALE_START_X,Interpolator.EASE_OUT);
        KeyValue boardScalesUpForY=new KeyValue(gameboard.scaleYProperty(),BOARD_SCALE_START_Y,Interpolator.EASE_OUT);

        return new KeyFrame(Duration.millis(200),menuGoesLeft,titleGoesUp,backComesDown,
                playerTurnComesDown,playerTurnBecomesSmallX,playerTurnBecomesSmallY,
                boardGoesLeft,boardGoesUp,boardScalesUpForX,boardScalesUpForY);

    }

    public Timeline createBackToMenuAnimation(){
        Timeline timeline=new Timeline();
        timeline.setAutoReverse(false);
        timeline.setCycleCount(1);


        KeyFrame keyFrame=getBackwardKeyFrameForPlay();
        timeline.getKeyFrames().addAll(keyFrame);

        return timeline;
    }

    @Override
    public void columnPressed(double x, int column, Gameboard gameboard) {
        System.out.println("registered click for column"+ column);
        if(connect4Field.checkIfPiecedCanBeDroppedIn(column)){
            char gamePiece = player[currentPlayer].getGamePiece();
            //TODO possible refactor
            Color diskColor = getColorForChar(gamePiece);//char enums are only used because business logic was integrated seperately
            int landingRow = connect4Field.getLandingRow(column);
            connect4Field.dropPieces(column,gamePiece);//TODO the above two methods can be clubbed
            gameboard.dropAndPlacePieceAt(landingRow,column,diskColor);

        }
    }

    @Override
    public void diskDidDrop(int row, int column, Gameboard gameboard) {
        if(connect4Field.didLastMoveWin()){
            System.out.println("Game over, winner"+player[currentPlayer].getName());
            showWinner(player[currentPlayer]);
        }else{
            nextPlayer();
            autoplayForComputerPlayer();
        }
    }

    private void autoplayForComputerPlayer() {
        if(player[currentPlayer] instanceof OffensivePlayer){
            int column = player[currentPlayer].nextMove();
            columnPressed(gameboard.xForColumn(column),column,gameboard);//mock this event
        }
    }

    private void showWinner(PlayerInterface winner){
        String name = winner.getName();
        if(winner instanceof OffensivePlayer){
            playerTurn.setText("You Lose!");
        }else{
            playerTurn.setText(name+" Won!");
        }

        gameboard.enableDropIndicator(false);
//        gameboard.captureBoardEvents(false);

        Timeline winningAnimation=new Timeline();
        winningAnimation.setAutoReverse(false);
        winningAnimation.setCycleCount(1);

        KeyValue playerTurnComesDown=new KeyValue(playerTurn.layoutYProperty(),PLAYER_TURN_WON_Y, Interpolator.EASE_IN);
        KeyValue playerTurnBecomesBigX=new KeyValue(playerTurn.scaleXProperty(),PLAYER_TURN_WON_SCALE, Interpolator.EASE_IN);
        KeyValue playerTurnBecomesBigY=new KeyValue(playerTurn.scaleYProperty(),PLAYER_TURN_WON_SCALE, Interpolator.EASE_IN);
        KeyFrame keyFrame=new KeyFrame(Duration.millis(100),playerTurnComesDown,playerTurnBecomesBigX,playerTurnBecomesBigY);

        winningAnimation.getKeyFrames().add(keyFrame);
        winningAnimation.play();
    }

    private void nextPlayer() {
        //increment for next player
        if(++currentPlayer>=totalPlayers){
            currentPlayer=0;
        }
        playerTurn.setText(player[currentPlayer].getName());
    }

    /**
     * returns a color for a enumerated char
     * @param c r-red,b-black,y-yellow.
     * @return the respective color,black if unknown color is given
     */
    private Color getColorForChar(char c){
        switch (c){
            case 'r':
            case 'R':
                return Color.RED;
            case 'y':
            case 'Y':
                return Color.YELLOW;
            case 'b':
            case 'B':
            default:
                return Color.BLACK;
        }
    }
}
