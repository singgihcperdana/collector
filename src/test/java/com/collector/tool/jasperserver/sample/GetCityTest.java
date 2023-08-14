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
		String resourceUri = "/Reports/Samples/get_city.csv";
		restUtils.sendRequestAndSaveToFile(resourceUri, null, "D:\\tmp\\report\\get_city.csv");
	}

}
