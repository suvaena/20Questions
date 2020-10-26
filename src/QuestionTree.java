//Suvaena Laventhiran
//Project 3
//November 25, 2019
//Plays a game of 20 questions with the user
//User input
//Outputs the game to text file

import java.io.PrintStream;
import java.util.Scanner;


public class QuestionTree {

	//state field values for the class (private)
	private QuestionNode root = new QuestionNode (); //root of the binary search tree
	private int numGamesPlayed = 0;
	private int numGamesWon_Comp = 0;
	
	private Scanner input;
	private PrintStream output;
	
	public QuestionTree (Scanner input, PrintStream output)
	{
		if (input == null)
		{
			throw new IllegalArgumentException();
		}
		if (output == null)
		{
			throw new IllegalArgumentException();
		}
		//NEED TO CONSTRUCT QUESTION NODE
			root.data = "Jedi";
                
		this.input = input;
		this.output = output;
	}
	
	//PLAY method - needs a public private pair
	public void play ()
	{
		//start playing one iteration of the guessing game
		//ask yes or no questions until the computer has traversed from the node to the leaves
		//if computer does win, print win message 
		//if computer doesn't win
			//ask user what object they are thinking of
			//ask for a question that will distinguish the object from player's guess
			//ask if the player's object is yes or no to the answer of that question
		//all user input and output done through Scanner and PrintStream
		//after game ends, client program prompts the user to play again or not (not part of this code!)

		root = play(root); //use the x = change(x) technique
	}
	
	//game process
	private QuestionNode play(QuestionNode node)
	{
		//currently at the answer leaf node or if game starts with 1 node present in QuestionTree --> output computer guess now 
		if (node.left == null && node.right == null)
		{
			output.print("Would your object happen to be " + node.data + "?");
			boolean check_questionAnswer = UserInterface.nextAnswer(input);
			
			//if computer guessed CORRECT answer
			if (check_questionAnswer)
			{
				output.println("I win!");
				numGamesWon_Comp++;
			}
			
			//if computer guessed INCORRECT answer
			else if (!check_questionAnswer)
			{
				output.print("I lose. What is your object?");
				String correctAnswer = input.nextLine();
				
				output.print("Type a yes/no question to distinguish your item from " + node.data + ":");
				String question = input.nextLine();
				
				output.print("And what is the answer for your object?");
				check_questionAnswer = UserInterface.nextAnswer(input);
				
				//create a new node
				QuestionNode newQuestion = new QuestionNode (question);
				QuestionNode newAnswer = new QuestionNode (correctAnswer);
				QuestionNode oldNode = node;
				
				if (check_questionAnswer == true)	//response was yes
				{
					newQuestion.left = newAnswer;
					newQuestion.right = oldNode;
				}
				
				else if (check_questionAnswer == false)	//response was no
				{
					newQuestion.left = oldNode;
					newQuestion.right = newAnswer;
				}
				
				node = newQuestion;
			}
			numGamesPlayed++;
		}
		
		//at a question node - just output the question and then use recursion to see if the child nodes are answers and questions until you finally reach an answer node
		else if (node.left != null && node.right != null)
		{
			//print question
			output.print(node.data);
			//get the yes/no answer
			boolean check_questionAnswer = UserInterface.nextAnswer(input);
			
			if (check_questionAnswer == true)	//goes to the left child and plays
			{
				node.left = play(node.left);
			}
			else if (check_questionAnswer == false)	//goes to the right child and plays
			{
				node.right = play(node.right);
			}
		}
		return node;
	}
	
	public void save (PrintStream output)
	{
		if (output == null)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			save (output, root);
		}
		
	}
	
	private void save (PrintStream output, QuestionNode root)
	{
		if (output == null)
		{
			throw new IllegalArgumentException();
		}
		
		if (root == null)
		{
			//throw new IllegalArgumentException();
		}
		else{
			//if both branches are null then it must be an answer node
			if (root.left==null && root.right==null)
			{
				output.println("A:" + root.data);
			}
			
			//if both branches are not null then it must be a question node
			if ((root.left != null) && (root.right != null))
			{				
				output.println("Q:" + root.data);
			}
		
			save(output, root.left);
			save(output, root.right);
		
		}
		
	}
	
	public void load (Scanner input)
	{
		if (input == null)
		{
			throw new IllegalArgumentException();
		}
		//replace current tree by reading from another tree from a file
		root = load(input, root);
	}
	
	private QuestionNode load (Scanner input, QuestionNode node)
	{
		if (input == null)
		{
			throw new IllegalArgumentException();
		}
		
		String word = input.nextLine();
		
		//check if line starts with A
		if (word.startsWith("A:"))
		{
			QuestionNode n1 = new QuestionNode(word.substring(2));
			return n1;
		}
		
		//check if line starts with Q
		else if (word.startsWith("Q:"))
		{
			QuestionNode n1 = new QuestionNode (word.substring(2));
			n1.left = load(input, n1.left);
			n1.right = load(input, n1.right);
			//update tree parameter as you go
			return n1;
		}
		
		return node;
	}
	
	//return an integer value
	public int totalGames()
	{
		return numGamesPlayed;
	}
	
	//return an integer value
	public int gamesWon()
	{
		return numGamesWon_Comp;
	}
	
}
