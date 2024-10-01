package main.pages;
import main.framework.Framework;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.WebElement;

import main.config.Config;

public class HomePage {
	
	private Framework framework = Framework.getInstance();
	private String Mode = "";
	private String homeTitle = "";
	private List<WebElement> homePageLinks;
	
	public void setupDriver(String Mode) {
		try {
			this.Mode = Mode;
			this.framework.instantiateDriver(Mode);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void navigateToHome() {
		this.framework.openBrowser(Config.phpTravelsDemoUrl);
	}
	
	public boolean navigateToLoginPage() {
		try {
			
			// Get the first window title and the title after clicking login button. If both are different, then navigation has happened
			this.homeTitle = this.framework.getWindowTitle();
			System.out.println("The Initial Window Title is : " + this.homeTitle);
			if(this.Mode.equals("mobile")) {
				this.framework.clickElement(Config.mobileHamburgerOptionsXPath);
			}
			this.framework.clickElement(Config.loginButtonXPath);
			
			// Get the number of window handles. There should be 2 of them, one initially opened and the one opened after clicking Login button
			ArrayList<String> windows = this.framework.getWindowHandles();
			
			// Switch to new window handle that just opened and get its title
			this.framework.switchToNewWindow(windows.get(1));
			String newWindowTitle = this.framework.getWindowTitle();
			System.out.println("The New Window Title is : " + newWindowTitle);
			
			// Evaluating the two conditions to make sure that navigation has taken place
			return !newWindowTitle.equals(this.homeTitle) && windows.size() == 2;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean switchBackHome() {
		try {
			System.out.println("Switching back to home page");
			this.framework.switchToDefaultPage();
			String currentTitle = this.framework.getWindowTitle();
			System.out.println("Window Title after Switching to Home Page : " + currentTitle);
			System.out.println("Home Title : " + this.homeTitle);
			return currentTitle.equals(this.homeTitle);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	public List<WebElement> getAllLinks() {
		try {
			this.homePageLinks = this.framework.findElementsByTag("a");
			return this.homePageLinks;
		}
		catch (Exception e) {
			System.out.println("Unable to find links");
			return Collections.emptyList();
		}
	}
	
	public List<Integer> getRequestStatusCodes() {
		List<Integer> statusCodes = new ArrayList<Integer>();
		try {
			HttpClient client = HttpClient.newHttpClient();
			for(WebElement linkElement: this.homePageLinks) {
				String href = this.framework.getAttribute(linkElement, "href");
				System.out.println("Link : " + href);
				try {
					HttpRequest request = HttpRequest.newBuilder()
			                .uri(URI.create(href))
			                .build();
					HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
					statusCodes.add(response.statusCode());
					System.out.println("Response Code: " + response.statusCode());
				}
				catch(Exception e) {
					System.out.println(e.getMessage());
					continue;
				}
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		return statusCodes;
	}
	
	public String takeScreenshot(String name) {
		return this.framework.takeScreenshot(name);
	}
	
	
	public void endTest() {
		this.framework.closeBrowser();
	}

}
