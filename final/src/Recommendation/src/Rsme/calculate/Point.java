package Rsme.calculate;

public class Point {
    
    private double x = 0;
    private double y = 0;
    
    /** Creates a new instance of Point */
    public Point(double x,double y) {
        this.setX(x);
        this.setY(y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    
    public String toString(){
        return "[" + x + "," + y + "]";
    }
    
    /**
    public static void main(String[] args) {
        System.out.println(new Point(3,4).toString());
    }*/
}
