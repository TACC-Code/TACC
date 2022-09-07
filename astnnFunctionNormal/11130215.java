class BackupThread extends Thread {
    private void setIcon(final ExpandItem newItem, final ZonePlayer dev) {
        Image oldImage = newItem.getImage();
        if (oldImage != null) {
            oldImage.dispose();
        }
        List<?> icons = dev.getMediaRendererDevice().getUPNPDevice().getDeviceIcons();
        if (icons == null || icons.isEmpty()) {
            newItem.setImage((Image) null);
            return;
        }
        final DeviceIcon deviceIcon = (DeviceIcon) icons.get(0);
        SonosController.getInstance().getWorkerExecutor().execute(new Runnable() {

            public void run() {
                InputStream is = null;
                URL url = deviceIcon.getUrl();
                try {
                    is = url.openStream();
                    final ImageData[] images = new ImageLoader().load(is);
                    if (images != null && images.length > 0) {
                        newItem.getDisplay().asyncExec(new Runnable() {

                            public void run() {
                                if (!newItem.isDisposed()) {
                                    newItem.setImage(new Image(newItem.getDisplay(), images[0]));
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex1) {
                        }
                    }
                }
            }
        });
    }
}
