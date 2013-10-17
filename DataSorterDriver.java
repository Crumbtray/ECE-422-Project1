import java.util.*;

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
		
		HeapSorter myThread = new HeapSorter(inputFile, outputFile, chancePrimaryFail);
		Timer t = new Timer();
		Watchdog w = new Watchdog(myThread);
		
		t.schedule(w, timeoutPeriod);
		myThread.start();
		boolean primarySuccess = false;
		
		
		try {
			myThread.join();
			t.cancel();
			primarySuccess = myThread.getSuccess();
		}
		catch(InterruptedException e)
		{
			System.out.println("Driver error:");
			e.printStackTrace();
		}
		
		if(primarySuccess)
		{
			System.out.println("Driver: Primary succeeded.");
		}
		else
		{
			System.out.println("Driver: Failure of first sorter, launching second sorter.");
		}
	}

}
