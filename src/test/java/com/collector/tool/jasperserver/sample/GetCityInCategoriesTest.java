package com.collector.tool.jasperserver.sample;

import com.collector.config.ConfigProperties;
import com.collector.model.JasperServer;
import com.collector.utils.JasperRestUtils;
import lombok.SneakyThrows;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class GetCityInCategoriesTest {

  private JasperServer jasperServer = ConfigProperties.getInstance().getJasperServer();

  @Test
  @SneakyThrows
  public void go(){
    JasperRestUtils restUtils = new JasperRestUtils();
    restUtils.loginToServer();
    final String resourceUri = "/Reports/Samples/get_city_in_categories.csv";
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("P_CATEGORIES", "Animation,Family"));

    HttpResponse httpResponse = restUtils.sendRequest(
        new HttpGet(), jasperServer.getBaseReport() + resourceUri, params, true);

    // Write binary content to output file
    InputStream is = httpResponse.getEntity().getContent();
    byte[] buffer = new byte[8 * 1024];
    File file = new File("D:\\tmp\\report\\get_city_in_categories.csv");
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
