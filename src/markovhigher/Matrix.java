/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package markovhigher;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Marta Albecka, Pawel Karpinski, Piotr Lecki
 */

/**
 *  * Base class for markov chain prediction.
 */
public class Matrix {
    
    private static Matrix instance;
    private static List<Note> prev = new ArrayList<>();
    private static Note current;
    /**
     * base structure in Matrix - holds all the diffrent elements that occurred in entry set
     *
     * @see Element
     */
    private List<Element> elementList = new ArrayList<>();

    /**
     * number of previous elements which will be taken into account during prediction, also size of prev
     */
    private int memory;
    /**
     * Private constructor to prevent making more than one instance of Matrix.
     *
     * @see Matrix#create(int) Use this method instead of constructor.
     * @see Matrix#memory
     */
    private Matrix(int memory) {
        this.memory = memory;
        clearPrev();
    }

    /**
     * Creates an instance of Matrix if it doesn't already exist - only one instance can be made.
     *
     * @return instance od Matrix
     * @see Matrix#memory
     */
    public static Matrix create(int memory) {
        if (instance != null)
            throw new IllegalAccessError("Matrix instance already exists");

        instance = new Matrix(memory);
        return instance;
    }

    /**
     * Clears prev list and adds memory amount of null elements to it. Sets a current element to null too.
     *
     * @see Matrix#memory
     * @see Matrix#updateWith(List)
     */
    public void clearPrev() {
        prev.clear();
        for (int i = 0; i < memory; i++) {
            prev.add(null);
        }
        current = null;
    }

    /**
     * Updates current element and prev list with value and increments the counter that it corresponds to.
     * Used during creation of matrix.
     *
     * @param value next value in starting vector
     */
    public void add(Note value) {
        updateWith(value);
        incrementCounter();
    }

    /**
     * @return element which has the same prev as current prev list
     */
    public Element getElement() {
        for (Element el : elementList) {
            if (compareLists(el.getPrev(), prev)) {
                return el;
            }
        }
        return null;
    }

    /**
     * Compares two lists of Notes
     * @param list1 first list to compare
     * @param list2 second list to compare
     * @return true if the lists are identical, false if not.
     */
    public boolean compareLists(List<Note> list1, List<Note> list2)
    {
        if(list1.size() != list2.size())
            throw new IllegalStateException("Prev list has wrong number of values - should be MEMORY");

        for(int i=0; i<list1.size(); i++) {
            if (!(list1.get(i) == null && list2.get(i) == null)) {
                if (list1.get(i) == null || list2.get(i) == null)
                    return false;
                else if (list1.get(i).compareTo(list2.get(i)) != 0)
                    return false;
            }
        }
        return true;
    }

    /**
     * Increments counter which corresponds to values in prev list and current element.
     * If it doesn't exist yet, it creates new counter.
     */
    private void incrementCounter() {
        Element el = getElement();
        if (el != null)
            el.increment(current);
        else
            elementList.add(new Element(prev, current));
    }

    /**
     * Shifts prev and replaces current value.
     *
     * @param value new current note
     */
    public void updateWith(Note value) {
        prev.remove(0);
        prev.add(current);
        current = value;
    }

    /**
     * Replaces prev list with a list.
     *
     * @param list list which prev will be replaced with.
     * @see Matrix#clearPrev() to clear prev list.
     */
    public void updateWith(List<Note> list) {
        prev.clear();
        prev = new ArrayList<>(list);
    }

    /**
     * Adds value to prev list also taking care of the non-significant one.
     * Used during the prediction part when initial values are already done so current element doesn't matter.
     *
     * @param value to be added to prev list
     */
    public void updatePrevWith(Note value) {
        prev.remove(0);
        prev.add(value);
        current = null;
    }

    /**
     * Gets sum of counters in element, which has the same prev as current prev list.
     * Used in the prediction part to know how many element occurred after set of elements currently in prev list.
     *
     * @return sum of counters
     */
    public int getCounterSum() {
        Element el = getElement();
        if (el == null)
            throw new IndexOutOfBoundsException("Cannot get such an element");

        int sum = 0;
        for (Pair<Note, Integer> pair : el.getCounterList()) {
            sum += pair.getValue();
        }
        return sum;
    } 
}