package com.cn.mcc.controller;

import com.baidu.aip.talker.controller.Session;
import com.baidu.aip.talker.facade.ISessionController;
import com.baidu.aip.talker.facade.exception.InitException;
import com.baidu.aip.talker.facade.exception.SendException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class BiccTest {
    /*
     * @param controller Controller 实例
     * @param file  需要识别的文件
     */
    public static void asrOne(ISessionController controller, String file) {
        // createConfig第一个参数RoleId 三选一：AGENT（仅客服录音） CLIENT (仅客户录音）BOTH（2路录音，见asrBoth）
        // createConfig第二个参数isCompress: 是否需要传输压缩数据，仅linux x64可以开启
        Session.Config config = Session.createConfig(Session.Config.RoleId.AGENT, false);
        // 下面一段为params的设置，仅需要最终识别结果，详细见文档，
       /* Map<String, Object> sentenceHandlerParams = new LinkedHashMap<>();
        sentenceHandlerParams.put("agent", "STANDARD_COMPLETED");
        sentenceHandlerParams.put("client", "STANDARD_COMPLETED");
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("sentenceHandler", sentenceHandlerParams);
        config.setParams(params);*/
        config.setAgentDn(123); // 坐席号, 用户自行定义

        try {
            Session session = controller.startSession(config);
            registerShutdown(controller, session); // ctrl C 退出时发送end 包
            asr(session, file, true);

            session.sendEndSpeech();
            session.destroy();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("meet exception to exit");
            System.exit(5);
        }
    }

    public static void asrBoth(final ISessionController controller, final String fileAgent, final String fileCustomer) {
        asrBoth(controller, fileAgent, fileCustomer, false);
    }

    public static void asrBoth(final ISessionController controller, final String fileAgent,
                               final String fileCustomer, boolean skipExit) {
        // createConfig BOTH（2路录音，见asrBoth）
        // createConfig第二个参数isCompress: 是否需要传输压缩数据，仅linux x64可以开启
        final Session.Config config = Session.createConfig(Session.Config.RoleId.BOTH, false);
        try {
            final Session session = controller.startSession(config);
            if (!skipExit) {
                registerShutdown(controller, session);
            }
            Thread th1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        asr(session, fileAgent, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("Exception caught, exit now");
                        System.exit(5);
                    }
                }
            });
            Thread th2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        asr(session, fileCustomer, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println("meet exception to exit");
                        System.exit(6);
                    }
                }
            });

            th1.start();
            th2.start();

            th1.join();
            th2.join();
            session.sendEndSpeech(); // 必须等坐席客服全部发送完成，才能end。
            session.destroy();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (SendException e) {
            e.printStackTrace();
        } catch (InitException e) {
            e.printStackTrace();
        }
    }

    private static void asr(Session session, String file, boolean isFirst) throws Exception {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(file);
            int n = 0;
            int packageDurationInMs = 160;
            do {
                try {
                    Thread.sleep(packageDurationInMs);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                byte[] bytes = new byte[packageDurationInMs * 16];
                n = stream.read(bytes);

                if (n > 0) {
                    if (isFirst) {
                        session.sendFirstRoleSpeech(bytes);
                    } else {
                        session.sendSecondRoleSpeech(bytes); // 如果roleId=2 会有第二路通话
                    }
                }
            } while (n > 0);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void registerShutdown(ISessionController controller, Session session) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    session.sendEndSpeech();
                    Thread.currentThread().sleep(1000);
                } catch (SendException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
