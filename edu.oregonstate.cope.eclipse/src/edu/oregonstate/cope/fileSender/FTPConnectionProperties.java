package edu.oregonstate.cope.fileSender;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;

public class FTPConnectionProperties {
	
	private static final char[] sk = {'t', 'h', 'i', 's', ' ', 
		'i', 's', ' ', 'a',
		's', 't', 'r', 'i', 'n', 'g', '.', ' ', 
		'r', 'e', 's', 'p', 'e', 'c', 't', ' ', 'i', 't', '!'};
	
	private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x9, (byte) 0x12,
        (byte) 0xda, (byte) 0x34, (byte) 0x8, (byte) 0x42,
    };
	
	private static Properties properties = null;
	
	private static void LoadProperties() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream("ftp.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getHost() {
		if(properties == null) {LoadProperties();}
		return properties.getProperty("host");
	}
	
	public static String getUsername() {
		if(properties == null) {LoadProperties();}
		return properties.getProperty("username");
	}
	
	public static String getCronConfiguration() {
		return getFrequency();
	}
	public static String getFrequency() {
		if(properties == null) {LoadProperties();}
		return properties.getProperty("frequency");
	}
	
	public static String getPassword() throws GeneralSecurityException, IOException {
		if(properties == null) {LoadProperties();}
		String encodedPassword = properties.getProperty("password");
		return decrypt(encodedPassword);
	}
	
	private static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(sk));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

	private static String decrypt(String property)
			throws GeneralSecurityException, IOException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
		SecretKey key = keyFactory.generateSecret(new PBEKeySpec(sk));
		Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
		pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
		return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
	}

    private static String base64Encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
    
    private static byte[] base64Decode(String property) throws IOException {
        return Base64.decodeBase64(property);
    }	
}
