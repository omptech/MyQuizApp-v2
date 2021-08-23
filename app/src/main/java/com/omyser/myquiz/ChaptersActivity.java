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
import static com.omyser.myquiz.SubjectActivity.classList;
import static com.omyser.myquiz.SubjectActivity.selected_class_index;
import static com.omyser.myquiz.ClassActivity.setsIDs;
import static com.omyser.myquiz.ClassActivity.selected_set_index;

public class ChaptersActivity extends AppCompatActivity {
    public static int selected_chapter_index = 0;
    private GridView sets_grid;
    private FirebaseFirestore firestore;
    private Dialog loadingDialog;

    public static List<String> ChaptersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapters);

        sets_grid = findViewById(R.id.chap_gridview);


        loadingDialog = new Dialog(ChaptersActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        firestore = FirebaseFirestore.getInstance();

        loadSets();

    }


    public void loadSets()
    {

        ChaptersList.clear();

        firestore.collection("QUIZ").document(catList.get(BoardActivity.selected_cat_index).getId()).collection(setsIDs.get(selected_set_index)).document(classList.get(selected_class_index).getId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                long noOfSets = (long)documentSnapshot.get("SETS");

                for(int i=1; i <= noOfSets; i++)
                {
                    ChaptersList.add(documentSnapshot.getString("SET" + String.valueOf(i) + "_ID"));
                }

                ChaptersAdapter adapter = new ChaptersAdapter(ChaptersList.size());
                sets_grid.setAdapter(adapter);

                loadingDialog.dismiss();

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChaptersActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        loadingDialog.dismiss();
                    }
                });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home)
        {
            ChaptersActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
