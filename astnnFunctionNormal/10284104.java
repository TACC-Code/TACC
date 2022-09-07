class BackupThread extends Thread {
    public TIFFImageWriterSpi() {
        super(PackageUtil.getVendor(), PackageUtil.getVersion(), names, suffixes, MIMETypes, writerClassName, STANDARD_OUTPUT_TYPE, readerSpiNames, false, TIFFStreamMetadata.nativeMetadataFormatName, "com.tomgibara.imageio.impl.tiff.TIFFStreamMetadataFormat", null, null, false, TIFFImageMetadata.nativeMetadataFormatName, "com.tomgibara.imageio.impl.tiff.TIFFImageMetadataFormat", null, null);
    }
}
