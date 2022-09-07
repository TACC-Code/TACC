class BackupThread extends Thread {
            void run(D data) {
                final int toRead;
                if (remainingLength != UNLIMITED && readDataSize > remainingLength) {
                    toRead = (int) remainingLength;
                } else {
                    toRead = readDataSize;
                }
                final Promise<Object[]> readAndWrite = Wait.all(readData(in, toRead), writeData(out, data));
                new ProcessWhen<Object[]>(readAndWrite) {

                    @Override
                    protected Promise<Void> resolved(Object[] value) throws Throwable {
                        D data = IOUtils.<D>blindCast(value[0]);
                        if (data == null) {
                            success(copiedLength);
                        } else {
                            copiedLength += data.length();
                            if (remainingLength != UNLIMITED) {
                                remainingLength -= data.length();
                            }
                            run(data);
                        }
                        return null;
                    }
                };
            }
}
