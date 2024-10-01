package main.pages;

import main.framework.Framework;

/*
 * Login Page methods
 */
public class LoginPage {

	private Framework framework = Framework.getInstance();
	
	public String getPageTitle() throws Exception {
		try {
			return this.framework.getWindowTitle();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public boolean isEmailInputPresent() {
		try {
			boolean emailInputPresent = this.framework.isElementPresentByName("email");
			return emailInputPresent;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isPasswordInputPresent() {
		try {
			boolean passwordInputPresent = this.framework.isElementPresentByName("password");
			return passwordInputPresent;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
