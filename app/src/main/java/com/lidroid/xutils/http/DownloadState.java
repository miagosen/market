package com.lidroid.xutils.http;

/**
 * Created by Antikvo.Miao on 2014/8/11.
 */
public class DownloadState {

  /**
   * 应用未安装
   */
  public static final int NOEXIST = 0;
  /**
   * 等待状态
   */
  public static final int WAITING = 1;
  /**
   * 已开始下载
   */
  public static final int STARTED = 2;
  /**
   * 下载/请求中
   */
  public static final int LOADING = 3;
  /**
   * 下载失败/请求失败
   */
  public static final int FAILURE = 4;
  /**
   * 用户取消
   */
  public static final int CANCELLED = 5;
  /**
   * 下载成功/请求成功
   */
  public static final int SUCCESS = 6;
  /**
   * 下载后正在安装
   */
  public static final int INSTALLING = 7;
  /**
   * 已经安装
   */
  public static final int INSTALLED = 8;
  /**
   * 有可更新文件
   */
  public static final int UPDATE = 9;

  /**
   * 暂停下载
   */
  public static final int PAUSE = 10;

}
