package com.lx.market.download;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class DownloadClient {
  HttpURLConnection conn;
  String url;
  public static final int CONNECTION_TIME_OUT = 10 * 1000; // 10 secs
  public static final int READING_TIME_OUT = 50 * 1000; // 50 secs

  public DownloadClient (String dl_url) {
	this.url = dl_url.replace(" ", "%20");
  }

  public InputStream getInputStream () throws IOException,
	SocketTimeoutException {
	return getInputStream(0);
  }

  /**
   * 从网络读取数据
   *
   * @param sizePos ： 断点续传时的起始位置
   * @return
   * @throws java.net.MalformedURLException
   * @throws java.net.SocketTimeoutException
   * @throws java.io.IOException
   */
  public InputStream getInputStream (long sizePos)
	throws MalformedURLException, SocketTimeoutException, IOException {

	URL aURL = new URL(url);
	conn = (HttpURLConnection) aURL.openConnection();
	conn.setConnectTimeout(CONNECTION_TIME_OUT);
	conn.setReadTimeout(READING_TIME_OUT);
	if (0 != sizePos) {
	  conn.setRequestProperty("Range", "bytes=" + sizePos + "-");
	}

	conn.connect();

	InputStream is = conn.getInputStream();
	return is;
  }

  public int getContentLength () {
	if (conn != null) {
	  return conn.getContentLength();
	}
	return 0;
  }

  public long getFileSize () throws IOException {
	URL aURL = new URL(url);
	conn = (HttpURLConnection) aURL.openConnection();
	conn.setConnectTimeout(CONNECTION_TIME_OUT);
	conn.setReadTimeout(READING_TIME_OUT);
	long size = conn.getContentLength();
	return size;
  }

  public void close () {
	disconnect();
	conn = null;
  }

  public void disconnect () {
	if (conn != null)
	  conn.disconnect();
  }

}
