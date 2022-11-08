package com.antonymilian.socialmediafya.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.utils.FileUtil;
import com.antonymilian.socialmediafya.providers.ImageProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PostActivity extends AppCompatActivity {

    ImageView mImagenViewPost1;
    ImageView mImagenViewPost2;
    File mImageFile;
    File mImageFile2;
    Button mButtonPost;
    ImageProvider mImageProvider;
    PostProvider mPostProvider;
    AuthProvider mAuthProvider;

    TextInputEditText mTextTitle;
    TextInputEditText mTextDescription;

    ImageView mImageViewNoticias;
    ImageView mImageViewInforme;
    ImageView mImageViewColegios;
    ImageView mImageViewOtros;
    CircleImageView mCircleImageBack;
    TextView mTextViewCategory;
    AlertDialog mDialog;

    String mCategory = "";
    String mTitle = "";
    String mDescription = "";

    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;

    //foto 1
    String mAbsolutePhotoPath;
    String mPhotoPath;
    File mPhotoFile;

    //foto 2
    String mAbsolutePhotoPath2;
    String mPhotoPath2;
    File mPhotoFile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mDialog = new SpotsDialog(PostActivity.this, "Espere por favor");

        mBuilderSelector = new AlertDialog.Builder(this);
        mBuilderSelector.setTitle("Selecciona una opción");
        options = new CharSequence[]{"Imagen de galeria", "Tomar foto"};

        mImagenViewPost1 = findViewById(R.id.imageViewPost1);
        mImagenViewPost2 = findViewById(R.id.imageViewPost2);
        mButtonPost = findViewById(R.id.btnPost);
        mTextTitle = findViewById(R.id.textInputvideoGame);
        mTextDescription = findViewById(R.id.textInpuDescription);
        mImageViewNoticias = findViewById(R.id.imageViewNoticias);
        mImageViewInforme = findViewById(R.id.imageViewInforme);
        mImageViewColegios = findViewById(R.id.imageViewColegios);
        mImageViewOtros = findViewById(R.id.imageViewOtros);
        mTextViewCategory = findViewById(R.id.textViewCategory);
        mCircleImageBack = findViewById(R.id.cicleImageBack);

        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //saveImage();
                clickPost();
            }
        });
        mImagenViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectOptionImage(1);
            }
        });

        mImagenViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectOptionImage(2);
            }
        });

        mImageViewNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Noticias";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Informe";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewColegios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Colegios";
                mTextViewCategory.setText(mCategory);
            }
        });

        mImageViewOtros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategory = "Otros";
                mTextViewCategory.setText(mCategory);
            }
        });

    }

    private void SelectOptionImage(final int numberImage) {

        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    if (numberImage == 1) {
                        openGallery(GALLERY_REQUEST_CODE);
                    }
                    else if (numberImage == 2) {
                        openGallery(GALLERY_REQUEST_CODE_2);
                    }
                }
                else if (i == 1){
                    if (numberImage == 1) {
                        takePhoto(PHOTO_REQUEST_CODE);
                    }
                    else if (numberImage == 2) {
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });
        mBuilderSelector.show();
    }

    private void takePhoto(int requestCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);

            }catch (Exception e){
                Toast.makeText(this, "Hubo un error con el archivo" + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            if(photoFile != null){
                Uri photoUri = FileProvider.getUriForFile(PostActivity.this, "com.antonymilian.socialmediafya", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException {
       File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       File photoFile = File.createTempFile(
               new Date() + "_photo",
               ".jpg",
               storageDir
       );

        if (requestCode == PHOTO_REQUEST_CODE) {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_2) {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsolutePhotoPath2 = photoFile.getAbsolutePath();
        }
        return photoFile;
    }

    private void clickPost() {
         mTitle = mTextTitle.getText().toString();
         mDescription = mTextDescription.getText().toString();

        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
// SELECCIONO AMBAS IMAGENES DE LA GALERIA
            if (mImageFile != null && mImageFile2 != null ) {
                saveImage(mImageFile, mImageFile2);
            }
            // TOMO LAS DOS FOTOS DE LA CAMARA
            else if (mPhotoFile != null && mPhotoFile2 != null) {
                saveImage(mPhotoFile, mPhotoFile2);
            }
            else if (mImageFile != null && mPhotoFile2 != null) {
                saveImage(mImageFile, mPhotoFile2);
            }
            else if (mPhotoFile != null && mImageFile2 != null) {
                saveImage(mPhotoFile, mImageFile2);
            }
            else{
                Toast.makeText(this, "Debe seleccionar una imagen!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Complete todo los campos para poder publicar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage(File imageFile1, File imageFile2) {
        mDialog.show();
        mImageProvider.save(PostActivity.this, imageFile1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();

                            mImageProvider.save(PostActivity.this, imageFile2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if(taskImage2.isSuccessful()){
                                        mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2) {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescripcion(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimestamp(new Date().getTime());
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if(taskSave.isSuccessful()){
                                                            clearForm();
                                                            Toast.makeText(PostActivity.this, "La información se almacenó correctamente!", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            Toast.makeText(PostActivity.this, "No se pudo almacenar la información!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }else{
                                        mDialog.dismiss();
                                        Toast.makeText(PostActivity.this, "La imagen 2 no se pudo guardar!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    //Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente!", Toast.LENGTH_LONG).show();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void clearForm() {
        mTextTitle.setText("");
        mTextDescription.setText("");
        mTextViewCategory.setText("");
        mImagenViewPost1.setImageResource(R.drawable.upload_image);
        mImagenViewPost2.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mCategory = "";
        mImageFile = null;
        mImageFile2 = null;
    }

    private void openGallery(int requestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                mPhotoFile = null;
                mImageFile = FileUtil.from(this, data.getData());
                mImagenViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR", "Se produjo un error!" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        if(requestCode == GALLERY_REQUEST_CODE_2 && resultCode == RESULT_OK){
            try {
                mPhotoFile2 = null;
                mImageFile2 = FileUtil.from(this, data.getData());
                mImagenViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImageFile2.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR", "Se produjo un error!" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE && resultCode == RESULT_OK) {
            mImageFile = null;
            mPhotoFile = new File(mAbsolutePhotoPath);
            Picasso.with(PostActivity.this).load(mPhotoPath).into(mImagenViewPost1);
        }

        /**
         * SELECCION DE FOTOGRAFIA
         */
        if (requestCode == PHOTO_REQUEST_CODE_2 && resultCode == RESULT_OK) {
            mImageFile2 = null;
            mPhotoFile2 = new File(mAbsolutePhotoPath2);
            Picasso.with(PostActivity.this).load(mPhotoPath2).into(mImagenViewPost2);
        }
    }
}