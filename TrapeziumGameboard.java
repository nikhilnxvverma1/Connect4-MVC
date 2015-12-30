import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Trapezium gameboard that reduces slots with each row we go down
 *
 * @author Nikhil Verma
 * @version 1.0: TrapeziumGameboard.java
 *          Revisions:
 *          Initial revision
 */
public class TrapeziumGameboard extends Gameboard {

    public TrapeziumGameboard(int rows, int columns, GameboardListener gameboardListener) {
        super(rows, columns, gameboardListener);
    }

    public TrapeziumGameboard(int rows, int columns, GameboardListener gameboardListener, double diskDiameter) {
        super(rows, columns, gameboardListener, diskDiameter);
    }

    @Override
    protected Path createHoleShape(double holeRadius) {
        Path holes=new Path();
        holes.setFill(Color.BLACK);
        holes.setStroke(null);
        for (int i = 0,skip=0; i < rows; i++,skip++) {
            for (int j = skip; j < columns-skip; j++) {
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
}
