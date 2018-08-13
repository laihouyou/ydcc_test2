package com.movementinsome.kernel.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

public class DeCompressUtil {

	public static void unrar(String srcPath, String unrarPath, String type)
			throws RarException, IOException {
		if (type.equals("rar")) {
			unrar(new File(srcPath), unrarPath);
		} else if (type.equals("zip")) {
			unZip(srcPath, unrarPath);
		}

	}

	public static void unrar(File srcFile, String unrarPath)
			throws RarException, IOException {
		if (null == unrarPath || "".equals(unrarPath)) {
			unrarPath = srcFile.getParentFile().getPath();
		}
		System.out.println("unrar file to :" + unrarPath);
		FileOutputStream fileOut;
		File file;
		Archive rarfile = new Archive(srcFile);
		FileHeader entry = rarfile.nextFileHeader();
		while (entry != null) {

			String entrypath = "";
			if (entry.isUnicode()) {// // 解決中文乱码
				entrypath = entry.getFileNameW().trim();
			} else {
				entrypath = entry.getFileNameString().trim();
			}

			entrypath = entrypath.replaceAll("\\\\", "/");

			file = new File(unrarPath + "/" + entrypath);
			System.out.println("unrar entry file :" + file.getPath());
			if (entry.isDirectory()) {
				file.mkdirs();
			} else {
				File parent = file.getParentFile();
				if (parent != null && !parent.exists()) {
					parent.mkdirs();
				}
				fileOut = new FileOutputStream(file);
				rarfile.extractFile(entry, fileOut);
				fileOut.close();
			}
			entry = rarfile.nextFileHeader();
		}
		rarfile.close();

	}

	/**
	 * 解压指定的ZIP文件
	 * 
	 * @param unZipFileName
	 *            文件名字符串（包含路径）
	 * @param outputDirectory
	 *            解压后存放目录
	 */
	public static void unZip(String archive, String decompressDir)
			throws IOException, FileNotFoundException, ZipException {
		BufferedInputStream bi;
		ZipFile zf = new ZipFile(archive);
		Enumeration e = zf.entries();
		while (e.hasMoreElements()) {
			ZipEntry ze2 = (ZipEntry) e.nextElement();
			String entryName = ze2.getName();
			String path = decompressDir + "/" + entryName;
			if (ze2.isDirectory()) {
				System.out.println("正在创建解压目录 - " + entryName);
				File decompressDirFile = new File(path);
				if (!decompressDirFile.exists()) {
					decompressDirFile.mkdirs();
				}
			} else {
				System.out.println("正在创建解压文件 - " + entryName);
				String fileDir = path.substring(0, path.lastIndexOf("/"));
				File fileDirFile = new File(fileDir);
				if (!fileDirFile.exists()) {
					fileDirFile.mkdirs();
				}
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(decompressDir + "/" + entryName));
				bi = new BufferedInputStream(zf.getInputStream(ze2));
				byte[] readContent = new byte[1024];
				int readCount = bi.read(readContent);
				while (readCount != -1) {
					bos.write(readContent, 0, readCount);
					readCount = bi.read(readContent);
				}
				bos.close();
			}
		}
		zf.close();
	}
}
