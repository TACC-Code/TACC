class BackupThread extends Thread {
    public static String getChannelListInstructions() {
        return HtmlUtility.htmlText(HtmlUtility.italicText("Start the upload of channel(s) from RBNB <br>" + "to the Archive by clicking on the <br>" + "'" + START_CAPTURE + "'" + " button. <br>" + "Drag channels for download (source channes only) <br> " + "to channel list in the dialog. Vidio channels and audio <br>" + "channes can only be run 'one to a thread.' Multiple numeric <br>" + "channels can be combined into one archive segment <br>" + "and, hence, run in one thread. Each thread results<br>" + "in a seperate, new archive segment."));
    }
}
