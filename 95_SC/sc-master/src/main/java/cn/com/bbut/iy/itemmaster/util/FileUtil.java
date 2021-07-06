package cn.com.bbut.iy.itemmaster.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Component
public final class FileUtil {
    /**
     * @desc 将源文件/文件夹生成指定格式的压缩文件,格式zip
     * @param resourcesFiles 源文件
     * @param targetPath  目的压缩文件保存路径
     * @return void
     * @throws Exception
     */
    public static void compressedFile(List<String> resourcesFiles, String targetPath,String oldZipFile) throws Exception{
        //删除旧文件
        if(!StringUtil.IsBlank(oldZipFile)){
            log.info("delete oldZipFile:"+oldZipFile+" start");
            File deleteOldZipFile = new File(oldZipFile);
            //文件存在则删除
            log.info("file exists:"+deleteOldZipFile.exists()+"  is file:"+deleteOldZipFile.isFile());
            if(deleteOldZipFile.exists() && deleteOldZipFile.isFile()){
                boolean deleteSuccess = deleteOldZipFile.delete();
                log.info("delete oldZipFile:"+deleteSuccess);
            }
            log.info("delete oldZipFile:"+oldZipFile+" end");
        }

        File targetFile = new File(targetPath);           //目的
        //如果目的路径不存在，则新建
        if(!targetFile.getParentFile().exists()){
            targetFile.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(targetPath);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));

        createCompressedFile(out, resourcesFiles);

        out.close();
    }

    /**
     * @desc 生成压缩文件。
     * 	     如果是文件夹，则使用递归，进行文件遍历、压缩
     *       如果是文件，直接压缩
     * @param out  输出流
     * @param resourcesFiles  目标文件
     * @return void
     * @throws Exception
     */
    public static void createCompressedFile(ZipOutputStream out,List<String> resourcesFiles) throws Exception{
        //打包处理文件
        for (String resourcesFile:resourcesFiles) {
            String[] resourcesFileInfo = resourcesFile.split(":");
            if(resourcesFileInfo.length == 2){
                File file = new File(resourcesFileInfo[0]);
                //文件输入流
                FileInputStream fis = new FileInputStream(file);
                String zipFileName = resourcesFileInfo[1]+file.getName().substring(file.getName().lastIndexOf("."));
                out.putNextEntry(new ZipEntry(zipFileName));
                //进行写操作
                int j =  0;
                byte[] buffer = new byte[1024];
                while((j = fis.read(buffer)) > 0){
                    out.write(buffer,0,j);
                }
                //关闭输入流
                fis.close();
            }
        }
    }

    /** 不使用ftp上传所以有关ftp方法都删除
     * 上传文件到ftp
     * resoursePath:资源文件路径
     * uploadPath:上传文件路径
     */
    /*public static void uploadFileToFtp(String resoursePath,String uploadPath,String oldFtpPath)throws Exception{
        FTPClient ftp = connFtp();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        ftp.setControlEncoding("UTF-8");
        // 删除旧文件
        if(oldFtpPath != null && !oldFtpPath.trim().equals("")){
            String oldUploadDir = oldFtpPath.substring(0, oldFtpPath.lastIndexOf("\\"));
            String oldUploadFileName = oldFtpPath.substring(oldFtpPath.lastIndexOf("\\")+1);
            // 进入文件所在目录，注意编码格式，以能够正确识别中文目录
            ftp.changeWorkingDirectory(new String(oldUploadDir.getBytes("UTF-8"),FTP.DEFAULT_CONTROL_ENCODING));
            // 删除指定文件
            ftp.deleteFile(oldUploadFileName);
            // 切换到根目录
            ftp.changeWorkingDirectory("/");
        }
        //上传新文件
        File localFile = new File(resoursePath);
        InputStream inStream;
        OutputStream outStream;
        String remotePath = new String(uploadPath.getBytes("UTF-8"), FTP.DEFAULT_CONTROL_ENCODING);
        inStream = new FileInputStream(localFile);
        //
        // 小文件直接 用
        // ftp.storeFile(remotePath, inStream);
        //大文件时用这个方法
        String uploadDir = uploadPath.substring(0, uploadPath.lastIndexOf("\\"));
        if(!ftp.changeWorkingDirectory(uploadDir)){
            if(!makeDir(ftp,uploadDir)){
                throw new Exception("FTP Make Dir filed!");
            }
        }
        outStream = ftp.storeFileStream(remotePath);
        // 大文件,可以自己掌握进度
        byte[] buffer = new byte[4000];
        while (true)
        {
            int n = inStream.read(buffer);
            if (n <= 0)
                break;
            outStream.write(buffer, 0, n);
        }
        inStream.close();
        outStream.close();
        ftp.disconnect();
    }

    public static FTPClient connFtp() throws Exception{
        FTPClient ftp = new FTPClient();
        String ftpHost = getProperty("ftp.host");
        String ftpPort = getProperty("ftp.port");
        ftp.connect(ftpHost,Integer.parseInt(ftpPort));
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("can not connect to ftp,please check your ip and port!");
        }
        String ftpUserName = getProperty("ftp.username");
        String ftpPassword = getProperty("ftp.password");
        boolean loginSuccess = ftp.login(ftpUserName, ftpPassword);
        if(!loginSuccess){
            throw new Exception("login ftp filed please check your name and password!");
        }
        return ftp;
    }

    public static String getProperty(String key){
        Yaml yaml = new Yaml();
        URL url = FileUtil.class.getClassLoader().getResource("application.yaml");
        if (url != null) {
            try{
                //也可以将值转换为Map
                Map map =(Map)yaml.load(new FileInputStream(url.getFile()));
                String[] keys = key.split("\\.");
                List<String> listKeys= Arrays.asList(keys);
                listKeys = new ArrayList(listKeys);
                return getPropertyMapValue(listKeys,map);
            }catch (Exception ex){
                log.error("FileUtil.getProperty get application.yaml failed!error:"+ex.getMessage());
            }
        }
        return "";
    }

    public static String getPropertyMapValue(List<String> keys,Map map){
        for(int i=0;i<keys.size();i++){
            String key = keys.get(i);
            Object value = map.get(key);
            if(value instanceof Map) {
                keys.remove(i);
                return getPropertyMapValue(keys,(Map) value);
            }else {
                return value.toString();
            }
        }
        return "";
    }

    *//**
     * ftp创建目录——ftpClient只支持一级一级创建
     * @param ftp
     * @param path
     * @return
     * @throws IOException
     *//*
    public static boolean makeDir(FTPClient ftp,String path) throws IOException {
        //分割
        String[] paths = path.split("\\\\");
        //创建成功标识
        boolean isMakeSucess=false;
        //遍历每一级路径
        for (String str : paths) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            //切换目录，根据切换是否成功判断子目录是否存在
            boolean changeSuccess = ftp.changeWorkingDirectory(str);
            isMakeSucess = changeSuccess;
            //该级路径不存在就创建并切换
            if (!changeSuccess) {
                isMakeSucess = ftp.makeDirectory(str);
                ftp.changeWorkingDirectory(str);
            }
        }
        return isMakeSucess;
    }

    *//**
     * ftp删除一个月以前的文件夹
     * @param path
     * @return
     * @throws IOException
     */
    public static boolean delOneMonthAgoDir(String path) throws Exception {
        //创建成功标识
        boolean isMakeSucess=true;
        //删除目录
        File fileDirectory = new File(path);
        //判断是否文件夹
        if(!fileDirectory.isDirectory()){
            //不是则提示
            throw new Exception("the delete update files directory is not directory");
        }
        File[] files = fileDirectory.listFiles();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        for (File deleteFile:files){
            //是文件夹并且为8位
            if(deleteFile.isDirectory() && deleteFile.getName().length() == 8){
                //判断日期是否与现在相差一个月
                int month = getMonthSpace(nowDate,deleteFile.getName());
                //如果大于则删除
                if(month > 1){
                    Boolean deleteSucces = deleteDir(deleteFile);
                    if(!deleteSucces){
                        //删除失败则提示
                        throw new Exception("the delete update files directory one month ago failed!the directory name:"+deleteFile.getName());
                    }
                }
            }
        }
        return isMakeSucess;
    }
    /**
     *
     * @param date1 <String>yyyyMMdd格式
     * @param date2 <String>yyyyMMdd格式
     * @return int 相差月数
     * @throws ParseException
     */
    public static int getMonthSpace(String date1, String date2)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        int result=0;
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(sdf.parse(date1));
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(sdf.parse(date2));
        result =(cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR)) * 12 + cal1.get(Calendar.MONTH)- cal2.get(Calendar.MONTH);
        return result;
    }

    /**
     * FTPClient 删除 Ftp 上的文件夹，包括其中的文件。 pathName = "/" 表示 ftp 根目录
     */
    /*public static boolean removeDirectoryALLFile(FTPClient ftpClient, String pathName) throws IOException {
        try {
            //验证FTP服务器是否登录成功
            int replyCode = ftpClient.getReplyCode();

            if(!FTPReply.isPositiveCompletion(replyCode)){
                return false;
            }
            ftpClient.changeWorkingDirectory(pathName);
            FTPFile[] files = ftpClient.listFiles();

            if (null != files && files.length > 0) {
                for (FTPFile file : files) {
                    if (file.isDirectory()) {
                        removeDirectoryALLFile( ftpClient, pathName + "/" + file.getName() );
                    } else {
                        if (!ftpClient.deleteFile(pathName + "/" + file.getName())) {
                            return false;
                        }
                    }
                }

            }
            // 切换到父目录，不然删不掉文件夹
            ftpClient.changeWorkingDirectory( pathName.substring(0, pathName.lastIndexOf("\\")) );
            ftpClient.removeDirectory(pathName);
        } catch (IOException e) {
            throw e;
        }
        return true;
    }*/

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
