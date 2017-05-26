package sem.simpleftp;

/**
 * Author Qing Lin
 */

public class FTPConfig {
    public FTPConfig(){}
    public FTPConfig(String host,int port) {
        this.ipAdress = host;
        this.port = port;
    }
    public String ipAdress;
    public int port;
    public String user;
    public String pwd;
    public int bufferSize = 1024*4;
}

