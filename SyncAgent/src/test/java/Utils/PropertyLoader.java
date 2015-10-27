package Utils;

import java.util.Iterator;
import java.util.ResourceBundle;

import org.junit.Test;

public class PropertyLoader {

	@Test
	public void test() {
		
		ResourceBundle rb = ResourceBundle.getBundle("UserSetting");
		
		Iterator<String> it = rb.keySet().iterator();
		while(it.hasNext()) {
			String key = it.next();
			String value = rb.getString(key);
			System.out.println(key + "=" + value);
		}
	}
	
	@Test
	public void test2() {
//		System.out.println(AccountUtil.getServerUrl());
		System.out.println(AccountUtil.getUserId());
	}
}
