package com.everest.courier;

public class Factory {
    public static Process getProcess(String name) {
        if(name.equals("cost"))
            return new Process();
        else
            return null;
    }
}
