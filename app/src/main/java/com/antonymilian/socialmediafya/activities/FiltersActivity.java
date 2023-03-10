package com.antonymilian.socialmediafya.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.antonymilian.socialmediafya.R;
import com.antonymilian.socialmediafya.adapters.PostsAdapter;
import com.antonymilian.socialmediafya.models.Post;
import com.antonymilian.socialmediafya.providers.AuthProvider;
import com.antonymilian.socialmediafya.providers.PostProvider;
import com.antonymilian.socialmediafya.utils.ViewedMessageHelper;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class FiltersActivity extends AppCompatActivity {

    String mExtraCategory;

    AuthProvider mAuthProvider;
    RecyclerView mRecyclerView;
    PostProvider mPostProvider;
    PostsAdapter mPostAdapter;
    Toolbar mToolbar;
    TextView mTextViewNumerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        mExtraCategory = getIntent().getStringExtra("category");

        mRecyclerView = findViewById(R.id.recycleViewFilter);
        mAuthProvider = new AuthProvider();
        mPostProvider = new PostProvider();
        mToolbar = findViewById(R.id.toolbar);
        mTextViewNumerFilter = findViewById(R.id.textViewnumberFilter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Filtros");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(FiltersActivity.this, 2));

        Toast.makeText(this, "La categoria que seleccionó es " + mExtraCategory, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Query query = mPostProvider.getPostByCategoryAndTimestamp(mExtraCategory);
        FirestoreRecyclerOptions<Post> options = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        mPostAdapter = new PostsAdapter(options, FiltersActivity.this, mTextViewNumerFilter);
        mRecyclerView.setAdapter(mPostAdapter);
        mPostAdapter.startListening();
        ViewedMessageHelper.updateOnline(true, FiltersActivity.this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPostAdapter.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ViewedMessageHelper.updateOnline(false, FiltersActivity.this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

}


