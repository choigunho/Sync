package IssueTest;
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
public class SYNCEA_357 {
	
	String userId = AccountUtil.getUserId();
	String pwd =  AccountUtil.getUserPwd();
	
	protected WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	FileUtil fu = new FileUtil();

	@Before
	public void setUp() throws Exception{
		// 동기화 폴더 초기화
		fu.cleanDirectory(userId);		
		
		// 웹 로그인
		driver = AccountUtil.login(userId, pwd);
	}
	
	@Test 
	public void SYNCEA357_이전과_다른_EFSS응답() throws Exception {
	
		// A폴더, B폴더 생성
		fu.createFolder("/A", userId);
		fu.createFolder("/B", userId);
				
		// 동기화 확인
		WebUtil.refreshUntil90Seconds(2, driver);
		List<String> list = WebUtil.getList(driver);
		assertTrue(list.contains("A"));
		assertTrue(list.contains("B"));
		
		// 로컬에서 A폴더를 B폴더 하위로 이동
		String srcDir = "/A";
		String dstDir = "/B";
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 Move 하위에 폴더 생성
		WebUtil.navigateToFolder("B", driver);
		Thread.sleep(1 * 1000);
		WebUtil.createFolder("A", driver);

		// 동기화 대기
		String targetDir = "/B";
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		List<String> fileList = fu.getFileList("/B", userId);
		assertTrue(fileList.contains("A"));
		assertTrue(fileList.contains("A (1)"));
		
	}
	
	@After
	public void tearDown() throws Exception {

		// 동기화 폴더 초기화
		System.out.println("============동기화 폴더 초기화 시작============");
		fu.cleanDirectory(userId);
		WebUtil.navigateToHome(driver);
		WebUtil.refreshUntil90Seconds(0, driver);
		
		//Close the browser
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}	
		
	}
}
