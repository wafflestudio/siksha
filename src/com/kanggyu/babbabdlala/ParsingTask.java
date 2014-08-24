package com.kanggyu.babbabdlala;

import java.io.IOException;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.util.Log;

public class ParsingTask extends AsyncTask<Void, Void, Boolean> {
	@Override
	protected Boolean doInBackground(Void... params) {
		HashMap<String,String[]> menu = new HashMap<String,String[]>();

				
		return true;
	}
	protected String[] parsing(String cafeName){
		Document doc;
		try {
			int i=0;
			String[] lunch = new String[5];
			String[] supper = new String[5];
			
			doc = Jsoup.connect("http://www.snuco.com/html/restaurant/restaurant_menu2.asp?date=2014-08-28").get();
			Elements cafeteria = doc.select("table:has(img[alt=상품명]) tr:contains("+cafeName+")");
			String lunchFullMenu = cafeteria.select("td:nth-child(5)").text();
			String supperFullMenu = cafeteria.select("td:nth-child(7)").text();
			//lunch menu array 
			for (int n=0; n<lunchFullMenu.length(); n++){
				if (lunchFullMenu.charAt(n)=='/'){
					lunch[i++] = checkPrice(lunchFullMenu.substring(0, n));
					lunchFullMenu = lunchFullMenu.substring(n+1);
				}
			}
			lunch[i] = checkPrice(lunchFullMenu);
			//dinner menu array
			for (int n=0; n<supperFullMenu.length(); n++){
				if (supperFullMenu.charAt(n)=='/'){
					supper[i++] = checkPrice(supperFullMenu.substring(0, n));
					supperFullMenu = supperFullMenu.substring(n+1);
				}
			}
			supper[i] = checkPrice(supperFullMenu);
			//return lunch menu and dinner menu in hashmap
			return lunch;

		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] result = new String[2];
		return result;
	}
	protected String checkPrice(String menu){
		if (menu.charAt(0)=='ⓐ'){
			return menu.substring(1)+"1700";
		}
		else if (menu.charAt(0)=='ⓑ'){
			return menu.substring(1)+"2000";
		}
		else if (menu.charAt(0)=='ⓒ'){
			return menu.substring(1)+"2500";
		}
		else if (menu.charAt(0)=='ⓓ'){
			return menu.substring(1)+"3000";
		}
		else if (menu.charAt(0)=='ⓔ'){
			return menu.substring(1)+"3500";
		}
		else if (menu.charAt(0)=='ⓕ'){
			return menu.substring(1)+"4000";
		}
		else if (menu.charAt(0)=='ⓖ'){
			return menu.substring(1)+"4500";
		}
		else {
			return menu.substring(1)+"0000";
		}
	}
}
