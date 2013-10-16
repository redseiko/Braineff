package com.rosch.braineff;

import android.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class EditorFragment extends Fragment implements View.OnClickListener,
	SaveFileFragment.OnSaveFileSuccessListener, LoadFileFragment.OnLoadFileSuccessListener
{
	public interface EditorFragmentHandler
	{
		public boolean onCompileProgram(Bundle arguments);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.editor_fragment, container, false);
		
		String filename = "";
		String contents = "";		
		
		((TextView) view.findViewById(R.id.file_filename)).setText(filename);
		((EditText) view.findViewById(R.id.file_contents)).setText(contents);
		
		// Easier to set each Buttons' listener than to setup a recursive loop.
		view.findViewById(R.id.kb_inc_ptr).setOnClickListener(this);
		view.findViewById(R.id.kb_dec_ptr).setOnClickListener(this);
		view.findViewById(R.id.kb_inc_data).setOnClickListener(this);
		view.findViewById(R.id.kb_dec_data).setOnClickListener(this);
		view.findViewById(R.id.kb_output_data).setOnClickListener(this);
		view.findViewById(R.id.kb_input_data).setOnClickListener(this);
		view.findViewById(R.id.kb_jump_forward).setOnClickListener(this);
		view.findViewById(R.id.kb_jump_backward).setOnClickListener(this);
		view.findViewById(R.id.kb_tab).setOnClickListener(this);
		view.findViewById(R.id.kb_backspace).setOnClickListener(this);
		view.findViewById(R.id.kb_space).setOnClickListener(this);
		view.findViewById(R.id.kb_enter).setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.editor_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		EditorFragmentHandler listener = (EditorFragmentHandler) getActivity();
		
		if (listener == null)
			return super.onOptionsItemSelected(item);
		
		switch (item.getItemId())
		{
		case R.id.menu_editor_compile:
			onEditorCompile();
			break;
			
		case R.id.menu_editor_save:
			onEditorSave();
			break;
			
		case R.id.menu_editor_load:
			onEditorLoad();
			break;
		}
		
		return true;
	}
		
	private boolean onEditorCompile()
	{
		EditorFragmentHandler handler = (EditorFragmentHandler) getActivity();		
		EditText sourceView = (EditText) getView().findViewById(R.id.file_contents);
		
		Bundle arguments = new Bundle();		
		arguments.putString("file_contents", sourceView.getText().toString());
		
		return handler.onCompileProgram(arguments);
		
	}
	
	private boolean onEditorSave()
	{		
		TextView filenameTextView = (TextView) getView().findViewById(R.id.file_filename);
		EditText contentsEditText = (EditText) getView().findViewById(R.id.file_contents);
		
		Bundle arguments = new Bundle();
		
		arguments.putString("file_filename", filenameTextView.getText().toString());
		arguments.putString("file_contents", contentsEditText.getText().toString());
		
		SaveFileFragment fragment = new SaveFileFragment();
		
		fragment.setArguments(arguments);
		fragment.setTargetFragment(this, 0);		
		fragment.show(getFragmentManager(), "save_file_fragment");
		
		return true;
	}
	
	private boolean onEditorLoad()
	{
		LoadFileFragment fragment = new LoadFileFragment();
		
		fragment.setTargetFragment(this,  0);
		fragment.show(getFragmentManager(), "load_file_fragment");
		
		return true;
	}
	
	public void setFilenameText(String text)
	{
		TextView filenameTextView = (TextView) getView().findViewById(R.id.file_filename);
		filenameTextView.setText(text);
	}

	@Override
	public void onClick(View view)
	{		
		EditText editorContents = (EditText) getView().findViewById(R.id.file_contents);
		String text = "";
		
		switch (view.getId())
		{
		case R.id.kb_inc_ptr:		text = ">";	break;			
		case R.id.kb_dec_ptr:		text = "<"; break;		
		case R.id.kb_inc_data:		text = "+"; break;			
		case R.id.kb_dec_data:		text = "-"; break;			
		case R.id.kb_output_data:	text = "."; break;			
		case R.id.kb_input_data:	text = ",";	break;			
		case R.id.kb_jump_forward:	text = "["; break;						
		case R.id.kb_jump_backward:	text = "]"; break;
		
		case R.id.kb_space:			text = "\u0020"; break;		
		case R.id.kb_tab:			text = "\u0009"; break;
		
		case R.id.kb_backspace:
			editorContents.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
			return;
			
		case R.id.kb_enter:
			editorContents.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
			return;
		
		default:
			return;
		}		
								
		if (text.isEmpty() == false)
			editorContents.getText().insert(editorContents.getSelectionStart(), text);
	}

	@Override
	public void onSaveFileSuccess(Bundle arguments)
	{	
		TextView filenameTextView = (TextView) getView().findViewById(R.id.file_filename);
		filenameTextView.setText(arguments.getString("file_filename"));
	}

	@Override
	public void onLoadFileSuccess(Bundle arguments)
	{
		TextView filenameTextView = (TextView) getView().findViewById(R.id.file_filename);
		filenameTextView.setText(arguments.getString("file_filename"));
		
		EditText contentsEditText = (EditText) getView().findViewById(R.id.file_contents);
		contentsEditText.setText(arguments.getString("file_contents"));
	}
}
