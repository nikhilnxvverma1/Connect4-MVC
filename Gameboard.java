import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;

/**
 * View for the connect 4 game that is generic (as in rectangular,see TrapeziumGameboard for modified version)
 *
 * @author Nikhil Verma
 * @version 1.0: Gameboard.java
 *          Revisions:
 *          Initial revision
 */
public class Gameboard extends Group implements EventHandler<MouseEvent>{

    public static final double DEFAULT_DISK_DIAMETER =18;
    public static final double GAP_BETWEEN_DISKS =5;
    protected int rows;
    protected int columns;
    private double diskDiameter = DEFAULT_DISK_DIAMETER;
    private Shape board;
    private Path dropIndicator;
    private Pane diskHolder;
    private GameboardListener gameboardListener;

    public Gameboard(int rows, int columns,GameboardListener gameboardListener) {
        this(rows,columns, gameboardListener,DEFAULT_DISK_DIAMETER);
    }

    public Gameboard(int rows, int columns ,GameboardListener gameboardListener,double diskDiameter) {
        this.rows = rows;
        this.columns = columns;
        this.diskDiameter = diskDiameter;
        this.gameboardListener=gameboardListener;
        initView();
    }

    protected void initView() {
        double bottomRightX=(columns)*(diskDiameter +GAP_BETWEEN_DISKS);
        double bottomRightY=rows * (diskDiameter + GAP_BETWEEN_DISKS);

        Rectangle boardShape = new Rectangle(0, 0, bottomRightX, bottomRightY);
        board = Path.subtract(boardShape, createHoleShape(diskDiameter / 2));
        board.setFill(Color.BLUE);

        diskHolder=new Pane();
        diskHolder.setPrefWidth(bottomRightX);
        diskHolder.setPrefHeight(bottomRightY);
        this.getChildren().addAll(diskHolder, board);

        dropIndicator=new Path();
        LineTo left=new LineTo(diskDiameter/2,-diskDiameter);
        LineTo right=new LineTo(-diskDiameter/2,-diskDiameter);
        dropIndicator.getElements().addAll(new MoveTo(0,0),left,right,new ClosePath());
        dropIndicator.setLayoutY(-10);
        dropIndicator.setFill(Color.DARKGRAY);
        dropIndicator.setStroke(null);
        this.getChildren().addAll(dropIndicator);
        dropIndicator.setVisible(false);

    }

    public void enableDropIndicator(boolean enable){
        dropIndicator.setVisible(enable);
        captureBoardEvents(enable);
    }

    public void captureBoardEvents(boolean capture) {
        if(capture){
            this.addEventHandler(MouseEvent.MOUSE_MOVED, this);
            this.addEventHandler(MouseEvent.MOUSE_CLICKED, this);
        }else{
            this.removeEventHandler(MouseEvent.MOUSE_MOVED, this);
            this.removeEventHandler(MouseEvent.MOUSE_CLICKED, this);
        }
    }


    protected Path createHoleShape(double holeRadius){
        Path holes=new Path();
        holes.setFill(Color.BLACK);
        holes.setStroke(null);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                double x= xForColumn(j)+GAP_BETWEEN_DISKS*1.5;
                double y= yForRow(i);

                MoveTo moveTo=new MoveTo(x,y);

                ArcTo drawCircleAround=new ArcTo();
                drawCircleAround.setX(x+1);
                drawCircleAround.setY(y);
                drawCircleAround.setRadiusX(holeRadius);
                drawCircleAround.setRadiusY(holeRadius);
                drawCircleAround.setSweepFlag(false);
                drawCircleAround.setLargeArcFlag(true);

                holes.getElements().addAll(moveTo,drawCircleAround,new ClosePath());

            }
        }
        return holes;
    }

    public void dropAndPlacePieceAt(final int dr, final int dc,Color color){
        double x=xForColumn(dc)+diskDiameter/2;
        double y=yForRow(dr)+diskDiameter/2;
        Circle disk = getDisk(color);
        disk.setLayoutX(x);
        disk.setLayoutY(-20);
        disk.setOpacity(0.5);
        diskHolder.getChildren().add(disk);

        //drop animation
        Timeline dropAnimation=new Timeline();
        dropAnimation.setAutoReverse(false);
        dropAnimation.setCycleCount(1);

        KeyValue diskFallsDown=new KeyValue(disk.layoutYProperty(),y, Interpolator.EASE_IN);
        KeyValue diskBecomesOpaque=new KeyValue(disk.opacityProperty(),1);
        KeyFrame keyFrame=new KeyFrame(Duration.millis(100),diskFallsDown,diskBecomesOpaque);

        dropAnimation.getKeyFrames().add(keyFrame);
        dropAnimation.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gameboardListener.diskDidDrop(dr,dc,Gameboard.this);
            }
        });
        dropAnimation.play();
    }

    public double xForColumn(int c){
        return (diskDiameter+GAP_BETWEEN_DISKS)*c+GAP_BETWEEN_DISKS/2;
    }

    public double yForRow(int r){
        return (diskDiameter+GAP_BETWEEN_DISKS)*r+GAP_BETWEEN_DISKS/2;
    }

    public void setScale(double scale){
        this.setScaleX(scale);
        this.setScaleY(scale);
    }

    public int columnFor(double x){
        int c= (int) (x/(diskDiameter+GAP_BETWEEN_DISKS));
        return c;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if(mouseEvent.getEventType()==MouseEvent.MOUSE_MOVED){
            int c=columnFor(mouseEvent.getX());
            double x=xForColumn(c)+diskDiameter/2;
            dropIndicator.setLayoutX(x);
        }else if(mouseEvent.getEventType()==MouseEvent.MOUSE_CLICKED){
            int c=columnFor(mouseEvent.getX());
            double x=xForColumn(c)+diskDiameter/2;
            gameboardListener.columnPressed(x,c,this);
        }
    }

    public Circle getDisk(Color color){
        //let the disks be slightly bigger so they don't leave any gap in the board
        return new Circle(diskDiameter/2+GAP_BETWEEN_DISKS/2,color);
    }

    public void removeAllDisks(){
        diskHolder.getChildren().clear();
    }
}

interface GameboardListener{
    void columnPressed(double x, int column,Gameboard gameboard);
    void diskDidDrop(int row, int column, Gameboard gameboard);
}