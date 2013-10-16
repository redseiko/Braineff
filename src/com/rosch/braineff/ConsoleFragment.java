package com.rosch.braineff;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConsoleFragment extends Fragment implements InterpreterFragment.InputOutputListener
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.console_fragment, container, false);
		
		view.findViewById(R.id.console_btn_run).setOnClickListener(new OnClickListener()
		{			
			@Override
			public void onClick(View view)
			{
				runProgram();
			}
		});
		
		return view;
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
