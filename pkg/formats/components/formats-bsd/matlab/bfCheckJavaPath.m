function [status, version] = bfCheckJavaPath(varargin)
% bfCheckJavaPath check ODE-Formats is included in the Java class path
%
% SYNOPSIS  bfCheckJavaPath()
%           status = bfCheckJavaPath(autoloadOdeFormats)
%           [status, version] = bfCheckJavaPath()
%
% Input
%
%    autoloadOdeFormats - Optional. A boolean specifying the action
%    to take if ODE-Formats is not in the javaclasspath.  If true,
%    tries to find a ODE-Formats jar file and adds it to the java
%    class path.
%    Default - true
%
% Output
%
%    status - Boolean. True if the ODE-Formats classes are in the Java
%    class path.
%
%    version - String specifying the current version of ODE-Formats if
%    ODE-Formats is in the Java class path. Empty string otherwise.

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Input check
ip = inputParser;
ip.addOptional('autoloadOdeFormats', true, @isscalar);
ip.parse(varargin{:});

[status, version] = has_working_odeformats();

if ~status && ip.Results.autoloadOdeFormats,
    jarPath = fullfile(fileparts(mfilename('fullpath')), ...
                       'odeformats_package.jar');
    if (exist(jarPath) == 2)
        javaaddpath(jarPath);
        [status, version] = has_working_odeformats();
        if (~status)
            javarmpath(jarPath);
        end
    end
end

% Return true if odeformats java interface is working, false otherwise.
% Not working will probably mean that the classes are not in
% the javaclasspath.
function [status, version] = has_working_odeformats()

status = true;
version = '';
try
    % If the following fails for any reason, then odeformats is not working.
    % Getting the version number and creating a reader is the bare minimum.
    reader = javaObject('loci.formats.in.FakeReader');
    if is_octave()
        version = char(java_get('loci.formats.FormatTools', 'VERSION'));
    else
        version = char(loci.formats.FormatTools.VERSION);
    end
catch
    status = false;
end
