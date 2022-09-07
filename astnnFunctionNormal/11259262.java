class BackupThread extends Thread {
    public static void delete(_PhotoBase photo) throws SQLException {
        if (photo == null) return;
        Session ssn = getSession();
        try {
            beginTransaction();
            int photo_size = DLOG4JUtils.sizeInKbytes(photo.getPhotoInfo().getSize());
            photo.getSite().getCapacity().incPhotoUsed(photo_size);
            photo.getAlbum().incPhotoCount(-1);
            photo.getUser().getCount().incPhotoCount(-1);
            AlbumBean parent = photo.getAlbum().getParent();
            int deep = 0;
            do {
                if (parent == null) break;
                deep++;
                parent.incPhotoCount(-1);
                parent = parent.getParent();
            } while (deep < 10);
            TagDAO.deleteTagByRefId(photo.getId(), TagBean.TYPE_PHOTO);
            List rpls = photo.getReplies();
            for (int i = 0; rpls != null && i < rpls.size(); i++) {
                PhotoReplyBean prb = (PhotoReplyBean) rpls.get(i);
                if (prb.getUser() != null) {
                    prb.getUser().getCount().incPhotoReplyCount(-1);
                }
            }
            executeUpdate("UPDATE AlbumBean AS a SET a.cover = NULL WHERE a.cover.id=?", photo.getId());
            ssn.delete(photo);
            commit();
        } catch (HibernateException e) {
            rollback();
            throw e;
        }
    }
}
