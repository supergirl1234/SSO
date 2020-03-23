package com.lei.db;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*模拟数据库*/
public class MockDB {
    /*记录token*/
    public  static Set<String> tokenSet=new HashSet<>();
    /*保存客户端退出地址,一个token是对应多个退出地址的*/
    public  static Map<String,Set<String>> clientLogoutUrlMap=new HashMap<>();
}
