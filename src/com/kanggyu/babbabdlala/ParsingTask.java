package com.kanggyu.babbabdlala;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class ParsingTask extends AsyncTask<Void, Void, Boolean>
{
	@Override
	protected Boolean doInBackground(Void... params)
	{
		Document doc;
		try
		{
			doc = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu2.asp").get();
			Elements forthCafe = doc.select("table:has(img[alt=상품명]) tr:contains(4식당)");
			String forthCafeLunch = forthCafe.select("td:nth-child(5)").text();
			String forthCafeSupper = forthCafe.select("td:nth-child(7)").text();
			
			Log.i("HTML", forthCafeLunch + "점심");
			Log.i("HTML", forthCafeSupper + "저녁");
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
