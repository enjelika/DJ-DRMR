package edu.uco.sdd.spring15.dj_drmr.record;

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

public class RecordDialogFragment extends DialogFragment {

	public interface RecordDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	RecordDialogListener mListener;

	
	public RecordDialogFragment() {
	}
	
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (RecordDialogListener) activity; 
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
			+ " must implement RecordDialogListener");
		}
	}

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();

		
		builder.setView(inflater.inflate(R.layout.record_info, null))
				.setPositiveButton(R.string.record_record,
						new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onDialogPositiveClick(RecordDialogFragment.this);
							}
						})
				.setNegativeButton(R.string.record_cancel,
						new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mListener.onDialogNegativeClick(RecordDialogFragment.this);
							}
						});

		return builder.create();

	}
}
