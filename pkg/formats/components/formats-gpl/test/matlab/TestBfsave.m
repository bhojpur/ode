% Integration tests for the bfSave utility function
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfsave < ReaderTest
    
    properties
        path
        I
        dimensionOrder = 'XYZCT'
    end
    
    methods
        function self = TestBfsave(name)
            self = self@ReaderTest(name);
        end
        
        function setUp(self)
            setUp@ReaderTest(self);
            mkdir(self.tmpdir);
            self.path = fullfile(self.tmpdir, 'test.ode.tif');
        end
        
        function tearDown(self)
            if exist(self.tmpdir, 'dir') == 7, rmdir(self.tmpdir, 's'); end
            tearDown@ReaderTest(self);
        end
        
        function checkMinimalMetadata(self)
            % Check dimensions of saved ode-tiff
            self.reader = bfGetReader(self.path);
            d = size(self.I);
            assertEqual(self.reader.getSizeX(), d(2));
            assertEqual(self.reader.getSizeY(), d(1));
            assertEqual(self.reader.getSizeZ(), d(self.dimensionOrder=='Z'));
            assertEqual(self.reader.getSizeC(), d(self.dimensionOrder=='C'));
            assertEqual(self.reader.getSizeT(), d(self.dimensionOrder=='T'));
            assertEqual(char(self.reader.getDimensionOrder()), self.dimensionOrder);
            metadataStore = self.reader.getMetadataStore();
            assertEqual(char(metadataStore.getImageID(0)), 'Image:0');
            assertEqual(char(metadataStore.getPixelsID(0)), 'Pixels:0');
            for i = 1 : d(self.dimensionOrder=='C')
                assertEqual(char(metadataStore.getChannelID(0, i - 1)),...
                    sprintf('Channel:0:%g', i - 1));
            end

        end
        
        % Input check tests
        function testNoInput(self)
            assertExceptionThrown(@() bfsave(),...
                'MATLAB:InputParser:notEnoughInputs');
        end
        
        function testNoOutputPath(self)
            self.I = 1;
            assertExceptionThrown(@() bfsave(self.I),...
                'MATLAB:InputParser:notEnoughInputs');
        end
        
        function testInvalidI(self)
            self.I = 'a';
            assertExceptionThrown(@() bfsave(self.I, self.path),...
                'MATLAB:InputParser:ArgumentFailedValidation');
        end
        
        function testInvalidDimensionOrder(self)
            self.I = 1;
            assertExceptionThrown(@() bfsave(self.I, self.path, 'XY'),...
                'MATLAB:InputParser:ArgumentFailedValidation');
        end
        
        % Dimension order tests
        function testDimensionOrderXYZCT(self)
            self.dimensionOrder = 'XYZCT';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        function testDimensionOrderXYZTC(self)
            self.dimensionOrder = 'XYZTC';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        function testDimensionOrderXYCZT(self)
            self.dimensionOrder = 'XYCZT';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        function testDimensionOrderXYCTZ(self)
            self.dimensionOrder = 'XYCTZ';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        function testDimensionOrderXYTCZ(self)
            self.dimensionOrder = 'XYTCZ';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        function testDimensionOrderXYTZC(self)
            self.dimensionOrder = 'XYTZC';
            self.I = zeros(2, 3, 4, 5, 6);
            bfsave(self.I, self.path, self.dimensionOrder);
            self.checkMinimalMetadata();
        end
        
        % Pixel type tests
        function checkPixelType(self, type)
            self.I = zeros(100, 100, type);
            bfsave(self.I, self.path);
            assertEqual(imread(self.path), self.I);
        end
        
        function testPixelsTypeUINT8(self)
            self.checkPixelType('uint8');
        end
        
        function testPixelsTypeINT8(self)
            self.checkPixelType('int8');
        end
        
        function testPixelsTypeUINT16(self)
            self.checkPixelType('uint16');
        end
        
        function testPixelsTypeINT16(self)
            self.checkPixelType('int16');
        end
        
        function testPixelsTypeUINT32(self)
            self.checkPixelType('uint32');
        end
        
        function testPixelsTypeINT32(self)
            self.checkPixelType('int32');
        end
        
        function testPixelsTypeFLOAT(self)
            self.checkPixelType('single');
        end
        
        function testPixelsTypeDOUBLE(self)
            self.checkPixelType('double');
        end
        
        % Bytes reading tests
        function testSinglePoint(self)
            self.I = 1;
            bfsave(self.I, self.path);
            assertEqual(imread(self.path), self.I);
        end
        
        function testSinglePlane(self)
            self.reader = bfGetReader('plane.fake');
            self.I = bfGetPlane(self.reader, 1);
            bfsave(self.I, self.path);
            assertEqual(imread(self.path), self.I);
        end
        
        % Compression type tests
        function checkCompression(self, type)
            self.reader = bfGetReader('plane.fake');
            self.path = fullfile(self.tmpdir, 'test.ode.tiff');
            self.I = bfGetPlane(self.reader, 1);
            bfsave(self.I, self.path, 'Compression', type);
            if ~ismember(type, {'JPEG-2000' ,'JPEG-2000 Lossy'})
                assertEqual(imread(self.path), self.I);
                inf = imfinfo(self.path);
                if strcmp(type, 'zlib')
                    assertEqual(inf.Compression, 'Deflate');
                else
                    assertEqual(inf.Compression, type);
                end
                assertEqual(imread(self.path), self.I)
            end
        end

        function testJPEG2000(self)
            self.checkCompression('JPEG-2000');
        end
        
        function testJPEG2000Lossy(self)
            self.checkCompression('JPEG-2000 Lossy');
        end
        
        function testUncompressed(self)
            self.checkCompression('Uncompressed');
        end
        
        function testLZW(self)
            self.checkCompression('LZW');
        end

        function testZlib(self)
            self.checkCompression('zlib');
        end
        
        function testInvalidCompressionType(self)
            self.I = 1;
            self.path = fullfile(self.tmpdir, 'test.ode.xml');
            assertExceptionThrown(@() bfsave(self.I, self.path, 'Compression', 'JPEG'),...
                'bfsave:unsupportedCompression');
        end
        % Big-tiff test
        function testBigTiff(self)
            self.I = zeros(100, 100);
            bfsave(self.I, self.path, 'BigTiff', true);
            assertEqual(imread(self.path), self.I);
        end
        
        % Metadata test
        function testCreateMinimalODEXMLMetadata(self)
            self.I = zeros(2, 3, 4, 5, 6);
            metadata = createMinimalODEXMLMetadata(self.I);
            bfsave(self.I, self.path, 'metadata', metadata);
            self.checkMinimalMetadata();
        end
        
        function testAdditionalODEXMLMetadata(self)
            self.I = zeros(2, 3, 4, 5, 6);
            metadata = createMinimalODEXMLMetadata(self.I);
            metadata.setImageDescription('description',0);
            bfsave(self.I, self.path, 'metadata', metadata);
            self.checkMinimalMetadata();
            d = self.reader.getMetadataStore().getImageDescription(0);
            assertEqual(char(d), 'description');
        end
    end
end
