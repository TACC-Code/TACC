class BackupThread extends Thread {
    public static synchronized BufferedImage indexImage(java.io.File file, long fid, long vdid) {
        File dest = new File(LimeXMLProperties.instance().getPath() + File.separator + "thumbnails");
        if (!dest.exists()) {
            dest.mkdirs();
        }
        ImageReader reader = null;
        ImageWriter writer = null;
        BufferedImage image = null;
        Iterator iter = ImageIO.getImageReadersByFormatName(FileUtility.getFileExtension(file));
        while (iter.hasNext()) {
            reader = (ImageReader) iter.next();
        }
        iter = ImageIO.getImageWritersByFormatName(EXTENSION);
        if (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
        }
        if (reader != null && writer != null) {
            try {
                ImageReadParam parm = new ImageReadParam();
                parm.setSourceSubsampling(1, 1, 0, 0);
                ImageInputStream stream = ImageIO.createImageInputStream(file);
                reader.setInput(stream, true, true);
                int width = reader.getWidth(0);
                int height = reader.getHeight(0);
                float aspect = reader.getAspectRatio(0);
                image = reader.read(0, parm);
                stream.flush();
                stream.close();
                reader.dispose();
                int divisor = 1;
                if (aspect > 1.0) {
                    divisor = width;
                } else {
                    divisor = height;
                }
                double ratio_2 = (THUMBNAIL_SIZE_100 / (double) divisor);
                double ratio_3 = (THUMBNAIL_SIZE_255 / (double) divisor);
                int thumb_width = (int) (width * ((double) MAX_WIDTH / (double) divisor));
                int thumb_height = (int) (height * ((double) MAX_HEIGHT / (double) divisor));
                if ((width < MAX_WIDTH) || (height < MAX_HEIGHT)) {
                    thumb_width = width;
                    thumb_height = height;
                }
                BufferedImage actual = new BufferedImage(thumb_width, thumb_height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = (Graphics2D) actual.getGraphics();
                g2d.drawImage(image, 0, 0, thumb_width, thumb_height, null);
                String path = LimeXMLProperties.instance().getPath() + File.separator + "thumbnails" + File.separator + "a" + (new Long(fid)).toString() + "." + EXTENSION;
                IIOImage img = new IIOImage(actual, null, null);
                ImageOutputStream iout = ImageIO.createImageOutputStream(new File(path));
                writer.setOutput(iout);
                writer.write(img);
                actual.flush();
                iout.flush();
                iout.close();
                g2d.finalize();
                thumb_width = (int) (width * ratio_3);
                thumb_height = (int) (height * ratio_3);
                BufferedImage thumber1 = new BufferedImage(thumb_width, thumb_height, BufferedImage.TYPE_INT_RGB);
                g2d = (Graphics2D) thumber1.getGraphics();
                g2d.drawImage(image, 0, 0, thumb_width, thumb_height, null);
                thumb_width = (int) (width * ratio_2);
                thumb_height = (int) (height * ratio_2);
                BufferedImage thumber2 = new BufferedImage(thumb_width, thumb_height, BufferedImage.TYPE_INT_RGB);
                g2d = (Graphics2D) thumber2.getGraphics();
                g2d.drawImage(image, 0, 0, thumb_width, thumb_height, null);
                List thumbnails = new ArrayList();
                thumbnails.add(thumber2);
                path = LimeXMLProperties.instance().getPath() + File.separator + "thumbnails" + File.separator + "b" + (new Long(fid)).toString() + "." + EXTENSION;
                img = new IIOImage(thumber1, thumbnails, null);
                iout = ImageIO.createImageOutputStream(new File(path));
                writer.setOutput(iout);
                writer.write(img);
                iout.flush();
                iout.close();
                writer.dispose();
                g2d.finalize();
                thumber1.flush();
                image.flush();
                return thumber2;
            } catch (Exception e) {
                if (image != null) {
                    image.flush();
                }
                LOG.trace(" ", e);
            }
        }
        return null;
    }
}
