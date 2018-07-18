package com.alibaba.idst.nls;

import com.alibaba.fastjson.JSON;
import com.alibaba.idst.nls.realtime.NlsClient;
import com.alibaba.idst.nls.realtime.NlsFuture;
import com.alibaba.idst.nls.realtime.event.NlsEvent;
import com.alibaba.idst.nls.realtime.event.NlsListener;
import com.alibaba.idst.nls.realtime.protocol.NlsRequest;
import com.alibaba.idst.nls.realtime.protocol.NlsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * asr 示例
 * 
 * @author zhishen
 *
 */
public class RealtimeAsrDemo implements NlsListener {
	protected NlsClient client = new NlsClient();
	protected static final String asrSC = "pcm";

	static Logger logger = LoggerFactory.getLogger(RealtimeAsrDemo.class);
	public String filePath = "src/main/resources/sound_demo.pcm";
	public String appKey = "nls-service-shurufa16khz";
	protected String ak_id = "LTAICfPYT9c38HWA";
	protected String ak_secret = "IjVOsezGbbEdoZnnwxlmYG6NFZlMWY";

	public RealtimeAsrDemo() {
	}

	public void shutDown() {
		logger.debug("close NLS client manually!");
		client.close();
		logger.debug("demo done");
	}

	public void start() {
		logger.debug("init Nls client...");
		client.init();
	}

	public void process() {
		logger.debug("open audio file...");
		FileInputStream fis = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
		} catch (Exception e) {
			logger.error("fail to open file", e);
		}
		if (fis != null) {
			logger.debug("create NLS future");
			process(fis);
			logger.debug("calling NLS service end");
		}
	}

	public void process(InputStream ins) {
		try {
			NlsRequest req = buildRequest();
			NlsFuture future = client.createNlsFuture(req, this);
			logger.debug("call NLS service");
			byte[] b = new byte[8000];
			int len = 0;
			while ((len = ins.read(b)) > 0) {
				future.sendVoice(b, 0, len);
				Thread.sleep(200);
			}
			logger.debug("send finish signal!");
			future.sendFinishSignal();

			logger.debug("main thread enter waiting .");
			future.await(100000);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected NlsRequest buildRequest() {
		NlsRequest req = new NlsRequest();
		req.setAppkey(appKey);
		req.setFormat(asrSC);
		req.setResponseMode("streaming");
		req.setSampleRate(16000);
		// 用户根据[热词文档](~~49179~~) 设置自定义热词。
		// 通过设置VocabularyId调用热词。
		 req.setVocabularyId("天气");
		// 设置关键词库ID 使用时请修改为自定义的词库ID
		 req.setKeyWordListId("c1391f1c1f1b4002936893c6d97592f3");
		// the id and the id secret
		req.authorize(ak_id, ak_secret);
		return req;

	}

	@Override
	public void onMessageReceived(NlsEvent e) {
		NlsResponse response = e.getResponse();
		response.getFinish();
		if (response.result != null) {
			System.out.println(response.getResult());
			logger.debug("status code = {},get finish is {},get recognize result: {}", response.getStatusCode(),
					response.getFinish(), response.getResult());
			if (response.getQuality() != null) {
				logger.info("Sentence {} is over. Get ended sentence recognize result: {}, voice quality is {}",
						response.result.getSentence_id(), response.getResult(),
						JSON.toJSONString(response.getQuality()));
			}
		} else {
			logger.info(JSON.toJSONString(response));
		}
	}

	@Override
	public void onOperationFailed(NlsEvent e) {
		logger.error("status code is {}, on operation failed: {}", e.getResponse().getStatusCode(),
				e.getErrorMessage());

	}

	@Override
	public void onChannelClosed(NlsEvent e) {
		logger.debug("on websocket closed.");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RealtimeAsrDemo lun = new RealtimeAsrDemo();

		logger.info("start ....");
		if (args.length < 4) {
			logger.info("RealtimeDemo need params: <app-key> <Id> <Secret> <audio-file> ");
			System.exit(-1);
		}
		lun.appKey = args[0];
		lun.ak_id = args[1];
		lun.ak_secret = args[2];
		lun.filePath = args[3];
		lun.start();
		lun.process();
		lun.shutDown();
	}

}
