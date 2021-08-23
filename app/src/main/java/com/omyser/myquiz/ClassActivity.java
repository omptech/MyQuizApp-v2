package com.omyser.myquiz;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.omyser.myquiz.BoardActivity.catList;
import static com.omyser.myquiz.BoardActivity.selected_cat_index;

public class ClassActivity extends AppCompatActivity {
    public static int selected_set_index = 0;
    private GridView sets_grid;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;
    ClassAdapter adapter;
    public static List<String> setsIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);

        sets_grid = findViewById(R.id.sets_gridview);


        loadingDialog = new Dialog(ClassActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();

        loadSets();

    }


    public void loadSets()
    {
        loadingDialog.show();
        setsIDs.clear();

        firestore.collection("QUIZ").document(catList.get(selected_cat_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfSets = (long)documentSnapshot.get("SETS");

                for(int i=1; i <= noOfSets; i++)
                {
                    setsIDs.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                }

                adapter = new ClassAdapter(setsIDs.size());
                sets_grid.setAdapter(adapter);

                loadingDialog.dismiss();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ClassActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }
    @Override
    protected void onResume() {
        super.onResume();

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            ClassActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
