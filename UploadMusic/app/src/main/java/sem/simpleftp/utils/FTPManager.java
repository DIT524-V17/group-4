package sem.simpleftp.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import sem.simpleftp.FTPConfig;

import android.util.Log;


public class FTPManager {
	private static final String TAG = "FTPManager";
	private static FTPManager mFtpManager;
	private FTPClient mFtpClient;
	private FTPSClient mFtpsClient;
	private static FTPConfig mConfig;
	private IRetrieveListener retrieveListener;
	private boolean isFTPS = false;
	volatile boolean isLogin = false;
	volatile boolean isStopDownload = false;

	private FTPManager() {
		mFtpClient = new FTPClient();
		mFtpsClient = new FTPSClient(false);
		mFtpsClient.setTrustManager(TrustManagerUtils
				.getAcceptAllTrustManager());
	}

	public static FTPManager getInstance(FTPConfig cfg) {
		if (mFtpManager == null) {
			mFtpManager = new FTPManager();
		}
		mConfig = cfg;
		return mFtpManager;
	}

	public static FTPManager getInstance(String host, int port) {
		if (mFtpManager == null) {
			mFtpManager = new FTPManager();
		}
		mConfig = new FTPConfig(host, port);
		return mFtpManager;
	}

	public void setRetrieveListener(IRetrieveListener retrieveListener) {
		this.retrieveListener = retrieveListener;
	}

	/**
	 *
	 * 
	 * @return
	 */
	public boolean connectFTPServer() {
		try {
			FTPClientConfig ftpClientCfg = new FTPClientConfig(
					FTPClientConfig.SYST_UNIX);
			ftpClientCfg.setLenientFutureDates(true);
			mFtpClient.configure(ftpClientCfg);
			mFtpClient.setConnectTimeout(15000);
			mFtpClient.connect(mConfig.ipAdress, mConfig.port);
			login();
			int replyCode = mFtpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				mFtpClient.disconnect();
				return false;
			}
		} catch (SocketException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 *
	 * @return
	 */
	public boolean login() {
		try {
			if (mFtpClient.isConnected()) {
				boolean isLogin = mFtpClient.login(mConfig.user, mConfig.pwd);
				if (!isLogin) {
					return false;
				}
				mFtpClient.setControlEncoding("GBK");
				mFtpClient.setFileType(FTPClient.FILE_STRUCTURE);
				mFtpClient.enterLocalActiveMode();
				// mFtpClient.enterRemotePassiveMode();
				// mFtpClient.enterRemoteActiveMode(
				// InetAddress.getByName(mConfig.ipAdress), mConfig.port);
				mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);
				return isLogin;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void close() {
		try {
			if (mFtpClient.isConnected()) {
				mFtpClient.logout();
			}
			mFtpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *
	 * @param remoteFileName
	 * @param localFileName
	 * @param currentSize
	 */
	public void downLoadFile(String remoteFileName, String localFileName,
			long currentSize) {
		Log.i(TAG, "downloadFile fileName=" + remoteFileName + " currentSize="
				+ currentSize);
		if (retrieveListener != null) {
			retrieveListener.onStart();
		}
		byte[] buffer = new byte[mConfig.bufferSize];
		int len = -1;
		long now = -1;
		boolean append = false;
		if (mFtpClient != null) {
			InputStream ins = null;
			FileOutputStream fos = null;
			try {
				File localFile = new File(localFileName);
				if (currentSize > 0) {
					mFtpClient.setRestartOffset(currentSize);
					now = currentSize;
					append = true;
				}
				ins = getRemoteFileStream(remoteFileName);
				fos = new FileOutputStream(localFile, append);
				if (ins == null) {
					throw new FileNotFoundException("remote file does not exist");
				}
				while ((len = ins.read(buffer)) != -1) {
					if (isStopDownload) {
						break;
					}
					fos.write(buffer, 0, len);
					now += len;
					retrieveListener.onTrack(now);

				}
				if (isStopDownload) {
					retrieveListener.onCancel();
				} else {
					if (mFtpClient.completePendingCommand()) {
						retrieveListener.onDone();
					} else {
						retrieveListener.onError(ERROR.DOWNLOAD_ERROR,
								"download fail");
					}
				}
			} catch (FileNotFoundException e) {
				retrieveListener.onError(ERROR.FILE_NO_FOUNT, "download fail:"
						+ e);
			} catch (IOException e) {
				retrieveListener.onError(ERROR.IO_ERROR, "download fail:" + e);
			} finally {
				try {
					ins.close();
					fos.close();
				} catch (Exception e2) {
				}
			}
		}
	}
	/**
	 *
	 * @param remotePath
	 * @param localPath
	 */
	public void downLoadFile(String remotePath, String localPath) {
		downLoadFile(remotePath, localPath, -1);
	}

	private InputStream getRemoteFileStream(String remoteFilePath) {
		InputStream is = null;
		try {
			is = mFtpClient.retrieveFileStream(remoteFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
	}
	/**
	 *
	 * @param localPath
	 * @param workDirectory
	 * @param desFileName
	 * @return
	 */
	public boolean uploadFile(String localPath, String workDirectory,
			String desFileName) {
		Log.i(TAG, "uploadFile localPath=" + localPath + " desFileName="
				+ desFileName);
		if (retrieveListener != null) {
			retrieveListener.onStart();
		}
		try {
			if (mFtpClient != null && mFtpClient.isConnected()) {

				mFtpClient.makeDirectory(workDirectory);
				mFtpClient.changeWorkingDirectory(workDirectory);
				mFtpClient.setBufferSize(1024);

				FileInputStream fis = new FileInputStream(localPath);
				boolean isUploadSuccess = mFtpClient
						.storeFile(desFileName, fis);
				if (isUploadSuccess) {
					if (retrieveListener != null) {
						retrieveListener.onDone();
					}
				} else {
					if (retrieveListener != null) {
						retrieveListener.onError(ERROR.UPLOAD_ERROR,
								"upload fail");
					}
				}
				fis.close();
				return isUploadSuccess;
			}
		} catch (IOException e) {
			e.printStackTrace();
			if (retrieveListener != null) {
				retrieveListener.onError(ERROR.IO_ERROR, "upload error:" + e);
			}
		}
		return false;
	}

	/**
	 *
	 * 
	 * @param localFileName
	 *
	 * @param remoteFileName
	 *
	 * @return
	 */
	public boolean uploadFile(String localFileName, String remoteFileName) {
		return uploadFile(localFileName, "/", remoteFileName);
	}

	public FTPFile[] getFTPFiles() {
		try {
			if(!mFtpClient.isConnected()){
				return null;
			}
			mFtpClient.changeToParentDirectory();
			return mFtpClient.listFiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean deleteFile(String pathname){
		try {
			return mFtpClient.deleteFile(pathname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean createDirectory(String pathname){
		try {
			return mFtpClient.makeDirectory(pathname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public FTPFile[] getFTPFiles(String remoteDir) {
		try {
			if(!mFtpClient.isConnected()){
				return null;
			}
			return mFtpClient.listFiles(remoteDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean isStopDownload() {
		return isStopDownload;
	}

	public void setStopDownload(boolean isStopDownload) {
		this.isStopDownload = isStopDownload;
	}

	public boolean isFTPS() {
		return isFTPS;
	}

	public void setFTPS(boolean isFTPS) {
		if (isFTPS) {
			mFtpClient = mFtpsClient;
		} else {
			mFtpClient = new FTPClient();
		}
		this.isFTPS = isFTPS;
	}

	public interface IRetrieveListener {
		public void onStart();

		public void onTrack(long curPos);

		public void onError(int errorCode, String errorMsg);

		public void onCancel();

		public void onDone();
	}

	public static class ERROR {
		public static final int FILE_NO_FOUNT = 4000;
		public static final int FILE_DOWNLOAD_ERROR = 4001;
		public static final int LOGIN_ERROR = 4002;
		public static final int CONNECT_ERROR = 4003;
		public static final int IO_ERROR = 4004;
		public static final int DOWNLOAD_ERROR = 4005;
		public static final int UPLOAD_ERROR = 4006;
	}
}
