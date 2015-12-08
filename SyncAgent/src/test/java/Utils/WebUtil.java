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
	
	public static List<String> getList(WebDriver driver) {
		
		List<String> list = new ArrayList<String>();
		List<WebElement> items = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		
		for(WebElement item: items) {

			WebElement name = item.findElement(By.className("file"));
			list.add(name.getText());
		}
		
		System.out.println(list);
		return list;
	}

	// 1분 후 페이지 새로 고침
	public static void refreshAfter60Seconds(WebDriver driver) throws Exception {
		
		for(int i = 1; i < 61; i++) {
			Thread.sleep(1 * 1000);
			System.out.println("무조건 60초 대기..." + (i) + " Seconds passed");
		}
		
		driver.navigate().refresh();
		Thread.sleep(1 * 1000);
	}
	
	// 1초 간격으로 라인의 개수 확인(60초 동안)
	public static void refreshUntil60Seconds(final int expectedCount, WebDriver driver) throws Exception {
		
		(new WebDriverWait(driver, 60)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				
				d.navigate().refresh();

				List<WebElement> tbody = d.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
				if(tbody.size() == expectedCount){
					return true;
				}
				
				System.out.println("웹 동기화 기다리는 중...");
				return false;
			}
		});
		
		Thread.sleep(1 * 1000);
	}
	
	public static void refreshUntil60Seconds(final String expectedName, WebDriver driver) throws Exception {
		
		(new WebDriverWait(driver, 60)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				
				d.navigate().refresh();

				List<String> list = WebUtil.getFileNameList(d);
				if(list.contains(expectedName)) {
					return true;
				}
				
				System.out.println("웹 동기화 기다리는 중...");
				return false;
			}
		});
		
		Thread.sleep(1 * 1000);
	}
	
	public static void createFolder(String folderName, WebDriver driver) throws Exception {
		
		// 폴더 생성
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("btn_folderadd")).isDisplayed();
			}
		});
		WebElement btn_folderadd = driver.findElement(By.className("btn_folderadd"));
		btn_folderadd.click();
		
		// 텍스트 입력
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.id("inputNewFolder")).isDisplayed();
			}
		});
		WebElement textField = driver.findElement(By.id("inputNewFolder"));
		textField.clear();
		textField.sendKeys(folderName);
		
		// 만들기 버튼 클릭
		WebElement btn = driver.findElement(By.className("btn_bgcolor_bl"));
		btn.click();
		
	}
	
	public static void deleteFolder(String folderName, WebDriver driver) throws Exception {
	
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		for(WebElement item: tbody) {

			WebElement fold = item.findElement(By.className("file"));
			if (fold.getText().equals(folderName)) {
				fold.click();
			}
		}
		
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("icon_ca_w_delete")).isDisplayed();
			}
		});
		WebElement btn_delete= driver.findElement(By.className("icon_ca_w_delete"));
		btn_delete.click();

		WebElement btn_ok = driver.findElement(By.className("button_st6"));
		btn_ok.click();
		
	}
	
	public static void moveFolder(String folderName, String parentFolder, WebDriver driver) throws Exception {
		
		// 아이템(파일or폴더) 클릭
		itemClick(folderName, driver);
		
		// 하단 메뉴에서 이동 클릭
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("icon_ca_w_move")).isDisplayed();
			}
		});
		WebElement btn_move = driver.findElement(By.className("icon_ca_w_move"));
		btn_move.click();
		
		// 이동 경로 설정
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("jqx-tree-dropdown-root")).findElement(By.className("jqx-tree-dropdown")).isDisplayed();
			}
		});
		WebElement popLayerList = driver.findElement(By.className("jqx-tree-dropdown-root")).findElement(By.className("jqx-tree-dropdown"));
		List<WebElement> items = popLayerList.findElements(By.className("jqx-tree-item-li"));
		for(WebElement item: items) {
			
			if(item.getText().equals(parentFolder)) {
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.className("jqx-tree-item")).isDisplayed();
					}
				});
				item.findElement(By.className("jqx-tree-item")).click();;
				
				WebElement ok = driver.findElement(By.id("mngOkBtn"));
				ok.click();
			}
		}
	}

	public static void rename(String oldName, String newName, WebDriver driver) throws Exception {
		
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		for(WebElement item: tbody) {
			
			WebElement fold = item.findElement(By.className("file"));
			if(fold.getText().equals(oldName)) {

				Thread.sleep(1 * 500);
				
				// 컨텍스트 메뉴 클릭
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.className("action_context")).isDisplayed();
					}
				});
				WebElement action_context = item.findElement(By.className("action_context"));
				action_context.click();
				
				Thread.sleep(1 * 1000);
				
				// 이름 변경 레이블 클릭
//				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//					public Boolean apply(WebDriver d) {
//						return d.findElement(By.className("change_name")).isDisplayed();
//					}
//				});
				WebElement change_name = item.findElement(By.className("change_name"));
				change_name.click();
				
				// 텍스트 입력
				try{
					WebElement textField = item.findElement(By.id("inputFilRenm"));	
					textField.clear();
					textField.sendKeys(newName);
				}catch(Exception e){
					WebElement textField = item.findElement(By.id("inputFldRenm"));
					textField.clear();
					textField.sendKeys(newName);
				}
				
				// 저장 버튼 클릭
				List<WebElement> btn = item.findElement(By.className("editing_file")).findElements(By.className("button_st2"));
				btn.get(1).click();
				
			}
		}
	}
	
	public static void navigateToFolder(String dstFolder, WebDriver driver) throws Exception {
		
		List<WebElement> folders = driver.findElements(By.className("fold_name"));
		
		for(WebElement folder: folders) {
//			if(folder.isDisplayed()){
//				System.out.println("folder.isDisplayed()");
//			}
			
			if(folder.getText().equals(dstFolder)) {
				folder.click();
			}
		}
		
		Thread.sleep(1 * 500);
	}
	
	public static void itemClick(String item, WebDriver driver) {
		
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		for(WebElement body: tbody) {
			
			WebElement fold = body.findElement(By.className("file"));
			if(fold.getText().equals(item)) {
				fold.click();
			}
		}
	}
	
}
