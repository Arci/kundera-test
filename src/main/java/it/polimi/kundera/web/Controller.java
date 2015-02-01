package it.polimi.kundera.web;

import lombok.NoArgsConstructor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@NoArgsConstructor
public abstract class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		act('G', request, response);
	}

	@Override
	protected final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		act('P', request, response);
	}

	private void act(char type, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Navigation nav = new Navigation(request, response);
		if (type == 'G') {
			get(nav);
		} else if (type == 'P') {
			post(nav);
		} else {
			nav.fwd(PagePath.ERROR);
		}
	}

	protected abstract void get(Navigation nav) throws IOException, ServletException;

	protected abstract void post(Navigation nav) throws IOException, ServletException;
}
