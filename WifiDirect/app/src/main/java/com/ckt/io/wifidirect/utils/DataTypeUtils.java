package com.ckt.io.wifidirect.utils;

import java.text.DecimalFormat;

/**
 * Created by admin on 2016/3/14.
 */
public class DataTypeUtils {
    public static byte[] intToBytes2(int n) {
        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    public static int byteToInt2(byte[] b) {
        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < b.length; i++) {
            n <<= 8;
            temp = b[i] & mask;
            n |= temp;
        }
        return n;
    }

    public static String format(double num) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(num);
    }
}
