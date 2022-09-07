class BackupThread extends Thread {
    public static void main(String[] args) {
        if (args.length < 5) {
            printUsageMessage();
            System.exit(1);
        }
        String outputFileName = null;
        finalImage = null;
        cols = Integer.parseInt(args[2]);
        rows = Integer.parseInt(args[3]);
        File[][] inputFile = new File[rows][cols];
        BufferedImage[][] image = new BufferedImage[rows][cols];
        Iterator readers;
        ImageReader imageReader;
        int imgIndex = 4;
        for (int i = 0; i < (rows); ++i) {
            for (int j = 0; j < (cols); ++j) {
                inputFile[i][j] = new File(args[imgIndex]);
                String fileExt = inputFile[i][j].getAbsolutePath().substring(inputFile[i][j].getAbsolutePath().indexOf('.') + 1, inputFile[i][j].getAbsolutePath().length());
                readers = ImageIO.getImageReadersBySuffix(fileExt);
                imageReader = (ImageReader) readers.next();
                try {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputFile[i][j]);
                    imageReader.setInput(imageInputStream, true);
                    image[i][j] = imageReader.read(0, null);
                } catch (IOException ex) {
                    Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.SEVERE, null, ex);
                }
                ++imgIndex;
            }
        }
        for (int i = 0; i < rows; ++i) {
            finalImageHeight += image[i][0].getHeight();
        }
        for (int i = 0; i < cols; ++i) {
            finalImageWidth += image[0][i].getWidth();
        }
        finalImage = new BufferedImage(finalImageWidth, finalImageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = finalImage.createGraphics();
        int x0 = 0;
        int y0 = 0;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                g2.drawImage(image[i][j], x0, y0, null);
                x0 += image[i][j].getWidth();
                image[i][j].flush();
            }
            x0 = 0;
            y0 += image[i][0].getHeight();
        }
        String arg1 = args[1];
        fileExtension = null;
        if (arg1.equals(JPEG2000) || arg1.equals(JPEG_2000)) fileExtension = "jp2"; else fileExtension = arg1;
        outputFileName = args[0] + "." + fileExtension;
        File outputFile = new File(outputFileName);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
            ImageIO.write(finalImage, arg1, out);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                finalImage.flush();
                out.flush();
                out.close();
                FileInputStream fis = new FileInputStream(outputFile);
                if (fis.getChannel().size() > 1) {
                    Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.INFO, "Conversion Done!");
                } else {
                    Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.WARNING, "Conversion problem encountered, no joy on " + outputFile.getName());
                }
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(ImageMergerConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
