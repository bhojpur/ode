# Bhojpur ODE - Web Client
==========================

Introduction
------------

The Bhojpur ODE.web provides a web based client and plugin infrastructure.

Dependencies
------------

Direct dependencies of ODE.web are:

- `ODE.py`_
- `ZeroC IcePy`_
- `Pillow`_
- `NumPy`_
- A WSGI capable web server

Installation
------------

See: `ODE`_ documentation

Usage
-----

See: `OMDE`_ documentation

Contributing
------------

See: `ODE`_ documentation

Running tests
-------------

The tests are located under the `test` directory. To run all the tests, use
the `test` target of `build.py` run from the root of your repository::

  ./build.py -f components/tools/OdeWeb/build.xml test

Unit tests
^^^^^^^^^^

Unit tests are stored under the `test/unit` folder and can be run by calling::

  ./build.py -f components/tools/OdeWeb/build.xml test

Integration tests
^^^^^^^^^^^^^^^^^

Integration tests are stored under `test/integration` and depend on the
ODE integration testing framework.  They can be run by calling::

  ./build.py -f components/tools/OdeWeb/build.xml integration

Reading about `Running and writing tests`_ in the `ODE`_ documentation
is essential.
