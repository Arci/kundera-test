package it.polimi.kundera.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Navigation {

	private HttpServletRequest request;
	private HttpServletResponse response;

	public Navigation(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void fwd(String destination) throws IOException, ServletException {
		request.getRequestDispatcher(destination).forward(request, response);
	}

	public void redirect(String destination) throws IOException, ServletException {
		redirect(destination, true);
	}

	public void redirect(String destination, boolean isLocal) throws IOException, ServletException {
		String prefix = isLocal ? request.getContextPath() : "";
		response.sendRedirect(prefix + destination);
	}

	@SuppressWarnings("unchecked")
	public <T> T getParam(String name) {
		return (T) request.getParameter(name);
	}

	public String[] getParamValues(String name) {
		return request.getParameterValues(name);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String name) {
		return (T) request.getAttribute(name);
	}

	public <T> void setAttribute(String name, T value) {
		request.setAttribute(name, value);
	}

	/**
	 * send a 404 error.
	 */
	public void sendNotFound() throws IOException {
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	public String getPath() {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

	public void setContentType(String type) {
		response.setContentType(type);
	}

	public PrintWriter getWriter() throws IOException {
		return response.getWriter();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}
}
