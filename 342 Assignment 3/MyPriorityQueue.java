/*
 * Shawn Massoud 
 * Novemner 19, 2015
 * Home Work 3
 */

public class MyPriorityQueue<T extends Comparable>
{
	Object[] items;
	int mySize;

 	 
	public MyPriorityQueue(int size)
	{
		items = new Object[size];
		int mySize = 0;
	}

	public int getSize()
	{	
		return mySize;	
	}


	public T dequeue()
	{
		if (mySize == 0)
			return null;
		
		T item = (T)items[0];

		mySize--;
		T last = (T)items[mySize];
		items[0] = last;

		int index = 0;
		while (index <= mySize)
		{
			int left = index*2+1;
			int right = left+1;
			if (left >= mySize)
				break;
			T leftItem = (T)items[left];
			T rightItem = null;
			if (right < mySize)
				rightItem = (T)items[right];
			int min;
			T minItem;
			if ((right >= mySize))// ||
			{
				min = left;
				minItem = leftItem;
			}
			else if ((leftItem.compareTo(rightItem) < 0))
			{
				min = left;
				minItem = leftItem;
			}
			else
			{
				min = right;
				minItem = rightItem;
			}
			if (minItem.compareTo(last) >= 0)
			{
				break;
			}
			
			items[index] = items[min];
			items[min] = (Object)last;
			index = min;
		}
		return item;
	}
	
	
	public boolean enqueue(T item)
	{
		if (mySize >= items.length)
			return false;
		
		int index = mySize;
		mySize++;
		
		while (index != 0)
		{
			int parent = (index-1)/2;
			if (item.compareTo((T)items[parent]) >= 0)
				break;
	
			items[index] = items[parent];
			index = parent;
		}
		items[index] = (Object) item;
		return true;
	}
	
	public String toString()
	{
		String s = "";
		int i;
		for (i = 0; i < mySize; i++)
			s += i + ": " + (T)items[i]+", ";
		return s;
	}
}