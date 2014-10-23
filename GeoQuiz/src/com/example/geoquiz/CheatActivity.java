package com.example.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends Activity {

	private boolean mAnswerIsTrue;
	private TextView mAnswerTextView;
	private Button mShowAnswer;

	// 使用包名，避免来自不同应用的extra间发生冲突
	public static final String EXTRA_ANSWER_IS_TRUE = "com.example.geoquiz.answer_is_true";
	public static final String EXTRA_ANSWER_SHOWN = "com.example.geoquiz.answer_shown";

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cheat);
		
		setAnswerShownResult(false);

		this.mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
		
		mAnswerTextView = (TextView) findViewById(R.id.answerTextView);
		this.mShowAnswer = (Button) findViewById(R.id.showAnswerButton);
		this.mShowAnswer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setAnswerShownResult(true);
				if(mAnswerIsTrue) {
					mAnswerTextView.setText(R.id.true_button);
				}
				else {
					mAnswerTextView.setText(R.id.false_button);
				}
				
			}
		});
		
	}
	
	private void setAnswerShownResult(boolean isAnswerShown) {
		Intent intent = new Intent();
		intent.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
		setResult(RESULT_OK, intent);
	}

}
