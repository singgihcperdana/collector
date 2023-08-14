package com.collector.tool.jasperserver.sample;

import com.collector.config.ConfigProperties;
import com.collector.model.JasperServer;
import com.collector.utils.JasperRestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author Thomas Zimmer - mail@ThomasZimmer.net - http://www.thomaszimmer.net
 *
 */
public class GetCityTest {

	private JasperServer jasperServer = ConfigProperties.getInstance().getJasperServer();

	private final Log log = LogFactory.getLog(getClass());
	
	@Test
	public void test() throws Exception {

		JasperRestUtils restUtils = new JasperRestUtils();
		restUtils.loginToServer();
		// GET request - download report by id from session
		String resourceUri = "/Reports/Samples/get_city.csv";

		HttpResponse httpResponse = restUtils.sendRequest(resourceUri, null);
		
		// Write binary content to output file
		InputStream is = httpResponse.getEntity().getContent();
		byte[] buffer = new byte[8 * 1024];
		File file = new File("D:\\tmp\\report\\get_city.csv");
		new File(file.getParent()).mkdirs();
		OutputStream output = new FileOutputStream(file);
		try {
		    int bytesRead;
		    while ((bytesRead = is.read(buffer)) != -1) {
		      output.write(buffer, 0, bytesRead);
		    }
		  } finally {
		    output.close();
				is.close();
		  }
	}

}
