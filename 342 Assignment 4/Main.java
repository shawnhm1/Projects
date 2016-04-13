
import java.io.*;
import java.util.*;

/*
 * Shawn Massoud * Novemner 25, 2015
 * Home Work 4
 * 
 * This program works with a smaller text file but has trouble with a large file.
 */

public class Main {
	
	private static final long ASCII_CODE = 8;
	private static final int CHAR_LIMIT = 256;

	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		
		long duration = 0;
		
		FileReader inputStream = null;
		String fileName = "WarAndPeace.txt";
		FileOutputStream outputStream = new FileOutputStream(new File("compressed.txt"));
		StringBuilder compressedSB = new StringBuilder();
		FileOutputStream codeStream = new FileOutputStream(new File("codes.txt"));
		PrintWriter decom = new PrintWriter(new File("decompressed.txt"));
		try {
				inputStream = new FileReader(fileName);
				int c;
				StringBuffer str = new StringBuffer();
				
				while ((c = inputStream.read()) != -1) {
					str.append((char)c);
				}
				inputStream.close();

				CodingTree ct = new CodingTree(new String(str));
				
				
				codeStream.write(ct.myCodes.toString().getBytes());
				codeStream.close();
				HashTable<String, String> ht = ct.myCodes;
				ht.stats();
				
				StringBuffer codeBuffer = new StringBuffer();
				StringBuffer wordBuffer = new StringBuffer();
				long asciiCost = str.length()*ASCII_CODE;
				long compressionCost = 0;
				for(int i = 0; i < str.length(); i++){
					Character ch = str.charAt(i);
					if((ch.compareTo('A') >= 0 && ch.compareTo('Z') <= 0) || (ch.compareTo('a') >= 0 && ch.compareTo('z') <= 0) 
							|| (ch.compareTo('0') >= 0 && ch.compareTo('9') <= 0) || ch.equals('\'') || ch.compareTo('-') == 0) {
						wordBuffer.append(ch);
					}
					else { 
						String codeStr = new String(wordBuffer);
						if(codeStr.length()>0){
							codeBuffer.append(ht.get(codeStr));		
						}
						codeBuffer.append(ht.get(ch.toString()));
						wordBuffer = new StringBuffer();
					}
					if(codeBuffer.length() > CHAR_LIMIT){
						while(codeBuffer.length() > ASCII_CODE){
							int chr = Integer.parseInt(codeBuffer.substring(0, 8),2);
							outputStream.write(chr);
							compressedSB.append(codeBuffer.substring(0, 8));
							codeBuffer.delete(0, 8);
							compressionCost += ASCII_CODE;
						}
					}					
				}
				compressionCost += codeBuffer.length();
				while(codeBuffer.length() > 8){
					int chr = Integer.parseInt(codeBuffer.substring(0, 8),2);
					outputStream.write(chr);
					compressedSB.append(codeBuffer.substring(0, 8));
					codeBuffer.delete(0, 8);
				}
				outputStream.close();
				
			
				decom.print(CodingTree.decompress(compressedSB.toString().toCharArray()));
				decom.close();
				
				duration = System.currentTimeMillis() - start;

				System.out.println("Uncompressed file size: " + asciiCost/ASCII_CODE + " bytes");
				System.out.println("Compressed file size: " + compressionCost/ASCII_CODE + " bytes");
				System.out.println("Compression ratio: " + compressionCost*100/asciiCost + "%");
				System.out.println("Running Time: " + duration + " milliseconds");
		} finally {}
	}
}
