package com.hengtiansoft.dst;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Util
{
	private static final String	CIPHER_AES_CBC_PKCS5_PADDING	= "AES/CBC/PKCS5Padding";
	private static final String	AES								= "AES";
	private static final String	ENCODE_UTF8						= "UTF8";
	private static String		KEY_SPEC						= null;
	private static String		IV_SPEC							= null;

	private static Logger		logger							= Logger.getLogger(Util.class);

	static
	{
		try
		{
			Properties config = new Properties();
			config.load(Util.class.getClassLoader().getResourceAsStream(
					"config.properties"));
			KEY_SPEC = config.getProperty("keySpec");
			IV_SPEC = config.getProperty("ivSpec");
		}
		catch (IOException e)
		{
			logger.fatal("Fail to read config.properties file", e);
		}
	}

	/**
	 * Used for generate AES key.
	 * 
	 * @param fileLocation
	 */
	public static void initKey(String fileLocation)
	{
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance(AES);
			kg.init(128);
			SecretKey key = kg.generateKey();
			byte[] keyFile = key.getEncoded();
			logger.info(key.getEncoded());
			logger.info(key.getFormat());
			FileOutputStream fo = null;

			try
			{
				fo = new FileOutputStream(fileLocation);
				fo.write(keyFile);
				logger.info("Completed initialize AES key");
			}
			catch (IOException e)
			{
				logger.fatal("Faile to write key file", e);
			}
			finally
			{
				if (fo != null)
				{
					try
					{
						fo.close();
					}
					catch (IOException e)
					{
						logger.error("fail to close file input stream", e);
					}
					finally
					{
						fo = null;
					}
				}
			}

		}
		catch (NoSuchAlgorithmException e)
		{
			logger.fatal("No AES", e);
		}
	}

	public static String encryptToken(String token)
	{
		try
		{
			return new BASE64Encoder().encode(getCipher(Cipher.ENCRYPT_MODE)
					.doFinal(token.getBytes(ENCODE_UTF8)));
		}
		catch (Throwable e)
		{
			logger.error("Fail to encrypt token: ", e);
		}
		return "";
	}

	public static String decryptToken(String token)
	{
		try
		{
			return new String(getCipher(Cipher.DECRYPT_MODE).doFinal(
					new BASE64Decoder().decodeBuffer(token)), ENCODE_UTF8);
		}
		catch (Throwable e)
		{
			logger.fatal("Faile to decrypt token", e);
		}
		return "";
	}

	private static Cipher getCipher(int mode)
	{
		try
		{
			SecretKeySpec skeySpec = new SecretKeySpec(
					KEY_SPEC.getBytes(ENCODE_UTF8), AES);
			Cipher cipher = Cipher.getInstance(CIPHER_AES_CBC_PKCS5_PADDING);
			cipher.init(mode, skeySpec,
					new IvParameterSpec(IV_SPEC.getBytes(ENCODE_UTF8)));
			return cipher;
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static String getRandomString(int length)
	{
		StringBuilder buffer = new StringBuilder(
				"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StringBuilder sb = new StringBuilder();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++)
		{
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Throwable
	{
		System.out.println(getRandomString(16));
	}

}
