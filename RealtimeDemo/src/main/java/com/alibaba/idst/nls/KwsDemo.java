package com.alibaba.idst.nls;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.idst.nls.realtime.NlsClient;
import com.alibaba.idst.nls.realtime.NlsFuture;
import com.alibaba.idst.nls.realtime.event.NlsEvent;
import com.alibaba.idst.nls.realtime.protocol.NlsRequest;
import com.alibaba.idst.nls.realtime.protocol.NlsResponse;
import com.alibaba.idst.nls.realtime.utils.KwsMngUtil;

/**
 * kws示例
 * kws 请求处理流程与asr类似，只是在返回结果中以keywords字段返回识别的关键词
 * @author zhishen
 *
 */
public class KwsDemo extends RealtimeAsrDemo {
	private String keywordListId;

	public KwsDemo(String keywordListId,String akId,String akSecret,String appKey) {
		this.keywordListId = keywordListId;
		super.ak_id=akId;
		super.ak_secret=akSecret;
		super.appKey=appKey;
	}
	/**
	 * kws 只需设置如下参数
	 */
	@Override
	protected NlsRequest buildRequest() {
		NlsRequest req = new NlsRequest();
		req.setAppkey(appKey);
		//只支持pcm格式
		req.setFormat(asrSC);
		//只支持16000采样率
		req.setSampleRate(16000);
		// 设置关键词词表id，若不设置此参数，当作asr请求处理
		req.setKeyWordListId(keywordListId);
		req.authorize(ak_id, ak_secret);
		return req;

	}

	@Override
	public void onMessageReceived(NlsEvent e) {
		NlsResponse response = e.getResponse();
		NlsResponse.Result result = response.getResult();
		//有关键词命中时会返回相应关键词，否则不返回keywords，也不返回asr识别结果
		if (result != null && result.getKeywords() != null) {
			List<Map<String, Object>> keywords = result.getKeywords();
			System.out.println("These are the keywords we found:" + JSON.toJSONString(keywords));

		} else {
			logger.info(JSON.toJSONString(response));
		}
	}

	public static void main(String[] args) {
		if (args.length < 3) {
			logger.debug("kwsDemo need params: <app-key> <Id> <Secret> ");
			System.exit(-1);
		}
		String appKey = args[0];
		String akId = args[1];
		String akSecret = args[2];
		InputStream ins=KwsDemo.class.getResourceAsStream("/sound_demo.pcm");
		String request = "{\n" + "    \"keywords\": [\n" + "        {\"keyword\":\"北京\", \"threshold\":0},\n"
				+ "        {\"keyword\":\"天气\", \"threshold\":0}\n" + "    ]\n" + "}";
		String response = KwsMngUtil.create(akId, akSecret, request);
		logger.info(response);
		String keywordListId=(String)JSONPath.read(response, "$.keyword_list_id");
		
		KwsDemo lun = new KwsDemo(keywordListId,akId,akSecret,appKey);
		logger.info("start ....");
		lun.start();
		lun.process(ins);
		lun.shutDown();
	}

}
