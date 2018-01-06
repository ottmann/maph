package com.example.elisabeth.depressionsapp.services;

/**
 * Created by elisabeth on 27.12.17.
 */

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AudioFileManager {

    /** Read all mp3 files from external storage "Music" folder */
    public List<HashMap<String,String>> getAllAudioFromDevice(final Context context) {

        List<HashMap<String,String>> fileList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        //String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%Music%"}, null);

        if (c != null) {
            while (c.moveToNext()) {

                String pathEnd = "";
                String path = c.getString(0);
                String name = path.substring(path.lastIndexOf("/") + 1);

                //System.out.println("Name :" + name);
                //System.out.println("Path :" + path);

                HashMap<String, String> song = new HashMap<>();

                if (path.length() > 3) {
                    pathEnd = path.substring(path.length() - 3);
                }

                if (pathEnd.equals("mp3")) {

                    System.out.println("MP3 name :" + name);
                    System.out.println("MP3 path :" + path);

                    song.put("file_path", path);
                    song.put("file_name", name);
                    fileList.add(song);
                }
            }
            c.close();
        }

        return fileList;
    }

    /*// SDCard Path
    //choose your path for me i choose sdcard
    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList songsList = new ArrayList();

    // Constructor
    public AudioFileManager() {

    }

    *//**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * *//*
    public ArrayList getPlayList(){
        File home = new File(MEDIA_PATH);

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap song = new HashMap();
                song.put("songTitle", file.getName().substring(0, (file.getName().length() - 4)));
                song.put("songPath", file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;
    }

    *//**
     * Class to filter files which are having .mp3 extension
     * *//*
    //you can choose the filter for me i put .mp3
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
*/
}
