package com.smartneasy.silvermayat73.View;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.smartneasy.silvermayat73.Adapter.RecyclerAdapter;
import com.smartneasy.silvermayat73.Model.Alumni;
import com.smartneasy.silvermayat73.R;


public class ItemsActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener{

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBListener;
    private List<Alumni> mAlumni;

    private void openDetailActivity(String[] data){
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_items );

        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressBar = findViewById(R.id.myDataLoaderProgressBar);
        mProgressBar.setVisibility(View.VISIBLE);

        mAlumni = new ArrayList<> ();
        mAdapter = new RecyclerAdapter (ItemsActivity.this, mAlumni);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ItemsActivity.this);

        mStorage = FirebaseStorage.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("teachers_uploads");

        mDBListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mAlumni.clear();

                for (DataSnapshot teacherSnapshot : dataSnapshot.getChildren()) {
                    Alumni upload = teacherSnapshot.getValue(Alumni.class);
                    upload.setKey(teacherSnapshot.getKey());
                    mAlumni.add(upload);
                }
                mAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ItemsActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
    public void onItemClick(int position) {
        Alumni clickedAlumni = mAlumni.get(position);
        String[] teacherData={clickedAlumni.getName(), clickedAlumni.getDescription(), clickedAlumni.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onShowItemClick(int position) {
        Alumni clickedAlumni = mAlumni.get(position);
        String[] teacherData={clickedAlumni.getName(), clickedAlumni.getDescription(), clickedAlumni.getImageUrl()};
        openDetailActivity(teacherData);
    }

    @Override
    public void onDeleteItemClick(int position) {
        Alumni selectedItem = mAlumni.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = mStorage.getReferenceFromUrl(selectedItem.getImageUrl());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void> () {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ItemsActivity.this, "Item deleted", Toast.LENGTH_SHORT).show();
            }
        });

    }
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBListener);
    }

}