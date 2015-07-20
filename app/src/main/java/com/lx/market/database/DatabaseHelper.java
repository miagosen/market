/**
 *
 */
package com.lx.market.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.lx.market.MarketApplication;
import com.lx.market.threads.SQLSingleThreadExcutor;

import java.util.Locale;

/**
 * 数据库操作类
 */
public class DatabaseHelper extends SQLiteOpenHelper {
  private static final String DB_NAME = "DownloadManager.db";// 数据库名称
  private static final int DB_VERSION = 1;// 数据库版本
  private static SQLiteDatabase database;
  private static DatabaseHelper dbHelper;

  public interface DatabaseInsertResult {
	void insertResult (long result);
  }

  public interface DatabaseDeleteResult {
	void deleteResult (int result);
  }

  /**
   * SQL语句
   */
  public static final String SQL_DELETE_TABLE = "drop table if exists ";
  public static final String SQL_CREATE_TABLE = "create table if not exists ";

  /**
   * SQLiteOpenHelper
   */
  public DatabaseHelper () {
	super(MarketApplication.curContext, DB_NAME, null, DB_VERSION);
  }

  public static synchronized void initSQLiteDatabase () {
	if (null == dbHelper) {
	  try {
		dbHelper = new DatabaseHelper();
		database = dbHelper.getWritableDatabase();
	  } catch (Exception e) {
		e.printStackTrace();
	  }
	}
  }

  public static void releaseDataBase () {
	if (null != database) {
	  database.close();
	  database = null;
	}

	if (null != dbHelper) {
	  dbHelper.close();
	  dbHelper = null;
	}
  }

  public static SQLiteDatabase getDatabase () {
	if (null == database) {
	  initSQLiteDatabase();
	}
	return database;
  }

  @Override
  public void onCreate (SQLiteDatabase db) {
	db.execSQL(DownloadInfoTable.SQL_CREATE_DOWNLOAD_INFO_TABLE);
//		FileUtils.saveFixedInfo(FileUtils.CHANNEL_ID,
//		  (ClientInfo.getInstance().channelCode));
  }

  @Override
  public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
	// 排行和分类的表结构创建
//		db.execSQL(GameRankTitleTable.SQL_CREATE_RANK_TITLE);
//		db.execSQL(GameRankListTable.SQL_CREATE_RANK_LIST);
//		db.execSQL(GameTypeTable.SQL_CREATE_TYPE_LIST);
//		// 升级user表
//		db.execSQL(UserTable.SQL_DELETE_USER_TABLE);
//		db.execSQL(UserTable.SQL_CREATE_USER_TABLE);
//	if (oldVersion == newVersion) {
//	  db.execSQL(DownloadInfoTable.SQL_DELETE_DOWNLOAD_INFO_TABLE);
//	}
  }

  public void onDowngrade (SQLiteDatabase db, int oldVersion, int newVersion) {
	Locale locale = Locale.getDefault();
	if (Locale.CHINA.getCountry().equals(locale.getCountry())) {
	  Toast.makeText(MarketApplication.curContext,
		"你进行了降级操作,为确保您的正常使用,请升级新版本.", Toast.LENGTH_LONG).show();
	} else {
	  Toast.makeText(
		MarketApplication.curContext,
		"You degraded operation, to ensure that you properly, please upgrade to the new version.",
		Toast.LENGTH_LONG).show();
	}
	clearDB(db);
  }

  private static void clearDB (SQLiteDatabase db) {
	//数据库降级，清空数据库，确保能启动
	db.execSQL(DownloadInfoTable.SQL_DELETE_DOWNLOAD_INFO_TABLE);
	db.execSQL(DownloadInfoTable.SQL_CREATE_DOWNLOAD_INFO_TABLE);

  }

  /**
   * 执行SQL语句
   *
   * @param sql
   */
  public static void executeSQL (final String sql, final Object[] bindArgs) {
	SQLSingleThreadExcutor.getInstance().execute(new Runnable() {

	  @Override
	  public void run () {
		try { //try - catch 原因:防止数据库前后版本不匹配,导致数据异常
		  if (null != bindArgs) {
			getDatabase().execSQL(sql, bindArgs);
		  } else {
			getDatabase().execSQL(sql);
		  }
		} catch (Exception e) {
		}
	  }
	});
  }

  public static void update (final String table, final ContentValues values, final String whereClause, final String[] whereArgs) {
	SQLSingleThreadExcutor.getInstance().execute(new Runnable() {
	  @Override
	  public void run () {
		try { //try - catch 原因:防止数据库前后版本不匹配,导致数据异常
		  getDatabase().update(table, values, whereClause, whereArgs);
		} catch (Exception e) {
		}

	  }
	});
  }

  public static void insert (final String table, final String nullColumnHack, final ContentValues values, final DatabaseInsertResult resultCB) {
	SQLSingleThreadExcutor.getInstance().execute(new Runnable() {
	  @Override
	  public void run () {
		try { //try - catch 原因:防止数据库前后版本不匹配,导致数据异常
		  long result = getDatabase().insert(table, nullColumnHack, values);
		  resultCB.insertResult(result);
		} catch (Exception e) {
		}
	  }
	});
  }

  public static void insert (final String table, final String nullColumnHack, final ContentValues values) {
	SQLSingleThreadExcutor.getInstance().execute(new Runnable() {
	  @Override
	  public void run () {
		try { //try - catch 原因:防止数据库前后版本不匹配,导致数据异常
		  getDatabase().insert(table, nullColumnHack, values);
		} catch (Exception e) {
		}
	  }
	});
  }

  public static void delete (final String table, final String whereClause, final String[] whereArgs) {
	SQLSingleThreadExcutor.getInstance().execute(new Runnable() {
	  @Override
	  public void run () {
		try { //try - catch 原因:防止数据库前后版本不匹配,导致数据异常
		  getDatabase().delete(table, whereClause, whereArgs);
		} catch (Exception e) {
		}

	  }
	});
  }

  /**
   * 查询操作可以多线程
   */
  public static Cursor query (String table, String[] columns, String selection,
							  String[] selectionArgs, String groupBy, String having,
							  String orderBy) {
	try {
	  return getDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
	} catch (Exception e) {
	  try {
		//如果数据查询操作异常，将清除所有数据，重新创建数据库
		clearDB(database);
	  } catch (Exception e2) {
	  }
	  return null;
	}
  }

  public static void beginTransaction () {
	try {
	  getDatabase().beginTransaction();
	} catch (Exception e) {
	}
  }

  public static void endTransaction () {
	try {
	  getDatabase().endTransaction();
	} catch (Exception e) {
	}

  }

  public static void setTransactionSuccessful () {
	try {
	  getDatabase().setTransactionSuccessful();
	} catch (Exception e) {
	}

  }

  public static Cursor rawQuery (String sql, String[] selectionArgs) {
	return getDatabase().rawQuery(sql, selectionArgs);
  }
}
