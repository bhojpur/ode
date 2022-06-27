function test = bfTestInRange(x,name,maxValue)
%bfTestInRange A validation function that tests if the argument
% is scalar integer between 1 and maxValue
%
% This should be faster than ismember(x, 1:maxValue) while also producing
% more readable errors.

% Bhojpur ODE-Formats package for reading and converting biological file formats.

    % Check to see if x is a single value
    test = isscalar(x);
    if(~test)
        error('bfTestInRange:notScalar', ...
            [name ' value, [' num2str(x) '], is not scalar']);
    end

    % Check to see if x is a whole number
    test = mod(x,1) == 0;
    if(~test)
        error('bfTestInRange:notAnInteger', ...
            [name ' value, ' num2str(x) ', is not an integer']);
    end

    % Check to see if x is between 1 and maxValue
    test = x >= 1 && x <= maxValue;
    if(~test)
        error('bfTestInRange:notInSequence', ...
            [name ' value, ' num2str(x) ', is not between 1 and ', ...
            num2str(maxValue)]);
    end
end
