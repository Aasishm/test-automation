package main.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.hc.client5.http.utils.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


public class Framework {
	
	class DriverException extends Exception {
		
		private static final long serialVersionUID = 1L;

		public DriverException(String errorMessage) {
			super(errorMessage);
		}
		
	}
	
	class ElementException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public ElementException(String errorMessage) {
			super(errorMessage);
		}
	}
	
	class WindowException extends Exception {
		
		private static final long serialVersionUID = 1L;
		
		public WindowException(String errorMessage) {
			super(errorMessage);
		}
	}
	
	private WebDriver driver;
	private String Mode;
	
	private static Framework instance;
	
	public static Framework getInstance() {
        if (instance == null) {
            instance = new Framework();
        }
        return instance;
    }
	
	public String getMode() {
		return this.Mode;
	}

	public void instantiateDriver(String Mode) throws DriverException {
		try {
			this.Mode = Mode;
			WebDriver driver = BrowserFactory.getWebDriver(Mode);
			this.driver = driver;
		}
		catch(Exception e) {
			throw new DriverException("Driver Failed to instantiate");
		}
	}
	
	public void openBrowser(String Url) {
		try {
			this.driver.get(Url);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public String getWindowTitle() throws WindowException{
		try {
			return this.driver.getTitle();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WindowException("Unable to get Window Title");
		}
	}
	
	public String getCurrentWindow() throws WindowException {
		try {
			return this.driver.getWindowHandle();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WindowException("Unable to get current Window Handle");
		}
	}
	
	public void switchToNewWindow(String windowId) throws WindowException{
		try {
			this.driver.switchTo().window(windowId);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new WindowException("Switching to Window Failed");
		}
	}
	
	public void clickElement(String XPath) throws ElementException{
		try {
			Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30));
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(XPath)));
			element.click();
			Thread.sleep(3000);
//			wait.until((ExpectedCondition<Boolean>) wd -> 
//	            ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
		}
		catch (Exception e) {
			throw new ElementException("Element Click Failed");
		}
	}
	
	public ArrayList<String> getWindowHandles() throws WindowException {
		try {
			Set<String> windowHandles = this.driver.getWindowHandles();
			return new ArrayList<String>(windowHandles);
		}
		catch (Exception e) {
			throw new WindowException("Failed to get Window Handles");
		}
	}
	
	public boolean isElementPresentByName(String name) {
		try {
			Wait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(name)));
			return true;
		}
		catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public void switchToDefaultPage() throws WindowException{
		try {
			ArrayList<String> windowHandles = this.getWindowHandles();
			this.driver.switchTo().window(windowHandles.get(0));
			System.out.println(this.driver.getTitle());
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new WindowException("Switching back to Home Page Failed");
		}
	}
	
	public List<WebElement> findElementsByTag(String tag) {
		try {
			List<WebElement> elements = this.driver.findElements(By.tagName(tag));
			return elements;
		}
		catch (Exception e) {
			return Collections.emptyList();
		}
	}
	
	public String getAttribute(WebElement element, String attribute) {
		try {
			String value = element.getAttribute(attribute);
			return value;
		}
		catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public boolean isElementPresentByXPath(String XPath) {
		try {
//			this.driver.findElement(By.xpath(XPath));
			Wait<WebDriver> wait = new WebDriverWait(this.driver, Duration.ofSeconds(25));
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(XPath)));
			return true;
		}
		catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public String takeScreenshot(String screenshotName) {
	    TakesScreenshot ts = (TakesScreenshot) this.driver;
	    File source = ts.getScreenshotAs(OutputType.FILE);
	    String dest = "test-output" + File.separator + "screenshots" + File.separator + screenshotName + ".png";
	    File destination = new File(dest);
	    
	    FileInputStream fileInputStream = null;
	    String encodedBase64 = null;
	    
	    try {
	        FileUtils.copyFile(source, destination);
	        fileInputStream = new FileInputStream(destination);
	        byte[] bytes =new byte[(int)destination.length()];
	        fileInputStream.read(bytes);
	        encodedBase64 = new String(Base64.encodeBase64(bytes));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return "data:image/png;base64," + encodedBase64;
	}
	
	public void closeBrowser() {
		if(this.driver != null) {
			this.driver.quit();
		}
	}
	

}
