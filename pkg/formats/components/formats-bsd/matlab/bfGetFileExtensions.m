function fileExt = bfGetFileExtensions
% bfGetFileExtensions list all extensions supported by ODE-Formats
%
% Synopsis: fileExt = bfGetExtensions()
%
% Input
%      none
%
% Output
%      fileExt:  a cell array of dimensions n x2 where the first colum
%      gives the extension and the second the name of the corresponding
%      format.
%      This cell array is formatted to be used with uigetfile funciton.

% Bhojpur ODE-Formats package for reading and converting biological file formats.

% Get all readers and create cell array with suffixes and names
readers = javaMethod('getReaders', javaObject('loci.formats.ImageReader'));
fileExt = cell(numel(readers), 2);
for i = 1:numel(readers)
    suffixes = readers(i).getSuffixes();
    if is_octave()
        %% FIXME when https://savannah.gnu.org/bugs/?42700 gets fixed
        ExtSuf = cell(numel(suffixes), 1);
        for j = 1:numel(suffixes)
            ExtSuf{j} = char(suffixes(j));
        end
        fileExt{i, 1} = ExtSuf;
    else
        fileExt{i, 1} = arrayfun(@char, suffixes, 'Unif', false);
    end
    fileExt{i, 2} = char(readers(i).getFormat());
end

% Concatenate all unique formats
allExt = unique(vertcat(fileExt{:, 1}));
allExt = allExt(~cellfun(@isempty, allExt));
fileExt = vertcat({allExt, 'All formats'}, fileExt);

% Format file extensions
for i = 1:size(fileExt, 1)
    fileExt{i, 1} = sprintf('*.%s;', fileExt{i, 1}{:});
    fileExt{i, 1}(end) = [];
end
