package com.kanggyu.babbabdlala;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ExpandableListView restaurantList;
	private ExpandableListAdapter listAdapter;
	
	private String[] restaurants;
	private ArrayList<String> menuContents = null;
	private ArrayList<ArrayList<String>> menus;
	
	private ImageButton sidebarBtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        restaurantList = (ExpandableListView)findViewById(R.id.restaurant_list); 
        restaurants = getResources().getStringArray(R.array.restaurant_list);
        menuContents = new ArrayList<String>();
        menus = new ArrayList<ArrayList<String>>();
        loadRestaurantData();
        
        listAdapter = new ExpandableListAdapter(MainActivity.this, restaurantList, restaurants, menus);
        restaurantList.setAdapter(listAdapter);
        
        sidebarBtn = (ImageButton)findViewById(R.id.open_sidebar);
    }
    
    private void loadRestaurantData()
    {
    	// Web parsing
        menuContents.add("제육볶음");
        menuContents.add("치킨텐더");
        
        for(int i = 0; i < restaurants.length; i++)
        	menus.add(menuContents);
    }
    
    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {
    	private String[] restaurants;
    	private ArrayList<ArrayList<String>> menus;
   
    	private LayoutInflater inflater;
    	private ExpandableListView restaurantList;
    	
    	private int previousExpanded = -1;

    	ExpandableListAdapter(Context context, ExpandableListView restaurantList, String[] restaurants, ArrayList<ArrayList<String>> menus)
    	{
            this.restaurants = restaurants;
            this.menus = menus;
            this.restaurantList = restaurantList;
     
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	@Override
    	public void onGroupExpanded(int groupPosition)
    	{
    		if(previousExpanded != -1)
    			restaurantList.collapseGroup(previousExpanded);
    		previousExpanded = groupPosition;
    	}
    	
    	@Override
    	public int getGroupCount()
    	{
    		return restaurants.length;
    	}
    	
    	@Override
    	public long getGroupId(int groupPosition)
    	{
    		return groupPosition;
    	}
    	
    	@Override
    	public String getGroup(int groupPosition)
    	{
    		return restaurants[groupPosition];
    	}
    	
    	@Override
    	public String getChild(int groupPosition, int childPosition)
    	{
    		return menus.get(groupPosition).get(childPosition);
    	}
    	
    	@Override
    	public int getChildrenCount(int groupPosition)
    	{
    		return menus.get(groupPosition).size();
    	}
    	
    	@Override
    	public long getChildId(int groupPosition, int childPosition)
    	{
    		return childPosition;
    	}
    	
    	@Override
    	public boolean hasStableIds() 
    	{
    		return true;
    	}
    	
    	@Override
    	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    	{ 		
    		ViewHolder viewHolder;
    		
    		if(convertView == null)
    		{
    			viewHolder = new ViewHolder();
    			convertView = inflater.inflate(R.layout.restaurant_list, null);
    			
    			viewHolder.restaurantName = (TextView)convertView.findViewById(R.id.restaurant_name);
    			
    			convertView.setTag(viewHolder);
    		}
    		else
    			viewHolder = (ViewHolder)convertView.getTag();
    	
    		viewHolder.restaurantName.setText(restaurants[groupPosition] + " " + groupPosition);
    		
    		return convertView;
    	}
    	
    	@Override
    	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    	{
    		ViewHolder viewHolder;
    		
    		if(convertView == null)
    		{
    			viewHolder = new ViewHolder();
    			convertView = inflater.inflate(R.layout.menu_details, null);
    			
    			viewHolder.restaurantMenu = (TextView)convertView.findViewById(R.id.menu_name);
    			viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progress);
    			viewHolder.favoriteBtn = (ImageButton)convertView.findViewById(R.id.favorite);
    	        viewHolder.showReviewBtn = (ImageButton)convertView.findViewById(R.id.show_review);
    	        viewHolder.writeReviewBtn = (ImageButton)convertView.findViewById(R.id.write_review);
    	        viewHolder.likeBtn = (ImageButton)convertView.findViewById(R.id.like);
    	        viewHolder.hateBtn = (ImageButton)convertView.findViewById(R.id.hate);
    	       
    	        convertView.setTag(viewHolder);
    		}
    		else
    			viewHolder = (ViewHolder)convertView.getTag();
    		
    		final ProgressBar progressBar = viewHolder.progressBar;
    		
	        viewHolder.likeBtn.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
        			progressBar.incrementProgressBy(5);
        		}
	        });
	        viewHolder.hateBtn.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					progressBar.incrementProgressBy(-5);
				}
	        });
	        
	        viewHolder.writeReviewBtn.setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View v)
	        	{
	        		startActivity(new Intent(MainActivity.this, ReviewActivity.class));
	        	}
	        });
    		viewHolder.restaurantMenu.setText(menus.get(groupPosition).get(childPosition) + " " + groupPosition + " " + childPosition);
    		
    		return convertView;
    	}
    	
    	@Override
    	public boolean isChildSelectable(int groupPosition, int childPosition)
    	{
    		return true;
    	}
    }
    
	private static class ViewHolder
	{
		private TextView restaurantName;
    	private TextView restaurantMenu;	
    	private ProgressBar progressBar;
    	
    	private ImageButton favoriteBtn;
    	private ImageButton showReviewBtn;
    	private ImageButton writeReviewBtn;
    	private ImageButton likeBtn;
    	private ImageButton hateBtn;
	}
}