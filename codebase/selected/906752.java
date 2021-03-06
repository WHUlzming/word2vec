package org.photovault.dcraw;

import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.KernelJAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderableOp;
import javax.media.jai.RenderedImageAdapter;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;
import javax.media.jai.operator.BandCombineDescriptor;
import javax.media.jai.operator.HistogramDescriptor;
import javax.media.jai.operator.LookupDescriptor;
import javax.media.jai.operator.RenderableDescriptor;
import javax.media.jai.operator.ScaleDescriptor;
import org.photovault.common.PhotovaultException;
import org.photovault.image.PhotovaultImage;

/**
 Class to represent a raw camera image and set the parameters related to
 processing it.
 <p>
 <strong>Image processing steps</strong>
 <p>
 The actual raw image loading and processing up to demosaicing is done with dcraw.
 Since dcraw pipeline may have some nonlinear processing steps (at least when 
 camera ICC profile is used) that are done <em>after</em> color correction the
 resulting image can have color effects that depend in non-desirable way from actual 
 white balance settings.
 <p>
 To avoid this I load image from dcraw always using the daylight color correction
 recommended in the image file (stored to daylightMultipliers). I then do the actual white 
 balance correction here. This is certainly non-optimal and at least in theory 
 can lead to lost accuracy or even color clipping. But at least it behaves
 predictably.
 <p>
 As a summary, the processing steps done for raw image are
 <ul>
 <li>dcraw loads raw data</li>
 <li>dcraw scales raw channels using daylight multipliers</li>
 <li>draw does demosaicing (using 2x2 box filter if small resolution is enough
 or AHD interpolation</li>
 <li>dcraw converts to linear sRGB color space using either camera specific color 
 matrix or ICC profile</li>
 <li>This class loads image from dcraw and forms image mipmap for JAI renderable 
 pipeline</li>
 <li>This class finalizes color correction to desired white balance</li>
 <li>This class does exposure & contrast adjustments and converts the image from 
 linear to gamma corrected sRGB color space.</li>
 <li>Base class ({@link PhotovaultImage}) applies desired lookup tables in gamma 
 corrected RGB & IHS color spaces if needed and instructs JAI to render final 
 image.
 </ul>
 
 */
public class RawImage extends PhotovaultImage {

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(RawImage.class.getName());

    /**
     ICC profile to use for raw conversion
     */
    ColorProfileDesc colorProfile = null;

    /**
     {@link DCRawProcessWrapper} used for raw conversions
     */
    DCRawProcessWrapper dcraw = null;

    /**
     The raw image file or <code>null</code> if not set
     */
    File f = null;

    /**
     16 bit linear image returned by dcraw or <code>null</code> if the image
     has not been read or the conversion settings have been changed after
     reading.
     */
    PlanarImage rawImage = null;

    /**
     Raw image with WB correction applied
     */
    RenderableOp wbAdjustedRawImage = null;

    /**
     Is the raw image loaded only half of the actual resolution?
     */
    boolean rawIsHalfSized = true;

    /**
     8 bit gamma corrected version of the image or <code>null</code> if the image
     has not been converted or the conversion settings have been changed after
     reading.
     */
    RenderableOp correctedImage = null;

    /**
     Is this file really a raw image?
     */
    boolean validRawFile = false;

    /**
     Pixel value that is considered white in the linear image.
     */
    int white = 0;

    /**
     EV correction in F-stops. Positive values make the image brighter,
     negative darker. 0 sets white point to 99%
     */
    double evCorr = 0;

    /**
     Lookup table from 16 bit linear to 8 bit gamma corrected image
     */
    byte[] gammaLut = new byte[0x10000];

    /**
     Timestamp of the raw image
     */
    private Date timestamp = null;

    /**
     Camera model that was used to take the picture
     */
    private String camera = null;

    /**
     Film speed in ISO units or -1 if not known
     */
    private int filmSpeed = -1;

    /**
     Shutter speed in seconds
     */
    private double shutterSpeed = 0;

    /**
     Aperture of the camera (in f stops)
     */
    private double aperture = 0;

    /**
     Focal length of the camera in millimeters
     */
    private double focalLength = 0;

    /**
     Does the raw file have an embedded ICC profile?
     */
    private boolean hasICCProfile = false;

    /**
     Channel multipliers recommended by camera
     */
    private double cameraMultipliers[] = null;

    /**
     Channel multipliers for image shot at daylight as read by dcraw.
     3 doubles: R, G, B.
     */
    private double daylightMultipliers[] = null;

    /**
     Channel multipliers used for the conversion from raw image to sRGB color 
     space.
     */
    private double chanMultipliers[] = null;

    /**
     Color temperature (in Kelvin)
     */
    private double ctemp = 0.0;

    /**
     Green channel gain compared to blackbody radiator color temperature
     */
    private double greenGain = 1.0;

    /**
     Level set as black
     */
    private int black = 0;

    /**
     Width of the raw image in pixels
     */
    private int width;

    /**
     Height of the raw image in pixels
     */
    private int height;

    private int histBins[][];

    /**
     Returns true if this file is really a raw image file that can be decoded.
     */
    public boolean isValidRawFile() {
        return validRawFile;
    }

    /**
     * Get the shooting time of the image
     * @return Shooting time as reported by dcraw or <CODE>null</CODE> if
     * not available
     */
    public Date getTimestamp() {
        return (timestamp != null) ? (Date) timestamp.clone() : null;
    }

    /**
     * Get the camera mode used to shoot the image
     * @return Camera model reported by dcraw
     */
    public String getCamera() {
        return camera;
    }

    /**
     * Set the camera model.
     * @param camera The new camera model
     */
    public void setCamera(String camera) {
        this.camera = camera;
    }

    /**
     * Get the film speed setting used when shooting the image
     * @return Film speed (in ISO) as reported by dcraw
     */
    public int getFilmSpeed() {
        return filmSpeed;
    }

    /**
     * Get the shutter speed used when shooting the image
     * @return Exposure time (in seconds) as reported by dcraw
     */
    public double getShutterSpeed() {
        return shutterSpeed;
    }

    /**
     * Get aperture (f-stop) used when shooting the image
     * @return F-stop number reported by dcraw
     */
    public double getAperture() {
        return aperture;
    }

    /**
     * Get the focal length from image file meta data.
     * @return Focal length used when taking the picture (in millimetres)
     */
    public double getFocalLength() {
        return focalLength;
    }

    /**
     * Does the raw file have an embedded ICC color profile
     * @return <CODE>true</CODE> if the file has an embedded ICC color profile that dcraw
     * can read, <CODE>false</CODE> otherwise
     */
    public boolean isHasICCProfile() {
        return hasICCProfile;
    }

    /**
     * Get the color channel multipliers recommended by camera
     * @return The multipliers (4 doubles, RGBG)
     */
    public double[] getCameraMultipliers() {
        return (cameraMultipliers != null) ? cameraMultipliers.clone() : null;
    }

    /**
     * Get the color channel multipliers that should be used for pictures
     * taken in daylight.
     * @return The multipliers (3 doubles, RGB)
     */
    public double[] getDaylightMultipliers() {
        return (daylightMultipliers != null) ? daylightMultipliers.clone() : null;
    }

    /**
     * Set the exposure correction. Photovault sets the default exposure so that
     * 99% of raw image pixels fall under 99% gray.
     * @param evCorr The correction in f-stops.
     */
    public void setEvCorr(double evCorr) {
        this.evCorr = evCorr;
        applyGammaLut();
        fireChangeEvent(new RawImageChangeEvent(this));
    }

    /**
     * Get the current exposure correction
     * @return Correction in F-stops
     */
    public double getEvCorr() {
        return evCorr;
    }

    /**
     Amount of highlight compression/expansion that will be applied (in f-stops)
     Value 0 will cause linear tone mapping. Positive values set the produced white
     to map to this many f-stops higher luminance.
     */
    double highlightCompression = 0.0;

    /**
     * Set the amount of highlight compression/expansion that will be applied in raw
     * conversion
     * @param c New highlight compression
     */
    public void setHighlightCompression(double c) {
        highlightCompression = c;
        applyGammaLut();
        fireChangeEvent(new RawImageChangeEvent(this));
    }

    /**
     * Get current highlight compression/expansion
     * @return The current value
     */
    public double getHighlightCompression() {
        return highlightCompression;
    }

    public int[][] getHistogramBins() {
        return (histBins != null) ? histBins.clone() : null;
    }

    /** Creates a new instance of RawImage
     @param f The raw file to open
     @throws PhotovaultException if dcraw has not been initialized properly
     */
    public RawImage(File f) throws PhotovaultException {
        this.f = f;
        readFileInfo();
    }

    /**
     List of {@linkto RawImageChangeListener}s that should be notified about
     changes to this image.
     */
    ArrayList listeners = new ArrayList();

    /**
     Register an object to be notified about changes to this image.
     @param l The new listener.
     */
    public void addChangeListener(RawImageChangeListener l) {
        listeners.add(l);
    }

    /**
     Remove a listener previpously registered with addChangeListener.
     @param l The listener that shouldno longer be notified.
     */
    public void removeChangeListener(RawImageChangeListener l) {
        listeners.remove(l);
    }

    /**
     Send {@linkto RawImageChangeEvent} to all listeners
     @param ev The event that will be sent.
     */
    private void fireChangeEvent(RawImageChangeEvent ev) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            RawImageChangeListener l = (RawImageChangeListener) iter.next();
            l.rawImageSettingsChanged(ev);
        }
    }

    public RenderedImage getImage() {
        return null;
    }

    /**
     *     Get a 8 bit gamma corrected version of the image.
     * @return The corrected image
     */
    public RenderableOp getCorrectedImage(int minWidth, int minHeight, boolean isLowQualityAcceptable) {
        boolean isHalfSizeEnough = (minWidth * 2 <= width) && (minHeight * 2 <= height);
        if (rawImage == null || (rawIsHalfSized && !isHalfSizeEnough)) {
            dcraw.setHalfSize(isHalfSizeEnough);
            loadRawImage();
            correctedImage = null;
        }
        if (correctedImage == null) {
            createGammaLut();
            LookupTableJAI jailut = new LookupTableJAI(gammaLut);
            correctedImage = LookupDescriptor.createRenderable(wbAdjustedRawImage, jailut, null);
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
            cm = new ComponentColorModel(cs, new int[] { 8, 8, 8 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        }
        return correctedImage;
    }

    /**
     Set the preferred minimum size for the resulting image. Raw converter can use
     optimizations (e.g. use 2x2 block filter instead of true demosaicing) if 
     the actual image is larger than this
     @param minWidth The preferred minimum image width.
     @param minHeight The preferred minimum image height.
     @return <code>True</code> if given minimum size was larger than the already 
     loaded version. In this case the caller should reload image using 
     {@link #getCorrectedImage()}.
     @deprecated Use scaling paremeters in getRenderedImage instead.
     */
    public boolean setMinimumPreferredSize(int minWidth, int minHeight) {
        boolean needsReload = (correctedImage == null);
        if (minWidth * 2 > width || minHeight * 2 > height) {
            dcraw.setHalfSize(false);
            if (rawIsHalfSized) {
                needsReload = true;
                rawImage = null;
                correctedImage = null;
            }
        } else {
            dcraw.setHalfSize(true);
        }
        return needsReload;
    }

    /**
     Color model of the image after raw conversion
     */
    ColorModel cm = null;

    /**
     Sample model of the image after raw conversion
     */
    SampleModel sm = null;

    /**
     Get the sample model of the image after raw conversion.
     */
    public SampleModel getCorrectedImageSampleModel() {
        return sm;
    }

    /**
     Get the color model of the image after raw conversion.
     */
    public ColorModel getCorrectedImageColorModel() {
        return cm;
    }

    /**
     * Load the raw image using dcraw. No processing is yet done for the image,
     * however, the histogram & white point is calculated.
     */
    private void loadRawImage() {
        if (dcraw == null) {
            dcraw = new DCRawProcessWrapper();
        }
        try {
            if (colorProfile != null) {
                dcraw.setIccProfile(colorProfile.getInstanceFile());
            } else {
                dcraw.setIccProfile(null);
            }
            if (this.chanMultipliers == null) {
                chanMultipliers = cameraMultipliers.clone();
                calcCTemp();
            }
            double dl[] = new double[4];
            dl[0] = daylightMultipliers[0];
            dl[1] = daylightMultipliers[1];
            dl[2] = daylightMultipliers[2];
            dl[3] = daylightMultipliers[1];
            dcraw.setWbCoeffs(dl);
            InputStream is = dcraw.getRawImageAsTiff(f);
            Iterator readers = ImageIO.getImageReadersByFormatName("TIFF");
            ImageReader reader = (ImageReader) readers.next();
            log.debug("Creating stream");
            ImageInputStream iis = ImageIO.createImageInputStream(is);
            reader.setInput(iis, false, false);
            BufferedImage img = reader.read(0);
            WritableRaster r = img.getRaster();
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
            ColorModel targetCM = new ComponentColorModel(cs, new int[] { 16, 16, 16 }, false, false, Transparency.OPAQUE, DataBuffer.TYPE_USHORT);
            rawImage = new TiledImage(new BufferedImage(targetCM, r, true, null), 256, 256);
            final float[] DEFAULT_KERNEL_1D = { 0.25f, 0.5f, 0.25f };
            ParameterBlock pb = new ParameterBlock();
            KernelJAI kernel = new KernelJAI(DEFAULT_KERNEL_1D.length, DEFAULT_KERNEL_1D.length, DEFAULT_KERNEL_1D.length / 2, DEFAULT_KERNEL_1D.length / 2, DEFAULT_KERNEL_1D, DEFAULT_KERNEL_1D);
            pb.add(kernel);
            BorderExtender extender = BorderExtender.createInstance(BorderExtender.BORDER_COPY);
            RenderingHints hints = JAI.getDefaultInstance().getRenderingHints();
            if (hints == null) {
                hints = new RenderingHints(JAI.KEY_BORDER_EXTENDER, extender);
            } else {
                hints.put(JAI.KEY_BORDER_EXTENDER, extender);
            }
            RenderedOp filter = new RenderedOp("convolve", pb, hints);
            pb = new ParameterBlock();
            pb.addSource(filter);
            pb.add(new Float(0.5F)).add(new Float(0.5F));
            pb.add(new Float(0.0F)).add(new Float(0.0F));
            pb.add(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
            RenderedOp downSampler = new RenderedOp("scale", pb, null);
            RenderableOp rawImageRenderable = RenderableDescriptor.createRenderable(rawImage, downSampler, null, null, null, null, null);
            double colorCorrMat[][] = new double[][] { { colorCorr[0], 0.0, 0.0, 0.0 }, { 0.0, colorCorr[1], 0.0, 0.0 }, { 0.0, 0.0, colorCorr[2], 0.0 } };
            wbAdjustedRawImage = BandCombineDescriptor.createRenderable(rawImageRenderable, colorCorrMat, null);
            reader.getImageMetadata(0);
            rawIsHalfSized = dcraw.ishalfSize();
            createHistogram();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (PhotovaultException ex) {
            ex.printStackTrace();
        }
        if (autoExposeRequested) {
            doAutoExpose();
        }
    }

    /**
     Recalculate image histogram (histBin structure).
     */
    private void createHistogram() {
        int numBins[] = { 65536 };
        double lowVal[] = { 0. };
        double highVal[] = { 65535. };
        RenderedOp histOp = HistogramDescriptor.create(rawImage, null, Integer.valueOf(1), Integer.valueOf(1), numBins, lowVal, highVal, null);
        Histogram hist = (Histogram) histOp.getProperty("histogram");
        histBins = hist.getBins();
    }

    /**
     * Logarithmic average of image luminance
     */
    private double logAvg = 0;

    /**
     * <CODE>true</CODE> if auto exposure calculation should be performed before accessing
     * image data next time
     */
    private boolean autoExposeRequested = true;

    /**
     * Recalculate the exposure values with autoexposure algorithm:
     * <ul>
     *    <li>Exposure is set so that log average brightness will map to 18% white</li>
     *    <li>Highlight compression is adjusted so that 1 % of pixels will be white.</li>
     * </ul>
     */
    public void autoExpose() {
        autoExposeRequested = true;
        if (rawImage != null) {
            doAutoExpose();
        } else {
            loadRawImage();
        }
        fireChangeEvent(new RawImageChangeEvent(this));
    }

    static double MAX_AUTO_HLIGHT_COMP = 1.0;

    static double MIN_AUTO_HLIGHT_COMP = 0.0;

    /**
     * Calculate the auto exposure settings
     */
    private void doAutoExpose() {
        autoExposeRequested = false;
        double lumMat[][] = { { 0.27, 0.67, 0.06, 0.0 } };
        RenderedOp lumImg = BandCombineDescriptor.create(rawImage, lumMat, null);
        int numBins[] = { 65536 };
        double lowVal[] = { 0. };
        double highVal[] = { 65535. };
        RenderedOp histOp = HistogramDescriptor.create(lumImg, null, Integer.valueOf(1), Integer.valueOf(1), numBins, lowVal, highVal, null);
        Histogram hist = (Histogram) histOp.getProperty("histogram");
        int[][] histBins = hist.getBins();
        double logSum = 0.0;
        int pixelCount = 0;
        for (int n = 0; n < histBins[0].length; n++) {
            double l = Math.log1p(n);
            logSum += l * histBins[0][n];
            pixelCount += histBins[0][n];
        }
        double dw = 65536.;
        if (pixelCount > 0) {
            logAvg = Math.exp(logSum / pixelCount);
            dw = logAvg / 0.18;
        }
        int whitePixels = pixelCount / 100;
        int brighterPixels = 0;
        int bin = 0xffff;
        for (; bin > 0; bin--) {
            brighterPixels += histBins[0][bin];
            if (brighterPixels >= whitePixels) break;
        }
        double hcRatio = ((double) bin) / dw;
        white = (int) dw;
        highlightCompression = Math.log(hcRatio) / Math.log(2.0);
        highlightCompression = Math.min(MAX_AUTO_HLIGHT_COMP, Math.max(MIN_AUTO_HLIGHT_COMP, highlightCompression));
    }

    /**
     * Read the metadata of the raw file (using dcraw) and set fields of
     * this objecta based on that.
     @throws PhotovaultException if dcraw is not initialized properly.
     */
    private void readFileInfo() throws PhotovaultException {
        if (dcraw == null) {
            dcraw = new DCRawProcessWrapper();
        }
        Map values = null;
        try {
            values = dcraw.getFileInfo(f);
        } catch (IOException ex) {
            throw new PhotovaultException(ex.getMessage(), ex);
        }
        if (values.containsKey("Decodable with dcraw")) {
            if (values.get("Decodable with dcraw").equals("yes")) {
                validRawFile = true;
            } else {
                return;
            }
        }
        if (values.containsKey("Camera")) {
            camera = (String) values.get("Camera");
        }
        if (values.containsKey("ISO speed")) {
            String isoStr = (String) values.get("ISO speed");
            try {
                filmSpeed = Integer.parseInt(isoStr.trim());
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        if (values.containsKey("Shutter")) {
            String shutterStr = (String) values.get("Shutter");
            shutterStr = shutterStr.replaceAll("sec", "");
            String fraction[] = shutterStr.split("/");
            try {
                if (fraction.length == 1) {
                    shutterSpeed = Double.parseDouble(fraction[0].trim());
                } else {
                    shutterSpeed = Double.parseDouble(fraction[0].trim()) / Double.parseDouble(fraction[1].trim());
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        if (values.containsKey("Output size")) {
            String value = (String) values.get("Output size");
            String dims[] = value.split("x");
            if (dims.length == 2) {
                try {
                    width = Integer.parseInt(dims[0].trim());
                    height = Integer.parseInt(dims[1].trim());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (values.containsKey("Timestamp")) {
            String tsStr = (String) values.get("Timestamp");
            DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy", Locale.US);
            try {
                timestamp = df.parse(tsStr);
            } catch (ParseException ex) {
                log.error(ex.getMessage());
                ex.printStackTrace();
            }
        }
        if (values.containsKey("Embedded ICC profile")) {
            String str = (String) values.get("Embedded ICC profile");
            this.hasICCProfile = str.trim().equals("yes");
        }
        if (values.containsKey("Aperture")) {
            String str = (String) values.get("Aperture");
            int apertureStart = str.indexOf("f/") + 2;
            if (apertureStart >= 2 && apertureStart < str.length()) {
                try {
                    aperture = Double.parseDouble(str.substring(apertureStart));
                } catch (NumberFormatException ex) {
                    log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
        if (values.containsKey("Focal Length")) {
            String value = (String) values.get("Focal Length");
            int numberEnd = value.indexOf("mm");
            if (numberEnd >= 0) {
                value = value.substring(0, numberEnd).trim();
                try {
                    focalLength = Double.parseDouble(value);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (values.containsKey("Daylight multipliers")) {
            String value = (String) values.get("Daylight multipliers");
            String mstr[] = value.split(" ");
            daylightMultipliers = new double[mstr.length];
            for (int n = 0; n < mstr.length; n++) {
                try {
                    daylightMultipliers[n] = Double.parseDouble(mstr[n]);
                } catch (NumberFormatException ex) {
                    log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
        if (values.containsKey("Camera multipliers")) {
            String value = (String) values.get("Camera multipliers");
            String mstr[] = value.split(" ");
            cameraMultipliers = new double[mstr.length];
            for (int n = 0; n < mstr.length; n++) {
                try {
                    cameraMultipliers[n] = Double.parseDouble(mstr[n]);
                } catch (NumberFormatException ex) {
                    log.error(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        } else if (daylightMultipliers != null) {
            cameraMultipliers = new double[4];
            cameraMultipliers[0] = daylightMultipliers[0];
            cameraMultipliers[1] = daylightMultipliers[1];
            cameraMultipliers[2] = daylightMultipliers[2];
            cameraMultipliers[3] = daylightMultipliers[1];
        } else {
            validRawFile = false;
        }
    }

    /**
     Calculate the gamma correction lookup table using current exposure & white
     point settings.
     */
    private void createGammaLut() {
        double dw = white;
        double exposureMult = Math.pow(2, evCorr);
        for (int n = 0; n < gammaLut.length; n++) {
            double r = exposureMult * ((double) (n - black)) / dw;
            double whiteLum = Math.pow(2, highlightCompression);
            r = (r * (1 + (r / (whiteLum * whiteLum)))) / (1 + r);
            double val = (r <= 0.018) ? r * 4.5 : Math.pow(r, 0.45) * 1.099 - 0.099;
            if (val > 1.) {
                val = 1.;
            } else if (val < 0.) {
                val = 0.;
            }
            int intVal = (int) (val * 256.);
            if (intVal > 255) {
                intVal = 255;
            }
            gammaLut[n] = (byte) (intVal);
        }
    }

    private void applyGammaLut() {
        createGammaLut();
        LookupTableJAI jailut = new LookupTableJAI(gammaLut);
        if (correctedImage != null) {
            correctedImage.setParameter(jailut, 0);
        }
    }

    /**
     * Get the width of corrected image
     * @return Width in pixels
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get height of the converted image
     * @return Height in pixels
     */
    public int getHeight() {
        return height;
    }

    public byte[] getGammaLut() {
        if (gammaLut == null) {
            createGammaLut();
        }
        return (gammaLut != null) ? gammaLut.clone() : null;
    }

    static final double XYZ_to_RGB[][] = { { 3.24071, -0.969258, 0.0556352 }, { -1.53726, 1.87599, -0.203996 }, { -0.498571, 0.0415557, 1.05707 } };

    /**
     * Convert a color temperature to RGB value of an white patch illuminated
     * with light with that temperature
     * @param T The color temperature of illuminant (in Kelvin)
     * @return Patches RGB value as 3 doubles (RGB)
     */
    public double[] colorTempToRGB(double T) {
        int c;
        double xD, yD, X, Y, Z, max;
        double RGB[] = new double[3];
        if (T <= 4000) {
            xD = 0.27475e9 / (T * T * T) - 0.98598e6 / (T * T) + 1.17444e3 / T + 0.145986;
        } else if (T <= 7000) {
            xD = -4.6070e9 / (T * T * T) + 2.9678e6 / (T * T) + 0.09911e3 / T + 0.244063;
        } else {
            xD = -2.0064e9 / (T * T * T) + 1.9018e6 / (T * T) + 0.24748e3 / T + 0.237040;
        }
        yD = -3 * xD * xD + 2.87 * xD - 0.275;
        X = xD / yD;
        Y = 1;
        Z = (1 - xD - yD) / yD;
        max = 0;
        for (c = 0; c < 3; c++) {
            RGB[c] = X * XYZ_to_RGB[0][c] + Y * XYZ_to_RGB[1][c] + Z * XYZ_to_RGB[2][c];
            if (RGB[c] > max) max = RGB[c];
        }
        for (c = 0; c < 3; c++) RGB[c] = RGB[c] / max;
        return RGB;
    }

    /**
     Converts RGB multipliers to color balance
     @param rgb The color triplet of a white patch in the raw image
     @return Array of 2 doubles that contains temperature & green gain for the
     light source that has illuminated the patch.
     */
    double[] rgbToColorTemp(double rgb[]) {
        double Tmax;
        double Tmin;
        double testRGB[] = null;
        Tmin = 2000;
        Tmax = 12000;
        double T;
        for (T = (Tmax + Tmin) / 2; Tmax - Tmin > 10; T = (Tmax + Tmin) / 2) {
            testRGB = colorTempToRGB(T);
            if (testRGB[2] / testRGB[0] > rgb[2] / rgb[0]) Tmax = T; else Tmin = T;
        }
        double green = (testRGB[1] / testRGB[0]) / (rgb[1] / rgb[0]);
        double result[] = { T, green };
        return result;
    }

    /**
     Multipliers used to correct colors from the raw image loaded with dcraw to
     the colors specified by chanMultipliers. Currently, dcraw is instructed to 
     load the image always with channel multipliers recommended for daylight
     (5500K). So is should always be so that chanMultipliers = colorCorr * daylightMultipliers
     
     */
    double colorCorr[] = new double[] { 1.0, 1.0, 1.0 };

    /**
     * Set the color temperature to use when converting the image
     * @param T Color temperature (in Kelvin)
     */
    public void setColorTemp(double T) {
        ctemp = T;
        applyWbCorrection();
        double rgb[] = colorTempToRGB(T);
        chanMultipliers = new double[4];
        chanMultipliers[0] = daylightMultipliers[0] / rgb[0];
        chanMultipliers[1] = daylightMultipliers[1] / rgb[1] * greenGain;
        chanMultipliers[2] = daylightMultipliers[2] / rgb[2];
        chanMultipliers[3] = chanMultipliers[1];
        fireChangeEvent(new RawImageChangeEvent(this));
    }

    /**
     * Set the green gain to use when converting the image
     * @param g Green gain
     */
    public void setGreenGain(double g) {
        greenGain = g;
        applyWbCorrection();
        rawSettings = null;
        fireChangeEvent(new RawImageChangeEvent(this));
    }

    private void applyWbCorrection() {
        double rgb[] = colorTempToRGB(ctemp);
        colorCorr = new double[3];
        colorCorr[0] = 1.0 / rgb[0];
        colorCorr[1] = greenGain / rgb[1];
        colorCorr[2] = 1.0 / rgb[2];
        double colorCorrMat[][] = new double[][] { { colorCorr[0], 0.0, 0.0, 0.0 }, { 0.0, colorCorr[1], 0.0, 0.0 }, { 0.0, 0.0, colorCorr[2], 0.0 } };
        if (wbAdjustedRawImage != null) {
            wbAdjustedRawImage.setParameter(colorCorrMat, 0);
        }
    }

    /**
     * Get color temperature of the image
     * @return Color temperature (in Kelvin)
     */
    public double getColorTemp() {
        return ctemp;
    }

    public double getGreenGain() {
        return greenGain;
    }

    void calcCTemp() {
        if (chanMultipliers == null) {
            chanMultipliers = cameraMultipliers.clone();
        }
        double rgb[] = { daylightMultipliers[0] / chanMultipliers[0], daylightMultipliers[1] / chanMultipliers[1], daylightMultipliers[2] / chanMultipliers[2] };
        double ct[] = rgbToColorTemp(rgb);
        ctemp = ct[0];
        greenGain = ct[1];
    }

    /**
     Get the current conversion settings
     @return The conversion settings
     */
    public RawConversionSettings getRawSettings() {
        RawSettingsFactory f = new RawSettingsFactory(null);
        f.setDaylightMultipliers(daylightMultipliers);
        f.setRedGreenRation(chanMultipliers[0] / chanMultipliers[1]);
        f.setBlueGreenRatio(chanMultipliers[2] / chanMultipliers[1]);
        f.setBlack(black);
        f.setWhite(white);
        f.setEvCorr(evCorr);
        f.setHlightComp(highlightCompression);
        f.setUseEmbeddedProfile(hasICCProfile);
        f.setColorProfile(colorProfile);
        RawConversionSettings s = null;
        try {
            s = f.create();
        } catch (PhotovaultException ex) {
            log.error("Error while reacting raw settings object: " + ex.getMessage());
        }
        return s;
    }

    /**
     The last raw settings that were set to this image or <code>null</code> if they
     are no longer valid
     */
    RawConversionSettings rawSettings = null;

    /**
     Set the conversion settings to use
     @param s The new conversion settings
     */
    public void setRawSettings(RawConversionSettings s) {
        if (s == null) {
            log.error("null raw settings");
            return;
        }
        if (rawSettings != null && rawSettings.equals(s)) {
            return;
        }
        rawSettings = s;
        if (chanMultipliers == null || Math.abs(daylightMultipliers[0] - s.getDaylightRedGreenRatio()) > 0.001 || Math.abs(daylightMultipliers[2] - s.getDaylightBlueGreenRatio()) > 0.001) {
            daylightMultipliers = new double[3];
            daylightMultipliers[0] = s.getDaylightRedGreenRatio();
            daylightMultipliers[1] = 1.;
            daylightMultipliers[2] = s.getDaylightBlueGreenRatio();
            correctedImage = null;
            rawImage = null;
        }
        chanMultipliers = new double[4];
        chanMultipliers[0] = s.getRedGreenRatio();
        chanMultipliers[1] = 1.;
        chanMultipliers[2] = s.getBlueGreenRatio();
        chanMultipliers[3] = 1.;
        calcCTemp();
        applyWbCorrection();
        evCorr = s.getEvCorr();
        highlightCompression = s.getHighlightCompression();
        white = s.getWhite();
        black = s.getBlack();
        applyGammaLut();
        hasICCProfile = s.getUseEmbeddedICCProfile();
        colorProfile = s.getColorProfile();
        autoExposeRequested = false;
        fireChangeEvent(new RawImageChangeEvent(this));
    }
}
