import java.io.*;
import java.util.Random;

public class Generator {

	public static void main(String[] args) {
		if(args.length != 2)
		{
			System.out.println("Invalid number of arguments.");
			System.out.println("Usage: java Generator \"filename\" (number of integer values)");
			System.exit(1);
		}
		
		int numValues = 0;
		String outFileName;
		
		outFileName = args[0];
		
		try 
		{
			numValues = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			System.err.println("Second argument must be an integer.");
			System.exit(1);
		}
		
		Random randomGenerator = new Random();
		
		try {
			
			File file = new File(outFileName);
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			
			for(int i = 0; i < numValues; i++)
			{
				int randomInt = randomGenerator.nextInt();
				output.write(String.valueOf(randomInt));
				output.newLine();
			}
			
			output.close();
		}
		catch(IOException ex)
		{
			System.err.println("Error in creating file, exiting...");
			System.exit(1);
		}
	}

}
