package com.esansoft.oasis.ui.settings_profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.esansoft.base.base_activity.BaseActivity;
import com.esansoft.base.base_header.BaseHeader;
import com.esansoft.base.util.ClsDateTime;
import com.esansoft.base.util.ClsImage;
import com.esansoft.oasis.R;

import java.io.File;
import java.util.Locale;

public class ProfileMain extends BaseActivity {
    //======================
    // Final
    //======================
    private final int TAKE_PHOTO = 0;
    private final int ALBUM = 1;
    private final int DELETE_PHOTO = 2;

    // 사진첨부
    private final int REQUEST_CODE_ALBUM_PHOTO = 204;
    // 사진첨부 사진 촬영
    private final int REQUEST_CODE_PHOTO_TAKE_PHOTO = 205;
    private final int REQUEST_CODE_CROP = 206;
    private final int REQUEST_CODE_CROP_ALBUM = 207;
    // 사진 타입
    private final int MEDIA_TYPE_IMAGE = 1;
    // 동영상 타입
    private final int MEDIA_TYPE_VIDEO = 2;


    //======================
    // Layout
    //======================
    private BaseHeader header;
    private ImageView imgProfilePhoto;
    private Button btnChangePhoto;

    //======================
    // Variable
    //======================
    private File fileTakePhoto;
    private Uri uriPhoto;
    private Uri uriAlbum;
    private String mBase64 = "";
    private String mUserImage = "";


    //======================
    // Initialize
    //======================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        header = findViewById(R.id.header);
        header.btnHeaderLeft.setOnClickListener(v -> finish());
        imgProfilePhoto = findViewById(R.id.imgProfilePhoto);
        if (Build.VERSION.SDK_INT >= 21) {
            imgProfilePhoto.setClipToOutline(true);
        }

        btnChangePhoto = findViewById(R.id.btnChangePhoto);
        btnChangePhoto.setOnClickListener(v -> setUserPhoto());
    }

    private void setUserPhoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        final String str[] = {
                "사진 찍기",
                "사진 선택",
                "사진 삭제"
        };

        builder.setTitle("프로필 사진 변경").setNegativeButton(R.string.common_cancel, null)
                .setItems(str, (dialog, which) -> {
                    switch (which) {
                        // 사직 찍기
                        case TAKE_PHOTO:
                            clickSendPhotoTakePhoto();
                            break;
                        // 사진 선택
                        case ALBUM:
                            clickSendPhotoAlbumPhoto();
                            break;
                        // 사직 삭제
                        case DELETE_PHOTO:
                            ClsImage.setUserPhoto(mContext, imgProfilePhoto, "", R.drawable.main_profile_no_image);
                            mBase64 = "";
                            mUserImage = "";
                            break;
                    }
                }).setCancelable(false).create();

        builder.show();
    }

    @Override
    protected void initialize() {
        String mUserImage = "";
        // 프로필 이미지 설정
        ClsImage.setUserPhoto(mContext, imgProfilePhoto, mUserImage, R.drawable.main_profile_no_image);
    }

    /**
     * 사진첨부 찍어서 보내기
     */
    protected void clickSendPhotoTakePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileTakePhoto = getOutputMediaFile(MEDIA_TYPE_IMAGE);
        uriPhoto = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", fileTakePhoto);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
        startActivityForResult(intent, REQUEST_CODE_PHOTO_TAKE_PHOTO);
    }

    /**
     * 사진첨부 앨범에서 사진 보내기
     */
    protected void clickSendPhotoAlbumPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_ALBUM_PHOTO);
    }


    @SuppressLint("SimpleDateFormat")
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Oasis");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Test", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = ClsDateTime.getNow("yyyyMMddHHmmss", Locale.US);
        File mediaFile;

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "_IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "_VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * 크롭하기
     */
    private void cropImage() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uriPhoto, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriPhoto);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        this.grantUriPermission("com.android.camera", uriPhoto,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        mActivity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 사진
        if (requestCode == REQUEST_CODE_CROP && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriPhoto);
                imgProfilePhoto.setImageBitmap(bitmap);
                mBase64 = ClsImage.getBase64ImageString(bitmap);
                mUserImage = uriPhoto.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_CROP_ALBUM && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriAlbum);
                imgProfilePhoto.setImageURI(uriAlbum);
                mBase64 = ClsImage.getBase64ImageString(bitmap);
                mUserImage = uriAlbum.getPath();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_CODE_ALBUM_PHOTO:
                        if (data.getData() != null) {
                            try {
                                uriPhoto = data.getData();
                                cropImage();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case REQUEST_CODE_PHOTO_TAKE_PHOTO:
                        if (!fileTakePhoto.exists()) {
                            return;
                        }

                        cropImage();
                        break;
                    case REQUEST_CODE_CROP:
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriPhoto);
                            imgProfilePhoto.setImageBitmap(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        }

    }
}
