package ua.org.algoritm.terminal.ConnectTo1c;

import com.google.android.material.shape.EdgeTreatment;
import com.jcraft.jsch.*;

public class SFTPClient {
    private String host = "";
    private String user = "";
    private String password = "";
    private int port = 22;
    private Session session = null;

    public SFTPClient(String host, String user, String password, int port) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.port = port;
    }

    public void connect() throws JSchException {
        JSch jsch = new JSch();

        session = jsch.getSession(user, host, port);

        // Uncomment the two lines below if the FTP server requires password
        session = jsch.getSession(user, host, port);
        session.setPassword(password);

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
    }

    public void upload(String source, String absolutePath, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;

        String path = "";
        for (String dir : absolutePath.split("/")) {
            if (dir.equals("foto")){
                path = dir;
                continue;
            }
            path = path + "/" + dir;
            try {
                sftpChannel.mkdir(path);
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        sftpChannel.cd(absolutePath);

        sftpChannel.put(source, destination);
        sftpChannel.exit();
    }

    public void download(String source, String destination) throws JSchException, SftpException {
        Channel channel = session.openChannel("sftp");
        channel.connect();
        ChannelSftp sftpChannel = (ChannelSftp) channel;
        sftpChannel.get(source, destination);
        sftpChannel.exit();
    }

    public void disconnect() {
        if (session != null) {
            session.disconnect();
        }
    }
}
