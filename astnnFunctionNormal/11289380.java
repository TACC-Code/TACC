class BackupThread extends Thread {
    public void testRelax() {
        {
            final Format f1 = new Format("abc", Format.byteArray);
            assertTrue(f1.relax().equals(f1));
        }
        {
            final Format f1 = new Format(null, Format.byteArray);
            assertTrue(f1.relax().equals(f1));
        }
        {
            final Format f1 = new Format("abc");
            assertTrue(f1.relax().equals(f1));
        }
        {
            final Format f1 = new Format(null);
            assertTrue(f1.relax().equals(f1));
        }
        {
            final IndexedColorFormat f1 = new IndexedColorFormat(new Dimension(1, 1), 2000, Format.byteArray, 3.f, 1, 2, new byte[] { 0, 0 }, new byte[] { 0, 0 }, new byte[] { 0, 0 });
            final IndexedColorFormat f2 = (IndexedColorFormat) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getRedValues(), f1.getRedValues());
            assertEquals(f2.getGreenValues(), f1.getGreenValues());
            assertEquals(f2.getBlueValues(), f1.getBlueValues());
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getLineStride(), -1);
            assertEquals(f2.getMapSize(), f1.getMapSize());
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
        }
        {
            final AudioFormat f1 = new AudioFormat(AudioFormat.DOLBYAC3, 2.0, 1, 2, 3, 4, 5, 6.0, Format.byteArray);
            final AudioFormat f2 = (AudioFormat) f1.relax();
            assertTrue(f1.equals(f2));
            assertEquals(f2.getSampleRate(), f1.getSampleRate());
            assertEquals(f2.getChannels(), f1.getChannels());
            assertEquals(f2.getEndian(), f1.getEndian());
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), f1.getFrameRate());
            assertEquals(f2.getFrameSizeInBits(), f1.getFrameSizeInBits());
            assertEquals(f2.getSampleSizeInBits(), f1.getSampleSizeInBits());
            assertEquals(f2.getSigned(), f1.getSigned());
        }
        {
            final RGBFormat f1 = new RGBFormat(new Dimension(1, 1), 2000, Format.byteArray, 2.f, 1, 2, 3, 4, 5, 6, 7, 8);
            final RGBFormat f2 = (RGBFormat) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getRedMask(), f1.getRedMask());
            assertEquals(f2.getGreenMask(), f1.getGreenMask());
            assertEquals(f2.getBlueMask(), f1.getBlueMask());
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getLineStride(), -1);
            assertEquals(f2.getEndian(), f1.getEndian());
            assertEquals(f2.getBitsPerPixel(), f1.getBitsPerPixel());
            assertEquals(f2.getFlipped(), f1.getFlipped());
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
        }
        {
            final YUVFormat f1 = new YUVFormat(new Dimension(1, 1), 2000, Format.byteArray, 2.f, 1, 2, 3, 4, 5, 6);
            final YUVFormat f2 = (YUVFormat) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
            assertEquals(f2.getOffsetU(), -1);
            assertEquals(f2.getOffsetV(), -1);
            assertEquals(f2.getOffsetY(), -1);
            assertEquals(f2.getStrideUV(), -1);
            assertEquals(f2.getStrideY(), -1);
            assertEquals(f2.getYuvType(), f1.getYuvType());
        }
        {
            final JPEGFormat f1 = new JPEGFormat(new Dimension(1, 1), 2000, Format.byteArray, 2.f, 1, 2);
            final JPEGFormat f2 = (JPEGFormat) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
            assertEquals(f2.getQFactor(), f1.getQFactor());
            assertEquals(f2.getDecimation(), f1.getDecimation());
        }
        {
            final H261Format f1 = new H261Format(new Dimension(1, 1), 2000, Format.byteArray, 2.f, 1);
            final H261Format f2 = (H261Format) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
            assertEquals(f2.getStillImageTransmission(), f1.getStillImageTransmission());
        }
        {
            final H263Format f1 = new H263Format(new Dimension(1, 1), 2000, Format.byteArray, 2.f, 1, 2, 3, 4, 5, 6);
            final H263Format f2 = (H263Format) f1.relax();
            assertFalse(f1.equals(f2));
            assertEquals(f2.getEncoding(), f1.getEncoding());
            assertEquals(f2.getDataType(), f1.getDataType());
            assertEquals(f2.getFrameRate(), -1.f);
            assertEquals(f2.getMaxDataLength(), -1);
            assertEquals(f2.getSize(), null);
            assertEquals(f2.getAdvancedPrediction(), f1.getAdvancedPrediction());
            assertEquals(f2.getArithmeticCoding(), f1.getArithmeticCoding());
            assertEquals(f2.getErrorCompensation(), f1.getErrorCompensation());
            assertEquals(f2.getHrDB(), f1.getHrDB());
            assertEquals(f2.getPBFrames(), f1.getPBFrames());
            assertEquals(f2.getUnrestrictedVector(), f1.getUnrestrictedVector());
        }
    }
}
