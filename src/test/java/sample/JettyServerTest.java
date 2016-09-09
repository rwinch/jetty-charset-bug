package sample;

import static org.junit.Assert.assertNull;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JettyServerTest {

	private static JettyServer jettyServer;

	@BeforeClass
	public static void setup() throws Exception {
		jettyServer = new JettyServer();
		jettyServer.start();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		jettyServer.stop();
	}

	/**
	 * <b>This Test Fails!</b>
	 *
	 * A {@link HttpServletResponseWrapper} is used and the
	 * {@link HttpServletResponse#setLocale(java.util.Locale)} is set.
	 *
	 * @throws Exception
	 */
	@Test
	public void getWhenRequestIsWrappedAndLocaleSetThenCharsetIsNull() throws Exception {
		String url = "http://localhost:8080/index.html?wrap=true&locale=true";

		assertNull(getCharsetFromUrl(url));
	}

	@Test
	public void getWhenRequestNotWrappedAndLocaleSetThenCharsetIsNull() throws Exception {
		String url = "http://localhost:8080/index.html?locale=true";

		assertNull(getCharsetFromUrl(url));
	}

	@Test
	public void getWhenRequestIsWrappedAndLocaleNotSetThenCharsetIsNull() throws Exception {
		String url = "http://localhost:8080/index.html?wrap=true";

		assertNull(getCharsetFromUrl(url));
	}

	@Test
	public void getWhenRequestNotWrappedAndLocaleNotSetThenCharsetIsNull() throws Exception {
		String url = "http://localhost:8080/index.html";

		assertNull(getCharsetFromUrl(url));
	}

	private String getCharsetFromUrl(String url) throws Exception {
		try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
			HttpGet httpget = new HttpGet(url);
			try (CloseableHttpResponse response = httpclient.execute(httpget)) {
				return getCharset(response.getEntity().getContentType());
			}
		}
	}

	private String getCharset(Header contentType) {
		HeaderElement[] elements = contentType.getElements();
		for (int i = 0; i < elements.length; i++) {
			HeaderElement headerElement = elements[i];
			NameValuePair[] parameters = headerElement.getParameters();
			for (int j = 0; j < parameters.length; j++) {
				NameValuePair nameValuePair = parameters[j];
				if ("charset".equals(nameValuePair.getName())) {
					return nameValuePair.getValue();
				}
			}
		}
		return null;
	}

}
