package com.example.geoquiz.pojo;

public class TrueFalse {
	
	private int mQuestion;
	private boolean mAnswer;
	
	public TrueFalse(int question, boolean answer) {
		this.mQuestion = question;
		this.mAnswer = answer;
	}

	/**
	 * @return the question
	 */
	public int getQuestion() {
		return mQuestion;
	}

	/**
	 * @param question the question to set
	 */
	public void setQuestion(int question) {
		mQuestion = question;
	}

	/**
	 * @return the answer
	 */
	public boolean isAnswer() {
		return mAnswer;
	}

	/**
	 * @param answer the answer to set
	 */
	public void setAnswer(boolean answer) {
		mAnswer = answer;
	}

}
