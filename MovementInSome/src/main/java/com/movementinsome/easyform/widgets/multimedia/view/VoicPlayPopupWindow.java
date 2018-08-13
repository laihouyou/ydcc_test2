package com.movementinsome.easyform.widgets.multimedia.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.R;

import java.io.File;
import java.util.List;

public class VoicPlayPopupWindow {
	private List<String> voicList;

	public VoicPlayPopupWindow(final Context context,View parent,List<String> voicList) {
		PopupWindow popupWindow = new PopupWindow(context);
		View view = View.inflate(context, R.layout.voice_play_window, null);
		ListView voic_play_list = (ListView) view
				.findViewById(R.id.voic_play_list);
		
		if (voicList.size() == 0) {
			Toast.makeText(context, "暂无录音！", Toast.LENGTH_LONG).show();
			popupWindow.dismiss();
		}
		voic_play_list.setAdapter(new VoicPlayListAdapter(context, voicList));
		
		// 设置SelectPicPopupWindow的View
		popupWindow.setContentView(view);
		// 设置SelectPicPopupWindow弹出窗体的宽
		popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 120);
	}

	public class VoicPlayListAdapter extends BaseAdapter {

		private List<String> fileNames;
		private Context context;

		public VoicPlayListAdapter(Context context, List<String> fileNames) {
			this.fileNames = fileNames;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return fileNames.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return fileNames.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(final int p, View v, ViewGroup arg2) {
			// TODO Auto-generated method stub
			if (v == null) {
				v = View.inflate(context, R.layout.voic_play_list_item, null);
			}
			ImageView voic_play_list_item_del = (ImageView) v.findViewById(R.id.voic_play_list_item_del);
			voic_play_list_item_del.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(context)
							.setTitle("提示")
							.setMessage("是否删除")
							.setPositiveButton("否",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub

										}
									})
							.setNegativeButton("是",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											File flie = new File(fileNames
													.get(p));
											flie.delete();
											fileNames.remove(p);
											VoicPlayListAdapter.this
													.notifyDataSetChanged();
										}
									}).show();
				}
			});
			TextView text = (TextView) v
					.findViewById(R.id.voic_play_list_item_text);
			text.setText(fileNames.get(p));
			text.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 Intent it = new Intent(Intent.ACTION_VIEW);
		             it.setDataAndType(Uri.parse("file://" + fileNames.get(p)), "audio/MP3");
		             context.startActivity(it); 
				}
			});
			return v;
		}

	}

	public void getFileList(String path, List<String> list) {
		File file = new File(path);
		// 如果是文件夹的话
		if (file.isDirectory()) {
			// 返回文件夹中有的数据
			File[] files = file.listFiles();
			// 先判断下有没有权限，如果没有权限的话，就不执行了
			if (null == files)
				return;

			for (int i = 0; i < files.length; i++) {
				getFileList(files[i].getAbsolutePath(), list);
			}
		} else {
			// 进行文件的处理
			String filePath = file.getAbsolutePath();
			// 添加
			if (filePath.toLowerCase().endsWith(".mp3")) {
				list.add(filePath);
			}
		}
	}

	/**
	 * @Description
	 * @param name
	 */
	private MediaPlayer mMediaPlayer = new MediaPlayer();

	private void playMusic(String name) {
		try {
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(name);
			mMediaPlayer.prepare();
			mMediaPlayer.start();
			mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer mp) {

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
