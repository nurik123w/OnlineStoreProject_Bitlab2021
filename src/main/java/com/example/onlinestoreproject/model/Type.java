package com.example.onlinestoreproject.model;

public enum Type {
    UNDEFINED,LAPTOP, TV,DESKTOP,PERIPHERY;
    public static int getTypeNumber(Type type) {
        switch (type) {
            case UNDEFINED:
                return 0;
            case LAPTOP:
                return 1;
            case TV:
                return 2;
            case DESKTOP:
                return 3;
            case PERIPHERY:
                return 4;
            default:
                return 1000;
        }
    }

}
