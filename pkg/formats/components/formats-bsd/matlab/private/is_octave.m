% Bhojpur ODE-Formats package for reading and converting biological file formats.

function is = is_octave ()
is = exist ('OCTAVE_VERSION', 'builtin') == 5;
end
