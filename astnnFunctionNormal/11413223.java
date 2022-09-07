class BackupThread extends Thread {
    HttpResponse rewrite(HttpResponse original) throws IOException, ImageReadException {
        return new JPEGOptimizer(new OptimizerConfig(), original).rewrite(JPEGOptimizer.readJpeg(original.getResponse()));
    }
}
