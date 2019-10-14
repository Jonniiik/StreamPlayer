package com.example.AudioStream.Fragments;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AudioStream.Adapter.OnActivityDataListener;
import com.example.AudioStream.Adapter.RvSongsAdapter;
import com.example.AudioStream.Common.Common;
import com.example.AudioStream.ModeliTunesApi.Result;
import com.example.AudioStream.R;
import com.example.AudioStream.Retrofit.RetrofitClient;
import com.example.AudioStream.Retrofit.IRetrofit;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements OnActivityDataListener {

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }


    public MusicFragment() {
        // Required empty public constructor
    }

    CompositeDisposable compositeDisposable;
    IRetrofit iRetrofit;
    RvSongsAdapter mAdapter;

    ImageView imageViewImageAlbumInPlayer;
    TextView textViewNameSongsInPlayer;
    TextView textViewNameGroupInPlayer;

    Button buttonLastTrack;
    Button buttonPlay;
    Button buttonNextTrack;
    SeekBar seekBarSounds;


    SearchView searchViewSongs;
    RecyclerView recyclerViewListSounds;

    MediaPlayer mediaPlayer;
    AudioManager audioManager;

    String soundURL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_music, container, false);

        recyclerViewListSounds = (RecyclerView) view.findViewById(R.id.recyclerViewListSounds);
        recyclerViewListSounds.setHasFixedSize(true);
        recyclerViewListSounds.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        searchViewSongs = (SearchView) view.findViewById(R.id.searchViewSongs);
        imageViewImageAlbumInPlayer = (ImageView) view.findViewById(R.id.imageViewImageAlbumInPlayer);
        textViewNameGroupInPlayer = (TextView) view.findViewById(R.id.textViewNameGroupInPlayer);
        textViewNameSongsInPlayer = (TextView) view.findViewById(R.id.textViewNameSongsInPlayer);
        buttonLastTrack = (Button) view.findViewById(R.id.buttonLastTrack);
        buttonPlay = (Button) view.findViewById(R.id.buttonPlay);
        buttonNextTrack = (Button) view.findViewById(R.id.buttonNextTrack);
        seekBarSounds = (SeekBar) view.findViewById(R.id.seekBarSounds);

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getRetrofit();
        iRetrofit = retrofit.create(IRetrofit.class);
        searchOnList();

        mediaPlayer = new MediaPlayer();
        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        buttonClick();
        return view;
    }

    private void buttonClick() {
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PlayerTask().execute(soundURL);
            }
        });
    }


    private void getInformationAdapterInto() {
        compositeDisposable.add(iRetrofit.getMusic(Common.Search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                               @Override
                               public void accept(Result result) throws Exception {
                                   getAdapter(result);

                               }
                           }, new Consumer<Throwable>() {
                               @Override
                               public void accept(Throwable throwable) throws Exception {
                                   Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                               }
                           }
                ));
    }

    private void getAdapter(Result result) {
        mAdapter = new RvSongsAdapter(getActivity(), result);
        recyclerViewListSounds.setAdapter(mAdapter);

    }

    private void searchOnList() {
        searchViewSongs.setIconified(true);
        searchViewSongs.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    Common.Search = newText.toLowerCase();
                    getInformationAdapterInto();
                }
                return false;
            }
        });
    }

    private void setInformation(String imageURL, String artistURL) throws IOException {
        if (imageURL != null)
            Picasso.get().load(imageURL).into(imageViewImageAlbumInPlayer);

        if (artistURL != null) {
            soundURL = artistURL;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
                buttonPlay.setBackgroundResource(R.drawable.ic_play);
                new PlayerTask().execute(soundURL);
            } else {
                new PlayerTask().execute(soundURL);
                buttonPlay.setBackgroundResource(R.drawable.ic_pause);
            }
        }

    }

    @Override
    public void onActivityDataListener(String imageURL, String artistURL) {
        try {
            setInformation(imageURL, artistURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class PlayerTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                buttonPlay.setBackgroundResource(R.drawable.ic_pause);
            } else {
                mediaPlayer.pause();
                buttonPlay.setBackgroundResource(R.drawable.ic_play);
            }
        }
    }
}
