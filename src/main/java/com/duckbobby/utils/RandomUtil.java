package com.duckbobby.utils;

import java.util.Random;

/**
 * 生成【min,max】的随机数
 * @author Administrator
 *
 */
public class RandomUtil {

	public static Integer random(Integer min, Integer max){
		Random random = new Random();
        Integer ran = random.nextInt(max - min + 1) + min;
        return ran;
	}
	//随机英文+数字昵称
    public static String getStringRandom(int length) {  
          
        String val = "";  
        Random random = new Random();  
          
        //参数length，表示生成几位随机数  
        for(int i = 0; i < length; i++) {  
              
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";  
            //输出字母还是数字  
            if( "char".equalsIgnoreCase(charOrNum) ) {  
                //输出是大写字母还是小写字母  
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;  
                val += (char)(random.nextInt(26) + temp);  
            } else if( "num".equalsIgnoreCase(charOrNum) ) {  
                val += String.valueOf(random.nextInt(10));  
            }  
        }  
        return val;  
    }  
}
