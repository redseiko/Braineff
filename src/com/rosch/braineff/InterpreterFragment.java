package com.rosch.braineff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Fragment;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class InterpreterFragment extends Fragment
{
	public interface InputOutputListener
	{
		public void onInterpreterOutput(String output);
		public String onInterpreterInput();
	}
	
	public enum InterpreterCommand
	{
		NOP,
		INC_PTR, DEC_PTR,
		INC_BYTE, DEC_BYTE,
		OUTPUT_BYTE, INPUT_BYTE,
		LOOP_BEGIN, LOOP_END;	
	}
	
	private ExecuteCommandsAsyncTask mExecuteTask = null;
	
	private InputOutputListener mListener = null;
	
	private List<InterpreterCommand> mCommands = new ArrayList<InterpreterCommand>();
	private int[] mCells = new int [10000];
	
	private int mCommandIndex = 0;
	private int mCellIndex = 0;
	
	private static int mExecuteCounter = 0;
	
	public class CompileFileAsyncTask extends AsyncTask<String, Integer, Boolean>
	{			
		private List<Integer> mLoopBegins = null;
		private List<Integer> mLoopEnds = null;		
		
		private String mErrorMessage = "";
		
		@Override
		protected void onPreExecute()
		{
			mCommands.clear();
			Arrays.fill(mCells, 0);			
			
			mCommandIndex = 0;
			mCellIndex = 0;
			
			mLoopBegins = new ArrayList<Integer>();
			mLoopEnds = new ArrayList<Integer>();
			
			Toast.makeText(getActivity(), "Compiling...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected Boolean doInBackground(String... params)
		{
			String contents = params[0];			
			
			for (int i = 0; i < contents.length(); i++)
			{
				InterpreterCommand command = InterpreterCommand.NOP;
				char symbol = contents.charAt(i);
				
				switch (symbol)
				{
				case '>':	command = InterpreterCommand.INC_PTR;	break;
				case '<':	command = InterpreterCommand.DEC_PTR;	break;
				case '+':	command = InterpreterCommand.INC_BYTE;	break;
				case '-':	command = InterpreterCommand.DEC_BYTE;	break;
				
				case '.':	command = InterpreterCommand.OUTPUT_BYTE;	break;
				case ',':	command = InterpreterCommand.INPUT_BYTE;	break;
				
				case '[':	command = InterpreterCommand.LOOP_BEGIN;	break;
				case ']':	command = InterpreterCommand.LOOP_END;		break;
				
				default:	break;
				}
				
				if (command == InterpreterCommand.NOP)
					continue;
				
				if (command == InterpreterCommand.LOOP_BEGIN)
					mLoopBegins.add(mCommands.size());
				
				if (command == InterpreterCommand.LOOP_END)
				{
					if (mLoopBegins.size() <= mLoopEnds.size())
					{
						mErrorMessage = "] at " + i + " missing matching [";
						return false;
					}
					
					mLoopEnds.add(mCommands.size());
				}				
				
				mCommands.add(command);
			}	
			
			if (mLoopBegins.size() > mLoopEnds.size())
			{
				mErrorMessage = "[ at " + mLoopBegins.get(mLoopBegins.size() - 1) + " missing matching ]";
				return false;
			}
			
			return true;
		}		
		
		@Override
		protected void onPostExecute(Boolean result)
		{
			Toast.makeText(getActivity(), mErrorMessage.isEmpty() ? "Compiled successfully. " + mCommands.size() + " commands found." : mErrorMessage, Toast.LENGTH_SHORT).show();				
		}
	}
	
	public class ExecuteCommandsAsyncTask extends AsyncTask<Integer, Integer, Integer>
	{		
		private int counter = mExecuteCounter++;
		
		@Override
		protected Integer doInBackground(Integer... params)
		{					
			Log.d("interpreter_fragment", "ExecuteCommandAsyncTask " + counter + " started.");
			boolean isActive = true;
			int numCommands = mCommands.size();
			
			mCommandIndex = 0;
			mCellIndex = 0;			
			
			while (isActive == true)
			{
				if ((isCancelled() == true) || (mCommandIndex >= numCommands))
					break;
				
				InterpreterCommand command = mCommands.get(mCommandIndex);				
				
				switch (command)
				{
				case INC_PTR:
					{
						mCellIndex++;
						
						if (mCellIndex >= mCells.length)
							isActive = false;						
					}
					break;
					
				case DEC_PTR:
					{
						mCellIndex--;
						
						if (mCellIndex < 0)
							isActive = false;
					}
					break;
					
				case INC_BYTE:
					{
						mCells[mCellIndex]++;
						
						if (mCells[mCellIndex] > 255)
							mCells[mCellIndex] = 0;
					}
					break;
					
				case DEC_BYTE:
					{
						mCells[mCellIndex]--;
						
						if (mCells[mCellIndex] < 0)
							mCells[mCellIndex] = 255;
					}
					break;
					
				case OUTPUT_BYTE:
					{
						publishProgress(mCells[mCellIndex]);	
					}
					break;
					
				case INPUT_BYTE:
					{
						
					}
					break;
					
				case LOOP_BEGIN:
					{
						if (mCells[mCellIndex] > 0)
							break;
						
						mCommandIndex++;
						
						while (mCommandIndex < numCommands)
						{
							if (mCommands.get(mCommandIndex) == InterpreterCommand.LOOP_END)
								break;
							
							mCommandIndex++;
						}
						
						if (mCommandIndex >= numCommands)
							isActive = false;
					}
					break;
					
				case LOOP_END:
					{
						if (mCells[mCellIndex] <= 0)
							break;
						
						mCommandIndex--;
						
						while (mCommandIndex >= 0)
						{
							if (mCommands.get(mCommandIndex) == InterpreterCommand.LOOP_BEGIN)
								break;
							
							mCommandIndex--;
						}
						
						if (mCommandIndex < 0)
							isActive = false;
					}
					break;
					
				default:
					break;										
				}
				
				mCommandIndex++;
			}		
			
			mExecuteTask = null;
			
			Log.d("interpreter_fragment", "ExecuteCommandAsyncTask " + counter + " stopped.");
			
			return 0;
		}	
		
		@Override
		public void onProgressUpdate(Integer... values)
		{
			if (mListener != null)
			{
				String output = String.valueOf((char) values[0].intValue());
				mListener.onInterpreterOutput(output);
			}
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		if (mExecuteTask != null)
			mExecuteTask.cancel(true);
	}
	
	public void compileFile(String contents)
	{
		CompileFileAsyncTask task = new CompileFileAsyncTask();
		task.execute(contents);
	}
	
	public void runProgram(InputOutputListener listener)
	{
		Arrays.fill(mCells, 0);			
			
		if (mExecuteTask != null)
			mExecuteTask.cancel(true);
		
		mListener = listener;
		
		mExecuteTask = new ExecuteCommandsAsyncTask();
		mExecuteTask.execute();
	}
}
