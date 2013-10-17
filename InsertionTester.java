import java.io.*;
import java.lang.*;

public class InsertionTester {
	public static void main(String[] args)
	{
		System.out.println("Java Library Path: " + System.getProperty("java.library.path"));
		System.loadLibrary("InsertionSort");
		InsertionSort sorter = new InsertionSort();
		int[] buffer = new int[]{0,1};
		sorter.binarySort(buffer);
	}
}
