package com.movementinsome.caice.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CreateFiles {

    //	String fileName = AppContext.getInstance().getAppStorePath()+"taskRecord";
    String fileNameTemp =
//		AppContext.getInstance().getAppStorePath()+
            "taskRecord" + "/commitTask" + ".txt";

    public  void CreateFiles(File path) throws IOException {
        //创建文件
        FileChannel fc = null;
        File file = new File(path.toString());
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
//		File[] fileList= file.listFiles();
//		if(fileList.length>0){
//			for (int i = 0; i < fileList.length; i++) {
//				if(fileList[i].length()<5242880){
//					fileNameTemp = fileList[i].getPath();
//					break;
//				}
//			}
//		}else{
//			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "hh:mm:ss");
//			String datetime = tempDate.format(new java.util.Date()).toString();
        fileNameTemp = path + "/采测数据" + ".txt";
//		}
    }

    public void CreateFiles2(File path, String name) throws IOException {
        //创建文件
        FileChannel fc = null;
        File file = new File(path.toString());
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
//		File[] fileList= file.listFiles();
//		if(fileList.length>0){
//			for (int i = 0; i < fileList.length; i++) {
//				if(fileList[i].length()<5242880){
//					fileNameTemp = fileList[i].getPath();
//					break;
//				}
//			}
//		}else{
//			SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "hh:mm:ss");
//			String datetime = tempDate.format(new java.util.Date()).toString();
        fileNameTemp = path + "/" + name + ".txt";
//		}
    }

    /**
     * 复制单个文件
     * @param oldPath
     * @param newPath
     */
    public static void mCopyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {
                InputStream inStream = new FileInputStream(oldfile);//读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    System.out.print(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
//                oldfile.delete();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("复制文件异常", e.toString());
        }
    }

    /**
     * 复制单个文件
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public  void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) {     //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                oldfile.delete();
            }
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 拷贝文件夹下所有文件到指定目录（不包括文件夹）
     * @param src
     * @param des
     */
    public static void CopyFolderAllFiles(String src, String des) {
        File file1=new File(src);
        File[] fs=file1.listFiles();
        File file2=new File(des);
        if(!file2.exists()){
            file2.mkdirs();
        }
        for (File f : fs) {
            if(f.isFile()){
                fileCopy(f.getPath(),des+"\\"+f.getName()); //调用文件拷贝的方法
            }
//            else if(f.isDirectory()){
//                copy(f.getPath(),des+"\\"+f.getName());
//            }
        }

    }

    /**
     * 文件拷贝的方法
     */
    private static void fileCopy(String src, String des) {

        BufferedReader br=null;
        PrintStream ps=null;

        try {
            br=new BufferedReader(new InputStreamReader(new FileInputStream(src)));
            ps=new PrintStream(new FileOutputStream(des));
            String s=null;
            while((s=br.readLine())!=null){
                ps.println(s);
                ps.flush();
            }

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{

            try {
                if(br!=null)  br.close();
                if(ps!=null)  ps.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    }

    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static boolean fileCopy(String oldFilePath,String newPath,String newName) throws IOException {
        //如果原文件不存在
        if(fileExists(oldFilePath) == false){
            return false;
        }
        //获得原文件流
        FileInputStream inputStream = new FileInputStream(new File(oldFilePath));
        byte[] data = new byte[1024];
        File newFile=new File(newPath,newName);
        if (!newFile.exists()){
            newFile.mkdirs();
        }
        //输出流
        FileOutputStream outputStream =new FileOutputStream(newFile);
        //开始处理流
        while (inputStream.read(data) != -1) {
            outputStream.write(data);
        }
        inputStream.close();
        outputStream.close();
        return true;
    }

    public void print(String str) {
        //把数据写入文件中
        FileWriter fw = null;
        BufferedWriter bw = null;
        String datetime = "";
        try {
            SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd" + " " + "HH:mm:ss");
            datetime = tempDate.format(new java.util.Date()).toString();
            fw = new FileWriter(fileNameTemp, true);
            // 创建FileWriter对象,用来写入字符流
            bw = new BufferedWriter(fw);//将缓冲对文件的输出
            String myreadline = "录入时间：" + datetime + " " + str;

            bw.write(myreadline + "\n" + " " + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void print2(String str) {
        //把数据写入文件中
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(fileNameTemp, true);
            // 创建FileWriter对象,用来写入字符流
            bw = new BufferedWriter(fw);//将缓冲对文件的输出

            bw.write(str + "\n"); // 写入文件
            bw.newLine();
            bw.flush(); // 刷新该流的缓冲
            bw.close();
            fw.close();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            try {
                bw.close();
                fw.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    //读文件
    public static String readSDFile(String fileName) throws IOException {

        File file = new File(fileName);

        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();

        byte[] buffer = new byte[length];
        fis.read(buffer);

        String in = new String(buffer);

        fis.close();
        return in;
    }

    //读文件
    public static String readFile(File file) throws IOException {


        FileInputStream fis = new FileInputStream(file);

        int length = fis.available();

        byte[] buffer = new byte[length];
        fis.read(buffer);

        String in = new String(buffer);

        fis.close();
        return in;
    }

    //写文件
    public void writeSDFile(String fileName, String write_str) throws IOException {

        File file = new File(fileName);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }

    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectorys(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return true;
    }

    // 1.获取SDCard中某个目录下图片路径集合
    public static List<String> getPictures(final String strPath) {
        List<String> list = new ArrayList<String>();
        File file = new File(strPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        File[] allfiles = file.listFiles();
        if (allfiles == null) {
            return list;
        }
        for (int k = 0; k < allfiles.length; k++) {
            final File fi = allfiles[k];
            if (fi.isFile()) {
                int idx = fi.getPath().lastIndexOf(".");
                if (idx <= 0) {
                    continue;
                }
                String suffix = fi.getPath().substring(idx);
                if (suffix.toLowerCase().equals(".jpg") ||
                        suffix.toLowerCase().equals(".jpeg") ||
                        suffix.toLowerCase().equals(".bmp") ||
                        suffix.toLowerCase().equals(".png") ||
                        suffix.toLowerCase().equals(".gif")) {
                    list.add(fi.getPath());
                }
            }
        }
        return list;
    }

    public static Date getFileCreateTime(File f) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(
                    getFileCreateTimeStr(f));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getFileCreateTimeStr(File f) {
        BufferedReader br = null;
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c dir \""
                        + f.getAbsolutePath() + "\" /tc");

            br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = null;
            for (int i = 0; i < 6; i++) {
                line = br.readLine();
            }

            return line.substring(0, 17).replace("/", "-").replace("  ", " ") + ":00";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }

    }
    //list集合去重
    public static <T> List<T> removeDuplicateWithList(List<T> list) {
        Set<T> set = new HashSet<T>();
        List<T> newList = new ArrayList<T>();
        for (Iterator<T> iter = list.iterator(); iter.hasNext();) {
            T element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }

    /**
     *  从assets目录中复制整个文件夹内容
     *  @param  context  Context 使用CopyFiles类的Activity
     *  @param  oldPath  String  原文件路径  如：/aa
     *  @param  newPath  String  复制后路径  如：xx:/bb/cc
     */
    public static void copyFilesFassets(Context context, String oldPath, String newPath) throws IOException {
        String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
        if (fileNames.length>=0){       //说明是文件
            InputStream is = context.getAssets().open(oldPath);
            File newFile=new File(newPath);
            if (!newFile.exists()){
                newFile.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(newFile);
            byte[] buffer = new byte[1024];
            int byteCount=0;
            while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }
    }

}