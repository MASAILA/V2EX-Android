package com.masaila.v2ex;

import java.net.URL;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.masaila.v2ex.adapter.ReplysAdapter;
import com.masaila.v2ex.api.ApiClient;
import com.masaila.v2ex.bean.Reply;
import com.masaila.v2ex.bean.Topic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TopicActivity extends Activity {

	private static final int UPDATE_CONTENT = 1;

	private Topic topic;
	private ReplysAdapter replysAdapter;

	private TextView textViewContent;
	private TextView textViewUsername;
	private TextView textViewReplyTime;
	private TextView textViewNode;
	private TextView textViewClick;
	private TextView textViewReplyCount;
	private TextView textViewTitle;
	private ImageView imageViewAvatar;
	private LinearLayout layoutProgress;

	private ListView listViewReply;
	private LinearLayout layoutHeader;

	private FinalBitmap finalBitmap;

	ImageGetter imgGetter = new Html.ImageGetter() {
		public Drawable getDrawable(String source) {
			Drawable drawable = null;
			URL url;
			try {
				url = new URL(source);
				drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			return drawable;
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case UPDATE_CONTENT:
				listViewReply.setAdapter(replysAdapter);
				textViewUsername.setText(topic.getUsername());
				textViewReplyTime.setText(topic.getReplyTime());
				textViewContent.setText(Html.fromHtml(topic.getContent(),
						imgGetter, null));
				textViewNode.setText(topic.getNode());
				textViewClick.setText(topic.getClick());
				textViewReplyCount.setText(topic.getReplyCount() + "条回复");
				textViewTitle.setText(topic.getTitle());
				finalBitmap.display(imageViewAvatar, topic.getAvatar());
				layoutProgress.setVisibility(View.GONE);
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		topic = (Topic) getIntent().getExtras().getSerializable("topic");
		struct();
		init();

		new Thread() {
			public void run() {
				topic = ApiClient.getTopic(topic.getUrl(), topic,
						TopicActivity.this);
				replysAdapter = new ReplysAdapter(ApiClient.getReplys(
						topic.getUrl(), Integer.valueOf(topic.getReplyCount()),
						TopicActivity.this), TopicActivity.this);
				handler.sendEmptyMessage(UPDATE_CONTENT);
			}
		}.start();
	}

	private void init() {
		setTitle(topic.getTitle());
		finalBitmap = FinalBitmap.create(TopicActivity.this);
		LayoutInflater layoutInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutHeader = (LinearLayout) layoutInflater.inflate(
				R.layout.listview_reply_header, null, false);
		listViewReply = (ListView) findViewById(R.id.listView_reply);
		layoutProgress = (LinearLayout) findViewById(R.id.linearLayout_progress);
		textViewUsername = (TextView) layoutHeader
				.findViewById(R.id.textView_username);
		textViewReplyTime = (TextView) layoutHeader
				.findViewById(R.id.textView_reply_time);
		imageViewAvatar = (ImageView) layoutHeader
				.findViewById(R.id.imageView_avatar);
		textViewContent = (TextView) layoutHeader
				.findViewById(R.id.textView_content);
		textViewClick = (TextView) layoutHeader
				.findViewById(R.id.textView_click);
		textViewReplyCount = (TextView) layoutHeader
				.findViewById(R.id.textView_reply_count);
		textViewTitle = (TextView) layoutHeader
				.findViewById(R.id.textView_title);
		textViewNode = (TextView) layoutHeader.findViewById(R.id.textView_node);
		listViewReply.addHeaderView(layoutHeader);
		listViewReply.setHeaderDividersEnabled(false);
		listViewReply.setAdapter(replysAdapter);
		textViewContent.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.topic, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return true;
	}

	public static void struct() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork() // or
																		// .detectAll()
																		// for
																		// all
																		// detectable
																		// problems
				.penaltyLog().build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
				.penaltyLog() // 打印logcat
				.penaltyDeath().build());
	}

}
