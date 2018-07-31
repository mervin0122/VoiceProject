package com.cn.mcc.bean.Recognizer.aa;

import java.util.Scanner;


/**
 * @desc:
 * @auth: zona
 * 2017年1月4日 上午10:08:21
 */
public class Test {

    private static String fileName = "1481023006148.wav";
    public static void main(String[] args) throws InterruptedException {

        Scanner scanner =new Scanner(System.in);

        MscTest mObject = new MscTest();
        int a = 1;
        boolean flag = true;
        for(int i=0; i<200; i++) {
//			MscTest mObject = new MscTest();
            if(flag) {
                System.out.print("place input num:");
                int num = scanner.nextInt();
                if(num > 10) {
                    flag = false;
                }
            }
            //语音识别， 并给mResult赋值, 在获取音频文件中的文字代码中添加while就是为了确保该行代码执行完成时，
            // 语音识别解析工作已经完成，否则可能获取不到识别结果， 或仅仅是获取到识别结果的一部分
            String result = mObject.voice2words(fileName);
            System.out.println(a+"--->MscTest.main()--"+result);
            a++;
        }
    }
}
