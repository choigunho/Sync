package Utils;

import static com.jayway.awaitility.Awaitility.await;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

import org.apache.commons.io.FileUtils;

import com.jayway.awaitility.Awaitility;

public class FileUtil {

	public void cleanDirectory(String userId) throws Exception {
		
		File dir = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + "/" );
		FileUtils.cleanDirectory(dir);
	}

	public void copyFile(File srcFile, File destFile, String userId) throws Exception {
		
		File dest = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + destFile);
		FileUtils.copyFile(srcFile, dest);
	}
	
	public void checkCountUntil60Seconds(int respectedCount, String userId, File targetDir) {
		
		Awaitility.setDefaultTimeout(60, TimeUnit.SECONDS);
		Awaitility.setDefaultPollInterval(1, TimeUnit.SECONDS);
		
		try {
			await().until(checkCountCallable(respectedCount, userId, targetDir));
		}catch(Exception e){
			fail("동기화 실패!");
		}
		
	}
	
	public Callable<Boolean> checkCountCallable(final int respectedCount, final String userId, final File targetDir) {
		
		return new Callable<Boolean>() {
			public Boolean call() throws Exception {
				
				File dir = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + targetDir);
				File[] fileList = dir.listFiles();
				//System.out.println("fileList.length: " + fileList.length);
				
				System.out.println("PC 동기화 기다리는 중...");
				
				List<String> list = new ArrayList<String>();
				for(File f: fileList){
					if(!f.getName().equals(".cellwe.sync") && !f.getName().equals("." + userId + ".sync")) {
						list.add(f.getName());
					}
				}
				
				if(list.size() == respectedCount) {
					return true;
				}
				
				return false;
			}
		};
	}
	
	public void copyFileToDirectory(File srcFile, File destDir, String userId) throws Exception {
		
		File dest = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + destDir);
		FileUtils.copyFileToDirectory(srcFile, dest);
	}
	
	public void createFolder(String folderName, String userId) throws Exception {
		
		File dir = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + "/" + folderName);
		FileUtils.forceMkdir(dir);
	}
	
	// 파일&폴더 목록 가져오기
	public List<String> getFileList(String parentDir, String userId) {
		
		File dir = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")" + parentDir);
		File[] fileList = dir.listFiles();

		List<String> list = new ArrayList<String>();
		for(File f: fileList){
			if(!f.getName().equals(".cellwe.sync") && !f.getName().equals("." + userId + ".sync")) {
				list.add(f.getName());
			}
		}
		
		return list;
	}
	
	public void moveDirectoryToDirectory(File srcDir, File dstDir, String userId) throws Exception {
		
		File src = new File(System.getProperty("user.home") + "/MyDrive(" + userId + ")/" + srcDir);
		File dst = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")/" + dstDir);
		FileUtils.moveDirectoryToDirectory(src, dst, false);
	}
	
	public void moveFileToDirectory(File srcFile, File dstDir, String userId) throws Exception {
		
		File src = new File(System.getProperty("user.home") + "/MyDrive(" + userId + ")/" + srcFile);
		File dst = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")/" + dstDir);
		FileUtils.moveFileToDirectory(src, dst, false);
	}
	
	public boolean renameFileDirectory(String oldName, String newName, String userId) {
		
		File oldNm= new File(System.getProperty("user.home") + "/MyDrive(" + userId + ")/" + oldName);
		File newNm = new File(System.getProperty("user.home") + "/MyDrive(" + userId + ")/" + newName);
		
		boolean isRenamed = oldNm.renameTo(newNm);
		
		return isRenamed;
		
	}
	
	public void deleteDirectory(String dirName, String userId) throws Exception {
		
		File dir = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")/" + dirName);
		
		FileUtils.deleteDirectory(dir);
	}
	
	public void forceDelete(String fileName, String userId) throws Exception {
		
		File file = new File(System.getProperty("user.home") + "/MyDrive("+ userId + ")/" + fileName);
		
		FileUtils.forceDelete(file);
	}
}
