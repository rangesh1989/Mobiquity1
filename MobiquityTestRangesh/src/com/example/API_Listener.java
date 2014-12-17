package com.example;

public interface API_Listener 
{
	public void onSuccess(int requestnumber, Object obj);	
	public void onFail(String errormessage);	
}