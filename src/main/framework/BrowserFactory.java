package main.framework;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.*;
public class BrowserFactory {
	
	public static WebDriver getWebDriver(String mode) {
		WebDriver driver = null;
		Map<String, String> mobileEmulation = new HashMap<>();
		if (mode.equalsIgnoreCase("mobile")) {
			mobileEmulation.put("deviceName", "iPhone X");
			ChromeOptions chromeOptions = new ChromeOptions();
			chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
			driver = new ChromeDriver(chromeOptions);
		} else {
			driver = new ChromeDriver();
		}
		driver.manage().window().maximize();
		return driver;

	}
}
