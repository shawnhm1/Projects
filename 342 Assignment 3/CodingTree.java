/*
 * Shawn Massoud 
 * Novemner 19, 2015
 * Home Work 3
 */

import java.util.*;
 
public class CodingTree {  
 
    public static MyPriorityQueue<Node> myQueue;
    public static HashMap<Character, String> codes;
    
    
    public CodingTree(String text) 
    {
        
        HashMap<Character, Integer> dict = new HashMap<Character, Integer>();
 
        for(int i = 0; i < text.length(); i++) 
        {
            char a = text.charAt(i);
 
            if(dict.containsKey(a))
                dict.put(a, dict.get(a)+1);
            else
                dict.put(a, 1);
        }
        
        myQueue = new MyPriorityQueue<Node>(1000);
        int n = 0;
 
        for(Character c : dict.keySet()) 
        {
        	Node newNode = new Node(c, dict.get(c));
        	newNode.isChar = true;
        	myQueue.enqueue(newNode);
            n++;
        }
 
        Node root = huffman(n);
 
        buildTable(root);
    }
 
    public static Node huffman(int n) 
    {
        Node x, y;
 
        for(int i = 1; i <= n-1; i++) 
        {
            Node z = new Node();
            z.left = x = myQueue.dequeue();
            z.right = y = myQueue.dequeue();
            z.freq = x.freq + y.freq;
            myQueue.enqueue(z);
        }
 
        return myQueue.dequeue();
    }
 
    public static void buildTable(Node root) 
    {
        codes = new HashMap<Character, String>();

        postorder(root, new String());
    }
 
    public static void postorder(Node n, String s) 
    {
        if(n == null)
        {
            return;
        }
        
        postorder(n.left, s+"0");
        postorder(n.right, s+"1");
 
        if(n.isChar) 
        {
            System.out.println("\'" + n.alpha + "\' -> " + s);
            codes.put(n.alpha, s);
        }
    }
 
    public static String compress(String s) 
    {
        String c = new String();
 
        for(int i = 0; i < s.length(); i++)
            c = c + codes.get(s.charAt(i));
 
        return c;
    }
 
}
 
class Node implements Comparable<Object>
{
 
    public char alpha;
    public int freq;
    public Node left, right;
    public boolean isChar;
 
    public Node(char a, int f) 
    {
        alpha = a;
        freq = f;
    }
 
    public Node() 
    {
 
    }
 
    public String toString() 
    {
        return alpha + " " + freq;
    }

	@Override
	public int compareTo(Object o) 
	{
		int freqA = this.freq;
        int freqB = ((Node)o).freq;
 
        return freqA-freqB;
	}
 
}