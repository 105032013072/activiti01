package com.bosssoft.install.activiti.cache;

public class TestMain {
	public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        UserService userService = proxy.getProxy(UserService.class);
        userService.loadUserByUserName("大明二代");
   }
}
