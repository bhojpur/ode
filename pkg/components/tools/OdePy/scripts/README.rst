Bhojpur ODE Core Scripts
========================

This directory contains Bhojpur ODE scripts which use the
ODE.scripts API. All scripts (e.g. ``*.py``) present in the
directory will be automatically distributed with all binary
builds. Which file-endings will be detected and how they
will be launched are both configured centrally in the server.
``.py``, ``.jy", and ``.m`` (MATLAB) files should all be detected
by default starting with Bhojpur ODE.

Categories
----------

Scripts are separated into several categories, one per directory.

| In the directory       | should be scripts which ...                                                         |
| ----------------       | ---------------------------                                                         |
| **analysis_scripts**   | crunch images to produce numerical results and similar tasks                        |
| **export_scripts**     | take one or more images as an input, and produce a representation for exchange      |
| **figure_scripts**     | take one or more images as an input, and produce a summary representation           |
| **hcs_scripts**        | work with screens/plates/wells rather than just images                              |
| **import_scripts**     | are run on images after import for extra processing                                 |
| **processing_scripts** | create new images from existing images or other data                                |
| **setup_scripts**      | are executed once, often by administrators, to configure Bhojpur ODE itself         |
| **util_scripts**       | perform other miscellaneous tasks like cleaning up or optimizing Bhojpur ODE itself |


Scripts which would like to rely on other scripts can use:

    import ode.<sub_dir>.<script_name>

For this to work, the official script in question must
be properly importable, i.e.:

    def run():
        client = ode.scripts.client(...)

    if __name__ == "__main__":
        run()

Bhojpur ODE User Scripts
------------------------

If you would like to provide your own scripts for others to install
into their Bhojpur ODE installations, please see http://bhojpur.net/info/scripts

Testing
-------

Integration tests under ``test/`` require a Bhojpur ODE server with scripts installed.
The tests are run by Travis for open PRs using ode-test-infra to deploy Bhojpur ODE
via Docker containers.

To run tests locally:

	# All tests
	$ python3 setup.py test

	# Single test in a single file
	$ python3 setup.py test -t test/integration/test_util_scripts.py -k test_dataset_to_plate
