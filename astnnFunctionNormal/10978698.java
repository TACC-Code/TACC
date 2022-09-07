class BackupThread extends Thread {
    public int putString(String theString, String targetChannel) {
        try {
            log.debug("Putting string\n" + theString + "\ninto channel \"" + targetChannel + "\"" + " on " + serverName);
            try {
                this.channelId = this.cmap.Add(targetChannel);
                this.cmap.PutTimeAuto("timeofday");
                this.cmap.PutMime(this.channelId, "text/plain");
            } catch (Exception e) {
                log.error("Error adding turbine channel: " + e);
            }
            this.cmap.PutDataAsString(this.channelId, theString);
            this.source.Flush(this.cmap);
        } catch (Exception e) {
            log.error("Error writing to turbine: " + e);
        }
        return this.channelId;
    }
}
