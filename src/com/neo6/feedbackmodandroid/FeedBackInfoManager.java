package com.neo6.feedbackmodandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.util.Log;

public class FeedBackInfoManager {
    private static final String TAG = FeedBackInfoManager.class.getSimpleName();
	private static FeedBackInfoManager sInstance = null;
	
	private Context mContext;
	private FeedBackBean mBean;
	
	
	private FeedBackInfoManager(Context mContext) {
		super();
		this.mContext = mContext;
	}
	
	public static FeedBackInfoManager getInstance(Context ctx){
		if (sInstance == null) {    
			sInstance = new FeedBackInfoManager(ctx);  
        }    
       return sInstance; 
	}
	

	public FeedBackBean getBean(){
	    if (mBean == null) {
	    	String jsonstr = getFromAssets("quick_feedback_model.json" ,mContext);
	    	java.lang.reflect.Type type = new TypeToken<FeedBackBean>() {}.getType();
		    Gson gson = new Gson();
	    	mBean = gson.fromJson(jsonstr, type);
		}
	    return mBean;
	}
	
	private int getProblemCount(FeedBackBean bean){
		return bean.feedbacks.size();
	}
	
	public int getProblemId(FeedBackBean bean, int location){
		if (location < getProblemCount(mBean)) {
			return bean.feedbacks.get(location).problem_id;
		}
		return 0;
	}
	
	private String getProblemDesc(FeedBackBean bean, int location){
		if (location < getProblemCount(mBean)) {
			return bean.feedbacks.get(location).problem_desc;
		}
		return "";
	}
	
	public String [] getProblemDescArray(FeedBackBean bean){
		String [] str = new String[getProblemCount(bean) - 1];//除去顶部的"我有建议"
		for (int i = 0; i < str.length; i++) {
			str[i] = (i+1)+". "+getProblemDesc(bean, i+1);//动态添加序号
		}
		Log.d(TAG, "bean.feedbacks.size():"+bean.feedbacks.size());
		return str;
	}
    
	private String getFromAssets(String fileName,Context ctx){ 
        try { 
            InputStreamReader inputReader = new InputStreamReader( ctx.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
        return "";
    } 
	
	
}
