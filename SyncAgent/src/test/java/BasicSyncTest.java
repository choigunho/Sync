import java.io.File;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BasicSyncTest {
	
	String userId = "test3";
	String pwd = "1111";
	
	protected WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	FileUtil fu = new FileUtil();

	@Before
	public void setUp() throws Exception {
		
		// 동기화 폴더 초기화
		fu.cleanDirectory(userId);
		
		// 웹 로그인
		driver = AccountUtil.login(userId, pwd);
	}
	
	@Test
	public void MyDrive폴더에_파일_한개_올리고_웹에서_확인() throws Exception {
		
		// 동기화 폴더에 파일 복사
		File srcFile = new File(this.getClass().getResource("TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// 60초 후 페이지 새로 고침
		WebUtil.refreshUntil60Seconds(1, driver);
				
		// 파일 갯수 확인
		int count = WebUtil.countFileAndFolders(driver);
		assertEquals(1, count);

		// 파일명 확인
		List<String> list = WebUtil.getFileNameList(driver);
		assertTrue(list.contains("File_DOCX.docx"));
		
	}

	@Test
	public void MyDrive폴더에_폴더_한개_만들고_웹에서_확인() throws Exception {
		
		// 폴더 생성
		fu.createFolder("/AAA", userId);
			
		// 60초 후 페이지 새로 고침
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// 폴더 갯수 확인
		int count = WebUtil.countFileAndFolders(driver);
		assertEquals(1, count);
				
		// 폴더명 확인
		List<String> list = WebUtil.getFileNameList(driver);
		assertTrue(list.contains("AAA"));
		
	}
	
	@Test
	public void 웹에서_폴더_생성하고_MyDrive폴더에서_확인() throws Exception {
		
		// 폴더 생성
		WebUtil.createFolder("folderBychoi", driver);
		
		// 동기화 대기
		String targetDir = File.separator;
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 폴더 생성 확인 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue("웹에서 생성한 폴더가 동기화되지 않음.", list.contains("folderBychoi"));
		
	}
	
	//@Test
	public void test1() throws Exception {

		// aaa 폴더 생성
		String path = System.getProperty("user.home") + "/MyDrive(test3)" + "/TestFolder";
		new File(path).mkdir();

		Thread.sleep(3 * 1000);
		
		// 폴더 삭제
		new File(path).delete();
		
	}
	
	//@Test
	public void test2() throws Exception {

		// bbb 폴더에 파일 복사
		File source = new File(this.getClass().getResource("TestFiles").getFile() + "/File_DOCX.docx");
		File dest = new File(System.getProperty("user.home") + "/MyDrive(test3)/bbb" + "/File_DOCX.docx");
		FileUtils.copyFile(source, dest);
		
		// 파일 삭제
		FileUtils.forceDelete(dest);
		
	}
	
	@After
	public void tearDown() throws Exception {

		// 동기화 폴더 초기화
		System.out.println("============동기화 폴더 초기화 시작============");
		fu.cleanDirectory(userId);
		WebUtil.navigateToHome(driver);
		WebUtil.refreshUntil60Seconds(0, driver);
		
		//Close the browser
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		
	}
}
