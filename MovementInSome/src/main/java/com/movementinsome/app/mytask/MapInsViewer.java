package com.movementinsome.app.mytask;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;

import com.movementinsome.R;
import com.movementinsome.map.MapViewer;

public class MapInsViewer extends MapViewer implements OnInitListener {

	//private TextToSpeech tts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_patrol_data_activity);
		//tts = new TextToSpeech(this, this);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}

}
