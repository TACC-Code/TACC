class BackupThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    while (!requests.isEmpty()) {
                        ImageRequest request = requests.poll();
                        final File file = request.img_filename;
                        System.err.println("Downloading: " + request.img_url);
                        try {
                            URLConnection conn = (URLConnection) request.img_url.openConnection();
                            conn.connect();
                            if (file.exists()) {
                                file.delete();
                            }
                            file.createNewFile();
                            FileOutputStream output = new FileOutputStream(file);
                            InputStream input = conn.getInputStream();
                            final byte[] buffer = new byte[1024];
                            int last_read = 0;
                            do {
                                last_read = input.read(buffer);
                                if (last_read > 0) {
                                    output.write(buffer, 0, last_read);
                                }
                            } while (last_read != -1);
                            input.close();
                            output.close();
                        } catch (IOException e) {
                            requests.add(request);
                            e.printStackTrace();
                        }
                        request.notifyFileFetched();
                    }
                    synchronized (ImageCache.this) {
                        try {
                            ImageCache.this.wait(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
}
