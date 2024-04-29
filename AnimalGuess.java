import java.io.FileNotFoundException;
import java.util.Scanner;

public class AnimalGuess {

    /**
     * Checks if user input is valid and analyzes if given a yes or no
     * @param input user inputted String
     * @return      boolean with value "true" if given a valid 'yes', "false" if given a value 'no', and throws an exception if given invalid input.
     */
    public static boolean readInput(String input) {
        input.toLowerCase();
        if (input.equals("y") || input.equals("yes")) {
            return true;
        } else if (input.equals("n") || input.equals("no")) {
            return false;
        } else {
            throw new RuntimeException("Invalid input. Recieved " + input + ", expecting 'y', 'n', 'yes', or 'no'.");
        }
    }

    /**
     * Loops until given valid user input
     * @param scanner   Scanner used to take user input
     * @param ans       initial user answer, unchecked
     * @return          final user answer, checked
     */
    public static String input(Scanner scanner, String ans) {
        boolean accepted = false;
        while (!accepted) {
            try {
                readInput(ans);
                accepted = true;
                return ans;
            } catch (Exception e) {
                System.out.println("Invalid input. Recieved " + ans + ", expecting 'y', 'n', 'yes', or 'no.' Try again.");
                ans = scanner.nextLine();
            }
        }
        return "";
    }

    /**
     * Runs an animal-guessing game that runs for as many rounds as a user wants. The tree of questions adapts to user input.
     * @param args  if not empty, then the first index is used to set up a tree with that file's values. Otherwise, the game runs with a starting tree of "cat."
     */
    public static void main(String[] args) {
        DecisionTree < String > animals;
        if (args.length != 0) {
            animals = DecisionTree.readFile(args[0]);
        } else {
            // each pass through the loop is one guessing round
            animals = new DecisionTree < > ("cat");
        }
        DecisionTree < String > further = animals;
        String path = "";
        Scanner scanner = new Scanner(System.in);
        boolean keepGoing = true;

        while (keepGoing) {
            // adaptive section
            if (further.isLeaf()) {

                System.out.println("Is your animal a " + further.getData() + "?");
                String result = scanner.nextLine();
                result = input(scanner, result);

                if (readInput(result)) {
                    System.out.println("I guessed it!");

                } else {
                    System.out.println("What was your animal?");
                    String newAnimal = scanner.nextLine();
                    System.out.println("Type a yes or no question that would distinguish between a " + further.getData() + " and a " + newAnimal + "?");
                    String newQuestion = scanner.nextLine();
                    System.out.println("Would you answer yes to this question for the " + newAnimal + "?");
                    boolean newAnimalAns = readInput(input(scanner, scanner.nextLine()));

                    String oldAnimal = animals.followPath(path).getData();
                    animals.followPath(path).setData(newQuestion);
                    if (newAnimalAns) {
                        animals.followPath(path).setLeft(new DecisionTree < > (newAnimal));
                        animals.followPath(path).setRight(new DecisionTree < > (oldAnimal));
                    } else {
                        animals.followPath(path).setRight(new DecisionTree < > (newAnimal));
                        animals.followPath(path).setLeft(new DecisionTree < > (oldAnimal));
                    }
                }

                System.out.println("Play again?");
                keepGoing = readInput(input(scanner, scanner.nextLine()));
                if (!keepGoing) {
                    System.out.println("Sorry to see you go!");
                    try {
                        if (args.length != 0) {
                            System.out.println("Printing tree in " + args[0]);
                            animals.printTree(args[0]);
                        } else {
                            System.out.println("    Printing tree in new file Animals.txt.");
                            animals.printTree("Animals.txt");
                        }

                    } catch (FileNotFoundException e) {
                        System.out.println("File not found");
                    }
                } else {
                    further = animals;
                    path = "";
                }
            } else {
                System.out.println(further.getData());
                boolean response = readInput(input(scanner, scanner.nextLine()));

                if (response) {
                    further = further.getLeft();
                    path += "y";
                } else {
                    further = further.getRight();
                    path += "n";
                }

                //System.out.println("    Moved down to " + further.getData() + " and path = " + path);
            }
        }

    }
}