package com.rosch.braineff;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener
{
	private Toolbar mainToolbar;
	private DrawerLayout drawerLayout;
	private NavigationView navigationView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_activity);

		setupToolbar();
		setupNavigationDrawer();
		setupNavigationView();

		showEditorFragment();
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

	private void setupNavigationView()
	{
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	private void showEditorFragment()
	{
		if (getFragmentManager().findFragmentByTag("editor_fragment") == null)
		{
			FragmentTransaction transaction = getFragmentManager().beginTransaction();

			transaction.replace(R.id.fragment_container, new EditorFragment(), "editor_fragment");
			transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
			transaction.commit();
		}
	}

	@Override
	public boolean onNavigationItemSelected(MenuItem menuItem)
	{
		// TODO: refactor me, because this is just silly.
		EditorFragment editorFragment =
			(EditorFragment) getFragmentManager().findFragmentByTag("editor_fragment");

		switch (menuItem.getItemId())
		{
			case R.id.compile:
				editorFragment.onEditorCompile();
				return true;

			case R.id.open_file:
				return editorFragment.onEditorLoad();

			case R.id.save_file:
				return editorFragment.onEditorSave();
		}

		return false;
	}
}
