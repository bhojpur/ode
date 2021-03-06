<?xml version="1.0" encoding="UTF-8"?>
<!--
  Bhojpur ODE Data Model transforms
  -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:math="http://exslt.org/math" exclude-result-prefixes="xs" version="1.0">
  <!-- getDefault function (AttributeName, NodeName, ParentNodeName?) -->
  <!--  returns the default as string -->
  <xsl:template name="GetDefaultUnit">
    <xsl:param name="theAttributeName"/>
    <xsl:param name="theElementName"/>
    <xsl:choose>
      <!-- codegen begin - defaults -->
      <xsl:when test="$theAttributeName = 'EmissionWavelength' and $theElementName = 'Channel'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'ExcitationWavelength' and $theElementName = 'Channel'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'PinholeSize' and $theElementName = 'Channel'">µm</xsl:when>
      <xsl:when test="$theAttributeName = 'Voltage' and $theElementName = 'Detector'">V</xsl:when>
      <xsl:when test="$theAttributeName = 'ReadOutRate' and $theElementName = 'DetectorSettings'">MHz</xsl:when>
      <xsl:when test="$theAttributeName = 'Voltage' and $theElementName = 'DetectorSettings'">V</xsl:when>
      <xsl:when test="$theAttributeName = 'AirPressure' and $theElementName = 'ImagingEnvironment'">mbar</xsl:when>
      <xsl:when test="$theAttributeName = 'Temperature' and $theElementName = 'ImagingEnvironment'">°C</xsl:when>
      <xsl:when test="$theAttributeName = 'RepetitionRate' and $theElementName = 'Laser'">Hz</xsl:when>
      <xsl:when test="$theAttributeName = 'Wavelength' and $theElementName = 'Laser'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'Power' and $theElementName = 'LightSource'">mW</xsl:when>
      <xsl:when test="$theAttributeName = 'Wavelength' and $theElementName = 'LightSourceSettings'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'WorkingDistance' and $theElementName = 'Objective'">µm</xsl:when>
      <xsl:when test="$theAttributeName = 'PhysicalSizeX' and $theElementName = 'Pixels'">µm</xsl:when>
      <xsl:when test="$theAttributeName = 'PhysicalSizeY' and $theElementName = 'Pixels'">µm</xsl:when>
      <xsl:when test="$theAttributeName = 'PhysicalSizeZ' and $theElementName = 'Pixels'">µm</xsl:when>
      <xsl:when test="$theAttributeName = 'TimeIncrement' and $theElementName = 'Pixels'">s</xsl:when>
      <xsl:when test="$theAttributeName = 'DeltaT' and $theElementName = 'Plane'">s</xsl:when>
      <xsl:when test="$theAttributeName = 'ExposureTime' and $theElementName = 'Plane'">s</xsl:when>
      <xsl:when test="$theAttributeName = 'PositionX' and $theElementName = 'Plane'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'PositionY' and $theElementName = 'Plane'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'PositionZ' and $theElementName = 'Plane'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'WellOriginX' and $theElementName = 'Plate'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'WellOriginY' and $theElementName = 'Plate'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'FontSize' and $theElementName = 'Shape'">pt</xsl:when>
      <xsl:when test="$theAttributeName = 'StrokeWidth' and $theElementName = 'Shape'">pixel</xsl:when>
      <xsl:when test="$theAttributeName = 'X' and $theElementName = 'StageLabel'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'Y' and $theElementName = 'StageLabel'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'Z' and $theElementName = 'StageLabel'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'CutIn' and $theElementName = 'TransmittanceRange'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'CutInTolerance' and $theElementName = 'TransmittanceRange'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'CutOut' and $theElementName = 'TransmittanceRange'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'CutOutTolerance' and $theElementName = 'TransmittanceRange'">nm</xsl:when>
      <xsl:when test="$theAttributeName = 'PositionX' and $theElementName = 'WellSample'">reference frame</xsl:when>
      <xsl:when test="$theAttributeName = 'PositionY' and $theElementName = 'WellSample'">reference frame</xsl:when>
      <!-- codegen end - defaults -->
      <xsl:otherwise>
        <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - GetDefaultUnit, the default for [<xsl:value-of select="$theAttributeName"/>] in element [<xsl:value-of select="$theElementName"/>] is not supported.</xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <!--  returns the value in the default units -->
  <!--  error and terminate if conversion not possible e.g. pixel->nm -->
  <xsl:template name="ConvertValueToDefault">
    <xsl:param name="theValue"/>
    <xsl:param name="theCurrentUnit"/>
    <xsl:param name="theAttributeName"/>
    <xsl:param name="theElementName"/>
    <!-- <xsl:message>ODE-XSLT: units-conversion.xsl - Message - ConvertValueToDefault, (<xsl:value-of select="$theValue"/>, <xsl:value-of select="$theCurrentUnit"/>, <xsl:value-of select="$theAttributeName"/>, <xsl:value-of select="$theElementName"/>).</xsl:message> -->
    <xsl:choose>
      <xsl:when test="$theCurrentUnit = ''">
        <!-- Already using default units so no conversion necessary -->
        <xsl:value-of select="$theValue"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="ConvertValueToUnit">
          <xsl:with-param name="theValue"><xsl:value-of select="$theValue"/></xsl:with-param>
          <xsl:with-param name="theCurrentUnit"><xsl:value-of select="$theCurrentUnit"/></xsl:with-param>
          <xsl:with-param name="theNewUnit">
            <xsl:call-template name="GetDefaultUnit">
              <xsl:with-param name="theAttributeName"><xsl:value-of select="$theAttributeName"/></xsl:with-param>
              <xsl:with-param name="theElementName"><xsl:value-of select="$theElementName"/></xsl:with-param>
            </xsl:call-template>
          </xsl:with-param>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  <xsl:template name="ConvertValueToUnit">
    <xsl:param name="theValue"/>
    <xsl:param name="theCurrentUnit"/>
    <xsl:param name="theNewUnit"/>
    <xsl:choose>
      <xsl:when test="$theNewUnit = ''">
        <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion of [<xsl:value-of select="$theCurrentUnit"/>] to an unknown unit.</xsl:message>
      </xsl:when>
      <xsl:when test="$theNewUnit = $theCurrentUnit"><xsl:value-of select="$theValue"/></xsl:when>
      <!-- codegen begin - convert -->
      <xsl:when test="$theCurrentUnit = 'YV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ZV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'EV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'PV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'TV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'GV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'MV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'kV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'daV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'V'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'nV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'aV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yV'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'yV'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YV'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZV'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EV'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PV'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TV'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GV'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MV'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kV'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hV'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daV'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'V'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dV'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cV'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mV'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µV'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nV'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pV'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fV'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aV'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zV'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'YHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ZHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'EHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'PHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'THz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'GHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'MHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'kHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'daHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Hz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'nHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'aHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yHz'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'yHz'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YHz'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZHz'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EHz'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PHz'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'THz'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GHz'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MHz'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kHz'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hHz'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daHz'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Hz'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dHz'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cHz'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mHz'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µHz'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nHz'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pHz'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fHz'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aHz'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zHz'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Ym'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 34)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 22) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((625*math:power(10, 18)) div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="(((125*math:power(10, 12)) div 3857097)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(((9842525*math:power(10, 18)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((9842525*math:power(10, 18)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="(((559234375*math:power(10, 13)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Zm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 31)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 19) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((625*math:power(10, 15)) div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="(((125*math:power(10, 9)) div 3857097)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(((9842525*math:power(10, 15)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((9842525*math:power(10, 15)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="(((559234375*math:power(10, 10)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Em'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 28)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 16) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((625*math:power(10, 12)) div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="(((125*math:power(10, 6)) div 3857097)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(((9842525*math:power(10, 12)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((9842525*math:power(10, 12)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="(((559234375*math:power(10, 7)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Pm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 13) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((625*math:power(10, 9)) div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((125000 div 3857097)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(((9842525*math:power(10, 9)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((9842525*math:power(10, 9)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="(((559234375*math:power(10, 4)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Tm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 10) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((625*math:power(10, 6)) div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((125 div 3857097)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(((9842525*math:power(10, 6)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((9842525*math:power(10, 6)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((5592343750 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Gm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 7) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((625000 div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div 30856776)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="(39370100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((4724412*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((9842525000 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((9842525000 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((22369375 div 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((28346472*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Mm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 4) div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((625 div 5912956545363)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div 30856776000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(472441200*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="(39370100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((9842525 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((9842525 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((178955 div 288)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(2834647200*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'km'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((10 div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((5 div 47303652362904)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((2362206 div 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div 120)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div 360)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div 57600)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((14173236 div 5)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div 1495978707)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div 94607304725808)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div 1200)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div 3600)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div 576000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((7086618 div 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dam'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div 14959787070)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div 946073047258080)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div 250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div 12000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div 36000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div 125)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div 149597870700)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div 9460730472580800)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div 2500)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div 1250)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div 1495978707000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div 94607304725808000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div 25000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div 12500)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div 125000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'nm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 21)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 24)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'am'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 27)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 30)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 28))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 25)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 25)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ym'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 33)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 31))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 28))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 28)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 28)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 29)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 25)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Å'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div math:power(10, 34))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div math:power(10, 31))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div math:power(10, 28))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1 div (1495978707*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((1 div (94607304725808*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (30856776*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((393701 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1181103 div (25*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((393701 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((393701 div (12*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((393701 div (36*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((35791 div (576*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((3543309 div (125*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ua'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1495978707 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1495978707 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1495978707 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1495978707 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1495978707 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1495978707 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1495978707 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((1495978707 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(1495978707*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(14959787070*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(149597870700*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(1495978707000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((1495978707*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((1495978707*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((1495978707*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((1495978707*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((1495978707*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((1495978707*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((1495978707*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((1495978707*math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="((1495978707*math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((1495978707*math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((6830953 div 431996825232)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((498659569 div (10285592*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((588968312924607 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1766904938773821 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((588968312924607 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((196322770974869 div 400)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((196322770974869 div 1200)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((17847524634079 div 192000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((10601429632642926 div 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ly'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((5912956545363 div (625*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((5912956545363 div (625*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((5912956545363 div (625*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((5912956545363 div (625*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((5912956545363 div (625*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((5912956545363 div 625000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((5912956545363 div 625)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((47303652362904 div 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="(94607304725808*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(946073047258080*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(9460730472580800*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(94607304725808000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((94607304725808*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((94607304725808*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((94607304725808*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((94607304725808*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((94607304725808*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((94607304725808*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((94607304725808*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((94607304725808*math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="((94607304725808*math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((94607304725808*math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((431996825232 div 6830953)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1970985515121 div (6428495*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((2327936904865958463 div 6250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((111740971433566006224 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((9311747619463833852 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((775978968288652821 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((258659656096217607 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((23514514190565237 div 4000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((670445828601396037344 div 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pc'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((3857097 div (125*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((3857097 div (125*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((3857097 div (125*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((3857097 div 125000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((3857097 div 125)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="(30856776*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="(30856776000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((30856776*math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((30856776*math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((30856776*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((30856776*math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((30856776*math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((30856776*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((30856776*math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((30856776*math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((30856776*math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((30856776*math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((30856776*math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((30856776*math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((30856776*math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="((30856776*math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((30856776*math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="(((10285592*math:power(10, 7)) div 498659569)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="(((6428495*math:power(10, 6)) div 1970985515121)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="(1214834356797600*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((145780122815712*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((12148343567976*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((1012361963998*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(((1012361963998*math:power(10, 5)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((57520566136250 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="((874680736894272*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'thou'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div (393701*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div (393701*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div (393701*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div (393701*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div (393701*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div 39370100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((10 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((math:power(10, 4) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((math:power(10, 5) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((math:power(10, 6) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((math:power(10, 7) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((math:power(10, 8) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((math:power(10, 9) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((math:power(10, 10) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((math:power(10, 13) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((math:power(10, 16) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((math:power(10, 19) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((math:power(10, 22) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((math:power(10, 25) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((math:power(10, 28) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="((math:power(10, 31) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((math:power(10, 17) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((math:power(10, 5) div 588968312924607)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((6250 div 2327936904865958463)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div 1214834356797600)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(12000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((250 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((250 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((25 div 1584)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(72000*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'li'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div (4724412*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div (4724412*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div (4724412*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div (4724412*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div (4724412*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div (4724412*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div 472441200)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((5 div 2362206)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((25 div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((250 div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((2500 div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((25000 div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(((25*math:power(10, 4)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(((25*math:power(10, 5)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(((25*math:power(10, 8)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(((25*math:power(10, 11)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(((25*math:power(10, 14)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(((25*math:power(10, 17)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(((25*math:power(10, 20)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(((25*math:power(10, 23)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(((25*math:power(10, 26)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(((25*math:power(10, 12)) div 1181103)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((25 div 1766904938773821)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((25 div 111740971433566006224)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (145780122815712*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((1 div 12000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((1 div 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((1 div 144)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((1 div 432)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((1 div 760320)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(6*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'in'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div (393701*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div (393701*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div (393701*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div (393701*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div (393701*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div (393701*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div 39370100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((10 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((100 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((1000 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((math:power(10, 4) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((math:power(10, 5) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((math:power(10, 6) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="((math:power(10, 7) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="((math:power(10, 10) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="((math:power(10, 13) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="((math:power(10, 16) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="((math:power(10, 19) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="((math:power(10, 22) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="((math:power(10, 25) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="((math:power(10, 28) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="((math:power(10, 14) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((100 div 588968312924607)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((25 div 9311747619463833852)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (12148343567976*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(12*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((1 div 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((1 div 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((1 div 63360)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(72*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ft'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((3 div (9842525*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((3 div (9842525*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((3 div (9842525*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((3 div (9842525*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((3 div (9842525*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((3 div 9842525000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((3 div 9842525)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((120 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((1200 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((12000 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(((12*math:power(10, 4)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(((12*math:power(10, 5)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(((12*math:power(10, 6)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(((12*math:power(10, 7)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(((12*math:power(10, 10)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(((12*math:power(10, 13)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(((12*math:power(10, 16)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(((12*math:power(10, 19)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(((12*math:power(10, 22)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(((12*math:power(10, 25)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(((12*math:power(10, 28)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(((12*math:power(10, 14)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((400 div 196322770974869)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((25 div 775978968288652821)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (1012361963998*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((3 div 250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(144*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="(12*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((1 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((1 div 5280)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(864*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'yd'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((9 div (9842525*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((9 div (9842525*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((9 div (9842525*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((9 div (9842525*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((9 div (9842525*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((9 div 9842525000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((9 div 9842525)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((360 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((3600 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((36000 div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(((36*math:power(10, 4)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(((36*math:power(10, 5)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(((36*math:power(10, 6)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(((36*math:power(10, 7)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(((36*math:power(10, 10)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(((36*math:power(10, 13)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(((36*math:power(10, 16)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(((36*math:power(10, 19)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(((36*math:power(10, 22)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(((36*math:power(10, 25)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(((36*math:power(10, 28)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(((36*math:power(10, 14)) div 393701)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((1200 div 196322770974869)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((25 div 258659656096217607)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((3 div (1012361963998*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((9 div 250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(432*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="(36*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(3*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((1 div 1760)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(2592*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mi'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((9 div (559234375*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((9 div (559234375*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((9 div (559234375*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((9 div (559234375*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((9 div 5592343750)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((36 div 22369375)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((288 div 178955)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((57600 div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((576000 div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="(((576*math:power(10, 4)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="(((576*math:power(10, 5)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="(((576*math:power(10, 6)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="(((576*math:power(10, 7)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(((576*math:power(10, 8)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(((576*math:power(10, 11)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(((576*math:power(10, 14)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(((576*math:power(10, 17)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(((576*math:power(10, 20)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(((576*math:power(10, 23)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(((576*math:power(10, 26)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(((576*math:power(10, 29)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(((576*math:power(10, 15)) div 35791)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((192000 div 17847524634079)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((4000 div 23514514190565237)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((3 div 57520566136250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((1584 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="(760320*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="(63360*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="(5280*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="(1760*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pt'"><xsl:value-of select="(4561920*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pt'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ym'"><xsl:value-of select="((1 div (28346472*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zm'"><xsl:value-of select="((1 div (28346472*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Em'"><xsl:value-of select="((1 div (28346472*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pm'"><xsl:value-of select="((1 div (28346472*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Tm'"><xsl:value-of select="((1 div (28346472*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gm'"><xsl:value-of select="((1 div (28346472*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mm'"><xsl:value-of select="((1 div 2834647200)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'km'"><xsl:value-of select="((5 div 14173236)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hm'"><xsl:value-of select="((25 div 7086618)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dam'"><xsl:value-of select="((125 div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'm'"><xsl:value-of select="((1250 div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dm'"><xsl:value-of select="((12500 div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cm'"><xsl:value-of select="((125000 div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm'"><xsl:value-of select="(((125*math:power(10, 4)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µm'"><xsl:value-of select="(((125*math:power(10, 7)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nm'"><xsl:value-of select="(((125*math:power(10, 10)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pm'"><xsl:value-of select="(((125*math:power(10, 13)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fm'"><xsl:value-of select="(((125*math:power(10, 16)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'am'"><xsl:value-of select="(((125*math:power(10, 19)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zm'"><xsl:value-of select="(((125*math:power(10, 22)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ym'"><xsl:value-of select="(((125*math:power(10, 25)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Å'"><xsl:value-of select="(((125*math:power(10, 11)) div 3543309)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ua'"><xsl:value-of select="((25 div 10601429632642926)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ly'"><xsl:value-of select="((25 div 670445828601396037344)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pc'"><xsl:value-of select="((1 div (874680736894272*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'thou'"><xsl:value-of select="((1 div 72000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'li'"><xsl:value-of select="((1 div 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'in'"><xsl:value-of select="((1 div 72)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ft'"><xsl:value-of select="((1 div 864)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yd'"><xsl:value-of select="((1 div 2592)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mi'"><xsl:value-of select="((1 div 4561920)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pixel'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'reference frame'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'YW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ZW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'EW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'PW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'TW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'GW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'MW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'kW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'daW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'W'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'nW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'aW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yW'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'yW'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YW'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZW'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EW'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PW'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TW'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GW'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MW'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kW'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hW'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daW'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'W'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dW'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cW'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mW'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µW'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nW'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pW'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fW'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aW'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zW'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'YPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 22)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 33)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 23)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 20)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 32)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ZPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 19)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 30)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 20)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 17)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 29)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'EPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 16)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 27)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 17)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 14)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 26)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'PPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 13)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 24)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 14)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 11)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 23)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'TPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 10)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 21)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 11)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 8)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 20)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'GPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 7)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 18)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 8)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 5)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 17)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'MPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 4)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 15)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 5)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((30400 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 14)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'kPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="$theValue"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((40 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 12)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((30400 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((152 div 20265)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 11)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="$theValue"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((4 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 11)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((3040 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((76 div 101325)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 10)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'daPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((2 div 20265)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 10)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((304 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((38 div 506625)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 9)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Pa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div 101325)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 9)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((152 div 20265)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div 2533125)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 8)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div 1013250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 8)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((76 div 101325)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div 25331250)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 7)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div 10132500)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 7)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((38 div 506625)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div 253312500)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 6)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div 101325000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 6)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div 2533125)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div 2533125000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 5)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((25000 div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div 2533125000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((200 div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'nPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((25 div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div 133322387415)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'pPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((1 div 6894757293168360)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div 133322387415000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((1 div (689475729316836*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div (133322387415*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'aPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 29))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((1 div (689475729316836*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div (133322387415*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 32))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 29))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 21)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((1 div (689475729316836*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 21)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div (133322387415*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'yPa'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div math:power(10, 29))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 35))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 32))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div math:power(10, 28))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div (101325*math:power(10, 24)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((1 div (689475729316836*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((19 div (2533125*math:power(10, 21)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div (2533125*math:power(10, 24)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((1 div (133322387415*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'bar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 29)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((4000 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 14)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 4)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((3040 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 13)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Mbar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 29)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 32)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 35)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 9)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 20)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 10)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 7)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 19)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'kbar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 29)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 32)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="(((4*math:power(10, 6)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 17)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(((304*math:power(10, 7)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="(((304*math:power(10, 4)) div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 16)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'dbar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 28)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((400 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 13)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((304000 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((304 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 12)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cbar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="$theValue"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((40 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 12)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((30400 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((152 div 20265)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 11)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mbar'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="$theValue"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((4 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((25*math:power(10, 11)) div 172368932329209)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((3040 div 4053)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((76 div 101325)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((2*math:power(10, 10)) div 26664477483)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'atm'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((4053 div (4*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((4053 div (4*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((4053 div (4*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((4053 div (4*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((4053 div (4*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((4053 div (4*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((4053 div (4*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((4053 div 40)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((4053 div 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((20265 div 2)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="(101325*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="(1013250*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="(10132500*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="(101325000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((101325*math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((101325*math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="((101325*math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((101325*math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="((101325*math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="((101325*math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="((101325*math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((4053 div 4000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((4053 div (4*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((4053 div (4*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((4053 div 400)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((4053 div 40)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((4053 div 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((120625*math:power(10, 9)) div 8208044396629)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(760*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((19 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((965*math:power(10, 9)) div 1269737023)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'psi'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 33)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 30)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 27)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 24)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 21)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 18)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 15)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((172368932329209 div 25000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="((172368932329209 div 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(6894757293168360*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((689475729316836*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="((689475729316836*math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="((689475729316836*math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="((689475729316836*math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((172368932329209 div (25*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((8208044396629 div (120625*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((155952843535951 div (3015625*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((155952843535951 div (3015625*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="((8208044396629 div 158717127875)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Torr'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((4053 div (304*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((4053 div (304*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((4053 div (304*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((4053 div (304*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((4053 div (304*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((4053 div (304*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((4053 div (304*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((4053 div 30400)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((4053 div 3040)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((4053 div 304)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((20265 div 152)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((101325 div 76)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((506625 div 38)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((2533125 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((2533125000 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(((2533125*math:power(10, 6)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(((2533125*math:power(10, 9)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(((2533125*math:power(10, 12)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(((2533125*math:power(10, 15)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(((2533125*math:power(10, 18)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(((2533125*math:power(10, 21)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((4053 div (304*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((4053 div (304*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((4053 div (304*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((4053 div 304000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((4053 div 30400)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((4053 div 3040)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1 div 760)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((3015625*math:power(10, 6)) div 155952843535951)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((24125*math:power(10, 6)) div 24125003437)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mTorr'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((4053 div (304*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((4053 div (304*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((4053 div (304*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((4053 div (304*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((4053 div (304*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((4053 div (304*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((4053 div 30400)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((20265 div 152)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((101325 div 76)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((506625 div 38)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((2533125 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((25331250 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((253312500 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((2533125000 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="(((2533125*math:power(10, 6)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(((2533125*math:power(10, 9)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(((2533125*math:power(10, 12)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="(((2533125*math:power(10, 15)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="(((2533125*math:power(10, 18)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="(((2533125*math:power(10, 21)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="(((2533125*math:power(10, 24)) div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((4053 div 3040)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((4053 div (304*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((4053 div (304*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((4053 div 304)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((20265 div 152)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((101325 div 76)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((25 div 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="(((3015625*math:power(10, 9)) div 155952843535951)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mm Hg'"><xsl:value-of select="(((24125*math:power(10, 9)) div 24125003437)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'mm Hg'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'YPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 32)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ZPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 29)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'EPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'PPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'TPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'GPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'MPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'daPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Pa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mPa'"><xsl:value-of select="((26664477483 div (2*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µPa'"><xsl:value-of select="((26664477483 div 200)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'nPa'"><xsl:value-of select="(133322387415*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'pPa'"><xsl:value-of select="(133322387415000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fPa'"><xsl:value-of select="((133322387415*math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'aPa'"><xsl:value-of select="((133322387415*math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zPa'"><xsl:value-of select="((133322387415*math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'yPa'"><xsl:value-of select="((133322387415*math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'bar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Mbar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'kbar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'dbar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 12)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cbar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mbar'"><xsl:value-of select="((26664477483 div (2*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'atm'"><xsl:value-of select="((1269737023 div (965*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'psi'"><xsl:value-of select="((158717127875 div 8208044396629)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Torr'"><xsl:value-of select="((24125003437 div (24125*math:power(10, 6)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'mTorr'"><xsl:value-of select="((24125003437 div (24125*math:power(10, 9)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'K'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = '°C'"><xsl:value-of select="($theValue+(-5463 div 20))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°F'"><xsl:value-of select="(((9 div 5)*$theValue)+(-45967 div 100))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°R'"><xsl:value-of select="((9 div 5)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'K'"><xsl:value-of select="($theValue+(5463 div 20))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°F'"><xsl:value-of select="(((9 div 5)*$theValue)+32)"/></xsl:when>
          <xsl:when test="$theNewUnit = '°R'"><xsl:value-of select="(((9 div 5)*$theValue)+(49167 div 100))"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = '°F'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'K'"><xsl:value-of select="(((5 div 9)*$theValue)+(45967 div 180))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°C'"><xsl:value-of select="(((5 div 9)*$theValue)+(-160 div 9))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°R'"><xsl:value-of select="($theValue+(45967 div 100))"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = '°R'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'K'"><xsl:value-of select="((5 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = '°C'"><xsl:value-of select="(((5 div 9)*$theValue)+(-5463 div 20))"/></xsl:when>
          <xsl:when test="$theNewUnit = '°F'"><xsl:value-of select="($theValue+(-45967 div 100))"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Ys'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 48)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 22)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 20)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="(((3125*math:power(10, 17)) div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Zs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 45)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 19)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 17)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="(((3125*math:power(10, 14)) div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Es'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 42)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 16)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 14)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="(((3125*math:power(10, 11)) div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Ps'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 39)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 13)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 11)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="(((3125*math:power(10, 8)) div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Ts'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 10)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 8)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="(((3125*math:power(10, 5)) div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Gs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 33)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 7)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(((25*math:power(10, 5)) div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((312500 div 27)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'Ms'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 30)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(((5*math:power(10, 4)) div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((2500 div 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((625 div 54)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ks'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 27)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((50 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((5 div 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((5 div 432)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'hs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 26)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((5 div 3)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div 36)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 864)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'das'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 25)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div 360)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 8640)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 's'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 24)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div 60)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div 3600)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 86400)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ds'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(100*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 8)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 11)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 14)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 17)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 20)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 23)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div 600)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div 36000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 864000)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'cs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="(10*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(math:power(10, 4)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 7)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 13)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 16)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 19)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 22)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div 6000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ms'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div 100)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div 10)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 21)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'µs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 18)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ns'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 15)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ps'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 12)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'fs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 9)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'as'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(math:power(10, 6)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'zs'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="(1000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 23)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'ys'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((1 div math:power(10, 48))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((1 div math:power(10, 45))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((1 div math:power(10, 42))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((1 div math:power(10, 39))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((1 div math:power(10, 36))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((1 div math:power(10, 33))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((1 div math:power(10, 30))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((1 div math:power(10, 27))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((1 div math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="((1 div math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="((1 div math:power(10, 24))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="((1 div math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((1 div math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((1 div math:power(10, 21))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((1 div math:power(10, 18))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((1 div math:power(10, 15))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((1 div math:power(10, 12))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((1 div math:power(10, 9))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="((1 div math:power(10, 6))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="((1 div 1000)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="((1 div (6*math:power(10, 25)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div (36*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div (864*math:power(10, 26)))*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'min'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((3 div (5*math:power(10, 22)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((3 div (5*math:power(10, 19)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((3 div (5*math:power(10, 16)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((3 div (5*math:power(10, 13)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((3 div (5*math:power(10, 10)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((3 div (5*math:power(10, 7)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((3 div (5*math:power(10, 4)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((3 div 50)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="((3 div 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(6*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(60*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(600*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="(6000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((6*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((6*math:power(10, 7))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((6*math:power(10, 10))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((6*math:power(10, 13))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((6*math:power(10, 16))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="((6*math:power(10, 19))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="((6*math:power(10, 22))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="((6*math:power(10, 25))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="((1 div 60)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 1440)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'h'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((9 div (25*math:power(10, 20)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((9 div (25*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((9 div (25*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((9 div (25*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((9 div (25*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((9 div (25*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((9 div 2500)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((18 div 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(36*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(360*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(3600*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(36000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((36*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((36*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((36*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((36*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((36*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((36*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="((36*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="((36*math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="((36*math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(60*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'd'"><xsl:value-of select="((1 div 24)*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theCurrentUnit = 'd'">
        <xsl:choose>
          <xsl:when test="$theNewUnit = 'UNKNOWN%SYMBOL'"><xsl:value-of select="ERROR"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ys'"><xsl:value-of select="((27 div (3125*math:power(10, 17)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Zs'"><xsl:value-of select="((27 div (3125*math:power(10, 14)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Es'"><xsl:value-of select="((27 div (3125*math:power(10, 11)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ps'"><xsl:value-of select="((27 div (3125*math:power(10, 8)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ts'"><xsl:value-of select="((27 div (3125*math:power(10, 5)))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Gs'"><xsl:value-of select="((27 div 312500)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'Ms'"><xsl:value-of select="((54 div 625)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ks'"><xsl:value-of select="((432 div 5)*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'hs'"><xsl:value-of select="(864*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'das'"><xsl:value-of select="(8640*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 's'"><xsl:value-of select="(86400*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ds'"><xsl:value-of select="(864000*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'cs'"><xsl:value-of select="((864*math:power(10, 4))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ms'"><xsl:value-of select="((864*math:power(10, 5))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'µs'"><xsl:value-of select="((864*math:power(10, 8))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ns'"><xsl:value-of select="((864*math:power(10, 11))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ps'"><xsl:value-of select="((864*math:power(10, 14))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'fs'"><xsl:value-of select="((864*math:power(10, 17))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'as'"><xsl:value-of select="((864*math:power(10, 20))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'zs'"><xsl:value-of select="((864*math:power(10, 23))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'ys'"><xsl:value-of select="((864*math:power(10, 26))*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'min'"><xsl:value-of select="(1440*$theValue)"/></xsl:when>
          <xsl:when test="$theNewUnit = 'h'"><xsl:value-of select="(24*$theValue)"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <xsl:when test="$theNewUnit = '°C'">
        <xsl:choose>
          <xsl:when test="$theCurrentUnit = '°F'"><xsl:value-of select="(($theValue - 32) * 5) div 9"/></xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion, [<xsl:value-of select="$theCurrentUnit"/>] to [<xsl:value-of select="$theNewUnit"/>] is not supported.</xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <!-- codegen end - convert -->
      <xsl:otherwise>
        <xsl:message terminate="yes">ODE-XSLT: units-conversion.xsl - ERROR - ConvertValueToUnit, cannot perform conversion from any unit to [<xsl:value-of select="$theNewUnit"/>].</xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>