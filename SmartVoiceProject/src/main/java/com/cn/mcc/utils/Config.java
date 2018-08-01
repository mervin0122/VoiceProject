package com.cn.mcc.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Config {
    private Log log = LogFactory.getLog(Config.class);
    private static final String CONIFG_FILE = "/config.properties";
    private static Config instance = null;
    private Properties props = new Properties();

    private Config() {
        this.init();
    }

    private void init() {
        InputStream in = null;

        try {
            in = Config.class.getResourceAsStream("/application.properties");
            this.props.load(in);
        } catch (IOException var11) {
            if(this.log.isErrorEnabled()) {
                this.log.error("loading /config.properties fail!", var11);
            }
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

        }

    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String get(String key) {
        return this.get(key, (String)null);
    }

    public String get(String key, String defaultValue) {
        return this.props.getProperty(key, defaultValue);
    }

}
