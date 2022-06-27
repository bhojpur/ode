function r = bfGetReader(varargin)
% BFGETREADER return a reader for a microscopy image using ODE-Formats
%
%   r = bfGetReader() creates an empty ODE-Formats reader extending
%   loci.formats.ReaderWrapper.
%
%   r = bfGetReader(id) where id is a path to an existing file creates and
%   initializes a reader for the input file.
%
% Examples
%
%    r = bfGetReader()
%    I = bfGetReader(path_to_file)
%
%
% See also: BFGETPLANE

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Input check
ip = inputParser;
ip.addOptional('id', '', @ischar);
ip.addOptional('stitchFiles', false, @isscalar);
ip.parse(varargin{:});
id = ip.Results.id;

% verify that enough memory is allocated
bfCheckJavaMemory();

% load the ODE-Formats library into the MATLAB environment
status = bfCheckJavaPath();
assert(status, ['Missing ODE-Formats library. Either add odeformats_package.jar '...
    'to the static Java path or add it to the Matlab path.']);

% Check if input is a fake string
isFake = strcmp(id(max(1, end - 4):end), '.fake');

if ~isempty(id) && ~isFake
    % Check file existence using fileattrib
    [status, f] = fileattrib(id);
    assert(status && f.directory == 0, 'bfGetReader:FileNotFound',...
        'No such file: %s', id);
    id = f.Name;
end

% set LuraWave license code, if available
if exist('lurawaveLicense', 'var')
    path = fullfile(fileparts(mfilename('fullpath')), 'lwf_jsdk2.6.jar');
    javaaddpath(path);
    javaMethod('setProperty', 'java.lang.System', ...
               'lurawave.license', lurawaveLicense);
end

% Create a loci.formats.ReaderWrapper object
r = javaObject('loci.formats.ChannelSeparator', ...
               javaObject('loci.formats.ChannelFiller'));
if ip.Results.stitchFiles
    r = javaObject('loci.formats.FileStitcher', r);
end

% Initialize the metadata store
ODEXMLService = javaObject('loci.formats.services.ODEXMLServiceImpl');
r.setMetadataStore(ODEXMLService.createODEXMLMetadata());

% Initialize the reader
if ~isempty(id), r.setId(id); end
