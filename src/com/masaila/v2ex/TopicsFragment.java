package com.masaila.v2ex;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import com.masaila.v2ex.adapter.TopicsAdapter;
import com.masaila.v2ex.api.ApiClient;
import com.masaila.v2ex.bean.Topic;
import com.masaila.v2ex.bean.User;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class TopicsFragment extends Fragment implements
		PullToRefreshAttacher.OnRefreshListener {

	private static final int GET_TOPICS = 1;
	private static final int SET_TOPICS = 2;
	private static final int UPDATE_TOPICS = 3;
	private static final int UPDATE_PROGRESSBAR = 4;

	private int page = 0;
	private boolean updating = false;
	private int updatingWhere;
	private int[] progressbarImgs = { R.drawable.list_loading_dark_0,
			R.drawable.list_loading_dark_1, R.drawable.list_loading_dark_2,
			R.drawable.list_loading_dark_3, R.drawable.list_loading_dark_4,
			R.drawable.list_loading_dark_5 };

	private TopicsAdapter adapter;
	private ArrayList<Topic> topics;
	private User user;

	private PullToRefreshAttacher pullToRefreshAttacher;
	private ImageView imageViewUpdating;
	private ListView listViewTopis;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case GET_TOPICS:

				break;
			case SET_TOPICS:
				listViewTopis.setAdapter(adapter);
				pullToRefreshAttacher.setRefreshComplete();
				break;
			case UPDATE_TOPICS:
				adapter.notifyDataSetChanged();
				imageViewUpdating.setVisibility(View.GONE);
				updating = false;
				pullToRefreshAttacher.setRefreshComplete();
				break;
			case UPDATE_PROGRESSBAR:
				imageViewUpdating
						.setImageResource(progressbarImgs[updatingWhere]);
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		new Thread() {
			public void run() {
				user = ApiClient.getUser(getActivity());
				topics = ApiClient.getTopics(getActivity());
				adapter = new TopicsAdapter(getActivity(), topics);
				handler.sendEmptyMessage(SET_TOPICS);
			}
		}.start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.fragment_topics, container, false);
		pullToRefreshAttacher = ((TopicsActivity) getActivity())
				.getPullToRefreshAttacher();
		listViewTopis = (ListView) view.findViewById(android.R.id.list);
		listViewTopis.setOnScrollListener(onScrollListener);
		listViewTopis.setOnItemClickListener(onItemClickListener);
		imageViewUpdating = (ImageView) view
				.findViewById(R.id.imageView_updating);
		pullToRefreshAttacher.addRefreshableView(listViewTopis, this);
		pullToRefreshAttacher.setRefreshing(true);
		return view;
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		new Thread() {
			public void run() {
				adapter = new TopicsAdapter(getActivity(),
						ApiClient.getTopics(getActivity()));
				handler.sendEmptyMessage(SET_TOPICS);
			}
		}.start();
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Bundle bundle = new Bundle();
			bundle.putSerializable("topic", topics.get(arg2));
			Intent intent = new Intent(getActivity(), TopicActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	};

	OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			switch (scrollState) {
			// 当不滚动时
			case OnScrollListener.SCROLL_STATE_IDLE:
				// 判断滚动到底部
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
					page++;
					updating = true;
					// progressBarUpdating.setVisibility(View.VISIBLE);
					imageViewUpdating.setVisibility(View.VISIBLE);

					new Thread() {
						public void run() {
							while (updating) {
								for (int i = 0; i < 6; i++) {
									updatingWhere = i;
									handler.sendEmptyMessage(UPDATE_PROGRESSBAR);
									try {
										Thread.sleep(300);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}.start();

					new Thread() {
						public void run() {
							ArrayList<Topic> topics = ApiClient
									.getTopicsAtPage(page, getActivity());
							for (int i = 0; i < topics.size(); i++) {
								adapter.addItem(topics.get(i));
							}
							handler.sendEmptyMessage(UPDATE_TOPICS);
						}
					}.start();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	};
}
