package com.example.elisabeth.depressionsapp.services;

/**
 * Created by elisabeth on 27.12.17.
 */

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class AudioFileManager {

    // SDCard Path
    //choose your path for me i choose sdcard
    final String MEDIA_PATH = new String("/sdcard/");
    private ArrayList songsList = new ArrayList();

    // Constructor
    public AudioFileManager() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
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

    /**
     * Class to filter files which are having .mp3 extension
     * */
    //you can choose the filter for me i put .mp3
    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }

}
