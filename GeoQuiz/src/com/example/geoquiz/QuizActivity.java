package com.example.geoquiz;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.geoquiz.pojo.TrueFalse;

public class QuizActivity extends ActionBarActivity {
	
	private static final String TAG = QuizActivity.class.getSimpleName();
	private static final String KEY_INDEX = "index";

	private Button mTrueButton;
	private Button mFalseButton;
	private ImageButton mNextButton;
	private ImageButton mPrevButton;
	private TextView mQuestionTextView;

	private int mCurrenctIndex;
	
	

	// private List<TrueFalse> mQuestionList = new ArrayList<TrueFalse>();

	private TrueFalse[] mQuestions = new TrueFalse[] {
			new TrueFalse(R.string.question_city, false),
			new TrueFalse(R.string.question_ocean, true),
			new TrueFalse(R.string.question_football, false),
			new TrueFalse(R.string.question_program, true) 
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		Log.d(TAG, "onCreate() called"); 
		
		if (savedInstanceState != null) {
			mCurrenctIndex = savedInstanceState.getInt(KEY_INDEX, 0);
		}
		

		this.mQuestionTextView = (TextView) findViewById(R.id.question_text_veiw);
		
		this.mQuestionTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrenctIndex = (mCurrenctIndex + 1) % mQuestions.length;
				updateQuestion();
			}
		});

		this.mTrueButton = (Button) findViewById(R.id.true_button);
		this.mFalseButton = (Button) findViewById(R.id.false_button);

		this.mTrueButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(QuizActivity.this, R.string.incorrect_toast,
//						Toast.LENGTH_SHORT).show();
				checkAnswer(true);
			}
		});
		this.mFalseButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(QuizActivity.this, R.string.correct_toast,
//						Toast.LENGTH_SHORT).show();
				checkAnswer(false);
			}
		});

		this.mNextButton = (ImageButton) findViewById(R.id.next_button);
		this.mNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCurrenctIndex = (mCurrenctIndex + 1) % mQuestions.length;
				updateQuestion();
			}
		});
		
		//上一个 action
		this.mPrevButton = (ImageButton) findViewById(R.id.prev_button);
		this.mPrevButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Question: 如果当前索引是0，点击上一个如果找到数组的最后一个
				/*数组的循环取值：
					如果数组长度为10， 那么索引值是0-9.
					currentIndex从0开始， 此时要获取上一个值：0 + 10 - 1 = 9，9 % 10 = 9， 这样就得到了数组的最后一个。
					currentIndex变成了9，此时要获取下一个值：9 + 1 = 10， 10 % 10 = 0.这样就又回到了第一个值。*/
				mCurrenctIndex = (mCurrenctIndex + mQuestions.length - 1) % mQuestions.length;
				updateQuestion();
			}
		});

		updateQuestion();
	}
	
	/**
	 * 保存当前Activity的状态
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d(TAG, "onSaveInstanceState() called"); 
		
		outState.putInt(KEY_INDEX, mCurrenctIndex);
	}
	
	@Override
	protected void onStart() {
		Log.d(TAG, "onStart() called"); 
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		Log.d(TAG, "onResume() called"); 
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Log.d(TAG, "onPause() called"); 
		super.onPause();
	}
	
	@Override
	protected void onStop() {
		Log.d(TAG, "onStop() called"); 
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy() called"); 
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void updateQuestion() {
		int question = this.mQuestions[mCurrenctIndex].getQuestion();
		this.mQuestionTextView.setText(question);
	}
	
	private void checkAnswer(boolean userPressedValue) {
		boolean answer = this.mQuestions[mCurrenctIndex].isAnswer();
		if (userPressedValue == answer){
			Toast.makeText(this, R.string.correct_toast, Toast.LENGTH_SHORT).show();
		}
		else{
			Toast.makeText(this, R.string.incorrect_toast, Toast.LENGTH_SHORT).show();
		}
	}
}
