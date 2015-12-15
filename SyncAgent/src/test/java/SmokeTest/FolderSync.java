package SmokeTest;

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
public class FolderSync {
	
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
	public void createAtPC() throws Exception {
		
		// pc에서 폴더 생성
		fu.createFolder("/New Folder", userId);
		
		// 웹에서 확인
		WebUtil.refreshUntil90Seconds(1, driver);
		List<String> list = WebUtil.getList(driver);
		assertTrue("웹에서 파일 동기화 실패!", list.contains("New Folder"));
		
	}
	
	@Test
	public void renameAtPC() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/New Folder", userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// PC에서 폴더명 변경
		String oldName = "New Folder";
		String newName = "New Folder renamed";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 폴더명 변경 확인
		WebUtil.refreshUntil60Seconds("New Folder renamed", driver);
		
	}
	
	@Test
	public void deleteAtPC() throws Exception {
	
		// 폴더 생성 후 동기화
		fu.createFolder("/New Folder", userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// PC에서 폴더 삭제
		String fileName = "New Folder";
		fu.forceDelete(fileName, userId);
		
		// 웹에서 확인
		WebUtil.refreshUntil90Seconds(0, driver);
		
	}
	
	@Test
	public void moveAtPC() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/New Folder1", userId);
		fu.createFolder("/New Folder2", userId);
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// PC에서 폴더 이동
		File srcDir = new File("New Folder1");
		File dstDir = new File("New Folder2");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 확인
		WebUtil.navigateToFolder("New Folder2", driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		List<String> list = WebUtil.getList(driver);
		assertTrue("웹에서 폴더 동기화 실패!", list.contains("New Folder1"));
		
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