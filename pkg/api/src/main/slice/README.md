# Bhojpur ODE - Application Programming Interface

The `Bhojpur ODE` has set of *application programming interface* specifications
written in [ZeroC ICE](https://zeroc.com) *interface definition language*. It
is a service *contract* between the Bhojpur ODE `client` and `server`. These
applications communication using `ICE` protocol.

## Java Server Bindings

The *server-side* framework is built using `Java` programming language and the
`Spring Framework`. Go to the `pkg/api/src/main/slice` folder. Firtsly, you need
to compile all the `.ice` files into Java source code. by issuing the following
commands in a new Terminal window. For example

```bash
cd pkg/api/src/main/slice
mkdir -p ../java
slice2java --output-dir ../java -I. ode/*.ice
```

## Python Client Bindings

The *client-side* framework is built using `Python` programming langauge. Go to
the `pkg/api/src/main/slice` folder. Firstly, you need to compile all the `.ice`
files into `Python` source code by issuing the following commands in a Terminal
window. For example

```bash
cd pkg/api/src/main/slice
mkdir -p ../python
slice2py --all --prefix ode_ --output-dir ../python -I. ode/*.ice
slice2py --all --prefix ode_ --output-dir ../python -I. ode/api/*.ice
slice2py --all --prefix ode_ --output-dir ../python -I. ode/cmd/*.ice
slice2py --all --prefix ode_ --output-dir ../python -I. ode/model/*.ice
```
