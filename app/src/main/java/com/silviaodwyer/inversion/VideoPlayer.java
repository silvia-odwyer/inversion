package com.silviaodwyer.inversion;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daasuu.epf.EPlayerView;
import com.daasuu.epf.filter.GlFilter;
import com.daasuu.epf.filter.GlSepiaFilter;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoPlayer {
  private EPlayerView ePlayerView;
  private SimpleExoPlayer player;
  private DataSource.Factory dataSourceFactory;
  private Context context;
  private String videoPath;
  private Activity activity;

  public VideoPlayer(Context ctx, String videoPath, Activity activity) {
    this.context = ctx;
    this.videoPath = videoPath;
    this.activity = activity;

      if (player != null) {
        player = null;
        ePlayerView = null;
        dataSourceFactory = null;
      }
      this.setupPlayer();

  }

  public void setupPlayer() {
    player = ExoPlayerFactory.newSimpleInstance(context);
    ePlayerView = new EPlayerView(context);

    ePlayerView.setSimpleExoPlayer(player);
    ePlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context,"Inversion"));

    MediaSource videoSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
      .createMediaSource(Uri.parse(videoPath));

    ProgressBar progressBar = (ProgressBar) this.activity.findViewById(R.id.progress_bar);

    player.addListener(new Player.EventListener() {

      @Override
      public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {}

      @Override
      public void onLoadingChanged(boolean isLoading) {}

      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING){
          progressBar.setVisibility(View.VISIBLE);
        } else {
          progressBar.setVisibility(View.INVISIBLE);
        }
      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {}


      @Override
      public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {}
    });

    player.prepare(videoSource);
    player.setPlayWhenReady(true);

    ePlayerView.onResume();
  }

  /**
   * Get the playerview associated with this video player.
   *
   * @return    PlayerView for this video player
   */
  public EPlayerView getPlayerView() {
    return this.ePlayerView;
  }

  /**
   * Get the exoplayer instance associated with this video player.
   *
   * @return    SimpleExoPlayer instance.
   */
  public SimpleExoPlayer getSimpleExoPlayer() {
    return this.player;
  }

  /**
  * Filter the video by applying the filter passed.
   **/
  public void filterVideo(GlFilter filter) {
    ePlayerView.setGlFilter(filter);
  }

}
