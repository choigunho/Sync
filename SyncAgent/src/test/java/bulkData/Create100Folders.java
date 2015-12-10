package bulkData;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Create100Folders {
	
	String userId = AccountUtil.getUserId();
	String pwd =  AccountUtil.getUserPwd();
	
	protected WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	FileUtil fu = new FileUtil();

	@Before
	public void setUp() throws Exception{
		
		// 동기화 폴더 초기화
		try{
			fu.cleanDirectory(userId);		
		} catch(Exception e){}
		
		// 웹 로그인
		driver = AccountUtil.login(userId, pwd);
	}
	
	@Test
	public void create100FoldersFromWeb() throws Exception {

		int totalFolders = 100;
		
		// 웹에서 폴더 100개 생성
		for(int i=1; i<=totalFolders; i++) {
			WebUtil.createFolder(driver);
			Thread.sleep(3000);
		}
		
		// PC에서 동기화 확인
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(totalFolders, userId, targetDir);
		
		// PC에서폴더 100개 삭제
		fu.cleanDirectory(userId);
		
		// 웹에서 동기화 확인
		WebUtil.refreshUntil60Seconds(0, driver);
		
	}
	
	@Test
	public void create100FoldersFromPC() throws Exception {
		
		int totalFolders = 100;
		
		// PC에서 폴더 100개 생성
		for(int i=1; i<=totalFolders; i++) {
			fu.createFolder(i + " new folder", userId);			
		}
		
		// 웹에서 동기화 확인
		WebUtil.refreshAfter60Seconds(driver);

		// Inbox 부분을 한 번 클릭해야 Page Down키가 동작함
		WebElement subject_line = driver.findElement(By.className("subject_line"));
		subject_line.click();
		
		// 페이지 다운
		Actions action = new Actions(driver);
		
		int pageDownCount = 6;
		for (int i=0; i<pageDownCount; i++) {
			action.sendKeys(Keys.PAGE_DOWN).perform();
			Thread.sleep(1 * 1000);	
		}
		
		List<String> list = WebUtil.getList(driver);
		assertEquals(totalFolders, list.size());
		
		// PC에서폴더 100개 삭제
		fu.cleanDirectory(userId);
		
		// 웹에서 동기화 확인
		WebUtil.refreshUntil60Seconds(0, driver);
		
	}
	
	@After
	public void tearDown() throws Exception {	
		
		// 동기화 폴더 초기화
		fu.cleanDirectory(userId);		
		
		//Close the browser
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}