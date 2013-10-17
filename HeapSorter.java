import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class HeapSorter extends Thread {
	
	private boolean success = false;
	private String inFile;
	private String outFile;
	private double chanceFail;
	
	private static int[] A;
	private static int heapSize;
	private static int left;
	private static int right;
	private static int accessCounter;
	
	public HeapSorter(String infile, String outfile, double chancefail)
	{
		this.inFile = infile;
		this.outFile = outfile;
		this.chanceFail = chancefail;
	}
	
	public void run()
	{
		accessCounter = 0;


		BufferedReader br = null;
		ArrayList<Integer> theArray = new ArrayList<Integer>();

		try {
			String currentLine;
			br = new BufferedReader(new FileReader(inFile));
			while((currentLine = br.readLine()) != null)
			{
				theArray.add(Integer.parseInt(currentLine));
			}

			br.close();
		}
		catch(IOException e)
		{
			System.err.println("Error in reading input file, exiting...");
			e.printStackTrace();
			this.stop();
		}

		A = convertIntegers(theArray);
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
			throw new ThreadDeath();
		}

		// Write to file.
		try {

			File file = new File(outFile);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));

			for(int i = 0; i < A.length; i++)
			{
				output.write(String.valueOf(A[i]));
				output.newLine();
			}
			output.close();
		}
		catch(IOException ex)
		{
			System.err.println("Error in creating output file, exiting...");
			ex.printStackTrace();
			System.exit(1);
		}
		
		System.out.println("Success!");
		success = true;
	}
	
	public boolean getSuccess()
	{
		return success;
	}
	
	private static int[] convertIntegers(ArrayList<Integer> integers)
	{
		int[] returnArray = new int[integers.size()];
		Iterator<Integer> iterator = integers.iterator();
		for(int i = 0; i < returnArray.length; i++)
		{
			returnArray[i] = iterator.next().intValue();
		}
		return returnArray;
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
