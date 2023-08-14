package com.collector.config;

import com.collector.model.JasperServer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {

  private static ConfigProperties instance = null;

  private JasperServer jasperServer;

  private ConfigProperties() {
    try (InputStream input = new FileInputStream("config.properties")) {
      Properties prop = new Properties();
      prop.load(input);
      jasperServer = new JasperServer();
      jasperServer.setScheme(prop.getProperty("jasperserver.scheme"));
      jasperServer.setHost(prop.getProperty("jasperserver.host"));
      jasperServer.setPort(Integer.parseInt(prop.getProperty("jasperserver.port")));
      jasperServer.setUsername(prop.getProperty("jasperserver.username"));
      jasperServer.setPassword(prop.getProperty("jasperserver.password"));
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public JasperServer getJasperServer() {
    return jasperServer;
  }

  public static synchronized ConfigProperties getInstance() {
    if (instance == null) {
      instance = new ConfigProperties();
    }
    return instance;
  }
}
