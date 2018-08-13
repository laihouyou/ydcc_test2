package com.movementinsome.kernel.gpswidget;

import java.util.List;

import android.content.Context;
import android.location.GpsSatellite;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SatellitesView extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String LOG_TAG = "SatellitesView";

	 /** The thread that actually draws the animation */
    private DrawSatellitesThread thread;
 

	public SatellitesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setKeepScreenOn(true);
		SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        
        // create thread only; it's started in surfaceCreated()
        thread = new DrawSatellitesThread(holder, context);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		thread.setSurfaceSize(width, height);

	}
	
    public DrawSatellitesThread getThread() {
        return thread;
    }


	@Override
	public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
	}
	
	
	
	public void repaintSatellites(List<GpsSatellite> satellites){
		thread.repaintSatellites(satellites);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		 // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        thread.setRunning(false);
        try{
        	thread.stop();
        }catch(Exception ex){
        	;
        }
/*        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }*/
	}
}
