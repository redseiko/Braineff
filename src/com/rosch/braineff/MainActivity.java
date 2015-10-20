package com.rosch.braineff;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity
{
	private Toolbar mainToolbar;
    private DrawerLayout drawerLayout;	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);
		
		setupToolbar();
		setupNavigationDrawer();
		
		if (getFragmentManager().findFragmentByTag("editor_fragment") == null)
		{
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
		
			transaction.replace(R.id.fragment_container, new EditorFragment(), "editor_fragment");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
	}
	
    private void setupToolbar()
    {
    	mainToolbar = (Toolbar) findViewById(R.id.toolbar);
    	setSupportActionBar(mainToolbar);

    	getSupportActionBar().setHomeButtonEnabled(true);
    	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    
    private void setupNavigationDrawer()
    {
    	drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    	ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
    		this, drawerLayout, mainToolbar, android.R.string.yes, android.R.string.no);

    	drawerLayout.setDrawerListener(drawerToggle);
    	drawerToggle.syncState();
    }
}
