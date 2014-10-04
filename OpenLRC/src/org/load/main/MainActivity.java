package org.load.main;

import java.io.File;

import org.load.lrcviewtest.R;
import org.load.openlrc.LrcView;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity {
	private LrcView mLrc;
	private MediaPlayer mPlayer;
	private String mDir = Environment.getExternalStorageDirectory() + File.separator + "Download" + File.separator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 歌曲路径
		String music = mDir + "1.mp3";
		// 歌词路径
		String lrc = mDir + "1.lrc";
		
		mLrc = (LrcView) findViewById(R.id.lrc);
		// 设置背景图片
		// 可以选择不设置
		// 最好是在真个屏幕设置该图片
		// 那样更好看
		mLrc.setBackground(BitmapFactory.decodeFile(mDir + "1.png"));
		
		mPlayer = new MediaPlayer();
		try {
			mPlayer.setDataSource(music);
			mPlayer.setOnPreparedListener(new PreparedListener());
			mPlayer.prepareAsync();
			
			// 设置lrc的路径
			mLrc.setLrcPath(lrc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class PreparedListener implements OnPreparedListener {
		@Override
		public void onPrepared(MediaPlayer mp) {
		
			mPlayer.start();
			new Thread(new Runnable() {
				@Override
				public void run() {
					// 当歌曲还在播放时
					// 就一直调用changeCurrent方法
					// 虽然一直调用， 但界面不会一直刷新
					// 只有当唱到下一句时才刷新
					while(mPlayer.isPlaying()) {
						// 调用changeCurrent方法， 参数是当前播放的位置
						// LrcView会自动判断需不需要下一行
						mLrc.changeCurrent(mPlayer.getCurrentPosition());
						
						// 当然这里还是要睡一会的啦
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
}
