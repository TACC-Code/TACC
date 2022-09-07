class BackupThread extends Thread {
        private void serObject(PdfObject obj, int level, ByteBuffer bb) throws IOException {
            if (level <= 0) return;
            if (obj == null) {
                bb.append("$Lnull");
                return;
            }
            obj = PdfReader.getPdfObject(obj);
            if (obj.isStream()) {
                bb.append("$B");
                serDic((PdfDictionary) obj, level - 1, bb);
                if (level > 0) {
                    md5.reset();
                    bb.append(md5.digest(PdfReader.getStreamBytesRaw((PRStream) obj)));
                }
            } else if (obj.isDictionary()) {
                serDic((PdfDictionary) obj, level - 1, bb);
            } else if (obj.isArray()) {
                serArray((PdfArray) obj, level - 1, bb);
            } else if (obj.isString()) {
                bb.append("$S").append(obj.toString());
            } else if (obj.isName()) {
                bb.append("$N").append(obj.toString());
            } else bb.append("$L").append(obj.toString());
        }
}
