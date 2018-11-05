

import java.util.Arrays;

import javax.swing.JOptionPane;

public class ArrayList<T> implements ListInterface<T> 
{
	private int numberOfEntries;
	private T[] data;
	private boolean initialized = false;
	private static final int DEFAULT_CAPACITY = 2500;
	private static final int MAX_CAPACITY = 10000000;
	
	public ArrayList()
	{
		this(DEFAULT_CAPACITY);
	}
	
	public ArrayList(int capacity)
	{
		checkCapacity(capacity);
		
		@SuppressWarnings("unchecked")
		T[] tempList = (T[]) new Object[capacity];
		data = tempList;
		numberOfEntries = 0;
		initialized = true;
	}
	
	
	@Override
	public void add(T newEntry) 
	{
		checkInitialization();
		data[numberOfEntries++] = newEntry;
		ensureCapacity();
	}

	@Override
	public void add(int position, T newEntry) 
	{
		checkInitialization();
		if(position >= 0 && position <= numberOfEntries)
		{
			if( position <= numberOfEntries)
				makeRoom(position);
			data[position] = newEntry;
			numberOfEntries++;
			ensureCapacity();
		}
		else
			throw new IndexOutOfBoundsException("Position of add's new entry is out of bounds.");
	}

	@Override
	public T remove(int position) 
	{
		checkInitialization();
		if( position >= 0 && position < numberOfEntries )
		{
			T returnVal = data[position];
			
			//if the removed element was not the last one, close the gap.
			if(position < numberOfEntries - 1)
				removeGap(position);
			numberOfEntries--;
			return returnVal;
		}
		else
			throw new IndexOutOfBoundsException("Illegal index given to remove operation.");
	}

	@Override
	public void clear() 
	{
		checkInitialization();
		for(int i = 0; i < numberOfEntries; i++)
			data[i] = null;
		numberOfEntries = 0;
	}

	@Override
	public T replace(int position, T newEntry) 
	{
		checkInitialization();
		if( position >= 0 && position < numberOfEntries )
		{
			T returnVal = data[position];
			data[position] = newEntry;
			return returnVal;
		}
		else
			throw new IndexOutOfBoundsException("Illegal index given to replace operation.");
	}

	@Override
	public T getEntry(int position) 
	{
		if(position < 0 || position >= numberOfEntries)
			throw new IndexOutOfBoundsException("Illegal position given to getEntry.");
		checkInitialization();
		return data[position];
	}

	@Override
	public T[] toArray()
	{
		@SuppressWarnings("unchecked")
		T[] tempList = (T[]) new Object[numberOfEntries];
		for(int i = 0; i < numberOfEntries; i++)
		{
			tempList[i] = data[i];
			//System.out.print("Hello");
		}
		return tempList;
	}

	@Override
	public boolean contains(T entry) 
	{
		checkInitialization();
		for(int i = 0; i < numberOfEntries; i++)
		{
			if(data[i].equals(entry))
				return true;
		}
		return false;
	}

	@Override
	public int getLength() 
	{
		return numberOfEntries;
	}

	@Override
	public boolean isEmpty() 
	{
		return numberOfEntries == 0;
	}

	private void checkCapacity(int capacity)
	{
		if(capacity > MAX_CAPACITY)
			throw new IllegalStateException("Attempt to create a list whose " + 
											"capacity exceeds allowed maximum of " + MAX_CAPACITY);
		if(capacity < 1)
			throw new IllegalStateException("Attempt to create a list whose " + 
											"capacity is not a positive integer.");			
	}
	
	/**
	 * Helper function for add()
	 */
	private void ensureCapacity()
	{
		if( numberOfEntries == data.length - 1 )
		{
			int newLength = 2*data.length;
			checkCapacity(newLength);
			data = Arrays.copyOf(data, newLength);
		}
	}
	
	private void checkInitialization()
	{
		if (!initialized)
			throw new SecurityException("ArrayList object is not initialized properly.");
	}
	
	private void makeRoom(int position)
	{
		int newIndex = position;
		int lastIndex = numberOfEntries;
		
		// Start at the end of the list and move each element to the spot one index higher
		// until we free up the spot at index 'position'.
		for(int index = lastIndex; index >= newIndex; index--)
			data[index + 1] = data[index];
	}
	
	private void removeGap(int position)
	{
		int removedIndex = position;
		int lastIndex = numberOfEntries;
		// Start at the position in the list where the gap is and move each element to the spot prior
		// until we've moved everything.
		for(int index = removedIndex; index < lastIndex; index++)
			data[index] = data[index + 1];
	}
	
	
}