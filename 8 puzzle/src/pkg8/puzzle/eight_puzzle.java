/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8.puzzle;

import java.util.Random;
import java.util.Stack;

/**
 *
 * @author R
 */
public class eight_puzzle {
    
    int Goal_Board[][] = {

        {1, 2, 3},

        {8, 0, 4},

        {7, 6, 5}

    };

    int Problem_Board[][] = {

        {2, 6, 3},

        {1, 0, 4},

        {8, 7, 5}

    };

    //initial empty square 

    int emptySquare_row = 0;

    int emptySquare_col = 0;

    int stepCounter = 0;

    int min_fn;

    Node min_fn_node;


    Random random = new Random();

    Stack<Node> stack_state = new Stack<>();//for backtracking


    //initializations

    public void initializations() {

        Empty_Square();

        min_fn = get_fn(Problem_Board);//-? min fn



        printState(Problem_Board, "PROBLEM:");

        System.out.println("initial empty square position: " + emptySquare_row + ", " + emptySquare_col);

        System.out.println("number of misplaced squares: " + min_fn);

        System.out.println("-----------");



        //start hill climbing search

        try {

            hill_climbing_search();

        } catch (Exception e) {

            System.out.println("Goal can not be reached, found closest solution state");

            printState(min_fn_node.state, "---------solution state------with min fn " + min_fn);

        }

    }



    //start hill climbing search for 8-puzzle problem

    public void hill_climbing_search() throws Exception {



        while (true) {

            System.out.println("- - - - - - - - - - - - - -");

            System.out.println("cost/steps: " + (++stepCounter));

            System.out.println("-----------------");



            //Priority.preState = game_board;//change pre state

            Node lowestPossible_fn_node = getLowestPossible_fn_node();

            addToStackState(priority.neighbors_nodeArray);//add neighbors to stack in high to low order fn



            printState(lowestPossible_fn_node.state, "*******new state");

       
            //check for local maxima

            int fnCounter = 1;

            for (int i = 1; i < priority.neighbors_nodeArray.length; i++) {

                if (priority.neighbors_nodeArray[i - 1].fn == priority.neighbors_nodeArray[i].fn) {//fns are equal

                    fnCounter++;

                }

            }

            if (priority.neighbors_nodeArray.length != 1 && fnCounter == priority.neighbors_nodeArray.length) {//all fns are equal, equal chances to choose

                System.out.println("****fn's are equal, found in local maxima****");



                //backtracking

                for (int i = 0; i < priority.neighbors_nodeArray.length; i++) {

                    if (stack_state != null) {

                        System.out.println("pop " + (i + 1));

                        stack_state.pop();

                    } else {

                        System.out.println("empty stack inside loop");

                    }

                }



                if (stack_state != null) {

                    Node gameNode = stack_state.pop();

                    Problem_Board = gameNode.state;//update game board

                    priority.preState = gameNode.parent;//update prestate

                    Empty_Square();//locate empty tile for updated state



                    printState(Problem_Board, "popped state from all equal fn");

                    System.out.println("empty tile position: " + emptySquare_row + ", " + emptySquare_col);

                } else {

                    System.out.println("stack empty inside first lm check");

                }

            } else {//for backtracking



                System.out.println("lowest fn: " + lowestPossible_fn_node.fn);



                if (lowestPossible_fn_node.fn == 0) {//no misplaced found

                    System.out.println("-------------------------");

                    System.out.println("8-Puzzle has been solved!");

                    System.out.println("-------------------------");

                    System.out.println("Total cost/steps to reach the goal: " + stepCounter);

                    System.out.println("-------------------------------------");

                    break;

                }



                if (lowestPossible_fn_node.fn <= min_fn) {

                    min_fn = lowestPossible_fn_node.fn;

                    min_fn_node = lowestPossible_fn_node;//store lowest fn solution



                    if (stack_state != null) {

                        Node gameNode = stack_state.pop();

                        Problem_Board = gameNode.state;//update game board

                        priority.preState = gameNode.parent;//update prestate

                        Empty_Square();//locate empty tile for updated state



                        printState(Problem_Board, "-------new state as going deeper");

                        System.out.println("empty tile position: " + emptySquare_row + ", " + emptySquare_col);

                    } else {

                        System.out.println("stack empty");

                    }



                } else {

                    System.out.println("---stuck in local maxima---");

                    System.out.println("getting higher, not possible");

                //break;



                    //backtracking

                    for (int i = 0; i < priority.neighbors_nodeArray.length; i++) {

                        if (stack_state != null) {

                            //System.out.println("pop " + (i + 1));

                            stack_state.pop();

                        } else {

                            System.out.println("empty stack inside loop");

                        }



                    }

                    if (stack_state != null) {



                        Node gameNode = stack_state.pop();

                        Problem_Board = gameNode.state;//update game board

                        priority.preState = gameNode.parent;//update prestate

                        Empty_Square();//locate empty tile for updated state



                        printState(Problem_Board, "popped state from getting higher");

                        System.out.println("empty tile position: " + emptySquare_row + ", " + emptySquare_col);

                    } else {

                        System.out.println("stack empty inside second lm check");

                    }

                }//end of if cond: new fn<=pre min fn 

            }//end of if cond: all fn equal

        }//while end

    }



    private Node getLowestPossible_fn_node() {



        if (emptySquare_row == 0 && emptySquare_col == 0) {//0,0 position is empty tile

            //System.out.println("Empty 0,0");

            Node fn_array[] = {get_fn_down(), get_fn_right()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 0 && emptySquare_col == 1) {//0,1 position is empty tile

            //System.out.println("Empty 0,1");

            Node fn_array[] = {get_fn_left(), get_fn_down(), get_fn_right()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 0 && emptySquare_col == 2) {//0,2 position is empty tile

            //System.out.println("Empty 0,2");

            Node fn_array[] = {get_fn_left(), get_fn_down()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 1 && emptySquare_col == 0) {//1,0 position is empty tile

            //System.out.println("Empty 1,0");

            Node fn_array[] = {get_fn_down(), get_fn_right(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 1 && emptySquare_col == 1) {//1,1 position is empty tile

            //System.out.println("Empty 1,1");

            Node fn_array[] = {get_fn_left(), get_fn_down(), get_fn_right(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 1 && emptySquare_col == 2) {//1,2 position is empty tile

            //System.out.println("Empty 1,2");

            Node fn_array[] = {get_fn_left(), get_fn_down(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 2 && emptySquare_col == 0) {//2,0 position is empty tile

            //System.out.println("Empty 2,0");

            Node fn_array[] = {get_fn_right(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 2 && emptySquare_col == 1) {//2,1 position is empty tile

            //System.out.println("Empty 2,1");

            Node fn_array[] = {get_fn_left(), get_fn_right(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        } else if (emptySquare_row == 2 && emptySquare_col == 2) {//2,2 position is empty tile

            //System.out.println("Empty 2,2");

            Node fn_array[] = {get_fn_left(), get_fn_up()};

            Node lowest_fn_node = priority.sort(fn_array);

            return lowest_fn_node;



        }

        return null;

    }



    //----------------------------

    //return number of misplaced tiles for left state

    private Node get_fn_left() {



        int left_state[][] = new int[Problem_Board.length][Problem_Board[0].length];

        for (int i = 0; i < Problem_Board.length; i++) {

            for (int j = 0; j < Problem_Board[0].length; j++) {



                if (i == emptySquare_row && j == emptySquare_col) {//empty tile, swap left

                    left_state[i][j] = Problem_Board[i][j - 1];

                    left_state[i][j - 1] = Problem_Board[i][j];

                } else {//normal copy

                    left_state[i][j] = Problem_Board[i][j];

                }

            }

        }

        printState(left_state, "left state");//print left state

        Node node = new Node(get_fn(left_state), left_state, Problem_Board);

        return node;

    }



    //return number of misplaced tiles for right state

    private Node get_fn_right() {



        int right_state[][] = new int[Problem_Board.length][Problem_Board[0].length];

        for (int i = 0; i < Problem_Board.length; i++) {

            for (int j = 0; j < Problem_Board[0].length; j++) {



                if (i == emptySquare_row && j == emptySquare_col) {//empty tile, swap right

                    right_state[i][j] = Problem_Board[i][j + 1];

                    right_state[i][j + 1] = Problem_Board[i][j];

                    j++;//as j++ position already copied/updated 

                } else {//normal copy

                    right_state[i][j] = Problem_Board[i][j];

                }

            }

        }



        printState(right_state, "right state");//print right state

        Node node = new Node(get_fn(right_state), right_state, Problem_Board);

        return node;

    }



    //return number of misplaced tiles for up state

    private Node get_fn_up() {



        int up_state[][] = new int[Problem_Board.length][Problem_Board[0].length];

        for (int i = 0; i < Problem_Board.length; i++) {

            for (int j = 0; j < Problem_Board[0].length; j++) {



                if (i == emptySquare_row && j == emptySquare_col) {//empty tile, swap up

                    up_state[i][j] =Problem_Board[i - 1][j];

                    up_state[i - 1][j] = Problem_Board[i][j];

                } else {//normal copy

                    up_state[i][j] = Problem_Board[i][j];

                }

            }

        }

        printState(up_state, "up state");//print up state

        Node node = new Node(get_fn(up_state), up_state, Problem_Board);

        return node;

    }



    //return number of misplaced tiles for down state

    private Node get_fn_down() {



        int down_state[][] = new int[Problem_Board.length][Problem_Board[0].length];

        for (int i = 0; i < Problem_Board.length; i++) {

            for (int j = 0; j < Problem_Board[0].length; j++) {



                if ((i - 1) == emptySquare_row && j == emptySquare_col) {//down pos of empty tile, swap down

                    down_state[i][j] = Problem_Board[i - 1][j];

                    down_state[i - 1][j] = Problem_Board[i][j];

                } else {//normal copy

                    down_state[i][j] = Problem_Board[i][j];

                }

            }

        }

        printState(down_state, "down state");//print down state

        Node node = new Node(get_fn(down_state), down_state, Problem_Board);

        return node;

    }



    //takes a game state and returns number of misplaced tiles

    private int get_fn(int[][] game_state) {



        int fn_count = 0;

        for (int i = 0; i < game_state.length; i++) {

            for (int j = 0; j < game_state[0].length; j++) {

                if (game_state[i][j] != Goal_Board[i][j] && game_state[i][j] != 0) {//found misplaced tiles

                    fn_count++;

                }

            }

        }

        return fn_count;

    }



    //takes parent removed,  sorted node array and add states to stack in high to low order 

    private void addToStackState(Node nodeArray[]) {

        for (int i = nodeArray.length - 1; i >= 0; i--) {

            stack_state.add(nodeArray[i]);//highest fn to lowest fn

        }

    }



    //find out the new empty tile position for current state

    private void Empty_Square() {



        nestedloop://to break inner and outer loop

        for (int i = 0; i < Problem_Board.length; i++) {

            for (int j = 0; j < Problem_Board[0].length; j++) {

                if (Problem_Board[i][j] == 0) {

                    emptySquare_row = i;

                    emptySquare_col = j;

                    break nestedloop;

                }

            }

        }

    }



    //print the current state of the game board

    private void printState(int[][] state, String message) {

        System.out.println(message);

        for (int i = 0; i < state.length; i++) {

            for (int j = 0; j < state[0].length; j++) {

                System.out.print(state[i][j] + "  ");

            }

            System.out.println();

        }

        System.out.println("--------");

    }

}
    
    


    

