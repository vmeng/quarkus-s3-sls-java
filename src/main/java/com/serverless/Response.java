package com.serverless;

import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
@JsonDeserialize(builder = Response.class)
public class Response {

	
	private String message;
	private Map<String, Object> input;
	
	
	public Response(String message, Map<String, Object> input) {
		this.message = message;
		this.input = input;
	}

	
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, Object> getInput() {
		return this.input;
	}
	public void setInput(Map<String, Object> input) {
		this.input = input;
	}
	@Override
	public String toString() {
		return "Response [toString()=" + this.message + ", input: "+ this.input.toString()+"]";
	}
}
