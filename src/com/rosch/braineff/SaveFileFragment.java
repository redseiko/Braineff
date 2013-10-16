package com.rosch.braineff;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnShowListener;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.widget.EditText;
import android.widget.Toast;

public class SaveFileFragment extends DialogFragment implements OnShowListener, OnClickListener
{
	public interface OnSaveFileSuccessListener
	{
		public void onSaveFileSuccess(Bundle arguments);		
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new Builder(getActivity());
		
		builder.setTitle(R.string.save_file_fragment_title);
		builder.setView(getActivity().getLayoutInflater().inflate(R.layout.save_file_fragment, null));
		builder.setNegativeButton(android.R.string.cancel, null);	
		builder.setPositiveButton(android.R.string.ok, this);
		
		AlertDialog dialog = builder.create();
		dialog.setOnShowListener(this);
		
		return dialog;
	}
	
	@Override
	public void onShow(DialogInterface dialog)
	{
		Bundle arguments = getArguments();
		String filename = arguments.getString("file_filename");

		EditText filenameEditText = (EditText) ((AlertDialog) dialog).findViewById(R.id.save_file_filename);
		filenameEditText.setText(filename);	
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		EditText filenameEditText = (EditText) ((AlertDialog) dialog).findViewById(R.id.save_file_filename);
		String filename = filenameEditText.getText().toString();
		
		if (filename.isEmpty() == true)
			filename = getString(R.string.save_file_filename_hint);
		
		if (filename.endsWith(".bf") == false)
			filename += ".bf";
		
		Bundle arguments = getArguments();
		arguments.putString("file_filename", filename);
		
		File file = new File(Environment.getExternalStorageDirectory() + "//Braineff//", filename);	
		
		try
		{
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
			
			writer.write(arguments.getString("file_contents"));
			writer.close();
		}
		catch (IOException exception)
		{
			Toast.makeText(getActivity(), getString(R.string.save_file_toast_failure, filename), Toast.LENGTH_SHORT).show();			
			
			return;
		}
				
		Toast.makeText(getActivity(), getString(R.string.save_file_toast_success, filename), Toast.LENGTH_SHORT).show();

		// Work-around as referenced here: code.google.com/p/android/issues/detail?id=38282
		MediaScannerConnection.scanFile(getActivity(), new String[] { file.getAbsolutePath() }, null, null);
		
		OnSaveFileSuccessListener listener = (OnSaveFileSuccessListener) getTargetFragment();		
		listener.onSaveFileSuccess(arguments);
	}
}
