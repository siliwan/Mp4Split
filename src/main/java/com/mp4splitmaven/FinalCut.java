package com.mp4splitmaven;


import com.mp4splitmaven.HelperClass.FfmpegManager;
import com.mp4splitmaven.HelperClass.TimeStampManager;

import java.io.File;
import java.util.ArrayList;

public class FinalCut {


    FfmpegManager ffmpegManager = new FfmpegManager();
    Settings settings = Settings.getInstance();
    String location = "finalCut";



    public FinalCut() {

    }
    public void finalCut() {
        {
            try {

                String[] filesToCut = getNameToCut();
                String ffmpegLocation = settings.getFfmpegLocation();

                for(String files: filesToCut) {
                    String fileLocation = findFile(getOriginalFileName(files),new File (settings.getInputLocationt()));
                    String[] startTime = {getStartTimeFromName(files)};
                    Long[] clipLength = {settings.getClipLength()*1000l};
                    String[] videoLength = new TimeStampManager().formatTime(clipLength);

                    ffmpegManager.cutVideoExact(ffmpegLocation,fileLocation,startTime,videoLength);
                }




            }catch (Exception e){
                LoggingHandler.println(LoggingHandler.FATAL,"There was a problem trying to cut the video exact", e);
            }
        }
    }

    public String[] getNameToCut(){
        ArrayList<String> names = new ArrayList<String >();
        try {
            File folder = new File(System.getProperty("user.dir") + "\\" + location);
            File[] listOfFiles = folder.listFiles();

            for(File file: listOfFiles){
                names.add(file.getName());
            }
        }catch (Exception e) {
            LoggingHandler.println(LoggingHandler.WARN, "There was a problem trying to find the Files", e);
        }
        return names.toArray(new String[0]);
    }

    public String findFile(String name, File file) throws Exception {
        try {
            File[] list = file.listFiles();
            if (list != null) {
                for (File fil : list) {
                    if (fil.isDirectory()) {
                        findFile(name, fil);
                    } else if (name.equalsIgnoreCase(fil.getName())) {

                        return fil.getAbsolutePath();
                    }
                }
            }
            throw new Exception();
        }catch (Exception e){
            LoggingHandler.println(LoggingHandler.FATAL, "Can't find original File", e);
            throw new Exception();
        }
    }

    public String getStartTimeFromName(String fileName){
        return  (fileName.substring(fileName.length()-12,fileName.length()-4)).replace(".",":");
    }

    public String getOriginalFileName(String filename){
        String name = filename.substring(0,filename.indexOf("_clip_"))+".mp4";
        System.out.println(name);
        return name;
    }
}
