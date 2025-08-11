import java.util.NoSuchElementException;
import java.util.List;
import java.util.LinkedList;

public class HashtableMap <KeyType, ValueType> implements MapADT <KeyType, ValueType> {


  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }

  protected LinkedList<Pair>[] table; // Stores the hash table array of linked lists
  protected int size; // Stores the number of key-value pairs

  /**
   * This constructs a Hashtable Map with a specified initial capacity. The constructor initializes
   * the internal table array to the given capacity, and fills each index with an empty LinkedList
   * to handle the collisions via chaining.
   * @param capacity the number of buckets in the hash table
   */
  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    // Java doesn't allow direct generic array creation, so we suppress the warning
    // and use an unchecked cast
    table = (LinkedList<Pair>[]) new LinkedList[capacity];
    // Start with zero entries in the table
    size = 0;
    // Initialize each of the array indices with a new LinkedList for chaining
    for (int i = 0; i < capacity; i++) {
      table[i] = new LinkedList<>();
    }
  }

  /**
   * Constructs a HashtableMap with a default capacity of 64. It is a shorthand for the other
   * constructor.
   */
  public HashtableMap() {
    this(64);
  }


  /**
   * The generateHashIndex method is a private helper method which generates a hash index for the given
   * key to determine its position in the hash table.
   * @param key the key for which the hash index is to be generated for
   * @return the computed index within the bounds of the hash table
   */
  private int generateHashIndex(KeyType key) {
    // Get the hash code of the key and mod it with the table length to ensure the index is within
    // the bounds of the array
    return (Math.abs(key.hashCode()) % table.length);
  }

  /**
   * The getPair method is a private helper method which searches for and returns the Pair object
   * that is associated with the specified key from a linked list of pairs. This is primarily used
   * for handling collisions within a hash table
   * @param pair the linked list of Pair objects to search through the
   * @param key the key to search for
   * @return the Pair object containing the specified key, or null if it isn't found
   */
  private Pair getPair(LinkedList <Pair> pair, KeyType key) {
    // Iterate through each Pair in the linked list
    for (Pair p : pair) {
      // If the key in the current pair matches our target, we return
      if (p.key.equals(key)) {
        return p;
      }
    }
    // If there is no matching key, return null
    return null;
  }

  /**
   * The rehashTable method is a private helper method which doubles the size of the hash table and
   * reinserts all existing key-value pairs to maintain the hash table's performance. This method is
   * called when the load factor exceeds our threshold of 0.80.
   */

  @SuppressWarnings("unchecked")
  private void rehashTable() {
    // Save a reference to our current table
    LinkedList<Pair>[] oldTable = table;
    // Creates a new table with it being double the size of the old one
    LinkedList<Pair>[] newTable = (LinkedList<Pair>[]) new LinkedList[oldTable.length * 2];
    // Initialize each linked list in the new table
    for (int i = 0; i < newTable.length; i++) {
      newTable[i] = new LinkedList<>();
    }
    // Replace our old table with the new one
    table = newTable;
    // Reset our size to 0, but it will be re-updated as we re-insert the pairs into the newly sized list
    size = 0;
    // Re-insert the key-value pairs into the new table using the put() method, which will
    // compute the new indices based on the resized table
    for (LinkedList <Pair> pair : oldTable) {
      for (Pair p : pair) {
        put(p.key, p.value);
      }
    }
  }

  /**
   * Adds a new key,value pair/mapping to this collection.It is ok that the value is null.
   *
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    // If the key is null, throw the appropriate exception
    if (key == null) {
      throw new NullPointerException("Key cannot be null");
    }
    // Generate the hash index for the given key
    int hashIndex = generateHashIndex(key);
    // Get the linked list at the computed hash index
    LinkedList<Pair> list = table[hashIndex];
    // Check if the key already exists in the linked list
    if (containsKey(key)) {
      // Throw the appropriate exception if the key exists
      throw new IllegalArgumentException("Key already exists within the map");
    }
    // If the key doesn't exist, add the new Pair with the key and value
    list.add(new Pair(key, value));
    size++; // Increment the number of key-value pairs in the table
    // If the load exceeds 0.8, rehash the table to maintain the hashtable's performance
    if ((double) size / table.length >= 0.8) {
      rehashTable();
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   *
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(KeyType key) {
    // If the key is null, return false (null keys aren't stored into the hashtable)
    if (key == null) {
      return false;
    }
    // Generate the hash index for the given key
    int index = generateHashIndex(key);
    // Get the linked list associated at that specific index
    LinkedList<Pair> list = table[index];
    // Use the getPair() method to check if a Pair with the given key exists in the list, and if found
    // return true; otherwise, return false
    return getPair(list, key) != null;
  }

  /**
   * Retrieves the specific value that a key maps to.
   *
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    if (key == null) {
      // Throw the appropriate exception if the key is null
      throw new NoSuchElementException("Key cannot be null");
    }
    // Generate the hash index for the given key
    int index = generateHashIndex(key);
    // Get the linked list at that index
    LinkedList<Pair> list = table[index];
    // Try to find the pair with the matching key in the list
    Pair pair = getPair(list, key);
    // If the key-value pair with the matching key in the list
    if (pair != null) {
      return pair.value;
    } else {
      // If the pair is not found, throw the appropriate exception
      throw new NoSuchElementException("Key does not exist in the map");
    }
  }

  /**
   * Remove the mapping for a key from this collection.
   *
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    // Throw the appropriate exception if the key is null (null keys aren't included in the hashtable)
    if (key == null) {
      throw new NoSuchElementException("Key cannot be null");
    }
    // Generate the hash index for the specific key
    int index = generateHashIndex(key);
    // Get the linked list at the specific hash index
    LinkedList<Pair> list = table[index];
    // Iterate through the list to find the key-value pair with the matching key
    for (int i = 0; i < list.size(); i++) {
      Pair pair = list.get(i);
      if (pair.key.equals(key)) {
        // If found, remove the pair from the list
        list.remove(i);
        size--; // Decrement the size of the map
        return pair.value; // Return the value of the removed pair
      }
    }
    // If the key wasn't found, throw the appropriate exception
    throw new NoSuchElementException("Key does not exist in the map");
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    // Iterate through each of the linked-lists in the table and clear its contents
    for (LinkedList<Pair> list : table) {
      list.clear(); // Remove all key-value pairs from the list
    }
    // Reset the size of the map to zero
    size = 0;
  }

  /**
   * Retrieves the number of keys stored in this collection.
   *
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   *
   * @return the size of the underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return table.length;
  }

  /**
   * Retrieves this collection's keys.
   * @return a list of keys in the underlying array for this collection
   */
   @Override
   public List<KeyType> getKeys() {
     // Create a new LinkedList to store the keys
     LinkedList<KeyType> keysList = new LinkedList<>();
     for (int i = 0; i < table.length; i++) {
	if (table[i] != null) {
	   for (int j = 0; j < table[i].size(); j++) {
		keysList.add(table[i].get(j).key);
	   }
	}
     }
     return keysList;
   }
}
