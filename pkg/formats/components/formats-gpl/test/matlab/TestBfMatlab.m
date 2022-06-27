% Abstract class for the ODE-Formats Matlab unit tests
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfMatlab < TestCase
    
    properties
        jarPath
        tmpdir
    end
    
    methods
        function self = TestBfMatlab(name)
            self = self@TestCase(name);
        end
        
        function setUp(self)
            % Get path to ODE-Formats JAR file (assuming it is in Matlab path)
            self.jarPath = which('odeformats_package.jar');
            assert(~isempty(self.jarPath));
            
            % Remove ODE-Formats JAR file from dynamic class path
            if ismember(self.jarPath,javaclasspath('-dynamic'))
                javarmpath(self.jarPath);
            end
            
            java_tmpdir = char(java.lang.System.getProperty('java.io.tmpdir'));
            uuid = char(java.util.UUID.randomUUID().toString());
            self.tmpdir = fullfile(java_tmpdir, uuid);
        end
        
        function tearDown(self)
            % Remove  ODE-Formats JAR file from dynamic class path
            if ismember(self.jarPath,javaclasspath('-dynamic'))
                javarmpath(self.jarPath);
            end
        end
    end
end
