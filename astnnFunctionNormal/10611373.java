class BackupThread extends Thread {
    protected void postData(double[] data) throws SAPIException {
        double adjustedTime = (data[data.length - 1] + timeOffset);
        logger.finer("Date adjusted for timezone:" + adjustedTime);
        logger.finer("Nice adjusted time:" + ISOtoRbnbTime.formatDate((long) adjustedTime * 1000));
        cmap.PutTime(adjustedTime, 0.0);
        if (seabirdParser.getChannels().length < data.length - 1) {
            logger.fine("data[] is of unexpected length:" + seabirdParser.getChannels().length);
            return;
        }
        try {
            for (int i = 0; i < data.length - 1; i++) {
                double[] dataTmp = new double[1];
                dataTmp[0] = data[i];
                cmap.PutDataAsFloat64(cmap.GetIndex(seabirdParser.getChannels()[i]), dataTmp);
                logger.finer("Posted data:" + data[i] + " into channel:" + seabirdParser.getChannels()[i]);
            }
            String[] metadataChannels = (String[]) seabirdParser.get("metadata-channels");
            String model = (String) seabirdParser.get("model");
            String serial = (String) seabirdParser.get("serial");
            cmap.PutDataAsString(cmap.GetIndex(metadataChannels[0]), model);
            cmap.PutDataAsString(cmap.GetIndex(metadataChannels[1]), serial);
            logger.finer("Posted metadata:" + model + ":" + serial + " to channels:" + metadataChannels[0] + ":" + metadataChannels[1]);
            logger.finer("Posted data and metadata");
        } catch (SAPIException sae) {
            sae.printStackTrace();
            throw sae;
        }
        source.Flush(cmap);
    }
}
