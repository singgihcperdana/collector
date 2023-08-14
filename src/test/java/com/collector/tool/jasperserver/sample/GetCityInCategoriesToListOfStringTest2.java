package com.collector.tool.jasperserver.sample;

import com.collector.utils.JasperRestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @author Thomas Zimmer - mail@ThomasZimmer.net - http://www.thomaszimmer.net
 *
 */
public class GetCityInCategoriesToListOfStringTest2 {

	private final Log log = LogFactory.getLog(getClass());

	@Test
	public void test() throws Exception {

		JasperRestUtils restUtils = new JasperRestUtils();
		restUtils.loginToServer();

		// GET request - download report by id from session
		String resourceUri = "/Reports/Samples/get_city_in_categories.csv";

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("P_CATEGORIES", "Family"));

		HttpResponse httpResponse = restUtils.sendRequest(resourceUri, params);
		
		// Write binary content to list
		InputStream inputStream = httpResponse.getEntity().getContent();
		List<String> lines = IOUtils.readLines(inputStream, "UTF-8");

		//remove header
		lines.remove(0);

		System.out.println("lines = " + lines.size());
		List<Film> films = lines.stream()
				.map(Film::new)
				.collect(Collectors.toList());
		System.out.println("films = " + films.get(0));
	}

	private class Film {
		private int id;
		private String tittle;
		private String category;

		public Film(String from) {
			String[] data = from.split(",");
			Arrays.parallelSetAll(data, i -> data[i].trim());
			this.id = Integer.parseInt(data[0]);
			this.tittle = data[1];
			this.category = data[2];
		}

		@Override
		public String toString() {
			 return "{id=" + id +",tittle="+tittle+",category="+category+ "}";
		}
	}

}
