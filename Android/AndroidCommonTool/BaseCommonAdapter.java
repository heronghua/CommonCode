package com.make.sqorts.adapter;

import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author heronghua1989@126.com
 *
 * @param <M> the moudle of one item
 */
public abstract class BaseCommonAdapter<M> extends BaseAdapter {
	/**
	 * the moudle  list
	 */
	private final List<M> data = new ArrayList<M>(); 
	
	private @LayoutRes int mLayout;
	
	public BaseCommonAdapter(int layout) {  
		this(new ArrayList<M>(),layout);
		
	} 
	
	public BaseCommonAdapter(List<M> list, int layout) {  
		resetData(list);
		this.mLayout = layout; 
		
	} 
	
	public void clearData(){
		this.data.clear();
	}
	
	public void addData(List<M> data){
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}
	
	public void resetData(List<M> data){
		clearData();
		if (data!=null) {
			addData(data);
		}else{
			this.notifyDataSetChanged();
		}
	}
	
	public List<M> getData() {
		return data;
	}

	@Override  
	public int getCount() {  
		return data == null ? 0 : data.size();  
	}  

	@Override  
	public M getItem(int position) {  
		return data == null ? null : data.get(position);  
	}  

	@Override  
	public long getItemId(int position) {  
		return position;  
	}  

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {  
		ViewHolder holder = ViewHolder.getInstance(parent, convertView, mLayout);  
		convert(holder, data.get(position));
		afterConvert(holder, data.get(position), position);
		return holder.getConvertView();  
	}  

	/** 
	 * initial each item of the AdapterView,get child view via {@code ViewHolder.findViewById()}
	 * @param holder
	 * @param currentMoudle the data moudle
	 */
	public abstract void convert(ViewHolder holder, M currentMoudle);
	
	public void afterConvert(ViewHolder holder, M currentMoudle,int position){
		
	}

	protected static class ViewHolder {  
		private View convertView;  
		/**a memory cache container*/
		private SparseArray<View> views;  

		private ViewHolder(View convertView) {  
			this.views = new SparseArray<>();
			this.convertView = convertView;  
			convertView.setTag(this);  
		}  

		public static ViewHolder getInstance(ViewGroup parent, View convertView, int layout) {  
			if (convertView == null) {  
				convertView = LayoutInflater.from(parent.getContext()).inflate(layout, parent,false);  
				return new ViewHolder(convertView);  
			}  
			// reuse convertView  
			return (ViewHolder) convertView.getTag(); 
		}  

		@SuppressWarnings("unchecked")  
		public <V extends View> V findViewById(int id) {  
			// get view from memory cache
			View view = views.get(id); 
			if (view == null) {  
				view = convertView.findViewById(id);  
				// put the view into memory cache
				views.append(id, view); 
			}  
			return (V) view;  
		}  

		private View getConvertView() {  
			return convertView;  
		}  
	}  

}
