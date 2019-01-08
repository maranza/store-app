package com.coresystems.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public final class ServletUtil {
	
	public static void sendError(String message, HttpServletResponse response, Integer status) throws IOException {
		response.setStatus(status);
		response.getWriter().println(String.format("{\" msg \":\"%s\"}", message));
	}
	
	public static void sendResponse(String message,  HttpServletResponse response) throws IOException{
		response.getWriter().println(String.format("{\"msg\":\"%s\"} ", message));
	}
}
