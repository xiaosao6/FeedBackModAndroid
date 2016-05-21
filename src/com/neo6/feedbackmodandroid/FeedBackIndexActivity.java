package com.neo6.feedbackmodandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FeedBackIndexActivity extends Activity implements OnClickListener, OnItemClickListener{

	private static final String TAG = FeedBackIndexActivity.class.getSimpleName();
    
    private FeedBackInfoManager mMgr;
    private FeedBackBean mBean;
    
    private TextView mHaveSuggestiontv;
    private ListView mFeedbackIndexListView;
    private String [] mStrs;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_index);
        
        initData();
        
		initUI();
        
    }
    
    private void initData() {
	    mMgr  = FeedBackInfoManager.getInstance(this);
	    mBean = mMgr.getBean();
        mStrs = mMgr.getProblemDescArray(mBean);
    }
    
    private void initUI() {

        mHaveSuggestiontv = (TextView) findViewById(R.id.feedback_have_suggestion_tv);
        mHaveSuggestiontv.setOnClickListener(this);
        
        mFeedbackIndexListView = (ListView) findViewById(R.id.feedback_index_lv);
        mFeedbackIndexListView.setOnItemClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.view_feedback_index_item, mStrs);
        mFeedbackIndexListView.setAdapter(adapter);
	}
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.feedback_have_suggestion_tv:
            int pro_id = mMgr.getProblemId(mBean, 0);
            Log.d(TAG, "pro_id:"+pro_id);
            gotoDetailActivity(pro_id);
            break;
        default:
            break;
        }
	}

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "position:"+position);
        int pro_id = mMgr.getProblemId(mBean, position+1);
        Log.d(TAG, "pro_id:"+pro_id);
        gotoDetailActivity(pro_id);
    }
    
    private void gotoDetailActivity(int probId){
        Intent it = new Intent(this, FeedBackDetailActivity.class);
        it.putExtra("selectedProbId", probId);
        startActivityForResult(it, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
        case RESULT_OK:
            this.finish();
            break;
        default:
            break;
        }
    }
    
}
