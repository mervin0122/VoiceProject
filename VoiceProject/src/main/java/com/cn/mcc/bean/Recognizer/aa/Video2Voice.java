package com.cn.mcc.bean.Recognizer.aa;


import org.apache.log4j.Logger;

import java.io.File;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class Video2Voice {

    private static Logger logger = Logger.getLogger(Video2Voice.class);
    /**
     * 提取视频中的音频文件
     * @param fileName
     * @return 音频文件名
     * @throws Exception
     */
    public static String transform(String fileName) throws Exception {

        File file = new File(fileName);
        if(!file.exists()) {
            logger.error("文件不存在："+fileName);
            throw new RuntimeException("文件不存在："+fileName);
        }

        //讯飞现在支持pcm，wav的语音流文件
        String name = fileName.substring(0, fileName.lastIndexOf(".")) + ".wav";
        logger.info("获取到的音频文件："+name);

        // 提取视频中的音频文件。 根据讯飞要求设置采样率， 位数，
        String cmd = "ffmpeg -i "+ fileName +" -f s16le -ar 16000 "+ name;
        Process process = Runtime.getRuntime().exec(cmd);//执行命令

        //
        InputStreamReader ir = new InputStreamReader(process.getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        //输出结果，需要有这部分代码， 否则不能生产抽取的音频文件
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        process.destroy();
        return name;
    }

    public static void main(String[] args) {
        try {
            transform(args[0]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
