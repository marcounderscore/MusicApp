package com.example.marco.musicapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.marco.musicapp.R;
import com.example.marco.musicapp.api.adapter.AlbumAdapter;
import com.example.marco.musicapp.api.model.Album;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumContentFragment extends Fragment {
    private final String url="http://192.168.0.26:4000/api/v1/music/album";
    private final String token="SFMyNTY.eyJzaWduZWQiOjE1MTI3NTUxODMsImRhdGEiOjJ9.SdNYgEbqz9bUHUyiqDGZ88a5Rz34jZ7-ouQez4J2kls";

    //String[] productos={"Celular","Tablet","Televisor"};
    List<Album> productos_list =new ArrayList<Album>();
    List<Album> product_list1=new ArrayList<Album>();
    RecyclerView rv_list_product ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_main_recicler, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv_list_product = view.findViewById(R.id.rvproductos_list);

        Toast toast = Toast.makeText(getContext(), url, Toast.LENGTH_SHORT);
        toast.show();

        load_productos();
    }

    private void load_productos() {
        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //Toast.makeText(getContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                /*System.out.println(response.toString());*/
                                genera_lista(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String>  params = new HashMap<String, String>();
                        params.put("Authorization", token);

                        return params;
                    }
                };
        requestQueue.add(jsonArrayRequest);
    }
    public void genera_lista(JSONArray response) {
        String imagen= "https://images.duckduckgo.com/iu/?u=http%3A%2F%2Fcdn.churchm.ag%2Fwp-content%2Fuploads%2F2015%2F01%2FBethel-Music-We-Will-Not-Be-Shaken-cover-art.jpg&f=1";
        List<String> list =new ArrayList<String>();
        for (int i=0;i<response.length();i++) {
            try {
                JSONObject jsonObject =response.getJSONObject(i);
                String dateStr = jsonObject.getString("release");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                JSONArray arr2 = jsonObject.getJSONArray("tracklist");

                /*Toast.makeText(getContext(),
                        jsonObject.getString("title")+"\n"+
                                jsonObject.getString("cover")+"\n"+
                                jsonObject.getInt("sale_price")+"\n"+
                                jsonObject.getInt("purchase_price")+"\n"+
                                jsonObject.getInt("id")+"\n"+
                                jsonObject.getString("release")+"\n"+
                                jsonObject.getJSONArray("tracklist"), Toast.LENGTH_SHORT).show();*/

                //Date birthDate;

                product_list1.add(new Album(
                        jsonObject.getString("title"),
                        jsonObject.getString("cover"),
                        jsonObject.getInt("sale_price"),
                        jsonObject.getInt("purchase_price"),
                        jsonObject.getInt("id"),
                        jsonObject.getString("release"),
                        jsonObject.getJSONArray("tracklist")
                ));
                list.add(jsonObject.getString("name"));
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        rv_list_product.setAdapter(new AlbumAdapter(getContext(), product_list1));
        rv_list_product.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}