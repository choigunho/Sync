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
	public void renameAtPC_renameAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);	
		
		// pc에서 이름 변경
		String oldName = "Conflict.pptx";
		String newName = "Conflict_PC.pptx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 이름 변경
		oldName = "Conflict.pptx";
		newName = "Conflict_WEB";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Conflict_PC.pptx"));
		assertTrue(list.contains("Conflict_WEB.pptx"));
		
	}
	
	@Test
	public void renameAtPC_moveAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);	
		
		// pc에서 이름 변경
		String oldName = "Conflict.pptx";
		String newName = "Conflict_PC.pptx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 이동
		String fileName = "Conflict.pptx";
		String dstFolder = "Move";
		WebUtil.moveToFolder(fileName, dstFolder, driver);
		
		// 동기화
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Move"));
		assertTrue(list.contains("(conflicted)Conflict_PC.pptx"));
		
		list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict.pptx"));
		
	}
	
	@Test @Ignore
	public void renameAtPC_modifyAtWEB() throws Exception {
		
	}
	
	@Test
	public void renameAtPC_deleteAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);	
		
		// pc에서 이름 변경
		String oldName = "Conflict.pptx";
		String newName = "Conflict_PC.pptx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict.pptx", driver);
		
		// 동기화 
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("(conflicted)Conflict_PC.pptx"));
		
	}
	
	@Test @Ignore
	public void modifyAtPC_renameAtWEB() throws Exception {
		
	}
	
	@Test @Ignore
	public void modifyAtPC_moveAtWEB() throws Exception {
		
	}
	
	@Test @Ignore
	public void modifyAtPC_modifyAtWEB() throws Exception {
		
	}
	
	@Test @Ignore
	public void modifyAtPC_deleteAtWEB() throws Exception {
		
	}
	
	@Test
	public void moveAtPC_renameAtWEB() throws Exception {
	
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 파일 이동
		File src = new File("Conflict.pptx");
		File dstDir = new File("Move");
		fu.moveFileToDirectory(src, dstDir, userId);
		
		// 웹에서 이름 변경
		String oldName = "Conflict.pptx";
		String newName = "Conflict_WEB";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("Move"));
		assertTrue(list.contains("Conflict_WEB.pptx"));
		
		list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("(conflicted)Conflict.pptx"));
		
	}
	
	@Test
	public void moveAtPC_moveAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("MovePC", userId);
		fu.createFolder("MoveWEB", userId);
		WebUtil.refreshUntil60Seconds(3, driver);
		
		// pc에서 파일 이동
		File src = new File("Conflict.pptx");
		File dstDir = new File("MovePC");
		fu.moveFileToDirectory(src, dstDir, userId);
		
		// 웹에서 파일 이동
		String fileName = "Conflict.pptx";
		String dstFolder = "MoveWEB";
		WebUtil.moveToFolder(fileName, dstFolder, driver);
		
		// 동기화
		File targetDir = new File("/MoveWEB");
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/movePC", userId);
		assertTrue(list.contains("(conflicted)Conflict.pptx"));
		
		list = fu.getFileList("/moveWEB", userId);
		assertTrue(list.contains("Conflict.pptx"));
		
	}
	
	@Test
	public void moveAtPC_deleteAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 파일 이동
		File src = new File("Conflict.pptx");
		File dstDir = new File("Move");
		fu.moveFileToDirectory(src, dstDir, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict.pptx", driver);
		
		// 동기화
		Thread.sleep(1 * 1000);
		Thread.sleep(1 * 1000);
		WebUtil.navigateToFolder("Move", driver);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("(conflicted)Conflict.pptx"));
		
	}
	
	@Test
	public void deleteAtPC_renameAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);	
		
		// pc에서 파일 삭제
		String fileName = "Conflict.pptx";
		fu.forceDelete(fileName, userId);
		
		// 웹에서 이름 변경
		String oldName = "Conflict.pptx";
		String newName = "Conflict_WEB";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화
		File targetDir = new File(File.separator);
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertEquals("Conflict_WEB.pptx", list.get(0));
		
	}
	
	@Test
	public void deleteAtPC_moveAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 파일 삭제
		String fileName = "Conflict.pptx";
		fu.forceDelete(fileName, userId);
		
		// 웹에서 파일 이동
		String file = "Conflict.pptx";
		String dstFolder = "Move";
		WebUtil.moveToFolder(file, dstFolder, driver);
		
		// 동기화
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(1, userId, targetDir);
		
		// 확인(PC) 
		List<String> list = fu.getFileList("/Move", userId);
		assertEquals("Conflict.pptx", list.get(0));
		
	}
	
	@Test
	public void deleteAtPC_deleteAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);
				
		// pc에서 파일 삭제
		String fileName = "Conflict.pptx";
		fu.forceDelete(fileName, userId);
		
		// 웹에서 삭제
		WebUtil.deleteFolder("Conflict.pptx", driver);
		
		// 동기화
		WebUtil.refreshAfter60Seconds(driver);
		
		// 확인(PC) 
		List<String> list = fu.getFileList(File.separator, userId);
		assertFalse(list.contains("Conflict.pptx"));
				
	}
	
	@After
	public void tearDown() throws Exception {

		// 동기화 폴더 초기화
		System.out.println("============동기화 폴더 초기화 시작============");
		fu.cleanDirectory(userId);
		WebUtil.refreshUntil60Seconds(0, driver);
		
		//Close the browser
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
	
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		
	}
}