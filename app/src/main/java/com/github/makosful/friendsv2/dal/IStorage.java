package com.github.makosful.friendsv2.dal;

import java.util.List;

public interface IStorage<T>
{
    /**
     * Writes a new entry into storage. The type of T is defined by the implementation
     * @param item The item to be stored
     * @return Returns true if the item was stored successfully. Returns false if any error was encountered
     */
    boolean create(T item);

    /**
     * Gets a specific item from this implementation with an ID that matches the one passed in
     * @param id The ID of the item to get
     * @return Returns a T item with the same ID as given. Returns null if no match was found
     */
    T readById(int id);

    /**
     * Gets a list of all items managed by this implementation.
     * @return Gets a list of all T items stored by this implementation
     */
    List<T> readAll();

    /**
     * Updates the item with the same ID as the item passed in.
     * Important - This will override all values with the ones from the new item, including null values
     * @param item The item with the new values
     * @return Return true of the item was updates successfully. Returns false if any error was encountered
     */
    boolean update(T item);

    /**
     * Deletes all items managed by this storage implementation
     * @return Returns true if all items were successfully deleted. Returns false if any error was encountered
     */
    boolean deleteAll();

    /**
     * Deletes a single item from the storage managed by this implementation
     * @param id The ID of the item to delete
     * @return Returns true if the item was deleted. False if any error was encountered
     */
    boolean deleteById(int id);
}
