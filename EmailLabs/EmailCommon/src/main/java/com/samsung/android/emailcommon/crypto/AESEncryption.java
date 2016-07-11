package com.samsung.android.emailcommon.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import android.text.TextUtils;
import android.util.Base64;

import com.samsung.android.emailcommon.system.CarrierValues;
import com.samsung.android.emailcommon.utility.Utility;

public class AESEncryption {

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";
	private static final String KEY_ALGORITHM = "AES";
    private static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA1";
//    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final int PBE_ITERATION_COUNT = 100;
    private static final int PBE_KEY_LENGTH = 128;
    private static final int IV_LENGTH = 16;
    private Cipher cipher;
    private static String salt = null;
    
    static byte iv[];
    public static String str = null;
    public static String uniqueStr = null;
    

    int next_str[][] = {{7, 8},{16, 6},{5, 0},{0, 0},{0, 0},{0, 0},{0, 11},{10, 0},{13, 8},{0, 0},{18, 10},{1, 0},{14, 0},{0, 12},{1, 2},{0, 0},{14, 0},{0, 0},{16, 0}};
    char out_char_str[][] = {{'q', '('},{'B', '2'},{'o', '5'},{'w', 'd'},{'k', '>'},{'e', '@'},{'<', 'q'},{'k', 'x'},{'l', 'q'},{'2', 'E'},{'?', 'f'},{'l', '1'},{'x', 'g'},{'H', 'B'},{'n', 'r'},{'=', '^'},{'!', 'L'},{'N', 'F'},{')', 'T'}};

    int next_salt[][] = {{9, 8},{0, 0},{5, 0},{0, 11},{0, 6},{7, 2},{3, 9},{0, 0},{6, 8},{5, 4},{0, 0},{0, 14},{0, 0},{0, 0},{0, 12},{0, 0}};
    char out_char_salt[][] = {{'e', 's'},{'p', '1'},{'g', 'c'},{']', 'a'},{'v', '_'},{'_', 'n'},{'s', 's'},{'?', 's'},{'m', 'a'},{'u', 'c'},{'H', 'E'},{'E', 'l'},{'I', '`'},{'U', 'g'},{'u', 't'},{'x', 'o'}};

    String loadStr() {
        String str1 = null;
        str1 = getInternalStr(0x000128cb, 19, next_str, out_char_str);
        return str1;
    }
    
    public String getHashedUniqueStr() {

        String keyString = CarrierValues.RO_SERIALNO;

        // If by some reason we could not generate unique key string, use old way to make it
        if (keyString == null || "unknown".equals(keyString)) {
            keyString = loadStr();
        } else {
            keyString = getSmallHash(keyString);
            if (TextUtils.isEmpty(keyString)) {
                keyString = loadStr();
            }
        }

        return keyString;
        }


    private String loadUniqueStr() {

        String keyString = CarrierValues.RO_SERIALNO;

        // If by some reason we could not generate unique key string, use old way to make it
        if (TextUtils.isEmpty(keyString) || "unknown".equals(keyString)) {
            keyString = loadStr();
    }

        return keyString;
    }

    String loadSalt() {
        String str1 = null;
        str1 = getInternalStr(0x0000ed2b, 16, next_salt, out_char_salt);
        return str1;
    }
    
    String getInternalStr(int v, int sz, int next[][], char go[][] )
    {
            char strs[] = new char[sz];
            int state = 0, len = 0;

            if (sz > 100){
                    return null;
            }

            while (len < sz) {
                    int input = 1&v; v >>= 1;
                    strs[len++] = go[state][input];
                    state = next[state][input];
            }
            return new String(strs);
    }
    
    public static String LOGTAG = "MEALY_TEST";

    public AESEncryption() {
    	
    	iv = generateIV();
    	str = loadStr();
    	salt = loadSalt();
        uniqueStr = loadUniqueStr();

        try {
            cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            cipher = null;
        }
        catch (NoSuchPaddingException e) {
            cipher = null;
        }
    }

    public SecretKey getSecretKey(String password) {
    	
            try {
                PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes("UTF-8"), PBE_ITERATION_COUNT, PBE_KEY_LENGTH);
                SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_ALGORITHM);
                SecretKey tmp = factory.generateSecret(pbeKeySpec);
                SecretKey secret = new SecretKeySpec(tmp.getEncoded(), KEY_ALGORITHM);
                return secret;
            }
            catch (Exception e) {
                return null;
            }
    }

    public String encrypt(String text, String passcode) {
      //  Thread.sleep(randomInt.nextInt(100));
        if (text == null || text.length() == 0) {
            return null;
        }
        byte[] encrypted = null;
        String encrypted64 = null;
        try {
           
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(passcode), ivspec);
            encrypted = cipher.doFinal(text.getBytes("UTF-8"));
            encrypted64 = Base64.encodeToString(encrypted, Base64.DEFAULT);
        }
        catch (Exception e) {                       
            return null;
        }
      //  Log.i(LOGTAG, "encoded txt :"+ encrypted64);
        return encrypted64;
    }

    public String decrypt(String code, String passcode) throws UnsupportedEncodingException {
       // Thread.sleep(randomInt.nextInt(100));
        if(code == null || code.length() == 0) {
            return null;
        }
        byte[] decrypted = null;
        try {
     
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(passcode), ivspec);
            decrypted = cipher.doFinal(Base64.decode(code, Base64.DEFAULT));
         //   Log.i(LOGTAG, "decrypted : "+decrypted);
        }
        catch (Exception e) {
            return null;
        }
        return new String(decrypted, "UTF-8");
    }

    private byte[] generateIV() {
        try {
            //SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] ivs = new byte[IV_LENGTH];
            //random.nextBytes(iv);

            return ivs;
        }
        catch (Exception e) {
            return null;
        }
    }
    
   // public native String loadStr();
   // public native String loadSalt();
    
    //static{
   // 	System.loadLibrary("jni_emailencryption");
   // }
    
    //MNO B2B START ns.bisht
    /**
     * Method to decode the input data using the given key and given algo
     * @param encrptedData
     * @param inputkey
     * @param algo
     * @return
     */
    public static String decryptWithGivenKey(String encrptedData, String inputkey, String algo) {
        String decryptedData = null;
        Key key = generateKey(inputkey,algo);
        Cipher c = null;
        try {
            c = Cipher.getInstance(algo);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.decode(encrptedData, Base64.DEFAULT);
            byte[] decVlaue = c.doFinal(decodedValue);
            decryptedData = new String(decVlaue);
            return decryptedData;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedData;
    }
    
    private static Key generateKey(String keyValueInput, String algo) {
        byte[] keyArray = Base64.decode(keyValueInput, Base64.DEFAULT);
        Key key = new SecretKeySpec(keyArray, algo);
        return key;
    }

    private static String getSmallHash(String value) {
        final MessageDigest sha;

        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException impossible) {
            return null;
        }

        byte[] bValue = Utility.toUtf8(value);
        if (bValue != null) {
            sha.update(bValue);
        }
        return getHexString(sha.digest());
    }

    private static String getHexString(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++)
            result = result.concat(String.format("%02x", b[i] & 0xff));
        return result;
    }
}


