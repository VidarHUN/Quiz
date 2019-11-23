package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartingScreenActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_QUIZ = 1;

    private TextView textViewScore;
    private TextView textViewTitle;

    private int score;

    private DatabaseReference database;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_screen);

        textViewScore = findViewById(R.id.text_view_score);
        textViewTitle = findViewById(R.id.title);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true); //Kicsi gyorsaság miatt kell

        loadScore();

        nameFromDatabase();

        /**
         * Kezdő gomb létrehozása és játékindítás listener beállítása
         */
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        /**
         * Kijelentkezés
         */
        Button buttonLogout = findViewById(R.id.logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(StartingScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        Button buttonLeaderboard = findViewById(R.id.leaderboard);
        buttonLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartingScreenActivity.this, LeaderboardActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    /**
     * Ezzel váltunk a már rendes játékra Intenttel
     */
    private void startQuiz() {
        Intent intent = new Intent(StartingScreenActivity.this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    /**
     * Pontszám frissítése
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                updateScore(score);
            }
        }
    }

    /**
     * Pontszám betöltésére szolgál
     */
    private void loadScore() {
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true); //Kicsi gyorsaság miatt kell
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key.equals(user.getUid())) {
                    score = Integer.parseInt(dataSnapshot.child("score").getValue().toString());
                    textViewScore.setText("Score: " + score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * Frissíti a kiírandó pontszámot
     *
     * @param scoreNew
     */
    private void updateScore(int scoreNew) {
        score += scoreNew;
        textViewScore.setText("Score: " + score);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        database.child("score").setValue(score);
    }

    /**
     * Név lekérése és felhasználó üdvözlése
     */
    private void nameFromDatabase() {
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                if (key.equals(user.getUid())) {
                    name = dataSnapshot.child("name").getValue().toString();
                    textViewTitle.setText("Welcome, " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
