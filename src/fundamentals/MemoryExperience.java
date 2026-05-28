package fundamentals;

import java.awt.*;

public class MemoryExperience {
    public static void main(String[] args) {
        //task is to compare two primitive ints, compare strings, compare arrays
        int x = 20;
        int y = 30;
        if (x > y){
            System.out.println("x is bigger");
        }else{
            System.out.println("y is bigger");
        }

      String name1 = "Java";
        String name2 = "Java";
        String name3 = new String("Java");
        System.out.println(name1 == name2);
        System.out.println(name1 == name3);
        System.out.println(name1.equals(name3));
        Point p1 = new Point(1, 1);
        Point p2 = p1;
        p2.y = 20;
        System.out.println(p2);

    }
}