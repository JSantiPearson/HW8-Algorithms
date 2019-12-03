import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A priority queue class implemented using a min heap. Priorities cannot be
 * negative.
 *
 * @author Jordan Pearson and Thalia Barr-Malec
 * @version Date
 *
 */
public class PriorityQueue {

	protected Map<Integer, Integer> location;
	protected List<Pair<Integer, Integer>> heap;

	private static final int ROOT_INDEX = 0;

	/**
	 * Constructs an empty priority queue
	 */
	public PriorityQueue() {
		heap = new ArrayList<Pair<Integer, Integer>>();
		location = new HashMap<Integer, Integer>();
	}

	// Jordan
	public static void main(String[] args) {
		
		PriorityQueue queue = new PriorityQueue();


		queue.push(30, -3);
		queue.push(1, -5);
		queue.push(4, -7);
		queue.push(8, -1);
		queue.push(3, -9);
		queue.push(40, -2);
		queue.pop();
		queue.pop();
		queue.changePriority(0, -2);
		queue.pop();

		for (int i = 0; i < queue.size(); i++) {
			System.out.print(queue.heap.get(i).priority);
			System.out.print("\t");
			System.out.println(queue.heap.get(i).element);
		}
		System.out.println(queue.location.toString());
		System.out.print(queue.getPriority(-7));


	}


	/**
	 * Insert a new element into the queue with the given priority.
	 *
	 * @param priority priority of element to be inserted
	 * @param element  element to be inserted <br>
	 *                 <br>
	 *                 <b>Preconditions:</b>
	 *                 <ul>
	 *                 <li>The element does not already appear in the priority
	 *                 queue.</li>
	 *                 <li>The priority is non-negative.</li>
	 *                 </ul>
	 *
	 */
	// Thalia
	public void push(int priority, int element) {
		if (priority < 0) { //throw error if negative input
			throw new AssertionError("Error: Priorities cannot be negative.");
		}
		if (isPresent(element)) { //throw error if the element is a duplicate
			throw new AssertionError("Error: Queue cannot have duplicate elements.");
		}
		Pair<Integer, Integer> new_pair = new Pair<>(priority, element);
		heap.add(new_pair); // add new pair to the heap
		location.put(new_pair.element, (heap.size() - 1));  //add new pair to the location map
		int index = heap.size() - 1;
		while(index >= 0) {  //percolate up until done (-1 will be returned)
			index = percolateUp(index);
		}

	}

	/**
	 * Remove the highest priority element <br>
	 * <br>
	 * <b>Preconditions:</b>
	 * <ul>
	 * <li>The priority queue is non-empty.</li>
	 * </ul>
	 *
	 */

	// Jordan
	public void pop() {
		//throw error if queue is empty
		if (isEmpty()){
			throw new AssertionError("Error: The queue cannot be empty.");
		}
		// swap the root pair and tail pair
    
		int tailIndex = heap.size() - 1;
		swap(ROOT_INDEX, tailIndex);

		// remove the tail pair (the old root)
		location.remove(heap.get(tailIndex).element);
		heap.remove(tailIndex);

		// call pushDown to put the new root in the correct position
		pushDown(ROOT_INDEX);
	}

	/**
	 * Returns the highest priority in the queue
	 * 
	 * @return highest priority value <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The priority queue is non-empty.</li>
	 *         </ul>
	 */
	// Jordan
	public int topPriority() {
		if (isEmpty()){
			throw new AssertionError("Error: Queue cannot be empty.");
		}
		// return the root pair's priority
		return heap.get(ROOT_INDEX).priority;
	}

	/**
	 * Returns the element with the highest priority
	 * 
	 * @return element with highest priority <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The priority queue is non-empty.</li>
	 *         </ul>
	 */
	// Jordan
	public int topElement() {
		if (isEmpty()){
			throw new AssertionError("Error: Queue cannot be empty.");
		}

		// return the root pair's element
		return heap.get(ROOT_INDEX).element;
	}

	/**
	 * Change the priority of an element already in the priority queue.
	 *
	 * @param newpriority the new priority
	 * @param element     element whose priority is to be changed <br>
	 *                    <br>
	 *                    <b>Preconditions:</b>
	 *                    <ul>
	 *                    <li>The element exists in the priority queue</li>
	 *                    <li>The new priority is non-negative</li>
	 *                    </ul>
	 */
	// Thalia
	public void changePriority(int newpriority, int element) {

		if (!isPresent(element)) { //if the element is not in queue, throw exception
			throw new AssertionError("Error: Element is missing.");
			}

			if (isEmpty()){ //if queue is empty, throw exception
				throw new AssertionError("Error: Queue cannot be empty.");
			}

		int index = location.get(element);
		heap.get(index).priority = newpriority;

		while (index > 0) { //percolate up until finished (-1 returned), if element needs to go down, -1 will return immediately
			index = percolateUp(index);
		}
		pushDown(index); //push down

	}

	/**
	 * Gets the priority of the element
	 *
	 * @param element the element whose priority is returned
	 * @return the priority value <br>
	 *         <br>
	 *         <b>Preconditions:</b>
	 *         <ul>
	 *         <li>The element exists in the priority queue</li>
	 *         </ul>
	 */
	// Thalia

	public int getPriority(int element) {

		if (!isPresent(element)) { //if element is not in the queue, throw error
			throw new AssertionError("Error: Element is missing.");
		}

		int index = location.get(element);
		return heap.get(index).priority;
	}

	/**
	 * Returns true if the priority queue contains no elements
	 * 
	 * @return true if the queue contains no elements, false otherwise
	 */
	// Jordan
	public boolean isEmpty() {
		// If the heap size is 0, it has no elements. Return true.
		if (heap.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns true if the element exists in the priority queue.
	 * 
	 * @return true if the element exists, false otherwise
	 */
	// Thalia
	public boolean isPresent(int element) {

		return location.containsKey(element);

	}

	/**
	 * Removes all elements from the priority queue
	 */
	// Thalia
	public void clear() {

		heap.clear();
		location.clear();

	}

	/**
	 * Returns the number of elements in the priority queue
	 * 
	 * @return number of elements in the priority queue
	 */
	// Jordan
	public int size() {
		// returns the heap size, which is the number of elements in the queue
		return heap.size();
	}

	/*********************************************************
	 * Private helper methods
	 *********************************************************/

	/**
	 * Push down the element at the given position in the heap
	 * 
	 * @param start_index the index of the element to be pushed down
	 * @return the index in the list where the element is finally stored
	 */
	// Jordan
	private int pushDown(int start_index) {
		//calculate start index priority, left child index, right child index.
		int startPriority = heap.get(start_index).priority;
		int left = left(start_index);
		int right = right(start_index);
		int child;

		//-1 is returned by left() and right() if the index does not exist.
		//If neither child exists, the start_index is in the correct location within the heap.
		if (left == -1 && right == -1) {
			return start_index;
		//if the left child does not exist, then the right child is highest priority.
		} else if (left == -1) {
			child = right;
		//if the right child does not exist, then the left child is highest priority.
		} else if (right == -1) {
			child = left;
		//if the right child is higher priority than left, assign right as child.
		} else if (heap.get(left).priority >= heap.get(right).priority) {
			child = right;
		//if all of the above is untrue, then the left child is highest priority.
		} else {
			child = left;
		}
		//If the highest priority child is higher priority than the start_index, swap them.
		if (startPriority > heap.get(child).priority) {
			swap(start_index, child);
			pushDown(child);
		//If not, it is in the correct spot and the start_index is returned.
		} else {
			return start_index;
		}
		return -1;
	}

	/**
	 * Percolate up the element at the given position in the heap
	 * 
	 * @param start_index the index of the element to be percolated up
	 * @return the index in the list where the element is finally stored
	 */
	// Thalia
	private int percolateUp(int start_index) {
		if (start_index == 0) {
			return -1;
		}

		int parentIndex = parent(start_index); //get parent index
		if (heap.get(start_index).priority < heap.get(parentIndex).priority) { //if the element is less than its parent, swap
			swap(start_index, parentIndex);
			return parentIndex; //return parent index
		}
		return -1; // if not then element does not need to go up, return -1 to indicate finished
	}

	/**
	 * Swaps two elements in the priority queue by updating BOTH the list
	 * representing the heap AND the map
	 * 
	 * @param i The index of the element to be swapped
	 * @param j The index of the element to be swapped
	 */
	// Thalia
	private void swap(int i, int j) {
		//swap elements
		Pair temp = heap.get(i);
		heap.set(i, heap.get(j));
		heap.set(j, temp);
		//update location
		location.put(heap.get(i).element, i);
		location.put(heap.get(j).element, j);
	}

	/**
	 * Computes the index of the element's left child
	 * 
	 * @param parent index of element in list
	 * @return index of element's left child in list
	 */
	// Jordan
	private int left(int parent) {
		// Calculate the index of the left child with the following formula
		int leftChildIndex = 2 * parent + 1;
		try {
			heap.get(leftChildIndex);
			return leftChildIndex;
		// If the index is out of bounds, then that means the (would be) index of the
		// left child does not exist. Return -1 for the pushDown method.
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Computes the index of the element's right child
	 * 
	 * @param parent index of element in list
	 * @return index of element's right child in list
	 */
	// Jordan
	private int right(int parent) {
		// Calculate the index of the right child with the following formula
		int rightChildIndex = 2 * parent + 2;
		try {
			heap.get(rightChildIndex);
			return rightChildIndex;
		// If the index is out of bounds, then that means the (would be) index of the
		// left child does not exist. Return -1 for the pushDown method.
		} catch (IndexOutOfBoundsException e) {
			return -1;
		}
	}

	/**
	 * Computes the index of the element's parent
	 * 
	 * @param child index of element in list
	 * @return index of element's parent in list
	 */
	// Jordan
	private int parent(int child) {
		// Calculate the index of the parent with the following formula
		int parentIndex = (int) Math.floor((child - 1) / 2);
		try {
			return parentIndex;
		} catch (AssertionError e) {
			throw new AssertionError("This Pair does not have a parent!");
		}
	}

	/*********************************************************
	 * These are optional private methods that may be useful
	 *********************************************************/

	/**
	 * Push down the root element
	 * 
	 * @return the index in the list where the element is finally stored
	 */
	private int pushDownRoot() {
		// TODO: A one-line function that calls pushDown()
		return 0;
	}

	/**
	 * Percolate up the last leaf in the heap, i.e. the most recently added element
	 * which is stored in the last slot in the list
	 *
	 * @return the index in the list where the element is finally stored
	 */
	private int percolateUpLeaf() {
		// TODO: A one-line function that calls percolateUp()
		return 0;
	}

	/**
	 * Returns true if element is a leaf in the heap
	 * 
	 * @param i index of element in heap
	 * @return true if element is a leaf
	 */
	private boolean isLeaf(int i) {
		// TODO: Fill in
		return false;
	}

	/**
	 * Returns true if element has two children in the heap
	 * 
	 * @param i index of element in the heap
	 * @return true if element in heap has two children
	 */
	private boolean hasTwoChildren(int i) {
		// TODO: Fill in
		return false;
	}

	/**
	 * Print the underlying list representation
	 */
	private void printHeap() {
		for (int i = 0; i < heap.size(); i++) {
			System.out.print(heap.get(i).priority);
			System.out.print("\t");
			System.out.println(heap.get(i).element);
		}
		System.out.println(location.toString());
	}

	/**
	 * Print the entries in the location map
	 */
	private void printMap() {
		// TODO: Fill in
	}

}