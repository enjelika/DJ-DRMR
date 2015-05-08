package edu.uco.sdd.spring15.dj_drmr;

import java.util.ArrayList;

import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class TrackResultsFragment extends DialogFragment{
	
	private ArrayList<SoundcloudResource> trackList;

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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		
		trackList = (ArrayList) bundle.getParcelableArrayList("results");
		String[] titles = new String[trackList.size()];
		
		// get the track names for the track list
		for (int i = 0; i < trackList.size(); i++) {
			titles[i] = trackList.get(i).getTitle();
		}
		
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(titles, new DialogInterface.OnClickListener() {
                   @Override
				public void onClick(DialogInterface dialog, int choice) {
                	   listener.onPickTrackClick(choice, TrackResultsFragment.this);
               }
        });
        return builder.create();
    }
}
