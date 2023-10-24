import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class App {

    private GameHelper helper = new GameHelper();
    private ArrayList<Ship> shipList = new ArrayList<Ship>();
    private int numOfGuesses = 0;

    private void setUpGame() {

        Ship one = new Ship();
        one.setName("HMS Hood");
        Ship two = new Ship();
        two.setName("HMS Prince of Wales");
        Ship three = new Ship();
        three.setName("HMS Repulse");
        shipList.add(one);
        shipList.add(two);
        shipList.add(three);

        System.out.println("Your goal is to sink three ships.");
        System.out.println("HMS Hood, HMS Prince of Wales, HMS Repulse");
        System.out.println("Try to sink them all in the fewest number of guesses");

        for (Ship shipToSet : shipList) {
            ArrayList<String> newLocation = helper.placeShip(3);
            shipToSet.setLocationCells(newLocation);
        } // close for loop
    }

    private void startPlaying() {
        while (!shipList.isEmpty()) {
            String userGuess = helper.getUserInput("Enter a guess");
            checkUserGuess(userGuess);
        } // close while
        finishGame();
    }

    private void finishGame() {
        System.out.println("All ships are sunk! Your stock is now worthless.");
        if (numOfGuesses <= 18) {
            System.out.println("It only took you " + numOfGuesses + " guesses.");
            System.out.println("You got out before your options sank.");
        } else {
            System.out.println("Took you long enough. " + numOfGuesses + " guesses.");
            System.out.println("Fish are dancing with your options.");
        }
    }

    private void checkUserGuess(String userGuess) {
        numOfGuesses++;
        String result = "miss";

        for (Ship shipToTest : shipList) {
            result = shipToTest.checkYourself(userGuess);

            if (result.equals("hit")) {
                break;
            }
            if (result.equals("kill")) {
                shipList.remove(shipToTest);
                break;
            }
        } // close for
        System.out.println(result);
    }

    public static void main(String[] args) {
        App game = new App();
        game.setUpGame();
        game.startPlaying();
    }

    class Ship {

        private ArrayList<String> locationCells;
        private String name;

        public void setLocationCells(ArrayList<String> loc) {
            locationCells = loc;
        }

        public void setName(String n) {
            name = n;
        }

        public String checkYourself(String userInput) {
            String result = "miss";
            int index = locationCells.indexOf(userInput);
            if (index >= 0) {
                locationCells.remove(index);

                if (locationCells.isEmpty()) {
                    result = "kill";
                    System.out.println("Ouch! You sunk " + name + "  : ( ");
                } else {
                    result = "hit";
                }
            }
            return result;
        }
    }

    class GameHelper {

        private static final String alphabet = "abcdefg";
        private int gridLength = 3;
        private int gridSize = 9;
        private int[] grid = new int[gridSize];
        private int comCount = 0;

        public String getUserInput(String prompt) {
            String inputLine = null;
            System.out.print(prompt + "  ");
            try {
                BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
                inputLine = is.readLine();
                if (inputLine.length() == 0)
                    return null;
            } catch (IOException e) {
                System.out.println("IOException: " + e);
            }
            return inputLine.toLowerCase();
        }

        public ArrayList<String> placeShip(int shipSize) {
            ArrayList<String> alphaCells = new ArrayList<String>();
            String temp = null;
            int[] coords = new int[shipSize];
            int attempts = 0;
            boolean success = false;
            int location = 0;

            comCount++;
            int incr = 1;
            if ((comCount % 2) == 1) {
                incr = gridLength;
            }

            while (!success && attempts++ < 200) {
                location = (int) (Math.random() * gridSize);
                int x = 0;
                success = true;
                while (success && x < shipSize) {
                    if (grid[location] == 0) {
                        coords[x++] = location;
                        location += incr;
                        if (location >= gridSize) {
                            success = false;
                        }
                        if (x > 0 && (location % gridLength == 0)) {
                            success = false;
                        }
                    } else {
                        success = false;
                    }
                }
            }

            int x = 0;
            int row = 0;
            int column = 0;

            while (x < shipSize) {
                grid[coords[x]] = 1;
                row = (int) (coords[x] / gridLength);
                column = coords[x] % gridLength;
                temp = String.valueOf(alphabet.charAt(column));

                alphaCells.add(temp.concat(Integer.toString(row)));
                x++;
            }
            return alphaCells;
        }
    }
}
