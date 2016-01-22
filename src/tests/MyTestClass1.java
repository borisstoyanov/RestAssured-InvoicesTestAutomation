package tests;

import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

public class MyTestClass1 {

	@BeforeGroups (groups = { "2.4.1.1" })
	public void beforeGroups(){
		System.out.println("BeforeGroups");
	}
	
	

	@Test(groups = { "2.4.1.1" })
	public void myTestMethod1() {
		System.out.println("myTestMethod1");
	}

	@Test(groups = { "2.4.1.1" })
	public void myTestMethod2() {
		System.out.println("myTestMethod2");
	}
}