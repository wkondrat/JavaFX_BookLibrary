package com.capgemini.javafx.dataprovider.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.capgemini.javafx.dataprovider.DataProvider;
import com.capgemini.javafx.dataprovider.data.BookStatusVO;
import com.capgemini.javafx.dataprovider.data.BookVO;

/**
 * Provides data. Data is stored locally in this object. Additionally a call
 * delay is simulated.
 *
 * @author WKONDRAT
 *
 */
public class DataProviderImpl implements DataProvider {

	private static final Logger LOG = Logger.getLogger(DataProviderImpl.class);

	/**
	 * Delay (in ms) for method calls.
	 */
//	private static final long CALL_DELAY = 3000;

	private Collection<BookVO> books = new ArrayList<>();

	public DataProviderImpl() {
//		books.add(new BookVO(1L,"Lokomotywa","Tuwim", BookStatusVO.FREE));
//		books.add(new BookVO(2L,"Lalka","Prus", BookStatusVO.FREE));
//		books.add(new BookVO(3L,"First Book","Tuwim", BookStatusVO.FREE));
//		books.add(new BookVO(4L,"Second Book","Krakowski", BookStatusVO.FREE));
//		books.add(new BookVO(5L,"Third Book","Kowalski", BookStatusVO.LOAN));
//		books.add(new BookVO(6L,"Lokomotywa","Tuwim", BookStatusVO.LOAN));
//		books.add(new BookVO(7L,"Szklanka","Czarna", BookStatusVO.MISSING));
	}

	@Override
	public Collection<BookVO> findBooks(String title, BookStatusVO bookStatus) {
		LOG.debug("Entering findBooks()");
		
		try {
			books.clear();
			sendGet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		/*
		 * Simulate a call delay.
		 */
//		try {
//			Thread.sleep(CALL_DELAY);
//			
//		} catch (InterruptedException e) {
//			throw new RuntimeException("Thread interrupted", e);
//		}

		Collection<BookVO> result = books.stream().filter(p -> //
		((title == null || title.isEmpty()) || (title != null && !title.isEmpty() && p.getTitle().contains(title))) //
				&& //
				((bookStatus == null) || (bookStatus != null && p.getBookStatus() == bookStatus)) //
		).collect(Collectors.toList());
		
		
		
		LOG.debug("Leaving findBooks()");
		return result;
	}
	
	private void sendGet() throws Exception {

		String url = "http://localhost:8080/webstore/rest/books";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		LOG.debug("\nSending 'GET' request to URL : " + url);
		LOG.debug("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
        JSONArray ja = new JSONArray(response.toString());

        int n = ja.length();
        for (int i = 0; i < n; i++) {
            JSONObject jo = ja.getJSONObject(i);

            long id = jo.getLong("id");
            String title = jo.getString("title");
            String authors = jo.getString("authors");
            BookStatusVO status = BookStatusVO.valueOf(jo.getString("status"));

            BookVO c = new BookVO(id, title, authors, status);
            books.add(c);
        }
        
		LOG.debug(response.toString());

	}
	
	
}
