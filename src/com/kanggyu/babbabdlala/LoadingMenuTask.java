package com.kanggyu.babbabdlala;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadingMenuTask
{
	private static HashMap<String, ArrayList<String>> map;
	private Context context;
	private ExpandableListView cafeList;
	private ExpandableListAdapter listAdapter;
	
	public LoadingMenuTask(Context context, ExpandableListView cafeList)
	{
		map = new HashMap<String, ArrayList<String>>();
		this.context = context;
		this.cafeList = cafeList;
	}
	
	public class Parsing extends AsyncTask<Void, Void, Boolean>
	{
		private Document graduatePage;
		private Document jikYoungPage;
		private Document junJikYoungPage;
		
		private Graduate graduate;
		private JikYoung jikYoung;
		private JunJikYoung junJikYoung;
		
		private ProgressDialog dialog;
		
		private final String[] jikYoungCafes = {"학생회관식당", "3식당", "기숙사식당", "자하연식당", "302동식당", "동원관식당", "감골식당"};
		private final String[] junJikYoungCafes = {"4식당", "두레미담", "301동식당", "공대간이식당", "솔밭간이식당", "상아회관", "220동식당"};
		
		private int nowHour;
		private int nowMinute;
		private int nowAmPm;
		
		@Override
		protected void onPreExecute()
		{
			nowHour = Calendar.getInstance().get(Calendar.AM_PM);
			nowAmPm = Calendar.getInstance().get(Calendar.HOUR);
			nowMinute = Calendar.getInstance().get(Calendar.MINUTE);
				
			dialog = new ProgressDialog(context);
			dialog.setCancelable(false);
			dialog.setTitle("잠시만 기다려주세요!");
			
			dialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params)
		{
			try
			{
				graduatePage = Jsoup.connect("http://dorm.snu.ac.kr/dk_board/facility/food.php").get();
				jikYoungPage = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu1.asp").get();
				junJikYoungPage = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu2.asp").get();
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			if(result == true)
			{
				graduate = new Graduate();
				jikYoung = new JikYoung();
				junJikYoung = new JunJikYoung();
				
				listAdapter = new ExpandableListAdapter(context, cafeList, MainActivity.cafes, MainActivity.operatingHours, MainActivity.locations);
				cafeList.setAdapter(listAdapter);
			}
			
			dialog.dismiss();
		}
		
		private class Graduate
		{
			private ArrayList<ArrayList<String>> weeklyBreakfast;
			private ArrayList<ArrayList<String>> weeklyLunch;
			private ArrayList<ArrayList<String>> weeklySupper;
			
			private int todayIndex;
			
			public Graduate()
			{
				todayIndex = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
				weeklyBreakfast = new ArrayList<ArrayList<String>>();
				weeklyLunch = new ArrayList<ArrayList<String>>();
				weeklySupper = new ArrayList<ArrayList<String>>();
				
				selecting();
			}
			
			public void selecting()
			{
				Elements tableBody = graduatePage.select("tbody").get(0).children();
				/* tbody 안에 있는 tr 태그를 모두 긁어온다. 의미가 같은 코드는 graduate.select("tbody:nth-child(0) tr");
				tableBody.size() = 14;
				tableBody의 14개의 tr 중 0~1번째는 아침, 2~4번째는 점심, 6~7번째는 저녁이다.
				그래서 아침, 점심, 저녁의 세 파트로 나누어 파싱을 진행한다. */
				
				/* 아침 관련 파싱 */
				for(int i = 0; i < 2; i++)
				{		
					Element tr = tableBody.get(i);
					Elements td = tr.select("td:not(td[rowspan], td[class=bg])");
					// 음식 이름이 있는 td만 모두 긁어온다.
					
					ArrayList<String> oneTrLine = new ArrayList<String>();
					for(Element e : td)	
					{
						String price = e.select("li").attr("class");
						
						if(e.text().trim().length() != 0)
						{
							if(price.equals("menu_a"))
								oneTrLine.add(e.text().trim() + "#2000");
							else if(price.equals("menu_b"))
								oneTrLine.add(e.text().trim() + "#2500");
							else if(price.equals("menu_c"))
								oneTrLine.add(e.text().trim() + "#3000");
							else if(price.equals("menu_d"))
								oneTrLine.add(e.text().trim() + "#3500");
							else if(price.equals("menu_e"))
								oneTrLine.add(e.text().trim() + "#4000");
						}
						else
							oneTrLine.add("");
					}
					weeklyBreakfast.add(oneTrLine);
				}
				
				/* 점심 관련 파싱 */
				for(int i = 2; i < 5; i++)
				{
					Element tr = tableBody.get(i);
					Elements td = tr.select("td:not(td[rowspan], td[class=bg])");
					// 음식 이름이 있는 td만 모두 긁어온다.
					
					ArrayList<String> oneTrLine = new ArrayList<String>();
					for(Element e : td)	
					{
						String price = e.select("li").attr("class");
						
						if(e.text().trim().length() != 0)
						{
							if(price.equals("menu_a"))
								oneTrLine.add(e.text().trim() + "#2000");
							else if(price.equals("menu_b"))
								oneTrLine.add(e.text().trim() + "#2500");
							else if(price.equals("menu_c"))
								oneTrLine.add(e.text().trim() + "#3000");
							else if(price.equals("menu_d"))
								oneTrLine.add(e.text().trim() + "#3500");
							else if(price.equals("menu_e"))
								oneTrLine.add(e.text().trim() + "#4000");
						}
						else
							oneTrLine.add("");
					}
					weeklyLunch.add(oneTrLine);
				}
				
				/* 저녁 관련 파싱 */
				for(int i = 6; i < 8; i++)
				{
					Element tr = tableBody.get(i);
					Elements td = tr.select("td:not(td[rowspan], td[class=bg])");
					// 음식 이름이 있는 td만 모두 긁어온다.
					
					ArrayList<String> oneTrLine = new ArrayList<String>();
					for(Element e : td)	
					{
						String price = e.select("li").attr("class");
						
						if(e.text().trim().length() != 0)
						{
							if(price.equals("menu_a"))
								oneTrLine.add(e.text().trim() + "#2000");
							else if(price.equals("menu_b"))
								oneTrLine.add(e.text().trim() + "#2500");
							else if(price.equals("menu_c"))
								oneTrLine.add(e.text().trim() + "#3000");
							else if(price.equals("menu_d"))
								oneTrLine.add(e.text().trim() + "#3500");
							else if(price.equals("menu_e"))
								oneTrLine.add(e.text().trim() + "#4000");
						}
						else
							oneTrLine.add("");
					}
					weeklySupper.add(oneTrLine);
				}
				
				ArrayList<String> temp = new ArrayList<String>();
				
				if(nowAmPm == 0 && nowHour < 10)
				{	
					for(int i = 0; i < weeklyBreakfast.size(); i++)
					{
						String str = weeklyBreakfast.get(i).get(todayIndex);
						if(!str.equals("") && !str.equals(" "))
							temp.add(str);
					}
				}
				else if((nowAmPm == 0 && nowHour >= 10) && (nowAmPm == 1 && nowHour < 4))
				{
					for(int i = 0; i < weeklyLunch.size(); i++)
					{
						String str = weeklyLunch.get(i).get(todayIndex);
						if(!str.equals("") && !str.equals(" "))
							temp.add(str);
					}
				}
				else
				{
					for(int i = 0; i < weeklySupper.size(); i++)
					{
						String str = weeklySupper.get(i).get(todayIndex);
						if(!str.equals("") && !str.equals(" "))
							temp.add(str);
					}
				}
				map.put("아워홈", temp);
			}
		}
		
		private class JikYoung
		{
			private ArrayList<String[]> breakfastAll;
			private ArrayList<String[]> lunchAll;
			private ArrayList<String[]> supperAll;

			public JikYoung()
			{
				breakfastAll = new ArrayList<String[]>();
				lunchAll = new ArrayList<String[]>();
				supperAll = new ArrayList<String[]>();
			
				selecting();
			}
	
			public void selecting()
			{
				Elements jikYoungTable = jikYoungPage.select("table:has(img[alt=상품명])");
				
				for(int i = 0; i < jikYoungCafes.length; i++)
				{
					Elements tr = jikYoungTable.select("tr:contains(" + jikYoungCafes[i] + ")");
					String[] breakfast = tr.select("td:nth-child(3)").text().split("/");
					String[] lunch = tr.select("td:nth-child(5)").text().split("/");
					String[] supper = tr.select("td:nth-child(7)").text().split("/");
					
					setPrice(breakfast);
					setPrice(lunch);
					setPrice(supper);
					
					breakfastAll.add(breakfast);
					lunchAll.add(lunch);
					supperAll.add(supper);
					
					ArrayList<String> temp = new ArrayList<String>();
					
					if(nowAmPm == 0 && nowHour < 10)
					{
						for(int j = 0; j < breakfast.length; j++)
							temp.add(breakfast[j].trim());
					}
					else if((nowAmPm == 0 && nowHour >= 10) && (nowAmPm == 1 && nowHour < 4))
					{
						for(int j = 0; j < lunch.length; j++)
							temp.add(lunch[j].trim());
					}
					else
					{
						for(int j = 0; j < supper.length; j++)
							temp.add(supper[j].trim());
					}
					map.put(jikYoungCafes[i], temp);
					
					/*
					ArrayList<String> temp = new ArrayList<String>();
					for(int j = 0; j < lunch.length; j++)
						temp.add(lunch[j].trim());
					map.put(jikYoungCafes[i], temp);*/
				}
			}
			
			public void setPrice(String[] arr) 
			{
				for(int j = 0; j < arr.length; j++)
				{
					char start = arr[j].charAt(0);
					
					if(start == 'ⓐ')
						arr[j] = arr[j].substring(1) + "#1700";
					else if(start == 'ⓑ')
						arr[j] = arr[j].substring(1) + "#2000";
					else if(start == 'ⓒ')
						arr[j] = arr[j].substring(1) + "#2500";
					else if(start == 'ⓓ')
						arr[j] = arr[j].substring(1) + "#3000";
					else if(start == 'ⓔ')
						arr[j] = arr[j].substring(1) + "#3500";
					else if(start == 'ⓕ')
						arr[j] = arr[j].substring(1) + "#4000";
					else if(start == 'ⓖ')
						arr[j] = arr[j].substring(1) + "#4500";
					else if(start == 'ⓗ')
						arr[j] = arr[j].substring(1) + "#Etc";
				}
			}
		}
	
		private class JunJikYoung
		{
			private ArrayList<String[]> lunchAll;
			private ArrayList<String[]> supperAll;
	
			public JunJikYoung()
			{
				lunchAll = new ArrayList<String[]>();
				supperAll = new ArrayList<String[]>();
				
				selecting();
			}
	
			public void selecting()
			{
				Elements junJikYoungTable = junJikYoungPage.select("table:has(img[alt=상품명])");
	
				for(int i = 0; i < junJikYoungCafes.length; i++)
				{
					Elements tr = junJikYoungTable.select("tr:contains(" + junJikYoungCafes[i] + ")");
					String[] lunch = tr.select("td:nth-child(5)").text().split("/");
					String[] supper = tr.select("td:nth-child(7)").text().split("/");

					setPrice(lunch);
					setPrice(supper);
					
					lunchAll.add(lunch);
					supperAll.add(supper); 
					
					ArrayList<String> temp = new ArrayList<String>();
					
					if((nowAmPm == 1 && nowHour >= 5) && (nowAmPm == 1 && nowHour < 12))
					{
						for(int j = 0; j < supper.length; j++)
							temp.add(supper[j].trim());
					}
					else
					{
						for(int j = 0; j < lunch.length; j++)
							temp.add(lunch[j].trim());
					}
					map.put(junJikYoungCafes[i], temp);
				}	
			}
			
			public void setPrice(String[] arr) 
			{
				for(int j = 0; j < arr.length; j++)
				{
					char start = arr[j].charAt(0);
					
					if(start == 'ⓐ')
						arr[j] = arr[j].substring(1) + "#1700";
					else if(start == 'ⓑ')
						arr[j] = arr[j].substring(1) + "#2000";
					else if(start == 'ⓒ')
						arr[j] = arr[j].substring(1) + "#2500";
					else if(start == 'ⓓ')
						arr[j] = arr[j].substring(1) + "#3000";
					else if(start == 'ⓔ')
						arr[j] = arr[j].substring(1) + "#3500";
					else if(start == 'ⓕ')
						arr[j] = arr[j].substring(1) + "#4000";
					else if(start == 'ⓖ')
						arr[j] = arr[j].substring(1) + "#4500";
					else if(start == 'ⓗ')
						arr[j] = arr[j].substring(1) + "#Etc";
				}
			}
		}
	}
	
	private class ExpandableListAdapter extends BaseExpandableListAdapter
	{
		private ArrayList<ArrayList<String>> cafesIncludeMenus;
		// 각자의 메뉴들(ArrayList<String>)을 담고 있는 식당들의 배열(ArrayList<String>).

		private ExpandableListView cafeList;
		private Context context;
		private String[] cafes;
		private HashMap<String, String> operatingHours;
		private HashMap<String, String> locations;
		
		private TextView cafeName;
		private TextView operatingHour;
		private TextView location;
		private Button cafeInfo;

		private int previousExpanded = -1;
		/* ListView에 있는 식당 목록 중 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있는지 나타내는 변수라고 생각해두자. 
		아무 식당의 하위 메뉴가 펼쳐져 있지 않을 때, previousExpanded == -1이다. */

		public ExpandableListAdapter(Context context, ExpandableListView cafeList, String[] cafes, HashMap<String, String> operatingHours, HashMap<String, String> locations)
		{
			this.context = context;
	        this.cafes = cafes;
	        this.cafeList = cafeList;
	        this.operatingHours = operatingHours;
	        this.locations = locations;
	        
	        cafesIncludeMenus = new ArrayList<ArrayList<String>>();
	        cafesIncludeMenus.add(map.get("301동식당"));
	        cafesIncludeMenus.add(map.get("302동식당"));
	        cafesIncludeMenus.add(map.get("공대간이식당"));
	        cafesIncludeMenus.add(map.get("학생회관식당"));
	        cafesIncludeMenus.add(map.get("자하연식당"));
	        cafesIncludeMenus.add(map.get("3식당"));
	        cafesIncludeMenus.add(map.get("4식당"));
	        cafesIncludeMenus.add(map.get("두레미담"));
	        cafesIncludeMenus.add(map.get("기숙사식당"));
	        cafesIncludeMenus.add(map.get("아워홈"));
	        cafesIncludeMenus.add(map.get("220동식당"));
	        cafesIncludeMenus.add(map.get("상아회관"));
	        cafesIncludeMenus.add(map.get("동원관식당"));
	        cafesIncludeMenus.add(map.get("감골식당"));
		}
		
		public void onGroupExpanded(int groupPosition) // 식당의 하위 메뉴를 펼칠 때 작동하는 함수.
		{
			if(previousExpanded != -1)
				cafeList.collapseGroup(previousExpanded);
			// ListView의 식당 목록에서 어떤 식당을 누를 때, 이미 다른 식당의 하위 메뉴가 펼쳐져 있으면 그 열려진 식당의 하위 메뉴를 닫는다.
			
			previousExpanded = groupPosition;
			// 마지막으로 눌러본(하위 메뉴를 펼쳐본) 식당의 Index(Position)를 previousExpanded에 담는다.
		}
		
		public void onGroupCollapsed(int groupPosition)
		{
			if(previousExpanded == groupPosition)
				previousExpanded = -1;
			/* 이 ExpandableListView는 하위 메뉴를 한 식당씩만 펼쳐볼 수 있게 해놨어요. 즉, 하나를 열면 하나가 닫혀진다라는 것.
			마지막으로 눌러본(하위 메뉴를 펼쳐본) 식당을 다시 닫으면...
			결국 ListView에는 모든 하위 메뉴가 닫혀있다는 의미이기 때문에, previousExpanded에 -1을 줌으로써 초기 상태와 같게 만들어줘요. */
		}
		
		public int getGroupCount() // 식당의 총 개수를 반환한다.
		{
			return cafes.length;
		}
		
		public long getGroupId(int groupPosition) // 식당마다의 Index(ListView에서 배치한 순서)를 반환한다.
		{
			return groupPosition;
		}
		
		public String getGroup(int groupPosition) // 각 식당의 이름을 반환하는 함수이다.
		{
			return cafes[groupPosition];
		}
		
		public String getChild(int groupPosition, int childPosition) // 각 식당의 메뉴 각각의 이름을 반환하는 함수.
		{
			return cafesIncludeMenus.get(groupPosition).get(childPosition);
		}
		
		public int getChildrenCount(int groupPosition) // 각 식당의 메뉴의 총 개수를 반환하는 함수.
		{
			return cafesIncludeMenus.get(groupPosition).size();
		}
		
		public long getChildId(int groupPosition, int childPosition) // 각 식당의 메뉴의 Index를 반환.
		{
			return childPosition;
		}
		
		public boolean hasStableIds()
		{
			return true;
		}
		
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{ 		
			/* ListView에 뿌려지는 View들 중 식당 이름이 보여지는 View를 반환하는 함수.
			이 함수에서 Layout Inflating을 하고, 리스트뷰에 보여지는 식당들의 이름을 Set할 수 있다. */
			
			final String name = cafes[groupPosition];
			
			if(convertView == null) // ListView에 뿌려지는 View 중 식당 이름을 보여주는 View가 아직 만들어지지도 않았을 때만(초기 상태) 실행.
			{
				convertView = LayoutInflater.from(context).inflate(R.layout.cafe_list, null);
				/* res > layout > cafe_list.xml에 정의되어있는 레이아웃을 부풀려서 View로 만든다.
				Inflating, 스터디 시간에 배웠죠?
	            xml에 짜놓은 코드를 바탕으로 View를 만들어주는 작업이라고 생각하시면 되요. */
			}
			
			cafeName = (TextView)convertView.findViewById(R.id.cafe_name);
    		cafeInfo = (Button)convertView.findViewById(R.id.show_info);
    		
			cafeName.setText(name); // 각 View에 식당 이름 설정.
			
			cafeInfo.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v)
				{
					CafeInfoDialog dialog = new CafeInfoDialog(context, name, operatingHours, locations);
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				}
			});
			
			return convertView;
		}
		
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
		{
			View view = null;
			String child = cafesIncludeMenus.get(groupPosition).get(childPosition);
			
			if(convertView == null)
				view = LayoutInflater.from(context).inflate(R.layout.menu_details, null);
			else
				view = convertView;
			
			RelativeLayout menuLayout = (RelativeLayout)view.findViewById(R.id.menu_details);
			final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progress);
			
			TextView cafeMenu = (TextView)view.findViewById(R.id.menu_name);	
			TextView price = (TextView)view.findViewById(R.id.menu_price);
			ImageButton favoriteBtn = (ImageButton)view.findViewById(R.id.favorite);
			ImageButton showReviewBtn = (ImageButton)view.findViewById(R.id.show_review);
			ImageButton writeReviewBtn = (ImageButton)view.findViewById(R.id.write_review);
			Button likeBtn = (Button)view.findViewById(R.id.like);
			Button dislikeBtn = (Button)view.findViewById(R.id.dislike);
			
			TextView noMenuLayout = (TextView)view.findViewById(R.id.no_menu);
			
			if(child.length() > 1)
			{	
				menuLayout.setVisibility(View.VISIBLE);
				noMenuLayout.setVisibility(View.GONE);
				
				cafeMenu.setText(child.substring(0, child.indexOf('#')));
				progressBar.setProgress(15);
				
				if(child.endsWith("1700"))
					price.setText("1.7");
				else if(child.endsWith("2000"))
					price.setText("2.0");
				else if(child.endsWith("2500"))
					price.setText("2.5");
				else if(child.endsWith("3000"))
					price.setText("3.0");
				else if(child.endsWith("3500"))
					price.setText("3.5");
				else if(child.endsWith("4000"))
					price.setText("4.0");
				else if(child.endsWith("4500"))
					price.setText("4.5");
				else if(child.endsWith("Etc"))
					price.setText("Etc");
			
				// "좋아" 버튼을 눌렀을 때, 발생되는 이벤트.
				likeBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						progressBar.incrementProgressBy(5);
						// 지금은 "좋아"를 누를 때마다 5%씩 Progress가 차오르지만, 나중에는 "좋아"와 "싫어"의 비율로 Progress가 정해질 예정.
					}
				});
	        
				// "싫어" 버튼을 눌렀을 때, 발생되는 이벤트.
				dislikeBtn.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						progressBar.incrementProgressBy(-5);
						// 지금은 "싫어"를 누를 때마다 -5%씩 Progress가 감소하지만, 나중에는 "좋아"와 "싫어"의 비율로 Progress가 정해질 예정.
					}
				});
	        
				// "후기 쓰기" 아이콘을 눌렀을 때, 발생되는 이벤트. 
				writeReviewBtn.setOnClickListener(new OnClickListener()
				{
					public void onClick(View v)
					{
						((Activity)context).startActivity(new Intent(context, ReviewActivity.class));
						// Intent를 이용하여(스터디 시간에 배웠어요!), MainActivity에서 ReviewActivity를 작동시키고, ReviewActivity로 화면 전환.
					}
				});
			}
			else
			{
				menuLayout.setVisibility(View.GONE);
				noMenuLayout.setVisibility(View.VISIBLE);
			}
			
			return view;
		}
		
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return false;
		}
	}
}