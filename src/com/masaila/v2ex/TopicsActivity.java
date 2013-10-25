package com.masaila.v2ex;

import net.tsz.afinal.FinalBitmap;

import com.masaila.v2ex.api.ApiClient;
import com.masaila.v2ex.bean.User;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicsActivity extends Activity {

	private static final int UPDATE_DRAWER = 1;

	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle drawerToggle;

	private PullToRefreshAttacher pullToRefreshAttacher;

	private ImageView imageViewAvatar;
	private TextView textViewUsername;

	private User user;
	private FinalBitmap finalBitmap;

	PullToRefreshAttacher getPullToRefreshAttacher() {
		return pullToRefreshAttacher;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case UPDATE_DRAWER:
				textViewUsername.setText(user.getUsername());
				System.err.println(user.getAvatar());
				finalBitmap.display(imageViewAvatar, user.getAvatar());
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
		setContentView(R.layout.activity_topics);

		init();

		pullToRefreshAttacher = PullToRefreshAttacher.get(this);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		drawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		drawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.app_name, /* "open drawer" description for accessibility */
		R.string.app_name /* "close drawer" description for accessibility */
		);

		drawerLayout.setDrawerListener(drawerToggle);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, new TopicsFragment()).commit();
	}

	private void init() {
		finalBitmap = FinalBitmap.create(TopicsActivity.this);
		textViewUsername = (TextView) findViewById(R.id.textView_username);
		imageViewAvatar = (ImageView) findViewById(R.id.imageView_avatar);
		new Thread() {
			public void run() {
				user = ApiClient.getUser(TopicsActivity.this);
				handler.sendEmptyMessage(UPDATE_DRAWER);
			}
		}.start();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

}
