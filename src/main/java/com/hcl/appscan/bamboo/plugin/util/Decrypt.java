package com.hcl.appscan.bamboo.plugin.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Decrypt {
    public static void main(String[] args) throws Exception {
        decrypt(args[0]);
    }

    public static String decrypt(String arg) throws Exception {
        String decryptValue = decryptValue(arg);
        System.out.println(decryptValue);
        return decryptValue;
    }

    public static String decryptValue(String arg) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String encryptionKey = "Beetlejuice version $version (c) Copyright 2003-2005 Pols Consulting Limited";
        DESedeKeySpec myKeySpec = new DESedeKeySpec(encryptionKey.getBytes("UTF8"));
        SecretKeyFactory myKeyFactory = SecretKeyFactory.getInstance("DESede");

        SecretKey secretKey = myKeyFactory.generateSecret(myKeySpec);

        Cipher decrypter = Cipher.getInstance("DESede");
        decrypter.init(2, secretKey);

        byte[] data = DatatypeConverter.parseBase64Binary(arg);

        return (new String(decrypter.doFinal(data)));
    }
}
