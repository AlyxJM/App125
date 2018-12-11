package edu.illinois.cs.cs125.lab12;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Main screen for our API testing app.
 */
public final class MainActivity extends AppCompatActivity {
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    /** Variable to store quote result of API. */
    private String quote = "";
    /**Variable to store url result of Image API. */
    private String url = "";

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up a queue for our Volley requests
        requestQueue = Volley.newRequestQueue(this);

        // Load the main layout for our activity
        setContentView(R.layout.activity_main);

        // Attach the handler to our quote button
        final Button buttonQuote = findViewById(R.id.buttonQuote);


        buttonQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Quote button clicked");
                startQuoteAPICall();
            }
        });
        // Attach the handler to our image button
        final Button buttonImage = findViewById(R.id.buttonImage);
        buttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Image button clicked");
                startImageAPICall();

            }
        });
        // Attach the handler to our both button
        final Button buttonBoth = findViewById(R.id.buttonBoth);
        buttonBoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d(TAG, "Both button clicked");
                startImageAPICall();
                startQuoteAPICall();
            }
        });
    }

    /**
     * Make image API call.
     */
    void startImageAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://api.unsplash.com/photos/random"
                            + "?client_id="
                            + "4a89728ee71c2b4687f2a18a3d029e7501b74003ad109a818ee170112582ef7e",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            try {
                                Log.d(TAG, response.getJSONObject("urls")
                                        .get("regular").toString());
                                url = response.getJSONObject("urls")
                                    .get("regular").toString();
                                final ImageView imageView = findViewById(R.id.imageView);
                                Picasso.with(imageView.getContext()).load(url).into(imageView);
                            } catch (Exception e) {
                                Log.e(TAG, "Problem parsing JSON", e);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                        }
                    }) {
            };
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    /**
     * Make quote API call.
     */
    void startQuoteAPICall() {
        try {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://quotesondesign.com/wp-json/posts?filter[orderby]=rand&filter[posts_per_page]=1",
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(final JSONArray response) {
                            Log.d(TAG, response.toString());
                            final TextView quoteTextView = findViewById(R.id.quoteTextView);
                            try {
                                Log.d(TAG, response.getJSONObject(0)
                                    .get("content").toString());
                                //quote = response.getJSONObject(0).get("content").toString();
                                String q = response.getJSONObject(0).get("content").toString();
                                q = q.replaceAll("<p>", "");
                                q = q.replaceAll("</p>", "");
                                q = q.replaceAll("[0-9]", "");
                                quote = q;
                                quoteTextView.setText(quote);
                            } catch (Exception e) {
                                Log.e(TAG, "Problem parsing JSON", e);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            Log.w(TAG, error.toString());
                        }
                    }) {
                };
            requestQueue.add(jsonArrayRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}