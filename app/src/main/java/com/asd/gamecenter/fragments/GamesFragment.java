package com.asd.gamecenter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.asd.gamecenter.R;
import com.asd.gamecenter.adapters.GameAdapter;
import com.asd.gamecenter.model.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GamesFragment extends Fragment {

    private ArrayList<Game> games;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games, container, false);

        final RecyclerView recyclerView = view.findViewById(R.id.rv_games);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        games = new ArrayList<>();

        String url = getString(R.string.api_games);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Game game = new Game();
                        game.setId(jsonObject.getString("id"));
                        game.setName(jsonObject.getString("name"));
                        game.setDescription(jsonObject.getString("description"));
                        game.setGenre(jsonObject.getString("genre"));
                        game.setPrice(jsonObject.getInt("price"));
                        game.setStock(jsonObject.getInt("stock"));
                        game.setRating(jsonObject.getDouble("rating"));

                        games.add(game);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setAdapter(new GameAdapter(getContext(), games));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

        return view;
    }
}
