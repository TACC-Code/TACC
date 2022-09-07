class BackupThread extends Thread {
    void setPixelData(byte[] data, ImageData imageData) {
        switch(headerChunk.getColorType()) {
            case PngIhdrChunk.COLOR_TYPE_GRAYSCALE_WITH_ALPHA:
                {
                    int width = imageData.width;
                    int height = imageData.height;
                    int destBytesPerLine = imageData.bytesPerLine;
                    int srcBytesPerLine = getAlignedBytesPerRow();
                    if (headerChunk.getBitDepth() > 8) srcBytesPerLine /= 2;
                    byte[] rgbData = new byte[destBytesPerLine * height];
                    byte[] alphaData = new byte[width * height];
                    for (int y = 0; y < height; y++) {
                        int srcIndex = srcBytesPerLine * y;
                        int destIndex = destBytesPerLine * y;
                        int destAlphaIndex = width * y;
                        for (int x = 0; x < width; x++) {
                            byte grey = data[srcIndex];
                            byte alpha = data[srcIndex + 1];
                            rgbData[destIndex + 0] = grey;
                            rgbData[destIndex + 1] = grey;
                            rgbData[destIndex + 2] = grey;
                            alphaData[destAlphaIndex] = alpha;
                            srcIndex += 2;
                            destIndex += 3;
                            destAlphaIndex++;
                        }
                    }
                    imageData.data = rgbData;
                    imageData.alphaData = alphaData;
                    break;
                }
            case PngIhdrChunk.COLOR_TYPE_RGB_WITH_ALPHA:
                {
                    int width = imageData.width;
                    int height = imageData.height;
                    int destBytesPerLine = imageData.bytesPerLine;
                    int srcBytesPerLine = getAlignedBytesPerRow();
                    if (headerChunk.getBitDepth() > 8) srcBytesPerLine /= 2;
                    byte[] rgbData = new byte[destBytesPerLine * height];
                    byte[] alphaData = new byte[width * height];
                    for (int y = 0; y < height; y++) {
                        int srcIndex = srcBytesPerLine * y;
                        int destIndex = destBytesPerLine * y;
                        int destAlphaIndex = width * y;
                        for (int x = 0; x < width; x++) {
                            rgbData[destIndex + 0] = data[srcIndex + 0];
                            rgbData[destIndex + 1] = data[srcIndex + 1];
                            rgbData[destIndex + 2] = data[srcIndex + 2];
                            alphaData[destAlphaIndex] = data[srcIndex + 3];
                            srcIndex += 4;
                            destIndex += 3;
                            destAlphaIndex++;
                        }
                    }
                    imageData.data = rgbData;
                    imageData.alphaData = alphaData;
                    break;
                }
            case PngIhdrChunk.COLOR_TYPE_RGB:
                imageData.data = data;
                break;
            case PngIhdrChunk.COLOR_TYPE_PALETTE:
                imageData.data = data;
                if (alphaPalette != null) {
                    int size = imageData.width * imageData.height;
                    byte[] alphaData = new byte[size];
                    byte[] pixelData = new byte[size];
                    imageData.getPixels(0, 0, size, pixelData, 0);
                    for (int i = 0; i < pixelData.length; i++) {
                        alphaData[i] = alphaPalette[pixelData[i] & 0xFF];
                    }
                    imageData.alphaData = alphaData;
                }
                break;
            default:
                imageData.data = data;
                break;
        }
    }
}
