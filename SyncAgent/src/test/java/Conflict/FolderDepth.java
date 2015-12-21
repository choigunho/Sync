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
public class FolderDepth {
	
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
	
	@Ignore("정책 미정") @Test
	public void createSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화
		fu.createFolder("/Folder", userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 하위 폴더 생성
		fu.createFolder("/Folder/Conflict17", userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteFolder("Folder", driver);
		
		// 확인
		// to do
		
		
	}
	
	@Ignore("정책 미정") @Test
	public void renameSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화
		fu.createFolder("/Folder/Conflict18", userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 하위 폴더 이름 변경
		fu.renameFileDirectory("/Folder/Conflict18", "/Folder/Conflict18_NEW", userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteFolder("Folder", driver);
		
		// 확인
		// to do
		
		
	}
	
	@Ignore("정책 미정") @Test
	public void moveSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화
		fu.createFolder("/Folder", userId);
		fu.createFolder("/Conflict19", userId);
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// pc에서 폴더 이동
		
		
	}
	
	@Ignore("정책 미정") @Test
	public void deleteSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
	}
	
	@Ignore("정책 미정") @Test
	public void deleteParentFolderAtPC_createSubFolderAtWEB() throws Exception {
		
	}
	
	@Ignore("정책 미정") @Test
	public void deleteParentFolderAtPC_renameSubFolderAtWEB() throws Exception {
		
	}
	
	@Ignore("정책 미정") @Test
	public void deleteParentFolderAtPC_moveSubFolderAtWEB() throws Exception {
		
	}
	
	@Ignore("정책 미정") @Test
	public void deleteParentFolderAtPC_deleteSubFolderAtWEB() throws Exception {
		
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