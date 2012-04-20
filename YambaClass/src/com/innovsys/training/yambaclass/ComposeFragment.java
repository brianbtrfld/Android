package com.innovsys.training.yambaclass;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ComposeFragment extends Fragment implements OnClickListener, TextWatcher
{
    private static final String TAG = "Yamba";
    
    private EditText m_editTextMessage;
    private TextView m_textCount;
    private Toast m_toast;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        //NON-UI initialization.
        super.onCreate(savedInstanceState);
        
        //Forces the state retention of the Fragment when an Activity is destroyed and
        //re-created, this tells the Activity Manager to not call onCreate or onDestroy
        //methods of the Fragment, hence retaining the object in memory while the associated
        //Activity goes through its life cycle.
        setRetainInstance(true);
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        //Inflate the fragment UI.
        //Creates View Hierarchy.
        View top = inflater.inflate(R.layout.compose_fragment, container, false);
        
        m_editTextMessage = (EditText) top.findViewById(R.id.editTextStatus);  
        m_textCount = (TextView) top.findViewById(R.id.textLetterCount);
        
        Button buttonUpdate = (Button) top.findViewById(R.id.buttonUpdateStatus);
        buttonUpdate.setOnClickListener(this);
        
        //Init the Toast object.
        //NOTE:  Need to use getApplicationContext() because it is a singleton object and 
        //exists for the life of the app, if it was not used, the Fragment will hang on to
        //a reference to the Activity even when garbage collection is trying to destroy the 
        //Activity, i.e. object or memory leak.
        m_toast = Toast.makeText(getActivity().getApplicationContext(), null, Toast.LENGTH_LONG);
        
        //set the initial value for text count field to 140
        //and set the text color to GREEN.
        m_textCount.setText(Integer.toString(140));
        m_textCount.setTextColor(Color.GREEN);
        
        //associate TextWatcher listener with this object.
        m_editTextMessage.addTextChangedListener(this);
        
        return top;
    }
    
    private class PostToTwitter extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected Integer doInBackground(String... statuses) 
        {
            int result = R.string.postSuccess;
            try 
            {
                //Make a connection to the Twitter API and 
                //call to set the status message.
                Twitter.Status status = YambaApplication.getInstance().getTwitter().setStatus(statuses[0].toString());
       
                //Log.
                Log.d(TAG, "Post successful: " + status.toString());
              
            } 
            catch (TwitterException e) 
            {
                //Log.
                Log.e(TAG, "Failed to post the message", e);
                
                //Change the result.
                result = R.string.postFailed;
            }
            
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) 
        {
            //IMPORTANT:  Method that is executed on the Main (or UI) Thread.
            //so you can perform method calls on UI views.
            
            if (result == R.string.postSuccess)
            {
                //Clear out what the user entered.
                m_editTextMessage.setText("");
            }
            
            //Display the result of the Twitter status update post.
            //HOWEVER, we do not want to create/destroy the Toast object
            //for every post, hence, why the Toast object was created in
            //onCreate.
            
            //  Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
            m_toast.setText(result);
            m_toast.show();
            
            //Re-enable the EditText view.
            m_editTextMessage.setEnabled(true);
        }
        
    }
    
    @Override
    public void onClick(View v) 
    {
        //Log messages are not typically written to a device.
        Log.d(TAG, "Update Status Button clicked");
        
        //If this is the handler for more than one button click
        int id = v.getId();
        
        //Switch on the specific button the user clicked.
        //Again, this allows for the same Activity or Activity class
        //to handle or respond to multiple onClick() events registered
        //by all of the buttons on the layout.
        switch (id)
        {
            case R.id.buttonUpdateStatus:
                //Process the Update Status button click.
                String updateMessage = m_editTextMessage.getText().toString();
                
                //Log what the user entered in the Status Message.
                Log.d(TAG, "User entered: " + updateMessage);
                
                if (TextUtils.isEmpty(updateMessage) == false)
                {
                    //Disable the EditText view.
                    m_editTextMessage.setEnabled(false);
                    
                    //Call Async Task to perform the status post.
                    new PostToTwitter().execute(updateMessage);
                }
                
                break;
                
            default:
                //Should never get here.
        }
    }
    
    @Override
    public void afterTextChanged(Editable statusText) 
    {
        int count = 140 - statusText.length();
        m_textCount.setText(Integer.toString(count));
        m_textCount.setTextColor(Color.GREEN);
        
        if (count < 20)
            m_textCount.setTextColor(Color.YELLOW);
        
        if (count < 10)
            m_textCount.setTextColor(Color.RED);
        
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) 
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) 
    {
        // TODO Auto-generated method stub
        
    }

}
