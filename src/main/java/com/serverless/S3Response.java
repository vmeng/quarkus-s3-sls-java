package com.serverless;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = S3Response.class)
public class S3Response {
	private String message;
	
	
	public S3Response(String message) {
		this.message = message;
	}


	public String getMessage() {
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "S3Response [toString()=" + super.toString() + "]";
	}
}
