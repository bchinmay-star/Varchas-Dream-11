package com.example.varchasdream11.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.varchasdream11.Matches.Match;
import com.example.varchasdream11.Matches.Sports;
import com.example.varchasdream11.R;
import com.example.varchasdream11.TeamPreview.TeamPreviewActivity;
import com.example.varchasdream11.TeamSelection.TeamSelectionActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MymatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MymatchesFragment extends Fragment {

    private RecyclerView MyMatchesRecyclerList;
    private DatabaseReference MyMatchRef;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MymatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MymatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MymatchesFragment newInstance(String param1, String param2) {
        MymatchesFragment fragment = new MymatchesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_mymatches, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MyMatchRef = FirebaseDatabase.getInstance().getReference().child("User matches").child(user.getUid());

        MyMatchesRecyclerList = (RecyclerView) root.findViewById(R.id.myMatchesRecyclerList);
        MyMatchesRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<MyMatch> options = new FirebaseRecyclerOptions.Builder<MyMatch>()
                .setQuery(MyMatchRef, MyMatch.class)
                .build();

        FirebaseRecyclerAdapter<MyMatch, MyMatchViewHolder> adapter = new FirebaseRecyclerAdapter<MyMatch, MyMatchViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull final MyMatchViewHolder holder, final int position, @NonNull final MyMatch model) {
                holder.teamText1.setText(model.getTeamName1());
                holder.teamText2.setText(model.getTeamName2());
                //holder.matchTime.setText(model.getMatchTime());

                Picasso.get().load(model.getTeamImage1()).placeholder(R.drawable.cricket_logo_remastered).into(holder.teamImage1);
                Picasso.get().load(model.getTeamImage2()).placeholder(R.drawable.cricket_logo_remastered).into(holder.teamImage2);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String match_id = getRef(position).getKey();

                        Intent matchIntent = new Intent(getActivity(), TeamPreviewActivity.class);
                        matchIntent.putExtra("match_id", match_id);
                        matchIntent.putExtra("teamName1", model.getTeamName1());
                        matchIntent.putExtra("teamName2", model.getTeamName2());
                        startActivity(matchIntent);


                    }
                });

            }

            @NonNull
            @Override
            public MyMatchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mymatches_sample, viewGroup, false);
                return new MyMatchViewHolder(view);
            }


        };

        MyMatchesRecyclerList.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyMatchViewHolder extends RecyclerView.ViewHolder {

        TextView teamText1, teamText2;
        ImageView teamImage1, teamImage2;

        public MyMatchViewHolder(@NonNull View itemView) {
            super(itemView);

//            matchTime = itemView.findViewById(R.id.matchTime);
            teamText1 = itemView.findViewById(R.id.teamText1);
            teamText2 = itemView.findViewById(R.id.teamText2);
            teamImage1 = itemView.findViewById(R.id.teamImage1);
            teamImage2 = itemView.findViewById(R.id.teamImage2);
        }
    }
}