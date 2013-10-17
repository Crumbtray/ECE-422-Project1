public class InsertionTester {
	public static void main(String[] args)
	{
		System.loadLibrary("InsertionSort");
		InsertionSort sorter = new InsertionSort();
		int[] buffer = new int[]{0,1};
		sorter.binarySort(buffer);
	}
}