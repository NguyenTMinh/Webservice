package com.example.webservicetest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SinhVienAdapter.ICallBack {
    private final String URL = "http://192.168.1.2/android/data.php";
    private final String URL_2 = "http://192.168.1.2/android/insert.php";
    private final String URL_RESULT = "http://192.168.1.2/android/result.php";
    private final String URL_DELETE = "http://192.168.1.2/android/delete.php";
    private final String URL_UPDATE = "http://192.168.1.2/android/update.php";
    private RecyclerView recyclerView;
    private Button button;
    private SinhVienAdapter adapter;
    private List<SinhVien> list;
    private EditText editName;
    private EditText editDate;
    private EditText editAddr;
    private TextView tvTitleDialog;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_test);
        adapter = new SinhVienAdapter(list,this);
        adapter.setCallBack(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        new LoadAsync().execute();

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.dialog_add_info);
            tvTitleDialog = dialog.findViewById(R.id.textView);
            tvTitleDialog.setText(R.string.title_them);
            editName = dialog.findViewById(R.id.et_name);
            editDate = dialog.findViewById(R.id.et_date);
            editAddr = dialog.findViewById(R.id.et_addr);
            Button btAdd = dialog.findViewById(R.id.button2);
            Button btCanc = dialog.findViewById(R.id.button3);
            btAdd.setOnClickListener(v1->{
                if(editAddr.getText().toString().trim().equals("")
                        || editName.getText().toString().trim().equals("")
                        || editDate.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                }else{
                    writeJSON(URL_2,URL_RESULT);
                    dialog.cancel();
                }
            });
            btCanc.setOnClickListener(v1 -> {
                dialog.cancel();
            });
            dialog.show();
        });

    }

    private void readJSON(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                list.add(new SinhVien(object.getInt("ID"),object.getString("Hoten")
                                        ,object.getInt("NamSinh"),object.getString("DiaChi")));
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                });
        queue.add(request);
    }

    private void writeJSON(String url,String url2){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest requestResult = new StringRequest(Request.Method.GET, url2
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                list.add(new SinhVien(Integer.parseInt(response),editName.getText().toString()
                        ,Integer.parseInt(editDate.getText().toString()),editAddr.getText().toString()));
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.trim().equals("fail")){
                            queue.add(requestResult);
                        }else{
                            Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name",editName.getText().toString().trim());
                params.put("date",editDate.getText().toString().trim());
                params.put("address",editAddr.getText().toString().trim());
                return params;
            }
        };
        queue.add(request);
    }

    private void deleteData(String url, int id){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response.trim() ,Toast.LENGTH_SHORT).show();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",String.valueOf(id));
                return params;
            }
        };
        queue.add(request);
    }

    private void updateData(String url,int id){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",String.valueOf(id));
                params.put("name",editName.getText().toString().trim());
                params.put("date",editDate.getText().toString().trim());
                params.put("address",editAddr.getText().toString().trim());
                return params;
            }
        };
        queue.add(request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null){
            adapter.release();
        }
    }

    @Override
    public void onDel(int position,int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Ban co muon xoa muc nay?");
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new DeleteAsync().execute(id);
                list.remove(position);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Huy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void onEdit(int position,int id) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.dialog_add_info);
        tvTitleDialog = dialog.findViewById(R.id.textView);
        tvTitleDialog.setText(R.string.title_sua);
        editName = dialog.findViewById(R.id.et_name);
        editName.setText(list.get(position).getName());
        editDate = dialog.findViewById(R.id.et_date);
        editDate.setText(list.get(position).getDateString());
        editAddr = dialog.findViewById(R.id.et_addr);
        editAddr.setText(list.get(position).getAddress());
        Button btAdd = dialog.findViewById(R.id.button2);
        Button btCanc = dialog.findViewById(R.id.button3);
        btAdd.setOnClickListener(v1->{
            if(editAddr.getText().toString().trim().equals("")
                    || editName.getText().toString().trim().equals("")
                    || editDate.getText().toString().trim().equals("")){
                Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }else{
                updateData(URL_UPDATE,id);
                dialog.cancel();
            }
        });
        btCanc.setOnClickListener(v1 -> {
            dialog.cancel();
        });
        dialog.show();
    }

    class LoadAsync extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            readJSON(URL);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(unused);
        }
    }
    class DeleteAsync extends AsyncTask<Integer,Void,Void>{

        @Override
        protected Void doInBackground(Integer... integers) {
            deleteData(URL_DELETE,integers[0]);
            return null;
        }
    }
}