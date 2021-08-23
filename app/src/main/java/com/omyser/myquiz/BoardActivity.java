package com.omyser.myquiz;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    public static List<BoardModel> catList = new ArrayList<>();
    public static int selected_cat_index = 0;
    private FirebaseFirestore firestore;
    private GridView catGrid;
    BoardAdapter adapter;
    private Dialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        catGrid = findViewById(R.id.catGridview);

        loadingDialog = new Dialog(BoardActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore = FirebaseFirestore.getInstance();
        loadData();
    }

    private void loadData()
    {
        loadingDialog.show();
        catList.clear();

        firestore.collection("QUIZ").document("Categories")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful())
                {
                    DocumentSnapshot doc = task.getResult();

                    if(doc.exists())
                    {
                        long count = (long)doc.get("COUNT");

                        for(int i=1; i <= count; i++)
                        {
                            String catName = doc.getString("CAT" + String.valueOf(i) + "_NAME");
                            String catID = doc.getString("CAT" + String.valueOf(i) + "_ID");

                            catList.add(new BoardModel(catID,catName));
                        }
                        adapter = new BoardAdapter(catList);
                        catGrid.setAdapter(adapter);

                    }
                    else
                    {
                        Toast.makeText(BoardActivity.this,"No Category Document Exists!",Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
                else
                {

                    Toast.makeText(BoardActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
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
            BoardActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
