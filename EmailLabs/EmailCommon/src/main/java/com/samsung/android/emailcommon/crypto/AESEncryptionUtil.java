package com.samsung.android.emailcommon.crypto;

import java.io.UnsupportedEncodingException;



public class AESEncryptionUtil  {
    /** Called when the activity is first created. */
	
	public static String LOGTAG = "MEALY_TEST";
	
    public static String AESEncryption(String password) {
        
        AESEncryption enc = new AESEncryption();
        
        // first param : string
        // second param : key : must be hidden
        //Log.d(LOGTAG, "AESEncryption.str : "+AESEncryption.uniqueStr);
        //Log.d(LOGTAG, "password : "+password);
        
        String encCode = enc.encrypt(password, AESEncryption.uniqueStr);

        //Log.i(LOGTAG, "encCode :"+encCode);
        return encCode;
    }

    public static String AESDecryption(String encryptpassword) {
        
        AESEncryption enc = new AESEncryption();
         
        // first param : string
        // second param : key : must be hidden
        //Log.d(LOGTAG, "AESDecryption.str : " + AESEncryption.uniqueStr);
        //Log.d(LOGTAG, "encryptpassword : "+encryptpassword);
        String decCode=encryptpassword;
        try {
            // first param : string
            // second param : key : must be hidden
            decCode = enc.decrypt(encryptpassword, AESEncryption.uniqueStr);
            //Log.i(LOGTAG, "decCode :"+decCode);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decCode;
        
    }
    

    public static String AESDecryptionOld(String encryptpassword) {

        AESEncryption enc = new AESEncryption();

        // first param : string
        // second param : key : must be hidden
        //Log.d(LOGTAG, "AESDecryption.str : "+AESEncryption.str);
        //Log.d(LOGTAG, "encryptpassword : "+encryptpassword);
        String decCode=encryptpassword;
        try {
            // first param : string
            // second param : key : must be hidden
            decCode = enc.decrypt(encryptpassword, AESEncryption.str);
            //Log.i(LOGTAG, "decCode :"+decCode);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decCode;

    }

    public static String AESEncryptionOldHashed(String password) {

        AESEncryption enc = new AESEncryption();

        // first param : string
        // second param : key : must be hidden
        //Log.d(LOGTAG, "AESEncryption.str : "+enc.getHashedUniqueStr());
        //Log.d(LOGTAG, "password : "+password);

        String encCode = enc.encrypt(password, enc.getHashedUniqueStr());

        //Log.i(LOGTAG, "encCode :"+encCode);
        return encCode;
    }
    public static String AESDecryptionOldHashed(String encryptpassword) {

        AESEncryption enc = new AESEncryption();

        // first param : string
        // second param : key : must be hidden
        //Log.d(LOGTAG, "AESDecryption.str : " + enc.getHashedUniqueStr());
        //Log.d(LOGTAG, "encryptpassword : "+encryptpassword);
        String decCode=encryptpassword;
        try {
            // first param : string
            // second param : key : must be hidden
            decCode = enc.decrypt(encryptpassword, enc.getHashedUniqueStr());
            //Log.i(LOGTAG, "decCode :"+decCode);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return decCode;

    }

    //MNO B2B START ns.bisht 
    /**
     * Method to return the decrypted data, using the given key
     * @param encryptedData
     * @param key
     * @return decryptedData
     */
    public static String AesDecryption(String encryptedData, String key, String algo) {
        String decryptedString = null;
        decryptedString = AESEncryption.decryptWithGivenKey(encryptedData, key, algo);
        return decryptedString;
    }
    //MNO B2B END ns.bisht
}
