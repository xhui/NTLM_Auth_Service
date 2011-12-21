package com.hengtiansoft.dst;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * An example to decrypt token, for test only.
 * 
 * @author maxxu
 */
public class ReceiverServlet extends HttpServlet
{

	private static final long	serialVersionUID	= -2361293193040397704L;
	private Logger				logger				= Logger.getLogger(ReceiverServlet.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException
	{
		String token = req.getParameter("token");
		logger.debug("Token is: " + token);
		if (token != null && !"".equals(token))
		{
			try
			{
				resp.getWriter().write(Util.decryptToken(token));
			}
			catch (IOException e)
			{
				logger.fatal("Fail to write to" + token, e);
			}
		}
	}

}