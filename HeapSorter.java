import java.util.Iterator;
import java.util.Random;

public class HeapSorter extends SorterThread {
	
	private boolean success = false;
	private double chanceFail;
	
	private static int[] A;
	private static int[] originalA;
	private static int heapSize;
	private static int left;
	private static int right;
	private static int accessCounter;
	
	public HeapSorter(int[] a, double chancefail)
	{
		this.A = a;
		this.chanceFail = chancefail;
		this.originalA = a;
	}
	
	public void run()
	{
		try {
			accessCounter = 0;

			HeapSort();

			// Before we write out to the file, perform our failure condition here.
			double hazard = (double) accessCounter * chanceFail;
			Random randomGenerator = new Random();
			double randomNumber = randomGenerator.nextDouble();
			double upperBound = 0.5 + hazard;
			if(randomNumber >= 0.5 && randomNumber <= upperBound)
			{
				// Get a failure!
				System.out.println("Failure of primary.");
				A = originalA;
				throw new ThreadDeath();
			}
			
			System.out.println("Success!");
			success = true;
		}
		catch (ThreadDeath td)
		{
			throw new ThreadDeath();
		}
	}
	
	public boolean getSuccess()
	{
		return success;
	}

	public int[] getSortedArray()
	{
		return A;
	}
	
	private static void HeapSort()
	{
		BuildMaxHeap();
		for(int i = heapSize; i > 0; i--)
		{
			exchange(i, 0);
			heapSize = heapSize - 1;
			MaxHeapify(0);
		}
	}
	
	private static void BuildMaxHeap()
	{
		heapSize = A.length - 1;
		for(int i = heapSize/2; i >= 0; i--)
		{
			MaxHeapify(i);
		}
	}
	
	private static void MaxHeapify(int i)
	{
		left = LeftChild(i);
		right = RightChild(i);
		int largest;
		
		if(left <= heapSize && A[left] > A[i])
		{
			accessCounter++;
			largest = left;
		}
		else
		{
			largest = i;
		}
		
		if(right <= heapSize && A[right] > A[largest])
		{
			accessCounter++;
			accessCounter++;
			largest = right;
		}
		
		if(largest != i)
		{
			exchange(i, largest);
			MaxHeapify(largest);
		}
	}
	
	static private void exchange(int i, int j)
	{
		int t = A[i];
		accessCounter++;
		A[i] = A[j];
		accessCounter++;
		accessCounter++;
		A[j] = t;
		accessCounter++;
	}
	
	static private int LeftChild(int i)
	{
		return 2 * i;
	}
	
	static private int RightChild(int i)
	{
		return 2 * i + 1;
	}
}
