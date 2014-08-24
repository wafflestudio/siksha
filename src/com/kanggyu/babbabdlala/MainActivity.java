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
	private ExpandableListView cafeList;
	private ExpandableListAdapter listAdapter;
	// 식당 리스트뷰를 출력하는 ExpandableListView 변수와 ListView의 레이아웃과 스크롤을 담당하는 Adapter.
	
	private String[] cafes; // 식당 이름들을 담은 배열.
	private ArrayList<String> menus = null; // 한 식당 메뉴들의 배열.
	private ArrayList<ArrayList<String>> cafesIncludeMenus; // 각자의 메뉴들을 담고 있는 식당들의 배열.
	// ArrayList<String>은 배열입니다. 단, String[]과 다르게 선언할 때 크기를 안 정해도 됩니다. 동적으로 크기를 늘리거나 줄일 수 있는 배열이거든요.
	
	private ImageButton sidebarBtn; // 사이드바를 나오게 하는 버튼. 
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 앱 상단에 제목 뜨는 Bar를 없앤다.
        setContentView(R.layout.activity_main);

        cafeList = (ExpandableListView)findViewById(R.id.restaurant_list); 
        cafes = getResources().getStringArray(R.array.restaurant_list);
        // res -> value -> strings.xml에 가면 식당 이름들이 담긴 string-array가 있어요!
        
        menus = new ArrayList<String>();
        cafesIncludeMenus = new ArrayList<ArrayList<String>>();
        // 그냥 변수 객체 생성...
        
        loadRestaurantData(); // 각 식당마다 메뉴들을 불러오는 함수에요. 자세한 함수 구현은 아래에 있어요.
        
        listAdapter = new ExpandableListAdapter(MainActivity.this, cafeList, cafes, cafesIncludeMenus);
        // ExpandableListView 변수 생성...
        cafeList.setAdapter(listAdapter); // Expand
        
        sidebarBtn = (ImageButton)findViewById(R.id.open_sidebar);
    }
    
    private void loadRestaurantData()
    {
    	new ParsingTask().execute((Void)null);
        
        for(int i = 0; i < cafes.length; i++)
        	cafesIncludeMenus.add(menus);
    }
    
    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {
    	private String[] cafes;
    	private ArrayList<ArrayList<String>> cafesIncludeMenus;
   
    	private LayoutInflater inflater;
    	private ExpandableListView cafeList;
    	
    	private int previousExpanded = -1;
    	// ListView에 있는 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있는지 나타내는 변수라고 생각해두자. 
    	// 아무 식당의 하위 메뉴가 펼쳐져 있지 않을 때, previousExpanded == -1이다.

    	// 이 Adpater의 생성자. 파라미터로 여러가지를 받아와서 이 Adpater 클래스의 변수에 저장한다.
    	ExpandableListAdapter(Context context, ExpandableListView cafeList, String[] cafes, ArrayList<ArrayList<String>> cafesIncludeMenus)
    	{
            this.cafes = cafes;
            this.cafesIncludeMenus = cafesIncludeMenus;
            this.cafeList = cafeList;
     
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	}
    	
    	@Override
    	public void onGroupExpanded(int groupPosition)
    	{
    		if(previousExpanded != -1)
    			cafeList.collapseGroup(previousExpanded);
    		// 리스트뷰에서 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있으면 그 열려진 식당의 하위 메뉴를 닫는다.
    		
    		previousExpanded = groupPosition;
    		// 마지막으로 눌러본 식당의 Index를 previousExpanded에 담는다.
    	}
    	
    	@Override
    	public int getGroupCount()
    	{
    		return cafes.length;
    	}
    	
    	@Override
    	public long getGroupId(int groupPosition)
    	{
    		return groupPosition;
    	}
    	
    	@Override
    	public String getGroup(int groupPosition)
    	{
    		return cafes[groupPosition];
    	}
    	
    	@Override
    	public String getChild(int groupPosition, int childPosition)
    	{
    		return cafesIncludeMenus.get(groupPosition).get(childPosition);
    	}
    	
    	@Override
    	public int getChildrenCount(int groupPosition)
    	{
    		return cafesIncludeMenus.get(groupPosition).size();
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
    			
    			viewHolder.cafeName = (TextView)convertView.findViewById(R.id.restaurant_name);
    			
    			convertView.setTag(viewHolder);
    		}
    		else
    			viewHolder = (ViewHolder)convertView.getTag();
    	
    		viewHolder.cafeName.setText(cafes[groupPosition]);
    		
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
    			
    			viewHolder.cafeMenu = (TextView)convertView.findViewById(R.id.menu_name);
    			viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progress);
    			viewHolder.favoriteBtn = (ImageButton)convertView.findViewById(R.id.favorite);
    	        viewHolder.showReviewBtn = (ImageButton)convertView.findViewById(R.id.show_review);
    	        viewHolder.writeReviewBtn = (ImageButton)convertView.findViewById(R.id.write_review);
    	        viewHolder.likeBtn = (ImageButton)convertView.findViewById(R.id.like);
    	        viewHolder.dislikeBtn = (ImageButton)convertView.findViewById(R.id.hate);
    	       
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
	        viewHolder.dislikeBtn.setOnClickListener(new OnClickListener()
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
    		viewHolder.cafeMenu.setText(cafesIncludeMenus.get(groupPosition).get(childPosition));
    		
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
		private TextView cafeName;
    	private TextView cafeMenu;	
    	private ProgressBar progressBar;
    	
    	private ImageButton favoriteBtn;
    	private ImageButton showReviewBtn;
    	private ImageButton writeReviewBtn;
    	private ImageButton likeBtn;
    	private ImageButton dislikeBtn;
	}
}