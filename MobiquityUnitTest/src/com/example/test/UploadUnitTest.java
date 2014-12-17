package com.example.test;

import android.test.ActivityInstrumentationTestCase2;
import junit.framework.TestCase;

import com.example.MainActivity;
import com.robotium.solo.Solo;
public class UploadUnitTest extends ActivityInstrumentationTestCase2<MainActivity> {

	/*
	 * This is a sample jUnit test to check the functionality of Upload and 
	 * Display buttons before Login . 
	 */
	private Solo solo;
	public UploadUnitTest() {
		super(MainActivity.class);
		
	}

	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(),getActivity());
	}

	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}

	public void testOnClick() {
//		fail("Not yet implemented");
	}
	
public void testRun()
{
	assertTrue("UploadTest failed:" +
			"Section One:" +
			"MainActivity did not load correctly.",
			solo.waitForActivity(
					com.example.MainActivity.class, 15000));
	
	
	solo.clickOnView(solo.getView(com.example.R.id.upload));
	assertTrue("UploadTest failed:" +
			"Section One:" +
			"FileUserActivity did not load correctly.",
			solo.waitForText("Please Login To Dropbox"));
	
	solo.clickOnView(solo.getView(com.example.R.id.display));
	assertTrue("SubmitTest failed:" +
			"Section One:" +
			"FileUserActivity did not load correctly.",
			solo.waitForText("Please Login To Dropbox"));
	
}
}
