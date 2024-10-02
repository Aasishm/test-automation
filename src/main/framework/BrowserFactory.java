package main.framework;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.*;
public class BrowserFactory {
	
	/*
	 * Configuring WebDriver for Mobile/Desktop
	 */
	public static WebDriver getWebDriver(String mode) {
		WebDriver driver = null;
		ChromeOptions chromeOptions = new ChromeOptions();
		chromeOptions.addArguments("headless");
		chromeOptions.addArguments("--no-sandbox");  // Bypass OS security model in Docker
	    chromeOptions.addArguments("--disable-dev-shm-usage"); // Prevents crashes in resource-constrained environments
	    chromeOptions.addArguments("--disable-gpu"); 
		//chromeOptions.addArguments("--window-position=-10000,-10000");
		if (mode.equalsIgnoreCase("mobile")) {
			Map<String, String> mobileEmulation = new HashMap<>();
			mobileEmulation.put("deviceName", "iPhone X");
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
		}
		driver = new ChromeDriver(chromeOptions);
		driver.manage().window().maximize();
		return driver;

	}
}
