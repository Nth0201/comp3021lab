package lab7;

/**
 *
 * COMP 3021
 *
This is a class that prints the maximum value of a given array of 90 elements

This is a single threaded version.

Create a multi-thread version with 3 threads:

one thread finds the max among the cells [0,29]
another thread the max among the cells [30,59]
another thread the max among the cells [60,89]

Compare the results of the three threads and print at console the max value.

 *
 * @author valerio
 *
 */
public class FindMax {
	class MyTask implements Runnable {
		private int largest;
		private int upper;
		private int lower;

		public MyTask(int lower, int upper) {
			this.upper = upper;
			this.lower = lower;
		}


		public int getlargest(){
			return largest;
		}

		@Override
		public void run(){
			largest = findMax(lower,upper);
		}
     }

	// this is an array of 90 elements
	// the max value of this array is 9999
	static int[] array = { 1, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2, 3, 4543,
			234, 3, 454, 1, 2, 3, 1, 9999, 34, 5, 6, 343, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3, 1, 34, 5, 6, 5, 63, 5, 34, 2, 78, 2, 3, 4, 5, 234, 678, 543, 45, 67, 43, 2,
			3, 4543, 234, 3, 454, 1, 2, 3 };

	public static void main(String[] args) {
		new FindMax().printMax();
	}

	public void printMax() {
		int largest;
		// this is a single threaded version
		MyTask task1 = new MyTask(0,29);
		MyTask task2 = new MyTask(30,59);
		MyTask task3 = new MyTask(60,89);
		Thread t1 = new Thread(task1);
		Thread t2 = new Thread(task2);
		Thread t3 = new Thread(task3);
		
		t1.start();
		t2.start();
		t3.start();
		
		try{
			t1.join();
			t2.join();
			t3.join();
			
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		if(task1.getlargest() > task2.getlargest()){
			largest = task1.getlargest();
		} else {
			largest = task2.getlargest();
		}
		if(largest < task3.getlargest() ){
			largest = task3.getlargest();
		}
		//int max = findMax(0, array.length - 1);
		System.out.println("the max value is " + largest);
	}

	/**
	 * returns the max value in the array within a give range [begin,range]
	 *
	 * @param begin
	 * @param end
	 * @return
	 */
	private int findMax(int begin, int end) {
		// you should NOT change this function
		int max = array[begin];
		for (int i = begin + 1; i <= end; i++) {
			if (array[i] > max) {
				max = array[i];
			}
		}
		return max;
	}
}
