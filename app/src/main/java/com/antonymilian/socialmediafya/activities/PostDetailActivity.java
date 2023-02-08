package com.antonymilian.socialmediafya.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.adapters.SliderAdapter;
import com.antonymilian.socialmediafya.models.SliderItem;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {

    SliderView mSliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItems = new ArrayList<>();
    PostProvider mPostProvider;
    UsersProvider mUsersProvider;

    String mExtraPostId;

    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewNameCategory;
    ImageView mImageViewCategory;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;

    CircleImageView mCircleImageViewBack;
    FloatingActionButton mFabComent;

    String mIdUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mSliderView = findViewById(R.id.imageSlider);

        mTextViewTitle = findViewById(R.id.textViewTitle);
        mTextViewDescription = findViewById(R.id.textViewDescription);
        mTextViewUsername = findViewById(R.id.textViewUsername);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewNameCategory = findViewById(R.id.textViewNameCategory);
        mImageViewCategory = findViewById(R.id.imageViewCategory);
        mCircleImageViewProfile = findViewById(R.id.circleImageProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);
        mCircleImageViewBack = findViewById(R.id.cicleImageBack);
        mFabComent = findViewById(R.id.fabComent);


        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mPostProvider = new PostProvider();
        mUsersProvider = new UsersProvider();

        mExtraPostId = getIntent().getStringExtra("id");

        getPost();

        mFabComent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComent();
            }
        });

        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToShowProfile();
            }
        });
    }

    private void showDialogComent() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu Comentario");

        final EditText editText = new EditText( PostDetailActivity.this);
        editText.setHint("Texto");


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36, 0, 36, 36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);

        alert.setView(container);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString();
            }
        });
        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void goToShowProfile() {

        if(!mIdUser.equals("")){
            Intent intent = new Intent(PostDetailActivity.this, UserProfileActivity.class);
            intent.putExtra("idUser", mIdUser);
            startActivity(intent);
        }else{
            Toast.makeText(this, "El id del usuario aún no se carga", Toast.LENGTH_SHORT).show();
        }

    }

    private void instanceSlider() {
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this, mSliderItems);
        mSliderView.setSliderAdapter(mSliderAdapter);
        mSliderView.setIndicatorAnimation(IndicatorAnimationType.THIN_WORM);
        mSliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mSliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        mSliderView.setIndicatorSelectedColor(Color.WHITE);
        mSliderView.setIndicatorUnselectedColor(Color.GRAY);
        mSliderView.setScrollTimeInSec(3);
        mSliderView.setAutoCycle(true);
        mSliderView.startAutoCycle();
    }

    private void getPost() {
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {

                    if (documentSnapshot.contains("image1")) {
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image1);
                        mSliderItems.add(item);
                    }
                    if (documentSnapshot.contains("image2")) {
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImageUrl(image2);
                        mSliderItems.add(item);
                    }

                    if(documentSnapshot.contains("title")){
                        String title = documentSnapshot.getString("title");
                        mTextViewTitle.setText(title.toUpperCase());
                    }
                    if(documentSnapshot.contains("descripcion")){
                        String descripcion = documentSnapshot.getString("descripcion");
                        mTextViewDescription.setText(descripcion);
                    }
                    if(documentSnapshot.contains("category")){
                        String category = documentSnapshot.getString("category");
                        mTextViewNameCategory.setText(category);

                        if(category.equals("Noticias")){
                            mImageViewCategory.setImageResource(R.drawable.ic_noticias);
                        }else if(category.equals("Informe")){
                            mImageViewCategory.setImageResource(R.drawable.ic_informe);
                        }else if(category.equals("Colegios")){
                            mImageViewCategory.setImageResource(R.drawable.ic_colegio);
                        }else if(category.equals("Otros")){
                            mImageViewCategory.setImageResource(R.drawable.ic_otros);
                        }
                    }
                    if(documentSnapshot.contains("idUser")){
                        mIdUser = documentSnapshot.getString("idUser");
                        getUserInfo(mIdUser);
                    }


                    instanceSlider();
                }
            }
        });
    }

    private void getUserInfo(String idUser){
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    if(documentSnapshot.contains("username")){
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if(documentSnapshot.contains("phone")){
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone.setText(phone);
                    }
                    if(documentSnapshot.contains("image_profile")){
                        String imagePrfile = documentSnapshot.getString("image_profile");
                        Picasso.with(PostDetailActivity.this).load(imagePrfile).into(mCircleImageViewProfile);
                    }
                }

            }
        });

    }
}