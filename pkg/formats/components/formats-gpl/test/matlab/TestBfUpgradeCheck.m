% Integration tests for the bfUpgradeCheck utility function
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfUpgradeCheck < ReaderTest
    
    properties
        upgrader
    end
    
    methods
        
        function self = TestBfUpgradeCheck(name)
            self = self@ReaderTest(name);
        end
        
        function tearDown(self)
            self.upgrader = [];
            upgrader = [];
            tearDown@ReaderTest(self);
        end
        
        function testJavaMethod(self)
            self.upgrader = javaObject('loci.formats.UpgradeChecker');
            upgrader = loci.formats.UpgradeChecker();
            assertEqual( self.upgrader.getClass, upgrader.getClass);
        end
        
    end
    
end