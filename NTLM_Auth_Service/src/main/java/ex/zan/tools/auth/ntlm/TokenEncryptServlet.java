package ex.zan.tools.auth.ntlm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class TokenEncryptServlet extends HttpServlet
{
	private static final long	serialVersionUID	= -4311569743494347194L;

	private Logger				logger				= Logger
															.getLogger(TokenEncryptServlet.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	{
		String redirectURL = req.getParameter("service");
		StringBuilder sb = new StringBuilder();
		sb.append(redirectURL);
		logger.debug("Remote user: " + req.getRemoteUser());
		String token = getToken(req.getRemoteUser());
		logger.debug("token: " + token);
		if (token != null)
		{
			sb.append(redirectURL.contains("?") ? "&" : "?");
			sb.append("token=");
			try
			{
				sb.append(URLEncoder.encode(Util.encryptToken(token), "UTF8"));
			}
			catch (UnsupportedEncodingException e)
			{
				logger.error("UTF8 unsupportted...", e);
				sb.append("none");
			}
		}
		try
		{
			resp.sendRedirect(sb.toString());
			logger.debug("redirected to : " + sb.toString());
		}
		catch (IOException e)
		{
			logger.fatal("Fail to redirect to" + redirectURL, e);
			try
			{
				// can not redirect, send to 401 error
				req.getRequestDispatcher("/401.jsp").forward(req, resp);
			}
			catch (Throwable t)
			{
				logger.fatal("Fail to forward to" + redirectURL, t);
			}
		}
	}

	private String getToken(String token)
	{
		if (token != null && !"".equals(token))
		{
			SimpleDateFormat df = new SimpleDateFormat();
			df.applyPattern("yyyy-MM-dd'T'HH:mm:ss");
			return (token.contains("\\") ? token.substring(token
					.lastIndexOf('\\') + 1) : token)
					+ "_____" + df.format(new Date());
		}
		return null;
	}
}