/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author mohamed
 * @param <T>
 */
public class CircularArray<T> {

    private final T[] arr;
    private int currentIndex;

    public CircularArray(T ... t) {
        arr = t;
        currentIndex = 0;
    }

    public void next() {
        currentIndex++;
        if (currentIndex == arr.length) {
            currentIndex = 0;
        }
    }

    public T get() {
        return arr[currentIndex];
    }    
    
}
