/**
 * Simple HashTable class.
 *
 * @author CS240 Instructors and RYAN SETZER
 */
public class HashTable<K, V> {

  /**
   * Item class is a simple wrapper for key/value pairs.
   */
  public class Item<K, V> { // leave this non-private for testing.

    private final K key;
    private V value;
    private boolean tombstone;

    /**
     * Create an Item object.
     */
    public Item(K key, V value) {
      this.key = key;
      this.value = value;
      this.tombstone = false;
    }

    /* Getters and setters */
    public K key() {
      return key;
    }

    public V value() {
      return value;
    }

    public void setValue(V value) {
      this.value = value;
    }

    public boolean isDeleted() {
      return tombstone;
    }

    public void delete() {
      tombstone = true;
    }
  }

  public static final int INITIAL_CAPACITY = 16; // must be a power of 2.
  public static final double MAX_LOAD = .5;

  public Item<K, V>[] table; // (Leave this non-private for testing.)
  private int size;

  /**
   * HashTable constructor.
   */
  @SuppressWarnings("unchecked")
  public HashTable() {
    table = (Item[]) new Item[INITIAL_CAPACITY];
    size = 0;
  }


  /**
   * Store the provided key/value pair.
   */
  public void put(K key, V value) {

    // UNFINISHED!

    // IMPLEMENTATION ADVICE: All java classes (including K!) have a hashCode method that returns an
    // integer. This could be ANY integer and probably *won't* be a valid index into your table
    // array. The hashCode for the key must be mapped to a valid index. This can be done using the
    // indexFor method included at the bottom of this file.
    //
    // Your code in this method should handle collisions using simple linear probing.

    Item<K, V> item = new Item<>(key, value);

    if (findKey(key) == -1) { // If key is not in table
      if ((double) (size) / table.length >= MAX_LOAD) {
        rehash();
      }
      int index = indexFor(key.hashCode(), table.length - 1);
      while (table[index] != null && !table[index].isDeleted()) {
        index++;
        if (index > table.length) {
          index = 0;
        }
      }
      table[index] = item;
      size++;
    } else {
      table[findKey(key)] = item;
    }
  }

  /**
   * Return the value associated with the provided key, or null if no such value exists.
   */
  public V get(K key) {
    // Notice that much of the logic will be the same for get and remove. I suggest completing the
    // findKey helper method below.
    int index = findKey(key);
    if (index != -1 && !table[index].isDeleted()) {
      return table[index].value();
    }
    return null;

  }

  /**
   * Remove the provided key from the hash table and return the associated value. Returns null if
   * the key is not stored in the table.
   */
  public V remove(K key) {
    int index = findKey(key);
    if (index != -1) {
      table[index].delete();
      size--;
      return table[index].value();
    }
    return null;
  }

  /**
   * Return the number of items stored in the table.
   */
  public int size() {
    return size;
  }

  // PRIVATE HELPER METHODS BELOW THIS POINT----------


  /**
   * Double the size of the hash table and rehash all existing items.
   */
  private void rehash() {
    // UNFINISHED!
    Item[] copyTable = table.clone();
    table = new Item[table.length * 2];
    size = 0;
    for (int i = 0; i < copyTable.length; i++) {
      if (copyTable[i] != null && !copyTable[i].isDeleted()) {
        put((K) copyTable[i].key(), (V) copyTable[i].value());
      }
    }
  }


  /**
   * Find the index of a key or return -1 if it can't be found. If removal is implemented, this will
   * skip over tombstone positions during the search.
   */
  private int findKey(K key) {
    // UNFINISHED -- THIS METHOD SHOULD BE HELPFUL FOR BOTH GET AND REMOVE.
    for (int i = 0; i < table.length; i++) {
      if (table[i] != null && table[i].key().equals(key) && !table[i].isDeleted()) {
        return i;
      }
    }
    return -1;
  }


  /**
   * Returns index for hash code h.
   */
  private int indexFor(int h, int length) {
    return hash(h) & (length - 1);
  }

  /**
   * Applies a supplemental hash function to a given hashCode, which defends against poor quality
   * hash functions. This is critical because HashMap uses power-of-two length hash tables, that
   * otherwise encounter collisions for hashCodes that do not differ in lower bits.
   *
   * <p>YOU SHOULD NOT CALL THIS METHOD DIRECTLY. IT IS A HELPER FOR THE indexFor METHOD ABOVE.
   */
  private int hash(int h) {
    // This function ensures that hashCodes that differ only by
    // constant multiples at each bit position have a bounded
    // number of collisions (approximately 8 at default load factor).
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
  }

  public void printAll() {
    for (int i = 0; i < table.length; i++) {
      try {
        System.out.println("table[" + i + "]: " + table[i].key());
      } catch (Exception e) {
        System.out.println(i);
      }
    }
  }


}


// The hash and indexFor methods are taken directly from the Java HashMap
// implementation with some modifications. That code is licensed as follows:
/*
 * Copyright 1997-2007 Sun Microsystems, Inc. All Rights Reserved. DO NOT ALTER OR REMOVE COPYRIGHT
 * NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License version 2 only, as published by the Free Software Foundation. Sun
 * designates this particular file as subject to the "Classpath" exception as provided by Sun in the
 * LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version 2 along with this work;
 * if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara, CA 95054 USA or visit
 * www.sun.com if you need additional information or have any questions.
 */


