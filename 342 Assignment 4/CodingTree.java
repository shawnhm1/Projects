
import java.util.*;

/*
 * Shawn Massoud * Novemner 25, 2015
 * Home Work 4
 * 
 * This program works with a smaller text file but has trouble with a large file.
 */

public class CodingTree{
	
	public static PriorityQueue<Node> myQueue;
	public static HashTable<String, String> myCodeToWord;
	public static HashTable<String, String> myCodes;
	
	public CodingTree(String text){
		HashMap<String, Integer> dictionary = new HashMap<String, Integer>();
		char[] array = text.toCharArray();
		
		StringBuilder str = new StringBuilder();
		
		for(int i=0; i<array.length;i++){
			char ch = array[i];
			if((ch>= 'a' && ch<= 'z') || (ch>='A' && ch<= 'Z') 
			|| ch=='\'' || ch=='-' || (ch>= '0' && ch<='9')) {
				str.append(ch);
			}else{
				if(str!=null && str.length() > 0){
					String token = str.toString();
					if(dictionary.containsKey(token)){
						dictionary.put(token, dictionary.get(token)+1);
					}else{
						dictionary.put(token, 1);
					}
					str.setLength(0);
				}
				String label = text.substring(i, i+1);
				if(dictionary.containsKey(label)){
					dictionary.put(label, dictionary.get(label)+1);
				}else{
					dictionary.put(label, 1);
				}
			}
		}
		
		myQueue = new PriorityQueue<Node> (100000);
		for(String s: dictionary.keySet()){
			Node newNode = new Node(s, dictionary.get(s));
			newNode.isString = true;
			myQueue.add(newNode);
		}
		Node root = Huffman();
		buildTable(root);
	}
	
	
	public static void buildTable(Node root){
		myCodes = new HashTable<String, String>();
		myCodeToWord = new HashTable<String, String>();
		postorder(root, new String());
	}
	
	public static Node Huffman(){
		Node x, y;
		while(myQueue.size() > 1){
			Node z = new Node();
			z.leftNode = x = myQueue.poll();
			z.rightNode = y = myQueue.poll();
			z.frequency = x.frequency + y.frequency;
			myQueue.add(z);
		}
		return myQueue.poll();
	}
	public static void postorder(Node n, String s){
		if(n == null)
			return;
		postorder(n.leftNode, s+'0');
		postorder(n.rightNode,s+'1');
		if(n.isString){
			System.out.println("\'" + n.alpha + "\'-> " + s);
			myCodes.put(n.alpha, s);
			myCodeToWord.put(s,  n.alpha);
		}
	}
	
	public static String decompress(char[] s){
		StringBuilder temp = new StringBuilder();
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < s.length; i++){
			temp.append(s[i]);
			if(myCodeToWord.containsKey(temp.toString())){
				result.append(myCodeToWord.get(temp.toString()));
				temp = new StringBuilder();				
			}
		}
		return result.toString();
	}
}

class Node implements Comparable{
	
	public Node leftNode;
	public Node rightNode;
	public boolean isString;
	public String alpha;
	public int frequency;
	public Node(String myAlpha, int myFrequency){
		alpha = myAlpha;
		frequency = myFrequency;
	}
	
	public Node(){
	}
	
	public String toString(){
		return alpha + " " + frequency;
	}
	
	public int compareTo(Object o){
		int frequencyA = this.frequency;
		int frequencyB = ((Node)o).frequency;
		return frequencyA - frequencyB;
	}
}







