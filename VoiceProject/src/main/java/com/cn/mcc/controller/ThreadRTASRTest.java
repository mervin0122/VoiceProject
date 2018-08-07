package com.cn.mcc.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cn.mcc.utils.voice.DraftWithOrigin;
import com.cn.mcc.utils.voice.EncryptUtil;
import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * 实时转写调用demo
 * 
 * @author white
 *
 */
public class ThreadRTASRTest  extends Thread{

    int threadId = 0;

    // appid 33 5b31b662
    private static final String APPID = "5b31b662";

    // appid对应的secret_key 44 63fda9ad0b5f0756407da547758a9150
    private static final String SECRET_KEY = "63fda9ad0b5f0756407da547758a9150";

    // 请求地址  rtasr.xfyun.cn/v1/ws
    private static final String HOST = "rtasr.xfyun.cn/v1/ws";

    private static final String BASE_URL = "ws://" + HOST;

    private static final String ORIGIN = "http://" + HOST;

    // 音频文件路径
    private static final String AUDIO_PATH = "msc/test_1.pcm";

    private static  String txts="";

    // 每次发送的数据大小 1280 字节
    private static final int CHUNCKED_SIZE = 1280;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS");

    public void run(){

        try {
            super.run();
            URI url = new URI(BASE_URL + getHandShakeParams());
            DraftWithOrigin draft = new DraftWithOrigin(ORIGIN);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            MyWebSocketClient client = new MyWebSocketClient(url, draft, countDownLatch);

            client.connect();

            while (!client.getReadyState().equals(READYSTATE.OPEN)) {
                System.out.println(getCurrentTimeStr() + "\t连接中");
                Thread.sleep(1000);
            }

            // 发送音频
            byte[] bytes = new byte[CHUNCKED_SIZE];
            try (RandomAccessFile raf = new RandomAccessFile(AUDIO_PATH, "r")) {
                int len = -1;
                while ((len = raf.read(bytes)) != -1) {
                    if (len < CHUNCKED_SIZE) {
                        send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
                        break;
                    }
                    send(client, bytes);

                    // 每隔40毫秒发送一次数据
                    Thread.sleep(40);

                }
                // 发送结束标识
                send(client, "{\"end\": true}".getBytes());
                System.out.println(getCurrentTimeStr() + "\t发送结束标识完成");

            } catch (Exception e) {
                e.printStackTrace();
            }

            // 等待连接关闭
            countDownLatch.await();
            System.out.println("===============" + txts);
            System.out.println(threadId + "  " + txts);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
      /*  for (int i = 0; i < 5; i++) {
            ThreadRTASRTest thread = new ThreadRTASRTest();
            thread.threadId = i;
            thread.start();
            try {
                Thread.currentThread().sleep(8000);
            } catch (Exception e) {
            }
        }*/
        java.util.concurrent.ThreadPoolExecutor threadPoolExecutor =
                new ThreadPoolExecutor(10, //corePoolSize 核心线程数
                        10, //maximumPoolSize 最大线程数
                        100, //keepAliveTime 线程池中超过corePoolSize数目的空闲线程最大存活时间；
                        // TimeUnit keepAliveTime时间单位
                        TimeUnit.SECONDS,
                        //workQueue 阻塞任务队列
                        new LinkedBlockingDeque<>(100),
                        //threadFactory 新建线程的工厂
                        Executors.defaultThreadFactory(),
                        //RejectedExecutionHandler当提交任务数超过maxmumPoolSize+workQueue之和时，
                        // 任务会交给RejectedExecutionHandler来处理
                        new ThreadPoolExecutor.AbortPolicy()
                );


       // for (int i = 0; i < 2; i++) {

            threadPoolExecutor.submit(new Runnable() {
                CountDownLatch countDownLatch = new CountDownLatch(1);
                @Override
                public void run() {
                    try {
                        URI url = new URI(BASE_URL + getHandShakeParams());
                        DraftWithOrigin draft = new DraftWithOrigin(ORIGIN);
                        CountDownLatch countDownLatch = new CountDownLatch(1);
                        MyWebSocketClient client = new MyWebSocketClient(url, draft, countDownLatch);

                        client.connect();

                        while (!client.getReadyState().equals(READYSTATE.OPEN)) {
                            System.out.println(getCurrentTimeStr() + "\t连接中");
                            Thread.sleep(1000);
                        }

                        // 发送音频
                        byte[] bytes = new byte[CHUNCKED_SIZE];
                        try (RandomAccessFile raf = new RandomAccessFile(AUDIO_PATH, "r")) {
                            int len = -1;
                            while ((len = raf.read(bytes)) != -1) {
                                if (len < CHUNCKED_SIZE) {
                                    send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
                                    break;
                                }
                                send(client, bytes);

                                // 每隔40毫秒发送一次数据
                                Thread.sleep(40);

                            }
                            // 发送结束标识
                            send(client, "{\"end\": true}".getBytes());
                            System.out.println(getCurrentTimeStr() + "\t发送结束标识完成");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 等待连接关闭
                        countDownLatch.await();
                        System.out.println("===============" + txts);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });


       // }

        /**
         * 如果不再需要新任务，请适当关闭threadPoolExecutor并拒绝新任务
         */
        threadPoolExecutor.shutdown();
       /* URI url = new URI(BASE_URL + getHandShakeParams());
        DraftWithOrigin draft = new DraftWithOrigin(ORIGIN);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        MyWebSocketClient client = new MyWebSocketClient(url, draft, countDownLatch);

        client.connect();

        while (!client.getReadyState().equals(READYSTATE.OPEN)) {
            System.out.println(getCurrentTimeStr() + "\t连接中");
            Thread.sleep(1000);
        }

        // 发送音频
        byte[] bytes = new byte[CHUNCKED_SIZE];
        try (RandomAccessFile raf = new RandomAccessFile(AUDIO_PATH, "r")) {
            int len = -1;
            while ((len = raf.read(bytes)) != -1) {
                if (len < CHUNCKED_SIZE) {
                    send(client, bytes = Arrays.copyOfRange(bytes, 0, len));
                    break;
                }
                send(client, bytes);

                // 每隔40毫秒发送一次数据
                Thread.sleep(40);

            }
            // 发送结束标识
            send(client,"{\"end\": true}".getBytes());
            System.out.println(getCurrentTimeStr() + "\t发送结束标识完成");

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 等待连接关闭
        countDownLatch.await();
        System.out.println("==============="+txts);*/
    }

    // 生成握手参数
    public static String getHandShakeParams() {
        String ts = System.currentTimeMillis()/1000 + "";
        String signa = "";
        try {
            signa = EncryptUtil.HmacSHA1Encrypt(EncryptUtil.MD5(APPID + ts), SECRET_KEY);
            return "?appid=" + APPID + "&ts=" + ts + "&signa=" + URLEncoder.encode(signa, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void send(WebSocketClient client, byte[] bytes) {
        if (client.isClosed()) {
            throw new RuntimeException("client connect closed!");
        }

        client.send(bytes);
    }

    public static String getCurrentTimeStr() {
        return sdf.format(new Date());
    }

    private static class MyWebSocketClient extends WebSocketClient {

        private CountDownLatch countDownLatch;

        public MyWebSocketClient(URI serverUri, Draft protocolDraft, CountDownLatch countDownLatch) {
            super(serverUri, protocolDraft);
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void onOpen(ServerHandshake handshake) {
            System.out.println(getCurrentTimeStr() + "\t连接建立成功！");
        }

        @Override
        public void onMessage(String msg) {
        	JSONObject msgObj = JSON.parseObject(msg);
            String action = msgObj.getString("action");
            if (Objects.equals("started", action)) {
                // 握手成功
                System.out.println(getCurrentTimeStr() + "\t握手成功！sid: " + msgObj.getString("sid"));
            } else if (Objects.equals("result", action)) {
                // 转写结果
                System.out.println(getCurrentTimeStr() + "\tresult: " + getContent(msgObj.getString("data")));
                txts= getContent(msgObj.getString("data"));
                //System.out.println("result: " + msgObj.getString("data"));
            } else if (Objects.equals("error", action)) {
                // 连接发生错误
                throw new RuntimeException(msg);
            }
        }

        @Override
        public void onError(Exception e) {
            System.out.println(getCurrentTimeStr() + "\t连接发生错误：" + e.getMessage() + ", " + new Date());
            e.printStackTrace();
        }

        @Override
        public void onClose(int arg0, String arg1, boolean arg2) {
            System.out.println(getCurrentTimeStr() + "\t链接关闭");
            countDownLatch.countDown();
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            try {
                System.out.println(getCurrentTimeStr() + "\t服务端返回：" + new String(bytes.array(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    // 把转写结果解析为句子
    private static String getContent(String message) {
        StringBuffer resultBuilder = new StringBuffer();
        try {
			JSONObject messageObj = JSON.parseObject(message);
			JSONObject cn = messageObj.getJSONObject("cn");
			JSONObject st = cn.getJSONObject("st");
			JSONArray rtArr = st.getJSONArray("rt");
			for (int i = 0; i < rtArr.size(); i++) {
				JSONObject rtArrObj = rtArr.getJSONObject(i);
				JSONArray wsArr = rtArrObj.getJSONArray("ws");
				for (int j = 0; j < wsArr.size(); j++) {
					JSONObject wsArrObj = wsArr.getJSONObject(j);
					JSONArray cwArr = wsArrObj.getJSONArray("cw");
					for (int k = 0; k < cwArr.size(); k++) {
						JSONObject cwArrObj = cwArr.getJSONObject(k);
						String wStr = cwArrObj.getString("w");
						resultBuilder.append(wStr);
					}
				}
			} 
		} catch (Exception e) {
			return message;
		}

		return resultBuilder.toString();
    }
}