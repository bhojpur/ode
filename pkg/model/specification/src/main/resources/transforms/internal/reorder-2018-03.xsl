<?xml version = "1.0" encoding = "UTF-8"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ODE="http://www.bhojpur.net/Schemas/ODE/2018-03"
	xmlns:AML="http://www.bhojpur.net/Schemas/AnalysisModule/2018-03"
	xmlns:CLI="http://www.bhojpur.net/Schemas/CLI/2018-03"
	xmlns:MLI="http://www.bhojpur.net/Schemas/MLI/2018-03"
	xmlns:STD="http://www.bhojpur.net/Schemas/STD/2018-03"
	xmlns:Bin="http://www.bhojpur.net/Schemas/BinaryFile/2018-03"
	xmlns:CA="http://www.bhojpur.net/Schemas/CA/2018-03"
	xmlns:SPW="http://www.bhojpur.net/Schemas/SPW/2018-03"
	xmlns:SA="http://www.bhojpur.net/Schemas/SA/2018-03"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xml="http://www.w3.org/XML/1998/namespace"
	exclude-result-prefixes="ODE AML CLI MLI STD Bin CA SPW SA"
	xmlns:exsl="http://exslt.org/common"
	extension-element-prefixes="exsl" version="1.0">
	<!-- xmlns="http://www.bhojpur.net/Schemas/ODE/2018-03"-->
	<xsl:variable name="newODENS">http://www.bhojpur.net/Schemas/ODE/2018-03</xsl:variable>
	<xsl:variable name="newSPWNS">http://www.bhojpur.net/Schemas/SPW/2018-03</xsl:variable>
	<xsl:variable name="newBINNS">http://www.bhojpur.net/Schemas/BinaryFile/2018-03</xsl:variable>
	<xsl:variable name="newCANS">http://www.bhojpur.net/Schemas/CA/2018-03</xsl:variable>
	<xsl:variable name="newSANS">http://www.bhojpur.net/Schemas/SA/2018-03</xsl:variable>
	<xsl:variable name="newSTDNS">http://www.bhojpur.net/Schemas/STD/2018-03</xsl:variable>
	<xsl:variable name="newAMLNS">http://www.bhojpur.net/Schemas/AnalysisModule/2018-03</xsl:variable>
	<xsl:variable name="newMLINS">http://www.bhojpur.net/Schemas/MLI/2018-03</xsl:variable>
	<xsl:variable name="newCLINS">http://www.bhojpur.net/Schemas/CLI/2018-03</xsl:variable>

	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="*"/>

	<!-- The order of all the Node with more then one type of children. -->
	<xsl:variable name="fixedOrders">
		<parentOrder name="ODE">
			<child name="Project"/>
			<child name="Dataset"/>
			<child name="Experiment"/>
			<child name="Plate"/>
			<child name="Screen"/>
			<child name="Experimenter"/>
			<child name="Group"/>
			<child name="Instrument"/>
			<child name="Image"/>
			<child name="SemanticTypeDefinitions"/>
			<child name="AnalysisModuleLibrary"/>
			<child name="CustomAttributes"/>
			<child name="StructuredAnnotations"/>
		</parentOrder>
		<parentOrder name="Image">
			<child name="CreationDate"/>
			<child name="ExperimenterRef"/>
			<child name="Description"/>
			<child name="ExperimentRef"/>
			<child name="GroupRef"/>
			<child name="DatasetRef"/>
			<child name="InstrumentRef"/>
			<child name="ObjectiveRef"/>
			<child name="ImagingEnvironment"/>
			<child name="Thumbnail"/>
			<child name="LogicalChannel"/>
			<child name="DisplayOptions"/>
			<child name="StageLabel"/>
			<child name="Pixels"/>
			<child name="Region"/>
			<child name="CustomAttributes"/>
			<child name="ROI"/>
			<child name="MicrobeamManipulation"/>
		</parentOrder>
		<parentOrder name="Pixels">
			<child name="BinData"/>
			<child name="TiffData"/>
			<child name="Plane"/>
		</parentOrder>
		<parentOrder name="Plane">
			<child name="PlaneTiming"/>
			<child name="StagePosition"/>
			<child name="HashSHA1"/>
		</parentOrder>
		<parentOrder name="Experiment">
			<child name="Description"/>
			<child name="ExperimenterRef"/>
			<child name="MicrobeamManipulationRef"/>
		</parentOrder>
		<parentOrder name="MicrobeamManipulation">
			<child name="ROIRef"/>
			<child name="ExperimenterRef"/>
			<child name="LightSourceRef"/>
		</parentOrder>
		<parentOrder name="LogicalChannel">
			<child name="LightSourceRef"/>
			<child name="OTFRef"/>
			<child name="DetectorRef"/>
			<child name="FilterSetRef"/>
			<child name="ChannelComponent"/>
		</parentOrder>
		<parentOrder name="DisplayOptions">
			<child name="RedChannel"/>
			<child name="GreenChannel"/>
			<child name="BlueChannel"/>
			<child name="GreyChannel"/>
			<child name="Projection"/>
			<child name="Time"/>
			<child name="ROI"/>
		</parentOrder>
		<parentOrder name="Instrument">
			<child name="Microscope"/>
			<child name="LightSource"/>
			<child name="Detector"/>
			<child name="Objective"/>
			<child name="FilterSet"/>
			<child name="Filter"/>
			<child name="Dichroic"/>
			<child name="OTF"/>
		</parentOrder>
		<parentOrder name="Experimenter">
			<child name="FirstName"/>
			<child name="LastName"/>
			<child name="Email"/>
			<child name="Institution"/>
			<child name="ODEName"/>
			<child name="GroupRef"/>
		</parentOrder>
		<parentOrder name="Objective">
			<child name="Correction"/>
			<child name="Immersion"/>
			<child name="LensNA"/>
			<child name="NominalMagnification"/>
			<child name="CalibratedMagnification"/>
			<child name="WorkingDistance"/>
		</parentOrder>
		<parentOrder name="Project">
			<child name="Description"/>
			<child name="ExperimenterRef"/>
			<child name="GroupRef"/>
		</parentOrder>
		<parentOrder name="Group">
			<child name="Leader"/>
			<child name="Contact"/>
		</parentOrder>
		<parentOrder name="Shape">
			<child name="Channels"/>
			<child name="Rect"/>
			<child name="Mask"/>
			<child name="Ellipse"/>
			<child name="Circle"/>
			<child name="Point"/>
			<child name="Polygon"/>
			<child name="Polyline"/>
			<child name="Line"/>
		</parentOrder>
		<parentOrder name="MaskPixels">
			<child name="BinData"/>
			<child name="TiffData"/>
		</parentOrder>
		<parentOrder name="OTF">
			<child name="ObjectiveRef"/>
			<child name="FilterSetRef"/>
			<child name="BinaryFile"/>
		</parentOrder>
		<parentOrder name="Dataset">
			<child name="Description"/>
			<child name="ExperimenterRef"/>
			<child name="GroupRef"/>
			<child name="ProjectRef"/>
			<child name="CustomAttributes"/>
		</parentOrder>
		<parentOrder name="Region">
			<child name="Region"/>
			<child name="CustomAttributes"/>
		</parentOrder>
	</xsl:variable>

	<!-- Rewriting all namespaces -->

	<xsl:template match="ODE:ODE">
		<ODE xmlns="http://www.bhojpur.net/Schemas/ODE/2018-03"
			xmlns:CA="http://www.bhojpur.net/Schemas/CA/2018-03"
			xmlns:STD="http://www.bhojpur.net/Schemas/STD/2018-03"
			xmlns:Bin="http://www.bhojpur.net/Schemas/BinaryFile/2018-03"
			xmlns:SPW="http://www.bhojpur.net/Schemas/SPW/2018-03"
			xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
			xsi:schemaLocation="http://www.bhojpur.net/Schemas/ODE/2018-03 http://www.bhojpur.net/Schemas/ODE/2018-03/ode.xsd">

			<xsl:apply-templates select="@*"/>
			<xsl:variable name="parentName">
				<xsl:value-of select="local-name(.)"/>
			</xsl:variable>
			<xsl:variable name="parentNode" select="."/>
			<xsl:variable name="parentOrderNode"
				select="exsl:node-set($fixedOrders)/parentOrder[@name=$parentName]"/>
			<xsl:for-each select="$parentOrderNode">
				<xsl:for-each select="*">
					<xsl:variable name="childName">
						<xsl:value-of select="@name"/>
					</xsl:variable>
					<xsl:for-each select="$parentNode/*[local-name(.)=$childName]">
						<xsl:apply-templates select="."/>
					</xsl:for-each>
				</xsl:for-each>
			</xsl:for-each>
		</ODE>
	</xsl:template>

	<xsl:template match="ODE:*">
		<xsl:element name="{name()}">
			<xsl:apply-templates select="@*"/>
			<xsl:variable name="parentName">
				<xsl:value-of select="local-name(.)"/>
			</xsl:variable>
			<xsl:variable name="parentNode" select="."/>
			<xsl:variable name="parentOrderNode"
				select="exsl:node-set($fixedOrders)/parentOrder[@name=$parentName]"/>
			<xsl:choose>
				<xsl:when test="count($parentOrderNode) = 0">
					<xsl:apply-templates select="node()"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$parentOrderNode">
						<xsl:for-each select="*">
							<xsl:variable name="childName">
								<xsl:value-of select="@name"/>
							</xsl:variable>
							<xsl:for-each select="$parentNode/*[local-name(.)=$childName]">
								<xsl:apply-templates select="."/>
							</xsl:for-each>
						</xsl:for-each>
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>

	<xsl:template match="CA:*">
		<xsl:element name="{name()}" namespace="{$newCANS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="Bin:*">
		<xsl:element name="{name()}" namespace="{$newBINNS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="STD:*">
		<xsl:element name="{name()}" namespace="{$newSTDNS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="SPW:*">
		<xsl:element name="{name()}" namespace="{$newSPWNS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="CLI:*">
		<xsl:element name="{name()}" namespace="{$newCLINS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="MLI:*">
		<xsl:element name="{name()}" namespace="{$newMLINS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<xsl:template match="AML:*">
		<xsl:element name="{name()}" namespace="{$newAMLNS}">
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>

	<!-- Default processing -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="text()|processing-instruction()|comment()">
		<xsl:copy>
			<xsl:apply-templates select="node()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>