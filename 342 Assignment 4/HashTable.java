
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/*
 * Shawn Massoud
 * Novemner 25, 2015
 * Home Work 4
 * 
 * This program works with a smaller text file but has trouble with a large file.
 */



public class HashTable<K, V> {
	private static final int CAPACITY = 32768;
	private int numberBuckets;
	private int elements;
	private List<Integer> myProbes;
	List<Integer> setKey = new ArrayList<Integer>();
	List<Integer> hp;
	int fillEntri;

	//private Li<MyHashEntry<K, V>>[] buckets;
	HashEnter<K,V> [] ht;
	
	public HashTable(){
		this(CAPACITY);
	}
	
	public HashTable(int num) {
		numberBuckets = num;
		ht = new HashEnter[num];
		myProbes = new ArrayList<Integer>();
	}
	
	public int hash(K key) {
			int hashValue = Math.abs(key.hashCode()%CAPACITY);
			return hashValue;
	}
	
	public V get(K theKey){
		int index = myHash(theKey);
		if(ht[index] != null){
			if(ht[index].key.equals(theKey)){
				return ht[index].value;
			}else {
				while(ht[index] != null && !ht[index].key.equals(theKey)){
					index = index+1;			
					if(index > CAPACITY-1){
						index = 0;
					}				
				}
				if(ht[index] != null && ht[index].key.equals(theKey)){
					return ht[index].value;
				}
			}
		}
		return null;
	}
	
	//Associates the specified value with the specified key in this map.
	public void put(K theKey, V theValue){
		int index = myHash(theKey);
		int pp =0;
		if(ht[index] != null){
			while(ht[index] != null && !ht[index].key.equals(theKey)){
				index++;
				pp++;
				if(index >CAPACITY-1){
					index =0;
				}
			}
			if(ht[index] == null){
				fillEntri++;
				myProbes.add(pp);
				ht[index] = new HashEnter<K,V>(theKey,theValue);
			}
			ht[index].value = theValue;
		}
		
		else if (ht[index] == null) {
			ht[index] = new HashEnter<K,V>(theKey,theValue);
			pp++;
			myProbes.add(pp);
			fillEntri++;
		}
		else if (ht[index].key.equals(theKey)){
			ht[index].value = theValue;
		}
		
	}
	public int myHash(K key) {
		int hashValue = Math.abs(key.hashCode()%CAPACITY);
		return hashValue;
	}
	

	public int arr_size(){
		return numberBuckets;
	}
	
	public void stats(){						
		Map<Integer, Integer> orderMap = new TreeMap<Integer, Integer>(
				new Comparator<Integer>() {
	 
				@Override
				public int compare(Integer o1, Integer o2) {
					return o2.compareTo(o1);
				}	 
			});
		for(int i = 0; i < myProbes.size(); i++){
			int num = myProbes.get(i);
			if(orderMap.containsKey(num)){
				orderMap.put(num, orderMap.get(num) +1);
			}else{
				orderMap.put(num, 1);
			}
		}
			setKey.addAll(orderMap.keySet());
		HashMap<Integer, Integer> fullMap = new HashMap<Integer, Integer>();
		int cal = (int) setKey.get(0) +1;
		for(int i = 1; i < cal ; i++){
			fullMap.put(i, 0);
		}
		for(int i = 0; i < setKey.size(); i++){
			if(fullMap.containsKey(setKey.get(i))){
				fullMap.remove(setKey.get(i), 0);
				fullMap.put((Integer) setKey.get(i), orderMap.get(setKey.get(i)));
			}
		}
		hp = new ArrayList();
		for(Entry<Integer, Integer> entry: fullMap.entrySet()){
			hp.add(entry.getValue());
		
		}
		int sumVal = 0;
		for(int i = 0; i < setKey.size(); i++){
			sumVal += (int)setKey.get(i) * (int)orderMap.get(setKey.get(i));
		}
		
			System.out.println("Hash Table Stats");
			System.out.println("================");
			System.out.println("Number of Entries: " + fillEntri);
			System.out.println("Number of Buckets: " + CAPACITY );
			System.out.println("Histogram of Probes: " + hp);
			System.out.println("Fill Percentage: " + (double)fillEntri *100/CAPACITY + "%");
			System.out.println("Max Linear prob:" + setKey.get(0));
			System.out.println("Average Linear prob:" + (double)sumVal/fillEntri);		
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder("[");
		for(int i = 0; i < CAPACITY; i++){
			if(ht[i] != null){
				sb.append("("+ht[i].key +","+ ht[i].value+"), ");			
			}
		}
		sb.deleteCharAt(sb.length()-1);
		sb.deleteCharAt(sb.length()-1);
		sb.append("]");
		return sb.toString();
	}
	
	private class HashEnter<k,v>{	
		private k key;
		private v value;
		public HashEnter(k Key,  v Value){	
			this.key = Key;
			this.value = Value;
		}
	}

	public boolean containsKey(K key) {
		int position = myHash(key);
		int h = 1;
		if(ht[position] != null ) 
			if(ht[position].key.equals(key)){
				return true;	
			}else{
				while(ht[position] != null && !ht[position].key.equals(key)){
					position = position + h*h++;
					//position = position+1;
					if(position > CAPACITY -1){
						position = 0;
					}
				
				}
				if(ht[position] != null && ht[position].key.equals(key)){
					return true;
				}
			}
		return false;		
	}
}
