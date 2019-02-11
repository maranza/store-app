package com.coresystems.filters;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
@WebFilter(dispatcherTypes = { DispatcherType.REQUEST }, asyncSupported = true, urlPatterns = {
		"/AuthenticationFilter", "/ProductServlet", "/customers", "/PaymentServlet" }, servletNames = { "CustomerServlet", "PaymentServlet", "ProductServlet" })
public class AuthenticationFilter implements Filter {

	public void init(FilterConfig fConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession httpSession = httpRequest.getSession();
		if (httpSession.getAttribute("username") == null) {
			response.setContentType("application/json");
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			httpResponse.setStatus(httpResponse.SC_FORBIDDEN);
			httpResponse.getWriter().println("{\"msg\":\"You are not authorized \"}");
			return;
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
