% TestBfInitLogging define test cases for bfInitLogging utility function
%
% Require MATLAB xUnit Test Framework to be installed
% http://www.mathworks.com/matlabcentral/fileexchange/22846-matlab-xunit-test-framework
% https://github.com/psexton/matlab-xunit (GitHub source code)

% Bhojpur ODE-Formats package for reading and converting biological file formats.

classdef TestBfInitLogging < TestBfMatlab
    
    properties
        root
    end
    
    methods
        function self = TestBfInitLogging(name)
            self = self@TestBfMatlab(name);
            import org.apache.log4j.Logger;
            self.root = Logger.getRootLogger();
        end
        
        function disableLogging(self)
            self.root.removeAllAppenders();
            bfCheckJavaPath();
            assertFalse(loci.common.DebugTools.isEnabled());
        end
        
        function testDefault(self)
            self.disableLogging();
            bfInitLogging();
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'WARN');
            bfInitLogging('INFO');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'WARN');
        end
        
        function testALL(self)
            self.disableLogging();
            bfInitLogging('ALL');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'ALL');
        end
        
        function testERROR(self)
            self.disableLogging();
            bfInitLogging('ERROR');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'ERROR');
        end
        
        function testDEBUG(self)
            self.disableLogging();
            bfInitLogging('DEBUG');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'DEBUG');
        end
        
        function testINFO(self)
            self.disableLogging();
            bfInitLogging('INFO');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'INFO');
        end
        
        function testFATAL(self)
            self.disableLogging();
            bfInitLogging('FATAL');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'FATAL');
        end
        
        function testOFF(self)
            self.disableLogging();
            bfInitLogging('OFF');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'OFF');
        end
        
        function testTRACE(self)
            self.disableLogging();
            bfInitLogging('TRACE');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'TRACE');
        end
        
        function testWARN(self)
            self.disableLogging();
            bfInitLogging('WARN');
            assertTrue(loci.common.DebugTools.isEnabled());
            assertEqual(char(self.root.getLevel.toString()), 'WARN');
        end

        function testSetRootLevel(self)
            self.disableLogging();
            loci.common.DebugTools.enableLogging();
            assertTrue(loci.common.DebugTools.isEnabled());
            loci.common.DebugTools.setRootLevel('INFO');
            assertEqual(char(self.root.getLevel.toString()), 'INFO');
            loci.common.DebugTools.setRootLevel('DEBUG');
            assertEqual(char(self.root.getLevel.toString()), 'DEBUG');
        end
    end
end
