
/**
 * Interface for a simple List object.
 */
public interface ListInterface<T>
{
	/**
	 * Add the new entry to the end of the list.
	 * @param 	newEntry: the entry to be added to the list
	 */
	public void add(T newEntry);
	
	/**
	 * Add the new entry to the list at the specified position.
	 * @param 	position: the index at which to add the new entry
	 * @param 	newEntry: the entry to be added to the list
	 * @throws	IndexOutOfBoundsException if either position < 0 
	 * 			or position > length. 
	 */
	public void add(int position, T newEntry);
	
	/**
	 * Removes the entrh from the specified position in the list.
	 * @param position: the index of the item to be removed.
	 * @return	the entry at the specified index
	 * @throws	IndexOutOfBoundsException if either position < 0 
	 * 			or position > length. 
	 */
	public T remove(int position);
	
	/**
	 * Clears the list of all elements.
	 */
	public void clear();
	
	/**
	 * Replace the element at the specified position with the new entry.
	 * @param position: the index at which to replace the element
	 * @param newEntry: the new element to be inserted at this position.
	 * @return 	the element formerly located at this position
	 * @throws	IndexOutOfBoundsException if either position < 0 
	 * 			or position > length. 
	 */
	public T replace(int position, T newEntry);
	
	/**
	 * Get the entry at the specified position.
	 * @param position: The index we're looking for.
	 * @return	The element located at this position.
	 * @throws	IndexOutOfBoundsException if either position < 0 
	 * 			or position > length.  
	 */
	public T getEntry(int position);
	
	/**
	 * Converts the list to an array
	 * @return	An array whose contents match the list's contents.
	 */
	public T[] toArray();
	
	/**
	 * Determines whether the list contains a particular entry.
	 * @param entry: The entry that we're looking for
	 * @return	True if the list contains the entry; false otherwise
	 */
	public boolean contains(T entry);
	
	/**
	 * Determine the length of the list.
	 * @return	the length of the list.
	 */
	public int getLength();
	
	/**
	 * Check to see if the list is empty.
	 * @return	True if the list is empty; false otherwise.
	 */
	public boolean isEmpty();
	
	
}