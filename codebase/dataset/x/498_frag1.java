    public void setOutputDirectory(File outputDirectory) {

        assert outputDirectory.isDirectory();

        this.outputDirectory = outputDirectory;

    }



    public void setWriteSingleFile(boolean writeSingleFile) {

        this.writeSingleFile = writeSingleFile;

    }



    public void setIsmvBuilder(Mp4Builder ismvBuilder) {

        this.ismvBuilder = ismvBuilder;

    }



    public void setManifestWriter(ManifestWriter manifestWriter) {

        this.manifestWriter = manifestWriter;
