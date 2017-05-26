package sem.simpleftp.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.util.Log;

/**
 * Main class for log.
 * 
 */
public class LogUtils {
    private static final long FILE_SIZE = 1 * 1024 * 1024;
    private static final String LOG_SUFFIX = "_log.txt";
    private static final String ERR_FILE = "Crash.txt";

    private static FileChannel sFileChannel = null;
    private static Context sContext;
    private static String sLogDir;
    private static String sLogFileName = null;
    private final static String TAG = "MyLog";
    private static final int BUFFER_SIZE = 10 * 1024;
    private static ByteBuffer sByteBuffer;
    private static long sLastWriteTime;
    private static String sLastWriteDate = "";

    /** Application tag prefix for LogCat. **/
    private static final String APP_NAME_PREFIX = "yto";

    /** Stores the enabled state of the LogUtils function. **/
    private static Boolean mEnabled = true;

    private static ExecutorService logExecutor = Executors
            .newSingleThreadExecutor();

    /**
     * Write info log string.
     * 
     * @param data
     *            String containing data to be logged.
     */
    public static void logI(final String data) {
        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            writeLog("I", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data);
            Log.i(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " + data);
        }
    }

    /**
     * Write debug log string.
     * 
     * @param data
     *            String containing data to be logged.
     */
    public static void logD(final String data) {
        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            writeLog("D", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data);
            Log.d(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " + data);
        }
    }

    /**
     * Write warning log string.
     * 
     * @param data
     *            String containing data to be logged.
     */
    public static void logW(final String data) {
        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            writeLog("W", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data);
            Log.w(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " + data);
        }
    }

    /**
     * Write error log string.
     * 
     * @param data
     *            String containing data to be logged.
     */
    public static void logE(final String data) {

        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            writeLog("E", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data);
            // temporary fix to avoid crash SMS.
            Log.w(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " + data);
        }
    }

    /**
     * Write verbose log string.
     * 
     * @param data
     *            String containing data to be logged.
     */
    public static void logV(final String data) {
        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            writeLog("V", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data);
            Log.v(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " + data);
        }
    }

    /**
     * Write info log string with specific component name.
     * 
     * @param name
     *            String name string to prepend to log data.
     * @param data
     *            String containing data to be logged.
     */
    public static void logWithName(final String name, final String data) {
        if (mEnabled) {
            writeLog("V", APP_NAME_PREFIX + name, data);
            Log.v(APP_NAME_PREFIX + name, data);
        }
    }

    /**
     * Write error log string with Exception thrown.
     * 
     * @param data
     *            String containing data to be logged.
     * @param exception
     *            Exception associated with error.
     */
    public static void logE(final String data, final Throwable exception) {

        // exception.getClass().toString());
        if (mEnabled) {
            Thread currentThread = Thread.currentThread();
            // temporary fix to avoid crash SMS.
            writeLog("W", APP_NAME_PREFIX, "[" + currentThread.getName() + "] "
                    + data + "   " + exception);
            writeExceptionInLog(APP_NAME_PREFIX + "[" + currentThread.getName()
                    + "] " + data, exception);
            // Log.w(APP_NAME_PREFIX, "[" + currentThread.getName() + "] " +
            // data, exception);
        }
    }

    /***
     * Returns if the logging feature is currently enabled.
     * 
     * @return TRUE if logging is enabled, FALSE otherwise.
     */
    public static Boolean isEnabled() {
        return mEnabled;
    }

    private static boolean sLogEnable = false;

    public static void setLogEnable(boolean enable) {
        sLogEnable = enable;
    }

    public static void v(String tag, String msg) {
        if (sLogEnable) {
            writeLog("V", tag, msg);
            Log.v(tag, msg);
        }
    }

    public static void v(String tag, Exception e) {
        if (sLogEnable) {
            writeLog("V", tag, e.toString());
            Log.v(tag, "" + e);
        }
    }

    public static void v(String tag, String msg, Exception e) {
        if (sLogEnable) {
            writeLog("V", tag, msg + "  " + e);
            Log.v(tag, msg + " " + e);
        }
    }

    public static void i(String tag, String msg) {
        if (sLogEnable) {
            writeLog("i", tag, msg);
            Log.i(tag, msg);
        }
    }

    public static void i(String tag, Exception e) {
        if (sLogEnable) {
            writeLog("I", tag, e.toString());
            Log.i(tag, "" + e);

        }
    }

    public static void i(String tag, String msg, Exception e) {
        if (sLogEnable) {
            writeLog("I", tag, msg + "  " + e);
            Log.i(tag, msg + " " + e);
        }
    }

    public static void d(String tag, String msg) {
        if (sLogEnable) {
            writeLog("D", tag, msg);
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, Exception e) {
        if (sLogEnable) {
            writeLog("D", tag, e.toString());
            Log.d(tag, "" + e);
        }
    }

    public static void d(String tag, String msg, Exception e) {
        if (sLogEnable) {
            writeLog("D", tag, msg + "  " + e);
            Log.d(tag, msg + " " + e);
        }
    }

    public static void w(String tag, String msg) {
        if (sLogEnable) {
            writeLog("W", tag, msg);
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, Exception e) {
        if (sLogEnable) {
            writeLog("W", tag, e.toString());
            Log.w(tag, "" + e);
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (sLogEnable) {
            writeLog("W", tag, msg + "  " + e);
            Log.w(tag, msg + " " + e);
        }
    }

    public static void e(String tag, String msg) {
        if (sLogEnable) {
            writeLog("E", tag, msg);
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, Exception e) {
        if (sLogEnable) {
            writeExceptionInLog(tag, e);
            Log.e(tag, "" + e);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        if (sLogEnable) {
            writeExceptionInLog(tag + "    " + msg, e);
            Log.e(tag, msg + " " + e);
        }
    }

    public static void init(Context context) {
        sContext = context;
        release();

        sLastWriteDate = DateUtils.getFormatedDateTime(LOG_FILE_DATE_FORMAT);

        String dir = sContext.getCacheDir().toString() + "/Logs/";
        sLogDir = dir;
        File dirFile = new File(dir);
        if (!dirFile.isDirectory() && !dirFile.mkdirs()) {
            Log.e(TAG, "Unable to create cache directory " + dir);
        }

        try {
            if (sLogFileName != null && sLogFileName.startsWith(sLogDir)) {
                File temp = new File(sLogFileName);
                if (temp.length() > FILE_SIZE) {
                    sLogFileName = null;
                }
            }

            if (sLogFileName == null) {
                sLogFileName = dir
                        + DateUtils.getFormatedDateTime(LOG_FILE_DATE_FORMAT)
                        + LOG_SUFFIX;
            }

            File temp = new File(sLogFileName);

            if (temp.length() > FILE_SIZE) {
                sFileChannel = new FileOutputStream(sLogFileName).getChannel();
            } else {
                sFileChannel = new FileOutputStream(sLogFileName, true)
                        .getChannel();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        sByteBuffer = ByteBuffer.allocate(BUFFER_SIZE);

    }

    public static synchronized void release() {
        if (sFileChannel != null) {
            try {
                sFileChannel.close();
            } catch (IOException e) {
                e(TAG, e);
            }
            sFileChannel = null;
        }
        if (sByteBuffer != null) {
            sByteBuffer = null;
        }
    }

    public static void flash() {
        try {
            if (sByteBuffer != null && sByteBuffer.position() > 0) {
                sByteBuffer.flip();
                sFileChannel.write(sByteBuffer);
                sByteBuffer.clear();
            }
        } catch (Exception e) {
            e(TAG, e);
        }
    }

    /**
     * Delete redundancy log files
     * 
     * @param keepSize
     *            The files number to keep
     */
    public static void deleteRedundancy(int keepSize) {
        File[] logs = getLogs();
        if (logs == null || logs.length <= keepSize) {
            return;
        }

        List<File> logFiles = Arrays.asList(logs);
        Collections.sort(logFiles);

        for (int i = 0; i < (logs.length - keepSize); i++) {
            logFiles.get(i).delete();
        }
    }

    private static void writeLogByOldIO(String level, String tag, String msg) {

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(level + "  ");
            sb.append(DateUtils.getFormatedDateTime(LOG_HOUR_DATE_FORMAT)
                    + "  " + tag + ":  ");

            // String temp = new String(msg.getBytes(), "utf-8");

            // sb.append(temp);
            sb.append(msg);
            sb.append("\n");

            if (sLogFileName == null) {
                sLogFileName = sLogDir
                        + DateUtils.getFormatedDateTime(LOG_FILE_DATE_FORMAT)
                        + LOG_SUFFIX;
            }

            File file = new File(sLogFileName);

            if (!file.exists() || file.length() > FILE_SIZE) {
                file.delete();
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file, true);
            out.write(sb.toString().getBytes());
            out.close();

        } catch (Exception e) {
            Log.e(tag, "" + e);
        }
    }

    private static void writeLogByNewIO(String level, String tag, String msg) {

        if (sFileChannel == null
                || !sLastWriteDate.equals(DateUtils
                        .getFormatedDateTime(LOG_FILE_DATE_FORMAT))) {
            sLogFileName = null;

            init(sContext);
            if (sFileChannel == null) {
                return;
            }
        }

        try {
            StringBuilder sb = new StringBuilder();
            sb.append(level + "  ");
            sb.append(DateUtils.getFormatedDateTime(LOG_HOUR_DATE_FORMAT)
                    + "  " + tag + ":  ");

            // String temp = new String(msg.getBytes(), "utf-8");

            // sb.append(temp);
            sb.append(msg);
            sb.append("\n");

            if (sb.toString().getBytes().length > BUFFER_SIZE / 2) {
                sFileChannel.write(ByteBuffer.wrap(sb.toString().getBytes()));

            } else {
                sByteBuffer.put(sb.toString().getBytes());
                if ((sByteBuffer.position() > BUFFER_SIZE / 2)
                        || (System.currentTimeMillis() - sLastWriteTime > 10 * 1000)) {
                    sByteBuffer.flip();
                    sFileChannel.write(sByteBuffer);
                    sByteBuffer.clear();
                }
            }
            sLastWriteTime = System.currentTimeMillis();
        } catch (IllegalArgumentException e) {
            sByteBuffer.clear();
            Log.e(TAG, "" + e);
        } catch (Exception e) {
            Log.e(TAG, "" + e);
        }

    }

    private static void writeLog(String level, String tag, String msg) {
        WriteLogRun r = new WriteLogRun();
        r.setTag(tag);
        r.setLevel(level);
        r.setMsg(msg);
        r.setOldIO(true);

        logExecutor.submit(r);
    }

    private static void runWriteExceptiopn(String tag, Throwable ex) {

        Log.e(tag, "", ex);

        StringBuffer buffer = new StringBuffer();
        if (tag != null) {
            buffer.append(tag);
            buffer.append("\n");
        }

        String date = DateUtils.getFormatedDateTime(LOG_ITEM_DATE_FORMAT);
        buffer.append(date);
        buffer.append("\n");

        if (StringUtils.isEmpty(sLogDir)) {
            sLogDir = sContext.getCacheDir().toString() + "/Logs/";
        }

        if (sLogFileName == null) {
            sLogFileName = sLogDir
                    + DateUtils.getFormatedDateTime(LOG_FILE_DATE_FORMAT)
                    + LOG_SUFFIX;
        }

        File file = new File(sLogFileName);

        try {
            if (!file.exists() || file.length() > FILE_SIZE) {
                file.delete();
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file, true);
            out.write(buffer.toString().getBytes());
            PrintStream p = new PrintStream(out);
            ex.printStackTrace(p);
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private static void writeExceptionInLog(String tag, Throwable ex) {
        WriteLogRun r = new WriteLogRun();
        r.setEx(ex);
        r.setTag(tag);
        r.setToCrash(false);

        logExecutor.submit(r);
    }

    private static void runWriteCrash(String tag, Throwable ex) {
        Log.e(tag, "", ex);

        StringBuffer buffer = new StringBuffer();
        if (tag != null) {
            buffer.append(tag);
            buffer.append("\n");
        }

        String date = DateUtils.getFormatedDateTime(LOG_ITEM_DATE_FORMAT);
        buffer.append(date);
        buffer.append("\n");

        File file = new File(sLogDir + ERR_FILE);

        try {
            if (!file.exists() || file.length() > FILE_SIZE) {
                file.delete();
                file.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(file, true);
            out.write(buffer.toString().getBytes());
            PrintStream p = new PrintStream(out);
            ex.printStackTrace(p);

            p.println();
            p.println();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public static void writeCrashException(String tag, Throwable ex) {
        WriteLogRun r = new WriteLogRun();
        r.setEx(ex);
        r.setTag(tag);
        r.setToCrash(true);

        logExecutor.submit(r);
    }

    /**
     * get log files
     * 
     * @return
     */
    public static File[] getLogs() {
        File logDirectory = new File(sLogDir);
        if (logDirectory.isDirectory()) {
            return logDirectory.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().endsWith(LOG_SUFFIX))
                        return true;
                    else
                        return false;
                }
            });
        } else
            return null;
    }

    private static class WriteLogRun implements Runnable {

        private String level;
        private String tag;
        private String msg;
        private Throwable ex;

        private boolean toCrash;
        private boolean isOldIO;

        @Override
        public void run() {
            if (ex == null) {
                if (isOldIO) {
                    writeLogByOldIO(level, tag, msg);
                } else {
                    writeLogByNewIO(level, tag, msg);
                }
            } else {
                if (toCrash) {
                    runWriteCrash(tag, ex);
                } else {
                    runWriteExceptiopn(tag, ex);
                }
            }

        }

        public void setOldIO(boolean isOldIO) {
            this.isOldIO = isOldIO;
        }

        public void setToCrash(boolean toCrash) {
            this.toCrash = toCrash;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setEx(Throwable ex) {
            this.ex = ex;
        }

    }

    private static final String LOG_FILE_DATE_FORMAT = "yyyy-MM-dd";
    private static final String LOG_HOUR_DATE_FORMAT = "HH:mm:ss:SSS";
    private static final String LOG_ITEM_DATE_FORMAT = LOG_FILE_DATE_FORMAT
            + " " + LOG_HOUR_DATE_FORMAT;
}
