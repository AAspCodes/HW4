/*
 * File: AdvRoom.java
 * ------------------
 * This file defines a class that models a single room in the
 * Adventure game.
 */

import java.io.*;
import java.util.*;

/* Class: AdvRoom */
/**
 * This class defines a single room in the Adventure game. A room is
 * characterized by the following properties:
 * 
 * <ul>
 * <li>A room number, which must be greater than zero
 * <li>Its name, which is a one-line string identifying the room
 * <li>Its description, which is a multiline array describing the room
 * <li>A list of objects contained in the room
 * <li>A flag indicating whether the room has been visited
 * <li>A motion table specifying the exits and where they lead </li>
 * 
 * The external format of the room data file is described in the assignment
 * handout. The comments on the methods exported by this class show how to use
 * the initialized data structure.
 */

public class AdvRoom{

	/* Method: getRoomNumber() */
	/**
	 * Returns the room number.
	 * 
	 * @usage int roomNumber = room.getRoomNumber();
	 * @return The room number
	 */
	public int getRoomNumber() {
		return roomNumber; // Replace with your code
	}

	/* Method: getName() */
	/**
	 * Returns the room name, which is its one-line description.
	 * 
	 * @usage String name = room.getName();
	 * @return The room name
	 */
	public String getName() {
		return name; // Replace with your code
	}

	/* Method: getDescription() */
	/**
	 * Returns an array of strings that correspond to the long description of
	 * the room (including the list of the objects in the room).
	 * 
	 * @usage String[] description = room.getDescription();
	 * @return An array of strings giving the long description of the room
	 */
	public String[] getDescription() {
		return desc; // Replace with your code
	}

	/* Method: addObject(obj) */
	/**
	 * Adds an object to the list of objects in the room.
	 * 
	 * @usage room.addObject(obj);
	 * @param The
	 *            AdvObject to be added
	 */
	public void addObject(AdvObject obj) {
		objectList.add(obj);
	}

	/* Method: removeObject(obj) */
	/**
	 * Removes an object from the list of objects in the room.
	 * 
	 * @usage room.removeObject(obj);
	 * @param The
	 *            AdvObject to be removed
	 */
	public void removeObject(AdvObject obj) {
		objectList.remove(obj);
	}

	/* Method: containsObject(obj) */
	/**
	 * Checks whether the specified object is in the room.
	 * 
	 * @usage if (room.containsObject(obj)) . . .
	 * @param The
	 *            AdvObject being tested
	 * @return true if the object is in the room, and false otherwise
	 */
	public boolean containsObject(AdvObject obj) {
		return objectList.contains(obj);
	}

	/* Method: getObjectCount() */
	/**
	 * Returns the number of objects in the room.
	 * 
	 * @usage int nObjects = room.getObjectCount();
	 * @return The number of objects in the room
	 */
	public int getObjectCount() {
		return objectList.size();
	}

	/* Method: getObject(index) */
	/**
	 * Returns the specified element from the list of objects in the room.
	 * 
	 * @usage AdvObject obj = room.getObject(index);
	 * @return The AdvObject at the specified index position
	 */
	public AdvObject getObject(int index) {
		return objectList.remove(index);
	}

	/* Method: setVisited(flag) */
	/**
	 * Sets the flag indicating that this room has been visited according to the
	 * value of the parameter. Calling setVisited(true) means that the room has
	 * been visited; calling setVisited(false) restores its initial unvisited
	 * state.
	 * 
	 * @usage room.setVisited(flag);
	 * @param flag
	 *            The new state of the "visited" flag
	 */
	public void setVisited(boolean flag) {
		beenVisited = flag; // Replace with your code
	}

	/* Method: hasBeenVisited() */
	/**
	 * Returns true if the room has previously been visited.
	 * 
	 * @usage if (room.hasBeenVisited()) . . .
	 * @return true if the room has been visited; false otherwise
	 */
	public boolean hasBeenVisited() {
		return beenVisited; // Replace with your code
	}

	/* Method: getMotionTable() */
	/**
	 * Returns the motion table associated with this room, which is an array of
	 * directions, room numbers, and enabling objects stored in a
	 * AdvMotionTableEntry.
	 * 
	 * @usage AdvMotionTableEntry[] motionTable = room.getMotionTable();
	 * @return The array of motion table entries associated with this room
	 */
	public AdvMotionTableEntry[] getMotionTable() {
		return motionTable; // Replace with your code
	}

	/* Method: readFromFile(rd) */
	/**
	 * Reads the data for this room from the Scanner scan, which must have been
	 * opened by the caller. This method returns a room if the room
	 * initialization is successful; if there are no more rooms to read,
	 * readFromFile returns null.
	 * 
	 * @usage AdvRoom room = AdvRoom.readFromFile(scan);
	 * @param scan
	 *            A scanner open on the rooms data file
	 * @return a room if successfully read; null if at end of file
	 */
	public static AdvRoom readFromFile(Scanner scan) {
		if (!scan.hasNext()) {
			return null;
		}
		
		AdvRoom room = new AdvRoom();
		room.roomNumber = scan.nextInt();
		scan.nextLine();
		room.name = scan.nextLine();
		
		List<String> newDesc = new ArrayList<String>();
		String line;
		while (!(line = scan.nextLine()).equals("-----")) {
			newDesc.add(line);
		}
		room.desc = newDesc.toArray(new String[newDesc.size()]);
		
		room.beenVisited = false;
		
		List<AdvMotionTableEntry> motionTable = new ArrayList<AdvMotionTableEntry>();
		while ((scan.hasNext()) && !(line = scan.nextLine()).equals("")) {
			
			String[] lineSplit = line.split(" ");
			
			String[] secondSplit = lineSplit[lineSplit.length - 1].split("/");
			
			String name = lineSplit[0];
			int roomNum = Integer.parseInt(secondSplit[0]);
			
			String key;
			try {
				key = secondSplit[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				key = null;
			}
			
			AdvMotionTableEntry entry = new AdvMotionTableEntry(name, roomNum, key);
			motionTable.add(entry);
		}
		room.motionTable = motionTable.toArray(new AdvMotionTableEntry[motionTable.size()]);
		
		
		return room; 
	}

	
	public void look() {
		for (String line: getDescription()) {
			System.out.println(line);
		}
		if (getObjectCount() > 0) {
			
			for (AdvObject obj: objectList) {
				System.out.println("I see " + obj.getDescription());
			}
		}
	
	}
	
	public String toString() {
		return String.format("roomName: %s, roomNumber: %d", getName(), getRoomNumber());
	}
	
	/* Private instance variables */
	private String name;
	private String[] desc;
	private int roomNumber;
	private boolean beenVisited;
	private AdvMotionTableEntry[] motionTable;
	private List<AdvObject> objectList = new ArrayList<AdvObject>();
}
