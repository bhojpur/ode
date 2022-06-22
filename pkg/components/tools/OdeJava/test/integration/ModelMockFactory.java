package integration;

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

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import ode.formats.model.UnitsFactory;
import ode.units.UNITS;
import ode.ServerError;
import ode.api.ITypesPrx;
import ode.model.*;
import ode.model.enums.UnitsLength;
import ode.model.enums.UnitsTime;
import ode.gateway.model.DatasetData;
import ode.gateway.model.PlateAcquisitionData;
import ode.gateway.model.PlateData;
import ode.gateway.model.ProjectData;
import ode.gateway.model.ScreenData;

/**
 * Helper class.
 */
public class ModelMockFactory {

    /** The default width of an image. */
    public static final int WIDTH = 100;

    /** The default height of an image. */
    public static final int HEIGHT = 100;

    /** The basic formats tested. */
    public static final String[] FORMATS = { "jpeg", "png" };

    /** Identifies the laser light source. */
    public static final String LASER = Laser.class.getName();

    /** Identifies the filament light source. */
    public static final String FILAMENT = Filament.class.getName();

    /** Identifies the arc light source. */
    public static final String ARC = Arc.class.getName();

    /** Identifies the arc light source. */
    public static final String LIGHT_EMITTING_DIODE = LightEmittingDiode.class.getName();

    /** The possible sources of light. */
    public static final String[] LIGHT_SOURCES = { LASER, FILAMENT, ARC,
            LIGHT_EMITTING_DIODE };

    /** The default number of channels. */
    public static final int DEFAULT_CHANNELS_NUMBER = 3;

    /** The default size along the X-axis. */
    public static final int SIZE_X = 10;

    /** The default size along the Y-axis. */
    public static final int SIZE_Y = 10;

    /** The number of z-sections. */
    public static final int SIZE_Z = 10;

    /** The number of time points. */
    public static final int SIZE_T = 10;

    /** The dimension order for the pixels type. */
    public static final String XYZCT = ode.model.enums.DimensionOrderXYZCT.value;

    /** The unsigned int 16 pixels Type. */
    public static final String UINT16 = ode.model.enums.PixelsTypeuint16.value;

    /** The unsigned int 8 pixels Type. */
    public static final String UINT8 = ode.model.enums.PixelsTypeuint8.value;
    
    /** The bit pixels Type. */
    public static final String BIT =  ode.model.enums.PixelsTypebit.value;

    /** Helper reference to the <code>ITypes</code> service. */
    private ITypesPrx typesService;

    /** all {@link ExperimentType}s */
    private ImmutableList<ExperimentType> experimentTypes;

    private static Frequency hz(double d) {
        return new FrequencyI(d, UNITS.HERTZ);
    }

    private static ElectricPotential volt(double d) {
        return new ElectricPotentialI(d, UNITS.VOLT);
    }

    private static Power watt(double d) {
        return new PowerI(d, UNITS.WATT);
    }

    /**
     * Creates a new instance.
     *
     * @param typesService
     * @throws ServerError unexpected
     */
    public ModelMockFactory(ITypesPrx typesService) throws ServerError {
        this.typesService = typesService;
        getExperimentTypes();
    }

    /**
     * Note the experiment types from the pixels service.
     * @throws ServerError unexpected
     */
    public void getExperimentTypes() throws ServerError {
        final Builder<ExperimentType> builder = ImmutableList.builder();
        for (final IObject experimentType : typesService.allEnumerations(ExperimentType.class.getName())) {
            builder.add((ExperimentType) experimentType);
        }
        experimentTypes = builder.build();
    }

    // POJO
    /**
     * Creates a default dataset and returns it.
     *
     * @return See above.
     */
    public DatasetData simpleDatasetData() {
        DatasetData dd = new DatasetData();
        dd.setName("t1");
        dd.setDescription("t1");
        return dd;
    }

    /**
     * Creates a default project and returns it.
     *
     * @return See above.
     */
    public ProjectData simpleProjectData() {
        ProjectData data = new ProjectData();
        data.setName("project1");
        data.setDescription("project1");
        return data;
    }

    /**
     * Creates a default screen and returns it.
     *
     * @return See above.
     */
    public ScreenData simpleScreenData() {
        ScreenData data = new ScreenData();
        data.setName("screen name");
        data.setDescription("screen description");
        data.setProtocolDescription("Protocol description");
        data.setProtocolIdentifier("Protocol identifier");
        data.setReagentSetDescripion("Reagent description");
        data.setReagentSetIdentifier("Reagent identifier");
        return data;
    }

    /**
     * Creates a default plate and returns it.
     *
     * @return See above.
     */
    public PlateData simplePlateData() {
        PlateData data = new PlateData();
        data.setName("plate name");
        data.setDescription("plate name");
        data.setStatus("done");
        data.setExternalIdentifier("External Identifier");
        return data;
    }

    /**
     * Creates a default plate acquisition and returns it.
     *
     * @return See above.
     */
    public PlateAcquisitionData simplePlateAcquisitionData() {
        PlateAcquisitionData data = new PlateAcquisitionData();
        data.setName("plate acquisition name");
        data.setDescription("plate acquisition name");
        return data;
    }

    /**
     * Creates a default image and returns it.
     *
     * @return See above.
     */
    public Image simpleImage() {
        // prepare data
        Image img = new ImageI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueName = String.format("test-image:%s", uuidAsString);
        String uniqueDesc = String.format("test-desc:%s", uuidAsString);
        img.setName(ode.rtypes.rstring(uniqueName));
        img.setDescription(ode.rtypes.rstring(uniqueDesc));
        img.setSeries(ode.rtypes.rint(0));
        Format f = new FormatI();
        f.setValue(ode.rtypes.rstring("JPEG"));
        img.setFormat(f);
        return img;
    }

    /**
     * @return a default fileset
     */
    public Fileset simpleFileset() {
        Fileset fs = new FilesetI();
        fs.setTemplatePrefix(ode.rtypes.rstring("fileset-" + System.nanoTime() + "/"));
        return fs;
    }

    /**
     * @return a new folder
     */
    public Folder simpleFolder() {
        final Folder folder = new FolderI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueName = String.format("test-folder:%s", uuidAsString);
        String uniqueDesc = String.format("test-desc:%s", uuidAsString);
        folder.setName(ode.rtypes.rstring(uniqueName));
        folder.setDescription(ode.rtypes.rstring(uniqueDesc));
        return folder;
    }

    /**
     * Creates a default project and returns it.
     *
     * @return See above.
     */
    public Project simpleProject() {
        // prepare data
        final Project project = new ProjectI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueName = String.format("test-project:%s", uuidAsString);
        String uniqueDesc = String.format("test-desc:%s", uuidAsString);
        project.setName(ode.rtypes.rstring(uniqueName));
        project.setDescription(ode.rtypes.rstring(uniqueDesc));
        return project;
    }

    /**
     * Creates a default screen and returns it.
     *
     * @return See above.
     */
    public Screen simpleScreen() {
        // prepare data
        final Screen screen = new ScreenI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueName = String.format("test-screen:%s", uuidAsString);
        String uniqueDesc = String.format("test-desc:%s", uuidAsString);
        screen.setName(ode.rtypes.rstring(uniqueName));
        screen.setDescription(ode.rtypes.rstring(uniqueDesc));
        return screen;
    }

    /**
     * Creates a default dataset and returns it.
     *
     * @return See above.
     */
    public Dataset simpleDataset() {
        // prepare data
        final Dataset dataset = new DatasetI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueName = String.format("test-dataset:%s", uuidAsString);
        String uniqueDesc = String.format("test-desc:%s", uuidAsString);
        dataset.setName(ode.rtypes.rstring(uniqueName));
        dataset.setDescription(ode.rtypes.rstring(uniqueDesc));
        return dataset;
    }

    /**
     * Creates a default experiment and returns it.
     *
     * @return See above.
     */
    public Experiment simpleExperiment() throws ServerError {
        // prepare data
        final Experiment experiment = new ExperimentI();
        String uuidAsString = UUID.randomUUID().toString();
        String uniqueDesc = String.format("test-exp:%s", uuidAsString);
        experiment.setDescription(ode.rtypes.rstring(uniqueDesc));
        experiment.setType(experimentTypes.get(0));
        return experiment;
    }

    /**
     * Create a FileAnnotation with Original File.
     * @return the FileAnnotation
     */
    protected FileAnnotation createFileAnnotation() {
        FileAnnotation fileAnnotation = new FileAnnotationI();
        OriginalFile originalFile = createOriginalFile();
        fileAnnotation.setFile(originalFile);
        return fileAnnotation;
    }

    /**
     * Creates and returns an original file object.
     * @return See above.
     */
    public OriginalFile createOriginalFile() {
        OriginalFileI oFile = new OriginalFileI();
        oFile.setName(ode.rtypes.rstring("Test_" + UUID.randomUUID().toString()));
        oFile.setPath(ode.rtypes.rstring("/ode/"));
        oFile.setSize(ode.rtypes.rlong(0));
        oFile.setHash(ode.rtypes.rstring("pending"));
        oFile.setMimetype(ode.rtypes.rstring("application/octet-stream"));
        return oFile;
    }

    /**
     * Creates and returns a thumbnail.
     *
     * @return See above.
     */
    public Thumbnail createThumbnail() {
        ThumbnailI thumbnail = new ThumbnailI();
        thumbnail.setMimeType(ode.rtypes.rstring("application/octet-stream"));
        thumbnail.setSizeX(ode.rtypes.rint(10));
        thumbnail.setSizeY(ode.rtypes.rint(11));
        return thumbnail;
    }

    /**
     * Creates and returns a detector. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Detector createDetector() throws Exception {
        List<IObject> types = typesService
                .allEnumerations(DetectorType.class.getName());
        Detector detector = new DetectorI();
        detector.setAmplificationGain(ode.rtypes.rdouble(0));
        detector.setGain(ode.rtypes.rdouble(1));
        detector.setManufacturer(ode.rtypes.rstring("manufacturer"));
        detector.setModel(ode.rtypes.rstring("model"));
        detector.setSerialNumber(ode.rtypes.rstring("serial number"));
        detector.setLotNumber(ode.rtypes.rstring("lot number"));
        detector.setOffsetValue(ode.rtypes.rdouble(0));
        detector.setType((DetectorType) types.get(0));
        return detector;
    }

    /**
     * Creates an Optical Transfer Function object.
     *
     * @param filterSet
     *            The filter set linked to it.
     * @param objective
     *            The objective linked to it.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public OTF createOTF(FilterSet filterSet, Objective objective)
            throws Exception {
        List<IObject> types = typesService.allEnumerations(PixelsType.class
                .getName());
        OTF otf = new OTFI();
        otf.setFilterSet(filterSet);
        otf.setObjective(objective);
        otf.setPath(ode.rtypes.rstring("/ODE"));
        otf.setOpticalAxisAveraged(ode.rtypes.rbool(true));
        otf.setPixelsType((PixelsType) types.get(0));
        otf.setSizeX(ode.rtypes.rint(10));
        otf.setSizeY(ode.rtypes.rint(10));
        return otf;
    }

    /**
     * Creates and returns a filter. This will have to be linked to an
     * instrument.
     *
     * @param cutIn
     *            The cut in value.
     * @param cutOut
     *            The cut out value.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Filter createFilter(int cutIn, int cutOut) throws Exception {
        List<IObject> types = typesService.allEnumerations(FilterType.class
                .getName());
        Filter filter = new FilterI();
        filter.setLotNumber(ode.rtypes.rstring("lot number"));
        filter.setSerialNumber(ode.rtypes.rstring("serial number"));
        filter.setManufacturer(ode.rtypes.rstring("manufacturer"));
        filter.setModel(ode.rtypes.rstring("model"));
        filter.setType((FilterType) types.get(0));

        TransmittanceRangeI transmittance = new TransmittanceRangeI();
        transmittance.setCutIn(new LengthI(cutIn, UnitsFactory.TransmittanceRange_CutIn));
        transmittance.setCutOut(new LengthI(cutOut, UnitsFactory.TransmittanceRange_CutOut));
        transmittance.setCutInTolerance(new LengthI(1, UnitsFactory.TransmittanceRange_CutInTolerance));
        transmittance.setCutOutTolerance(new LengthI(1, UnitsFactory.TransmittanceRange_CutOutTolerance));
        filter.setTransmittanceRange(transmittance);
        return filter;
    }

    /**
     * Creates a basic filter set.
     *
     * @return See above.
     */
    public FilterSet createFilterSet() {
        FilterSet set = new FilterSetI();
        set.setLotNumber(ode.rtypes.rstring("lot number"));
        set.setManufacturer(ode.rtypes.rstring("manufacturer"));
        set.setModel(ode.rtypes.rstring("model"));
        set.setSerialNumber(ode.rtypes.rstring("serial number"));
        return set;
    }

    /**
     * Creates and returns a dichroic. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Dichroic createDichroic() throws Exception {
        Dichroic dichroic = new DichroicI();
        dichroic.setManufacturer(ode.rtypes.rstring("manufacturer"));
        dichroic.setModel(ode.rtypes.rstring("model"));
        dichroic.setLotNumber(ode.rtypes.rstring("lot number"));
        dichroic.setSerialNumber(ode.rtypes.rstring("serial number"));
        return dichroic;
    }

    /**
     * Creates and returns an objective. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Objective createObjective() throws Exception {
        Objective objective = new ObjectiveI();
        objective.setManufacturer(ode.rtypes.rstring("manufacturer"));
        objective.setModel(ode.rtypes.rstring("model"));
        objective.setSerialNumber(ode.rtypes.rstring("serial number"));
        objective.setLotNumber(ode.rtypes.rstring("lot number"));
        objective.setCalibratedMagnification(ode.rtypes.rdouble(1));
        // correction
        List<IObject> types = typesService.allEnumerations(Correction.class
                .getName());
        objective.setCorrection((Correction) types.get(0));
        // immersion
        types = typesService.allEnumerations(Immersion.class.getName());
        objective.setImmersion((Immersion) types.get(0));

        objective.setIris(ode.rtypes.rbool(true));
        objective.setLensNA(ode.rtypes.rdouble(0.5));
        objective.setNominalMagnification(ode.rtypes.rdouble(1));
        objective.setWorkingDistance(new LengthI(1, UnitsFactory.Objective_WorkingDistance));
        return objective;
    }

    /**
     * Creates and returns the settings of the specified objective.
     *
     * @param objective
     *            The objective to link the settings to.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public ObjectiveSettings createObjectiveSettings(Objective objective)
            throws Exception {
        List<IObject> types = typesService.allEnumerations(Medium.class
                .getName());
        ObjectiveSettings settings = new ObjectiveSettingsI();
        settings.setCorrectionCollar(ode.rtypes.rdouble(1));
        settings.setRefractiveIndex(ode.rtypes.rdouble(1));
        settings.setMedium((Medium) types.get(0));
        settings.setObjective(objective);
        return settings;
    }

    /**
     * Creates and returns the stage label.
     *
     * @return See above.
     */
    public StageLabel createStageLabel() {
        StageLabel label = new StageLabelI();
        label.setName(ode.rtypes.rstring("label"));
        label.setPositionX(new LengthI(1, UnitsFactory.StageLabel_X));
        label.setPositionY(new LengthI(1, UnitsFactory.StageLabel_Y));
        label.setPositionZ(new LengthI(1, UnitsFactory.StageLabel_Z));
        return label;
    }

    /**
     * Creates and returns the environment.
     *
     * @return See above.
     */
    public ImagingEnvironment createImageEnvironment() {
        ImagingEnvironment env = new ImagingEnvironmentI();
        env.setAirPressure(new PressureI(1, UnitsFactory.ImagingEnvironment_AirPressure));
        env.setCo2percent(ode.rtypes.rdouble(0.5));
        env.setHumidity(ode.rtypes.rdouble(0.5));
        env.setTemperature(new TemperatureI(1, UnitsFactory.ImagingEnvironment_Temperature));
        return env;
    }

    /**
     * Creates and returns the settings of the specified detector.
     *
     * @param detector
     *            The detector to link the settings to.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    DetectorSettings createDetectorSettings(Detector detector) throws Exception {
        List<IObject> types = typesService.allEnumerations(Binning.class
                .getName());
        DetectorSettings settings = new DetectorSettingsI();
        settings.setBinning((Binning) types.get(0));
        settings.setDetector(detector);
        settings.setGain(ode.rtypes.rdouble(1));
        settings.setOffsetValue(ode.rtypes.rdouble(1));
        settings.setReadOutRate(hz(1));
        settings.setVoltage(volt(1));
        return settings;
    }

    /**
     * Creates and returns the settings of the specified source of light.
     *
     * @param light
     *            The light to link the settings to.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public LightSettings createLightSettings(LightSource light)
            throws Exception {
        List<IObject> types = typesService.allEnumerations(MicrobeamManipulationType.class.getName());
        LightSettings settings = new LightSettingsI();
        settings.setLightSource(light);
        settings.setAttenuation(ode.rtypes.rdouble(1));
        MicrobeamManipulation mm = new MicrobeamManipulationI();
        mm.setType((MicrobeamManipulationType) types.get(0));
        mm.setExperiment(simpleExperiment());
        // settings.setMicrobeamManipulation(mm);
        settings.setWavelength(new LengthI(500.1, UnitsFactory.LightSourceSettings_Wavelength));
        return settings;
    }

    /**
     * Creates a light path.
     *
     * @param emissionFilter
     *            The emission filter or <code>null</code>.
     * @param dichroic
     *            The dichroic or <code>null</code>.
     * @param excitationFilter
     *            The excitation filter or <code>null</code>.
     * @return See above.
     */
    public LightPath createLightPath(Filter emissionFilter, Dichroic dichroic,
            Filter excitationFilter) {
        LightPath path = new LightPathI();
        if (dichroic != null)
            path.setDichroic(dichroic);
        return path;
    }

    /**
     * Creates and returns a filament. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Filament createFilament() throws Exception {
        List<IObject> types = typesService.allEnumerations(FilamentType.class.getName());
        Filament filament = new FilamentI();
        filament.setManufacturer(ode.rtypes.rstring("manufacturer"));
        filament.setModel(ode.rtypes.rstring("model"));
        filament.setPower(watt(1));
        filament.setSerialNumber(ode.rtypes.rstring("serial number"));
        filament.setLotNumber(ode.rtypes.rstring("lot number"));
        filament.setType((FilamentType) types.get(0));
        return filament;
    }

    /**
     * Creates and returns a filament. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Arc createArc() throws Exception {
        List<IObject> types = typesService.allEnumerations(ArcType.class
                .getName());
        Arc arc = new ArcI();
        arc.setManufacturer(ode.rtypes.rstring("manufacturer"));
        arc.setModel(ode.rtypes.rstring("model"));
        arc.setPower(watt(1));
        arc.setSerialNumber(ode.rtypes.rstring("serial number"));
        arc.setLotNumber(ode.rtypes.rstring("lot number"));
        arc.setType((ArcType) types.get(0));
        return arc;
    }

    /**
     * Creates and returns a filament. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public LightEmittingDiode createLightEmittingDiode() throws Exception {
        LightEmittingDiode light = new LightEmittingDiodeI();
        light.setManufacturer(ode.rtypes.rstring("manufacturer"));
        light.setModel(ode.rtypes.rstring("model"));
        light.setPower(watt(1));
        light.setSerialNumber(ode.rtypes.rstring("serial number"));
        light.setLotNumber(ode.rtypes.rstring("lot number"));
        return light;
    }

    /**
     * Creates and returns a laser. This will have to be linked to an
     * instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Laser createLaser() throws Exception {
        Laser laser = new LaserI();
        laser.setManufacturer(ode.rtypes.rstring("manufacturer"));
        laser.setModel(ode.rtypes.rstring("model"));
        laser.setLotNumber(ode.rtypes.rstring("lot number"));
        laser.setSerialNumber(ode.rtypes.rstring("serial number"));
        // type
        List<IObject> types = typesService.allEnumerations(LaserType.class
                .getName());
        laser.setType((LaserType) types.get(0));
        // laser medium
        types = typesService.allEnumerations(LaserMedium.class.getName());
        laser.setLaserMedium((LaserMedium) types.get(0));

        // pulse
        types = typesService.allEnumerations(Pulse.class.getName());
        laser.setPulse((Pulse) types.get(0));

        laser.setFrequencyMultiplication(ode.rtypes.rint(1));
        laser.setPockelCell(ode.rtypes.rbool(false));
        laser.setTuneable(ode.rtypes.rbool(true));
        laser.setWavelength(new LengthI(500.1, UnitsFactory.Laser_Wavelength));
        laser.setPower(watt(1));
        laser.setRepetitionRate(hz(1));
        return laser;
    }

    /**
     * Creates and returns an instrument.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Instrument createInstrument() throws Exception {
        List<IObject> types = typesService.allEnumerations(MicroscopeType.class.getName());
        Instrument instrument = new InstrumentI();
        MicroscopeI microscope = new MicroscopeI();
        microscope.setManufacturer(ode.rtypes.rstring("manufacturer"));
        microscope.setModel(ode.rtypes.rstring("model"));
        microscope.setSerialNumber(ode.rtypes.rstring("serial number"));
        microscope.setLotNumber(ode.rtypes.rstring("lot number"));
        microscope.setType((MicroscopeType) types.get(0));
        instrument.setMicroscope(microscope);
        return instrument;
    }

    /**
     * Creates and returns an instrument. The creation using the
     * <code>add*</code> methods has been tested i.e. addDectector, etc.
     *
     * @param light
     *            The type of light source.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Instrument createInstrument(String light) throws Exception {
        return createInstrument(light, null);
    }

    /**
     * Creates and returns an instrument. The creation using the
     * <code>add*</code> methods has been tested i.e. addDectector, etc.
     *
     * @param light
     *            The type of light source.
     * @param pump
     *            Pass the type of light source of the pump or <code>null</code>
     *            .
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Instrument createInstrument(String light, String pump)
            throws Exception {
        Instrument instrument = createInstrument();
        instrument.addDetector(createDetector());
        instrument.addFilter(createFilter(500, 560));
        Dichroic dichroic = createDichroic();
        instrument.addDichroic(dichroic);
        Objective objective = createObjective();
        FilterSet filterSet = createFilterSet();
        instrument.addObjective(objective);
        instrument.addFilterSet(filterSet);
        instrument.addOTF(createOTF(filterSet, objective));
        if (LASER.equals(light)) {
            Laser laser = createLaser();
            instrument.addLightSource(laser);
            if (pump != null) {
                LightSource ls = null;
                if (LASER.equals(pump))
                    ls = createLaser();
                else if (FILAMENT.equals(pump))
                    ls = createArc();
                else if (ARC.equals(pump))
                    ls = createFilament();
                if (ls != null) {
                    instrument.addLightSource(ls);
                    laser.setPump(ls);
                }
            }
        } else if (FILAMENT.equals(light))
            instrument.addLightSource(createFilament());
        else if (ARC.equals(light))
            instrument.addLightSource(createArc());
        else if (LIGHT_EMITTING_DIODE.equals(light))
            instrument.addLightSource(createLightEmittingDiode());
        return instrument;
    }

    /**
     * Creates a plane info object.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public PlaneInfo createPlaneInfo() throws Exception {
        return createPlaneInfo(0, 0, 0);
    }

    /**
     * Creates a plane info object.
     *
     * @param z
     *            The selected z-section.
     * @param t
     *            The selected time-point.
     * @param c
     *            The selected channel.
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public PlaneInfo createPlaneInfo(int z, int t, int c) throws Exception {
        PlaneInfo planeInfo = new PlaneInfoI();
        planeInfo.setTheZ(ode.rtypes.rint(z));
        planeInfo.setTheC(ode.rtypes.rint(c));
        planeInfo.setTheT(ode.rtypes.rint(t));

        UnitsTime seconds = UnitsTime.SECOND;
        Time deltaT = new TimeI();
        deltaT.setValue(0.5);
        deltaT.setUnit(seconds);
        planeInfo.setDeltaT(deltaT);
        return planeInfo;
    }

    /**
     * Creates a pixels object.
     *
     * @param sizeX
     *            The size along the X-axis.
     * @param sizeY
     *            The size along the Y-axis.
     * @param sizeZ
     *            The number of Z-sections.
     * @param sizeT
     *            The number of time-points.
     * @param sizeC
     *            The number of channels.
     * @param pxType
     *            The pixels type (e.g. unit16)
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Pixels createPixels(int sizeX, int sizeY, int sizeZ, int sizeT,
            int sizeC, String pxType) throws Exception {
        List<IObject> types = typesService.allEnumerations(PixelsType.class
                .getName());
        Iterator<IObject> i = types.iterator();
        PixelsType object;
        PixelsType type = null;
        while (i.hasNext()) {
            object = (PixelsType) i.next();
            if (pxType.equals(object.getValue().getValue())) {
                type = object;
                break;
            }
        }
        if (type == null)
            type = (PixelsType) types.get(0);
        types = typesService.allEnumerations(DimensionOrder.class.getName());
        i = types.iterator();
        DimensionOrder o;
        DimensionOrder order = null;
        while (i.hasNext()) {
            o = (DimensionOrder) i.next();
            if (XYZCT.equals(o.getValue().getValue())) {
                order = o;
                break;
            }
        }
        if (order == null)
            order = (DimensionOrder) types.get(0);

        UnitsLength mm = UnitsLength.MILLIMETER;
        Length mm1 = new LengthI();
        mm1.setValue(1.0);
        mm1.setUnit(mm);

        Pixels pixels = new PixelsI();
        pixels.setPhysicalSizeX(mm1);
        pixels.setPhysicalSizeY(mm1);
        pixels.setPhysicalSizeZ(mm1);
        pixels.setSizeX(ode.rtypes.rint(sizeX));
        pixels.setSizeY(ode.rtypes.rint(sizeY));
        pixels.setSizeZ(ode.rtypes.rint(sizeZ));
        pixels.setSizeT(ode.rtypes.rint(sizeT));
        pixels.setSizeC(ode.rtypes.rint(sizeC));
        pixels.setSha1(ode.rtypes.rstring("Pending..."));
        pixels.setPixelsType(type);
        pixels.setDimensionOrder(order);
        List<Channel> channels = new ArrayList<Channel>();
        for (int j = 0; j < sizeC; j++) {
            channels.add(createChannel(j));
        }
        pixels.addAllChannelSet(channels);

        for (int z = 0; z < sizeZ; z++) {
            for (int t = 0; t < sizeT; t++) {
                for (int c = 0; c < sizeC; c++) {
                    pixels.addPlaneInfo(createPlaneInfo(z, t, c));
                }
            }
        }
        return pixels;
    }

    /**
     * Creates a channel.
     *
     * @param w
     *            The wavelength in nanometers.
     * @return See Above.
     * @throws Exception
     */
    public Channel createChannel(int w) throws Exception {
        Channel channel = new ChannelI();
        LogicalChannel lc = new LogicalChannelI();
        lc.setEmissionWave(new LengthI(200.1, UnitsFactory.Channel_EmissionWavelength));
        List<IObject> types = typesService.allEnumerations(ContrastMethod.class.getName());
        ContrastMethod cm = (ContrastMethod) types.get(0);

        types = typesService.allEnumerations(Illumination.class.getName());
        Illumination illumination = (Illumination) types.get(0);
        types = typesService.allEnumerations(AcquisitionMode.class
                .getName());
        AcquisitionMode mode = (AcquisitionMode) types.get(0);
        lc.setContrastMethod(cm);
        lc.setIllumination(illumination);
        lc.setMode(mode);
        channel.setLogicalChannel(lc);
        StatsInfo info = new StatsInfoI();
        info.setGlobalMin(ode.rtypes.rdouble(0.0));
        info.setGlobalMax(ode.rtypes.rdouble(1.0));
        channel.setStatsInfo(info);
        return channel;
    }

    /**
     * Creates a default pixels set.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Pixels createPixels() throws Exception {
        return createPixels(SIZE_X, SIZE_Y, SIZE_Z, SIZE_T,
                DEFAULT_CHANNELS_NUMBER, UINT16);
    }

    /**
     * Creates an image. This method has been tested in
     * <code>PixelsServiceTest</code>.
     *
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Image createImage() throws Exception {
        return createImage(1, 1, 1, 1, 1, UINT16);
    }

    /**
     * Creates an image.
     *
     * @param sizeX
     *            The size along the X-axis.
     * @param sizeY
     *            The size along the Y-axis.
     * @param sizeZ
     *            The number of Z-sections.
     * @param sizeT
     *            The number of time-points.
     * @param sizeC
     *            The number of channels.
     * @param pxType
     *            The pixels type (e.g. unit16)
     * @return See above.
     * @throws Exception
     *             Thrown if an error occurred.
     */
    public Image createImage(int sizeX, int sizeY, int sizeZ, int sizeT,
            int sizeC, String pxType) throws Exception {
        Image image = simpleImage();
        Pixels pixels = createPixels(sizeX, sizeY, sizeZ, sizeT, sizeC, pxType);
        image.addPixels(pixels);
        return image;
    }

    /**
     * Creates a plate.
     *
     * @param rows
     *            The number of rows.
     * @param columns
     *            The number of columns.
     * @param fields
     *            The number of fields.
     * @param numberOfPlateAcquisition
     *            The number of plate acquisitions.
     * @param fullImage
     *            Pass <code>true</code> to add image with pixels,
     *            <code>false</code> to create a simple image.
     * @return See above.
     */
    public Plate createPlate(int rows, int columns, int fields,
            int numberOfPlateAcquisition, boolean fullImage) throws Exception {
        return fullImage ?
                createPlate(rows, columns, fields, numberOfPlateAcquisition, 1, 1, 1, 1, 1) :
                createPlate(rows, columns, fields, numberOfPlateAcquisition, 0, 0, 0, 0, 0);
    }

    /**
     * Creates a plate.
     *
     * @param rows
     *            The number of rows.
     * @param columns
     *            The number of columns.
     * @param fields
     *            The number of fields.
     * @param numberOfPlateAcquisition
     *            The number of plate acquisitions.
     * @param sizeX
     *            The size along the X-axis.
     * @param sizeY
     *            The size along the Y-axis.
     * @param sizeZ
     *            The number of Z-sections.
     * @param sizeT
     *            The number of time-points.
     * @param sizeC
     *            The number of channels.
     * @return See above.
     */
    public Plate createPlate(int rows, int columns, int fields,
            int numberOfPlateAcquisition, int sizeX, int sizeY, int sizeZ, int sizeT,
            int sizeC) throws Exception {
        if (numberOfPlateAcquisition < 0)
            numberOfPlateAcquisition = 0;
        final boolean fullImage = sizeX > 0 && sizeY > 0 && sizeZ > 0 && sizeT > 0 && sizeC > 0;
        Plate p = new PlateI();
        p.setRows(ode.rtypes.rint(rows));
        p.setColumns(ode.rtypes.rint(columns));
        p.setName(ode.rtypes.rstring("plate name"));
        p.setDescription(ode.rtypes.rstring("plate description"));
        p.setStatus(ode.rtypes.rstring("plate status"));
        p.setExternalIdentifier(ode.rtypes.rstring("external identifier"));
        // now make wells
        Well well;
        WellSample sample;
        List<PlateAcquisition> pas = new ArrayList<PlateAcquisition>();
        PlateAcquisition pa;
        for (int i = 0; i < numberOfPlateAcquisition; i++) {
            pa = new PlateAcquisitionI();
            pa.setName(ode.rtypes.rstring("plate acquisition"));
            pa.setPlate(p);
            pas.add(pa);
        }
        Iterator<PlateAcquisition> i;
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                well = new WellI();
                well.setRow(ode.rtypes.rint(row));
                well.setColumn(ode.rtypes.rint(column));
                if (pas.size() == 0) {
                    for (int field = 0; field < fields; field++) {
                        sample = new WellSampleI();
                        if (fullImage)
                            sample.setImage(createImage(sizeX, sizeY, sizeZ, sizeT, sizeC, UINT16));
                        else
                            sample.setImage(simpleImage());
                        well.addWellSample(sample);
                    }
                } else {
                    i = pas.iterator();
                    while (i.hasNext()) {
                        pa = i.next();
                        for (int field = 0; field < fields; field++) {
                            sample = new WellSampleI();
                            if (fullImage)
                                sample.setImage(createImage(sizeX, sizeY, sizeZ, sizeT, sizeC, UINT16));
                            else
                                sample.setImage(simpleImage());
                            well.addWellSample(sample);
                            pa.addWellSample(sample);
                        }
                    }
                }

                p.addWell(well);
            }
        }
        return p;
    }

    /**
     * Creates a basic plate and links the wells to the passed reagent.
     *
     * @param rows
     *            The number of rows.
     * @param columns
     *            The number of columns.
     * @param fields
     *            The number of fields.
     * @param r
     *            The reagent.
     * @return See above.
     */
    public Plate createPlateWithReagent(int rows, int columns, int fields,
            Reagent r) {
        Plate p = new PlateI();
        p.setRows(ode.rtypes.rint(rows));
        p.setColumns(ode.rtypes.rint(columns));
        p.setName(ode.rtypes.rstring("plate"));
        // now make wells
        Well well;
        WellSample sample;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                well = new WellI();
                well.setRow(ode.rtypes.rint(row));
                well.setColumn(ode.rtypes.rint(column));
                well.linkReagent(r);
                for (int field = 0; field < fields; field++) {
                    sample = new WellSampleI();
                    sample.setImage(simpleImage());
                    well.addWellSample(sample);
                }
                p.addWell(well);
            }
        }
        return p;
    }

    /**
     * Returns the reagent.
     *
     * @return See above.
     */
    public Reagent createReagent() {
        Reagent reagent = new ReagentI();
        reagent.setDescription(ode.rtypes.rstring("Reagent Description"));
        reagent.setName(ode.rtypes.rstring("Reagent Name"));
        reagent.setReagentIdentifier(ode.rtypes.rstring("Reagent Identifier"));
        return reagent;
    }

    /**
     * Returns an Image with a Roi and one Rectangle attached.
     */
    public Image createImageWithRoi() throws Exception {
        Roi roi = new RoiI();
        roi.addShape(new RectangleI());
        Image image = createImage();
        image.addRoi(roi);
        return image;
    }

    // imaging
    /**
     * Creates an image file of the specified format.
     *
     * @param file
     *            The file where to write the image.
     * @param format
     *            One of the follow types: jpeg, png.
     * @throws Exception
     *             Thrown if an error occurred while encoding the image.
     */
    public void createImageFile(File file, String format) throws Exception {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
        ImageWriter writer = writers.next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(file);
        writer.setOutput(ios);
        writer.write(new BufferedImage(WIDTH, HEIGHT,
                BufferedImage.TYPE_INT_RGB));
        ios.close();
    }

    /**
     * For a given IAnnotated type
     *
     * @param o
     * @param a
     */
    public IObject createAnnotationLink(IObject o, Annotation a)
            throws Exception {
        String name;
        if (o instanceof Annotation) {
            name = "ode.model.AnnotationAnnotationLinkI";
        } else {
            name = o.getClass().getSimpleName();
            name = name.substring(0, name.length() - 1);
            name = "ode.model." + name + "AnnotationLinkI";
        }
        Class<?> linkClass = Class.forName(name);
        IObject link = (IObject) linkClass.newInstance();
        Method linkMethod = null;
        for (Method m : linkClass.getMethods()) {
            if ("link".equals(m.getName())) {
                linkMethod = m;
                break;
            }
        }
        linkMethod.invoke(link, o, a, null); // Last is Ice.Current;
        return link;
    }
}
