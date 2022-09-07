class BackupThread extends Thread {
    private void importData(String sourceName, File dataFile) {
        if (dataFile == null) {
            listener.postError("Data file not specified.");
            return;
        }
        DataFileReader reader;
        try {
            reader = new DataFileReader(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
            listener.postError("Problem reading data file header.");
            return;
        }
        List<DataChannel> channels = reader.getChannels();
        cindex = new int[channels.size()];
        samples = Integer.parseInt(reader.getProperty("samples"));
        int archiveSize = (int) Math.ceil((double) samples / SAMPLES_PER_FLUSH);
        source = new Source(1, "create", archiveSize);
        cmap = new ChannelMap();
        try {
            for (int i = 0; i < channels.size(); i++) {
                DataChannel channel = channels.get(i);
                cindex[i] = cmap.Add(channel.getName());
                cmap.PutMime(cindex[i], "application/octet-stream");
                if (channel.getUnit() != null) {
                    cmap.PutUserInfo(cindex[i], "units=" + channel.getUnit());
                }
            }
            source.OpenRBNBConnection(rbnbHostName + ":" + rbnbPortNumber, sourceName);
            source.Register(cmap);
        } catch (SAPIException e) {
            e.printStackTrace();
            listener.postError("Unable to connect to the server.");
            return;
        }
        boolean error = false;
        try {
            NumericDataSample sample;
            while ((sample = reader.readSample()) != null) {
                postDataSamples(sample.getTimestamp(), sample.getValues());
            }
            source.Flush(cmap);
        } catch (Exception e) {
            e.printStackTrace();
            error = true;
        }
        if (error) {
            source.CloseRBNBConnection();
            if (canceled) {
                listener.postError("The import was canceled.");
            } else {
                listener.postError("Problem importing data file.");
            }
        } else {
            source.Detach();
            listener.postCompletion();
        }
        canceled = false;
    }
}
