package com.neo6.feedbackmodandroid;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

import com.neo6.feedbackmodandroid.FeedBackBean.FeedBackInfo.FeedBackForm;
import com.neo6.feedbackmodandroid.FeedBackBean.FeedBackInfo.FeedBackForm.FeedBackType;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

public class FeedBackDetailActivity extends Activity implements OnClickListener, OnCheckedChangeListener,android.widget.RadioGroup.OnCheckedChangeListener{

private static final String TAG = FeedBackDetailActivity.class.getSimpleName();
    
    private FeedBackInfoManager mMgr;
    private FeedBackBean mBean;
    
    private LinearLayout mRootLayout;
    private int mLayoutIndex = 0;
    private List<FeedBackForm> mForms;
    private HashSet<Integer> mMulChoice = new HashSet<Integer>();//多选反馈
    
    private String mProbId = "";//问题题号
    private String mChoicesStr = "";//选择项
    private String mContentStr = "";//反馈文本
    private String mContactStr = "";//联系方式
    
    private int mPadding, mPadding_small; 
    private LinearLayout.LayoutParams mLinearparam;
    private EditText mContactet;
    private Drawable schecked_ico, sunchecked_ico, mchecked_ico, munchecked_ico;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_detail);
        
        initData();
        
		findView();
	}

	private void initData() {
	    mMgr  = FeedBackInfoManager.getInstance(this);
        mBean = mMgr.getBean();
	    
	    int prob_id = getIntent().getIntExtra("selectedProbId", 0);
	    mProbId = prob_id + mProbId;
        
        for (int i = 0; i < mBean.feedbacks.size(); i++) {
            if (mBean.feedbacks.get(i).problem_id == prob_id) {
                mForms  = mBean.feedbacks.get(i).forms;
            }
        }
        
        mPadding = this.getResources().getDisplayMetrics().widthPixels/18;//全局左右padding
        mPadding_small = mPadding/4;//prompt上下padding
        mLinearparam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        //单选按钮：
        schecked_ico = getResources().getDrawable(R.drawable.feedback_schoice_checked);
        schecked_ico.setBounds(0, 0, schecked_ico.getMinimumWidth(), schecked_ico.getMinimumHeight());
        sunchecked_ico = getResources().getDrawable(R.drawable.feedback_schoice_unchecked);
        sunchecked_ico.setBounds(0, 0, schecked_ico.getMinimumWidth(), schecked_ico.getMinimumHeight());
        //多选按钮：
        mchecked_ico = getResources().getDrawable(R.drawable.feedback_mchoice_checked);
        mchecked_ico.setBounds(0, 0, mchecked_ico.getMinimumWidth(), mchecked_ico.getMinimumHeight());
        munchecked_ico = getResources().getDrawable(R.drawable.feedback_mchoice_unchecked);
        munchecked_ico.setBounds(0, 0, munchecked_ico.getMinimumWidth(), munchecked_ico.getMinimumHeight());
	}

    private void findView() {
		
        mRootLayout = (LinearLayout) findViewById(R.id.feedback_detail_ll);
        Button commit_btn = (Button) findViewById(R.id.feedback_detail_commit_btn);
        commit_btn.setOnClickListener(this);
        mContactet = (EditText) findViewById(R.id.feedback_detail_contact_et);
        
        for (int i = 0; i < mForms.size(); i++) {
            if (mForms.get(i).type == FeedBackType.single_select) {
                getSingleSelectView(i);
            } else if(mForms.get(i).type == FeedBackType.multi_select){
                getMultiSelectView(i);
            } else if(mForms.get(i).type == FeedBackType.texts){
                getEdittextView(i);
            }
        }
	}

	private void getEdittextView(int index) {
        FeedBackForm form = mForms.get(index);
        
        EditText et = (EditText) View.inflate(this, R.layout.view_feedback_edittext, null);
        et.setTag(form);
        et.setHint(form.hint);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(form.max_len)});
        
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        if (index != 0) {
            p.setMargins(0, mPadding/2, 0, 0);
        }
        et.setLayoutParams(p);
        
        mRootLayout.addView(et, mLayoutIndex++);
    }

    private void getMultiSelectView(int index) {
        TextView prompt_tv = new TextView(this);
        prompt_tv.setText(mForms.get(index).prompt);
        setPromptMargin(prompt_tv, index);
        mRootLayout.addView(prompt_tv, mLayoutIndex++);
        
        FeedBackForm form = mForms.get(index);
        CheckBox cb;
        for (int j = 0; j < form.items.size(); j++) {
            cb = (CheckBox) View.inflate(this, R.layout.view_feedback_checkbox, null);
            cb.setId(form.items.get(j).select_id);
            cb.setText(form.items.get(j).select_desc);
            cb.setOnClickListener(this);
            
            cb.setOnCheckedChangeListener(this);
            mRootLayout.addView(cb, mLayoutIndex++, mLinearparam);
        }
    }

    private void getSingleSelectView(int index) {
        RadioGroup rgroup = new RadioGroup(this);
        rgroup.setTag(mForms.get(index));
        
        TextView prompt_tv = new TextView(this);
        prompt_tv.setText(mForms.get(index).prompt);
        setPromptMargin(prompt_tv, index);
        mRootLayout.addView(prompt_tv, mLayoutIndex++);
        
        FeedBackForm form = mForms.get(index);
        RadioButton rb;
        for (int j = 0; j < form.items.size(); j++) {
            rb = (RadioButton) View.inflate(this, R.layout.view_feedback_radiobtn, null);
            rb.setId(form.items.get(j).select_id);
            rb.setText(form.items.get(j).select_desc);
            rb.setOnClickListener(this);
            
            rgroup.addView(rb, j, mLinearparam);
            rgroup.setOnCheckedChangeListener(this);
        }
        mRootLayout.addView(rgroup, mLayoutIndex++);
    }

    private void setPromptMargin(TextView tv, int index){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        if (index == 0) {
            params.setMargins(mPadding, mPadding_small, 0, mPadding_small);
        } else {
            params.setMargins(mPadding, mPadding_small*3, 0, mPadding_small);
        }
        
        tv.setLayoutParams(params);
        tv.setTextColor(getResources().getColor(R.color.feedback_gray));
    }
    
    private String getFailToast(FeedBackForm form, View input){
        if (form.required == false) {
            Log.d(TAG, "本题非必填");
            return "";
        } else if (form.type == FeedBackType.single_select) {
            if (((RadioGroup) input).getCheckedRadioButtonId() != -1) {
                Log.d(TAG, "本题单选，已选择id："+((RadioGroup) input).getCheckedRadioButtonId());
                return "";
            }
        } else if (form.type == FeedBackType.multi_select) {
            for (int i = 0; i < form.items.size(); i++) {
                if (mMulChoice.contains(form.items.get(i).select_id)) {
                    Log.d(TAG, "本题多选，已有选择");
                    return "";
                }
            }
        } else if (form.type == FeedBackType.texts) {
            if (!((EditText) input).getText().toString().isEmpty()) {
                Log.d(TAG, "本题填空，已填写："+((EditText) input).getText().toString());
                return "";
            }
        }
        Log.d(TAG, "本题必填，但输入为空");
        return form.report_fail;
    }

    private Integer getSingleChoice(FeedBackForm form, View input){
        if (form.type == FeedBackType.single_select && ((RadioGroup) input).getCheckedRadioButtonId() != -1) {
            return ((RadioGroup) input).getCheckedRadioButtonId();
        }
        return null;
    }
    
    private void fillWholeInput(HashSet<Integer> SChoices, HashSet<Integer> MChoices){
        SChoices.addAll(MChoices);//必须是多选(成员变量)添加到单选(new出来的)
        TreeSet<Integer> ts = new TreeSet<Integer>(SChoices);
        ts.comparator();
        String trim_ts = ts.toString().replace(" ", "");
        mChoicesStr = trim_ts.substring(1, trim_ts.length()-1);//选择项
        Log.d(TAG, "choice_str:"+mChoicesStr);
        
        StringBuffer content_buf = new StringBuffer();
        for (int i = 0; i < mForms.size(); i++) {
            if (mForms.get(i).type == FeedBackType.texts) {
                EditText et = (EditText) mRootLayout.findViewWithTag(mForms.get(i));
                String content = et.getText().toString();
                if (!TextUtils.isEmpty(content)) {
                    content_buf.append(content+"||");
                }
            }
        }
        mContentStr = content_buf.toString();//文本框
        if (mContentStr.length()>2 && mContentStr.endsWith("||")) {
            mContentStr = mContentStr.substring(0, mContentStr.length()-2);
        }
        Log.d(TAG, "content_str:"+mContentStr);
        
        String contact_str = mContactet.getText().toString();
        if (!TextUtils.isEmpty(contact_str)) {
            mContactStr = contact_str;//联系方式
        }
        Log.d(TAG, "contact_str:"+mContactStr);
    }
    
    @Override
	public void onClick(View v) {
	    switch (v.getId()) {
        case R.id.feedback_detail_commit_btn:
        	boolean netOK = true;/*need to check first*/
            if (netOK) {
                HashSet<Integer> sigChoice = new HashSet<Integer>();//单选反馈
                
                String toast = "";
                for (int i = 0; i < mForms.size(); i++) {
                    toast = getFailToast(mForms.get(i),mRootLayout.findViewWithTag(mForms.get(i)));
                    Integer coi  = getSingleChoice(mForms.get(i),mRootLayout.findViewWithTag(mForms.get(i)));
                    if (coi != null) {sigChoice.add(coi);}
                    if (toast != "") {
                        Toast.makeText(this, /*mForms.get(i).type+*/toast, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                
                if (toast == "") {//未出现toast，允许上报
                    fillWholeInput(sigChoice, mMulChoice);
                    
                    //此处执行上报逻辑:
                    //
                    //end
          
                    Toast.makeText(this, R.string.feedback_done_toast, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    this.finish();
                }
                
            } else {
                Toast.makeText(this, "请检查网络", Toast.LENGTH_SHORT).show();
            }
            break;
        default:
            break;
        }
	}

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        Log.d(TAG, "group.checked_btnid:"+group.getCheckedRadioButtonId());
        
        RadioButton b = (RadioButton) findViewById(checkedId);
        b.setCompoundDrawables(schecked_ico, null, null, null);
        
        Log.d(TAG, "group.btncount:"+group.getChildCount());
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton arb = (RadioButton) group.getChildAt(i);
            if (arb.getId() != checkedId) {
                arb.setCompoundDrawables(sunchecked_ico, null, null, null);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            buttonView.setCompoundDrawables(mchecked_ico, null, null, null);
            mMulChoice.add((Integer)buttonView.getId());
        } else {
            buttonView.setCompoundDrawables(munchecked_ico, null, null, null);
            mMulChoice.remove((Integer)buttonView.getId());
        }
    }

}
