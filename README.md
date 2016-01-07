OpenLRC
=======
##注意：新版本的控件支持歌词的滑动
## [可以到这里下载](https://git.oschina.net/qibin/LitePlayer)

一个可以在android下显示歌词的控件
可以设置歌词字体大小，当前行歌词的颜色、其他行歌词的颜色、行间距等。


![image](https://github.com/qibin0506/OpenLRC/blob/master/show/show.png)


使用方法：

  1、xml布局：
	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	xmlns:lrc="http://schemas.android.com/apk/res/org.load.lrcviewtest"
	xmlns:tools="http://schemas.android.com/tools"
	android:layout_width="match_parent"
	android:layout_height="match_parent"
	android:orientation="vertical"
	tools:context=".MainActivity"
	android:background="@android:color/black" >

	<org.load.openlrc.LrcView
		android:id="@+id/lrc"
		android:layout_marginTop="100dip"
		android:layout_width="match_parent"
		android:layout_height="match_parent"
		android:background="@android:color/white"
		lrc:currentTextColor="@android:color/holo_orange_light"
		lrc:normalTextColor="@android:color/white"
		lrc:rows="5"
		lrc:textSize="20sp"
		lrc:dividerHeight="10dip" />
	
</LinearLayout>

  2、在activity中使用：
	
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
  
