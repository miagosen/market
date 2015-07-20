package com.lx.market.constants;

/**
 * Created by Antikvo.Miao on 2014/7/25.
 */
public class GlobalConstants {
  public static final int ASSEMBLY_TYPE_BANNER = 1;
  public static final int ASSEMBLY_TYPE_GRID_N_1 = 2;//N*1
  public static final int ASSEMBLY_TYPE_LIST_1_N = 3;//1*N
  public static final int ACTION_TYPE_APK = 1;//应用
  public static final int ACTION_TYPE_ASSEMBLE = 2;//栏目
  public static final int ACTION_TYPE_WAP = 3;//应用
  public static final int PAGE_SIZE = 10;//每次请求应用数

  //手机参数
  public static int SCREEN_WIDTH; //屏幕宽
  public static int SCREEN_HEIGHT; //屏幕高
  public static int SCREEN_DPI; //DPI
  public static float SCALED_DPI; //DPI系数
  public static float DPI;
  public static int SPLASH_IMAGE_HEIGHT;
  public static int COMMON_MARGIN; //通用的MARGIN，4个DP，从attrs里面读取
  public static int DB_VERSION = 1;

  //--------------------------统计---位置-------------------------------------------------------
  public static final int POSITION_HOME_POPULAR_BAR = 1;
  public static final int POSITION_HOME_FLOW_BAR = 2;
  public static final int POSITION_HOME_APP_LIST = 3;
  //--------------------------统计---位置-------------------------------------------------------

}
