# Bhojpur ODE - Optical Data Engine

The `Bhojpur ODE` is a high performance optical data processing engine based on
the [Bhojpur.NET Platform](https://github.com/bhojpur/platform) ecosystem for
delivery of distributed `applications` or `services`. It features advanced
visualization (both in 2D and 3D modes) of medical volumetric data, provided in
popular file formats: `DICOM`, `NIfTI`, `KTX`<sup>™</sup>, `HDR`, etc.

## Motivation

The `Bhojpur ODE` could be used in a medical center with diagnostic equipment.

- ⚡️ MRI and CT scanning results could be viewed in any computer (inside the medical
office and in the client's home)
- 💡 Qualified medical personnel could provide diagnosis or conclusion based on
provided visualization.
- :globe_with_meridians: App could be used as a web application as a part of large
websites and services
- :mortar_board: Could be used by research institutes due to NIfTI, HDR, etc. file
standards support
- :hospital: Medical centers can create their clients database, based on this viewer.

## Installation

It works as a standalone HTML5 web application. The latest version can be used with
`WebGL`-enabled desktop browsers (Chrome, Firefox, Opera) and allows limited usage
with mobile browsers (Android Chrome). Version for Safari (macOS, iOS) is planned for
future.

```bash
npm install
npm run start
```

## References

The `DICOM` file format description can be found [here](http://dicom.nema.org/standard.html)
and [here](https://www.leadtools.com/sdk/medical/dicom-spec). KTX<sup>™</sup> file format
details are listed in [KTX](https://www.khronos.org/registry/KTX/specs/1.0/ktxspec_v1.html).
Popular DICOM loader framework: [GDCM](https://sourceforge.net/projects/gdcm/).
Some JavaScript libraries to work with DICOM file format:

1. [dicomParser](https://github.com/chafey/dicomParser)
2. [Daikon](https://github.com/rii-mango/Daikon)
3. [Xtk](https://github.com/xtk/X#readme).

## 3D Volumetric Rendering

The `three.js` is used as some gateway to `WebGL` renderer. The current `three.js` version does
not support 3D textures, so we use tricky way to build 2D texture from initial 3D texture by
linking 2D slices all together as a large tile map. This idea (with source codes) can be seen
in project [WebGL Volume Rendering](https://github.com/lebarba/WebGLVolumeRendering).

## Build Source Code

The `Bhojpur ODE` platform core components is developed using several software technologies
(e.g. [Ivy](https://ant.apache.org/ivy/), [Groovy](https://groovy-lang.org), [Gradle](https://gradle.org),
[Ice](https://zeroc.com/products/ice), [Python](https://www.python.org),
[Node.js](https://nodejs.org), [Java](https://www.java.com)), [Spring](https://spring.io).

Based on a Gradle [ice builder plugin](https://github.com/zeroc-ice/ice-builder-gradle),
we have created a custom Gradle plugin for the Bhojpur ODE.

### Pre-requisites

You need to install [ZeroC ICE](https://doc.zeroc.com/ice/) middleware for compiling 
`.ice` interface definition language files. For example, on a `macOS` operating system

```bash
brew install zeroc-ice/tap/ice
brew install ice
brew install zeroc-ice/tap/ice-builder-xcode
slice2cpp -v
slice2java -v
sudo pip3 install zeroc-ice
slice2py -v
brew install --cask zeroc-ice/tap/icegridgui
export ODEDIR==$(pwd)
```

The ICE tools generate `Java` source code for the *server-side* and `Python` source code
for the *client-side* frameworks.

### Source Code Generator

You need to install `Python` >= 3.8, `Django`, `appdirs`, `Pillow`, `numpy`, `tables`,
`mox3`, and other software libraries.

```bash
sudo pip3 install appdirs django Pillow numpy tables mox3
sudo pip3 install -U -r requirements.txt
```

### Server-side Framework

It is based on `Java` >= 1.8 and `Spring Framework`. The Bhojpur ODE server is designed
to securely store, retrieve, and process digital images efficiently.

### Client-side Framework

It is based on `Python` >= 3.8 and `Ice` >= 3.7. The client is designed to serve image
data efficiently using web-based APIs. You could use the [odectl](/cmd/odectl) tool as
a starting point to interact with the Bhojpur ODE server.

```bash
export ODE_HOME==$(pwd)
```

### Web Forms

You can build it using `node.js`

```bash
cd pkg/webui
npm install
node_modules/webpack/bin/webpack.js --progress
```

### Web Application

Now, change directory to `pkg` and issue the following commands.

```bash
cd pkg
python3 ./engine/manage.py runserver 0:8000
```
