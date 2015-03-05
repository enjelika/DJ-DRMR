package edu.uco.sdd.spring15.dj_drmr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class TrackResultsFragment extends DialogFragment{
	
	private String[] trackList;

	public interface TrackResultsListener {
		public void onPickTrackClick(int trackIndex, DialogFragment dialog);
	}
	
	TrackResultsListener listener;
	
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		try {
			listener = (TrackResultsListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString()
                    + " must implement TrackResultsListner");
		}
	}
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		trackList = bundle.getStringArray("results");
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(trackList, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int choice) {
                	   listener.onPickTrackClick(choice, TrackResultsFragment.this);
               }
        });
        return builder.create();
    }
}
