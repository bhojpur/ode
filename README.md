# Bhojpur ODE - Optical Data Engine

The `Bhojpur ODE` is a high performance optical data processing engine based on
the [Bhojpur.NET Platform](https://github.com/bhojpur/platform) ecosystem for
delivery of distributed `applications` or `services`. It features advanced
visualization (both in 2D and 3D modes) of medical volumetric data, provided in
popular file formats: DICOM, NIfTI, KTX<sup>™</sup>, HDR, etc.

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
WebGL-enabled desktop browsers (Chrome, Firefox, Opera) and allows limited usage
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

You need to install `Python` >= 3.8, `Django`, and [ZeroC ICE](https://doc.zeroc.com/ice/)
for compiling `.ice` interface definition language files.

```bash
pip3 install zeroc-ice
slice2py
```

Now, change directory to `pkg` and issue the following commands.

```bash
cd pkg
python3 ./engine/manage.py runserver
```
