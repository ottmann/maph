package com.example.elisabeth.depressionsapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.icu.text.SymbolTable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elisabeth.depressionsapp.services.AudioFileManager;

public class MusicActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        AudioFileManager am = new AudioFileManager();
        List<HashMap<String,String>> fileList = am.getAllAudioFromDevice(getApplicationContext());
        System.out.println("List");
        System.out.println("List");
        System.out.println("List");
        System.out.println("Items: " + fileList);
    }


/*    // Songs list
    public ArrayList songsList = new ArrayList();

    Button display;

    @Override
    public void onCreate(Bundle savedInstanceState)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //display = (Button) findViewById(R.id.Display);

        final ArrayList songsListData = new ArrayList();

        AudioFileManager plm = new AudioFileManager();
        this.songsList = plm.getPlayList();

        // Adding menuItems to ListView
        final ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.single_item_view, new String[] { "songTitle" }, new int[] {});

        // looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            Object song = songsList.get(i);

            // adding HashList to ArrayList
            songsListData.add(song);
        }

        setListAdapter(adapter);


    }*/
/* } */


/*
package com.example.elisabeth.depressionsapp;

import android.app.Activity;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MusicActivity extends AppCompatActivity {

    private String[] mAudioPath;
    private MediaPlayer mMediaPlayer;
    private String[] mMusicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        //use setVolumeControlStream to allow the user to change
        //the volume using the hardware controls
        //setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //we now instantiate the player using our music file;
        //note that since it is a resource in the "raw" directory,
        //we don't need the file's extension;
        //our file here is in fact called "soundfile.mp3";
        //player = MediaPlayer.create(this, R.raw.joker);
        //we start playing the file!
        //player.start();

        mMediaPlayer = new MediaPlayer();

        ListView mListView = (ListView) findViewById(R.id.listView);

        mMusicList = getAudioList();

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mMusicList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                try {
                    playSong(mAudioPath[arg2]);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String[] getAudioList() {
        final Cursor mCursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA }, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();

        String[] songs = new String[count];
        String[] mAudioPath = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                mAudioPath[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                i++;
            } while (mCursor.moveToNext());
        }

        mCursor.close();

        return songs;
    }

    private void playSong(String path) throws IllegalArgumentException,
            IllegalStateException, IOException {

        Log.d("ringtone", "playSong :: " + path);

        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
        //mMediaPlayer.setLooping(true);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    //TODO: BLUETOOTH
    */
    //TODO: this is working to play on headset
/*    protected static final String TAG = "ZS-A2dp";

    Button mBtPlay;

    BluetoothAdapter mBtAdapter;
    BluetoothA2dp mA2dpService;

    AudioManager mAudioManager;
    MediaPlayer mPlayer;

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "receive intent for action : " + action);
            if (action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_DISCONNECTED);
                if (state == BluetoothA2dp.STATE_CONNECTED) {
                    setIsA2dpReady(true);
                    playMusic();
                } else if (state == BluetoothA2dp.STATE_DISCONNECTED) {
                    setIsA2dpReady(false);
                }
            } else if (action.equals(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED)) {
                int state = intent.getIntExtra(BluetoothA2dp.EXTRA_STATE, BluetoothA2dp.STATE_NOT_PLAYING);
                if (state == BluetoothA2dp.STATE_PLAYING) {
                    Log.d(TAG, "A2DP start playing");
                    Toast.makeText(getApplication().getBaseContext(), "A2dp is playing", Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "A2DP stop playing");
                    Toast.makeText(MusicActivity.this, "A2dp is stopped", Toast.LENGTH_SHORT).show();
                }
            }
        }

    };

    boolean mIsA2dpReady = false;

    void setIsA2dpReady(boolean ready) {
        mIsA2dpReady = ready;
        Toast.makeText(this, "A2DP ready ? " + (ready ? "true" : "false"), Toast.LENGTH_SHORT).show();
    }

    private BluetoothProfile.ServiceListener mA2dpListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile a2dp) {
            Log.d(TAG, "a2dp service connected. profile = " + profile);
            if (profile == BluetoothProfile.A2DP) {
                mA2dpService = (BluetoothA2dp) a2dp;
                if (mAudioManager.isBluetoothA2dpOn()) {
                    setIsA2dpReady(true);
                    playMusic();
                } else {
                    Log.d(TAG, "bluetooth a2dp is not on while service connected");
                }
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            setIsA2dpReady(false);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_music);
        LinearLayout ll = new LinearLayout(this);
        setContentView(ll);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        registerReceiver(mReceiver, new IntentFilter(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED));
        registerReceiver(mReceiver, new IntentFilter(BluetoothA2dp.ACTION_PLAYING_STATE_CHANGED));

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        mBtAdapter.getProfileProxy(this, mA2dpListener, BluetoothProfile.A2DP);

    }

    @Override
    protected void onDestroy() {
        mBtAdapter.closeProfileProxy(BluetoothProfile.A2DP, mA2dpService);
        releaseMediaPlayer();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        releaseMediaPlayer();
        super.onPause();
    }

    private void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    private void playMusic() {

        StrictMode.ThreadPolicy old = StrictMode.getThreadPolicy();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder(old)
                .permitDiskWrites()
                .build());


        mPlayer = new MediaPlayer();

        Log.d(TAG, "start play music");
        //use setVolumeControlStream to allow the user to change
        //the volume using the hardware controls
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //we now instantiate the player using our music file;
        //note that since it is a resource in the "raw" directory,
        //we don't need the file's extension;
        //our file here is in fact called "soundfile.mp3";
        mPlayer = MediaPlayer.create(this, R.raw.joker);
        //we start playing the file!
        mPlayer.start();

        StrictMode.setThreadPolicy(old);
    }*/

}

