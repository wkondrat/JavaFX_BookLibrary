package com.capgemini.javafx.dataprovider.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.capgemini.javafx.dataprovider.DataProvider;
import com.capgemini.javafx.dataprovider.data.BookStatusVO;
import com.capgemini.javafx.dataprovider.data.BookVO;
import com.google.gson.Gson;

public class DataProviderImpl implements DataProvider {

	private static final Logger LOG = Logger.getLogger(DataProviderImpl.class);

	private Collection<BookVO> books = new ArrayList<>();

	public DataProviderImpl() {

	}
	
	@Override
	public Collection<BookVO> findBooksByParameters(String title, String authors, BookStatusVO bookStatus)
			throws Exception {
		LOG.debug("Entering findBooks()");

		try {
			books.clear();
			sendGet2(title, authors);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		Collection<BookVO> result = books.stream().filter(p -> //
		((title == null || title.isEmpty()) || (title != null && !title.isEmpty() && p.getTitle().contains(title))) //
				&& //
				((bookStatus == null) || (bookStatus != null && p.getBookStatus() == bookStatus)) //
		).collect(Collectors.toList());

		LOG.debug("Leaving findBooks()");
		return result;
	}
	
	private void sendGet2(String title, String authors) throws Exception {

		String url = "http://localhost:8080/webstore/rest/search?title="+ title+"&authors=" +authors;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		LOG.debug("\nSending 'GET' request to URL : " + url);
		LOG.debug("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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

			long id2 = jo.getLong("id");
			String title2 = jo.getString("title");
			String authors2 = jo.getString("authors");
			BookStatusVO status2 = BookStatusVO.valueOf(jo.getString("status"));

			BookVO c = new BookVO(id2, title2, authors2, status2);
			books.add(c);
		}

		LOG.debug(response.toString());

	}

}
