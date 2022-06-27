% TestBfCheckJavaPath define test cases for bfCheckJavaPath utility function
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfCheckJavaPath < TestBfMatlab
    
    properties
        status
        version
        maxTime = .1
    end
    
    methods
        function self = TestBfCheckJavaPath(name)
            self = self@TestBfMatlab(name);
        end
        
        function testDefault(self)
            self.status = bfCheckJavaPath();
            assertTrue(self.status);
        end
        
        function testAutoloadOdeformats(self)
            self.status = bfCheckJavaPath(true);
            assertTrue(self.status);
        end
        
        function testNoAutoloadOdeformats(self)
            isStatic = ismember(self.jarPath,...
                javaclasspath('-static'));
            self.status = bfCheckJavaPath(false);
            if isStatic
                assertTrue(self.status);
            else
                assertFalse(self.status);
            end
        end
        
        function testPerformance(self)
            nCounts = 10;
            times = zeros(nCounts);
            for i = 1 : nCounts
                tic;
                bfCheckJavaPath();
                times(i) = toc;
            end
            
            % First call should be the longest as the ODE-Formats JAR file is
            % added to the javaclasspath
            assertTrue(times(1) > times(2));
            % Second call should still be longer than all the following
            % ones. Profiling reveals some amount of time is spent while
            % computing javaclasspath.local_get_static_path
            assertTrue(all(times(2) > times(3:end)));
            % From the third call and onwards, javaclasspath and thus
            % bfCheckJavaPath should return fast
            assertTrue(mean(times(3:end)) < self.maxTime);
        end
        
        function testJavaMethod(self)
            self.status = bfCheckJavaPath(true);
            version = char(loci.formats.FormatTools.VERSION);
            [self.status self.version]= bfCheckJavaPath(false);
            assertEqual(self.version,version);
            if (exist ('OCTAVE_VERSION', 'builtin'))
                version = char(java_get('loci.formats.FormatTools', 'VERSION'));
                assertEqual( self.version, version);
            end
        end
    end
end
