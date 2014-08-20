package com.kanggyu.babbabdlala;

import android.app.Activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends Activity
{
	private ImageButton sidebarBtn; // 클릭했을 때, 사이드바를 나오게 하는 버튼.
	private LoadingMenuTask loadingMenuTask;
	private ExpandableListView cafeList; // 식당들의 ListView를 출력하는 ExpandableListView 변수.

	public static String[] cafes; // 식당 이름들을 담은 배열.
	public static HashMap<String, String> operatingHours;
	public static HashMap<String, String> locations;
	
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
        sidebarBtn = (ImageButton)findViewById(R.id.open_sidebar); // res > layout > activity_main.xml에 open_sidebar가 있음.
        
        operatingHours = new HashMap<String, String>();
        locations = new HashMap<String, String>();
        loadingMenuTask = new LoadingMenuTask(MainActivity.this, cafeList);
        
        loadingCafeData();
    }
    
    private void loadingCafeData()
    {
    	/* 운영 시간 */
    	operatingHours.put("301동 식당", "오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    	operatingHours.put("302동 식당", "오전 11:15 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    	operatingHours.put("공대 간이 식당", "미확인");
    	operatingHours.put("학생회관 식당", "<A코너>\n오전 11:00 ~ 오후 2:00\n오후 4:30 ~ 오후 6:30\n\n<B코너>\n오전 8:00 ~ 오전 10:00\n오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00\n\n<C코너>\n오전 10:00 ~ 오후 4:30\n오후 5:30 ~ 오후 9:00");
    	operatingHours.put("자하연 식당", "오전 11:00 ~ 오후 2:00 / 오후 5:00 ~ 오후 7:00");
    	operatingHours.put("농생대 3식당", "<3층>\n오전 11:00 ~ 오후 2:00\n\n<4층>\n오전 11:30 ~ 오후 1:30\n오후 5:00 ~ 오후 7:00");
    	operatingHours.put("사범대 4식당", "<1층>\n오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 6:30\n\n<2층>오전 11:30 ~ 오후 7:00");
    	operatingHours.put("두레미담", "오전 11:30 ~ 오후 9:00");
    	operatingHours.put("기숙사 919동 식당", "오전 7:30 ~ 오전 9:30\n오전 11:30 ~ 오후 1:30\n오후 5:30 ~ 오후 7:30");
    	operatingHours.put("대학원 기숙사 식당", "오전 7:30 ~ 오전 9:30\n오전 11:30 ~ 오후 1:30\n오후 5:30 ~ 오후 7:30");
    	operatingHours.put("대학원 220동 식당", "오전 8:00 ~ 오후 5:00\n오전 11:00 ~ 오후 2:00\n오후 5:00 ~ 오후 7:00");
    	operatingHours.put("상아회관 식당", "미확인");
    	operatingHours.put("동원생활관 식당", "오전 11:00 ~ 오후 2:00 / 오후 5:00 ~ 오후 7:00");
    	operatingHours.put("감골 식당", "오전 11:00 ~ 오후 2:00 / 오후 5:00 ~ 오후 6:30\n\n<채식 뷔페>\n오전 11:30 ~ 오후 1:30");

    	/* 위치 */
    	locations.put("301동 식당", "301동");
    	locations.put("302동 식당", "302동");
    	locations.put("공대 간이 식당", "30-2동");
    	locations.put("학생회관 식당", "63동");
    	locations.put("자하연 식당", "109동");
    	locations.put("농생대 3식당", "75-1동 전망대");
    	locations.put("사범대 4식당", "76동");
    	locations.put("두레미담", "75-1동 5층");
    	locations.put("기숙사 919동 식당", "919동 1층");
    	locations.put("대학원 기숙사 식당", "901동 1층");
    	locations.put("대학원 220동 식당", "220동");
    	locations.put("상아회관 식당", "연건캠퍼스 19동");
    	locations.put("동원생활관 식당", "113동");
    	locations.put("감골 식당", "101동");
    	
    	/* Web Parsing 호출 */
    	loadingMenuTask.new Parsing().execute((Void)null);
    }
}