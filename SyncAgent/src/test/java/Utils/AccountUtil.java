package Utils;

import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountUtil {

	public static WebDriver login(String userid, String password) throws Exception {
		
		WebDriver driver = new ChromeDriver();
		driver.get(getServerUrl());
		
		WebElement id = driver.findElement(By.xpath("//*[@id=\"wrap\"]/div/div/div[2]/div/div[1]/div[1]/form/fieldset/div[1]/input"));
		id.sendKeys(userid);
		
		WebElement pwd = driver.findElement(By.id("inputLoginPwd"));
		pwd.sendKeys(password);
		
		WebElement btn = driver.findElement(By.className("logins_btn"));
		btn.click();
		
		// Wait for the page to load, timeout after 10 seconds
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("btn_folderadd")).isDisplayed();
			}
		});
		
		Thread.sleep(1 * 1000);
		return driver;
	}
	
	public static String getUserId() {
		ResourceBundle rb = ResourceBundle.getBundle("UserSetting");
		return rb.getString("TEST_ACCOUNT_USER_ID");
	}
	
	public static String getUserPwd() {
		ResourceBundle rb = ResourceBundle.getBundle("UserSetting");
		return rb.getString("TEST_ACCOUNT_USER_PW");
	}
	
	public static String getServerUrl() {
		ResourceBundle rb = ResourceBundle.getBundle("UserSetting");
		return rb.getString("CellWe_VER_SERVER_URL");
	}
	
}
