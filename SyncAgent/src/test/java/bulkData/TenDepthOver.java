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
public class TenDepthOver {
	
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
	public void tenDepthFolderSync() throws Exception {

		// pc에서 10개 depth로 폴더 생성
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File("/1/2/3/4/5/6/7/8/9/10");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// 웹에서 동기화 확인
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("1", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("2", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("3", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("4", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("5", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("6", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("7", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("8", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("9", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder("10", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		List<String> list = WebUtil.getList(driver);
		assertTrue("웹에서 파일 동기화 실패!", list.contains("File_DOCX.docx"));
		
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