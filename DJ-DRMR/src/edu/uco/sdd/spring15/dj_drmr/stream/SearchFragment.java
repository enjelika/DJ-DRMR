package edu.uco.sdd.spring15.dj_drmr.stream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import edu.uco.sdd.spring15.dj_drmr.R;

public class SearchFragment extends DialogFragment {

	public interface SearchListener {
		public void onSearchDialogPositiveClick(DialogFragment dialog);

		public void onSearchDialogNegativeClick(DialogFragment dialog);
	}

	SearchListener mListener;

	
	public SearchFragment() {
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (SearchListener) activity; 
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
			+ " must implement SearchListener");
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		
		builder.setView(inflater.inflate(R.layout.search_info, null))
				.setPositiveButton(R.string.search,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onSearchDialogPositiveClick(SearchFragment.this);
							}
						})
				.setNegativeButton(R.string.record_cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onSearchDialogNegativeClick(SearchFragment.this);
							}
						});

		return builder.create();

	}
}
