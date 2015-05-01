package edu.uco.sdd.spring15.dj_drmr;

import java.io.IOException;
import com.soundcloud.api.ApiWrapper;
import com.soundcloud.api.Endpoints;
import com.soundcloud.api.Params;
import com.soundcloud.api.Request;
import com.soundcloud.api.Token;

import edu.uco.sdd.spring15.dj_drmr.stream.SoundcloudResource;

public class SongDAO {
	public static void upload(Token token, Song song, String title) throws IOException{
		 ApiWrapper wrapper = new ApiWrapper(SoundcloudResource.CLIENT_ID, SoundcloudResource.CLIENT_SECRET,  null,  token);
		 song.getSong().setReadable(true, false);
		 wrapper.post(Request.to(Endpoints.TRACKS)
         .add(Params.Track.TITLE, title)
         .add(Params.Track.TAG_LIST, song.getTags())
         .withFile(Params.Track.ASSET_DATA, song.getSong()));
	}
}