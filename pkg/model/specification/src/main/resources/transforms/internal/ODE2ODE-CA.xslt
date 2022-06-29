<?xml version = "1.0" encoding = "UTF-8"?>

<xsl:transform
	xmlns:xsl = "http://www.w3.org/1999/XSL/Transform" version = "1.0"
	xmlns:xsi = "http://www.w3.org/2001/XMLSchema-instance"
	xmlns:ODE = "http://www.bhojpur.net/XMLschemas/ODE/FC/ode.xsd"
	xmlns:STD = "http://www.bhojpur.net/XMLschemas/STD/RC2/STD.xsd"
	xmlns:STD3 = "http://www.bhojpur.net/XMLschemas/STD/RC3/STD.xsd"
	xmlns:Bin = "http://www.bhojpur.net/XMLschemas/BinaryFile/RC1/BinaryFile.xsd"
	xmlns:AML = "http://www.bhojpur.net/XMLschemas/AnalysisModule/RC1/AnalysisModule.xsd"
	xmlns:DH = "http://www.bhojpur.net/XMLschemas/DataHistory/IR3/DataHistory.xsd"
	xmlns = "http://www.bhojpur.net/XMLschemas/CA/RC1/CA.xsd">

	<!-- Pass everything through that doesn't match the defined Bhojpur ODE namespace -->
	<xsl:template match = "*">
		<xsl:copy-of select = "."/>
	</xsl:template>
	<xsl:template match = "ODE:ODE">
		<xsl:element name = "ODE" namespace = "http://www.bhojpur.net/XMLschemas/CA/RC1/CA.xsd"
			xmlns:Bin = "http://www.bhojpur.net/XMLschemas/BinaryFile/RC1/BinaryFile.xsd">
			<xsl:attribute name = "xsi:schemaLocation">
				<xsl:value-of select = "@xsi:schemaLocation"/>
			</xsl:attribute>
			<!-- Need to copy STD and AML also -->
			<!-- Deal with the hierarchy -->
			<xsl:apply-templates select = "ODE:Project"/>
			<xsl:apply-templates select = "ODE:Dataset"/>
			<xsl:apply-templates select = "ODE:Image"/>

			<xsl:element name = "CustomAttributes">
				<xsl:apply-templates select = "ODE:Experimenter"/>
				<xsl:apply-templates select = "ODE:Group"/>
				<xsl:apply-templates select = "ODE:Experiment"/>
				<xsl:apply-templates select = "ODE:Instrument"/>
				<xsl:apply-templates select = "ODE:Plate"/>
				<xsl:apply-templates select = "ODE:Screen"/>
				<xsl:apply-templates select = "ODE:CustomAttributes/*"/>
			</xsl:element>

			<xsl:apply-templates select = "STD:*"/>
			<xsl:apply-templates select = "STD3:*"/>
			<xsl:apply-templates select = "AML:*"/>
			<xsl:apply-templates select = "DH:*"/>
		</xsl:element>
	</xsl:template>
	<!-- Project -->
	<xsl:template match = "ODE:Project">
		<xsl:element name = "Project">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:attribute name = "Experimenter">
				<xsl:value-of select = "ODE:ExperimenterRef/@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Group">
				<xsl:value-of select = "ODE:GroupRef/@ID"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Description" mode = "OptionalAttribute"/>
		</xsl:element>
	</xsl:template>
	<!-- Dataset -->
	<xsl:template match = "ODE:Dataset">
		<xsl:element name = "Dataset">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:attribute name = "Locked">
				<xsl:value-of select = "@Locked"/>
			</xsl:attribute>
			<xsl:attribute name = "Experimenter">
				<xsl:value-of select = "ODE:ExperimenterRef/@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Group">
				<xsl:value-of select = "ODE:GroupRef/@ID"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Description" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:ProjectRef" mode = "CopyRefs"/>

			<xsl:if test = "ODE:CustomAttributes/*">
				<xsl:element name = "CustomAttributes">
					<xsl:copy-of select = "ODE:CustomAttributes/*"/>
				</xsl:element>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<!-- Image and the required Dimensions Image attribute -->
	<xsl:template match = "ODE:Image">
		<xsl:element name = "Image">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:attribute name = "CreationDate">
				<xsl:value-of select = "ODE:CreationDate"/>
			</xsl:attribute>
			<xsl:attribute name = "Experimenter">
				<xsl:value-of select = "ODE:ExperimenterRef/@ID"/>
			</xsl:attribute>
			<xsl:if test = "string-length(ODE:GroupRef/@ID) > 0">
				<xsl:attribute name = "Group">
					<xsl:value-of select = "ODE:GroupRef/@ID"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test = "string-length(@DefaultPixels) > 0">
				<xsl:attribute name = "DefaultPixels">
					<xsl:value-of select = "@DefaultPixels"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:if test = "not (string-length(@DefaultPixels) > 0)">
				<xsl:if test = "string-length(ODE:Pixels [1] /@ID) > 0">
					<xsl:attribute name = "DefaultPixels">
						<xsl:value-of select = "ODE:Pixels [1] /@ID"/>
					</xsl:attribute>
				</xsl:if>
				<xsl:if test = "not (string-length(ODE:Pixels [1] /@ID) > 0)">
					<xsl:if test = "string-length(ODE:CustomAttributes/ODE:Pixels [1] /@ID) > 0">
						<xsl:attribute name = "DefaultPixels">
							<xsl:value-of select = "ODE:CustomAttributes/ODE:Pixels [1] /@ID"/>
						</xsl:attribute>
					</xsl:if>
				</xsl:if>
			</xsl:if>
			<xsl:apply-templates select = "ODE:Description" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:DatasetRef" mode = "CopyRefs"/>
			<xsl:element name = "CustomAttributes">
				<xsl:if test="@PixelSizeX">
					<xsl:element name = "Dimensions">
						<xsl:attribute name = "ID">
							<xsl:value-of select = "generate-id()"/>
						</xsl:attribute>
						<xsl:attribute name = "PixelSizeX">
							<xsl:value-of select = "@PixelSizeX"/>
						</xsl:attribute>
						<xsl:apply-templates select = "@PixelSizeY" mode = "OptionalAttribute"/>
						<xsl:apply-templates select = "@PixelSizeZ" mode = "OptionalAttribute"/>
						<xsl:apply-templates select = "@WaveIncrement" mode = "OptionalAttribute">
							<xsl:with-param name = "Name">PixelSizeC</xsl:with-param>
						</xsl:apply-templates>
						<xsl:apply-templates select = "@TimeIncrement" mode = "OptionalAttribute">
							<xsl:with-param name = "Name">PixelSizeT</xsl:with-param>
						</xsl:apply-templates>
					</xsl:element>
				</xsl:if>
				<xsl:apply-templates select = "ODE:ExperimentRef"/>
				<xsl:apply-templates select = "ODE:InstrumentRef"/>
				<xsl:apply-templates select = "ODE:ImagingEnvironment"/>
				<xsl:apply-templates select = "ODE:Thumbnail"/>
				<xsl:apply-templates select = "ODE:ChannelInfo"/>
				<xsl:apply-templates select = "ODE:DisplayOptions"/>
				<xsl:apply-templates select = "ODE:StageLabel"/>
				<xsl:apply-templates select = "ODE:PlateRef"/>
				<xsl:apply-templates select = "ODE:Pixels"/>
				<xsl:copy-of select = "ODE:Feature"/>
				<xsl:copy-of select = "ODE:CustomAttributes/*"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!-- Image attributes -->

	<!-- ExperimentRef -->
	<xsl:template match = "ODE:Image/ODE:ExperimentRef">
		<xsl:element name = "ImageExperiment">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeRefs"/>
		</xsl:element>
	</xsl:template>
	<!-- InstrumentRef -->
	<xsl:template match = "ODE:Image/ODE:InstrumentRef">
		<xsl:element name = "ImageInstrument">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeRefs"/>
			<xsl:apply-templates select = "../ODE:ObjectiveRef" mode = "MakeRefs"/>
		</xsl:element>
	</xsl:template>
	<!-- ImagingEnvironment -->
	<xsl:template match = "ODE:Image/ODE:ImagingEnvironment">
		<xsl:apply-templates select = "." mode = "Element2Attributes"/>
	</xsl:template>
	<!-- Thumbnail -->
	<xsl:template match = "ODE:Image/ODE:Thumbnail">
		<xsl:element name = "Thumbnail">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@href" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">Path</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "@MIMEtype" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">MimeType</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
		<!--<xsl:apply-templates select = "." mode = "Element2Attributes"/>-->
	</xsl:template>
	<!-- PixelChannelComponent -->
	<xsl:template match = "ODE:ChannelInfo/ODE:ChannelComponent">
		<xsl:element name = "PixelChannelComponent">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Pixels">
				<xsl:value-of select = "@Pixels"/>
			</xsl:attribute>
			<xsl:attribute name = "Index">
				<xsl:value-of select = "@Index"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@ColorDomain" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = ".." mode = "MakeRefs">
				<xsl:with-param name = "Name">LogicalChannel</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- ChannelInfo -->
	<xsl:template match = "ODE:Image/ODE:ChannelInfo">
		<xsl:element name = "LogicalChannel">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@SamplesPerPixel" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:LightSourceRef/@Attenuation" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">LightAttenuation</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:LightSourceRef/@Wavelength" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">LightWavelength</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:DetectorRef/@Offset" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">DetectorOffset</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:DetectorRef/@Gain" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">DetectorGain</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "@IlluminationType" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@PinholeSize" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@PhotometricInterpretation" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Mode" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@ContrastMethod" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:AuxLightSourceRef/@Attenuation" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">AuxLightAttenuation</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:AuxLightSourceRef/@Technique" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">AuxTechnique</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:AuxLightSourceRef/@Wavelength" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">AuxLightWavelength</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "@ExWave" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">ExcitationWavelength</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "@EmWave" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">EmissionWavelength</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "@Fluor" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@NDfilter" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">NDFilter</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:LightSourceRef" mode = "MakeRefs"/>
			<xsl:apply-templates select = "ODE:AuxLightSourceRef" mode = "MakeRefs"/>
			<xsl:apply-templates select = "ODE:OTFRef" mode = "MakeRefs"/>
			<xsl:apply-templates select = "ODE:DetectorRef" mode = "MakeRefs"/>
			<xsl:apply-templates select = "ODE:FilterRef" mode = "MakeRefs"/>
		</xsl:element>
		<xsl:apply-templates select = "ODE:ChannelComponent"/>
	</xsl:template>
	<!-- DisplayOptions - DisplayChannels -->
	<xsl:template match = "ODE:DisplayOptions/*" mode = "MakeDisplayChannel">
		<xsl:element name = "DisplayChannel">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "ChannelNumber">
				<xsl:value-of select = "@ChannelNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "BlackLevel">
				<xsl:value-of select = "@BlackLevel"/>
			</xsl:attribute>
			<xsl:attribute name = "WhiteLevel">
				<xsl:value-of select = "@WhiteLevel"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Gamma" mode = "OptionalAttribute"/>
		</xsl:element>
	</xsl:template>
	<!-- DisplayOptions -->
	<xsl:template match = "ODE:DisplayOptions">
		<xsl:element name = "DisplayOptions">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "DisplayRGB">
				<xsl:choose>
					<xsl:when test="@Display = 'RGB'">
						<xsl:text>true</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>false</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name = "RedChannelOn">
				<xsl:choose>
					<xsl:when test="ODE:RedChannel/@isOn = 'true' or ODE:RedChannel/@isOn = '1'">
						<xsl:text>true</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>false</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name = "GreenChannelOn">
				<xsl:choose>
					<xsl:when test="ODE:GreenChannel/@isOn = 'true' or ODE:GreenChannel/@isOn = '1'">
						<xsl:text>true</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>false</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:attribute name = "BlueChannelOn">
				<xsl:choose>
					<xsl:when test="ODE:BlueChannel/@isOn = 'true' or ODE:BlueChannel/@isOn = '1'">
						<xsl:text>true</xsl:text>
					</xsl:when>
					<xsl:otherwise>
						<xsl:text>false</xsl:text>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
			<xsl:apply-templates select = "@Zoom" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:GreyChannel/@ColorMap" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:Projection/@Zstart" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">ZStart</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:Projection/@Zstop" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">ZStop</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:Time/@Tstart" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">TStart</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:Time/@Tstop" mode = "OptionalAttribute">
				<xsl:with-param name = "Name">TStop</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:RedChannel" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>RedChannel</xsl:text>
				</xsl:with-param>
				<xsl:with-param name = "ID">
					<xsl:value-of select = "generate-id(ODE:RedChannel)"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:GreenChannel" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>GreenChannel</xsl:text>
				</xsl:with-param>
				<xsl:with-param name = "ID">
					<xsl:value-of select = "generate-id(ODE:GreenChannel)"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:BlueChannel" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>BlueChannel</xsl:text>
				</xsl:with-param>
				<xsl:with-param name = "ID">
					<xsl:value-of select = "generate-id(ODE:BlueChannel)"/>
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:GreyChannel" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>GreyChannel</xsl:text>
				</xsl:with-param>
				<xsl:with-param name = "ID">
					<xsl:value-of select = "generate-id(ODE:GreyChannel)"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
		<xsl:apply-templates select = "ODE:RedChannel" mode = "MakeDisplayChannel"/>
		<xsl:apply-templates select = "ODE:GreenChannel" mode = "MakeDisplayChannel"/>
		<xsl:apply-templates select = "ODE:BlueChannel" mode = "MakeDisplayChannel"/>
		<xsl:apply-templates select = "ODE:GreyChannel" mode = "MakeDisplayChannel"/>
		<xsl:apply-templates select = "ODE:ROI"/>
	</xsl:template>
	<!-- DisplayOptions - ROI -->
	<xsl:template match = "ODE:DisplayOptions/ODE:ROI">
		<xsl:element name = "DisplayROI">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "X0">
				<xsl:value-of select = "@X0"/>
			</xsl:attribute>
			<xsl:attribute name = "Y0">
				<xsl:value-of select = "@Y0"/>
			</xsl:attribute>
			<xsl:attribute name = "X1">
				<xsl:value-of select = "@X1"/>
			</xsl:attribute>
			<xsl:attribute name = "Y1">
				<xsl:value-of select = "@Y1"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Z0" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Z1" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@T0" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@T1" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = ".." mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>DisplayOptions</xsl:text>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- PlateRef -->
	<xsl:template match = "ODE:Image/ODE:PlateRef">
		<xsl:element name = "ImagePlate">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Well">
				<xsl:value-of select = "@Well"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Sample" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "." mode = "MakeRefs"/>
		</xsl:element>
	</xsl:template>
	<!-- StageLabel -->
	<xsl:template match = "ODE:StageLabel">
		<xsl:element name = "StageLabel">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@X" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Y" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Z" mode = "OptionalAttribute"/>
		</xsl:element>
	</xsl:template>
	<!-- Pixels -->
	<xsl:template match = "ODE:Pixels">
		<xsl:element name = "Pixels">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<!--xsl:attribute name = "Method">
				<xsl:value-of select = "ODE:DerivedFrom/@Method"/>
			</xsl:attribute-->
			<!--xsl:attribute name = "DimensionOrder">
				<xsl:value-of select = "@DimensionOrder"/>
			</xsl:attribute-->
			<xsl:attribute name = "SizeX">
				<xsl:value-of select = "@SizeX"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeY">
				<xsl:value-of select = "@SizeY"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeZ">
				<xsl:value-of select = "@SizeZ"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeC">
				<xsl:value-of select = "@SizeC"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeT">
				<xsl:value-of select = "@SizeT"/>
			</xsl:attribute>
			<xsl:attribute name = "PixelType">
				<xsl:value-of select = "@PixelType"/>
			</xsl:attribute>
			<!--xsl:attribute name = "BigEndian">
				<xsl:value-of select = "@BigEndian"/>
			</xsl:attribute-->
			<!--xsl:apply-templates select = "ODE:DerivedFrom" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>DerivedFrom</xsl:text>
				</xsl:with-param>
			</xsl:apply-templates-->
		</xsl:element>
	</xsl:template>

	<!--

		Global Attributes

	-->

	<!-- Experimenter -->
	<xsl:template match = "ODE:Experimenter">
		<xsl:element name = "Experimenter">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "FirstName">
				<xsl:value-of select = "ODE:FirstName"/>
			</xsl:attribute>
			<xsl:attribute name = "LastName">
				<xsl:value-of select = "ODE:LastName"/>
			</xsl:attribute>
			<xsl:attribute name = "Email">
				<xsl:value-of select = "ODE:Email"/>
			</xsl:attribute>
			<xsl:attribute name = "Group">
				<xsl:value-of select = "ODE:GroupRef/@ID [1]"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Institution" mode = "OptionalAttribute"/>
		</xsl:element>
		<xsl:apply-templates select = "ODE:GroupRef" mode = "MakeMapRefs"/>
	</xsl:template>
	<!-- Group -->
	<xsl:template match = "ODE:Group">
		<xsl:element name = "Group">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Leader" mode = "MakeRefs">
				<xsl:with-param name = "Name">Leader</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select = "ODE:Contact" mode = "MakeRefs">
				<xsl:with-param name = "Name">Contact</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- Experiment -->
	<xsl:template match = "ODE:Experiment">
		<xsl:element name = "Experiment">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Description" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "ODE:ExperimenterRef" mode = "MakeRefs"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument -->
	<xsl:template match = "ODE:Instrument">
		<xsl:element name = "Instrument">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "ODE:Microscope/@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "ODE:Microscope/@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "SerialNumber">
				<xsl:value-of select = "ODE:Microscope/@SerialNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "ODE:Microscope/@Type"/>
			</xsl:attribute>
		</xsl:element>
		<xsl:apply-templates select = "ODE:LightSource"/>
		<xsl:apply-templates select = "ODE:Detector"/>
		<xsl:apply-templates select = "ODE:Objective"/>
		<xsl:apply-templates select = "ODE:Filter"/>
		<xsl:apply-templates select = "ODE:OTF"/>
	</xsl:template>
	<!-- Instrument - LightSource -->
	<xsl:template match = "ODE:Instrument/ODE:LightSource">
		<xsl:element name = "LightSource">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "SerialNumber">
				<xsl:value-of select = "@SerialNumber"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
		<xsl:apply-templates select = "ODE:Laser"/>
		<xsl:apply-templates select = "ODE:Filament"/>
		<xsl:apply-templates select = "ODE:Arc"/>
	</xsl:template>
	<!-- LightSource - Laser -->
	<xsl:template match = "ODE:LightSource/ODE:Laser">
		<xsl:element name = "Laser">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:attribute name = "Medium">
				<xsl:value-of select = "@Medium"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Wavelength" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@FrequencyDoubled" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Tunable" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Pulse" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Power" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
			<xsl:apply-templates select = "ODE:Pump" mode = "MakeRefs">
				<xsl:with-param name = "Name">
					<xsl:text>Pump</xsl:text>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:element>
	</xsl:template>
	<!-- LightSource - Filament -->
	<xsl:template match = "ODE:LightSource/ODE:Filament">
		<xsl:element name = "Filament">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Power" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- LightSource - Arc -->
	<xsl:template match = "ODE:LightSource/ODE:Arc">
		<xsl:element name = "Arc">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Power" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Dectector -->
	<xsl:template match = "ODE:Instrument/ODE:Detector">
		<xsl:element name = "Detector">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "SerialNumber">
				<xsl:value-of select = "@SerialNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "@Gain" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Voltage" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "@Offset" mode = "OptionalAttribute"/>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Objective -->
	<xsl:template match = "ODE:Instrument/ODE:Objective">
		<xsl:element name = "Objective">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "SerialNumber">
				<xsl:value-of select = "@SerialNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "LensNA">
				<xsl:value-of select = "ODE:LensNA"/>
			</xsl:attribute>
			<xsl:attribute name = "Magnification">
				<xsl:value-of select = "ODE:Magnification"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Filter -->
	<xsl:template match = "ODE:Instrument/ODE:Filter">
		<xsl:element name = "Filter">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
		<xsl:apply-templates select = "ODE:ExFilter"/>
		<xsl:apply-templates select = "ODE:Dichroic"/>
		<xsl:apply-templates select = "ODE:EmFilter"/>
		<xsl:apply-templates select = "ODE:FilterSet"/>
	</xsl:template>
	<!-- Instrument - Filter - ExFilter -->
	<xsl:template match = "ODE:Instrument/ODE:Filter/ODE:ExFilter">
		<xsl:element name = "ExcitationFilter">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "LotNumber">
				<xsl:value-of select = "@LotNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Filter - Dichroic -->
	<xsl:template match = "ODE:Instrument/ODE:Filter/ODE:Dichroic">
		<xsl:element name = "Dichroic">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "LotNumber">
				<xsl:value-of select = "@LotNumber"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Filter - EmFilter -->
	<xsl:template match = "ODE:Instrument/ODE:Filter/ODE:EmFilter">
		<xsl:element name = "EmissionFilter">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "LotNumber">
				<xsl:value-of select = "@LotNumber"/>
			</xsl:attribute>
			<xsl:attribute name = "Type">
				<xsl:value-of select = "@Type"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - Filter - FilterSet -->
	<xsl:template match = "ODE:Instrument/ODE:Filter/ODE:FilterSet">
		<xsl:element name = "FilterSet">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "Manufacturer">
				<xsl:value-of select = "@Manufacturer"/>
			</xsl:attribute>
			<xsl:attribute name = "Model">
				<xsl:value-of select = "@Model"/>
			</xsl:attribute>
			<xsl:attribute name = "LotNumber">
				<xsl:value-of select = "@LotNumber"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
		</xsl:element>
	</xsl:template>
	<!-- Instrument - OTF -->
	<xsl:template match = "ODE:Instrument/ODE:OTF">
		<xsl:element name = "OTF">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeX">
				<xsl:value-of select = "@SizeX"/>
			</xsl:attribute>
			<xsl:attribute name = "SizeY">
				<xsl:value-of select = "@SizeY"/>
			</xsl:attribute>
			<xsl:attribute name = "PixelType">
				<xsl:value-of select = "@PixelType"/>
			</xsl:attribute>
			<xsl:attribute name = "OpticalAxisAverage">
				<xsl:value-of select = "@OpticalAxisAvrg"/>
			</xsl:attribute>
			<xsl:apply-templates select = "." mode = "MakeParentRef"/>
			<xsl:apply-templates select = "ODE:ObjectiveRef" mode = "MakeRefs"/>
			<xsl:apply-templates select = "ODE:FilterRef" mode = "MakeRefs"/>
		</xsl:element>
	</xsl:template>
	<!-- Screen -->
	<xsl:template match = "ODE:Screen">
		<xsl:element name = "Screen">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:attribute name = "ExternalReference">
				<xsl:value-of select = "@ExternRef"/>
			</xsl:attribute>
			<xsl:apply-templates select = "ODE:Description" mode = "OptionalAttribute"/>
		</xsl:element>
	</xsl:template>
	<!-- Plate -->
	<xsl:template match = "ODE:Plate">
		<xsl:element name = "Plate">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
			<xsl:attribute name = "Name">
				<xsl:value-of select = "@Name"/>
			</xsl:attribute>
			<xsl:attribute name = "ExternalReference">
				<xsl:value-of select = "@ExternRef"/>
			</xsl:attribute>
		</xsl:element>
		<xsl:apply-templates select = "ODE:ScreenRef" mode = "MakeMapRefs"/>
	</xsl:template>
	<!--

		Utility Templates

	-->

	<!--
		A utility template to convert child elements and attributes to attributes.
		Does not deal with grand-child elements correctly
	-->
	<xsl:template match = "*" mode = "Element2Attributes">
		<xsl:element name = "{name()}">
			<xsl:if test = "not (string-length(@ID) > 0)">
				<xsl:attribute name = "ID">
					<xsl:value-of select = "generate-id()"/>
				</xsl:attribute>
			</xsl:if>
			<xsl:for-each select = "@*">
				<xsl:if test = "string-length() > 0">
					<xsl:attribute name = "{name()}">
						<xsl:value-of select = "."/>
					</xsl:attribute>
				</xsl:if>
			</xsl:for-each>
			<xsl:for-each select = "*[name() != 'CustomAttributes'][substring(name(),string-length(name())-2,3) != 'Ref']">
				<xsl:attribute name = "{name()}">
					<xsl:value-of select = "."/>
				</xsl:attribute>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
	<!-- A utility template to make a reference to a parent element -->
	<xsl:template match = "*" mode = "MakeParentRef">
		<xsl:apply-templates select = ".." mode = "MakeRefs">
			<xsl:with-param name = "Name">
				<xsl:value-of select = "name(..)"/>
			</xsl:with-param>
		</xsl:apply-templates>
	</xsl:template>
	<!-- A utility template to make references -->
	<xsl:template match = "*" mode = "MakeRefs">
		<!--
		By default, $Name is composed of the element name minus its last three letters
		(i.e. ExperimenterRef element will set $Name to 'Experimenter'.
		-->
		<xsl:param name = "Name" select = "substring(name(),1,string-length(name())-3)"/>
		<xsl:param name = "ID" select = "@ID"/>
		<xsl:attribute name = "{$Name}">
			<xsl:value-of select = "$ID"/>
		</xsl:attribute>
	</xsl:template>
	<!-- A utility template to copy elements and their attributes -->
	<xsl:template match = "*" mode = "CopyRefs">
		<xsl:element name = "{name()}">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "@ID"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!--
		A utility template to make reference maps for many-to-many relationships.
		Given a Reference element (i.e. GroupRef), will construct a map element with two 'Ref' elements -
		one to the parent, and one to the passed-in element.
	-->
	<xsl:template match = "*" mode = "MakeMapRefs">
		<!--
		Defaults:
			$ParentName is the name of the parent element
			$ReferenceName is the name of the element minus the last three letters.
			$Ref1 is $ParentName.
			$Ref2 is $ReferenceName.
			$MapName is composed of the $ParentName concatenated with $ReferenceName.
				(i.e. GroupRef in an Experimenter element will set $MapName to 'ExperimenterGroup'.
			$ID1 is the value of the parent element's $Ref1+'ID' attribute
			$ID2 is the value of the element's $Ref2+'ID' attribute
		-->
		<xsl:param name = "ParentName" select = "name(..)"/>
		<xsl:param name = "ReferenceName" select = "substring(name(),1,string-length(name())-3)"/>
		<xsl:param name = "Ref1" select = "$ParentName"/>
		<xsl:param name = "Ref2" select = "$ReferenceName"/>
		<xsl:param name = "MapName" select = "concat($ParentName,$ReferenceName)"/>
		<xsl:param name = "ID1" select = "../@ID"/>
		<xsl:param name = "ID2" select = "@ID"/>
		<xsl:element name = "{$MapName}">
			<xsl:attribute name = "ID">
				<xsl:value-of select = "generate-id()"/>
			</xsl:attribute>
			<xsl:attribute name = "{$Ref1}">
				<xsl:value-of select = "$ID1"/>
			</xsl:attribute>
			<xsl:attribute name = "{$Ref2}">
				<xsl:value-of select = "$ID2"/>
			</xsl:attribute>
		</xsl:element>
	</xsl:template>

	<xsl:template match = "*" mode = "OptionalAttribute">
		<xsl:param name = "Name" select = "name()"/>
		<xsl:param name = "Value" select = "."/>
		<xsl:if test = "string-length($Value) > 0">
			<xsl:attribute name = "{$Name}">
				<xsl:value-of select = "$Value"/>
			</xsl:attribute>
		</xsl:if>
	</xsl:template>
	<xsl:template match = "@*" mode = "OptionalAttribute">
		<xsl:param name = "Name" select = "name()"/>
		<xsl:param name = "Value" select = "."/>
		<xsl:if test = "string-length($Value) > 0">
			<xsl:attribute name = "{$Name}">
				<xsl:value-of select = "$Value"/>
			</xsl:attribute>
		</xsl:if>
	</xsl:template>

	<!-- This just prints out the names of whatever nodes it gets -->
	<xsl:template match = "*" mode = "print">
		<xsl:value-of select = "name()"/>
	</xsl:template>
	<!-- This copies whatever nodes it gets -->
	<xsl:template match = "*" mode = "copy">
		<xsl:copy/>
	</xsl:template>
</xsl:transform>