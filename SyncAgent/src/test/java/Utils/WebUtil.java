package Utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebUtil {

	public static int countFileAndFolders(WebDriver driver) {
		
		// 동기화 확인
		List<WebElement> links = driver.findElements(By.className("subject_line"));
		return links.size() - 2;
	}
	
	public static List<String> getFileNameList(WebDriver driver) {
		
		List<String> list = new ArrayList<String>();
		
		List<WebElement> items = driver.findElements(By.className("subject_line"));
		
		for(WebElement item: items) {
			list.add(item.getText());
		}
		
		return list;
	}

	// 1분 후 페이지 새로 고침
	public static void refreshAfter60Seconds(WebDriver driver) throws Exception {
		
		for(int i = 1; i < 61; i++) {
			Thread.sleep(1 * 1000);
			System.out.println("무조건 60초 대기..." + (i) + " Seconds passed");
		}
		driver.navigate().refresh();
		
	}
	
	// 1초 간격으로 폴더+파일 목록수 확인(60초 동안)
	public static void refreshUntil60Seconds(final int expectedCount, WebDriver driver) throws Exception {
		
		(new WebDriverWait(driver, 60)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				
				d.navigate().refresh();
				List<WebElement> links = d.findElements(By.className("subject_line"));
				
				System.out.println("웹 동기화 기다리는 중...");
				
				if(links.size() - 2 == expectedCount){
					return true;
				}
				
				return false;
			}
		});
	}
	
	public static void createFolder(String folderName, WebDriver driver) throws Exception {
		
		// 폴더 생성
		List<WebElement> folderadd = driver.findElements(By.className("etc_service"));
		folderadd.get(0).findElements(By.className("set_add")).get(1).click();
		
		Thread.sleep(1000);
		
		// 텍스트 입력
		WebElement textField = driver.findElement(By.id("inputNewFolder"));
		textField.clear();
		textField.sendKeys(folderName);
		
		// 만들기 버튼 클릭
		WebElement btn = driver.findElement(By.className("btn_bgcolor_bl"));
		btn.click();
		
	}
	
	public static void deleteFolder(String folderName, WebDriver driver) throws Exception {
	
		WebElement we = driver.findElement(By.className("drive_scroll_com"));
		Thread.sleep(1 * 1000);
		
		List<WebElement> subjectItems = we.findElements(By.className("subject_line"));
		for(WebElement item: subjectItems) {
			if (item.getText().equals(folderName)) {
				
				item.click();
				
			}
		}
		
		WebElement btn_delete= driver.findElement(By.className("icon_ca_w_delete"));
		btn_delete.click();
//		Thread.sleep(1 * 1000);
		
		WebElement btn_ok = driver.findElement(By.className("button_st6"));
		btn_ok.click();
		
	}
	
	public static void moveFolder(String folderName, String parentFolder, WebDriver driver) throws Exception {
		
		WebElement we = driver.findElement(By.className("drive_scroll_com"));
		
		Thread.sleep(1 * 1000);
		
		List<WebElement> subjectItems = we.findElements(By.className("subject_line"));
		for(WebElement item: subjectItems) {
			if (item.getText().equals(folderName)) {
				
				item.click();
				
				WebElement btn_move = driver.findElement(By.className("icon_ca_w_move"));
				btn_move.click();
				
			}
		}
		
		Thread.sleep(1 * 1000);
		
		WebElement popLayerList = driver.findElement(By.className("jqx-tree-dropdown-root")).findElement(By.className("jqx-tree-dropdown"));
		
		List<WebElement> items = popLayerList.findElements(By.className("jqx-tree-item-li"));
		for(WebElement item: items) {
			
			if(item.getText().equals(parentFolder)) {
				item.findElement(By.className("jqx-tree-item")).click();;
				
//				Thread.sleep(1 * 1000);
				
				WebElement ok = driver.findElement(By.id("mngOkBtn"));
				ok.click();
			}
		}
		
	}
	
	public static void rename(String oldName, String newName, WebDriver driver) throws Exception {
		
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		
		for(WebElement item: tbody) {
			
			WebElement fold = item.findElement(By.className("fold_name"));
			if(fold.getText().equals(oldName)) {

//				Thread.sleep(1 * 500);
				
				// 컨텍스트 메뉴 클릭
				WebElement action_context = item.findElement(By.className("action_context"));
				action_context.click();
				
				Thread.sleep(1 * 500);
				
				// 이름 변경 레이블 클릭
				WebElement change_name = item.findElement(By.className("change_name"));
				change_name.click();
				
//				Thread.sleep(1 * 500);
				
				// 텍스트 입력
				WebElement textField = item.findElement(By.id("inputFldRenm"));
				textField.clear();
				textField.sendKeys(newName);
				
				// 저장 버튼 클릭
				List<WebElement> btn = item.findElement(By.className("editing_file")).findElements(By.className("button_st2"));
				btn.get(1).click();
			}
		}
	}
	
	public static void navigateToFolder(String dstFolder, WebDriver driver) throws Exception {
		
		List<WebElement> folds = driver.findElements(By.className("fold_name"));
//		Thread.sleep(1 * 1000);
		
		for(WebElement fold: folds) {
			if(fold.getText().equals(dstFolder)) {
				fold.click();
			}
		}
	}
	
}
