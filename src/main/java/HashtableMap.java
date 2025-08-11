import java.util.NoSuchElementException;
import java.util.List;
import java.util.LinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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


  /**
   * The putAndGetTest() method tests the put() and get() methods of Hashtable Map. It tests the
   * HashtableMap implementation to ensure that new key-value pairs are added and retrievable,
   * null values are allowed, adding a null key throws a NullPointerException, adding a duplicate key
   * throws an IllegalArgumentException, and getting a non-existent key throws a NoSuchElementException.
   */
  @Test
  public void putAndGetTest(){
    // Create a HashtableMap for testing
    HashtableMap<String, Integer> map = new HashtableMap<>();
    // Insert key-value pairs into the map
    map.put("A", 1);
    map.put("B", 2);
    map.put("C", 3);
    map.put("D", 4);
    map.put("E", null); // Add a non-null key which null value for testing

    // Verify that the correct values are retrieved from each of the keys
   Assertions.assertEquals(1, map.get("A"));
   Assertions.assertEquals(2, map.get("B"));
   Assertions.assertEquals(3, map.get("C"));
   Assertions.assertEquals(4, map.get("D"));
   Assertions.assertNull(map.get("E")); // See if a null value is properly retrieved

    // Test to see that tht put() and get() methods throw the appropriate exceptions
   Assertions.assertThrows(NullPointerException.class, () -> map.put(null, 4));
   Assertions.assertThrows(IllegalArgumentException.class, () -> map.put("A", 8));
   Assertions.assertThrows(NoSuchElementException.class, () -> map.get("F"));
  }

  /**
   * The containsKeyAndGetCapacityTest() method tests the containsKey() and getCapacity() methods. It ensures
   * that the containsKey() methods correctly identifies the keys that are and aren't in the map, and
   * tests that the getCapacity method correctly returns the map's capacity.
   */
  @Test
  public void containsKeyAndGetCapacityTest(){
    // Create a new map for testing
    HashtableMap<String, Integer> map = new HashtableMap<>();
    // Add some key-value pairs into the map
    map.put("A", 1);
    map.put("B", 2);
    map.put("C", 3);
    map.put("D", 4);
    // Check if the keys exist in the map
    Assertions.assertTrue(map.containsKey("A"));
    Assertions.assertTrue(map.containsKey("B"));
    Assertions.assertTrue(map.containsKey("C"));
    Assertions.assertTrue(map.containsKey("D"));
    Assertions.assertFalse(map.containsKey("E")); // Ensure that the keys that don't exist in the map aren't in the map
    // Test the getCapacity method
    Assertions.assertEquals(64, map.getCapacity()); // Test it for the default constructor
    HashtableMap<String, Integer> newMap = new HashtableMap<>(10);
    Assertions.assertEquals(10, newMap.getCapacity()); // Test it for the parameterized constructor
  }

  /**
   * The removeTest() method tests the remove() method and ensures the correct map behavior afterward.
   * It ensures that you can remove an existing key, return the correct value, and decrease the size after removing,
   * checks if the remaining keys are unaffected, checks that the capacity remains the same, and
   * that removing a non-existent key throws a NoSuchElementException.
   *
   */
  @Test
  public void removeTest(){
    // Create a map for testing
    HashtableMap<String, Integer> map = new HashtableMap<>();
    // Add key-value pairs
    map.put("A", 1);
    map.put("B", 2);
    map.put("C", 3);
    map.put("D", 4);
    map.put("E", 5);
    // Remove one key and verify its return value
    Assertions.assertEquals(1, map.remove("A"));
    // Check that the size decremented after removal
    Assertions.assertEquals(4, map.getSize());
    // Check that the key is no longer in the map
    Assertions.assertFalse(map.containsKey("A"));
    // Verify that the remaining keys are intact
    Assertions.assertEquals(2, map.get("B"));
    Assertions.assertEquals(3, map.get("C"));
    Assertions.assertEquals(4, map.get("D"));
    Assertions.assertEquals(5, map.get("E"));
    // Check that the capacity remained the same
    Assertions.assertEquals(64, map.getCapacity());
    // Attempt to remove a non-existent key and see if the appropriate exception is thrown
    Assertions.assertThrows(NoSuchElementException.class, () -> map.remove("F"));
    Assertions.assertThrows(NoSuchElementException.class, () -> map.remove("A")); // Already removed
  }

  /**
   * The clearTest() method tests the clear() method of the map. It ensures that all key value pairs
   * are removed, size becomes 0, containsKey() returns false for all of the previously added keys,
   * and that the capacity remains unchanged.
   */
  @Test
  public void clearTest(){
    // Create a new map for testing
    HashtableMap<String, Integer> map = new HashtableMap<>();
    // Add key-value pairs
    map.put("A", 1);
    map.put("B", 2);
    map.put("C", 3);
    map.put("D", 4);
    map.put("E", 5);
    // Check the size before clearing
    Assertions.assertEquals(5, map.getSize());
    map.clear(); // Clear the map
    // Check the size after clearing
    Assertions.assertEquals(0, map.getSize());
    // Check that the keys were all properly removed from the map
    Assertions.assertFalse(map.containsKey("A"));
    Assertions.assertFalse(map.containsKey("B"));
    Assertions.assertFalse(map.containsKey("C"));
    Assertions.assertFalse(map.containsKey("D"));
    Assertions.assertFalse(map.containsKey("E"));
    // Check that the capacity remained the same
    Assertions.assertEquals(64, map.getCapacity());
  }

  /**
   * The collisionsTest() method tests the handling of collisions with a small capacity. It ensures
   * that multiple keys with the same hash index are stored and retrieved correctly and removal in
   * collision scenarios works as expected.
   */
  @Test
  public void collisionsTest(){
    // Create a new map for testing, have a small capacity to force collisions
    HashtableMap<Integer, String> map = new HashtableMap<>(5);
    // These keys are likely to collide
    map.put(0, "zero");
    map.put(5, "five");
    map.put(10, "ten");
    Assertions.assertEquals(5, map.getCapacity()); // Check that your custom capacity is the same
    // Check that all of your keys return the correct values
    Assertions.assertEquals("zero", map.get(0));
    Assertions.assertEquals("five", map.get(5));
    Assertions.assertEquals("ten", map.get(10));
    Assertions.assertEquals(3, map.getSize());
    // Remove a single key and verify the map's behavior
    Assertions.assertEquals("five", map.remove(5));
    Assertions.assertEquals(2, map.getSize());
    Assertions.assertFalse(map.containsKey(5));
    Assertions.assertTrue(map.containsKey(0));
    Assertions.assertTrue(map.containsKey(10));
  }
}
