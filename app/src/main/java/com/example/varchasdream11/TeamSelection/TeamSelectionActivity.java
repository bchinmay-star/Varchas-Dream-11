package com.example.varchasdream11.TeamSelection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varchasdream11.Fragments.MymatchesFragment;
import com.example.varchasdream11.MainActivity;
import com.example.varchasdream11.Matches.Match;
import com.example.varchasdream11.Matches.Sports;
import com.example.varchasdream11.R;
import com.example.varchasdream11.databinding.ActivitySportsBinding;
import com.example.varchasdream11.databinding.ActivityTeamSelectionBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TeamSelectionActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private String match_receiver_id;
    private RecyclerView Team1RecyclerList;
    private DatabaseReference Team1Ref;
    private String team1_name;
    ActivityTeamSelectionBinding binding;
    private RecyclerView Team2RecyclerList;
    private DatabaseReference Team2Ref;
    private String team2_name;
    public int total_points = 0;
    public int teamA_count = 0;
    public int teamB_count = 0;
    public String color_ref = "#000000";
    public String color_text_ref;
    public String color_ref_2;
    public String color_text_ref_2;
    public int total_players_A = 0;
    public int total_players_B = 0;
    public int curr_cardA_points = 0;
    public int curr_cardB_points = 0;
    public Button ProceedButton;
    List<Map<String, String>> myMap = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTeamSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        team1_name = getIntent().getExtras().get("team1Name").toString();
        team2_name = getIntent().getExtras().get("team2Name").toString();

        Team1Ref = FirebaseDatabase.getInstance().getReference().child("Players").child(team1_name);
        Team2Ref = FirebaseDatabase.getInstance().getReference().child("Players").child(team2_name);

        Team1RecyclerList = (RecyclerView) findViewById(R.id.team1RecyclerList);
        Team1RecyclerList.setLayoutManager(new LinearLayoutManager(this));

        Team2RecyclerList = (RecyclerView) findViewById(R.id.team2RecyclerList);
        Team2RecyclerList.setLayoutManager(new LinearLayoutManager(this));

        TextView team1 = (TextView) findViewById(R.id.textView5);
        team1.setText(team1_name);

        TextView team2 = (TextView) findViewById(R.id.textView7);
        team2.setText(team2_name);

        TextView team1render = (TextView) findViewById(R.id.textView8);
        team1render.setText(team1_name);

        TextView team2render = (TextView) findViewById(R.id.textView9);
        team2render.setText(team2_name);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Team> options1 = new FirebaseRecyclerOptions.Builder<Team>()
                .setQuery(Team1Ref, Team.class)
                .build();

        FirebaseRecyclerOptions<Team> options2 = new FirebaseRecyclerOptions.Builder<Team>()
                .setQuery(Team2Ref, Team.class)
                .build();


        final FirebaseRecyclerAdapter<Team, TeamViewHolder> adapter1 = new FirebaseRecyclerAdapter<Team, TeamViewHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull final TeamViewHolder holder, final int position, @NonNull final Team model) {
                holder.playerName.setText(model.getPlayerName());
//              holder.playerTeam.setText(model.getPlayerTeam());
                holder.playerCategory.setText(model.getPlayerCategory());
                holder.playerCredits.setText(model.getPlayerCredits());
                total_players_A += 1;

                ProceedButton = findViewById(R.id.proceedBtn);

                TextView points_ref = (TextView) findViewById(R.id.textView6);
                points_ref.setText(total_points + "/100");

                TextView teamA_ref = (TextView) findViewById(R.id.textView3);
                teamA_ref.setText(teamB_count + "/" + total_players_A);

                final int[] cardArray = new int[100];
                Picasso.get().load(model.getPlayerImage()).placeholder(R.drawable.cricket_logo_remastered).into(holder.playerImage);


                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @SuppressLint("SetTextI18n")
                    @Override

                    public void onClick(View v) {
                        //Toast.makeText(TeamSelectionActivity.this, position+"Card is selected", Toast.LENGTH_SHORT).show();

                        color_ref = "#ffffff";
                        color_text_ref = "#ffffff";
//                              teamA_count += 1;
                        curr_cardA_points = Integer.parseInt(holder.playerCredits.getText().toString());
                        if (teamA_count < 7 && teamA_count < (11 - teamB_count) && (total_points + curr_cardA_points) <= 100) {
                            cardArray[position] += 1;
                            if ((cardArray[position] % 2 != 0)) {
                                //Toast.makeText(TeamSelectionActivity.this, "if tap", Toast.LENGTH_SHORT).show();
                                teamA_count += 1;
                                color_ref = "#000000";
                                color_text_ref = "#000000";
                                total_points += curr_cardA_points;
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team1_name + model.getPlayerID());
                                myMap.add(myMap1);
//                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team1_name+model.getPlayerID()).child("playerName").setValue(model.getPlayerName());
//                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team1_name+model.getPlayerID()).child("playerCategory").setValue(model.getPlayerCategory());
//                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team1_name+model.getPlayerID()).child("playerCredits").setValue(model.getPlayerCredits());
//                                        //holder.layout_ref.setBackgroundColor(Color.parseColor("#ffffff"));
                            } else {
                                //Toast.makeText(TeamSelectionActivity.this, "else tap", Toast.LENGTH_SHORT).show();
                                teamA_count -= 1;
                                color_ref = "#ffffff";
                                color_text_ref = "#ffffff";
                                total_points -= curr_cardA_points;
                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team1_name + model.getPlayerID());
                                myMap.remove(myMap1);

//                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                        int x=teamA_count+teamB_count;
//                                        FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team1_name+model.getPlayerID()).removeValue();


                            }
                        } else {

                            if ((cardArray[position] % 2 != 0)) {
                                //Toast.makeText(TeamSelectionActivity.this, "final", Toast.LENGTH_SHORT).show();
                                cardArray[position] += 1;
                                teamA_count -= 1;
                                color_ref = "#ffffff";
                                color_text_ref = "#ffffff";
                                total_points -= curr_cardA_points;
                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team1_name + model.getPlayerID());
                                myMap.remove(myMap1);
                            } else {
                                if (total_points + curr_cardA_points > 100) {
                                    Toast.makeText(TeamSelectionActivity.this, "Cannot spend more than 100 credits !", Toast.LENGTH_SHORT).show();
                                } else if (teamB_count + teamA_count == 11) {

                                    Toast.makeText(TeamSelectionActivity.this, "Maximum 11 players overall only", Toast.LENGTH_SHORT).show();
                                } else if (teamA_count == 7 || teamB_count == 7) {
                                    Toast.makeText(TeamSelectionActivity.this, "Maximum 7 from a single team", Toast.LENGTH_SHORT).show();
                                }
                                // This comment was for the sake of pushing commit
                            }
                        }

                        holder.playerCredits.setTextColor(Color.parseColor(color_text_ref));
                        holder.itemView.setBackgroundColor(Color.parseColor(color_ref));
                        holder.playerCategory.setTextColor(Color.parseColor(color_text_ref));

                        TextView points_ref = (TextView) findViewById(R.id.textView6);
                        points_ref.setText(total_points + "/100");

                        TextView teamA_ref = (TextView) findViewById(R.id.textView3);
                        teamA_ref.setText(teamA_count + "/" + total_players_A);

                        ProceedButton.setEnabled(false);

                        if (teamA_count + teamB_count == 11) {
                            ProceedButton.setEnabled(true);
                            ProceedButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View view) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    for (int i = 0; i < myMap.size(); i++) {
                                        Map<String, String> inner = myMap.get(i);
                                        for (Map.Entry<String, String> entry : inner.entrySet()) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name + "vs" + team2_name).child(inner.get("playerID")).child(entry.getKey()).setValue(entry.getValue());
                                            ;
                                        }
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("User matches").child(user.getUid()).child(team1_name + "vs" + team2_name).child("teamName1").setValue(team1_name);
                                    FirebaseDatabase.getInstance().getReference().child("User matches").child(user.getUid()).child(team1_name + "vs" + team2_name).child("teamName2").setValue(team2_name);

//
                                    Intent intent = new Intent(TeamSelectionActivity.this, MainActivity.class);
                                    startActivity(intent);

                                }
                            });
                        }

                    }

                });
            }


            @NonNull
            @Override
            public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card1, viewGroup, false);
                TeamViewHolder viewHolder = new TeamViewHolder(view);
                return viewHolder;
            }
        };

        FirebaseRecyclerAdapter<Team, TeamViewHolder> adapter2 = new FirebaseRecyclerAdapter<Team, TeamViewHolder>(options2) {
            @Override
            protected void onBindViewHolder(@NonNull final TeamViewHolder holder, final int position, @NonNull final Team model) {
                holder.playerName.setText(model.getPlayerName());
                holder.playerCategory.setText(model.getPlayerCategory());
                holder.playerCredits.setText(model.getPlayerCredits());
                total_players_B += 1;

                ProceedButton = findViewById(R.id.proceedBtn);

                TextView points_ref = (TextView) findViewById(R.id.textView6);
                points_ref.setText(total_points + "/100");

                TextView teamB_ref = (TextView) findViewById(R.id.textView4);
                teamB_ref.setText(teamB_count + "/" + total_players_B);

                Picasso.get().load(model.getPlayerImage()).placeholder(R.drawable.cricket_logo_remastered).into(holder.playerImage);

                final int[] cardArray = new int[100];

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(TeamSelectionActivity.this, position+"Card is selected", Toast.LENGTH_SHORT).show();

                        color_ref_2 = "#ffffff";
                        color_text_ref_2 = "#ffffff";
//                              teamA_count += 1;
                        curr_cardB_points = Integer.parseInt(holder.playerCredits.getText().toString());
                        if (teamB_count < 7 && teamB_count < (11 - teamA_count) && (total_points + curr_cardB_points) <= 100) {
                            cardArray[position] += 1;
                            if ((cardArray[position] % 2 != 0)) {
                                //Toast.makeText(TeamSelectionActivity.this, "if tap", Toast.LENGTH_SHORT).show();
                                teamB_count += 1;
                                color_ref_2 = "#000000";
                                color_text_ref_2 = "#000000";
                                total_points += curr_cardB_points;

                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team2_name + model.getPlayerID());

                                myMap.add(myMap1);
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team2_name+model.getPlayerID()).child("playerName").setValue(model.getPlayerName());
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team2_name+model.getPlayerID()).child("playerCategory").setValue(model.getPlayerCategory());
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team2_name+model.getPlayerID()).child("playerCredits").setValue(model.getPlayerCredits());

                                //holder.layout_ref.setBackgroundColor(Color.parseColor("#ffffff"));
                            } else {
                                //Toast.makeText(TeamSelectionActivity.this, "else tap", Toast.LENGTH_SHORT).show();
                                teamB_count -= 1;
                                color_ref_2 = "#ffffff";
                                color_text_ref_2 = "#ffffff";
                                total_points -= curr_cardB_points;
                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team1_name + model.getPlayerID());
                                myMap.remove(myMap1);

//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                int x=teamA_count+teamB_count;
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team2_name+model.getPlayerID()).removeValue();
//

                            }
                        } else {

                            if ((cardArray[position] % 2 != 0)) {
                                //Toast.makeText(TeamSelectionActivity.this, "final", Toast.LENGTH_SHORT).show();
                                cardArray[position] += 1;
                                teamB_count -= 1;
                                color_ref_2 = "#ffffff";
                                color_text_ref_2 = "#ffffff";
                                total_points -= curr_cardB_points;
                                Map<String, String> myMap1 = new HashMap<String, String>();
                                myMap1.put("playerName", model.getPlayerName());
                                myMap1.put("playerCategory", model.getPlayerCategory());
                                myMap1.put("playerCredits", model.getPlayerCredits());
                                myMap1.put("playerID", team1_name + model.getPlayerID());
                                myMap.remove(myMap1);

//                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                int x=teamA_count+teamB_count;
//                                FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name +"vs"+team2_name).child(team2_name+model.getPlayerID()).removeValue();

                            } else {
                                if (total_points + curr_cardB_points > 100) {
                                    Toast.makeText(TeamSelectionActivity.this, "Cannot spend more than 100 credits !", Toast.LENGTH_SHORT).show();
                                } else if (teamB_count + teamA_count == 11) {
                                    Toast.makeText(TeamSelectionActivity.this, "Maximum 11 players overall only", Toast.LENGTH_SHORT).show();
                                } else if (teamA_count == 7 || teamB_count == 7) {

                                    Toast.makeText(TeamSelectionActivity.this, "Maximum 7 from a single team", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }


                        holder.playerCredits.setTextColor(Color.parseColor(color_text_ref_2));
                        holder.itemView.setBackgroundColor(Color.parseColor(color_ref_2));
                        holder.playerCategory.setTextColor(Color.parseColor(color_text_ref_2));

                        TextView points_ref = (TextView) findViewById(R.id.textView6);
                        points_ref.setText(total_points + "/100");


                        TextView teamB_ref = (TextView) findViewById(R.id.textView4);
                        teamB_ref.setText(teamB_count + "/" + total_players_B);

                        ProceedButton.setEnabled(false);

                        if (teamA_count + teamB_count == 11) {

                            ProceedButton.setEnabled(true);
                            ProceedButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    for (int i = 0; i < myMap.size(); i++) {
                                        Map<String, String> inner = myMap.get(i);
                                        for (Map.Entry<String, String> entry : inner.entrySet()) {

                                            FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child(team1_name + "vs" + team2_name).child(inner.get("playerID")).child(entry.getKey()).setValue(entry.getValue());
                                            ;
                                        }
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("User matches").child(user.getUid()).child(team1_name + "vs" + team2_name).child("teamName1").setValue(team1_name);
                                    FirebaseDatabase.getInstance().getReference().child("User matches").child(user.getUid()).child(team1_name + "vs" + team2_name).child("teamName2").setValue(team2_name);

                                    Intent intent = new Intent(TeamSelectionActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                });
            }

            @NonNull
            @Override
            public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.player_card, viewGroup, false);
                TeamViewHolder viewHolder = new TeamViewHolder(view);
                return viewHolder;
            }
        };


        Team1RecyclerList.setAdapter(adapter1);
        adapter1.startListening();

        Team2RecyclerList.setAdapter(adapter2);
        adapter2.startListening();
    }

    public static class TeamViewHolder extends RecyclerView.ViewHolder {

        //LinearLayout layout_ref;
        TextView playerName, playerTeam, playerCategory, playerCredits;
        ImageView playerImage;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            //layout_ref = (LinearLayout)itemView.findViewById(R.id.team_1_lin_layout);
            playerName = itemView.findViewById(R.id.playerName);
            playerCategory = itemView.findViewById(R.id.playerCategory);
            playerCredits = itemView.findViewById(R.id.playerCredits);
            playerImage = itemView.findViewById(R.id.playerImage);
        }
    }


}