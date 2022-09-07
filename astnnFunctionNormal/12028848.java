class BackupThread extends Thread {
    public DfsIndexRecordReader(JobConf jobConf, InputSplit inputSplit, IDocumentDuplicateInformation duplicateInformation) throws IOException {
        _duplicateInformation = duplicateInformation;
        FileSystem fileSystem = FileSystem.get(jobConf);
        _fileSplit = (FileSplit) inputSplit;
        Path indexPath = _fileSplit.getPath();
        String md5 = MD5Hash.digest(indexPath.toString()).toString();
        _uncompressedIndexPath = new Path(FileOutputFormat.getOutputPath(jobConf), ".indexes/" + indexPath.getName() + "-" + md5 + "-uncompress");
        FileUtil.unzipInDfs(fileSystem, indexPath, _uncompressedIndexPath);
        try {
            int bufferSize = jobConf.getInt(IHadoopConstants.IO_FILE_BUFFER_SIZE, 4096);
            _indexReader = IndexReader.open(new DfsDirectory(fileSystem, _uncompressedIndexPath, bufferSize));
            _maxDoc = _indexReader.maxDoc();
        } catch (Exception e) {
            LOG.warn("can not open index '" + indexPath + "', ignore this index.", e);
        }
    }
}
