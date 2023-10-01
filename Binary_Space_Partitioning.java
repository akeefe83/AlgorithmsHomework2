import java.lang.*;

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
        private Coordinate s;
        private Coordinate e;
        private char o; //need to determine if line is horizontal, vertical, or diagonal, which can be done by seeing if either the x or y coordinates of the start and end points are
        //equal or different from each other.

        public Line(Coordinate start, Coordinate end){
            if (start.equals(end)){
                System.out.println("BOOOO YOU SUCK");
            }
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
            if (this.equals(two)){
                return 0;
            }
            if (this.o == 'v' && two.o == 'v'){
                //in this case if the two lines are parallel we know that they are either in front, behind, or on top of each other, and so we simply compare their x coordinates to
                //see where they align. If the line being added is ahead of the line already in the tree we know that it is in front, and if not then it's behind. 1 represents in front
                //and -1 represents behind
                if (two.s.x > this.s.x){
                    return 1;
                }
                else{
                    return -1;
                }
            }
            else if (this.o == 'h' && two.o == 'h'){
                //Once again, these lines would be parallel and instead we choose to compare the y coordinates to see where each line is positioned to each other.
                if (two.s.y > this.s.y){
                    return -1;
                }
                else{
                    return 1;
                }
            }
            else{
                //need to check if the line is ahead or behind the other one, or if part is ahead or behind. If first line (the one already in tree) is horizontal, make sure
                //that the vertical line's y coordinates are either both more than or both less than or equal to the y coordinates of the horizontal. Opposite for if they are 
                //flipped
                //diagonal is different story, would need to check both x and y coordinates.
                if (this.o == 'h'){
                    //check if the main line (one already in the tree) is horizontal, if it is then compare the value of the y coordinate to the y coordinates
                    //of the line that is going to be added
                    if (two.s.y <= this.s.y && two.e.y <= this.s.y){
                        //if the y value of the line going to be added is less than the one already added we know the line must be in front of the one already in the tree
                        //and we return a positive one to represent this
                        return 1;
                    }
                    else if (two.s.y >= this.s.y && two.e.y >= this.s.y){
                        //if the y value is greater than the one already added we know it must behind the one already added in the tree and we return a negative one to represent this
                        return -1;
                    }
                    else{
                        //if the line is not wholly in front or behind the line that is already in the tree then it must intersect in some capacity, and so we must divide the line
                        //in two. This is returned as a negative 2 to represent that it originates from the horizontal comparison.
                        return -2;
                    }
                }
                else if (this.o == 'v'){ 
                    //check if the main line (the one already in the tree) is vertical, if it is compare the value of the x coordinate to the x coordinates
                    //of the line that is going to be added, which is the line "two" in this case
                    if (two.s.x <= this.s.x && two.e.x <= this.s.x){
                        //if the x coordinates of the line being added are less than the line before it then they must be behind the line already in the tree, and we return -1
                        //to represent this
                        return -1;
                    }
                    else if (two.s.x >= this.s.x && two.e.x >= this.s.x){
                        //if the x coordinates are larger then the line must be in front of the line already in the tree and we return 1 to represent this
                        return 1;
                    }
                    else{
                        //if the line is not wholly in front or behind the line that is already in the tree then we know that this line must intersect the other, and we must
                        //split it in two. It is a positive two in this case to specify that it originates from the vertical comparison.
                        return 2;
                    }
                }
                else{
                    //this is a bit of an edge case that would need to be debugged or further worked out, as if you are comparing the one diagonal line in this case you would need
                    //to find the slope of it, and figure out what it would potentially intersect with.
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
            if (this.size == 0){ //check to see if the tree is empty before checking any potential nodes, if it is empty create a new node as the head
                n = new Node(l);
                head = n;
                this.size++;
                return n;
            }
            if (n == null){ //check to see if the node is null, if it is make its value a new node with line l
                n = new Node(l);
                this.size++;
                return n;
            }
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
                else if (n.value.s.y == l.e.y){
                    n.back = insert(n.back, new Line(l.s, n.value.e));
                    n.front = insert(n.front, new Line(n.value.e, l.e));
                }
            }
            else if (n.value.compareLines(l) == -2){
                if (n.value.s.x == l.s.x){
                    n.back = insert(n.back, new Line(l.s, n.value.s));
                    n.front = insert(n.front, new Line(n.value.s, l.e));
                }
                else if (n.value.s.x == l.e.x){
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
        //the following series of lines are how I have personally codified the lines of the image, with line a being the first line of the animation from the webpage, and b
        //being the line after, so on and so forth. I would recommend drawing out a grid to understand how they are plotted out. Currently the insertion method works, to my 
        //best understanding, my only hang up is ensuring users can choose the starting line and that there is a clear output of the in-order traversal of the tree.
        //I guess there isn't a need to graphically output the image, so I was sweating for nothing.
        Line a = new Line(new Coordinate(0,10), new Coordinate(15, 10));
        Line b = new Line(new Coordinate(2,0), new Coordinate(2, 10));
        Line c = new Line(new Coordinate(13,0), new Coordinate(13, 10));
        Line d = new Line(new Coordinate(2,2), new Coordinate(13, 2));
        Line e = new Line(new Coordinate(6,6), new Coordinate(6, 8));
        Line f = new Line(new Coordinate(6,6), new Coordinate(8, 6));
        Line g = new Line(new Coordinate(6,8), new Coordinate(8, 6));
        Line [] map = {a, b, c, d, e, f, g};
        BSP mapTree = new BSP();
        for (int i = 0; i < map.length; i++){
            Node n = mapTree.head;
            mapTree.insert(n, map[i]);
        }
        mapTree.inOrderTraversal();
    }
}