package com.masaila.v2ex.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.masaila.v2ex.R;
import com.masaila.v2ex.bean.Topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TopicsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Topic> topics;
	private LayoutInflater layoutInflater;
	private FinalBitmap finalBitmap;

	public TopicsAdapter(Context context, ArrayList<Topic> topics) {
		this.context = context;
		this.topics = topics;
		layoutInflater = LayoutInflater.from(context);
		finalBitmap = FinalBitmap.create(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return topics.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return topics.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.listview_item_topics, null);
			holder = new ViewHolder();
			holder.textViewUsername = (TextView) convertView.findViewById(R.id.textView_username);
			holder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
			holder.textViewNode = (TextView) convertView.findViewById(R.id.textView_node);
			holder.textViewReplyCount = (TextView) convertView.findViewById(R.id.textView_reply_count);
			holder.textViewReplyTime = (TextView) convertView.findViewById(R.id.textView_reply_time);
			holder.imageViewAvatar = (ImageView) convertView.findViewById(R.id.imageView_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textViewTitle.setText(topics.get(position).getTitle());
		holder.textViewUsername.setText(topics.get(position).getUsername());
		holder.textViewNode.setText(topics.get(position).getNode());
		holder.textViewReplyCount.setText(topics.get(position).getReplyCount());
		holder.textViewReplyTime.setText(topics.get(position).getReplyTime());
		finalBitmap.display(holder.imageViewAvatar, topics.get(position).getAvatar());
		return convertView;
	}
	
	/**
	 * 添加列表项
	 * @param item
	 */
	public void addItem(Topic topic) {
		topics.add(topic);
	}


	static class ViewHolder {
		ImageView imageViewAvatar;
		TextView textViewTitle;
		TextView textViewUsername;
		TextView textViewNode;
		TextView textViewReplyCount;
		TextView textViewReplyTime;
	}

}
