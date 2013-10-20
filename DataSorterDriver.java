import java.util.*;
import java.io.*;

public class DataSorterDriver {

	public static void main(String[] args) {
		if(args.length != 5)
		{
			System.out.println("Invalid number of arguments.");
			System.exit(1);
		}
		
		String inputFile = args[0];
		String outputFile = args[1];
		double chancePrimaryFail = 0;
		double chanceBackupFail = 0;
		long timeoutPeriod = 0;
		int[] a;
		int[] sortedA = null;
		
		try {
			chancePrimaryFail = Double.parseDouble(args[2]);
			chanceBackupFail = Double.parseDouble(args[3]);
			timeoutPeriod = Long.parseLong(args[4]);
		}
		catch(NumberFormatException e)
		{
			System.err.println("Invalid argument ----");
			e.printStackTrace();
			System.exit(1);
		}


		// Try to get the input.
		a = ReadFile(inputFile);
		
		//Execute Primary
		HeapSorter heapSorter = new HeapSorter(a, chancePrimaryFail);
		sortedA = SortExecution(heapSorter, timeoutPeriod);

		if(sortedA != null && acceptanceTest(sortedA))
		{
			System.out.println("Driver: Primary Succeeded.");
			WriteFile(outputFile, sortedA);
			System.exit(1);
		}
		else {
			//Raise local exception
			System.out.println("LOCAL EXCEPTION:");
			System.out.println("Primary Failed.");
			

			// While backups available, rollback to computation start
			a = ReadFile(inputFile);
			
			// Execute backup
			System.loadLibrary("InsertionSorter");
			InsertionSorter insertionSorter = new InsertionSorter(a, chanceBackupFail);
			sortedA = SortExecution(insertionSorter, timeoutPeriod);

			if(sortedA != null && acceptanceTest(sortedA))
			{
				System.out.println("Driver: Secondary succeeded.");
				WriteFile(outputFile, sortedA);
				System.exit(1);
			}
		}
		
		// Raise Failure Exception.
		System.out.println("FAILURE EXCEPTION.");
		System.out.println("Driver: primary and secondary have failed.");
		System.exit(1);
	}

	public static int[] SortExecution(SorterThread thread, long timeoutPeriod)
	{
		int[] result = null;
		Timer t = new Timer();
		Watchdog w = new Watchdog(thread);
		t.schedule(w, timeoutPeriod);
		thread.start();
		boolean threadSuccess = false;
		
		try {
			thread.join();
			t.cancel();
			threadSuccess = thread.getSuccess();
			if(threadSuccess)
			{			
				result = thread.getSortedArray();
			}
		}
		catch(InterruptedException e)
		{
			System.out.println("Driver error:");
			e.printStackTrace();
			System.exit(1);
		}

		return result;
	}

	public static int[] ReadFile(String inFile)
	{
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
			System.exit(1);
		}

		int[] a = convertIntegers(theArray);
		return a;
	}


	public static void WriteFile(String outFile, int[] a)
	{
		// Write to file.
		try {

			File file = new File(outFile);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));

			for(int i = 0; i < a.length; i++)
			{
				output.write(String.valueOf(a[i]));
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

	private static boolean acceptanceTest(int[] a)
	{
		for(int i = 0; i < a.length - 1; i++)
		{	
			if(a[i] > a[i+1])
			{
				System.out.println("Acceptance test failed.");
				System.out.println(a[i] + " should be less than " + a[i+1]);
				return false;
			}
		}
		return true;
	}
}
