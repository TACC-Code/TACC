class BackupThread extends Thread {
        @Override
        protected ValueType createLoadable() throws LoaderException {
            try {
                if (!properties.containsKey(Loader.KEY_RESOURCEURI)) {
                    throw new LoaderException(Loader.KEY_RESOURCEURI + " not specified for object.");
                }
                String path = (String) properties.get(Loader.KEY_RESOURCEURI);
                URL url = new URL(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(url.openStream());
                ValueType object = (ValueType) objectInputStream.readObject();
                return object;
            } catch (MalformedURLException e) {
                throw new LoaderException(e);
            } catch (IOException e) {
                throw new LoaderException(e);
            } catch (ClassNotFoundException e) {
                throw new LoaderException(e);
            }
        }
}
