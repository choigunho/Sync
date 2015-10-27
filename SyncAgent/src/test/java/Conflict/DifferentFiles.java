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
public class DifferentFiles {
	
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
	public void createAtPC_renameAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 파일 생성
		srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict_PC.docx");
		destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// 웹에서 이름 변경
		String oldName = "Conflict.docx";
		String newName = "Conflict_PC";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// 확인
		List<String> list = fu.getFileList(File.separator, userId);
		assertTrue(list.contains("(conflicted)Conflict_PC.docx"));
		assertTrue(list.contains("Conflict_PC.docx"));
		
	}
	
	@Test
	public void createAtPC_moveAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 파일 생성
		srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.docx");
		destDir = new File("/Move");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// 웹에서 이동
		String fileName = "Conflict.docx";
		String dstFolder = "Move";
		WebUtil.moveFolder(fileName, dstFolder, driver);
		
		// 동기화
		Thread.sleep(1 * 1000);
		Thread.sleep(1 * 1000);
		WebUtil.navigateToFolder("Move", driver);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// 확인
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("(conflicted)Conflict.docx"));
		assertTrue(list.contains("Conflict.docx"));
		
	}
	
	@Test
	public void renameAtPC_moveAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict_PC.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.docx");
		destDir = new File("/Move");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil60Seconds(2, driver);
		WebUtil.navigateToFolder("Move", driver);
		WebUtil.refreshUntil60Seconds(1, driver);
		driver.navigate().back();
		driver.navigate().refresh();
		Thread.sleep(1 * 1000);
		
		// pc에서 이름 변경
		String oldName = "/Move/Conflict.docx";
		String newName = "/Move/Conflict_PC.docx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 이동
		String fileName = "Conflict_PC.docx";
		String dstFolder = "Move";
		WebUtil.moveFolder(fileName, dstFolder, driver);
		
		// 동기화
		File targetDir = new File("/Move");
		fu.checkCountUntil60Seconds(2, userId, targetDir);
		
		// 확인
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("(conflicted)Conflict_PC.docx"));
		assertTrue(list.contains("Conflict_PC.docx"));

	}
	
	@Test
	public void moveAtPC_renameAtWEB() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict_PC.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.docx");
		destDir = new File("/Move");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil60Seconds(2, driver);
		WebUtil.navigateToFolder("Move", driver);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 파일 이동
		File src = new File("Conflict_PC.docx");
		File dstDir = new File("Move");
		fu.moveFileToDirectory(src, dstDir, userId);
		
		// 웹에서 이름 변경
		String oldName = "Conflict.docx";
		String newName = "Conflict_PC";
		WebUtil.rename(oldName, newName, driver);
		
		// 동기화
		WebUtil.refreshUntil60Seconds(2, driver);
	
		// 확인
		List<String> list = fu.getFileList("/Move", userId);
		assertTrue(list.contains("Conflict_PC.docx"));
		assertTrue(list.contains("Conflict_PC (1).docx"));
				
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