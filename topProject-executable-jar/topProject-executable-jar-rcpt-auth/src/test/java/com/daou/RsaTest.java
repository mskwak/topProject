package com.daou;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

public class RsaTest {

	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		String publicKey = "D1E4297D787302003419A2F758BD9C79A341255031E758D85D8FDA4E4577DEA0A0FEA7408B0E11A0505791BCA4E8E8DD1CA122873318F231A621C3C971B2BBAF1668BEAE76DAED7B2A1E510EE1292FDAF09BB4C930242BCB26ADBD7762BD02AC7D99E1BA2BCDDB4C5AC15D97CC5B0E31133D2C702BE762D29EADC6A12104B46B";
		String exponent = "10001";
		BigInteger modulus = new BigInteger(publicKey, 16);
		BigInteger pubExp = new BigInteger(exponent, 16);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

//		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1");
//		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);

		byte[] cipherData = cipher.doFinal("2016y02m25d 17h05m53s|^|t2st_nate9445|^|test123!@".getBytes());
		String str = Base64.encodeBase64String(cipherData);
		System.out.println(str);
	}
}
