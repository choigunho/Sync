package Conflict;

import org.openqa.selenium.WebDriver;
import org.junit.*;
import org.junit.runners.MethodSorters;

import Utils.AccountUtil;
import Utils.FileUtil;
import Utils.WebUtil;
import static org.junit.Assert.*;

@Ignore("기대결과 미정") 
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FoldersDepth {
	
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
	public void createSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		fu.createFolder(parentFolder, userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 폴더 생성
		String subFolder = "/Conflict17";
		fu.createFolder(parentFolder + subFolder, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		//Thread.sleep(60 * 1000);
		
	}
	
	@Test
	public void renameSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFolder = "/Conflict18";
		fu.createFolder(parentFolder + subFolder, userId);

		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 하위 폴더 이름 변경
		String subFolder_NEW = "/Conflict18_NEW";
		fu.renameFileDirectory(parentFolder + subFolder, parentFolder + subFolder_NEW, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.navigateToHome(driver);
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		//Thread.sleep(60 * 1000);
		
	}
	
	@Test
	public void moveSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFolder = "/Conflict19";
		
		fu.createFolder(parentFolder, userId);
		fu.createFolder(subFolder, userId);
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// pc에서 폴더 이동
		fu.moveDirectoryToDirectory(subFolder, parentFolder, userId);
		
		// web에서 상위 폴더 삭제
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		
		
	}
	
	@Test
	public void deleteSubFolderAtPC_deleteParentFolderAtWEB() throws Exception {
	
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFolder = "/conflict20";
		fu.createFolder(parentFolder + subFolder, userId);
		
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 폴더 삭제
		fu.deleteDirectory(parentFolder + subFolder, subFolder);
		
		// web에서 상위 폴더 삭제
		WebUtil.navigateToHome(driver);
		WebUtil.deleteItem(parentFolder, driver);
		
		// 확인
		// to do
		
	}
	
	@Test
	public void deleteParentFolderAtPC_createSubFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		fu.createFolder(parentFolder, userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 상위 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 하위 폴더 생성
		WebUtil.navigateToFolder(parentFolder, driver);
		String subFolder = "conflict21";
		WebUtil.createFolder(subFolder, driver);
		
		// 확인
		// to do
		
	}
	
	@Test
	public void deleteParentFolderAtPC_renameSubFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFolder = "/conflict22";
		fu.createFolder(parentFolder + subFolder, userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 상위 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 하위 폴더 이름 변경
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.rename(subFolder, subFolder + "_New", driver);
		
		// 확인
		// to do
		// Thread.sleep(60 * 1000);
		
		
	}
	
	@Test
	public void deleteParentFolderAtPC_moveSubFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFoler = "/conflict23";
		
		fu.createFolder(parentFolder, userId);
		fu.createFolder(subFoler, userId);
		WebUtil.refreshUntil90Seconds(2, driver);
		
		// pc에서 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 폴더 이동
		WebUtil.moveToFolder(subFoler, parentFolder, driver);
		
		// 확인
		// to do
		// Thread.sleep(10 * 1000);
		
		
	}
	
	@Test
	public void deleteParentFolderAtPC_deleteSubFolderAtWEB() throws Exception {
		
		// 동기화 폴더 생성 후 동기화(사전 조건)
		String parentFolder = "/Folder";
		String subFolder = "/conflict24";
		fu.createFolder(parentFolder + subFolder, userId);
		WebUtil.refreshUntil90Seconds(1, driver);
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.refreshUntil90Seconds(1, driver);
		
		// pc에서 상위 폴더 삭제
		fu.deleteDirectory(parentFolder, userId);
		
		// web에서 하위 폴더 삭제
		WebUtil.navigateToFolder(parentFolder, driver);
		WebUtil.deleteItem(subFolder, driver);
		
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