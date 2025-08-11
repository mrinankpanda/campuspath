public class HashtableMapTests {
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