package com.rosch.braineff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class InterpreterActivity extends AppCompatActivity
{
	private Toolbar toolbar;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.interpeter_activity);
		
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);		
		
		Bundle arguments = getIntent().getExtras();
		
		InterpreterFragment interpreterFragment = new InterpreterFragment();
		
		getFragmentManager()
			.beginTransaction()
			.add(interpreterFragment, "interpreter_fragment")
			.commit();
		
		interpreterFragment.compileFile(arguments.getString("file_contents"));
			
		ConsoleFragment fragment = new ConsoleFragment();
		fragment.setArguments(arguments);
		
		getFragmentManager()
			.beginTransaction()
			.replace(R.id.fragment_container, fragment, "console_fragment")
			.commit();
	}
	
	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
	}
}
