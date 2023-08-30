package cmsc433;

import java.util.List;

/**
 * Customers are simulation actors that have two fields: a name, and a list
 * of Food items that constitute the Customer's order. When running, an
 * customer attempts to enter the Ratsie's (only successful if the
 * Ratsie's has a free table), place its order, and then leave the
 * Ratsie's when the order is complete.
 */
public class Customer implements Runnable {
	// JUST ONE SET OF IDEAS ON HOW TO SET THINGS UP...
	private final String name;
	private final List<Food> order;
	private final int orderNum;

	private static int runningCounter = 0;

	private Order order2;

	/**
	 * You can feel free modify this constructor. It must take at
	 * least the name and order but may take other parameters if you
	 * would find adding them useful.
	 */
	public Customer(String name, List<Food> order) {
		this.name = name;
		this.order = order;
		this.orderNum = ++runningCounter;

		this.order2 = new Order(orderNum, this.name, this.order);
	}

	public String toString() {
		return name;
	}

	/**
	 * This method defines what an Customer does: The customer attempts to
	 * enter the Ratsie's (only successful when the Ratsie's has a
	 * free table), place its order, and then leave the Ratsie's
	 * when the order is complete.
	 */
	public void run() {
		// YOUR CODE GOES HERE...
		Simulation.logEvent(SimulationEvent.customerStarting(this));
		try{
			Simulation.sem.acquire();

			Simulation.logEvent(SimulationEvent.customerEnteredRatsies(this));

			synchronized(Simulation.orders){
				Simulation.logEvent(SimulationEvent.customerPlacedOrder(this, order, orderNum));
				Simulation.orders.add(order2);
				Simulation.orders.notifyAll();
			}

			synchronized(order2){
				while (!Simulation.completed.contains(order2)){
					order2.wait();
				}
				Simulation.logEvent(SimulationEvent.customerReceivedOrder(this, order, order2.orderNumber));
			}

			Simulation.logEvent(SimulationEvent.customerLeavingRatsies(this));
		} catch (InterruptedException e){
			e.printStackTrace();
		} finally {
			Simulation.sem.release();
		}



	}
}
