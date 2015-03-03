package com.dwij.syc.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.syc.R;

public class SplashScreen extends Activity {

	/** Called when the activity is first created. */

	private boolean mIsBackButtonPressed;
	private static final int SPLASH_DURATION = 4000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		Handler handler = new Handler();

		handler.postDelayed(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				// make sure we close the splash screen so the user won't come back when it presses back key

				finish();

				if (!mIsBackButtonPressed) {
					// start the home screen if the back button wasn't pressed already
					Intent isplash = new Intent(SplashScreen.this, SYCenters.class);
					startActivity(isplash);
				}

			}

		}, SPLASH_DURATION);

	}

	@Override
	public void onBackPressed() {

		// set the flag to true so the next activity won't start up
		mIsBackButtonPressed = true;
		super.onBackPressed();

	}

}