package Conflict;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@Ignore("기대결과 미정") 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FolderFileDepth {
	
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
	public void createFileAtPC_deleteFolderAtWEB() throws Exception {
		
		// 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		fu.createFolder(parentFolder, userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 파일 생성		
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(parentFolder);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
	}
	
	@Test
	public void renameFileAtPC_deleteFolderAtWEB() throws Exception {
		
		// 파일 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		File destDir = new File(parentFolder);
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 파일 이름 변경
		String oldName = parentFolder + "/Conflict.pptx";
		String newName = parentFolder + "/Conflict_NEW.pptx";
		fu.renameFileDirectory(oldName, newName, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.navigateToHome(driver);
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
		
		
	}
	
	@Test
	public void moveFileAtPC_deleteFolderAtWEB() throws Exception {
		
		// 파일, 폴더 생성 후 동기화
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		String parentFolder = "/Folder";
		fu.createFolder(parentFolder, userId);
		
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// pc에서 파일 이동
		String file = "/Conflict.pptx";
		fu.moveFileToDirectory(file, parentFolder, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
		
	}
	
	@Test
	public void deleteFileAtPC_deleteFolderAtWEB() throws Exception {
		
		// 파일 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		File destDir = new File(parentFolder);
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + "/Conflict.pptx");
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 파일 삭제
		fu.forceDelete(parentFolder + "/Conflict.pptx", userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.navigateToHome(driver);
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
		
	}
	
	@Ignore("웹에서 파일 추가 유틸 함수 미구현") @Test
	public void deleteFolderAtPC_createFileAtWEB() throws Exception {
		
	}
	
	@Test
	public void deleteFolderAtPC_renameFileAtWEB() throws Exception {
	
		// 파일 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		File destDir = new File(parentFolder);
		String fileName = "/Conflict.pptx";
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + fileName);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 상위 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 파일 이름 변경
		WebUtil.rename(fileName, fileName + "_NEW", driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
	}
	
	@Test
	public void deleteFolderAtPC_moveFileAtWEB() throws Exception {
		
		// 파일, 폴더 생성 후 동기화
		String fileName = "/Conflict.pptx";
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + fileName);
		File destDir = new File(File.separator);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		String parentFolder = "/Folder";
		fu.createFolder(parentFolder, userId);
		
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// pc에서 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 파일 이동
		WebUtil.moveToFolder(fileName, parentFolder, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
		
	}
	
	@Test
	public void deleteFolderAtPC_deleteFileAtWEB() throws Exception {
		
		// 파일 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		File destDir = new File(parentFolder);
		String fileName = "/Conflict.pptx";
		File srcFile = new File(this.getClass().getResource("/TestFiles").getFile() + fileName);
		fu.copyFileToDirectory(srcFile, destDir, userId);
		
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 상위 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 파일 삭제
		WebUtil.deleteItem(fileName, driver);
		
		// 확인
		// to do
		Thread.sleep(10 * 1000);
		
		
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