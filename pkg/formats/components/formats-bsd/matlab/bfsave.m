function bfsave(varargin)
% BFSAVE Save a 5D matrix into an ODE-TIFF using ODE-Formats library
%
%    bfsave(I, outputPath) writes the input 5D matrix into a new file
%    specified by outputPath.
%
%    bfsave(I, outputPath, dimensionOrder) specifies the dimension order of
%    the input matrix. This value will be ignored if an ODE-XML metadata
%    object is also passed to bfsave via the metadata key/value parameter.
%    Default value is XYZCT.
%
%    bfsave(I, outputPath, 'Compression', compression) specifies the
%    compression to use when writing the ODE-TIFF file.
%
%    bfsave(I, outputPath, 'BigTiff', true) allows to save the file using
%    64-bit offsets
%
%    bfsave(I, outputPath, 'metadata', metadata) allows to use a custom
%    ODE-XML metadata object when saving the file instead of creating a
%    minimal ODE-XML metadata object from the input 5D matrix.
%
%    For more information, see https://docs.bhojpur.net/latest/ode-formats/developers/matlab-dev.html
%
%    Examples:
%
%        bfsave(zeros(100, 100), outputPath)
%        bfsave(zeros(100, 100, 2, 3, 4), outputPath)
%        bfsave(zeros(100, 100, 20), outputPath, 'dimensionOrder', 'XYTZC')
%        bfsave(zeros(100, 100), outputPath, 'Compression', 'LZW')
%        bfsave(zeros(100, 100), outputPath, 'BigTiff', true)
%        bfsave(zeros(100, 100), outputPath, 'metadata', metadata)
%
% See also: BFGETREADER, CREATEMINIMALODEXMLMETADATA

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% verify that enough memory is allocated
bfCheckJavaMemory();

% Check for required jars in the Java path
bfCheckJavaPath();

% Input check
ip = inputParser;
ip.addRequired('I', @isnumeric);
ip.addRequired('outputPath', @ischar);
ip.addOptional('dimensionOrder', 'XYZCT', @(x) ismember(x, getDimensionOrders()));
ip.addParamValue('metadata', [], @(x) isa(x, 'loci.formats.ode.ODEXMLMetadata'));
ip.addParamValue('Compression', '',  @ischar);
ip.addParamValue('BigTiff', false , @islogical);
ip.parse(varargin{:});

% Create Writer object from output path
imageWriter = javaObject('loci.formats.ImageWriter');
writer = imageWriter.getWriter(ip.Results.outputPath);

% Create metadata
if isempty(ip.Results.metadata)
    metadata = createMinimalODEXMLMetadata(ip.Results.I,...
        ip.Results.dimensionOrder);
else
    metadata = ip.Results.metadata;
    if ~ismember('dimensionOrder', ip.UsingDefaults)
        warning('''dimensionOrders'' is ignored if passing ''metadata''');
    end
end

writer.setWriteSequentially(true);
writer.setMetadataRetrieve(metadata);
if ~isempty(ip.Results.Compression)
    compressionTypes = getCompressionTypes(writer);
    if ~ismember(ip.Results.Compression, compressionTypes)
        e = MException('bfsave:unsupportedCompression', ...
            'Unsupported compression: %s.', ip.Results.Compression);
        throw(e);
    end
    writer.setCompression(ip.Results.Compression);
end
if ip.Results.BigTiff
    writer.setBigTiff(ip.Results.BigTiff);
end
writer.setId(ip.Results.outputPath);

% Load conversion tools for saving planes
switch class(ip.Results.I)
    case {'int8', 'uint8'}
        getBytes = @(x) x(:);
    case {'uint16','int16'}
        getBytes = @(x) javaMethod('shortsToBytes', 'loci.common.DataTools', x(:), 0);
    case {'uint32','int32'}
        getBytes = @(x) javaMethod('intsToBytes', 'loci.common.DataTools', x(:), 0);
    case {'single'}
        getBytes = @(x) javaMethod('floatsToBytes', 'loci.common.DataTools', x(:), 0);
    case 'double'
        getBytes = @(x) javaMethod('doublesToBytes', 'loci.common.DataTools', x(:), 0);
end

% Save planes to the writer
nPlanes = metadata.getPixelsSizeZ(0).getValue() *...
    metadata.getPixelsSizeC(0).getValue() *...
    metadata.getPixelsSizeT(0).getValue();
zctCoord = [size(ip.Results.I, 3) size(ip.Results.I, 4)...
    size(ip.Results.I, 5)];
for index = 1 : nPlanes
    [i, j, k] = ind2sub(zctCoord, index);
    plane = ip.Results.I(:, :, i, j, k)';
    writer.saveBytes(index-1, getBytes(plane));
end
writer.close();

end

function dimensionOrders = getDimensionOrders()
% List all values of DimensionOrder
dimensionOrderValues = javaMethod('values', 'ode.xml.model.enums.DimensionOrder');
dimensionOrders = cell(numel(dimensionOrderValues), 1);
for i = 1 :numel(dimensionOrderValues)
    dimensionOrders{i} = char(dimensionOrderValues(i).toString());
end
end

function compressionTypes = getCompressionTypes(writer)
% List all values of Compression
if is_octave()
    %% FIXME when https://savannah.gnu.org/bugs/?42700 gets fixed
    types = writer.getCompressionTypes();
    nTypes = numel(types);
    compressionTypes = cell(nTypes, 1);
    for i = 1:nTypes
        compressionTypes{i} = char(types(i));
    end
else
    compressionTypes = arrayfun(@char, writer.getCompressionTypes(),...
                                'UniformOutput', false);
end
end