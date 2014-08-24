package com.kanggyu.babbabdlala;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class GraduateParsingTask extends AsyncTask<Void, Void, Void>{
	private final static String TAG = "GraduateParsingTask";
	@Override
	protected Void doInBackground(Void... params) {
		try {
			HttpURLConnection con 
				= (HttpURLConnection) new URL("http://dorm.snu.ac.kr/dk_board/facility/food.php").openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			StringBuffer strbuf = new StringBuffer();
			for (String tmp = br.readLine() ; tmp != null ; tmp = br.readLine()) 
			{
				strbuf.append(tmp);
			}
			
			Document dietListDoc = Jsoup.parse(strbuf.toString());
			Elements dietTable = dietListDoc.select("tbody").get(0).children();
			
			int max=0;
			int min=0;
			
			for (int i = 0; i < 9; i++) 
			{
				Element oneLine = dietTable.get(i);
				if (i <= 1) 
				{
					// 아침 
					Elements cells = oneLine.children();
					
					if(oneLine.childNodeSize()==21)
					{
						max= 10;
						min= 3;
					}
					else if(oneLine.childNodeSize()==19)
					{
						max= 9;
						min= 2;
					}
					else if(oneLine.childNodeSize()==17)
					{
						max= 8;
						min= 1;
					}
					
					String conan = String.valueOf(oneLine.childNodeSize());
						
					for (int day = min; day < max; day++) 
					{
						// from sunday to sat
						Element oneCell = cells.get(day);
						Log.e(TAG, oneCell.select("span").text());
					}
					Log.e(TAG, conan);
					
				}
				else if (i <= 5) 
				{
					//lunch
					Elements cells = oneLine.children();
					
					if(oneLine.childNodeSize()==21)
					{
						max= 10;
						min= 3;
					}
					else if(oneLine.childNodeSize()==19)
					{
						max= 9;
						min= 2;
					}
					else if(oneLine.childNodeSize()==17)
					{
						max= 8;
						min= 1;
					}
					
					String conan = String.valueOf(oneLine.childNodeSize());
					
					for (int day = min; day < max; day++) 
					{
						// from sunday to sat
						Element oneCell = cells.get(day);
						Log.e(TAG, oneCell.select("span").text());
					}
					Log.e(TAG, conan);
				}
				else
				{
					//dinner
					Elements cells = oneLine.children();
					
					if(oneLine.childNodeSize()==21)
					{
						max= 10;
						min= 3;
					}
					else if(oneLine.childNodeSize()==19)
					{
						max= 9;
						min= 2;
					}
					else if(oneLine.childNodeSize()==17)
					{
						max= 8;
						min= 1;
					}
					
					String conan = String.valueOf(oneLine.childNodeSize());
					
					for (int day = min; day < max; day++) 
					{
						// from sunday to sat
						Element oneCell = cells.get(day);
						Log.e(TAG, oneCell.select("span").text());
					}
					Log.e(TAG, conan);
				}
			}//
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
