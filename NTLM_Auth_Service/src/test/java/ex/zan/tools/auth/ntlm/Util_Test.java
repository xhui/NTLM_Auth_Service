package ex.zan.tools.auth.ntlm;

import junit.framework.Assert;

import org.junit.Test;

import ex.zan.tools.auth.ntlm.Util;

public class Util_Test
{
	private static final String	TOKEN	= "test";

	@Test
	public void testEncryptAndDecrypt()
	{
		Assert.assertEquals(TOKEN, Util.decryptToken(Util.encryptToken(TOKEN)));
	}

}
