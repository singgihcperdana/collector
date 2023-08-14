package com.collector.page;

import com.collector.utils.CollectorUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InsertEventsPage {

  private String cookieFile;
  private String payloadFolder;
  private String timeInterval;

  public InsertEventsPage() {
    try {
      CollectorUtils.printSeparator(5);
      printMenu();
      CollectorUtils.printSeparator(5);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void printMenu() throws IOException {

    Scanner scanner = new Scanner(System.in);

    System.out.print("> Enter cookie location (default cookie.txt): ");
    cookieFile = scanner.nextLine();

    System.out.print("> Enter payload folder location (default 'payload/..'): ");
    payloadFolder = scanner.nextLine();

    System.out.print("> Enter time interval in seconds (default 5 seconds): ");
    timeInterval = scanner.nextLine();

    validation();
    execute();

  }

  private String loadCookie(String location) throws IOException {
    return FileUtils.readFileToString(new File(location), StandardCharsets.UTF_8.name());
  }

  private void execute() throws IOException {
    System.out.println("Processing...");
    CollectorUtils.insertEvents(payloadFolder, loadCookie(cookieFile), Integer.valueOf(timeInterval));
    System.out.println("Done...");
  }

  private void validation(){
    if(StringUtils.isEmpty(cookieFile)) cookieFile = "cookie.txt";
    if(StringUtils.isEmpty(payloadFolder)) payloadFolder = "payload";
    if(StringUtils.isEmpty(timeInterval)) timeInterval = "5";

    if(!new File(cookieFile).isFile()){
      throw new RuntimeException("cookieFile not found");
    }

    if(!new File(payloadFolder).isDirectory()){
      throw new RuntimeException("payload folder not valid, must a folder");
    }
  }

}
