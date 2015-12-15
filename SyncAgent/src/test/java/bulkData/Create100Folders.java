package bulkData;

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
public class Create100Folders {
	
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
	public void create100FoldersFromWeb() throws Exception {

		int totalFolders = 100;
		
		// 웹에서 폴더 100개 생성
		WebUtil.createManyFolders(totalFolders, driver);
		
		// PC에서 동기화 확인
		String targetDir = File.separator;
		fu.checkCountUntil60Seconds(totalFolders, userId, targetDir);
		
	}
	
	@Test
	public void create100FoldersFromPC() throws Exception {
		
		// PC에서 폴더 100개 생성
		int totalFolders = 100;
		for(int i=1; i<=totalFolders; i++) {
			fu.createFolder("/New folder " + i, userId);			
		}
		
		// 웹에서 동기화 확인
		WebUtil.refreshAfter60Seconds(driver); 
		WebUtil.pageDown(6, driver); // Page Down 키 입력 (부분 렌더링된 나머지 목록을 가져오기 위함)
		List<String> list = WebUtil.getList(driver);
		assertEquals(totalFolders, list.size());
		
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