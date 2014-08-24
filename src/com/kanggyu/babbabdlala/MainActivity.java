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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private ExpandableListView cafeList; // 식당들의 ListView를 출력하는 ExpandableListView 변수.
	private ExpandableListAdapter listAdapter; // ListView의 레이아웃과 스크롤, 클릭 등의 설정을 담당하는 ExpandableListAdapter 변수.
	
	private String[] cafes; // 식당 이름들을 담은 배열.
	private ArrayList<String> menus = null; // 한 식당의 메뉴들을 담은 배열.
	private ArrayList<ArrayList<String>> cafesIncludeMenus; // 각자의 메뉴들(ArrayList<String>)을 담고 있는 식당들의 배열(ArrayList<String>).
	// ArrayList<String>은 배열입니다. 단, String[]과 다르게 배열을 선언할 때 크기를 안 정해도 됩니다. 동적으로 크기를 늘리거나 줄일 수 있는 배열이거든요.
	
	private ImageButton sidebarBtn; // 클릭했을 때, 사이드바를 나오게 하는 버튼. 
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 앱 상단에 제목이 뜨는 Bar를 없앤다.
        setContentView(R.layout.activity_main);
        
        cafes = getResources().getStringArray(R.array.cafe_list);
        // res > value > strings.xml에 가면 식당 이름들이 담긴 string-array가 있어요!
        
        cafeList = (ExpandableListView)findViewById(R.id.cafe_list);
        // res > layout > activity_main.xml에 cafe_list가 있음.
        menus = new ArrayList<String>();
        cafesIncludeMenus = new ArrayList<ArrayList<String>>();
        // 그냥 변수 객체 생성...
        
        loadRestaurantData(); // 각 식당마다 메뉴들을 웹 파싱으로 불러오는 함수에요. 자세한 함수 구현은 아래에 있어요.
        
        listAdapter = new ExpandableListAdapter(MainActivity.this, cafeList, cafes, cafesIncludeMenus);
        cafeList.setAdapter(listAdapter);
        
        sidebarBtn = (ImageButton)findViewById(R.id.open_sidebar); // res > layout > activity_main.xml에 open_sidebar가 있음.
        
        new GraduateParsingTask().execute((Void) null);
        /*
         * 성만이 형의 대학원 파싱을 호출하는 코드.
         * 나중에 은향이까지 파싱이 끝나면, 이 코드는 지워지고 밑 loadRestaurantData() 함수 안에 있는 new LoadingMenuTask().execute((Void)null)에 포함될거에요. 
         */
    }
    
    private void loadRestaurantData()
    {
    	new ParsingTask().execute((Void)null);
    	/*
    	 * 은향이의 생협 파싱을 호출하는 코드. 나중에 파싱 다하면, new LoadingMenuTask().execute((Void)null)로 바뀜.
    	 */
    	
    	// 여기부터~
    	menus.add("된장찌개");
    	menus.add("순두부찌개");
        
        for(int i = 0; i < cafes.length; i++)
        	cafesIncludeMenus.add(menus);
        // 여기까지는 나중에 파싱이 완성되면, LoadingMenuTask.java 파일에서 파싱된 메뉴들로 채울 것이니 딱히 필요없는 코드~
    }
    
    private class ExpandableListAdapter extends BaseExpandableListAdapter
    {
    	private String[] cafes;
    	private ArrayList<ArrayList<String>> cafesIncludeMenus;
   
    	private LayoutInflater inflater;
    	private ExpandableListView cafeList;
    	
    	private int previousExpanded = -1;
    	/* ListView에 있는 식당 목록 중 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있는지 나타내는 변수라고 생각해두자. 
    	아무 식당의 하위 메뉴가 펼쳐져 있지 않을 때, previousExpanded == -1이다. */

    	// 이 Adpater의 생성자. 파라미터로 여러가지를 받아와서 이 Adpater 클래스의 private 변수들에 저장한다.
    	ExpandableListAdapter(Context context, ExpandableListView cafeList, String[] cafes, ArrayList<ArrayList<String>> cafesIncludeMenus)
    	{
            this.cafes = cafes;
            this.cafesIncludeMenus = cafesIncludeMenus;
            this.cafeList = cafeList;
     
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /* Inflating, 스터디 시간에 배웠죠?
            xml에 짜놓은 코드를 바탕으로 View를 만들어주는 작업이라고 생각하시면 되요. LayoutInflater는 그 작업을 도와주는 셈이구요. */
    	}
    	
    	@Override
    	public void onGroupExpanded(int groupPosition) // 식당의 하위 메뉴를 펼칠 때 작동하는 함수.
    	{
    		if(previousExpanded != -1)
    			cafeList.collapseGroup(previousExpanded);
    		// ListView의 식당 목록에서 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있으면 그 열려진 식당의 하위 메뉴를 닫는다.
    		
    		previousExpanded = groupPosition;
    		// 마지막으로 눌러본(하위 메뉴를 펼쳐본) 식당의 Index(Position)를 previousExpanded에 담는다.
    	}
    	
    	@Override
    	public void onGroupCollapsed(int groupPosition)
    	{
    		if(previousExpanded == groupPosition)
    			previousExpanded = -1;
    		/* 이 ExpandableListView는 하위 메뉴를 한 식당씩만 펼쳐볼 수 있게 해놨어요. 즉, 하나를 열면 하나가 닫혀진다라는 것.
    		마지막으로 눌러본(하위 메뉴를 펼쳐본) 식당을 다시 닫으면...
    		결국 ListView에는 모든 하위 메뉴가 닫혀있다는 의미이기 때문에, previousExpanded에 -1을 줌으로써 초기 상태와 같게 만들어줘요. */
    	}
    	
    	@Override
    	public int getGroupCount() // 식당의 총 개수를 반환한다.
    	{
    		return cafes.length;
    	}
    	
    	@Override
    	public long getGroupId(int groupPosition) // 식당마다의 Index(ListView에서 배치한 순서)를 반환한다.
    	{
    		return groupPosition;
    	}
    	
    	@Override
    	public String getGroup(int groupPosition) // 각 식당의 이름을 반환하는 함수이다.
    	{
    		return cafes[groupPosition];
    	}
    	
    	@Override
    	public String getChild(int groupPosition, int childPosition) // 각 식당의 메뉴 각각의 이름을 반환하는 함수.
    	{
    		return cafesIncludeMenus.get(groupPosition).get(childPosition);
    	}
    	
    	@Override
    	public int getChildrenCount(int groupPosition) // 각 식당의 메뉴의 총 개수를 반환하는 함수.
    	{
    		return cafesIncludeMenus.get(groupPosition).size();
    	}
    	
    	@Override
    	public long getChildId(int groupPosition, int childPosition) // 각 식당의 메뉴의 Index를 반환.
    	{
    		return childPosition;
    	}
    	
    	@Override
    	public boolean hasStableIds() // 얘는 나도 모르겠음..... ㅋ_ㅋ
    	{
    		return true;
    	}
    	
    	@Override
    	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    	{ 		
    		/* ListView에 뿌려지는 View들 중 식당 이름이 보여지는 View를 반환하는 함수.
    		이 함수에서 Layout Inflating을 하고, 리스트뷰에 보여지는 식당들의 이름을 Set할 수 있다. */
    		
    		ViewHolder viewHolder; // ViewHolder에 관해서는 밑에 ViewHolder 클래스 설명을 참조하세요.
    		
    		if(convertView == null) // ListView에 뿌려지는 View 중 식당 이름을 보여주는 View가 아직 만들어지지도 않았을 때(초기 상태), 실행.
    		{
    			viewHolder = new ViewHolder();
    			convertView = inflater.inflate(R.layout.cafe_list, null);
    			// res > layout > cafe_list.xml에 정의되어있는 레이아웃을 부풀려서 View로 만든다.
    			
    			viewHolder.cafeName = (TextView)convertView.findViewById(R.id.cafe_name);
    			convertView.setTag(viewHolder);
    		}
    		else
    			viewHolder = (ViewHolder)convertView.getTag(); // 식당 이름을 보여주는 View가 이미 만들어져있을 때, 이미 만든 것을 재활용한다. (스터디 시간에 배웠어요. 모르면 참고!)
    	
    		viewHolder.cafeName.setText(cafes[groupPosition]); // 각 View에 식당 이름 설정.
    		
    		return convertView;
    	}
    	
    	@Override
    	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    	{
    		// 메뉴를 보여주는 View를 반환하는 함수.
    		
    		ViewHolder viewHolder;
    		
    		if(convertView == null) // getGroupView와 똑같이 이해하세요...
    		{
    			viewHolder = new ViewHolder();
    			convertView = inflater.inflate(R.layout.menu_details, null);
    			
    			viewHolder.cafeMenu = (TextView)convertView.findViewById(R.id.menu_name);
    			viewHolder.progressBar = (ProgressBar)convertView.findViewById(R.id.progress);
    			viewHolder.favoriteBtn = (ImageButton)convertView.findViewById(R.id.favorite);
    	        viewHolder.showReviewBtn = (ImageButton)convertView.findViewById(R.id.show_review);
    	        viewHolder.writeReviewBtn = (ImageButton)convertView.findViewById(R.id.write_review);
    	        viewHolder.likeBtn = (Button)convertView.findViewById(R.id.like);
    	        viewHolder.dislikeBtn = (Button)convertView.findViewById(R.id.dislike);
    	       
    	        convertView.setTag(viewHolder);
    		}
    		else
    			viewHolder = (ViewHolder)convertView.getTag();
    		
    		final ProgressBar progressBar = viewHolder.progressBar;
    		
    		// "좋아" 버튼을 눌렀을 때, 발생되는 이벤트.
	        viewHolder.likeBtn.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
        			progressBar.incrementProgressBy(5);
        			// 지금은 "좋아"를 누를 때마다 5%씩 Progress가 차오르지만, 나중에는 "좋아"와 "싫어"의 비율로 Progress가 정해질 예정.
        		}
	        });
	        // "싫어" 버튼을 눌렀을 때, 발생되는 이벤트.
	        viewHolder.dislikeBtn.setOnClickListener(new OnClickListener()
	        {
				@Override
				public void onClick(View v)
				{
					progressBar.incrementProgressBy(-5);
					// 지금은 "싫어"를 누를 때마다 -5%씩 Progress가 감소하지만, 나중에는 "좋아"와 "싫어"의 비율로 Progress가 정해질 예정.
				}
	        });
	        
	        // "후기 쓰기" 아이콘을 눌렀을 때, 발생되는 이벤트. 
	        viewHolder.writeReviewBtn.setOnClickListener(new OnClickListener()
	        {
	        	public void onClick(View v)
	        	{
	        		startActivity(new Intent(MainActivity.this, ReviewActivity.class));
	        		// Intent를 이용하여(스터디 시간에 배웠어요!), MainActivity에서 ReviewActivity를 작동시키고, ReviewActivity로 화면 전환.
	        	}
	        });
	        
	        // 각 메뉴들의 이름 설정.
    		viewHolder.cafeMenu.setText(cafesIncludeMenus.get(groupPosition).get(childPosition));
    		
    		return convertView;
    	}
    	
    	@Override
    	public boolean isChildSelectable(int groupPosition, int childPosition) // 얘도 잘 모르겠음 ㅠ_ㅠ
    	{
    		return true;
    	}
    }
    
	private static class ViewHolder
	{
		/* ViewHolder란, getView(getGroupView, getChildView) 함수에서 Layout Inflating할 때 자주 쓰는 View들을 모아놓은 것.
		여기에 여러가지 요소를 정의해놓고, getGroupView와 getChildView에서 알맞은 것을 갖다가 쓰면 됩니다. */
		
		private TextView cafeName;
    	private TextView cafeMenu;	
    	private ProgressBar progressBar;
    	
    	private ImageButton favoriteBtn;
    	private ImageButton showReviewBtn;
    	private ImageButton writeReviewBtn;
    	private Button likeBtn;
    	private Button dislikeBtn;
	}
}