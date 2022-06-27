function bfUpgradeCheck(varargin)
% Check for new version of ODE-Formats and update it if applicable
%
% SYNOPSIS: bfUpgradeCheck(autoDownload, 'STABLE')
%
% Input
%    autoDownload - Optional. A boolean specifying of the latest version
%    should be downloaded
%
%    versions -  Optional: a string sepecifying the version to fetch.
%    Should be either trunk, daily or stable (case insensitive)
%
% Output
%    none

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Check input
ip = inputParser;
ip.addOptional('autoDownload', false, @isscalar);
versions = {'stable', 'daily', 'trunk'};
ip.addOptional('version', 'STABLE', @(x) any(strcmpi(x, versions)))
ip.parse(varargin{:})

% Create UpgradeChecker
upgrader = javaObject('loci.formats.UpgradeChecker');
if upgrader.alreadyChecked(), return; end

% Check for new version of ODE-Formats
if is_octave()
    caller = 'Octave';
else
    caller = 'MATLAB';
end
if ~ upgrader.newVersionAvailable(caller)
    fprintf('*** odeformats_package.jar is up-to-date ***\n');
    return;
end

fprintf('*** A new stable version of ODE-Formats is available ***\n');
% If appliable, download new version of OdeFormats
if ip.Results.autoDownload
    fprintf('*** Downloading... ***');
    path = fullfile(fileparts(mfilename('fullpath')), 'odeformats_package.jar');
    buildName = [upper(ip.Results.version) '_BUILD'];
    upgrader.install(loci.formats.UpgradeChecker.(buildName), path);
    fprintf('*** Upgrade will be finished when MATLAB is restarted ***\n');
end
