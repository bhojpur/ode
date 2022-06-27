function [I,idx] = bfGetPlaneAtZCT( r, z, c, t, varargin )
%bfGetPlaneAtCZT Obtains an image plane for the Z, C, T
%
%   I = bfGetPlaneAtZCT(r, z, c, t) returns a specified plane from the input
%   format reader. The indices specifying the plane to retrieve should be
%   contained between 1 and the number of planes for each dimesnion.
%
%   I = bfGetPlaneAtZCT(r, z, c, t, ...) does as above but passes extra
%   arguments to bfGetPlane for tiling, etc.
%
% Examples
%
%    r = bfGetReader('example.tif');
%    I = bfGetPlaneAtZCT(r, 1, 1, 1) % First plane of the series
%    I = bfGetPlaneAtZCT(r,r.getSizeZ(),r.getSizeC(),r.getSizeT()) % Last plane of the series
%    I = bfGetPlaneAtZCT(r, 1, 1, 1, 1, 1, 1, 20, 20) % 20x20 tile originated at (0, 0)
%
% See also: BFGETREADER, BFGETPLANE, loci.formats.IFormatReader.getIndex

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Input check
ip = inputParser;
isValidReader = @(x) isa(x, 'loci.formats.IFormatReader') && ...
    ~isempty(x.getCurrentFile());
ip.addRequired('r', isValidReader);
ip.parse(r);

ip = inputParser;
% No validation because we already checked
% Kept for positional errors
ip.addRequired('r');

% Check ZCT coordinates are within range
ip.addOptional('z',1,@(x) bfTestInRange(x,'z',r.getSizeZ()));
ip.addOptional('c',1,@(x) bfTestInRange(x,'c',r.getSizeC()));
ip.addOptional('t',1,@(x) bfTestInRange(x,'t',r.getSizeT()));

ip.parse(r, z, c, t);

javaIndex = r.getIndex(z-1, c-1, t-1);
I = bfGetPlane(r, javaIndex+1);

end