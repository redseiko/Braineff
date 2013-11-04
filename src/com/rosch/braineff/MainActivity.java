package com.rosch.braineff;

import java.io.File;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		// Ensure the shared storage folder is created.
		File directory = new File(Environment.getExternalStorageDirectory() + "//Braineff//");
		directory.mkdir();
		
		setContentView(R.layout.main_activity);
		
		if (getFragmentManager().findFragmentByTag("editor_fragment") == null)
		{
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
			transaction.replace(R.id.fragment_container, new EditorFragment(), "editor_fragment");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (super.onCreateOptionsMenu(menu) == false)
			return false;
		
		getMenuInflater().inflate(R.menu.main_activity, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{			
		return super.onOptionsItemSelected(item);
	}
}
