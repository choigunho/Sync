package Conflict;

import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SameFiles {
	
	String userId = "test3";
	String pwd = "1111";
	
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
	public void renameAtPC_renameAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사
		File srcFile = new File(this.getClass().getResource("TestFiles").getFile() + "/Conflict1.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
				
		
		
		
		
		
		
		
		
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
