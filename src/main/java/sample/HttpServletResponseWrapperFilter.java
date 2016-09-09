package sample;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Filter to demo the bug which happens if both are true:
 *
 * <ul>
 * <li>the {@link HttpServletResponse} is wrapped with
 * {@link HttpServletResponseWrapper} - performed if HTTP parameter "wrap" is
 * non-null</li>
 * <li>the {@link HttpServletResponse#setLocale(Locale)} is set - performed if
 * HTTP parameter "locale" is non-null</li>
 * </ul>
 *
 * @author Rob Winch
 */
@WebFilter("/*")
public class HttpServletResponseWrapperFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		boolean wrap = request.getParameter("wrap") != null;
		boolean locale = request.getParameter("locale") != null;

		if (wrap) {
			response = new HttpServletResponseWrapper((HttpServletResponse) response);
		}

		if (locale) {
			response.setLocale(Locale.US);
		}

		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}
