import acm.graphics.GRect;
import java.util.ArrayList;

public class Mutiworms {

    public static void main(String[] args) {

    }

}

class Person extends GRect implements Shootable {

    private int health = 100;
    private boolean isRed;
    private ArrayList<Shootable> inventory;
    private boolean hasTurn;

    public Person(double width, double height, boolean redStatus) {
        super(width, height);
        isRed = redStatus;
    }

    public void shootThis() {

    }

}

interface Shootable {

    double yaccel = 9.8;
    void shootThis();

}

