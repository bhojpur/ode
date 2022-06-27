function metadata = createMinimalODEXMLMetadata(I, varargin)
% CREATEMINIMALODEXMLMETADATA Create an ODE-XML metadata object from an input matrix
%
%    createMinimalODEXMLMetadata(I) creates an ODE-XML metadata object from
%    an input 5-D array. Minimal metadata information is stored such as the
%    pixels dimensions, dimension order and type. The output object is a
%    metadata object of type loci.formats.ode.ODEXMLMetadata.
%
%    createMinimalODEXMLMetadata(I, dimensionOrder) specifies the dimension
%    order of the input matrix. Default valuse is XYZCT.
%
%    Examples:
%
%        metadata = createMinimalODEXMLMetadata(zeros(100, 100));
%        metadata = createMinimalODEXMLMetadata(zeros(10, 10, 2), 'XYTZC');
%
% See also: BFSAVE

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Not using the inputParser for first argument as it copies data
assert(isnumeric(I), 'First argument must be numeric');

% Input check
ip = inputParser;
ip.addOptional('dimensionOrder', 'XYZCT', @(x) ismember(x, getDimensionOrders()));
ip.parse(varargin{:});

% Create metadata
toInt = @(x) javaObject('ode.xml.model.primitives.PositiveInteger', ...
                        javaObject('java.lang.Integer', x));
ODEXMLService = javaObject('loci.formats.services.ODEXMLServiceImpl');
metadata = ODEXMLService.createODEXMLMetadata();
metadata.createRoot();
metadata.setImageID('Image:0', 0);
metadata.setPixelsID('Pixels:0', 0);
metadata.setPixelsBigEndian(javaObject('java.lang.Boolean', 'TRUE'), 0);

% Set dimension order
dimensionOrderEnumHandler = javaObject('ode.xml.model.enums.handlers.DimensionOrderEnumHandler');
dimensionOrder = dimensionOrderEnumHandler.getEnumeration(ip.Results.dimensionOrder);
metadata.setPixelsDimensionOrder(dimensionOrder, 0);

% Set pixels type
pixelTypeEnumHandler = javaObject('ode.xml.model.enums.handlers.PixelTypeEnumHandler');
if strcmp(class(I), 'single')
    pixelsType = pixelTypeEnumHandler.getEnumeration('float');
else
    pixelsType = pixelTypeEnumHandler.getEnumeration(class(I));
end
metadata.setPixelsType(pixelsType, 0);

% Read pixels size from image and set it to the metadat
sizeX = size(I, 2);
sizeY = size(I, 1);
sizeZ = size(I, find(ip.Results.dimensionOrder == 'Z'));
sizeC = size(I, find(ip.Results.dimensionOrder == 'C'));
sizeT = size(I, find(ip.Results.dimensionOrder == 'T'));
metadata.setPixelsSizeX(toInt(sizeX), 0);
metadata.setPixelsSizeY(toInt(sizeY), 0);
metadata.setPixelsSizeZ(toInt(sizeZ), 0);
metadata.setPixelsSizeC(toInt(sizeC), 0);
metadata.setPixelsSizeT(toInt(sizeT), 0);

% Set channels ID and samples per pixel
for i = 1: sizeC
    metadata.setChannelID(['Channel:0:' num2str(i-1)], 0, i-1);
    metadata.setChannelSamplesPerPixel(toInt(1), 0, i-1);
end

end

function dimensionOrders = getDimensionOrders()
% List all values of DimensionOrder
dimensionOrderValues = javaMethod('values', 'ode.xml.model.enums.DimensionOrder');
dimensionOrders = cell(numel(dimensionOrderValues), 1);
for i = 1 :numel(dimensionOrderValues),
    dimensionOrders{i} = char(dimensionOrderValues(i).toString());
end
end
