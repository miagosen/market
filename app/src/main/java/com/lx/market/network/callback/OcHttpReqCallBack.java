package com.lx.market.network.callback;

/**
 * Created by Antikvo.Miao on 2014/8/6.
 */

public interface OcHttpReqCallBack {
  /**
   * 如果请求成功，返回resp，如果请求失败，返回字符串结果
   *
   * @param result    响应结果
   * @param respOrMsg 对应的resp 或 失败信息
   */
  public void onResponse (boolean result, Object respOrMsg);
}
