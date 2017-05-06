package sem.simpleftp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.apache.commons.net.ftp.FTPFile;
import sem.simpleftp.FTPConfig;
import sem.simpleftp.utils.FTPManager;
import sem.simpleftp.utils.FTPManager.IRetrieveListener;
import sem.simpleftp.utils.StringUtils;
import sem.simpleftp.utils.ToastUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int SHOW_DIALOG = 1000;
    private static final int HIDE_DIALOG = 1001;
    private Button mLoginButton;
    private EditText mUserEt;
    private EditText mPasswordEt;
    private Button mDownloadBtn;
    private Button mUploadBtn;

    private FTPManager mFtpManager;
    private InputMethodManager mImm;
    private ProgressDialog mProgressDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SHOW_DIALOG) {
                showProgressDialog(msg.obj == null ? "Please waiting..." : msg.obj
                        .toString());
            } else if (msg.what == HIDE_DIALOG) {
                hideProgressDialog();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        FTPConfig config = new FTPConfig("192.168.1.29", 21);
        config.user = "group4";
        config.pwd = "123456";
        mUserEt.setText(config.user);
        mPasswordEt.setText(config.pwd);
        mFtpManager = FTPManager.getInstance(config);
    }

    private void initView() {
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        mUserEt = (EditText) findViewById(R.id.username_et);
        mPasswordEt = (EditText) findViewById(R.id.password_et);
        mDownloadBtn = (Button) findViewById(R.id.button1);
        mDownloadBtn.setOnClickListener(this);
        mUploadBtn = (Button) findViewById(R.id.button2);
        mUploadBtn.setOnClickListener(this);
    }

    private void showProgressDialog(String content) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this,
                    ProgressDialog.STYLE_HORIZONTAL);
        }
        mProgressDialog.setTitle("prompting message");
        mProgressDialog.setMessage(content);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {
                mFtpManager.close();
            }
        }.start();
        ToastUtil.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // connecting and login  test
            case R.id.login_button:
                loginFtp();
                break;
            // download specified file
            case R.id.button1:
                downloadFile();
                break;
            // upload specified file from Android to FPT server
            case R.id.button2:
                uploadFile();
                break;
        }
    }

    /**
     *  Login function test
     */
    private void loginFtp() {
        mImm.hideSoftInputFromWindow(mPasswordEt.getWindowToken(), 0);
        if (StringUtils.isEmpty(mUserEt.getText().toString().trim())) {
            ToastUtil.showShortToast(this, "Username can not be empty");
            return;
        }
        if (StringUtils.isEmpty(mPasswordEt.getText().toString().trim())) {
            ToastUtil.showShortToast(this, "Password can not be empty");
            return;
        }
        new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "start login!");
                if (mFtpManager.connectFTPServer()) {
                    Log.d(TAG, "connectFTPServer  = true");
                    //view remote FTP server files
                    FTPFile[] files = mFtpManager.getFTPFiles();
                    Log.i(TAG, "files.size="+files.length);
                    for(FTPFile f:files){
                        Log.i(TAG, "file:"+f.getName());
                    }
                    ToastUtil.showShortToast(MainActivity.this, "Connection successful", true);
                }else{
                    Log.d(TAG, "connectFTPServer  = false");
                }
            }
        }.start();
    }
    /**
     * Get a directory for download content
     * @return
     */
    public String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // check if SD card existed
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// get root directory
        }
        return sdDir.toString();
    }

    /**
     * Download function test
     */
    private void downloadFile() {
        new Thread() {
            @Override
            public void run() {
                String localPath = getSDPath();
                if (!StringUtils.isEmpty(localPath)) {
                    localPath = localPath + "/ftp_demo.log";
                } else {
                    localPath = getApplicationContext().getFilesDir()
                            .getAbsolutePath() + "/ftp_demo.log";
                }
                Log.d(TAG, "localPath=" + localPath);
                mFtpManager.setRetrieveListener(new IRetrieveListener() {
                    @Override
                    public void onTrack(long curPos) {
                        Log.d(TAG, "--onTrack--" + curPos);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "--onStart--");
                        mHandler.sendEmptyMessage(SHOW_DIALOG);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.e(TAG, "download error:" + msg);
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                        ToastUtil.showShortToast(getApplicationContext(), "Download error",
                                true);
                    }

                    @Override
                    public void onDone() {
                        Log.i(TAG, "download success");
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                        ToastUtil.showShortToast(MainActivity.this, "Download success",
                                true);
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "download onCancel");
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                    }
                });
                mFtpManager.downLoadFile("/ftp_test.log", localPath);
            }
        }.start();
    }
    /**
     * Upload
     */
    private void uploadFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String localPath = getSDPath();
                if (!StringUtils.isEmpty(localPath)) {
                    localPath = localPath + "/ftp_demo.log";
                } else {
                    localPath = getApplicationContext().getFilesDir()
                            .getAbsolutePath() + "/ftp_demo.log";
                }
                Log.d(TAG, "localPath=" + localPath);
                File file = new File(localPath);
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(file, false);
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "utf-8"));
                    bw.write("FTP Upload Test");
                    bw.flush();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mFtpManager.setRetrieveListener(new IRetrieveListener() {
                    @Override
                    public void onTrack(long curPos) {
                        Log.d(TAG, "upload--onTrack--" + curPos);
                    }

                    @Override
                    public void onStart() {
                        Log.d(TAG, "upload--onStart--");
                        Message msg = mHandler.obtainMessage(SHOW_DIALOG);
                        msg.obj = "Uploading...";
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        Log.e(TAG, "upload error:" + msg);
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                        ToastUtil.showShortToast(MainActivity.this, "Upload error",
                                true);
                    }

                    @Override
                    public void onDone() {
                        Log.i(TAG, "upload success");
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                        ToastUtil.showShortToast(MainActivity.this, "Upload success",true);
                    }

                    @Override
                    public void onCancel() {
                        Log.i(TAG, "upload onCancel");
                        mHandler.sendEmptyMessage(HIDE_DIALOG);
                    }
                });
                mFtpManager.uploadFile(localPath, "ftp_up.log");
            }
        }).start();
    }
}
