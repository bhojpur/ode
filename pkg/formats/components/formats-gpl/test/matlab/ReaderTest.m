% Abstract class for the ODE-Formats Matlab unit tests using readers
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef ReaderTest < TestBfMatlab
    
    properties
        reader
        sizeX
        sizeY
        sizeZ
        sizeC
        sizeT
    end
    
    methods
        function self = ReaderTest(name)
            self = self@TestBfMatlab(name);
        end
        
        function setUp(self)
            setUp@TestBfMatlab(self)
            javaaddpath(self.jarPath);
            self.reader = loci.formats.in.FakeReader();
            self.sizeX = self.reader.DEFAULT_SIZE_X;
            self.sizeY = self.reader.DEFAULT_SIZE_Y;
            self.sizeZ = self.reader.DEFAULT_SIZE_Z;
            self.sizeC = self.reader.DEFAULT_SIZE_C;
            self.sizeT = self.reader.DEFAULT_SIZE_T;
            loci.common.DebugTools.setRootLevel('ERROR');
            import ode.units.UNITS.*;
        end
        
        function tearDown(self)
            if ~isempty(self.reader),
                self.reader.close();
                self.reader = [];
            end
            tearDown@TestBfMatlab(self)
        end
    end
end
