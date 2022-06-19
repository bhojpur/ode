package ode.formats.importer;

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import static ode.rtypes.rbool;
import static ode.rtypes.rstring;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ode.formats.importer.transfers.FileTransfer;
import ode.formats.importer.transfers.UploadFileTransfer;
import ode.services.server.repo.path.ClientFilePathTransformer;
import ode.services.server.repo.path.FsFile;
import ode.constants.namespaces.NSAUTOCLOSE;
import ode.constants.namespaces.NSFILETRANSFER;
import ode.grid.ImportSettings;
import ode.model.Annotation;
import ode.model.BooleanAnnotation;
import ode.model.BooleanAnnotationI;
import ode.model.CommentAnnotation;
import ode.model.CommentAnnotationI;
import ode.model.Fileset;
import ode.model.FilesetEntry;
import ode.model.FilesetEntryI;
import ode.model.IObject;
import ode.model.NamedValue;
import ode.model.UploadJob;
import ode.model.UploadJobI;

public class ImportContainer
{
    private String reader;
    private String[] usedFiles;
    private long usedFilesTotalSize;
    private Boolean isSPW;
    private File file;
    private Double[] userPixels;
    private String userSpecifiedName;
    private String userSpecifiedDescription;
    private boolean doThumbnails = true;
    private boolean noStatsInfo = false;
    private List<Annotation> customAnnotationList;
    private IObject target;
    private String checksumAlgorithm;
    private ImportConfig config;

    public ImportContainer(File file, IObject target, Double[] userPixels,
            String reader, String[] usedFiles, Boolean isSPW) {
        this(null, file, target, userPixels, reader, usedFiles, isSPW);
    }

    public ImportContainer(ImportConfig config,
            File file, IObject target, Double[] userPixels,
            String reader, String[] usedFiles, Boolean isSPW) {
        this.config = config;
        this.file = file;
        this.target = target;
        this.userPixels = userPixels;
        this.reader = reader;
        this.usedFiles = usedFiles;
        this.isSPW = isSPW;
    }

    // Various Getters and Setters //

    /**
     * Retrieves whether or not we are performing thumbnail creation upon
     * import completion.
     * @return <code>true</code> if we are to perform thumbnail creation and
     * <code>false</code> otherwise.
     */
    public boolean getDoThumbnails()
    {
        return doThumbnails;
    }

    /**
     * Sets whether or not we are performing thumbnail creation upon import
     * completion.
     * @param v <code>true</code> if we are to perform thumbnail creation and
     * <code>false</code> otherwise.
     */
    public void setDoThumbnails(boolean v)
    {
        doThumbnails = v;
    }

    /**
     * Retrieves whether or not we disabling <code>StatsInfo</code> population.
     * @return <code>true</code> if we are to disable <code>StatsInfo</code>
     * population. <code>false</code> otherwise.
     */
    public boolean getNoStatsInfo()
    {
        return noStatsInfo;
    }

    /**
     * Sets whether or not we disabling <code>StatsInfo</code> population.
     * @param v <code>true</code> if we are to disable <code>StatsInfo</code>
     * population. <code>false</code> otherwise.
     */
    public void setNoStatsInfo(boolean v)
    {
        noStatsInfo = v;
    }

    /**
     * Retrieves the current custom image/plate name string.
     * @return As above. <code>null</code> if it has not been set.
     */
    public String getUserSpecifiedName()
    {
        return userSpecifiedName;
    }

    /**
     * Sets the custom image/plate name for import. If this value is left
     * null, the image/plate name supplied by Bio-Formats will be used.
     * @param v A custom image/plate name to use for all entities represented
     * by this container.
     */
    public void setUserSpecifiedName(String v)
    {
        userSpecifiedName = v;
    }

    /**
     * Retrieves the current custom image/plate description string.
     * @return As above. <code>null</code> if it has not been set.
     */
    public String getUserSpecifiedDescription()
    {
        return userSpecifiedDescription;
    }

    /**
     * Sets the custom image/plate description for import. If this value is left
     * null, the image/plate description supplied by Bio-Formats will be used.
     * @param v A custom image/plate description to use for all images represented
     * by this container.
     */
    public void setUserSpecifiedDescription(String v)
    {
        userSpecifiedDescription = v;
    }

    /**
     * The list of custom, user specified, annotations to link to all images
     * represented by this container.
     * @return See above.
     */
    public List<Annotation> getCustomAnnotationList()
    {
        return customAnnotationList;
    }

    /**
     * Sets the list of custom, user specified, annotations to link to all
     * images represented by this container.
     * @param v The list of annotations to use.
     */
    public void setCustomAnnotationList(List<Annotation> v)
    {
        customAnnotationList = v;
    }

    /**
     * Return the reader class name used for reading the contents of this
     * import container.
     * @return See above.
     */
    public String getReader() {
        return reader;
    }

    /**
     * Sets the reader class name used for reading the contents of this
     * import container.
     * @param reader Bio-Formats reader class name.
     */
    public void setReader(String reader) {
        this.reader = reader;
    }

    /**
     * Return a list of file names that belong to this import container.
     * @return See above.
     */
    public String[] getUsedFiles() {
        return usedFiles;
    }

    /**
     * Set the list of image file names that belong to this import container.
     * @param usedFiles
     */
    public void setUsedFiles(String[] usedFiles) {
        this.usedFiles = usedFiles;
    }

    /**
     * Returns the total size in bytes (based on <code>File.length()</code>)
     * of all files in this import container.
     * @return See above.
     */
    public long getUsedFilesTotalSize() {
        return usedFilesTotalSize;
    }

    /**
     * Return true if this import container contains a Screen/Plate/Well image
     * group. False otherwise.
     * @return See above.
     */
    public Boolean getIsSPW() {
        return isSPW;
    }

    /**
     * Set true if the import container is filled in with a Screen/Plate/Well
     * image structure. False otherwise.
     * @param isSPW True if container contains S/P/W, false otherwise.
     */
    public void setIsSPW(Boolean isSPW) {
        this.isSPW = isSPW;
    }

    /**
     * @return the File
     */
    public File getFile() {
        return file;
    }

    /**
     * Package-private setter added during the 4.1 release to fix name ordering.
     * A better solution would be to have a copy-constructor which also takes a
     * chosen file.
     */
    void setFile(File file) {
        this.file = file;
    }

    public IObject getTarget() {
        return target;
    }

    public void setTarget(IObject obj) {
        this.target = obj;
    }

    public Double[] getUserPixels() {
        return userPixels;
    }

    public void setUserPixels(Double[] userPixels)
    {
        this.userPixels = userPixels;
    }

    public String getChecksumAlgorithm() {
        return this.checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String ca) {
        this.checksumAlgorithm = ca;
    }

    public void fillData(ImportSettings settings, Fileset fs,
            ClientFilePathTransformer sanitizer) throws IOException {
        fillData(config, settings, fs, sanitizer, null);
    }

    public void fillData(ImportSettings settings, Fileset fs,
            ClientFilePathTransformer sanitizer, FileTransfer transfer) throws IOException {
        fillData(config, settings, fs, sanitizer, transfer);
    }

    public void fillData(ImportConfig config, ImportSettings settings, Fileset fs,
            ClientFilePathTransformer sanitizer) throws IOException {
        fillData(config, settings, fs, sanitizer, null);
    }

    public void fillData(ImportConfig config, ImportSettings settings, Fileset fs,
            ClientFilePathTransformer sanitizer, FileTransfer transfer) throws IOException {

        if (config == null) {
            config = new ImportConfig(); // Lazily load
        }

        // TODO: These should possible be a separate option like
        // ImportUserSettings rather than misusing ImportContainer.
        settings.doThumbnails = rbool(getDoThumbnails());
        settings.noStatsInfo = rbool(getNoStatsInfo());
        settings.userSpecifiedTarget = getTarget();
        settings.userSpecifiedName = getUserSpecifiedName() == null ? null
                : rstring(getUserSpecifiedName());
        settings.userSpecifiedDescription = getUserSpecifiedDescription() == null ? null
                : rstring(getUserSpecifiedDescription());
        // Creating a new list so that later additions aren't propagated
        // to further images. trac:13074
        settings.userSpecifiedAnnotationList = getCustomAnnotationList() == null ? null :
            new ArrayList<Annotation>(getCustomAnnotationList());

        // pass an annotation
        if (config.autoClose.get()) {
            BooleanAnnotation ba = new BooleanAnnotationI();
            ba.setBoolValue(rbool(true));
            ba.setNs(rstring(NSAUTOCLOSE.value));
            settings.userSpecifiedAnnotationList.add(ba);
        }

        if (getUserPixels() != null) {
            Double[] source = getUserPixels();
            double[] target = new double[source.length];
            for (int i = 0; i < source.length; i++) {
                if (source[i] == null) {
                    target = null;
                    break;
                }
                target[i] = source[i];
            }
            settings.userSpecifiedPixels = target; // May be null.
        }

        // Fill used paths
        for (String usedFile : getUsedFiles()) {
            final FilesetEntry entry = new FilesetEntryI();
            final FsFile fsPath = sanitizer.getFsFileFromClientFile(new File(usedFile), Integer.MAX_VALUE);
            entry.setClientPath(rstring(fsPath.toString()));
            fs.addFilesetEntry(entry);
        }

        // Record any special file transfer
        if (transfer != null &&
                !transfer.getClass().equals(UploadFileTransfer.class)) {
            String type = transfer.getClass().getName();
            CommentAnnotation transferAnnotation = new CommentAnnotationI();
            transferAnnotation.setNs(rstring(NSFILETRANSFER.value));
            transferAnnotation.setTextValue(rstring(type));
            fs.linkAnnotation(transferAnnotation);
        }

        // Fill BF info
        final List<NamedValue> clientVersionInfo = new ArrayList<NamedValue>();
        clientVersionInfo.add(new NamedValue(ImportConfig.VersionInfo.BIO_FORMATS_READER.key, reader));
        config.fillVersionInfo(clientVersionInfo);
        UploadJob upload = new UploadJobI();
        upload.setVersionInfo(clientVersionInfo);
        fs.linkJob(upload);

    }

    public void updateUsedFilesTotalSize() {
        usedFilesTotalSize = 0;
        for (String filePath : usedFiles) {
            usedFilesTotalSize += new File(filePath).length();
        }
    }

}