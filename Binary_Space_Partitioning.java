import java.lang.*;
import java.util.*;

public class Binary_Space_Partitioning {

    public static class Coordinate{
        private int x;
        private int y;

        public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    public static class Line{
        private Coordinate s; //s is the starting point of the line
        private Coordinate e; //e is the ending point of the line
        private char o; //o refers to the orientation of the line, if it is horizontal, vertical, or diagonal, which can be used in determining positioning of lines with respect to one another.

        public Line(Coordinate start, Coordinate end){
            this.s = start;
            this.e = end;
            if (s.x == e.x){
                this.o = 'v';
            }
            else if (s.y == e.y){
                this.o = 'h';
            }
            else{
                this.o = 'd';
            }
        }

        public int compareLines(Line two){

            //the main logic for comparing lines comes from two understandings: lines are either parallel or potentially intersect
                //first we must check if the lines are parallel and compare them in a simple way if they are
                //if they are not parallel then we must check if they intersect in some way
            //the return values correspond to these positions: 
            //0 = the two lines are the same, 
            //1 = the second line (one being added) is in front of the current (one already added)
            //-1 = the second line is behind the current line
            //2 = the two lines intersect with one another and the second line must be split in two (corresponds to a vertical intersection)
            //-2 = the two lines intersect and the second line must be split (corresponds with horizontal intersection)

            if (this.equals(two)){
                return 0;
            }
            if (this.o == 'v' && two.o == 'v'){
                
                if (two.s.x > this.s.x){
                    return 1;
                }
                else{
                    return -1;
                }
            }
            else if (this.o == 'h' && two.o == 'h'){
                if (two.s.y > this.s.y){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else{
                if (this.o == 'h'){
                    if (two.s.y <= this.s.y && two.e.y <= this.s.y){
                        return 1;
                    }
                    else if (two.s.y >= this.s.y && two.e.y >= this.s.y){
                        return -1;
                    }
                    else{
                        return -2;
                    }
                }
                else if (this.o == 'v'){ 
                    if (two.s.x <= this.s.x && two.e.x <= this.s.x){
                        return -1;
                    }
                    else if (two.s.x >= this.s.x && two.e.x >= this.s.x){
                        return 1;
                    }
                    else{
                        return 2;
                    }
                }
                else{
                    return 2;
                }
            }
        }
    }

    public static class Node{
        private Line value; //this is the actual value that the node possesses
        private Node front; //this will be the right side of the node
        private Node back; //this will be the left side of the node

        public Node(Line x){
            this.value = x;
            this.front = null;
            this.back = null;
        }
    }

    public static class BSP{
        private Node head;
        private int size;

        public BSP(){
            this.head = null;
            this.size = 0;
        }

        public Node insert(Node n, Line l){

            //check to see if the tree is empty before checking any potential nodes, if it is empty create a new node as the head
            if (this.size == 0){ 
                n = new Node(l);
                head = n;
                this.size++;
                return n;
            }

            //check to see if the node is null, if it is make its value a new node with line l
            if (n == null){ 
                n = new Node(l);
                this.size++;
                return n;
            }

            //if the node is not null we must compare the values of each node and find out whether the new node is in front or behind the previous one
            if (n.value.compareLines(l) == 1){
                n.front = insert(n.front, l);
            }
            else if (n.value.compareLines(l) == -1){
                n.back = insert(n.back, l);
            }
            else if (n.value.compareLines(l) == 2){
                if (n.value.s.y == l.s.y){
                    n.back = insert(n.back, new Line(l.s, n.value.s));
                    n.front = insert(n.front, new Line(n.value.s, l.e));
                }
                else if (n.value.e.y == l.s.y){
                    n.back = insert(n.back, new Line(l.s, n.value.e));
                    n.front = insert(n.front, new Line(n.value.e, l.e));
                }
            }
            else if (n.value.compareLines(l) == -2){
                if (n.value.s.x == l.s.x){
                    n.back = insert(n.back, new Line(l.s, n.value.s));
                    n.front = insert(n.front, new Line(n.value.s, l.e));
                }
                else if (n.value.e.x == l.s.x){
                    n.back = insert(n.back, new Line(l.s, n.value.e));
                    n.front = insert(n.front, new Line(n.value.e, l.e));
                }
            }
            return n; 
        }

        private void inOrderTraversal(Node n){
            if (n == null){
                return;
            }
            inOrderTraversal(n.back);
            System.out.print(n.value.s.x + "," + n.value.s.y + " ");
            System.out.print(n.value.e.x + "," + n.value.e.y + "\n");
            inOrderTraversal(n.front);
        }

        public void inOrderTraversal(){
            inOrderTraversal(head);
        }
    }

    public static void main(String [] args){
        //the following series of lines are how I have personally codified the lines of the image, using start points and end points to create each line

        Line a = new Line(new Coordinate(0,10), new Coordinate(15, 10));
        Line b = new Line(new Coordinate(2,0), new Coordinate(2, 10));
        Line c = new Line(new Coordinate(13,0), new Coordinate(13, 10));
        Line d = new Line(new Coordinate(2,2), new Coordinate(13, 2));
        Line e = new Line(new Coordinate(6,6), new Coordinate(6, 8));
        Line f = new Line(new Coordinate(6,6), new Coordinate(8, 6));
        Line g = new Line(new Coordinate(6,8), new Coordinate(8, 6));
        Line [] map = {a, b, c, d, e, f, g};

        System.out.println("Please choose which line to start from by typing in its corresponding number. The lines are described by their starting coordinates and ending coordinates.");
        System.out.println("0: (" + a.s.x + "," + a.s.y + ") (" + a.e.x + "," + a.e.y +")");
        System.out.println("1: (" + b.s.x + "," + b.s.y + ") (" + b.e.x + "," + b.e.y +")");
        System.out.println("2: (" + c.s.x + "," + c.s.y + ") (" + c.e.x + "," + c.e.y +")");
        System.out.println("3: (" + d.s.x + "," + d.s.y + ") (" + d.e.x + "," + d.e.y +")");
        System.out.println("4: (" + e.s.x + "," + e.s.y + ") (" + e.e.x + "," + e.e.y +")");
        System.out.println("5: (" + f.s.x + "," + f.s.y + ") (" + f.e.x + "," + f.e.y +")");
        System.out.println("6: (" + g.s.x + "," + g.s.y + ") (" + g.e.x + "," + g.e.y +")");
        Scanner scan = new Scanner(System.in);
        int answer = scan.nextInt();
        scan.close();

        BSP mapTree = new BSP();
        Node n = mapTree.head;
        mapTree.insert(n, map[answer]);

        for (int i = 0; i < map.length; i++){
            if (i != answer){
                n = mapTree.head;
                mapTree.insert(n, map[i]);
            }
            else{
                continue;
            }
        }
        mapTree.inOrderTraversal();
    }
}