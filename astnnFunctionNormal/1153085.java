class BackupThread extends Thread {
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isIsDisposedMethod(method)) {
                return new Boolean(disposed);
            } else if (isDisposeMethod(method)) {
                for (Iterator itor = images.entrySet().iterator(); itor.hasNext(); ) {
                    Map.Entry e = (Map.Entry) itor.next();
                    Image value = (Image) e.getValue();
                    dispose(value);
                }
                disposed = true;
                return null;
            } else if (Util.isGetter(method) && Image.class.equals(method.getReturnType())) {
                Property property = Util.getProperty(method);
                URL url = interfaceType.getResource(base + File.separator + property.getName() + ".png");
                Image image = (Image) images.get(url);
                if (image == null) {
                    image = new Image(Display.getCurrent(), url.openStream());
                    images.put(url, image);
                }
                return image;
            } else {
                return Util.defaultHandleObjectInvoke(interfaceType, this, proxy, method, args);
            }
        }
}
