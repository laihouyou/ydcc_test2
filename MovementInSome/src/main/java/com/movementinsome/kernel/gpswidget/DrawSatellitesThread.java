package com.movementinsome.kernel.gpswidget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PaintFlagsDrawFilter;
import android.location.GpsSatellite;
import android.view.SurfaceHolder;

import com.movementinsome.R;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;


public class DrawSatellitesThread extends Thread {
	// 卫星图
	private Bitmap satelliteBitmap;
	private Bitmap satelliteFixBitmap;
	private Bitmap compassBitmap;

	private Paint paint;

	/** Handle to the surface manager object we interact with */
	private SurfaceHolder surfaceHolder;

	/** Indicate whether the surface has been created & is ready to draw */
	private boolean isRunning = false;
	private int cx=0;
	private int cy=0;
	private int compassRadius = 434 / 2;
	
	PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	
	public static final LinkedBlockingQueue<List<GpsSatellite>> queue = 
			new LinkedBlockingQueue<List<GpsSatellite>>(60);

	public DrawSatellitesThread(SurfaceHolder surfaceHolder, Context context) {
		this.surfaceHolder = surfaceHolder;
		Resources res = context.getResources();
		// cache handles to our key sprites & other drawables
		compassBitmap = BitmapFactory.decodeResource(res, R.drawable.compass);
		compassRadius = compassBitmap.getWidth() / 2;

		satelliteBitmap = BitmapFactory.decodeResource(res,
				R.drawable.satellite_mark);
		satelliteFixBitmap = BitmapFactory.decodeResource(res,
				R.drawable.satellite_fix_mark);

		paint = new Paint();
		paint.setSubpixelText(true);
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.RED);
		paint.setTextSize(24);
		paint.setTextAlign(Align.CENTER);
	}
	
    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        synchronized (surfaceHolder) {
			cx = width / 2;
			cy = height  / 2;
        }
    }
	
	@Override
	public void run() {		
		List<GpsSatellite> list=null;
		Canvas c = null;
		
		try {
			c = surfaceHolder.lockCanvas(null);
			//初始化画板的中心坐标
			cx = c.getWidth() / 2;
			cy = c.getWidth()  / 2;
			synchronized (surfaceHolder) {
				doDraw(c,null);
			}
		} finally {
			if (c != null) {
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
		while (isRunning) {
			try{
				list = queue.take();				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				c = surfaceHolder.lockCanvas(null);
				synchronized (surfaceHolder) {
					doDraw(c,list);
				}
			} finally {
				if (c != null) {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}


	public void setRunning(boolean b) {
		isRunning = b;
	}

	public void repaintSatellites(List<GpsSatellite> list) {
		synchronized (surfaceHolder) {
			try {
				queue.offer(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * 绘制背景罗盘
     * @param canvas
     * @param cx  罗盘中心点位于画布上的X坐标
     * @param cy  罗盘中心点位于画布上的Y坐标
     * @param r   罗盘的半径
     */
	private void drawBackground(Canvas canvas, int cx, int cy, int r) {
		int x = cx - r;
		int y = cy - r;
		canvas.drawBitmap(compassBitmap, x, y, paint);
	}

	/**
	 * 将角度转换为弧度，以用于三角函数的运算
	 * 
	 * @param degree
	 * @return
	 */
	private double degreeToRadian(double degree) {
		return (degree * Math.PI) / 180.0d;
	}

	/*
	 * 将SNR的值，转化为通用的信号强度级别，主要用于在绘制卫星时，通过颜色来表明它的信号强度，暂时没用到
	 * SNR is mapped to signal strength [0,1,4-9] COMMENT SNR: >500 >100 >50 >10
	 * >5 >0 bad n/a COMMENT sig: 9 8 7 6 5 4 1 0 COMMENT
	 */
	private int snrToSignalLevel(float snr) {
		int level = 0;
		if (snr >= 0 && snr < 5) {
			level = 4;
		} else if (snr >= 5 && snr < 10) {
			level = 5;
		} else if (snr >= 10 && snr < 50) {
			level = 6;
		} else if (snr >= 50 && snr < 100) {
			level = 7;
		} else if (snr >= 100 && snr < 500) {
			level = 8;
		} else if (snr >= 500) {
			level = 9;
		}
		return level;
	}
	
    /**
     * 在背景罗盘上绘制卫星
     * @param canvas
     * @param satellite
     * @param cx  中心圆点的X座标
     * @param cy  中心圆点的Y座标
     * @param r   罗盘背景的半径
     */
	private void drawSatellite(Canvas canvas,GpsSatellite satellite, int cx, int cy, int r) {

		/**
		 * GPS卫星导航仪通常选用仰角大于5º，小于85º。 因为当卫星仰角大于85º时，L1波段的电离层折射误差较大，故规定仰角大于85º时，
		 * 定位无效，不进行数据更新。而卫星仰角越小，则对流层折射误差越大，故一般选用仰角大于5º的卫星来定位。
		 */
		//得到仰角
		float elevation = satellite.getElevation();
        //通过仰角，计算出这个卫星应该绘制到离圆心多远的位置，这里用的是角度的比值
		double r2 = r * ((90.0f - elevation) / 90.0f);
        
		/*得到方位角（与正北向也就是Y轴顺时针方向的夹角，注意我们通常几何上的角度
         * 是与X轴正向的逆时针方向的夹角）,在计算X，Y座标的三角函数时，要做转换
         */
		double azimuth = satellite.getAzimuth();
        
		/*
         * 转换成XY座标系中的夹角,方位角是与正北向也就是Y轴顺时针方向的夹角，
         * 注意我们通常几何上的角度是与X轴正向的逆时针方向的夹角）,
         * 在计算X，Y座标的三角函数时，要做转换
         */
		double radian = degreeToRadian(360-azimuth + 90);
           
		double x = cx + Math.cos(radian) * r2;
		double y = cy + Math.sin(radian) * r2;
		if (satellite.usedInFix()){
			//得到卫星图标的半径
			int sr = satelliteFixBitmap.getWidth() / 2;
	        //以x,y为中心绘制卫星图标
			canvas.drawBitmap(satelliteFixBitmap, (float) (x - sr), (float) (y - sr),paint);
		}else{
			//得到卫星图标的半径
			int sr = satelliteBitmap.getWidth() / 2;
	        //以x,y为中心绘制卫星图标
			canvas.drawBitmap(satelliteBitmap, (float) (x - sr), (float) (y - sr),paint);
		}
		//在卫星图标的位置上绘出文字（卫星编号及信号强度）
		int snr=(int)satellite.getSnr();//信噪比
        int signLevel=snrToSignalLevel(snr);  //暂时不用
		String info = String.format("#%s_%s", satellite.getPrn(), snr);
		canvas.drawText(info, (float) (x), (float) (y), paint);

	}


	private void doDraw(Canvas canvas, List<GpsSatellite> satellites) {
		if (canvas != null) {
			// 绘制背景罗盘
			drawBackground(canvas, cx, cy, compassRadius);
			//绘制卫星分布
			if (satellites != null) {
				for (GpsSatellite satellite : satellites) {
					drawSatellite(canvas,satellite, cx, cy, compassRadius);
				}
			}
		}

	}

}
