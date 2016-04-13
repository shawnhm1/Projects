import java.io.*;
import java.util.*;


/*
 * Shawn Massoud 
 * Novemner 19, 2015
 * Home Work 3 
 */



public class Main 
{

	public static void main(String[] args) throws IOException 
	{
		long start = System.currentTimeMillis();
		long duration = 0;
		
		FileReader inputFlow = null;
		String fileName = "WarAndPeace.txt";
		FileOutputStream outputFlow = new FileOutputStream(new File("compressed.txt"));
		FileOutputStream codeFlow = new FileOutputStream(new File("codes.txt"));
		
		try 
		{
			inputFlow = new FileReader(fileName);
			int s;
			StringBuilder message = new StringBuilder();

			// Read all the characters from the file into a single string
			while ((s = inputFlow.read()) != -1) 
			{
				message.append((char)s);
			}
			inputFlow.close();

			// pass the single string into the constructor, which will than carry 
			// out all the operations necessary to create the encoding
			
			CodingTree ct = new CodingTree(message.toString());
			
			// Now we are going to write the code files 
			// (character being index and String is the value)
			// ct.codes is a Map<Character, String> member of CodingTree 
			// consisting of one (char, binary-codeword) pair
			
			codeFlow.write(ct.codes.toString().getBytes());
			codeFlow.close();
						
			StringBuffer buffer = new StringBuffer();
			long asciiCost = message.length()*8;
			long compressedCost = 0;
			
			// Here we are going to add bits to buffer (a String) until its 
			// it is larger than 256 bits, which at that point will than 
			// output the bytes one at a time
		
			for(int i = 0; i < message.length(); i++)
			{
				buffer.append(ct.codes.get(message.charAt(i)));
				if(buffer.length() >  256)
				{
					while(buffer.length() > 8)
					{
						int chr = Integer.parseInt(buffer.substring(0, 8),2);
						outputFlow.write(chr);
						buffer.delete(0, 8);
						compressedCost += 8;
					}
				}					
			}
			
			compressedCost += buffer.length();
			
			// Here we are going to output the last block
			// of bits remaining of size less than 256
			while(buffer.length() > 8){
				int chr = Integer.parseInt(buffer.substring(0, 8),2);
				outputFlow.write(chr);
				buffer.delete(0, 8);
			}
			// output last bits
			outputFlow.write(Integer.parseInt(buffer.toString(),2));
			outputFlow.close();
			
			// calculate time of computation
			duration = System.currentTimeMillis() - start;

			// output stats from our program 
			System.out.println("Uncompressed file size: " + asciiCost/8 + " bytes");
			System.out.println("Compressed file size: " + compressedCost/8 + " bytes");
			System.out.println("Compression ratio: " + compressedCost*100/asciiCost + "%");
			System.out.println("Running Time: " + duration + " milliseconds");
		} 
		finally {}
	
	}

}
