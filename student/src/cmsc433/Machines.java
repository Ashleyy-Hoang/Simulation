package cmsc433;

import java.util.concurrent.Semaphore;

/**
 * Machines are used to make different kinds of Food. Each Machine type makes
 * just one kind of Food. Each machine type has a count: the set of machines of
 * that type can make that many food items in parallel. If the machines are
 * asked to produce a food item beyond its count, the requester blocks. Each
 * food item takes at least item.cookTime10S seconds to produce. In this
 * simulation, use Thread.sleep(item.cookTime10S) to simulate the actual cooking
 * time.
 */
public class Machines {

	public enum MachineType {
		sodaMachines, fryers, grillPresses, ovens
	};

	// Converts Machines instances into strings based on MachineType.
	public String toString() {
		switch (machineType) {
			case sodaMachines:
				return "Soda Machines";
			case fryers:
				return "Fryers";
			case grillPresses:
				return "Grill Presses";
			case ovens:
				return "Ovens";
			default:
				return "INVALID MACHINE TYPE";
		}
	}

	public final MachineType machineType;
	public final Food machineFoodType;

	// YOUR CODE GOES HERE...
	final int capacity;
	Semaphore sem;


	/**
	 * The constructor takes at least the name of the machines, the Food item they
	 * make, and their count. You may extend it with other arguments, if you wish.
	 * Notice that the constructor currently does nothing with the count; you must
	 * add code to make use of this field (and do whatever initialization etc. you
	 * need).
	 */
	public Machines(MachineType machineType, Food foodIn, int countIn) {
		this.machineType = machineType;
		this.machineFoodType = foodIn;

		// YOUR CODE GOES HERE...
		this.capacity = countIn;
		this.sem = new Semaphore(capacity,true);

		//Simulation.logEvent(SimulationEvent.machinesStarting(this, foodIn, countIn));

	}

	/**
	 * This method is called by a Cook in order to make the Machines' food item. You
	 * can extend this method however you like, e.g., you can have it take extra
	 * parameters or return something other than Object. You will need to implement
	 * some means to notify the calling Cook when the food item is finished.
	 */
	public Thread makeFood() throws InterruptedException {
		// YOUR CODE GOES HERE...

		Thread thread = new Thread(new CookAnItem(machineFoodType, this));
		thread.start();;

		return thread;
	}

	// THIS MIGHT BE A USEFUL METHOD TO HAVE AND USE BUT IS JUST ONE IDEA
	private class CookAnItem implements Runnable {
		Food food;
		Machines machine;

		public CookAnItem(Food food, Machines machine){
			this.food = food;
			this.machine = machine;

		}
		public void run() {
			
			try {
				//YOUR CODE GOES HERE...

				sem.acquire();
				Simulation.logEvent((SimulationEvent.machinesCookingFood(machine, machineFoodType)));

				Thread.sleep(food.cookTime10S);

				Simulation.logEvent(SimulationEvent.machinesDoneFood(machine, machineFoodType));
				sem.release();

			} catch(InterruptedException e) { }
		}
	}
}
