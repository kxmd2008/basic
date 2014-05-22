package org.luis.basic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Basic implement on file operation with java.io.file api
 * 
 */
public final class FileUtil {

	/**
	 * mkdirs for a file;
	 * 
	 * @param filePath
	 * @return true if dir is created;
	 */
	public static synchronized boolean mkdirs(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			String parent = file.getParent();
			return new File(parent).mkdirs();
		} else {
			return false;
		}
	}

	/**
	 * build the dirPath this is the real one..
	 * 
	 * @param dirPath
	 */
	public static synchronized void mkRealDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			String parent = file.getParent();
			File p = new File(parent);
			if (!p.exists()) {
				mkRealDir(parent);
				mkRealDir(dirPath);
			} else {
				file.mkdirs();
			}
		}
	}

	/**
	 * delete a file recursivly;
	 * 
	 * @param path
	 */
	public static boolean delete(String path) {
		return delete(new File(path));
	}

	/**
	 * delete a file recursivly;
	 * 
	 * @param f
	 */
	public static boolean delete(File f) {
		boolean deleted = false;
		try {
			if (f.exists()) {
				if (f.isFile()) {
					deleted = f.delete();
				} else {
					File[] subs = f.listFiles();
					if (subs != null && subs.length > 0) {
						for (int i = 0; i < subs.length; i++) {
							delete(subs[i]);
						}
					}
					deleted = f.delete();
				}
			}
		} catch (Exception e) {
		}
		return deleted;
	}

	public static boolean appendInfoToFile(String info, String filePath) {
		try {
			mkdirs(filePath);
			File f = new File(filePath);
			info = info + "\r\n";
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			byte[] b = info.getBytes();
			raf.seek(raf.length());
			raf.write(b);
			raf.close();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public static boolean appendInfoToFile(String[] info, String filePath) {
		for (int i = 0; i < info.length; i++) {
			appendInfoToFile(info[i], filePath);
		}
		return true;
	}

	public static boolean appendFileToExistFile(String src, String target) {
		String info = readInfoFromFile(src);
		mkdirs(target);
		appendInfoToFile(info, target);
		return true;
	} 

	/**
	 * 从数据
	 * 
	 * @param is
	 * @return
	 */
	public static String readInfoFromInputStream(InputStream is) {
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");// 字节流到字符流转换的桥梁
			BufferedReader reader = new BufferedReader(isr); // 提供缓冲读取 提高速度
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
		} catch (Exception ex) {
		}
		return sb.toString();
	}

	public static String readInfoFromFile(String filePath) {
		StringBuffer sb = new StringBuffer();
		try {
			mkdirs(filePath);
			File f = new File(filePath);
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			String s = "";
			while ((s = raf.readLine()) != null) {
				// sb.append(s+" ");
				// ����....
				sb.append(s + "\r\n");
			}
			raf.close();
		} catch (Exception ex) {

		}
		return sb.toString();
	}// readInfoFromFile()

	public static String[] readInfosFromFile(String filePath) {
		List<String> al = new ArrayList<String>();
		try {
			// mkdirs(filePath);
			File f = new File(filePath);
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			String s = "";
			while ((s = raf.readLine()) != null) {
				al.add(s);
			}
			raf.close();
		} catch (Exception ex) {
			// System.out.println("can not read sql file...");
		}
		String[] infos = new String[al.size()];
		infos = (String[]) (al.toArray(infos));
		return infos;
	}// readInfoFromFile()

	/**
	 * copy a file or directory;
	 * 
	 * @param src
	 * @param target
	 * @throws IOException
	 */
	public static void copy(String src, String target) throws IOException {
		File srcFile = new File(src);
		if (srcFile.exists()) {
			if (srcFile.isFile()) {
				BufferedOutputStream bos = null;
				BufferedInputStream bis = null;
				try {
					int bytesRead;
					byte[] buf = new byte[8096];

					mkdirs(src);
					mkdirs(target);

					File targetFile = new File(target);
					if (targetFile.exists() && targetFile.isDirectory()) {
						targetFile = new File(targetFile, srcFile.getName());
					}

					bos = new BufferedOutputStream(new FileOutputStream(
							targetFile));
					bis = new BufferedInputStream(new FileInputStream(new File(
							src)));
					while ((bytesRead = bis.read(buf)) >= 0) {
						bos.write(buf, 0, bytesRead);
					}
					bos.flush();
				} finally {
					if (bos != null) {
						bos.close();
					}
					if (bis != null) {
						bis.close();
					}
				}
			} else {
				// crate tareget dir;
				new File(target).mkdir();

				// copy sub files and directories;
				char ch = src.charAt(src.length() - 1);
				if (ch != '\\' && ch != '/') {
					src += File.separatorChar;
				}

				ch = target.charAt(target.length() - 1);
				if (ch != '\\' && ch != '/') {
					target += File.separatorChar;
				}

				String[] subs = srcFile.list();
				for (int i = 0; i < subs.length; i++) {
					copy(src + subs[i], target + subs[i]);
				}
			}
		}
	}

	public static void writeToFile(InputStream is, String target)
			throws IOException {
		BufferedOutputStream bos = null;
		try {
			int bytesRead;
			byte[] buf = new byte[8096];
			mkdirs(target);
			File targetFile = new File(target);
			bos = new BufferedOutputStream(new FileOutputStream(targetFile));

			while ((bytesRead = is.read(buf)) >= 0) {
				bos.write(buf, 0, bytesRead);
			}
			bos.flush();
		} finally {
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * replace a symbol in a string;
	 * 
	 * @param src
	 * @param prefix
	 * @param suffix
	 * @param props
	 * @return String with token replaced
	 */
	public static String replace(String src, String prefix, String suffix,
			Map<String, Object> props) {
		int index1;
		int index2;
		int len1 = prefix.length();
		int len2 = suffix.length();

		StringBuffer sb = new StringBuffer();

		index1 = src.indexOf(prefix);
		while (index1 >= 0) {
			sb.append(src.substring(0, index1));
			src = src.substring(index1 + len1);
			if (src.startsWith(prefix)) {
				sb.append(prefix);
				break;
			} else {
				index2 = src.indexOf(suffix);
				if (index2 >= 0) {
					String t = src.substring(0, index2);
					Object o = props.get(t);
					String sp = (o == null ? "" : o.toString());
					sb.append(sp);
					src = src.substring((index2 + len2));
					index1 = src.indexOf(prefix);
				} else {
					sb.append(prefix);
					break;
				}
			}
		}
		sb.append(src);
		return new String(sb);
	}

	/**
	 * replace a symbol in a file;
	 * 
	 * @param filePath
	 *            the path to the file to be replaced;
	 * @param prefix
	 *            the prefix of the symbol;
	 * @param suffix
	 *            the suffix of the symbol;
	 * @param props
	 *            Properties that contains values used to replace;
	 * @throws IOException
	 */
	public static void replaceFile(String filePath, String prefix,
			String suffix, Map<String, Object> props) throws IOException {
		CharArrayWriter caw = new CharArrayWriter(8096);
		BufferedReader r = new BufferedReader(new FileReader(filePath));

		// read and replace symbols in the file;
		String line = r.readLine();
		while (line != null) {
			String rl = replace(line, prefix, suffix, props);
			caw.write(rl);
			caw.write("\n");
			line = r.readLine();
		}
		r.close();

		caw.flush();
		char[] ca = caw.toCharArray();
		caw.close();

		// write back the replaced contents to the orinal files;
		BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		bw.write(ca);
		bw.flush();
		bw.close();
	}

	/**
	 * replace a properties files;
	 * 
	 * @param filePath
	 *            the path to the file;
	 * @param prefix
	 *            the prefix of the symbol;
	 * @param suffix
	 *            the suffix of the symbol;
	 * @param props
	 *            the Properties contains value used to replace;
	 * @throws IOException
	 */
	public static Properties readPropFile(String filePath, String prefix,
			String suffix, Map<String, Object> props) throws IOException {
		return readPropFile(new File(filePath), prefix, suffix, props);
	}

	/**
	 * read a property file and replace sysmbols in this property file if any;
	 * 
	 * @param file
	 *            the File object to be read;
	 * @param prefix
	 *            the prefix of the symbols to be replaced;
	 * @param suffix
	 *            the suffxi of symbols to be replaced;
	 * @param props
	 *            the Properties to be replaced;
	 * @return
	 * @throws IOException
	 */
	public static Properties readPropFile(File file, String prefix,
			String suffix, Map<String, Object> props) throws IOException {

		Properties p = new Properties();
		p.load(new FileInputStream(file));

		// read and replace symbols in the file;
		Enumeration<?> enum2 = p.keys();
		while (enum2.hasMoreElements()) {
			String key = (String) enum2.nextElement();
			String value = (String) p.get(key);
			value = replace(value, prefix, suffix, props);
			p.setProperty(key, value);
		}

		// store the replaced properties;
		return p;
	}

	/**
	 * 
	 * 
	 * @param path
	 * @return File[] matches suffix
	 */
	public static File[] filerFiles(String path, String suffix) {
		return filterFiles(path, suffix, false);
	}

	/**
	 * filter files from a path recursivly;
	 * 
	 * @param path
	 * @param suffix
	 * @param recusive
	 * @return File[] matched suffix, including sub directories;
	 */
	public static File[] filterFiles(String path, String suffix,
			boolean recusive) {
		int index = 0;
		File f = new File(path);
		if (!f.exists()) {
			return new File[0];
		} else {
			File[] children = f.listFiles();
			int count = children.length;
			File[] is = new File[count];
			for (int i = 0; i < count; i++) {
				File mapping = children[i];
				if (mapping.isFile()) {
					String name = mapping.getName();
					if (name.toLowerCase().endsWith(suffix)) {
						is[index++] = children[i];
					}
				} else {
					if (recusive) {
						File[] subfiles = filterFiles(
								children[i].getAbsolutePath(), suffix, true);
						File[] is2 = new File[count + subfiles.length];
						System.arraycopy(is, 0, is2, 0, index);
						System.arraycopy(subfiles, 0, is2, index,
								subfiles.length);
						index += subfiles.length;
						is = is2;
					}
				}
			}
			File[] result = new File[index];
			System.arraycopy(is, 0, result, 0, index);
			return result;
		}
	}

	/**
	 * get the file displayName from a file path;
	 * 
	 * @param filePath
	 * @return name of the file, exclude the path;
	 */
	public static String getFileName(String filePath) {
		if (filePath == null) {
			return null;
		} else {
			int index = filePath.lastIndexOf('\\');
			if (index >= 0) {
				return filePath.substring(index + 1);
			} else {
				index = filePath.lastIndexOf('/');
				if (index >= 0) {
					return filePath.substring(index + 1);
				} else {
					return filePath;
				}
			}
		}
	}

	/**
	 * get the displayName of a file; not including the file's path;
	 * 
	 * @param file
	 * @return name of a File, exclude the path;
	 */
	public static String getFileName(File file) {
		if (file == null) {
			return null;
		} else {
			return getFileName(file.getAbsolutePath());
		}
	}

	/**
	 * get the extension displayName of a file;
	 * 
	 * @param fileName
	 *            the file displayName;
	 * @return the ext displayName;
	 */
	public static String getExtName(String fileName) {
		int index = fileName.lastIndexOf('.');
		if (index >= 0) {
			return fileName.substring(index + 1);
		} else {
			return null;
		}
	}

	/**
	 * the returned path is always ending with a '/' or '\\' char;
	 * 
	 * @param path
	 * @return regular path;
	 */
	public static String getRegularPath(String path) {
		if (path == null || path.length() < 1) {
			return path;
		} else {
			char ch = path.charAt(path.length() - 1);
			if (ch != '\\' && ch != '/') {
				return path + "/";
			} else {
				return path;
			}
		}
	}

	/**
	 * 将String文本，以UTF-8的格式写到指定文件中
	 * @param info
	 * @param filePath
	 */
	public static void writeInfoToFile(String info, String filePath) {
		try {
			mkdirs(filePath);

			FileOutputStream fos = new FileOutputStream(new File(filePath));
			OutputStreamWriter writer = new OutputStreamWriter(fos, "UTF-8");
			writer.write(info);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}