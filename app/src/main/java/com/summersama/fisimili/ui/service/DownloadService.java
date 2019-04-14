package com.summersama.fisimili.ui.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.summersama.fisimili.utils.FNApplication;
import com.summersama.fisimili.utils.ProgressResponseBody;
import okhttp3.*;

import java.io.*;

/**
 * @author Loveloliii
 */
public class DownloadService extends Service {
    private static final String TAG = DownloadService.class.getCanonicalName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if(url == null){
            url ="";
        }
        Log.i(TAG, "onStartCommand: "+url);
        //启动下载任务
        new DownLoadTask().execute(url);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * 下载任务
     */
    private class DownLoadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            startDownload(params);
            return null;
        }
    }

    private void startDownload(String[] params) {
        Request request = new Request.Builder()
                .url(params[0])
                .build();
        final ProgressResponseBody.ProgressListener listener = (bytesRead, contentLength, done) -> {
            final int percent = (int) (100 * bytesRead / contentLength);
            Intent intent = new Intent();
            intent.setAction("updateR");
            intent.putExtra("progress",percent);
            //发送广播
            sendBroadcast(intent);
            Log.d(TAG,"下载："+bytesRead+"--->"+contentLength+":"+(100*bytesRead)/contentLength+"%");
        };
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    return response.newBuilder()
                            .body(new ProgressResponseBody(response.body(),listener))
                            .build();
                }).build();
        //发送响应
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                save(response);
            }
        });
    }

    private void save(Response response) {
        /* long length = response.body().contentLength();
       if (length == 0){
            Intent i = install(null);
            if (PApplication.topActivity().getPackageManager().queryIntentActivities(i, 0).size() > 0) {
                PApplication.topActivity().startActivityForResult(i, 99);
            }
            return;
        }*/
        // 保存文件到本地
        InputStream is = null;
        RandomAccessFile randomAccessFile = null;
        BufferedInputStream bis = null;

        byte[] buff = new byte[2048];
        int len = 0;
        try {
            is = response.body().byteStream();
            bis  =new BufferedInputStream(is);

            File file = getFile();
            // 随机访问文件，可以指定断点续传的起始位置
            randomAccessFile =  new RandomAccessFile(file, "rwd");
            //randomAccessFile.seek (startsPoint);
            while ((len = bis.read(buff)) != -1) {
                randomAccessFile.write(buff, 0, len);
            }

            // 下载完成
            Intent i = install();
            if (i != null && context.getPackageManager().queryIntentActivities(i, 0).size() > 0) {
                FNApplication.topActivity().startActivityForResult(i, 99);
            }else {
                FNApplication.topActivity().runOnUiThread(()-> Toast.makeText(activity,"can not download",Toast.LENGTH_SHORT).show());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (bis != null){
                    bis.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
    private Context activity =   FNApplication.getContext();
    private Context context = FNApplication.getContext();
    private Intent install() {
        File x =getFile();
        if (!x.exists()){
            return null;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            Uri fileUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".fileProvider", x);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(x), "application/vnd.android.package-archive");
        }
        return intent;
    }

    private File getFile() {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = activity.getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path =activity.getCacheDir().getAbsolutePath();
        }
        File file = new File(path+File.separator+"install"+File.separator+"app-release.apk");
        File parentPath = new File(path+File.separator+"install");
        if(!parentPath.exists()){
            parentPath.mkdirs();
        }

        return file;
    }

}