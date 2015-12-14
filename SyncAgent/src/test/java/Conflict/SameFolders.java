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
public class SameFolders {
	
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
	public void renameAtPC_moveAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict2", userId);
		fu.createFolder("/Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 이름 변경
		String oldName = "Conflict2";
		String newName = "Conflict2_PC";
		fu.renameFileDirectory(oldName, newName, userId);
		
		Thread.sleep(1 * 1000);
		
		// 웹에서 이동
		String folderName = "Conflict2";
		String parentFolder = "Move";
		WebUtil.moveToFolder(folderName, parentFolder, driver);
		
		// 동기화
		String targetDir = "/Move";
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Move"));
		assertFalse(list.contains("Conflict2_PC"));
		
		list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict2_PC"));
		
	}
	
	@Test
	public void renameAtPC_deleteAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict3", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 이름 변경
		String oldName = "Conflict3";
		String newName = "Conflict3_PC";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict3", driver);
		
		// 동기화
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("(conflicted)Conflict3_PC"));
	
		// 확인(웹)
		list = WebUtil.getFileNameList(driver);
		assertTrue(list.contains("(conflicted)Conflict3_PC"));
	}
	
	@Test
	public void renameAtPC_renameAtWEB() throws Exception {
	
		// Conflict1 폴더 생성 후 동기화
		fu.createFolder("/conflict1", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 이름 변경
		String oldName = "conflict1";
		String newName1 = "conflict_PC";
		fu.renameFileDirectory(oldName, newName1, userId);
		
		Thread.sleep(1 * 1000);
		
		// 웹에서 이름 변경
		String newName2 = "conflict_WEB";
		WebUtil.rename(oldName, newName2, driver);
		
		// 동기화
		WebUtil.refreshUntil60Seconds(newName1, driver);
		
		// 폴더명 변경 확인(웹)
		List<String> list = WebUtil.getFileNameList(driver);
		assertTrue(list.contains(newName1));
		assertFalse(list.contains(oldName));
		assertFalse(list.contains(newName2));
		
		// 폴더명 변경 확인(PC) 
		list = fu.getFileList(File.separator, userId);
		assertTrue("컨플릭트 정책 실패", list.contains(newName1));
		assertFalse(list.contains(oldName));
		assertFalse(list.contains(newName2));
		
	}
	
	@Test
	public void moveAtPC_renameAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict4", userId);
		fu.createFolder("/Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 폴더 이동
		File srcDir = new File("Conflict4");
		File dstDir = new File("Move");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		Thread.sleep(1 * 1000);
		
		// 웹에서 이름 변경
		String oldName = "Conflict4";
		String newName = "Conflict4_WEB";
		WebUtil.rename(oldName, newName, driver);	
		
		// 동기화
		String targetDir = File.separator;
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict4_WEB"));
		list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("(conflicted)Conflict4")); 
	}
	
	@Test
	public void moveAtPC_moveAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict5", userId);
		fu.createFolder("/Move01", userId);
		fu.createFolder("/Move02", userId);
		WebUtil.refreshUntil60Seconds(3, driver);
		
		// pc에서 폴더 이동
		File srcDir = new File("Conflict5");
		File dstDir = new File("Move01");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 이동
		String folderName = "Conflict5";
		String parentFolder = "Move02";
		WebUtil.moveToFolder(folderName, parentFolder, driver);
		
		// 동기화
		WebUtil.refreshAfter60Seconds(driver);
		
		// 확인(pc)
		List<String> list = fu.getFileList("/Move02", userId);
		assertFalse(list.contains("Conflict5"));
		list = fu.getFileList("/Move01", userId);
		assertTrue(list.contains("Conflict5"));
		
	}
	
	@Test
	public void moveAtPC_deleteAtWEB() throws Exception {
	
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict6", userId);
		fu.createFolder("/Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 폴더 이동
		File srcDir = new File("Conflict6");
		File dstDir = new File("Move");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict6", driver);
		
		// 확인(pc)
		File dir = new File("/Move");
		fu.checkUtil60Seconds("(conflicted)Conflict6", userId, dir);
		
	}
	
	@Test
	public void deleteAtPC_renameAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict7", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 삭제
		String folderName = "Conflict7";
		fu.deleteDirectory(folderName, userId);
		
		// 웹에서 이름 변경
		String oldName = "Conflict7";
		String newName = "Conflict7_WEB";
		WebUtil.rename(oldName, newName, driver);	
		
		// 동기화
		String targetDir = File.separator;
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(pc)
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict7_WEB"));
		
	}
	
	@Test
	public void deleteAtPC_moveAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict8", userId);
		fu.createFolder("/Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 삭제
		String folderName = "Conflict8";
		fu.deleteDirectory(folderName, userId);
		
		// 웹에서 이동
		String srcFolder = "Conflict8";
		String dstFolder = "Move";
		WebUtil.moveToFolder(srcFolder, dstFolder, driver);
		
		// 동기화
		String targetDir = "/Move";
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(pc)
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict8"));
		
	}
	
	@Test
	public void deleteAtPC_deleteAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Conflict9", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 삭제
		String folderName = "Conflict9";
		fu.deleteDirectory(folderName, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict9", driver);
		
		// 동기화
		WebUtil.refreshAfter60Seconds(driver);
		
		// 확인(pc)
		List<String> list = fu.getFileList(File.separator, userId);
		assertFalse(list.contains("Conflict9"));
		
		// 확인(웹)
		list = WebUtil.getFileNameList(driver);
		assertFalse(list.contains("Conflict9"));
		
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
