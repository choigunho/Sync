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
public class FileSync {
	
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

		// pc에서 파일 생성
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// 웹에서 동기화 확인
		WebUtil.refreshUntil60Seconds(1, driver);
		List<String> list = WebUtil.getFileNameList(driver);
		assertTrue("웹에서 파일 동기화 실패!", list.contains("File_DOCX.docx"));
		
	}
	
	@Test
	public void renameAtPC() throws Exception {
				
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 파일명 변경
		String oldName = "File_DOCX.docx";
		String newName = "File_DOCX_renamed.docx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// 웹에서 이름변경 확인
		WebUtil.refreshUntil60Seconds("File_DOCX_renamed.docx", driver);
		
	}
	
	@Test
	public void deleteAtPC() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// pc에서 파일 삭제
		String fileName = "File_DOCX.docx";
		fu.forceDelete(fileName, userId);
		
		// 웹에서 삭제 확인
		WebUtil.refreshUntil60Seconds(0, driver);
		
	}
	
	@Test
	public void moveAtPC() throws Exception {
		
		// 동기화 폴더에 파일 복사 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/File_DOCX.docx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		fu.createFolder("/Move", userId);
		WebUtil.refreshUntil60Seconds(2, driver);
		
		// pc에서 파일 이동
		File src = new File("File_DOCX.docx");
		File dstDir = new File("Move");
		fu.moveFileToDirectory(src, dstDir, userId);
		
		WebUtil.navigateToFolder("Move", driver);
		WebUtil.refreshUntil60Seconds(1, driver);
		
		// 웹에서 동기화 확인
		List<String> list = WebUtil.getList(driver);
		assertTrue("웹에서 파일 동기화 실패!", list.contains("File_DOCX.docx"));
		
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