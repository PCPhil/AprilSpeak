package philousify.aprilspeak;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 0;
    private static final String url = "http://zenit.senecac.on.ca:19350/cgi-bin/Define2.php";
    public static final String TAG = "MainActivity";

    private TextToSpeech t1;
    private EditText etWord;
    private String words;
    private List arrayWords;
    private TextView tvDefine;
    private TextView tvTerm;

    private ArrayList<String> defList;


    private Button b1;
    private Button b2;
    private Button b3;
    private Button bDefine;

    private SpeechRecognizer mSpeechRecognizer;
    private RecognitionListener mRecognitionListener;
    private Intent mRecognizerIntent;
    final String TAG_ALF = "alf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etWord = (EditText) findViewById(R.id.editText);
        b1 = (Button) findViewById(R.id.btnsay);
        tvDefine = (TextView) findViewById(R.id.tvDefine);

        arrayWords = new ArrayList<String>();
        try {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
        }catch(Exception e){
            Log.d(TAG, e + "");
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                   // String toSpeak = (etWord.getText().toString()==null)?tvDefine.getText().toString():etWord.getText().toString();
                    //String toSpeak = (tvDefine.getText().toString()!="")?tvDefine.getText().toString():etWord.getText().toString();
                    @Override
                    public void onInit(int status) {
                        String toSpeak;
                        if(tvDefine.getText().length()>0){
                            toSpeak = tvDefine.getText().toString();
                        }
                        else{
                            toSpeak = etWord.getText().toString();
                        }
                        Log.d(TAG, "onInit: " + etWord.getText().toString());
                        t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
            }
        });
        words = "";

        b2 = (Button) findViewById(R.id.start);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(getBaseContext());
                    mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
                        @Override
                        public void onReadyForSpeech(Bundle bundle) {
                            Log.d(TAG, "onReadyForSpeech: ");
                        }

                        @Override
                        public void onBeginningOfSpeech() {
                            Log.d(TAG, "onBeginningOfSpeech: ");
                        }

                        @Override
                        public void onRmsChanged(float v) {
                            Log.d(TAG, "onRmsChanged: ");
                        }

                        @Override
                        public void onBufferReceived(byte[] bytes) {
                            Log.d(TAG, "onBufferReceived: ");
                        }

                        @Override
                        public void onEndOfSpeech() {
                            Log.d(TAG, "onEndOfSpeech: ");
                        }

                        @Override
                        public void onError(int i) {
                            Log.d(TAG, "onError: " + i);
                            switch (i){
                                case 6:
                                    Toast.makeText(getBaseContext(), "No matches", Toast.LENGTH_SHORT).show();
                                    break;
                                case 7:
                                    Toast.makeText(getBaseContext(), "No Matches", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                        @Override
                        public void onResults(Bundle bundle) {
                            Log.d(TAG, "onResults: ");
                            arrayWords = bundle.getStringArrayList(mSpeechRecognizer.RESULTS_RECOGNITION);
                            for(Object s : arrayWords){
                                words+=s + "\n";

                            }
                            etWord.setText(words);
                            words = "";
                        }

                        @Override
                        public void onPartialResults(Bundle bundle) {
                            Log.d(TAG, "onPartialResults: ");
                        }

                        @Override
                        public void onEvent(int i, Bundle bundle) {
                            Log.d(TAG, "onEvent: ");
                        }
                    });
                    mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getBaseContext().getPackageName());
                    mSpeechRecognizer.startListening(mRecognizerIntent);
                    Log.d(TAG, "" + mSpeechRecognizer.isRecognitionAvailable(getBaseContext()));
                }catch(Exception e){
                    Log.d(TAG, e+"");
                }
                Toast.makeText(getBaseContext(),"start",Toast.LENGTH_LONG).show();
              }
        });


        //Flush data
        b3 = (Button) findViewById(R.id.clear);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* FragmentManager fragmentManager = getSupportFragmentManager();
                 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                ArticleListFragment alf = new ArticleListFragment();
                ArticleReaderFragment arf = new ArticleReaderFragment();

                fragmentTransaction.replace(R.id.activity_main,arf,TAG_ALF);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/

                etWord.setText(null);
                tvTerm.setText(null);
                tvDefine.setText(null);
                Toast.makeText(MainActivity.this, "Cleansed", Toast.LENGTH_SHORT).show();


            }
        });
        bDefine = (Button) findViewById(R.id.define);
        defList = new ArrayList<>();
        tvTerm = (TextView) findViewById(R.id.tvTerm);
        //Define word
        bDefine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                       new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               Log.d(TAG, response);
                               String ap = response.replace(".",".\n");
                               String[] str = ap.split("\n");
                               String prod = "";
                               for(int i = 0 ; i<str.length;i++){
                                   str[i] = str[i].substring(0,1).toUpperCase() + str[i].substring(1).toLowerCase();
                                   prod += i+1 + ". " + str[i] + "\n";
                               }

                                tvDefine.setText(prod);
                               String s = etWord.getText().toString();

                                tvTerm.setText(s.substring(0,1).toUpperCase() + s.substring(1).toLowerCase());
                           }
                       },
                       new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.d(TAG, "onErrorResponse: ");
                   }
               }){

                   protected Map<String,String> getParams() throws AuthFailureError {
                       Map<String,String> map = new HashMap<>();
                       words = etWord.getText().toString();
                       Log.d(TAG, "getParams: " + words);
                       map.put("word",words);

                       return map;
                   }
               };


                SingletonRequest.getInstance(getBaseContext()).addToRequestQueue(stringRequest);


            }//onClick
        });//bDefine
    }//End of onCreate


    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    //GOOGLE VOICE
    public void onClick(View view){
        displaySpeechRecognizer();
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());

// Start the activity, the intent will be populated with the speech text
        try{
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    } catch (ActivityNotFoundException a) {
        Toast.makeText(getApplicationContext(), "Your device is not supported!",
                Toast.LENGTH_SHORT).show();
    }
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            // Do something with spokenText
            words += etWord.getText().toString();


            etWord.setText(words + " " + spokenText);
        }
        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toSpeak = etWord.getText().toString();
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        super.onActivityResult(requestCode, resultCode, data);
    }
}
