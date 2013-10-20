import java.util.Iterator;
import java.util.Random;

public class InsertionSorter extends SorterThread{
	
	private boolean success = false;
	private static double ChanceFail;
	private static int[] A;
	
	public InsertionSorter(int[] a, double chancefail)
	{
		this.A = a;
		this.ChanceFail = chancefail;
	}

	public void run()
	{
		try{
			System.loadLibrary("InsertionSort");
			int result = this.binarySort(A, ChanceFail);
			if(result == 1)
			{
				success = true;
			}
		}
		catch(ThreadDeath td)
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

	public native int binarySort(int[] buf, double failureChance);
}
