/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eight.queen.hillclimbing;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author R
 */
public class EightQueenHillclimbing {

    public class Node implements Comparable<Node>{

	private static final int N=8; //8 queens

	public Queen[] state; //the node's state

	private ArrayList<Node> neighbours;

	private int hn; //heuristic score

	/* Constructor*/
        
	public Node(){

		state = new Queen[N]; //empty state

		neighbours = new ArrayList<Node>(); //empty neighbour list

	} 


	public Node(Node n){

		state = new Queen[N];

		neighbours = new ArrayList<Node>();

		for(int i=0; i<N; i++)

			state[i] = new Queen(n.state[i].getRow(), n.state[i].getColumn());

		hn=0;

	}

	public ArrayList<Node> generateNeighbours(Node startState){

		int count=0;
                
		for(int i=0; i<N; i++){

			for(int j=1; j<N; j++){

				neighbours.add(count, new Node(startState));

				neighbours.get(count).state[i].moveDown(j);

				//make sure to compute its hn value

				neighbours.get(count).computeHeuristic();

				

				count++;

			}

		}

		

		return neighbours;

	}


	public Node getRandomNeighbour(Node startState){

		Random gen = new Random();

		int col = gen.nextInt(N);

		int d = gen.nextInt(N-1)+1;

		Node neighbour = new Node(startState);

		neighbour.state[col].moveDown(d);

		neighbour.computeHeuristic();

		return neighbour;

	}

	public int computeHeuristic(){

		for(int i=0; i<N-1; i++){

			for(int j=i+1; j<N; j++){

				if(state[i].canAttack(state[j])){

						hn++;

				}
			}
		}

		return hn;
	}

        
//compares based on heuristic values
        
	public int compareTo(Node n){

		if(this.hn < n.hn)

			return -1;

		else if(this.hn > n.hn)

			return 1;

		else 

			return 0;

	}

	public void setState(Queen[] s){

		for(int i=0; i<N; i++){

			state[i]= new Queen(s[i].getRow(), s[i].getColumn());

		}
	}

	public Queen[] getState(){

		return state;

	}

	public String toString(){

		String result="";

		String[][] board = new String[N][N];

                
		// empty spaces
		for(int i=0; i<N; i++)

			for(int j=0; j<N; j++)

				board[i][j]="0 ";

		

		//place the queens on the board
		for(int i=0; i<N; i++){

			board[state[i].getRow()][state[i].getColumn()]="Q ";

		}

		

		//print values 
		for(int i=0; i<N; i++){

			for(int j=0; j<N; j++){

				result+=board[i][j];

			}

			result+="\n";

		}

		

		return result;

	}

}
    
    
    
    
    
    public class Queen {

	private int row;

	private int column;

	

	/**

	 * Constructor. Sets Queen's row and column

	 * @param r

	 * @param c

	 */

	public Queen(int r, int c){

		row = r;

		column  = c;

	}

	public boolean canAttack(Queen q){

		boolean canAttack=false;

		//test rows and columns

		if(row==q.getRow() || column==q.getColumn())

			canAttack=true;

		//test diagonal

		else if(Math.abs(column-q.getColumn()) == Math.abs(row-q.getRow()))

			canAttack=true;

			

		return canAttack;

	}


	public void moveDown(int spaces){

		row+=spaces;

		if(row>7 && row%7!=0){

			row=(row%7)-1;

		}

		else if(row>7 && row%7==0){

			row=7;

		}

	}


	public void setRow(int r){

		row = r;

	}


	public int getRow(){

		return row;

	}


	public void setColumn(int c){

		column = c;

	}

	public int getColumn(){

		return column;

	}

	

	public String toString(){

		return "("+row+", "+column+")";

	}

}
    
    
    
    public class RandomRestart {

	private HillClimbing hillClimber;

	private int nodesGenerated;

	private Node start;


	public RandomRestart(Queen[] startBoard){

		hillClimber = new HillClimbing(startBoard);

		nodesGenerated = 0;

	}

	public Node randomRestart(){

		Node currentNode = hillClimber.getStartNode();

		setStartNode(currentNode);

		int heuristic = currentNode.hn;

				

		while(heuristic!=0){

			Node nextNode = hillClimber.hillClimbing();

			nodesGenerated+=hillClimber.getNodesGenerated();

			heuristic = nextNode.hn;

			

			if(heuristic!=0){ //restart

				hillClimber = new HillClimbing();

			}else

				currentNode = nextNode;

		}

		return currentNode;

	}

	

	public void setStartNode(Node n){

		start = n;

	}


	public Node getStartNode(){

		return start;

	}

	public int getNodesGenerated(){

		return nodesGenerated;

	}

}
    
    public class HillClimbing {

	private final static int N=8;

	private Queen[] startState;

	private Node start; //start state

	private int nodesGenerated;


	public HillClimbing(){

		start = new Node(); //empty start node

		startState = new Queen[N]; //empty start state

		startState();

		nodesGenerated=0;

	}


	public HillClimbing(Queen[] s){

		start = new Node();

		startState = new Queen[N];

		for(int i=0; i<s.length; i++){

			startState[i] = new Queen(s[i].getRow(), s[i].getColumn());

		}

		start.setState(startState);

		start.computeHeuristic();

		

		nodesGenerated=0;

	}



	public void startState(){
            
		Random gen = new Random();

		for(int i=0; i<N; i++){

			startState[i] = new Queen(gen.nextInt(N), i);

		}

		start.setState(startState);

		start.computeHeuristic();


	}

	public Node hillClimbing(){

		Node currentNode = start;

		while(true){

			ArrayList<Node> successors = currentNode.generateNeighbours(currentNode);

			nodesGenerated+=successors.size();

			

			Node nextNode = null;

			

			for(int i=0; i<successors.size(); i++){

				if(successors.get(i).compareTo(currentNode) < 0){

					nextNode = successors.get(i);

				}

			}

			

			if(nextNode==null)

				return currentNode;

			

			currentNode = nextNode;

		}

	}


	public Node getStartNode(){

		return start;

	}

	public int getNodesGenerated(){

		return nodesGenerated;

	}

}

	public Queen[] generateBoard(){

		Queen[] start = new Queen[8];

		Random gen = new Random();

		

		for(int i=0; i<8; i++){

			start[i] = new Queen(gen.nextInt(8),i);

		}

		return start;

	}
 
        public class SimulatedAnnealing {

	private final static int N=8;

	int nodesGenerated;

	private Queen[] startState;

	private Node start;

	public SimulatedAnnealing(Queen[] s){

		nodesGenerated = 0;

		start = new Node();

		startState = new Queen[N];

		

		for(int i=0; i<N; i++){

			startState[i] = new Queen(s[i].getRow(), s[i].getColumn());

		}

		start.setState(startState);

		start.computeHeuristic();

	}


	public void startState(){

		start = new Node();

		startState = new Queen[N];

		Random gen = new Random();

		

		for(int i=0; i<N; i++){

			startState[i] = new Queen(gen.nextInt(N), i);

		}

		start.setState(startState);

		start.computeHeuristic();

	}

	public Node simulatedAnneal(double initialTemp, double step){

		Node currentNode = start;

		double temperature = initialTemp;

		double val = step;

		double probability;

		int delta;

		double determine;

		

		Node nextNode = new Node();

		

		while(currentNode.hn!=0 && temperature > 0){

			//select a random neighbour from currentNode

			nextNode = currentNode.getRandomNeighbour(currentNode);

			nodesGenerated++;

			

			if(nextNode.hn==0)

				return nextNode;

			

			delta = currentNode.hn - nextNode.hn;

			

			if(delta > 0){ //currentNode has a higher heuristic

				currentNode = nextNode;

			}else{ 

				probability = Math.exp(delta/temperature);

				//Do we want to choose nextNode or stick with currentNode?

				determine = Math.random();

				

				if(determine <= probability){ //choose nextNode

					currentNode = nextNode;

				}

			}

			temperature = temperature - val;

		}

		

		return currentNode;

	}


	public int getNodesGenerated(){

		return nodesGenerated;

	}

	

	public Node getStartNode(){

		return start;

	}

}
        
        public static void main(String[] args) {
        // TODO code application logic here
        
        EightQueenHillclimbing board = new EightQueenHillclimbing();

		int numberOfRuns = 12;

		int hillClimbNodes=0, randomRestartNodes=0, annealNodes=0;

		int hillClimbSuccesses=0, randomRestartSuccesses=0, annealSuccesses=0;

		

		for(int i=0; i<numberOfRuns; i++){

			Queen[] startBoard = board.generateBoard();

                        

			HillClimbing hillClimber = new HillClimbing(startBoard);

			RandomRestart randomRestart = new RandomRestart(startBoard);

			SimulatedAnnealing anneal = new SimulatedAnnealing(startBoard);			

			

			Node hillSolved = hillClimber.hillClimbing();

			Node randomSolved = randomRestart.randomRestart();

			Node annealSolved = anneal.simulatedAnneal(28, 0.0001);

			

			if(hillSolved.hn==0){

				//System.out.println("Hill Climbing Solved:\n"+hillSolved);

				hillClimbSuccesses++;

			}

			if(randomSolved.hn==0){

				//System.out.println("Random Restart Solved:\n"+randomSolved);

				randomRestartSuccesses++;

			}

			if(annealSolved.hn==0){

				//System.out.println("Anneal Solved:\n"+annealSolved);

				annealSuccesses++;

			}

			

			hillClimbNodes += hillClimber.getNodesGenerated();

			randomRestartNodes += randomRestart.getNodesGenerated();

			annealNodes += anneal.getNodesGenerated();

		}

		

		System.out.println("Hill climb successes: "+hillClimbSuccesses);

		System.out.println("Random restart successes: "+randomRestartSuccesses);

		System.out.println("Simulated Annealing successes: "+annealSuccesses);

		System.out.println();

		

		double hillClimbPercent = (double)hillClimbSuccesses/(double)numberOfRuns;

		System.out.println(hillClimbPercent);

		double randomRestartPercent = (double)(randomRestartSuccesses/numberOfRuns);

		double annealPercent = (double)(annealSuccesses/numberOfRuns);

		NumberFormat fmt = NumberFormat.getPercentInstance();

		

		System.out.println("Hill climbing:\nNodes: "+hillClimbNodes);

		System.out.println("Percent successes: "+fmt.format(hillClimbPercent));

		System.out.println("Random Restart:\nNodes: "+randomRestartNodes);

		System.out.println("Percent successes: "+fmt.format(randomRestartPercent));

		System.out.println("Simulated Annealing:\nNodes: "+annealNodes);

		System.out.println("Percent successes: "+fmt.format(annealPercent));

	}

        
        
        
}
        
    
    

