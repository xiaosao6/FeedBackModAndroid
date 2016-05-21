package com.neo6.feedbackmodandroid;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@GsonObject
public class FeedBackBean {
    
    @SerializedName("feedbacks")
	public List<FeedBackInfo> feedbacks;


	public static class FeedBackInfo {
	    
	    @SerializedName("problem_desc")
		public String problem_desc;
	    
	    @SerializedName("problem_id")
		public int problem_id;
	    
	    @SerializedName("forms")
		public List<FeedBackForm> forms;// 问题的表单内容，可能包括：单选/多选/填空


		public static class FeedBackForm {

		    @SerializedName("type")
			public FeedBackType type;
		    
		    @SerializedName("prompt")
			public String prompt;
		    
		    @SerializedName("required")
			public boolean required;
		    
		    @SerializedName("report_fail")
			public String report_fail;
		    
		    @SerializedName("hint")
			public String hint;
		    
		    @SerializedName("max_len")
			public int max_len;
		    
		    @SerializedName("items")
			public List<FeedBackSelectItem> items;// 选择项


			public static class FeedBackSelectItem {

			    @SerializedName("select_desc")
				public String select_desc;
			    
			    @SerializedName("select_id")
				public int select_id;

			}

			public enum FeedBackType {
				single_select, multi_select, texts;
			}
		}

	}

}