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
public class DifferntFolders {
	
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
	public void createAtPC_createAtWEB() throws Exception {
		
		// pc에서 폴더 생성
		fu.createFolder("Conflict10", userId);
		
		// 웹에서 폴더 생성
		WebUtil.createFolder("Conflict10", driver);
		
		// 동기화 
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict10"));
		assertTrue(list.contains("(conflicted)Conflict10"));
		
	}
	
	@Test
	public void createAtPC_renameAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("Conflict", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 폴더 생성
		fu.createFolder("Conflict11", userId);
		
		Thread.sleep(1 * 1000);
		
		// 웹에서 이름 변경
		String oldName = "Conflict";
		String newName = "Conflict11";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화 
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict11"));
		assertTrue(list.contains("(conflicted)Conflict11"));
		
	}
	
	@Test
	public void createAtPC_moveAtWEB() throws Exception {
	
		// 폴더 생성 후 동기화
		fu.createFolder("Conflict12", userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 폴더 생성
		fu.createFolder("/Move/Conflict12", userId);
		
		// 웹에서 이동
		String folderName = "Conflict12";
		String parentFolder = "Move";
		WebUtil.moveFolder(folderName, parentFolder, driver);
		
		// 동기화 
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict12"));
		assertTrue(list.contains("(conflicted)Conflict12"));
		
	}
	
	@Test
	public void renameAtPC_createAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("Conflict", userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 이름 변경
		String oldName = "Conflict";
		String newName = "Conflict13";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 폴더 생성
		WebUtil.createFolder("Conflict13", driver);
		
		// 동기화 
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict13"));
		assertTrue(list.contains("(conflicted)Conflict13"));
		
	}
	
	@Test
	public void renameAtPC_moveAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Move/Conflict", userId);
		fu.createFolder("Conflict14", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 이름 변경
		String oldName = "/Move/Conflict";
		String newName = "/Move/Conflict14";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 이동
		String folderName = "Conflict14";
		String parentFolder = "Move";
		WebUtil.moveFolder(folderName, parentFolder, driver);

		// 동기화 
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Move"));
		assertFalse(list.contains("Conflict14"));
		
		list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict14"));
		assertTrue(list.contains("(conflicted)Conflict14"));
		
	}
	
	@Test
	public void moveAtPC_createAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("Conflict15", userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 폴더 이동
		File srcDir = new File("Conflict15");
		File dstDir = new File("Move");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 Move 하위에 폴더 생성
		WebUtil.navigateToFolder("Move", driver);
		Thread.sleep(1 * 1000);
		WebUtil.createFolder("Conflict15", driver);
		
		// 동기화 
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict15"));
		assertTrue(list.contains("Conflict15 (1)"));
		
	}
	
	@Test
	public void moveAtPC_renameAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화
		fu.createFolder("/Move/Conflict", userId);
		fu.createFolder("Conflict16", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 폴더 이동
		File srcDir = new File("Conflict16");
		File dstDir = new File("Move");
		fu.moveDirectoryToDirectory(srcDir, dstDir, userId);
		
		// 웹에서 이름 변경
		Thread.sleep(1 * 1000);
		WebUtil.navigateToFolder("Move", driver);
		String oldName = "Conflict";
		String newName = "Conflict16";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화 
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict16"));
		assertTrue(list.contains("Conflict16 (1)"));
				
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
