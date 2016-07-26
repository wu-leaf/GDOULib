package com.fedming.gdoulib.util;

import com.fedming.gdoulib.bean.CommonException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DataUtil {
    public DataUtil() {
    }

    public static String doGet(String urlStr) throws CommonException {
        StringBuffer sb = new StringBuffer();

        try {
            URL e = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) e.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.9) Gecko/20100315 Firefox/3.5.9");
            if (conn.getResponseCode() != 200) {
                throw new CommonException("访问网络失败");
            } else {
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                char[] buf = new char[1024];

                int len1;
                while ((len1 = isr.read(buf)) != -1) {
                    sb.append(new String(buf, 0, len1));
                }

                is.close();
                isr.close();
                conn.disconnect();
                return sb.toString();
            }
        } catch (Exception var8) {
            throw new CommonException("访问网络失败");
        }
    }

}