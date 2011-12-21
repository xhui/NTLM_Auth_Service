package com.hengtiansoft.dst;

import junit.framework.Assert;

import org.junit.Test;

public class Util_Test
{
	private static final String	TOKEN	= "test";

	@Test
	public void testEncryptAndDecrypt()
	{
		Assert.assertEquals(TOKEN, Util.decryptToken(Util.encryptToken(TOKEN)));
	}

}
