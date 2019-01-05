/*
 * Created by Ofek Pintok on 1/5/19 7:40 PM
 * Copyright (c) 2019 . All rights reserved
 * Last modified 1/5/19 4:40 PM
 */

package com.ofek.movieapp.models;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class VideosListResponse {

	@SerializedName("id")
	private int id;

	@SerializedName("results")
	private List<VideoResponse> results;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setResults(List<VideoResponse> results){
		this.results = results;
	}

	public List<VideoResponse> getResults(){
		return results;
	}
}