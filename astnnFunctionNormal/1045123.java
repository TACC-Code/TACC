class BackupThread extends Thread {
    private CZLSMInfoExtended getCZ_LSMINFO(RandomAccessStream stream, long position, boolean thumb) {
        CZLSMInfoExtended cz = new CZLSMInfoExtended();
        try {
            if (position == 0) return cz;
            stream.seek((int) position + 8);
            cz.DimensionX = ReaderToolkit.swap(stream.readInt());
            cz.DimensionY = ReaderToolkit.swap(stream.readInt());
            cz.DimensionZ = ReaderToolkit.swap(stream.readInt());
            cz.DimensionChannels = ReaderToolkit.swap(stream.readInt());
            cz.DimensionTime = ReaderToolkit.swap(stream.readInt());
            cz.IntensityDataType = ReaderToolkit.swap(stream.readInt());
            cz.ThumbnailX = ReaderToolkit.swap(stream.readInt());
            cz.ThumbnailY = ReaderToolkit.swap(stream.readInt());
            cz.VoxelSizeX = ReaderToolkit.swap(stream.readDouble());
            cz.VoxelSizeY = ReaderToolkit.swap(stream.readDouble());
            cz.VoxelSizeZ = ReaderToolkit.swap(stream.readDouble());
            cz.OriginX = ReaderToolkit.swap(stream.readDouble());
            cz.OriginY = ReaderToolkit.swap(stream.readDouble());
            cz.OriginZ = ReaderToolkit.swap(stream.readDouble());
            cz.ScanType = ReaderToolkit.swap(stream.readShort());
            cz.SpectralScan = ReaderToolkit.swap(stream.readShort());
            cz.DataType = ReaderToolkit.swap(stream.readInt());
            cz.OffsetVectorOverlay = ReaderToolkit.swap(stream.readInt());
            cz.OffsetInputLut = ReaderToolkit.swap(stream.readInt());
            cz.OffsetOutputLut = ReaderToolkit.swap(stream.readInt());
            cz.OffsetChannelColors = ReaderToolkit.swap(stream.readInt());
            cz.TimeIntervall = ReaderToolkit.swap(stream.readDouble());
            cz.OffsetChannelDataTypes = ReaderToolkit.swap(stream.readInt());
            cz.OffsetScanInformation = ReaderToolkit.swap(stream.readInt());
            cz.OffsetKsData = ReaderToolkit.swap(stream.readInt());
            cz.OffsetTimeStamps = ReaderToolkit.swap(stream.readInt());
            cz.OffsetEventList = ReaderToolkit.swap(stream.readInt());
            cz.OffsetRoi = ReaderToolkit.swap(stream.readInt());
            cz.OffsetBleachRoi = ReaderToolkit.swap(stream.readInt());
            cz.OffsetNextRecording = ReaderToolkit.swap(stream.readInt());
            cz.DisplayAspectX = ReaderToolkit.swap(stream.readDouble());
            cz.DisplayAspectY = ReaderToolkit.swap(stream.readDouble());
            cz.DisplayAspectZ = ReaderToolkit.swap(stream.readDouble());
            cz.DisplayAspectTime = ReaderToolkit.swap(stream.readDouble());
            cz.OffsetMeanOfRoisOverlay = ReaderToolkit.swap(stream.readInt());
            cz.OffsetTopoIsolineOverlay = ReaderToolkit.swap(stream.readInt());
            cz.OffsetTopoProfileOverlay = ReaderToolkit.swap(stream.readInt());
            cz.OffsetLinescanOverlay = ReaderToolkit.swap(stream.readInt());
            cz.ToolbarFlags = ReaderToolkit.swap(stream.readInt());
            cz.OffsetChannelWavelength = ReaderToolkit.swap(stream.readInt());
            cz.OffsetChannelFactors = ReaderToolkit.swap(stream.readInt());
            cz.ObjectiveSphereCorrection = ReaderToolkit.swap(stream.readInt());
            cz.OffsetUnmixParameters = ReaderToolkit.swap(stream.readInt());
            if (cz.OffsetChannelDataTypes != 0) {
                cz.OffsetChannelDataTypesValues = getOffsetChannelDataTypesValues(stream, cz.OffsetChannelDataTypes, cz.DimensionChannels);
            }
            if (cz.OffsetChannelColors != 0) {
                ChannelNamesAndColors channelNamesAndColors = getChannelNamesAndColors(stream, cz.OffsetChannelColors, cz.DimensionChannels);
                cz.channelNamesAndColors = channelNamesAndColors;
            }
            if (cz.OffsetChannelWavelength != 0) {
                cz.channelWavelength = getLambdaStamps(stream, cz.OffsetChannelWavelength);
            }
            if (cz.OffsetTimeStamps != 0) {
                cz.timeStamps = getTimeStamps(stream, cz.OffsetTimeStamps);
                if ((cz.ScanType == 3) || (cz.ScanType == 4) || (cz.ScanType == 5) || (cz.ScanType == 6) || (cz.ScanType == 9) || (cz.ScanType == 10)) {
                    if (cz.OffsetEventList != 0) cz.eventList = getEventList(stream, cz.OffsetEventList, cz.timeStamps.FirstTimeStamp);
                }
            }
            if (cz.OffsetScanInformation != 0) {
                if (!thumb) cz.scanInfo = getScanInfo(stream, cz.OffsetScanInformation);
            }
        } catch (IOException getCZ_LSMINFO_exception) {
            getCZ_LSMINFO_exception.printStackTrace();
        }
        return cz;
    }
}
