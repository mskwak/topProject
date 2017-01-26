package com.hangugi.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	private FilterConfig filterConfig;

	@Override
	public void destroy() {
		logger.info("destroy SessionFilter...");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		logger.info("doFilter SessionFilter...");
		filterChain.doFilter(servletRequest, servletResponse);
	}

/*
		if(servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
			HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

			String url = httpServletRequest.getServletPath();

			logger.info(url);

			HttpSession httpSession = httpServletRequest.getSession(false);

			if(httpSession == null) {
				httpSession = httpServletRequest.getSession(true);
				httpServletResponse.sendRedirect("/login.do");
				return;
			}

			filterChain.doFilter(servletRequest, servletResponse);

*/
/*
			Cookie[] cookies = httpServletRequest.getCookies();

			if(cookies != null) {
				for(Cookie cookie: cookies) {
					String name = cookie.getName();
					String value = cookie.getValue();
					logger.info(name + ":" + value);
				}
			}



			String id = httpSession.getId();
			long ctime = httpSession.getCreationTime();
			long atime = httpSession.getLastAccessedTime();
			int interval = httpSession.getMaxInactiveInterval();

			logger.info(id + "," + ctime + "," + atime + "," + interval);

			Enumeration<String> e = httpSession.getAttributeNames();

			while(e.hasMoreElements()) {
				logger.info(e.nextElement());
			}
*/


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("init SessionFilter...");
		this.filterConfig = filterConfig;
	}
}
