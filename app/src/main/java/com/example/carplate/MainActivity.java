package com.example.carplate;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.ml.EM;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {
    public static final String SVM_XML = "svm.xml";
    public static final String ANN_XML = "ann.xml";
    public static final int num = 27;
    public static final String FILE_EXT = ".jpg";
    private static String svmpath = null;
    private static String annpath = null;
    private static String[] imgpath = null;
    private ImageView imageView = null;
    private Bitmap bmp = null;
    private TextView m_text = null;
    private String path = null;
    public static int index = 6;
    static {
        if (!OpenCVLoader.initDebug()) {
        } else {
            System.loadLibrary("imageproc");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFile();
        imageView = (ImageView) findViewById(R.id.image_view);
        m_text = (TextView) findViewById(R.id.myshow);
        bmp = BitmapFactory.decodeFile(imgpath[index]);
        imageView.setImageBitmap(bmp);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static File getRobotCacheFile(Context context, String fileName) throws IOException {
        File cacheFile = new File(context.getCacheDir(), fileName);
        if (cacheFile.exists()) {
            Log.d(MainActivity.class.getSimpleName(), fileName + " is exists");
            return cacheFile;
        }
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new IOException("Could not open " + fileName, e);
        }
        return cacheFile;
    }

    private void loadFile() {
        try {
            svmpath = getRobotCacheFile(getApplication(), SVM_XML).getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            annpath = getRobotCacheFile(getApplication(), ANN_XML).getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            imgpath = new String[num + 1];
            for (int i = 0; i <= num; i++) {
                imgpath[i] = getRobotCacheFile(getApplication(), i + FILE_EXT).getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public void click(View view)  {

        try {
            index = index > num ? 0 : ++index;
            bmp = BitmapFactory.decodeFile(imgpath[index]);
            imageView.setImageBitmap(bmp);
            System.out.println("entering the jni");
            byte[] resultByte = CarPlateDetection.ImageProc(imgpath[index], svmpath, annpath);
            String result = new String(resultByte, "UTF-8");
            System.out.println(result);
            m_text.setText(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
    }
}
