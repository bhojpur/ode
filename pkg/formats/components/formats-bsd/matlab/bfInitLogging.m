function bfInitLogging(varargin)
% BFINITLOGGING initializes the ODE-Formats logging system
%
%   bfInitLogging() initializes the logging system at WARN level by default.
%
%   bfInitLogging(level) sets the logging level to use when initializing
%   the logging system
%
% Examples
%
%    bfInitLogging();
%    bfInitLogging('DEBUG');

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Check ODE-Formats is set in the Java class path
bfCheckJavaPath();

% Input check
levels = {'ALL', 'DEBUG', 'ERROR', 'FATAL', 'INFO', 'OFF', 'TRACE', 'WARN'};
ip = inputParser;
ip.addOptional('level', 'WARN', @(x) ismember(x, levels));
ip.parse(varargin{:});

% Set logging level
javaMethod('enableLogging', 'loci.common.DebugTools', ip.Results.level);
