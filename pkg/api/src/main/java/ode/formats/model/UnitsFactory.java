package ode.formats.model;

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

import ode.model.units.BigResult;
import ode.units.unit.Unit;
import ode.model.ElectricPotential;
import ode.model.ElectricPotentialI;
import ode.model.Frequency;
import ode.model.FrequencyI;
import ode.model.Length;
import ode.model.LengthI;
import ode.model.Power;
import ode.model.PowerI;
import ode.model.Pressure;
import ode.model.PressureI;
import ode.model.Temperature;
import ode.model.TemperatureI;
import ode.model.Time;
import ode.model.TimeI;
import ode.model.enums.UnitsElectricPotential;
import ode.model.enums.UnitsFrequency;
import ode.model.enums.UnitsLength;
import ode.model.enums.UnitsPower;
import ode.model.enums.UnitsPressure;
import ode.model.enums.UnitsTemperature;
import ode.model.enums.UnitsTime;


/**
 * Utility class to generate and convert unit objects.
 *
 * Be especially careful when using methods which take a string
 * since there are 2 types of enumerations, CODE-based and
 * SYMBOL-based.
 */
public class UnitsFactory {

    //
    // ElectricPotential
    //

  public static ode.xml.model.enums.UnitsElectricPotential
      makeElectricPotentialUnitXML(String unit) {
    return ElectricPotentialI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.ElectricPotential
      makeElectricPotentialXML(double d, String unit) {
    return ElectricPotentialI.makeXMLQuantity(d, unit);
  }

  public static ElectricPotential makeElectricPotential(
      double d, Unit<ode.units.quantity.ElectricPotential> unit) {
    return new ElectricPotentialI(d, unit);
  }

  public static ElectricPotential makeElectricPotential(
      double d, UnitsElectricPotential unit) {
    return new ElectricPotentialI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link ElectricPotential} to a Bhojpur ODE
   * ElectricPotential. A null will be returned if the input is null.
   */
  public static ElectricPotential convertElectricPotential(
      ode.units.quantity.ElectricPotential value) {
    if (value == null)
      return null;
    String internal = xmlElectricPotentialEnumToODE(value.unit().getSymbol());
    UnitsElectricPotential ul = UnitsElectricPotential.valueOf(internal);
    return new ode.model.ElectricPotentialI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.ElectricPotential convertElectricPotential(
      ElectricPotential t) {
    return ElectricPotentialI.convert(t);
  }

  public static ElectricPotential convertElectricPotential(
      ElectricPotential value, Unit<ode.units.quantity.ElectricPotential> ul
  ) throws BigResult {
    return convertElectricPotentialXML(value, ul.getSymbol());
  }

  public static ElectricPotential convertElectricPotentialXML(
      ElectricPotential value, String xml) throws BigResult {
    String ode = xmlElectricPotentialEnumToODE(xml);
    return new ElectricPotentialI(value, ode);
  }

  public static String xmlElectricPotentialEnumToODE(
      Unit<ode.units.quantity.ElectricPotential> xml) {
    return ode.model.enums.UnitsElectricPotential.bySymbol(
        xml.getSymbol()).toString();
  }

  public static String xmlElectricPotentialEnumToODE(String xml) {
    return ode.model.enums.UnitsElectricPotential.bySymbol(xml).toString();
  }

  //
  // Frequency
  //

  public static ode.xml.model.enums.UnitsFrequency makeFrequencyUnitXML(
      String unit) {
    return FrequencyI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Frequency makeFrequencyXML(
      double d, String unit) {
    return FrequencyI.makeXMLQuantity(d, unit);
  }

  public static Frequency makeFrequency(
      double d, Unit<ode.units.quantity.Frequency> unit) {
    return new FrequencyI(d, unit);
  }

  public static Frequency makeFrequency(double d, UnitsFrequency unit) {
    return new FrequencyI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Frequency} to a Bhojpur ODE Frequency. A
   * null will be returned if the input is null.
   */
  public static Frequency convertFrequency(ode.units.quantity.Frequency value) {
    if (value == null)
      return null;
    String internal = xmlFrequencyEnumToODE(value.unit().getSymbol());
    UnitsFrequency ul = UnitsFrequency.valueOf(internal);
    return new ode.model.FrequencyI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Frequency convertFrequency(Frequency t) {
    return FrequencyI.convert(t);
  }

  public static Frequency convertFrequency(
      Frequency value, Unit<ode.units.quantity.Frequency> ul) throws BigResult {
    return convertFrequencyXML(value, ul.getSymbol());
  }

  public static Frequency convertFrequencyXML(Frequency value, String xml)
      throws BigResult {
    String ode = xmlFrequencyEnumToODE(xml);
    return new FrequencyI(value, ode);
  }

  public static String xmlFrequencyEnumToODE(
      Unit<ode.units.quantity.Frequency> xml) {
    return ode.model.enums.UnitsFrequency.bySymbol(xml.getSymbol()).toString();
  }

  public static String xmlFrequencyEnumToODE(String xml) {
    return ode.model.enums.UnitsFrequency.bySymbol(xml).toString();
  }

  //
  // Length
  //

  public static ode.xml.model.enums.UnitsLength makeLengthUnitXML(String unit) {
    return LengthI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Length makeLengthXML(double d, String unit) {
    return LengthI.makeXMLQuantity(d, unit);
  }

  public static Length makeLength(
      double d, Unit<ode.units.quantity.Length> unit) {
    return new LengthI(d, unit);
  }

  public static Length makeLength(double d, UnitsLength unit) {
    return new LengthI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Length} to a Bhojpur ODE Length. A null will be
   * returned if the input is null.
   */
  public static Length convertLength(ode.units.quantity.Length value) {
    if (value == null)
      return null;
    String internal = xmlLengthEnumToODE(value.unit().getSymbol());
    UnitsLength ul = UnitsLength.valueOf(internal);
    return new ode.model.LengthI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Length convertLength(Length t) {
    return LengthI.convert(t);
  }

  public static Length convertLength(
      Length value, Unit<ode.units.quantity.Length> ul) throws BigResult {
    return convertLengthXML(value, ul.getSymbol());
  }

  public static Length convertLengthXML(Length value, String xml)
      throws BigResult {
    String ode = xmlLengthEnumToODE(xml);
    return new LengthI(value, ode);
  }

  public static String xmlLengthEnumToODE(
      Unit<ode.units.quantity.Length> xml) {
    return ode.model.enums.UnitsLength.bySymbol(xml.getSymbol()).toString();
  }

  public static String xmlLengthEnumToODE(String xml) {
    return ode.model.enums.UnitsLength.bySymbol(xml).toString();
  }

  //
  // Power
  //

  public static ode.xml.model.enums.UnitsPower makePowerUnitXML(String unit) {
    return PowerI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Power makePowerXML(double d, String unit) {
    return PowerI.makeXMLQuantity(d, unit);
  }

  public static Power makePower(
      double d, Unit<ode.units.quantity.Power> unit) {
    return new PowerI(d, unit);
  }

  public static Power makePower(double d, UnitsPower unit) {
    return new PowerI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Power} to a Bhojpur ODE Power. A null
   * will be returned if the input is null.
   */
  public static Power convertPower(ode.units.quantity.Power value) {
    if (value == null)
      return null;
    String internal = xmlPowerEnumToODE(value.unit().getSymbol());
    UnitsPower ul = UnitsPower.valueOf(internal);
    return new ode.model.PowerI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Power convertPower(Power t) {
    return PowerI.convert(t);
  }

  public static Power convertPower(
      Power value, Unit<ode.units.quantity.Power> ul) throws BigResult {
    return convertPowerXML(value, ul.getSymbol());
  }

  public static Power convertPowerXML(Power value, String xml)
    throws BigResult {
    String ode = xmlPowerEnumToODE(xml);
    return new PowerI(value, ode);
  }

  public static String xmlPowerEnumToODE(Unit<ode.units.quantity.Power> xml) {
    return ode.model.enums.UnitsPower.bySymbol(xml.getSymbol()).toString();
  }

  public static String xmlPowerEnumToODE(String xml) {
    return ode.model.enums.UnitsPower.bySymbol(xml).toString();
  }

    //
    // Pressure
    //

  public static ode.xml.model.enums.UnitsPressure makePressureUnitXML(
      String unit) {
    return PressureI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Pressure makePressureXML(
      double d, String unit) {
    return PressureI.makeXMLQuantity(d, unit);
  }

  public static Pressure makePressure(
      double d, Unit<ode.units.quantity.Pressure> unit) {
    return new PressureI(d, unit);
  }

  public static Pressure makePressure(double d, UnitsPressure unit) {
    return new PressureI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Pressure} to a Bhojpur ODE Pressure. A null will be
   * returned if the input is null.
   */
  public static Pressure convertPressure(ode.units.quantity.Pressure value) {
    if (value == null)
      return null;
    String internal = xmlPressureEnumToODE(value.unit().getSymbol());
    UnitsPressure ul = UnitsPressure.valueOf(internal);
    return new ode.model.PressureI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Pressure convertPressure(Pressure t) {
    return PressureI.convert(t);
  }

  public static Pressure convertPressure(
      Pressure value, Unit<ode.units.quantity.Pressure> ul) throws BigResult {
    return convertPressureXML(value, ul.getSymbol());
  }

  public static Pressure convertPressureXML(Pressure value, String xml)
      throws BigResult {
    String ode = xmlPressureEnumToODE(xml);
    return new PressureI(value, ode);
  }

  public static String xmlPressureEnumToODE(
      Unit<ode.units.quantity.Pressure> xml) {
    return ode.model.enums.UnitsPressure.bySymbol(xml.getSymbol()).toString();
  }

  public static String xmlPressureEnumToODE(String xml) {
    return ode.model.enums.UnitsPressure.bySymbol(xml).toString();
  }

  //
  // Temperature
  //

  public static ode.xml.model.enums.UnitsTemperature makeTemperatureUnitXML(
      String unit) {
    return TemperatureI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Temperature makeTemperatureXML(
      double d, String unit) {
    return TemperatureI.makeXMLQuantity(d, unit);
  }

  public static Temperature makeTemperature(
      double d, Unit<ode.units.quantity.Temperature> unit) {
    return new TemperatureI(d, unit);
  }

  public static Temperature makeTemperature(double d, UnitsTemperature unit) {
    return new TemperatureI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Temperature} to a Bhojpur ODE
   * Temperature. A null will be returned if the input is null.
   */
  public static Temperature convertTemperature(
      ode.units.quantity.Temperature value) {
    if (value == null)
      return null;
    String internal = xmlTemperatureEnumToODE(value.unit().getSymbol());
    UnitsTemperature ul = UnitsTemperature.valueOf(internal);
    return new ode.model.TemperatureI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Temperature convertTemperature(
      Temperature t) {
    return TemperatureI.convert(t);
  }

  public static Temperature convertTemperature(
      Temperature value, Unit<ode.units.quantity.Temperature> ul)
      throws BigResult {
    return convertTemperatureXML(value, ul.getSymbol());
  }

  public static Temperature convertTemperatureXML(
      Temperature value, String xml) throws BigResult {
    String ode = xmlTemperatureEnumToODE(xml);
    return new TemperatureI(value, ode);
  }

  public static String xmlTemperatureEnumToODE(
      Unit<ode.units.quantity.Temperature> xml) {
    return ode.model.enums.UnitsTemperature.bySymbol(
        xml.getSymbol()).toString();
  }

  public static String xmlTemperatureEnumToODE(String xml) {
    return ode.model.enums.UnitsTemperature.bySymbol(xml).toString();
  }

  //
  // Time
  //

  public static ode.xml.model.enums.UnitsTime makeTimeUnitXML(String unit) {
    return TimeI.makeXMLUnit(unit);
  }

  public static ode.units.quantity.Time makeTimeXML(double d, String unit) {
    return TimeI.makeXMLQuantity(d, unit);
  }

  public static Time makeTime(
      double d, Unit<ode.units.quantity.Time> unit) {
    return new TimeI(d, unit);
  }

  public static Time makeTime(double d, UnitsTime unit) {
    return new TimeI(d, unit);
  }

  /**
   * Convert a Bio-Formats {@link Time} to a Bhojpur ODE Time. A null will be
   * returned if the input is null.
   */
  public static Time convertTime(ode.units.quantity.Time value) {
    if (value == null)
      return null;
    String internal = xmlTimeEnumToODE(value.unit().getSymbol());
    UnitsTime ul = UnitsTime.valueOf(internal);
    return new ode.model.TimeI(value.value().doubleValue(), ul);
  }

  public static ode.units.quantity.Time convertTime(Time t) {
    return TimeI.convert(t);
  }

  public static Time convertTime(Time value, Unit<ode.units.quantity.Time> ul)
      throws BigResult {
    return convertTimeXML(value, ul.getSymbol());
  }

  public static Time convertTimeXML(Time value, String xml) throws BigResult {
    String ode = xmlTimeEnumToODE(xml);
    return new TimeI(value, ode);
  }

  public static String xmlTimeEnumToODE(Unit<ode.units.quantity.Time> xml) {
    return ode.model.enums.UnitsTime.bySymbol(xml.getSymbol()).toString();
  }

  public static String xmlTimeEnumToODE(String xml) {
    return ode.model.enums.UnitsTime.bySymbol(xml).toString();
  }

  public static UnitsLength Plane_PositionX = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Plane.getPositionXUnitXsdDefault()));
  public static UnitsLength Plane_PositionZ = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Plane.getPositionZUnitXsdDefault()));
  public static UnitsLength Plane_PositionY = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Plane.getPositionYUnitXsdDefault()));
  public static UnitsTime Plane_DeltaT = UnitsTime.valueOf(
      xmlTimeEnumToODE(ode.xml.model.Plane.getDeltaTUnitXsdDefault()));
  public static UnitsTime Plane_ExposureTime = UnitsTime.valueOf(
      xmlTimeEnumToODE(ode.xml.model.Plane.getExposureTimeUnitXsdDefault()));
  public static UnitsLength Shape_StrokeWidth = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Shape.getStrokeWidthUnitXsdDefault()));
  public static UnitsLength Shape_FontSize = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Shape.getFontSizeUnitXsdDefault()));
  public static UnitsElectricPotential DetectorSettings_Voltage =
      UnitsElectricPotential.valueOf(
          xmlElectricPotentialEnumToODE(
              ode.xml.model.DetectorSettings.getVoltageUnitXsdDefault()));
  public static UnitsFrequency DetectorSettings_ReadOutRate =
      UnitsFrequency.valueOf(
          xmlFrequencyEnumToODE(
              ode.xml.model.DetectorSettings.getReadOutRateUnitXsdDefault()));
  public static UnitsTemperature ImagingEnvironment_Temperature =
      UnitsTemperature.valueOf(
          xmlTemperatureEnumToODE(
              ode.xml.model.ImagingEnvironment.getTemperatureUnitXsdDefault()));
  public static UnitsPressure ImagingEnvironment_AirPressure =
      UnitsPressure.valueOf(
          xmlPressureEnumToODE(
              ode.xml.model.ImagingEnvironment.getAirPressureUnitXsdDefault()));
  public static UnitsLength LightSourceSettings_Wavelength =
      UnitsLength.valueOf(
          xmlLengthEnumToODE(
              ode.xml.model.LightSourceSettings.getWavelengthUnitXsdDefault()));
  public static UnitsLength Plate_WellOriginX = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Plate.getWellOriginXUnitXsdDefault()));
  public static UnitsLength Plate_WellOriginY =
      UnitsLength.valueOf(
          xmlLengthEnumToODE(
              ode.xml.model.Plate.getWellOriginYUnitXsdDefault()));
  public static UnitsLength Objective_WorkingDistance =
      UnitsLength.valueOf(
          xmlLengthEnumToODE(
              ode.xml.model.Objective.getWorkingDistanceUnitXsdDefault()));
  public static UnitsLength Pixels_PhysicalSizeX = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Pixels.getPhysicalSizeXUnitXsdDefault()));
  public static UnitsLength Pixels_PhysicalSizeZ = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Pixels.getPhysicalSizeZUnitXsdDefault()));
  public static UnitsLength Pixels_PhysicalSizeY = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Pixels.getPhysicalSizeYUnitXsdDefault()));
  public static UnitsTime Pixels_TimeIncrement = UnitsTime.valueOf(
      xmlTimeEnumToODE(
          ode.xml.model.Pixels.getTimeIncrementUnitXsdDefault()));
  public static UnitsLength StageLabel_Z = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.StageLabel.getZUnitXsdDefault()));
  public static UnitsLength StageLabel_Y = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.StageLabel.getYUnitXsdDefault()));
  public static UnitsLength StageLabel_X = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.StageLabel.getXUnitXsdDefault()));
  public static UnitsPower LightSource_Power = UnitsPower.valueOf(
      xmlPowerEnumToODE(ode.xml.model.LightSource.getPowerUnitXsdDefault()));
  public static UnitsElectricPotential Detector_Voltage =
      UnitsElectricPotential.valueOf(
          xmlElectricPotentialEnumToODE(
              ode.xml.model.Detector.getVoltageUnitXsdDefault()));
  public static UnitsLength WellSample_PositionX = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.WellSample.getPositionXUnitXsdDefault()));
  public static UnitsLength WellSample_PositionY = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.WellSample.getPositionYUnitXsdDefault()));
  public static UnitsLength Channel_EmissionWavelength = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Channel.getEmissionWavelengthUnitXsdDefault()));
  public static UnitsLength Channel_PinholeSize = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Channel.getPinholeSizeUnitXsdDefault()));
  public static UnitsLength Channel_ExcitationWavelength = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.Channel.getExcitationWavelengthUnitXsdDefault()));
  public static UnitsLength TransmittanceRange_CutOutTolerance =
      UnitsLength.valueOf(
          xmlLengthEnumToODE(
              ode.xml.model.TransmittanceRange.
                  getCutOutToleranceUnitXsdDefault()));
  public static UnitsLength TransmittanceRange_CutInTolerance =
      UnitsLength.valueOf(
          xmlLengthEnumToODE(
              ode.xml.model.TransmittanceRange.
                  getCutInToleranceUnitXsdDefault()));
  public static UnitsLength TransmittanceRange_CutOut = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.TransmittanceRange.getCutOutUnitXsdDefault()));
  public static UnitsLength TransmittanceRange_CutIn = UnitsLength.valueOf(
      xmlLengthEnumToODE(
          ode.xml.model.TransmittanceRange.getCutInUnitXsdDefault()));
  public static UnitsFrequency Laser_RepetitionRate = UnitsFrequency.valueOf(
      xmlFrequencyEnumToODE(
          ode.xml.model.Laser.getRepetitionRateUnitXsdDefault()));
  public static UnitsLength Laser_Wavelength = UnitsLength.valueOf(
      xmlLengthEnumToODE(ode.xml.model.Laser.getWavelengthUnitXsdDefault()));

}