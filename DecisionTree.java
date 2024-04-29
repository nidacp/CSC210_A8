import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

public class DecisionTree < E > extends BinaryTree < E > {


    /**
     * Constructor of a DecisionTree with a root value
     * @param e root value of type E
     */
    public DecisionTree(E e) {
        super(e);
    }

    /**
     * Constructor of a tree with an inputted root and trees to put to its left and right.
     * @param e     root value
     * @param left  tree to the left of the root
     * @param right tree to the right of the root
     */
    public DecisionTree(E e, BinaryTree < E > left, BinaryTree < E > right) {
        super(e);
        this.setLeft(left);
        this.setRight(right);
    }

    /**
     * Constructor from an existing tree
     * @param tree  existing DecisionTree
     */
    public DecisionTree(DecisionTree < E > tree) {
        super(tree.getData());
        this.setLeft(tree.getLeft());
        this.setRight(tree.getRight());
    }

    /**
     * Setter for the left side of a node
     * @param left  a binary tree to add to the left of a node
     */
    public void setLeft(BinaryTree < E > left) {
        if (left instanceof DecisionTree) {
            super.setLeft(left);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Setter for the right side of a node
     * @param right  a binary tree to add to the right of a node
     */
    public void setRight(BinaryTree < E > right) {
        if (right instanceof DecisionTree) {
            super.setRight(right);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Getter for the left side of a node
     * @return  a DecisionTree to the left of a node
     */
    public DecisionTree < E > getLeft() {
        return (DecisionTree < E > ) super.getLeft();
    }

    /**
     * Getter for the right side of a node
     * @return  a DecisionTree to the right of a node
     */
    public DecisionTree < E > getRight() {
        return (DecisionTree < E > ) super.getRight();
    }

    // function followPath(String) that traces path through YN string from root
    // return the node that's reached (both can be iteratively or recursively)

    /**
     * finds a decision tree based on an inputted String
     * @param yn    String of yes and no values, yesses leading left and nos leading right
     * @return      DecisionTree at the final destination
     */
    public DecisionTree < E > followPath(String yn) {
        if (yn == "") {
            return this;
        }

        yn = yn.toLowerCase();

        if (yn.charAt(0) == 'y') {
            return this.getLeft().followPath(yn.substring(1, yn.length()));
        } else if (yn.charAt(0) == 'n') {
            return this.getRight().followPath(yn.substring(1, yn.length()));
        } else {
            throw new RuntimeException("Char " + yn.charAt(0) + " found.");
        }
    }

    /**
     * prints the tree with the path + value of each node. Ordered by breadth-first search.
     * @param filename                  String name of the file which is being printed
     * @throws FileNotFoundException
     */
    public void printTree(String filename) throws FileNotFoundException {
        try {
            PrintWriter t = new PrintWriter(filename);
            t.close();
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }

        PrintWriter out = new PrintWriter(filename);
        // print tree nodes line by line using breadth-first traversal
        // queue or nodes and parallel queue holding their paths
        Queue < DecisionTree < E >> queue = new ArrayDeque < > ();
        Queue < String > paths = new ArrayDeque < > ();

        queue.add(this);
        paths.add("");
        while (!queue.isEmpty()) {
            DecisionTree < E > currentNode = queue.remove();
            String path = paths.remove();
            out.print(path + " ");
            out.println(currentNode.getData());

            if (currentNode.isBranch()) {
                paths.add(path + "y");
                queue.add(currentNode.getLeft());
                paths.add(path + "n");
                queue.add(currentNode.getRight());
            }
        }

        out.close();
    }

    /**
     * reads through a file with path + value of each node and creates a DecisionTree.
     * @param filename  String name of the file to be read
     * @return          a DecisionTree of type String with all the node values in the file at the position in the file
     */
    public static DecisionTree < String > readFile(String filename) {
        //System.out.println("Entering readFile.");
        // split the line at the first space character to separate the path string from the node data (using `indexOf` and `substring`)
        File file = new File(filename);
        Scanner scanner = new Scanner(System.in);
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Something weird's going on.");
        }
        String first = scanner.nextLine();
        //System.out.println(first);
        DecisionTree < String > x = new DecisionTree < > (first);


        //System.out.println("Entering while loop next...");

        while (scanner.hasNextLine()) {
            //System.out.println("    Another round of the while loop.");
            String temp = scanner.nextLine();
            String path = temp.substring(0, temp.indexOf(" ") - 1);
            String data = temp.substring(temp.indexOf(" ") + 1, temp.length());

            if (temp.charAt(temp.indexOf(" ") - 1) == 'Y') {
                x.followPath(path).setLeft(new DecisionTree < > (data));
                //System.out.println("Adding " + data + " at path " + path + "Y");
            } else if (temp.charAt(temp.indexOf(" ") - 1) == 'N') {
                x.followPath(path).setRight(new DecisionTree < > (data));
                //System.out.println("Adding " + data + " at path " + path + "N");
            } else {
                //System.out.println("THERE'S BEEN A HORRIBLE MISTAKE!");
            }
        }
        //System.out.println("    While loop done.");

        scanner.close();
        return x;
    }

    /*public static void main(String[] args) {
        DecisionTree < String > x = new DecisionTree < > ("Is it a mammal?");
        x.setLeft(new DecisionTree < > ("Cow"));
        x.setRight(new DecisionTree < > ("Crocodile"));

        try {
            System.out.println("Reached 1");
            x.printTree("AnimalTree");
        } catch (FileNotFoundException e) {
            System.out.println("Reached 2");
        }
        System.out.println("Done!");
    }*/
}