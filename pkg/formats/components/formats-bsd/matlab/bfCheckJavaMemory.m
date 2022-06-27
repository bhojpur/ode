function [] = bfCheckJavaMemory(varargin)
% bfCheckJavaMemory warn if too little memory is allocated to Java
%
% SYNOPSIS  bfCheckJavaMemory()
%
% Input
%
%   minMemory - (Optional) The minimum suggested memory setting in MB.
%   Default: 512
%
% Output
%
%    A warning message is printed if too little memory is allocated.

% Bhojpur ODE-Formats package for reading and converting biological file formats.


runtime = javaMethod('getRuntime', 'java.lang.Runtime');
maxMemory = runtime.maxMemory() / (1024 * 1024);

ip = inputParser;
ip.addOptional('minMemory', 512, @isscalar);
ip.parse(varargin{:});
minMemory = ip.Results.minMemory;

warningID = 'BF:lowJavaMemory';

if maxMemory < minMemory - 64
    warning_msg = [...
        '*** Insufficient memory detected. ***\n'...
        '*** %dm found ***\n'...
        '*** %dm or greater is recommended ***\n'...
        '*** See http://www.mathworks.com/matlabcentral/answers/92813 ***\n'...
        '*** for instructions on increasing memory allocation. ***\n'];
    warning(warningID, warning_msg, round(maxMemory), minMemory);
end
