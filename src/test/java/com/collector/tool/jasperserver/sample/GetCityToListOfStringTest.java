package com.collector.tool.jasperserver.sample;

import com.collector.config.ConfigProperties;
import com.collector.model.JasperServer;
import com.collector.utils.JasperRestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;

/**
 * 
 * @author Thomas Zimmer - mail@ThomasZimmer.net - http://www.thomaszimmer.net
 *
 */
public class GetCityToListOfStringTest {

	private JasperServer jasperServer = ConfigProperties.getInstance().getJasperServer();

	private final Log log = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception {

		JasperRestUtils restUtils = new JasperRestUtils();
		restUtils.loginToServer();

		// GET request - download report by id from session
		final String resourceUri = "/Reports/Samples/get_city.csv";

		HttpResponse httpResponse = restUtils.sendRequest(
				new HttpGet(), jasperServer.getBaseReport() + resourceUri, null, true);
		
		// Write binary content to output file
		InputStream inputStream = httpResponse.getEntity().getContent();
		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");
		System.out.println("lines = " + lines.size());
	}

}
