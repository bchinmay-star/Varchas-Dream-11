package com.example.varchasdream11.TeamPreview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varchasdream11.MainActivity;
import com.example.varchasdream11.R;
import com.example.varchasdream11.TeamSelection.Team;
import com.example.varchasdream11.TeamSelection.TeamSelectionActivity;
import com.example.varchasdream11.databinding.ActivitySignInBinding;
import com.example.varchasdream11.databinding.ActivityTeamPreviewBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class TeamPreviewActivity extends AppCompatActivity {
    ActivityTeamPreviewBinding binding;
    private String team_name1;
    private String team_name2;
    private DatabaseReference TeamRef;
    private RecyclerView PreviewRecyclerList;
    public Button ViewLeaderboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamPreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        team_name1 = getIntent().getExtras().get("teamName1").toString();
        team_name2 = getIntent().getExtras().get("teamName2").toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TeamRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team_name1+"vs"+team_name2);
        PreviewRecyclerList = (RecyclerView) findViewById(R.id.previewRecyclerList);
        PreviewRecyclerList.setLayoutManager(new LinearLayoutManager(this));

        TextView team1 = (TextView) findViewById(R.id.textView5_preview);
        team1.setText(team_name1);

        TextView team2 = (TextView) findViewById(R.id.textView7_preview);
        team2.setText(team_name2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Team> options = new FirebaseRecyclerOptions.Builder<Team>()
                .setQuery(TeamRef, Team.class)
                .build();

        final FirebaseRecyclerAdapter<Team, TeamPreviewActivity.PreviewViewHolder> adapter = new FirebaseRecyclerAdapter<Team, TeamPreviewActivity.PreviewViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final TeamPreviewActivity.PreviewViewHolder holder, final int position, @NonNull final Team model) {
                holder.playerName.setText(model.getPlayerName());
//              holder.playerTeam.setText(model.getPlayerTeam());
                holder.playerCategory.setText(model.getPlayerCategory());
                holder.playerCredits.setText(model.getPlayerCredits());

                ViewLeaderboardButton = findViewById(R.id.viewLeaderboardBtn);

                Picasso.get().load(model.getPlayerImage()).placeholder(R.drawable.cricket_logo_remastered).into(holder.playerImage);
                //Toast.makeText(TeamSelectionActivity.this, position+"Card is selected", Toast.LENGTH_SHORT).show();

                ViewLeaderboardButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
//
//                                    Intent intent = new Intent(TeamSelectionActivity.this, MainActivity.class);
//                                    startActivity(intent);

                    }
                });


            }

            @NonNull
            @Override
            public TeamPreviewActivity.PreviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card, viewGroup, false);
                TeamPreviewActivity.PreviewViewHolder viewHolder = new TeamPreviewActivity.PreviewViewHolder(view);
                return viewHolder;
            }
        };

        PreviewRecyclerList.setAdapter(adapter);
        adapter.startListening();

    }

    public static class PreviewViewHolder extends RecyclerView.ViewHolder {

        //LinearLayout layout_ref;
        TextView playerName, playerCategory, playerCredits;
        ImageView playerImage;

        public PreviewViewHolder(@NonNull View itemView) {
            super(itemView);

            //layout_ref = (LinearLayout)itemView.findViewById(R.id.team_1_lin_layout);
            playerName = itemView.findViewById(R.id.playerName);
            playerCategory = itemView.findViewById(R.id.playerCategory);
            playerCredits = itemView.findViewById(R.id.playerCredits);
            playerImage = itemView.findViewById(R.id.playerImage);
        }
    }


}