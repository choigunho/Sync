package Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
		
		Collections.sort(list);
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
				System.out.println("웹에서 동기화된 파일(폴더): " + tbody.size() + " " + getList(d));
								
				if(tbody.size() == expectedCount){
					return true;
				}
				
				return false;
			}
		});
		
		Thread.sleep(1 * 2000);
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
	
	public static void createManyFolders(int count, WebDriver driver) throws Exception {
		
		for(int i = 0; i < count; i++) {
			String folderName = "new folder " + i;
			createFolder(folderName, driver);
			Thread.sleep(3000);
		}
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
		
		System.out.println("웹에서 새로운 폴더 생성: " + folderName);
	}
	
	public static void deleteFolder(String folderName, WebDriver driver) throws Exception {
	
		// 아이템 클릭
		itemClick(folderName, driver);
		
		// 하단 메뉴에서 삭제 클릭
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("icon_ca_w_delete")).isDisplayed();
			}
		});
		WebElement btn_delete= driver.findElement(By.className("icon_ca_w_delete"));
		btn_delete.click();

		WebElement btn_ok = driver.findElement(By.className("button_st6"));
		btn_ok.click();
		
		System.out.println("웹에서 파일(폴더) 삭제: " + folderName);
		
	}
	
	public static void moveToFolder(String itemName, String parentFolder, WebDriver driver) throws Exception {
	
		// 아이템(파일or폴더) 클릭
		itemClick(itemName, driver);
		
		// 하단 메뉴에서 이동 클릭
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("icon_ca_w_move")).isDisplayed();
			}
		});
		WebElement btn_move = driver.findElement(By.className("icon_ca_w_move"));
		btn_move.click();
		System.out.println("[action log] 이동 버튼 클릭");
		
		Thread.sleep(1 * 1000);
		
		List<WebElement> items = driver.findElements(By.className("jqx-tree-item-li"));
		items.remove(0);
		for(WebElement item: items) {
			if(item.getText().equals(parentFolder)) {
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.className("jqx-tree-item")).isDisplayed();
					}
				});
				item.findElement(By.className("jqx-tree-item")).click();
				
				WebElement ok = driver.findElement(By.id("mngOkBtn"));
				ok.click();
				System.out.println("[action log] 확인 버튼 클릭");
			}
		}
		System.out.println("웹에서 이동: " + itemName + " -> " + parentFolder);
	}
	
	public static void rename(String oldName, String newName, WebDriver driver) throws Exception {
		
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		for(WebElement item: tbody) {
			
			WebElement fold = item.findElement(By.className("file"));
			if(fold.getText().equals(oldName)) {

				Thread.sleep(1 * 1000);
				
				// 컨텍스트 메뉴 클릭
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.className("action_context")).isDisplayed();
					}
				});
				WebElement action_context = item.findElement(By.className("action_context"));
				action_context.click();
				System.out.println("[action log] 더보기 메뉴 열기");
				
				Thread.sleep(2 * 1000);
				
				// 이름 변경 레이블 클릭
				/*
				(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
					public Boolean apply(WebDriver d) {
						return d.findElement(By.className("change_name")).isEnabled();
					}
				});
				*/
				WebElement change_name = item.findElement(By.className("change_name"));
				change_name.click();
				System.out.println("[action log] 이름 변경 클릭");
				
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
				System.out.println("[action log] 저장 버튼 클릭");
				
				System.out.println("웹에서 이름 변경: " + oldName + " -> " + newName);
			}
		}
	}
	
	public static void navigateToFolder(String dstFolder, WebDriver driver) throws Exception {
		
		List<WebElement> folders = driver.findElements(By.className("fold_name"));
		
		for(WebElement folder: folders) {
			if(folder.getText().equals(dstFolder)) {
				folder.click();
				System.out.println("[action log] " + dstFolder + " 폴더 페이지로 이동");
			}
		}
		Thread.sleep(1000);
	}
	
	public static void itemClick(String item, WebDriver driver) throws Exception {
		
		List<WebElement> tbody = driver.findElements(By.cssSelector("tbody[sf-virtual-repeat]"));
		for(WebElement body: tbody) {
			
			WebElement fold = body.findElement(By.className("file"));
			if(fold.getText().equals(item)) {
				fold.click();
				System.out.println("[action log] " + item + " 클릭");
			}
		}
		Thread.sleep(1000);
	}

	public static void pageDown(int count, WebDriver driver) throws Exception {
		
		// Inbox 부분을 한 번 클릭해야 Page Down키가 동작함
		WebElement subject_line = driver.findElement(By.className("subject_line"));
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className("subject_line")).isDisplayed();
			}
		});
		subject_line.click();
		
		// 페이지 다운
		Actions action = new Actions(driver);
		for (int i=0; i<count; i++) {
			action.sendKeys(Keys.PAGE_DOWN).perform();
			Thread.sleep(1 * 1000);	
		}
	}
	
	public static void navigateToHome(WebDriver driver) {
		
		driver.get(AccountUtil.getServerUrl());
		
	}
}
