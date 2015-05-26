package com.haikuowuya.microlife;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.haikuowuya.microlife.base.BaseActivity;
import com.haikuowuya.microlife.zxing.AmbientLightManager;
import com.haikuowuya.microlife.zxing.BeepManager;
import com.haikuowuya.microlife.zxing.BitmapDecoder;
import com.haikuowuya.microlife.zxing.CameraManager;
import com.haikuowuya.microlife.zxing.CaptureActivityHandler;
import com.haikuowuya.microlife.zxing.DecodeThread;
import com.haikuowuya.microlife.zxing.InactivityTimer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by raiyi-suzhou on 2015/5/21 0021.
 */
public class QRActivity extends BaseActivity
{
    private static final String QR_TITLE="二维码扫描" ;

    private static final int REQUEST_QR_IMAGE_CODE = 11111;
    private static final String TAG = QRActivity.class.getSimpleName();
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private ImageView mIvFlashlight;
    private ImageView mIvPhoto;

    private CallBackImpl mCallBackImpl;
    private AmbientLightManager mAmbientLightManager;
    private boolean mIsFlashLigthOpen;
    private Rect mCropRect = null;
    private AlertDialog mAlertDialog;
    private TextView mTvResult;
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            if (msg.obj != null && msg.obj instanceof Result)
            {
                Result result = (Result) msg.obj;
                handleDecode(result, new Bundle());
            }
        };
    };

    public Handler getHandler()
    {
        return handler;
    }

    public CameraManager getCameraManager()
    {
        return cameraManager;
    }

    private boolean isHasSurface = false;
    public static void actionQR(Activity activity)
    {
        Intent intent = new Intent(activity, QRActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public CharSequence getActivityTitle()
    {
        return QR_TITLE;
    }


    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qr);;// TODO
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        mAmbientLightManager = new AmbientLightManager(this);

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        mIvFlashlight = (ImageView) findViewById(R.id.iv_flashlight);
        mIvPhoto = (ImageView) findViewById(R.id.iv_photo);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mIvFlashlight.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (cameraManager != null)
                {
                    mIsFlashLigthOpen = !mIsFlashLigthOpen;
                    cameraManager.setTorch(mIsFlashLigthOpen);
                }
            }
        });
        mIvPhoto.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(getImageIntent(), REQUEST_QR_IMAGE_CODE);
            }
        });


        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
        cameraManager = new CameraManager(getApplication());
        mAmbientLightManager.start(cameraManager);
        handler = null;
        if (isHasSurface)
        {
            // 暂停而没有销毁时
            initCamera(scanPreview.getHolder());
        }
        else
        {
            // 第一次创建
            mCallBackImpl = new CallBackImpl();
            scanPreview.getHolder().addCallback(mCallBackImpl);
        }
        inactivityTimer.onResume();
    }

    @Override
    protected void onPause()
    {
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }
        if (null != mAlertDialog && mAlertDialog.isShowing())
        {
            mAlertDialog.dismiss();
        }
        inactivityTimer.onPause();
        mAmbientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface)
        {
            scanPreview.getHolder().removeCallback(mCallBackImpl);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult
     *            The contents of the barcode.
     * @param bundle
     *            The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle)
    {
        inactivityTimer.onActivity();// 默认当扫描成功后5分钟后自动结束当前Activity
        beepManager.playBeepSoundAndVibrate();
        final String resultText = rawResult.getText().trim();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", resultText);
        String message = "你想对扫描结果如何处理\n" + resultText;
        String buttonText = "关闭";
        if (Patterns.WEB_URL.matcher(resultText).matches())
        {
            buttonText = "打开链接";
//            searchOrLoad(resultText);
        }
        else
        {
            buttonText = "去搜索";
            mTvResult.setText("扫描详情:"+resultText );
        }
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("扫描结果");
		builder.setMessage(message);
		builder.setPositiveButton("重新扫描", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				restartPreviewAfterDelay(200L);
			}
		});
		builder.setNegativeButton(buttonText,
			new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which)
				{
					searchOrLoad(resultText);
				}
			});
		builder.setCancelable(false);
		mAlertDialog = builder.create();
		mAlertDialog.setOnKeyListener(new DialogInterface.OnKeyListener()
		{
			public boolean onKey(DialogInterface dialog, int keyCode,
				KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_BACK)
				{
					dialog.dismiss();
					restartPreviewAfterDelay(200L);
					return true;
				}
				return false;
			}
		});
		mAlertDialog.show();
    }

    /***
     * 对扫描结果进行处理
     * @param resultText
     */
    private void searchOrLoad(final String resultText)
    {

        if (Patterns.WEB_URL.matcher(resultText).matches())
        {
            String url = resultText;
            if (!url.startsWith("http://"))
            {
                url = "http://" + url;
            }
            WebViewActivity.actionWebView(mActivity, url);

        }

    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        if (surfaceHolder == null)
        {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen())
        {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try
        {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null)
            {
                handler = new CaptureActivityHandler(this, cameraManager,
                        DecodeThread.ALL_MODE);
            }
            initCrop();
        }
        catch (IOException ioe)
        {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        }
        catch (RuntimeException e)
        {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit()
    {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("相机打开出错，请稍后重试");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS)
    {
        mTvResult.setText("");
        if (handler != null)
        {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect()
    {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop()
    {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeightInPX();// 减去状态栏高度

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;
        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;
        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK && requestCode == REQUEST_QR_IMAGE_CODE
                && data != null)
        {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            String imgPath =null;
            if (uri != null)
            {
                ContentResolver resolver = this.getContentResolver();
                String[] columns = { MediaStore.Images.Media.DATA };
                Cursor cursor = null;
                cursor = resolver.query(uri, columns, null, null, null);
                if (18 < Build.VERSION.SDK_INT)
                {// http://blog.csdn.net/tempersitu/article/details/20557383
                    imgPath = uri.getPath();
                    if (!TextUtils.isEmpty(imgPath) && imgPath.contains(":"))
                    {
                        String imgIndex = imgPath.split(":")[1];
                        cursor = resolver.query(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                columns, "_id=?", new String[] { imgIndex }, null);
                    }
                }
                if (null != cursor && cursor.moveToFirst())
                {
                    int columnIndex = cursor.getColumnIndex(columns[0]);
                    imgPath = cursor.getString(columnIndex);
                    cursor.close();
                }
                if(!TextUtils.isEmpty(imgPath))
                {
                    bitmap = scale(imgPath, 320, 480);
                }
            }
            else
            {
                Object object = data.getExtras().get("data");
                if (null != object && object instanceof Bitmap)
                {
                    bitmap = (Bitmap) object;
                }
            }
            if(null != bitmap)
            {
                final Bitmap tmpBitmap = bitmap;
                new Thread(){
                    public void run() {
                        BitmapDecoder bitmapDecoder = new BitmapDecoder(
                                QRActivity.this);
                        Result result = bitmapDecoder.getRawResult(tmpBitmap);
                        Message message = mHandler.obtainMessage();
                        message.obj = result;
                        mHandler.sendMessage(message);
                    };
                }.start();
            }

        }
    }
    public static Bitmap scale(String path, int maxWidth, int maxHeight)
    {
        try
        {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            InputStream input = new FileInputStream(path);
            int sourceWidth = options.outWidth;
            int sourceHeight = options.outHeight;
            input.close();
            float rate = Math.max(sourceWidth / (float) maxWidth, sourceHeight
                    / (float) maxHeight);
            options.inJustDecodeBounds = false;
            options.inSampleSize = (int) rate;
            input = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, options);
            int w0 = bitmap.getWidth();
            int h0 = bitmap.getHeight();
            float scaleWidth = maxWidth / (float) w0;
            float scaleHeight = maxHeight / (float) h0;
            float maxScale = Math.min(scaleWidth, scaleHeight);

            Matrix matrix = new Matrix();
            matrix.reset();
            if (maxScale < 1)
                matrix.postScale(maxScale, maxScale);
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, w0, h0,
                    matrix, true);
            input.close();
            return resizedBitmap;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取状态栏高度单位px
     * */
    public int getStatusBarHeightInPX()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height",
                "dimen", "android");
        if (resourceId > 0)
        {
            TypedValue value = new TypedValue();
            getResources().getValue(resourceId, new TypedValue(), true);
            return (int) (TypedValue.complexToFloat(value.data) * getResources()
                    .getDisplayMetrics().density);
        }
        return result;
    }

    /***请求选择一个图片*/
    public static Intent getImageIntent()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType("image/*");
        return intent;
    }

    private class CallBackImpl implements SurfaceHolder.Callback
    {
        @Override
        public void surfaceCreated(SurfaceHolder holder)
        {
            if (!isHasSurface)
            {
                isHasSurface = true;
                initCamera(holder);
            }
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height)
        {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder)
        {
            isHasSurface = false;
        }

    }

}