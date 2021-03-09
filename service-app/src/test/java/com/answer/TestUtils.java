package com.answer;

import java.util.Random;

public class TestUtils {

    public static String randomString(int len) {
        Random r = new java.util.Random();
        return Long.toString(r.nextLong() & Long.MAX_VALUE, 36);

    }
}

