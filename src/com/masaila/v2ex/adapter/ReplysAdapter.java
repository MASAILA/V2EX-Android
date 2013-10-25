package com.masaila.v2ex.adapter;

import java.net.ContentHandler;
import java.net.URL;
import java.util.ArrayList;

import net.tsz.afinal.FinalBitmap;

import com.masaila.v2ex.MainActivity;
import com.masaila.v2ex.R;
import com.masaila.v2ex.adapter.TopicsAdapter.ViewHolder;
import com.masaila.v2ex.bean.Reply;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ReplysAdapter extends BaseAdapter {

	private ArrayList<Reply> replies;
	private LayoutInflater layoutInflater;
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

	public ReplysAdapter(ArrayList<Reply> replies, Context context) {
		this.replies = replies;
		layoutInflater = LayoutInflater.from(context);
		finalBitmap = FinalBitmap.create(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return replies.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return replies.get(arg0);
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
			convertView = layoutInflater.inflate(R.layout.listview_item_reply,
					null);
			holder = new ViewHolder();
			holder.textViewUsername = (TextView) convertView
					.findViewById(R.id.textView_username);
			holder.textViewReplyTime = (TextView) convertView
					.findViewById(R.id.textView_reply_time);
			holder.textViewContent = (TextView) convertView
					.findViewById(R.id.textView_content);
			holder.imageViewAvatar = (ImageView) convertView
					.findViewById(R.id.imageView_avatar);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textViewUsername.setText(replies.get(position).getUsername());
		holder.textViewReplyTime.setText(replies.get(position).getReplyTime());
		holder.textViewContent.setText(Html.fromHtml(replies.get(position)
				.getContent(), imgGetter, null));
		holder.textViewContent.setMovementMethod(LinkMovementMethod
				.getInstance());
		finalBitmap.display(holder.imageViewAvatar, replies.get(position)
				.getAvatar());
		return convertView;
	}

	static class ViewHolder {
		ImageView imageViewAvatar;
		TextView textViewUsername;
		TextView textViewReplyTime;
		TextView textViewContent;
	}

}
