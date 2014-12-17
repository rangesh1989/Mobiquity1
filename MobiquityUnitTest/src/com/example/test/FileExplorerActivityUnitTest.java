package com.example.test;

import android.provider.MediaStore;
import android.test.ActivityInstrumentationTestCase2;
import junit.framework.TestCase;

import com.example.FileexplorerActivity;
import com.example.MainActivity;
import com.robotium.solo.Solo;
public class FileExplorerActivityUnitTest extends ActivityInstrumentationTestCase2<FileexplorerActivity> {

	/*
	 * This is a sample jUnit test to check the functionality of Upload and 
	 * Display buttons before Login . 
	 */
	private Solo solo;
	public FileExplorerActivityUnitTest() {
		super(FileexplorerActivity.class);
		
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
					com.example.FileexplorerActivity.class, 15000));
	
	
	solo.clickOnView(solo.getView(com.example.R.id.share_with_fb));
	assertTrue("Share Test failed:" +
			"Section One:" +
			"FileUserActivity did not load correctly.",
			solo.waitForText("No Image selected"));
	
	solo.clickOnView(solo.getView(com.example.R.id.upload_image));
	assertTrue("Upload Image failed:" +
			"Section One:" +
			"FileUserActivity did not load correctly.",
			solo.waitForText("No Image Selected"));
	

}
}
