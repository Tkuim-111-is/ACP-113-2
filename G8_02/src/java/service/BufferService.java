/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.Item;

public class BufferService {
    private static Item buffer = null;

    public static synchronized String produce(Item item) {
        if (buffer == null) {
            buffer = item;
            return "ok";
        } else {
            return "full";
        }
    }

    public static synchronized Item consume() {
        if (buffer == null) {
            return null;
        } else {
            Item temp = buffer;
            buffer = null;
            return temp;
        }
    }
}
