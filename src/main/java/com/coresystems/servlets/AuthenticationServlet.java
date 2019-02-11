package com.coresystems.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Servlet implementation class AuthenticationServlet
 */
@WebServlet("/AuthenticationServlet")
public class AuthenticationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(AuthenticationServlet.class.getName());

	
	
	@Override
	public void init(){
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		
		String logout = request.getParameter("logout");
		if (logout.equals("true")) {
			HttpSession session = request.getSession();
			session.invalidate();
			response.getWriter().println(" {\" success\" : \"true\" } ");
		}else {
			response.getWriter().println(" {\"success \" : \"false\"} ");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		try {

			Gson gson = new Gson();
			JsonObject json = gson.fromJson(request.getReader(), JsonObject.class);

			if (!json.has("username")) {
				response.getWriter().println("{\"error\" : \"username is required\"}");

				return;
			}
			if (!json.has("password")) {
				response.getWriter().println("{\"error\" : \"password is required\"}");
				return;
			}
			String username = json.get("username").getAsString();
			String password = json.get("password").getAsString();

			if (username.equals("admin") && password.equals("password")) {
				HttpSession session = request.getSession();
				session.setAttribute("username", username);
				response.getWriter().println("{\"success\" : \"true\"}");
			} else {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.getWriter().println("{\"success\" : \"false\"}");
				logger.info("Failed to authenticate");
			}

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			response.getWriter().println("{\"failed\" : \"failed to pass json\"}");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().println("{\"error\" : \"system error\"}");
			logger.info(e.getMessage());
		}
	}

}
