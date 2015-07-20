package com.lx.market.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.lx.market.constants.FileConstants;
import com.lx.market.model.Count;
import com.lx.market.model.Size;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
  /**
   * 根据后缀找到指定目录下所有的文件
   *
   * @param targetList 需要插入的列表
   * @param suffix     后缀
   */
  public static void getFileListBySuffix (ArrayList<File> targetList, File dir, String suffix) {
	if (suffix == null || TextUtils.isEmpty(suffix.trim())) {
	  return;
	}
	try {
	  File[] files = dir.listFiles();
	  if (files != null) {
		for (File f : files) {
		  if (f.isFile()) {
			if (f.getName().toLowerCase().endsWith(suffix.toLowerCase())) {
			  targetList.add(f);
			}
		  } else {
			getFileListBySuffix(targetList, f, suffix);
		  }
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }
  public static String getFileSizeString (long fileSize) {
	DecimalFormat df = new DecimalFormat("#.00");
	String size = "";
	if (fileSize < 1024) {
	  size = df.format((double) fileSize) + "B";
	} else if (fileSize < 1024 * 1024) {
	  size = df.format((double) fileSize / 1024) + "K";
	} else if (fileSize < 1024 * 1024 * 1024) {
	  size = df.format((double) fileSize / 1024 / 1024) + "M";
	} else if (fileSize < 1024 * 1024 * 1024 * 1024) {
	  size = df.format((double) fileSize / 1024 / 1024 / 1024) + "G";
	}
	return size;
  }
  /**
   * 根据文件名找到指定目录下所有的文件
   *
   * @param targetList 需要插入的列表
   * @param fileName   文件名
   */
  public static void getFileListByName (ArrayList<File> targetList, File dir, String fileName) {
	if (fileName == null || TextUtils.isEmpty(fileName.trim())) {
	  return;
	}
	try {
	  File[] files = dir.listFiles();
	  if (files != null) {
		for (File f : files) {
		  if (f.isFile()) {
			if (f.getName().toLowerCase().equals(fileName.toLowerCase())) {
			  targetList.add(f);
			}
		  } else {
			getFileListByName(targetList, f, fileName);
		  }
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	} catch (Error e) {
	  e.printStackTrace();
	}
  }

  /**
   * 判断文件是否已经存在于指定目录中, 返回完全路径
   *
   * @param dir
   * @param fileName
   * @return
   */
  public static void isFileInDir (File dir, String fileName, boolean isFile, StringBuffer ret) {
	if (TextUtils.isEmpty(fileName.trim()) || !TextUtils.isEmpty(ret.toString().trim())) {
	  return;
	}
	try {
	  File[] files = dir.listFiles();
	  if (files != null) {
		for (File f : files) {
		  if (isFile) {
			if (f.isFile()) {
			  if (fileName.equalsIgnoreCase(f.getName())) {
				ret.append(f.getAbsolutePath());
				break;
			  }
			} else {
			  isFileInDir(f, fileName, isFile, ret);
			}
		  } else {
			if (f.isDirectory()) {
			  if (fileName.equalsIgnoreCase(f.getName())) {
				ret.append(f.getAbsolutePath());
			  } else {
				isFileInDir(f, fileName, isFile, ret);
			  }
			}
		  }
		}
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
  }

  /**
   * 复制文件
   *
   * @param sourceFile 源文件
   * @param targetFile 输出文件
   */
  public static void copyFile (File sourceFile, File targetFile) throws IOException {
	copyStream(new FileInputStream(sourceFile), new FileOutputStream(targetFile));
  }

  /**
   * 复制文件
   *
   * @param is 文件输入流
   * @param os 文件输出流
   */
  public static void copyStream (InputStream is, OutputStream os) throws IOException {
	BufferedInputStream bis = new BufferedInputStream(is);
	BufferedOutputStream bos = new BufferedOutputStream(os);
	// 缓冲数组
	byte[] b = new byte[1024 * 2];
	int len;
	while ((len = bis.read(b)) != -1) {
	  bos.write(b, 0, len);
	}
	// 刷新此缓冲的输出流
	bos.flush();

	// 关闭流
	bis.close();
	bos.close();
	is.close();
	os.close();
  }

  /**
   * 复制文件夹
   *
   * @param sourceDir 源文件夹
   * @param targetDir 输出文件夹
   */
  public static void copyDirectiory (String sourceDir, String targetDir) throws IOException {
	// 新建目标目录
	File fileTargetDir = new File(targetDir);
	if (!fileTargetDir.exists()) {
	  fileTargetDir.mkdirs();
	}
	// 获取源文件夹当前下的文件或目录
	File[] file = (new File(sourceDir)).listFiles();
	for (int i = 0; i < file.length; i++) {
	  if (file[i].isFile()) {
		// 源文件
		File sourceFile = file[i];
		// 目标文件
		File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
		copyFile(sourceFile, targetFile);
	  }
	  if (file[i].isDirectory()) {
		// 准备复制的源文件夹
		String dir1 = sourceDir + "/" + file[i].getName();
		// 准备复制的目标文件夹
		String dir2 = targetDir + "/" + file[i].getName();
		copyDirectiory(dir1, dir2);
	  }
	}
  }

  /**
   * 获取当前目录下所有子文件的路径
   *
   * @param pathList
   * @param dir
   * @throws java.io.IOException
   */
  public static void getAllFilePath (List<String> pathList, File dir) throws IOException {
	File[] files = dir.listFiles();
	if (files != null) {
	  for (File f : files) {
		if (f.isDirectory()) {
		  getAllFilePath(pathList, f);
		} else if (f.isFile()) {
		  pathList.add(f.getAbsolutePath());
		}
	  }
	}
  }

  /**
   * 获取文件内容
   *
   * @param f 文件
   * @return 内容
   */
  public static String readFile (File f) throws IOException {
	StringBuffer sb = new StringBuffer();
	InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
	BufferedReader br = new BufferedReader(isr);
	String readoneline;
	while ((readoneline = br.readLine()) != null) {
	  sb.append(readoneline);
	}
	br.close();
	isr.close();
	return sb.toString();
  }

  /**
   * 获取文件内容
   *
   * @param is 输入流
   * @return
   * @throws java.io.IOException
   */
  public static String readFile (InputStream is) throws IOException {
	StringBuffer sb = new StringBuffer();
	InputStreamReader isr = new InputStreamReader(is, "UTF-8");
	BufferedReader br = new BufferedReader(isr);
	String readoneline;
	while ((readoneline = br.readLine()) != null) {
	  sb.append(readoneline);
	}
	br.close();
	isr.close();
	return sb.toString();
  }

  /**
   * 将内容写入文件
   *
   * @param content 内容
   * @param file    文件
   */
  public static void writeFile (File file, String content, boolean isAppend) throws IOException {
	FileWriter fw;
	BufferedWriter bw;
	fw = new FileWriter(file, isAppend);
	bw = new BufferedWriter(fw);
	bw.write(content);
	bw.flush();
	bw.close();
	fw.close();
  }

  /**
   * 字节流转换为字节数组
   *
   * @param is
   * @return
   * @throws java.io.IOException
   */
  public static byte[] InputStreamToByte (InputStream is) throws IOException {
	ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
	int ch;
	while ((ch = is.read()) != -1) {
	  bytestream.write(ch);
	}
	byte imgdata[] = bytestream.toByteArray();
	bytestream.close();
	return imgdata;
  }

  /**
   * 获取文件大小
   *
   * @param fileName
   * @return
   */
  public static int getFileSize (String fileName) {
	int ret = 0;
	FileInputStream fis;
	try {
	  fis = new FileInputStream(new File(fileName));
	  ret = fis.available();
	  fis.close();
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}
	return ret;
  }

  /**
   * @param @param  fileName
   * @param @return
   * @return Object
   * @throws
   * @Title: readObjectFromFile
   * @Description: 从指定文件读取对象
   */
  public static Object readObjectFromFile (String fileName) {
	FileInputStream freader;
	Object ret = null;

	try {
	  freader = new FileInputStream(fileName);
	  ObjectInputStream objectInputStream = new ObjectInputStream(freader);

	  ret = objectInputStream.readObject();
	  freader.close();
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	} catch (ClassNotFoundException e) {
	  e.printStackTrace();
	}
	return ret;
  }

  /**
   * @param @param fileName
   * @param @param srcObject
   * @return void
   * @throws
   * @Title: writeObjectToFile
   * @Description:
   */
  public static void writeObjectToFile (String fileName, Object srcObject, boolean isAppend) {
	try {
	  FileOutputStream outStream = new FileOutputStream(fileName, isAppend);
	  ObjectOutputStream objectOutputStream = new ObjectOutputStream(outStream);

	  objectOutputStream.writeObject(srcObject);
	  outStream.close();
	} catch (FileNotFoundException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

  /**
   * 删除目录
   *
   * @param sPath
   * @return
   */
  public static boolean deleteDirectory (String sPath) {
	File dirFile = new File(sPath);
	// 如果dir对应的文件不存在，或者不是一个目录，则退出
	if (!dirFile.exists() || !dirFile.isDirectory()) {
	  return false;
	}
	// 删除文件夹下的所有文件(包括子目录)
	File[] files = dirFile.listFiles();
	if (files != null) {
	  for (int i = 0; i < files.length; i++) {
		// 删除子文件
		if (files[i].isFile()) {
		  files[i].delete();
		} // 删除子目录
		else {
		  deleteDirectory(files[i].getAbsolutePath());
		}
	  }
	}
	// 删除当前目录
	if (dirFile.delete()) {
	  return true;
	} else {
	  return false;
	}
  }

  /**
   * 复制asset中的所有内容到指定目录
   *
   * @param context
   * @param assetDir
   * @param dir         目标目录
   * @param count       用于计数
   * @param reserveRoot 是否保留第一级目录
   */
  public static void CopyAssets (Context context, String assetDir, String dir, Count count, boolean reserveRoot) {
	String[] files;
	try {
	  files = context.getResources().getAssets().list(assetDir);
	} catch (IOException e1) {
	  return;
	}
	if (!reserveRoot) {
	  dir = dir.replace("/" + assetDir, "");
	}
	File mWorkingPath = new File(dir);
	if (!mWorkingPath.exists()) {
	  mWorkingPath.mkdirs();
	}
	for (int i = 0; i < files.length; i++) {
	  try {
		String fileName = files[i];
		// we make sure file name not contains '.' to be a folder.
		if (!fileName.contains(".")) {
		  CopyAssets(context, assetDir + "/" + fileName, dir + "/" + fileName + "/", count, reserveRoot);
		} else {
		  File outFile = new File(mWorkingPath, fileName);
		  if (!outFile.exists())
			outFile.createNewFile();
		  InputStream in = context.getAssets().open(assetDir + "/" + fileName);
		  OutputStream out = new FileOutputStream(outFile);
		  byte[] buf = new byte[1024];
		  int len;
		  while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		  }
		  in.close();
		  out.close();
		  count.setCount(count.getCount() + 1);
		}
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}
  }

  /**
   * 复制asset中的所有内容到指定目录
   *
   * @param context
   * @param assetDir
   * @param dir         目标目录
   * @param count       用于计数
   * @param reserveRoot 是否保留第一级目录
   */
  public static void CopyAssets (Context context, String assetDir, String dir, Count count, boolean reserveRoot, Size size) {
	String[] files;
	try {
	  files = context.getResources().getAssets().list(assetDir);
	} catch (IOException e1) {
	  return;
	}
	if (!reserveRoot) {
	  dir = dir.replace("/" + assetDir, "");
	}
	File mWorkingPath = new File(dir);
	if (!mWorkingPath.exists()) {
	  mWorkingPath.mkdirs();
	}
	for (int i = 0; i < files.length; i++) {
	  try {
		String fileName = files[i];
		// we make sure file name not contains '.' to be a folder.
		if (!fileName.contains(".")) {
		  CopyAssets(context, assetDir + "/" + fileName, dir + "/" + fileName + "/", count, reserveRoot, size);
		} else {
		  File outFile = new File(mWorkingPath, fileName);
		  if (!outFile.exists())
			outFile.createNewFile();
		  InputStream in = context.getAssets().open(assetDir + "/" + fileName);
		  OutputStream out = new FileOutputStream(outFile);
		  byte[] buf = new byte[1024];
		  int len;
		  while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			size.setFileSize(size.getFileSize() + len);
		  }
		  in.close();
		  out.close();
		  count.setCount(count.getCount() + 1);
		}
	  } catch (IOException e) {
		e.printStackTrace();
	  }
	}
  }

  /**
   * 获取用于判断debug模式的文件
   */
  public static File getDebugFile () {
	File SDFile = android.os.Environment.getExternalStorageDirectory();
	File myFile = new File(SDFile.getAbsolutePath() + File.separator + "debug" + File.separator + "U1Mo5Y2zbt.properties");
	return myFile;
  }

  /**
   * 替换文件中内内容
   *
   * @param path      文件路径
   * @param original  需要替换的内容
   * @param newString 替换后的内容
   */
  public static void modifyString (String path, String original, String newString) {
	write(path, readAndModifyString(path, original, newString));
  }

  /**
   * 读取文件并替换文件中内内容
   *
   * @param filePath  文件路径
   * @param original  需要替换的内容
   * @param newString 替换后的内容 返回 修改后内容 只可用于小文件的修改
   */
  public static String readAndModifyString (String filePath, String original, String newString) {
	BufferedReader br = null;
	String line = null;
	StringBuffer buf = new StringBuffer();
	try {
	  br = new BufferedReader(new FileReader(filePath));
	  while ((line = br.readLine()) != null) {
		buf.append(line.replaceAll(original, newString));
		buf.append("\n");
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	} finally {
	  if (br != null) {
		try {
		  br.close();
		} catch (IOException e) {
		  br = null;
		}
	  }
	}

	return buf.toString();
  }

  /**
   * 将内容回写到文件中
   *
   * @param filePath
   * @param content
   */
  public static void write (String filePath, String content) {
	BufferedWriter bw = null;

	try {
	  // 根据文件路径创建缓冲输出流
	  bw = new BufferedWriter(new FileWriter(filePath));
	  // 将内容写入文件中
	  bw.write(content);
	} catch (Exception e) {
	  e.printStackTrace();
	} finally {
	  // 关闭流
	  if (bw != null) {
		try {
		  bw.close();
		} catch (IOException e) {
		  bw = null;
		}
	  }
	}
  }

  public static void UnZipFolder (String zipFileString, String outPathString) throws Exception {
	ZipInputStream inZip = new ZipInputStream(new FileInputStream(new File(zipFileString)));
	ZipEntry zipEntry;
	String szName = "";

	while ((zipEntry = inZip.getNextEntry()) != null) {
	  szName = zipEntry.getName();
	  if (zipEntry.isDirectory()) {
		// get the folder name of the widget
		szName = szName.substring(0, szName.length() - 1);
		File folder = new File(outPathString + File.separator + szName);
		folder.mkdirs();
	  } else {
		File file = new File(outPathString + File.separator + szName);
		file.createNewFile();
		// get the output stream of the file
		FileOutputStream out = new FileOutputStream(file);
		int len;
		byte[] buffer = new byte[1024];
		// read (len) bytes into buffer
		while ((len = inZip.read(buffer)) != -1) {
		  // write (len) byte from buffer at the position 0
		  out.write(buffer, 0, len);
		  out.flush();
		}
		out.close();
	  }
	}
	inZip.close();
  }

  public static final String CONFIG_FILE = ".config";

  /**
   * 从配置文件中读取配置信息
   *
   * @param name
   * @return
   */
  public static String getConfigByNameFromFile (String name) {
	String ret = null;
	Properties p = new Properties();
	File file = new File(FileConstants.SDCARD + FileConstants.FILE_ROOT + "/" + CONFIG_FILE);
	FileInputStream fis = null;
	try {
	  if (file.exists()) {
		fis = new FileInputStream(file);
		p.load(fis);
		ret = p.getProperty(name);
	  }
	} catch (Exception e) {
	  e.printStackTrace();
	}
	return ret;
  }

  /**
   * 插入配置信息到文件
   */
  public static void putConfigToFile (String name, String value) {
	Properties p = new Properties();
	File file = new File(FileConstants.SDCARD + FileConstants.FILE_ROOT + "/" + CONFIG_FILE);
	try {
	  if (!file.exists()) {
		file.createNewFile();
	  }
	  FileInputStream fis = new FileInputStream(file);
	  p.load(fis);
	  p.setProperty(name, value);
	  p.store(new FileOutputStream(file), "");
	} catch (Exception e) {
	  Logger.p(e);
	}
  }

  public static boolean hasFileInAssets (final Context context, final String fileName) {
	AssetManager am = context.getAssets();
	try {
	  String[] names = am.list("");
	  for (int i = 0; i < names.length; i++) {
		if (names[i].equals(fileName.trim())) {
		  return true;
		}
	  }
	} catch (IOException e) {
	}
	return false;
  }

  public static File getFileFromAssets (Context context, String fileName) {
	File file = null;
	try {
	  File f = new File(FileConstants.SDCARD + FileConstants.FILE_ROOT);
	  if (!f.exists()) {
		f.mkdirs();
	  }
	  InputStream is = context.getAssets().open(fileName);
	  file = new File(FileConstants.SDCARD + FileConstants.FILE_ROOT + "/" + fileName);
	  file.createNewFile();
	  FileOutputStream fos = new FileOutputStream(file);
	  byte[] temp = new byte[1024];
	  int i = 0;
	  while ((i = is.read(temp)) > 0) {
		fos.write(temp, 0, i);
	  }
	  fos.close();
	  is.close();
	} catch (IOException e) {
	  Logger.p(e);
	}
	return file;
  }

  /**
   * 根据文件字节数显示文件大小
   *
   * @param size
   * @return
   */
  public static String getFileSize (long size) {
	DecimalFormat df = new DecimalFormat("###.##");
	float f;
	if (size < 1024 * 1024) {
	  f = ((float) size / (float) 1024);
	  return (df.format(new Float(f).doubleValue()) + "KB");
	} else if (size < 1024 * 1024 * 1024) {
	  f = ((float) size / (float) (1024 * 1024));
	  return (df.format(new Float(f).doubleValue()) + "MB");
	} else {
	  f = ((float) size / (float) (1024 * 1024 * 1024));
	  return (df.format(new Float(f).doubleValue()) + "GB");
	}

  }

  public static String getTmpDownloadFile (String packageName, int versionCode) {
	return FileConstants.APK_DIR_PATH + File.separator + packageName + "_" + versionCode + ".tmp";
  }

  public static String getAPKFilePath (String packageName, int versionCode) {
	return FileConstants.APK_DIR_PATH + File.separator + packageName + "_" + versionCode + ".apk";
  }
}
