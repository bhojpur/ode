% Tests for the bfCheckJavaMemory utility function
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfCheckJavaMemory < TestBfMatlab
    
    properties
        minMemory
        warning_id = ''
    end
    
    methods
        function self = TestBfCheckJavaMemory(name)
            self = self@TestBfMatlab(name);
        end
        
        % Dimension size tests
        function runJavaMemoryCheck(self)
            lastwarn('');
            bfCheckJavaMemory(self.minMemory)
            [last_warning_msg, last_warning_id] = lastwarn;
            assertEqual(last_warning_id, self.warning_id);
            lastwarn('');
        end
        
        function testZero(self)
            self.minMemory = 0;
            self.runJavaMemoryCheck()
        end
        
        function testMaxMemory(self)
            self.minMemory = self.getRuntime();
            self.runJavaMemoryCheck()
        end
        
        function testLowMemory(self)
            self.minMemory = round(self.getRuntime() + 100);
            self.warning_id = 'BF:lowJavaMemory';
            self.runJavaMemoryCheck()
        end
    end
    methods(Static)
        
        function memory = getRuntime()
            runtime = java.lang.Runtime.getRuntime();
            memory = runtime.maxMemory() / (1024 * 1024);
        end
    end
    
end
