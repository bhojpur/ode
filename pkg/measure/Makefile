# Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
# THE SOFTWARE.

DIR ?= /tmp/

all: core/Units.ice core2 core3 core4 core5 formats/UnitsFactory.java model model2 sql xsd

# exclude Angle
units ?= units/ElectricPotential.txt units/Frequency.txt  units/Length.txt  units/Pressure.txt  units/Power.txt  units/Temperature.txt  units/Time.txt
model_enums ?= model/UnitsElectricPotential.java model/UnitsFrequency.java model/UnitsLength.java model/UnitsPressure.java model/UnitsPower.java model/UnitsTemperature.java model/UnitsTime.java
model_objs ?=  model2/ElectricPotential.java     model2/Frequency.java     model2/Length.java     model2/Pressure.java     model2/Power.java     model2/Temperature.java     model2/Time.java
core_ice ?=   core/ElectricPotential.ice       core/Frequency.ice       core/Length.ice       core/Pressure.ice       core/Power.ice       core/Temperature.ice       core/Time.ice
core_java ?=  core/ElectricPotentialI.java     core/FrequencyI.java     core/LengthI.java     core/PressureI.java     core/PowerI.java     core/TemperatureI.java     core/TimeI.java
core_py ?=    core/ode_model_ElectricPotentialI.py     core/ode_model_FrequencyI.py     core/ode_model_LengthI.py     core/ode_model_PressureI.py     core/ode_model_PowerI.py     core/ode_model_TemperatureI.py     core/ode_model_TimeI.py
core_cpp ?=   core/ElectricPotentialI.cpp      core/FrequencyI.cpp      core/LengthI.cpp      core/PressureI.cpp      core/PowerI.cpp      core/TemperatureI.cpp      core/TimeI.cpp 
core_h ?=     core/ElectricPotentialI.h        core/FrequencyI.h        core/LengthI.h        core/PressureI.h        core/PowerI.h        core/TemperatureI.h        core/TimeI.h   

files: $(units)

model:  $(model_enums)
model2: $(model_objs)
sql:    sql/UnitsElectricPotential.sql    sql/UnitsFrequency.sql    sql/UnitsLength.sql    sql/UnitsPressure.sql    sql/UnitsPower.sql    sql/UnitsTemperature.sql    sql/UnitsTime.sql
core2: $(core_ice)
core3: $(core_java)
core4: $(core_py)
core5: $(core_cpp) $(core_h)
xsd:    xsd/units-conversion.xsl

core/Units.ice: $(units)
	mkdir -p core
	./generate.py --combine templates/Units.ice $(units) > $@

formats/UnitsFactory.java: $(units)
	mkdir -p formats
	./generate.py --combine templates/formats.java $(units) > $@

core/%.ice: units/%.txt
	mkdir -p core
	./generate.py templates/core.ice $< > $@

core/%I.java: units/%.txt
	mkdir -p core
	./generate.py templates/core.java $< > $@

core/ode_model_%I.py: units/%.txt
	mkdir -p core
	./generate.py templates/core.py $< > $@

core/%I.cpp: units/%.txt
	mkdir -p core
	./generate.py templates/core.cpp $< > $@

core/%I.h: units/%.txt
	mkdir -p core
	./generate.py templates/core.h $< > $@

model/Units%.java: units/%.txt
	mkdir -p model
	./generate.py templates/model $< > $@

model2/%.java: units/%.txt
	mkdir -p model2
	./generate.py templates/model.java $< > $@

sql/Units%.sql: units/%.txt
	mkdir -p sql
	./generate.py templates/sql $< > $@

xsd/units-conversion.xsl: $(units)
	mkdir -p xsd
	./generate.py --markup --combine templates/xsd $(units) > $@

move:
	/bin/mv $(model_enums) $(DIR)/components/model/src/ode/model/enums
	/bin/mv $(model_objs) $(DIR)/components/model/src/ode/model/units
	/bin/mv $(core_ice) $(DIR)/components/core/resources/ode/model/
	/bin/mv $(core_java) $(DIR)/components/core/src/ode/model/
	/bin/mv core/Units.ice $(DIR)/components/core/resources/ode/model
	/bin/mv $(core_py) $(DIR)/components/tools/OdePy/src
	/bin/mv $(core_cpp) $(DIR)/components/tools/OdeCpp/src/ode/model
	/bin/mv $(core_h) $(DIR)/components/tools/OdeCpp/src/ode/model

bfmove:
	/bin/mv xsd/units-conversion.xsl $(BFDIR)/components/specification/transforms


clean:
	rm -rf core formats model model2 sql xsd

.PHONY: sql files model clean mov