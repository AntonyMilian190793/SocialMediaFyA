package com.antonymilian.socialmediafya.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;

public class PostActivity extends AppCompatActivity {

    ImageView mImagenViewPost1;
    File mImageFile;
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

    TextView mTextViewCategory;
    private final int GALLERY_REQUEST_CODE = 1;

    String mCategory = "";
    String mTitle = "";
    String mDescription = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mImageProvider = new ImageProvider();
        mPostProvider = new PostProvider();
        mAuthProvider = new AuthProvider();

        mImagenViewPost1 = findViewById(R.id.imageViewPost1);
        mButtonPost = findViewById(R.id.btnPost);
        mTextTitle = findViewById(R.id.textInputvideoGame);
        mTextDescription = findViewById(R.id.textInpuDescription);
        mImageViewNoticias = findViewById(R.id.imageViewNoticias);
        mImageViewInforme = findViewById(R.id.imageViewInforme);
        mImageViewColegios = findViewById(R.id.imageViewColegios);
        mImageViewOtros = findViewById(R.id.imageViewOtros);
        mTextViewCategory = findViewById(R.id.textViewCategory);

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
                openGalLery();
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

    private void clickPost() {
         mTitle = mTextTitle.getText().toString();
         mDescription = mTextDescription.getText().toString();

        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty()){
            if(mImageFile != null){
                saveImage();
            }else{
                Toast.makeText(this, "Debe seleccionar una imagen!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Complete todo los campos para poder publicar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImage() {
        mImageProvider.save(PostActivity.this, mImageFile).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            Post post = new Post();
                            post.setImage1(url);
                            post.setTitle(mTitle);
                            post.setDescripcion(mDescription);
                            post.setCategory(mCategory);
                            post.setIdUser(mAuthProvider.getUid());
                            mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> taskSave) {

                                    if(taskSave.isSuccessful()){
                                        Toast.makeText(PostActivity.this, "La información se almacen´correctamente!", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(PostActivity.this, "No se pudo almacenar la información!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    //Toast.makeText(PostActivity.this, "La imagen se almaceno correctamente!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(PostActivity.this, "Hubo un error al almacenar la imagen!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openGalLery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK){
            try {
                mImageFile = FileUtil.from(this, data.getData());
                mImagenViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImageFile.getAbsolutePath()));
            }catch (Exception e){
                Log.d("ERROR", "Se produjo un error!" + e.getMessage());
                Toast.makeText(this, "Se produjo un error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}