/************************************************************************** * 河北世窗信息技术股份有限公司 * All rights reserved. * * 项目名称：宝贝计划   ***************************************************************************/
package com.bbjh.common.utils;

import com.bbjh.common.enums.ResultCode;
import com.bbjh.common.exception.ScException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

@Slf4j
public class HttpClientUtil {

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String get(String url, String param) throws Exception {
        URL connURL = new URL(url);
        HttpURLConnection httpCon = (HttpURLConnection) connURL.openConnection();

        // 设置http请求的头部
        httpCon.setUseCaches(false);// Post 请求不能使用缓存
        httpCon.setDoOutput(true); // http正文内，因此需要设为true, 默认情况下是false;
        httpCon.setDoInput(true); // 设置是否从httpUrlConnection读入，默认情况下是true;

        // 设定传送的内容类型是可序列化的java对象
        httpCon.setRequestProperty("Content-type", "text/xml; charset=UTF-8");
        httpCon.setRequestMethod("GET"); // 设定请求的方法为"POST"，默认是GET
        httpCon.setConnectTimeout(3000); // 连接主机的超时时间（单位：毫秒）
        httpCon.setReadTimeout(3000); // 从主机读取数据的超时时间（单位：毫秒）

        // 写入http请求的正文
        DataOutputStream dataOutputStream = new DataOutputStream(httpCon.getOutputStream());
        // dataOutputStream.write(message.getBytes("UTF-8"));
        dataOutputStream.flush();
        dataOutputStream.close();
        int responseCode = httpCon.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) // 返回码正确
        {
            // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }
            bufReader.close();
            return sb.toString();
        } else
        // 返回码错误，例如：404
        {
            log.error("调用Http请求有异常！返回码为：" + responseCode);
            return "";
        }
    }

    public static String get(String url, Map<String, String> paramsMap, String encoding) throws ScException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            HttpGet httpGet = new HttpGet(url);

            if (paramsMap != null && !paramsMap.isEmpty()) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    params.add(pair);
                }
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params, encoding));
                httpGet = new HttpGet(url + "?" + str);
            }

            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ScException(ResultCode.HTTP_EXCEPTION);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String getWithProxy(String url, Map<String, String> paramsMap, String encoding) throws ScException {
        // ConnectionSocketFactory注册
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new MyConnectionSocketFactory()).register("https", new MySSLConnectionSocketFactory(SSLContexts.createSystemDefault())).build();
        // HTTP客户端连接管理池
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(reg);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

        CloseableHttpResponse response = null;
        InputStream is = null;
        try {
            // socks代理地址
            InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1", 1080);
            HttpClientContext context = HttpClientContext.create();
            context.setAttribute("socks.address", socksaddr);

            HttpGet httpGet = new HttpGet(url);

            if (paramsMap != null && !paramsMap.isEmpty()) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> param : paramsMap.entrySet()) {
                    NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
                    params.add(pair);
                }
                String str = EntityUtils.toString(new UrlEncodedFormEntity(params, encoding));
                httpGet = new HttpGet(url + "?" + str);
            }

            response = httpclient.execute(httpGet, context);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ScException(ResultCode.HTTP_EXCEPTION);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     * @throws Exception
     */
    public static String post(String url, String param) throws Exception {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new javax.net.ssl.HostnameVerifier() {
            public boolean verify(String hostname, SSLSession sslsession) {
                return true;
            }
        });
        return post(url, param, false);
    }

    public static String post(String url, String param, boolean isZip) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Encoding", isZip ? "gzip" : "");
            // conn.setRequestProperty("Content-Type", "application/json");
            // conn.setRequestProperty("user-agent",
            // "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            // 定义BufferedReader输入流来读取URL的响应
            if (!isZip) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            } else {
                InputStream inputStream = new GZIPInputStream(conn.getInputStream());
                in = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            }
            // while(!in.ready()) {
            // Thread.sleep(1);
            // }
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            log.error("发送 POST 请求出现异常！" + e.getMessage());
            throw e;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result.toString();
    }


    static class MyConnectionSocketFactory implements ConnectionSocketFactory {

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            // socket代理
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(
                final int connectTimeout,
                final Socket socket,
                final HttpHost host,
                final InetSocketAddress remoteAddress,
                final InetSocketAddress localAddress,
                final HttpContext context) throws IOException, ConnectTimeoutException {
            Socket sock;
            if (socket != null) {
                sock = socket;
            } else {
                sock = createSocket(context);
            }
            if (localAddress != null) {
                sock.bind(localAddress);
            }
            try {
                sock.connect(remoteAddress, connectTimeout);
            } catch (SocketTimeoutException ex) {
                throw new ConnectTimeoutException(ex, host, remoteAddress.getAddress());
            }
            return sock;
        }

    }

    static class MySSLConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MySSLConnectionSocketFactory(final SSLContext sslContext) {

            super(sslContext, ALLOW_ALL_HOSTNAME_VERIFIER);
        }

        @Override
        public Socket createSocket(final HttpContext context) throws IOException {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

        @Override
        public Socket connectSocket(int connectTimeout, Socket socket, HttpHost host, InetSocketAddress remoteAddress,
                                    InetSocketAddress localAddress, HttpContext context) throws IOException {
            // Convert address to unresolved
            InetSocketAddress unresolvedRemote = InetSocketAddress
                    .createUnresolved(host.getHostName(), remoteAddress.getPort());
            return super.connectSocket(connectTimeout, socket, host, unresolvedRemote, localAddress, context);
        }
    }
}
