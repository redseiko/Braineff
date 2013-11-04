package com.rosch.braineff;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConsoleFragment extends Fragment implements InterpreterFragment.InputOutputListener
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.console_fragment, container, false);
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.console_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.menu_console_run)
		{
			runProgram();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	public void runProgram()
	{
		InterpreterFragment fragment = (InterpreterFragment) getFragmentManager().findFragmentByTag("interpreter_fragment");
		
		if (fragment == null)
			return;
		
		fragment.runProgram(this);
	}

	@Override
	public void onInterpreterOutput(String output)
	{
		TextView consoleOutput = (TextView) getView().findViewById(R.id.console_output);
		consoleOutput.append(output);	
	}

	@Override
	public String onInterpreterInput()
	{
		return null;
	}
}
