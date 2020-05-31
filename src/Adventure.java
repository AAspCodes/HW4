/*
 * File: Adventure.java
 * --------------------
 * This program plays the Adventure game from Assignment #4.
 */

import java.io.*;
import java.util.*;

/* Class: Adventure */
/**
 * This class is the main program class for the Adventure game.
 */

public class Adventure extends AdventureStub {

	// Use this scanner for any console input
	private static Scanner scan = new Scanner(System.in);

	/**
	 * This method is used only to test the program
	 */
	public static void setScanner(Scanner theScanner) {
		scan = theScanner;
		// Delete the following line when done
		AdventureStub.setScanner(theScanner);
	}

	/**
	 * Runs the adventure program
	 */
	public static void main(String[] args) {
		System.out.print("What will be you adventure today? ");
		
		// get game choice
		String input = scan.nextLine();
		input = input.substring(0,1).toUpperCase() + input.substring(1,input.length()).toLowerCase();
		
		
		
		
		Adventure ad = new Adventure(
								loadRooms(input),
								createCommandMap()
								);
		
		ad.mainLoop();
		
	}
	
	
	
	/**
	 * Return hash map of all commands.
	 * 
	 * @return 
	 * 		HashMap<String,AdvCommand> of all commands.
	 */
	private static Map<String, AdvCommand> createCommandMap() {
		Map<String,AdvCommand> commands = new HashMap<String, AdvCommand>();
		commands.put("QUIT", AdvCommand.QUIT);
		commands.put("LOOK", AdvCommand.LOOK);
		commands.put("INVENTORY", AdvCommand.INVENTORY);
		commands.put("TAKE", AdvCommand.TAKE);
		commands.put("DROP", AdvCommand.DROP);
		commands.put("HELP", AdvCommand.HELP);
		return commands;
	}
	
	
	/**
	 * Load the the AdvRoom data from the {input}Rooms.txt file into an ArrayList of AdvRoom objects.
	 * return the ArrayList.
	 * 
	 * @param input
	 * 		The string input by the user for which game version they want to play.
	 * @return
	 * 		ArrayList<AdvRoom> of Rooms
	 */
	private static List<AdvRoom> loadRooms(String input) {
		Scanner roomScanner;
		
		// load rooms Data into scanner
		try {
			roomScanner = new Scanner(new File(input + "Rooms.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		
		List<AdvRoom> rooms = new ArrayList<AdvRoom>();
		// make index 0 null so that the room numbers line up with their index.
		rooms.add(null);
		
		
		AdvRoom newRoom;
		
		while ((newRoom = AdvRoom.readFromFile(roomScanner)) != null) {
			rooms.add(newRoom);
		}
		
		
		List<AdvObject> objects = loadObjects(input);
		for (AdvObject obj: objects) {
			AdvRoom room = rooms.get(obj.getInitialLocation());
			room.addObject(obj);
		}
		
		
		return rooms;
	}
	
	private static List<AdvObject> loadObjects(String input) {
		Scanner objScanner;
		
		// load rooms Data into scanner
		try {
			objScanner = new Scanner(new File(input + "Objects.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
			return null;
		}
		
		List<AdvObject> objects = new ArrayList<AdvObject>();
		AdvObject newObject;
		
		while ((newObject = AdvObject.readFromFile(objScanner)) != null) {
			objects.add(newObject);
		}
		
		return objects;
	}
	
	
	/**
	 * Construct an Adventure Object.
	 * 
	 * @param rooms
	 * 		 ArrayList of AdvRoom objects.
	 * @param objects
	 * 		 ArrayList of AdvObject objects.
	 * @param commands
	 * 		HashMap of AdvCommand subclass Objects
	 */
	private Adventure(List<AdvRoom> rooms, Map<String, AdvCommand> commands) {
		this.rooms = rooms;
		this.commands = commands;
		setRoom(1);
	}
	
	
	
	private void mainLoop() {
		command("LOOK");		
		while (true) {
			parseInput(scan.nextLine().toUpperCase());
		}
	}
	
	private void parseInput(String input) {
		if (input.equals("")) {
			return;
		}
		// check if a direction
		if (go(input)) {
			return;
		}
		
		String[] splitInput = input.split(" ");
		
		if (splitInput.length > 2) {
			System.out.println("invalid command");
			return;
		}
		
		String verb = splitInput[0];
		
		if (splitInput.length > 1) {
			// check for object
			String objectName = splitInput[1];
		}
		
		// check if a command
		if (command(verb)) {
			return;
		}
		
// damn take and give...
		
		// else print "invalid command"
		// couldn't parse the input
		System.out.println("invalid input");
	}
	
	private boolean go(String msg) {	
		for ( AdvMotionTableEntry entry: room.getMotionTable()) {
			if (msg.equals(entry.getDirection())) {
				(new AdvMotionCommand(msg)).execute(this, null);
				return true;
			}
		}
		
		// no match was found
		return false;
	}
	
	private boolean command(String msg) {
		return command(msg, null);
	}
	
	private boolean command(String msg, AdvObject obj) {
			AdvCommand c = commands.get(msg);
			if (c != null) {
				c.execute(this, obj);
				return true;
			} else {
				return false;
			}
	}
	


	/* Method: executeMotionCommand(direction) */
	/**
	 * Executes a motion command. This method is called from the
	 * AdvMotionCommand class to move to a new room.
	 * 
	 * @param direction
	 *            The string indicating the direction of motion
	 */
	public void executeMotionCommand(String direction) {
		int roomNum = 0;
		
		for ( AdvMotionTableEntry entry: room.getMotionTable()) {
			// if direction is valid
			if (direction.equals(entry.getDirection())) {
				
				if (entry.getKeyName() != null && inventory.get(entry.getKeyName()) == null) {
						// key was needed, and you don't have the key
						continue;
				} else {
					// either a key was needed, and you had it, or no key was needed
					roomNum = entry.getDestinationRoom();
					break;
				}
			}
		}
		
		setRoom(roomNum);
		command("LOOK");
		
	}
	
	private void setRoom(int roomNum) {
		if (roomNum == 0) {
			// game ended
			System.out.println("Thank you for playing.");
			System.exit(0);
			return;
		}
		
		
		room = rooms.get(roomNum);
	}
	


	/* Method: executeQuitCommand() */
	/**
	 * Implements the QUIT command. This command should ask the user to confirm
	 * the quit request and, if so, should exit from the play method. If not,
	 * the program should continue as usual.
	 */
	public void executeQuitCommand() {
		System.out.print("Are you sure (Y/N)? ");
		
		if ( scan.nextLine().toUpperCase().equals("Y")) {
			// exit
			System.out.println("ok, bye bye");
			System.exit(0);
		}
	}

	/* Method: executeHelpCommand() */
	/**
	 * Implements the HELP command. Your code must include some help text for
	 * the user.
	 */
	public void executeHelpCommand() {
		Scanner hScan;
		
		// try to read the help file into the scanner object.
		try {
			hScan = new Scanner(new File("help.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		// print out every line.
		while (hScan.hasNext()) {
			System.out.println(hScan.nextLine());
		}
		
		hScan.close();
		
	}

	/* Method: executeLookCommand() */
	/**
	 * Implements the LOOK command. This method should give the full description
	 * of the room and its contents.
	 */
	public void executeLookCommand() {
		this.room.look();
	}

	/* Method: executeInventoryCommand() */
	/**
	 * Implements the INVENTORY command. This method should display a list of
	 * what the user is carrying.
	 */
	public void executeInventoryCommand() {
		if(this.inventory.size() > 0) {
			for (int i = 0; i < this.inventory.size(); i++) {
				System.out.println(inventory.get(i));
			}

		} else {
			System.out.println("You are empty-handed.");
		}
	}

	/* Method: executeTakeCommand(obj) */
	/**
	 * Implements the TAKE command. This method should check that the object is
	 * in the room and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to take
	 */
	public void executeTakeCommand(AdvObject obj) {
		if (this.room.containsObject(obj)) {
			this.inventory.add(obj);
			this.room.removeObject(obj);
			System.out.println("Object taken.");
		} else {
			System.out.println("Cannot take object.");
		}

	}

	/* Method: executeDropCommand(obj) */
	/**
	 * Implements the DROP command. This method should check that the user is
	 * carrying the object and deliver a suitable message if not.
	 * 
	 * @param obj
	 *            The AdvObject you want to drop
	 */
	public void executeDropCommand(AdvObject obj) {
		if (this.inventory.contains(obj)) {
			this.inventory.remove(obj);
			this.room.addObject(obj);
			System.out.println("You have dropped the " + obj.getName() + ".");
		} else {
			System.out.println("Cannot drop the " + obj.getName() + ".");
		}
		

	}

	/* Private instance variables */
	
	private AdvRoom room;
	private List<AdvRoom> rooms;
	private List<AdvObject> inventory;
	private Map<String, AdvCommand> commands;
}
