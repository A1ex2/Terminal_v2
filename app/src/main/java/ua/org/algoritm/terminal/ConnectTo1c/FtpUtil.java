package ua.org.algoritm.terminal.ConnectTo1c;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;

public class FtpUtil {

    /**
     * Description: 向FTP服务器上传文件
     *
     * @param host     FTP服务器hostname
     * @param port     FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param basePath FTP服务器基础目录
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01。文件的路径为basePath+filePath
     * @param filename 上传到FTP服务器上的文件名
     * @param input    输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String host, int port, String username, String password, String basePath,
                                     String filePath, String filename, InputStream input) {
        boolean result = false;

//        try {
            FTPClient ftp = null;
            ftp = new FTPClient();
            try {
                int reply;
                ftp.connect(host, port);// 连接FTP服务器
                // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
                ftp.login(username, password);// 登录
                reply = ftp.getReplyCode();
                if (!FTPReply.isPositiveCompletion(reply)) {
                    ftp.disconnect();
                    return result;
                }
                //切换到上传目录
                if (!ftp.changeWorkingDirectory(basePath + filePath)) {
                    //去掉前面的/
                    if (filePath.startsWith("/")) {
                        filePath = filePath.substring(1);
                    }
                    //如果目录不存在创建目录
                    String[] dirs = filePath.split("/");
                    String tempPath = basePath;
                    int i = 0;
                    for (String dir : dirs) {
                        if (null == dir || "".equals(dir)) continue;
                        if (i == 0) {
                            tempPath += dir;
                        } else {
                            tempPath = dir;
                        }
                        if (!ftp.changeWorkingDirectory(tempPath)) {
                            if (!ftp.makeDirectory(tempPath)) {
                                return result;
                            } else {
                                ftp.changeWorkingDirectory(tempPath);
                            }
                        }
                        i++;
                    }
                }
                //设置上传文件的类型为二进制类型
                ftp.setFileType(FTP.BINARY_FILE_TYPE);
                //上传文件
                if (!ftp.storeFile(filename, input)) {
                    return result;
                }
                input.close();
                ftp.logout();
                result = true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException ioe) {
                    }
                }
            }

//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        return result;
    }

    /**
     * Description: 从FTP服务器下载文件
     *
     * @param host       FTP服务器hostname
     * @param port       FTP服务器端口
     * @param username   FTP登录账号
     * @param password   FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName   要下载的文件名
     * @param localPath  下载后保存到本地的路径
     * @return
     */
    public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
                                       String fileName, String localPath) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(host, port);
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
            ftp.login(username, password);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                if (ff.getName().equals(fileName)) {
                    File localFile = new File(localPath + "/" + ff.getName());
                    OutputStream is = new FileOutputStream(localFile);
                    ftp.retrieveFile(ff.getName(), is);
                    is.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

//    public static void main(String[] args) throws FileNotFoundException {
//
//        String host = "10.1.1.109";
//        int port = 21;
//        String username = "admin";
//        String password = "admin123";
//        String basePath = "";
//        String filePath = "/lyc/123";
//        String filename = "lycTest2.jpg";
//        InputStream input = new FileInputStream(new File("d:/tmp/dd.jpg"));
//        boolean uploadFile = uploadFile(host, port, username, password, basePath, filePath, filename, input);
//        System.out.println("上传结果" + uploadFile);
////		boolean uploadFile = downloadFile(host, port, username, password, "/Data/ftproot/lyc/123", "lycTest.jpg", "e:/");
////		System.out.println("下载成功!");
//    }

//    public void test() throws FileNotFoundException {
//        String host = "113.209.67.100";
//        int port = 2200;
//        String username = "dxhy";
//        String password = "Dxhy!@#123";
//        String basePath = "";
//        String filePath = "lyc/13";
//        String filename = "lycTest.jpg";
//        InputStream input = new FileInputStream(new File("d:/tmp/abc.png"));
//        boolean uploadFile = uploadFile(host, port, username, password, basePath, filePath, filename, input);
//        System.out.println("上传结果" + uploadFile);
////		boolean uploadFile = downloadFile(host, port, username, password, "/Data/ftproot/lyc/123", "lycTest.jpg", "e:/");
////        System.out.println("下载成功!");
//    }
}