package bulkData;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Create100FoldersFromWeb {
	
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
	public void create100Folders() throws Exception {

		// 웹에서 폴더 100개 생성
		for(int i=0; i<100; i++) {
			WebUtil.createFolder(driver);
			Thread.sleep(2000);
		}
		
		// PC에서 동기화 확인
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(100, userId, targetDir);
		
	}
	
	@After
	public void tearDown() throws Exception {

		//Close the browser
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		
		// 동기화 폴더 초기화
		try{
			fu.cleanDirectory(userId);		
		} catch(Exception e){
		}
		
	}
}