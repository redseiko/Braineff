package com.rosch.braineff;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class LoadFileFragment extends DialogFragment implements OnClickListener
{
	public interface OnLoadFileSuccessListener
	{
		public void onLoadFileSuccess(Bundle arguments);
	}
	
	private String filenames[] = null;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setTitle(R.string.load_file_fragment_title);
				
		File file = new File(Environment.getExternalStorageDirectory() + "//Braineff//");
		filenames = file.list();
		
		if (filenames != null && filenames.length > 0)
		{
			builder.setItems(file.list(), this);
			builder.setNegativeButton(android.R.string.cancel, null);
		}
		else
		{
			builder.setMessage(R.string.load_file_msg_no_files);
			builder.setPositiveButton(android.R.string.ok, null);
		}
		
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		String filename = filenames[which];
		String contents = "";
		
		File file = new File(Environment.getExternalStorageDirectory() + "//Braineff//", filename);	

		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));			
			String line = reader.readLine();
			
			while (line != null)
			{
				contents += line;
				line = reader.readLine();
			}
			
			reader.close();
		}
		catch (IOException exception)
		{
			Toast.makeText(getActivity(), getString(R.string.load_file_toast_failure, filename), Toast.LENGTH_SHORT).show();
			return;
		}
		
		Toast.makeText(getActivity(), getString(R.string.load_file_toast_success, filename), Toast.LENGTH_SHORT).show();
		
		Bundle arguments = new Bundle();
		
		arguments.putString("file_filename", filename);
		arguments.putString("file_contents", contents);
		
		OnLoadFileSuccessListener listener = (OnLoadFileSuccessListener) getTargetFragment();
		listener.onLoadFileSuccess(arguments);
	}
}
