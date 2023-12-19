package ru.otus.library;

import java.security.MessageDigest;
import java.util.stream.Stream;

public class Hash {
    public static void main(String[] args) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("MD5");
//        digest.update("!somePassWithDigits123AndSymbols''".getBytes());
        digest.update("password".getBytes());

        byte[] hash = digest.digest();
        StringBuffer result = new StringBuffer();
        for(byte item: hash) {
            result.append(String.format("%X", item));
        }
        System.out.println(result.toString());
    }
}
