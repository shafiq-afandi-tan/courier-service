package com.everest.courier;


public class Application {
    public static void main(String[] args) {
        new MainProcess(new Factory(), args).run(null);
    }
}
