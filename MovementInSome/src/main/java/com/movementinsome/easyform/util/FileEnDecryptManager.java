package com.movementinsome.easyform.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * ���ܽ��ܹ�����
 * 
 * �����㷨 : ���ļ����������ÿ���ֽ�����ֽڵ��±����.
 * �����㷨 : �Ѿ����ܵ��ļ���ִ��һ�ζ��ļ����������ÿ���ֽ�����ֽڵ��±����
 * 
 * @author Administrator
 * 
 */
public class FileEnDecryptManager {

	private FileEnDecryptManager() {
	}

	private static FileEnDecryptManager instance = null;

	public static FileEnDecryptManager getInstance() {
		synchronized (FileEnDecryptManager.class) {
			if (instance == null)
				instance = new FileEnDecryptManager();
		}
		return instance;
	}

	/**
	 * ��¼�ϴν��ܹ���ļ���
	 */
	private final String LastDecryptFile = /*Framework
			.getModule(DownloadModule.class).getDownloadDir().getAbsolutePath()+*/
			"/LastDecryptFilename.ttt";

	/**
	 * LastDecryptFilename.ttt �ļ��Ƿ����
	 */
	private boolean isClear = false;

	/**
	 * �������
	 * 
	 * @param fileUrl
	 *            �ļ����·��
	 * @return
	 */
	public boolean InitEncrypt(String fileUrl) {
		encrypt(fileUrl);
		return true;
	}

	private final int REVERSE_LENGTH = 56;

	/**
	 * �ӽ���
	 * 
	 * @param strFile
	 *            Դ�ļ����·��
	 * @return
	 */
	private boolean encrypt(String strFile) {
		int len = REVERSE_LENGTH;
		try {
			File f = new File(strFile);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			long totalLen = raf.length();

			if (totalLen < REVERSE_LENGTH)
				len = (int) totalLen;

			FileChannel channel = raf.getChannel();
			MappedByteBuffer buffer = channel.map(
					FileChannel.MapMode.READ_WRITE, 0, REVERSE_LENGTH);
			byte tmp;
			for (int i = 0; i < len; ++i) {
				byte rawByte = buffer.get(i);
				tmp = (byte) (rawByte ^ i);
				buffer.put(i, tmp);
			}
			buffer.force();
			buffer.clear();
			channel.close();
			raf.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * �������
	 * 
	 * @param fileUrl
	 *            Դ�ļ����·��
	 */
	public void Initdecrypt(String fileUrl) {
		try {
			if (isDecripted(fileUrl)) {
				decrypt(fileUrl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void decrypt(String fileUrl) {
		encrypt(fileUrl);
	}

	/**
	 * fileName �ļ��Ƿ��Ѿ�������
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private boolean isDecripted(String fileName) throws IOException {
		// �ϴμ��ܵ��ļ�
		File lastDecryptFile = new File(LastDecryptFile);
		if (lastDecryptFile.exists() && isClear == false) {
			String lastDecryptfilepath = getLastDecryptFilePath(LastDecryptFile);
			if (lastDecryptfilepath != null
					&& lastDecryptfilepath.equals(fileName)) {
				return false;
			} else {
				clear();
			}
		}
		StringBufferWrite(fileName);
		return true;
	}

	/**
	 * ����Ҫ���ܵ��ļ����·��д��LastDecryptFile
	 * 
	 * @param filePath
	 *            ��Ҫ���ܵ��ļ����·��
	 * @param content
	 * @throws IOException
	 */
	private void StringBufferWrite(String filePath) throws IOException {
		File lastDecryptFile = new File(LastDecryptFile);
		if (!lastDecryptFile.exists())
			lastDecryptFile.createNewFile();
		FileOutputStream out = new FileOutputStream(lastDecryptFile, true);
		StringBuffer sb = new StringBuffer();
		sb.append(filePath);
		out.write(sb.toString().getBytes("utf-8"));
		out.close();
	}

	/**
	 * ��ռ��ܼ�¼
	 */
	public synchronized void clear() {
		isClear = true;
		File decryptTempFile = new File(LastDecryptFile);
		if (decryptTempFile.exists()) {
			try {
				String fileName = getLastDecryptFilePath(LastDecryptFile);
				decrypt(fileName);
				new File(LastDecryptFile).delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		isClear = false;
	}

	/**
	 * ��LastDecryptFile�ж�ȡ��¼
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	private String getLastDecryptFilePath(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String str = br.readLine();
		br.close();
		return str;
	}
}

