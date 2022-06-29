#!/usr/bin/env python3
# -*- coding: utf-8 -*-

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

import copy
import os

from ode.modeltools.exceptions import ModelProcessingError

TYPE_SOURCE = "source"

class Language(object):
    """
    Base class for output language.
    Updates the type maps with the model namespace.
    """
    def __init__(self, namespace, templatepath):
        self.modelNamespace = namespace
        self._templatepath = templatepath

        # Separator for package/namespace
        self.package_separator = None

        # The default base class for Bhojpur ODE XML model objects.
        self.default_base_class = None

        # A global mapping from XSD Schema types and language types
        # that is used to inform and override type mappings for Bhojpur ODE
        # Model properties which are comprised of XML Schema
        # attributes, elements and Bhojpur ODE XML reference virtual types. It
        # is a superset of primitive_type_map.
        self.type_map = None

        self.fundamental_types = set()

        self.primitive_types = set()

        self.primitive_base_types = set()

        self.base_class = None

        self.template_map = {
            'ENUM': 'ODEXMLModelEnum.template',
            'ENUM_INCLUDEALL': 'ODEXMLModelAllEnums.template',
            'ENUM_HANDLER': 'ODEXMLModelEnumHandler.template',
            'QUANTITY': 'ODEXMLModelQuantity.template',
            'CLASS': 'ODEXMLModelObject.template',
            'METADATA_STORE': 'MetadataStore.template',
            'METADATA_RETRIEVE': 'MetadataRetrieve.template',
            'METADATA_AGGREGATE': 'AggregateMetadata.template',
            'ODEXML_METADATA': 'ODEXMLMetadataImpl.template',
            'DUMMY_METADATA': 'DummyMetadata.template',
            'FILTER_METADATA': 'FilterMetadata.template'
            }

        # A global type mapping from XSD Schema types to language
        # primitive base classes.
        self.primitive_type_map = {
            'PositiveInt': 'PositiveInteger',
            'NonNegativeInt': 'NonNegativeInteger',
            'PositiveLong': 'PositiveLong',
            'NonNegativeLong': 'NonNegativeLong',
            'PositiveFloat': 'PositiveFloat',
            'NonNegativeFloat': 'NonNegativeFloat',
            'PercentFraction': 'PercentFraction',
            'Color': 'Color',
            'Text': 'Text',
            namespace + 'dateTime':   'Timestamp'
            }
            
        # A global type mapping from XSD Schema substitution groups to language abstract classes
        self.abstract_type_map = dict()
        # A global type mapping from XSD Schema abstract classes to their equivalent substitution group
        self.substitutionGroup_map = dict()    

        # A global type mapping from XSD Schema elements to language model
        # object classes.  This will cause source code generation to be
        # skipped for this type since it's implemented natively.
        self.model_type_map = {}

        # A global type mapping from XSD Schema types to base classes
        # that is used to override places in the model where we do not
        # wish subclassing to take place.
        self.base_type_map = {
            'UniversallyUniqueIdentifier': self.getDefaultModelBaseClass(),
            'base64Binary': self.getDefaultModelBaseClass()
            }
        
        # A global set XSD Schema types use as base classes which are primitive  
        self.primitive_base_types = set([
            "base64Binary"])

        self.model_unit_map = {}
        self.model_unit_default = {}

        self.name = None
        self.template_dir = None
        self.source_suffix = None
        self.converter_dir = None
        self.converter_name = None

        self.odexml_model_package = None
        self.odexml_model_enums_package = None
        self.odexml_model_quantity_package = None
        self.odexml_model_odexml_model_enum_handlers_package = None
        self.metadata_package = None
        self.odexml_metadata_package = None

    def _initTypeMap(self):
        self.type_map['Leader'] = 'Experimenter'
        self.type_map['Contact'] = 'Experimenter'
        self.type_map['Pump'] = 'LightSource'

    def getDefaultModelBaseClass(self):
        return None

    def getTemplate(self, name):
        return self.template_map[name]

    def getTemplateDirectory(self):
        return self.template_dir

    def templatepath(self, template):
        return os.path.join(self._templatepath, self.getTemplateDirectory(),
                            self.getTemplate(template))

    def getConverterDir(self):
        return self.converter_dir
        
    def getConverterName(self):
        return self.converter_name
        
    def generatedFilename(self, name, type):
        gen_name = None
        if type == TYPE_SOURCE and self.source_suffix is not None:
            gen_name = name + self.source_suffix
        else:
            raise ModelProcessingError(
                "Invalid language/filetype combination: %s/%s"
                % (self.name, type))
        return gen_name

    def hasBaseType(self, type):
        if type in self.base_type_map:
            return True
        return False

    def baseType(self, type):
        try:
            return self.base_type_map[type]
        except KeyError:
            return None

    def hasFundamentalType(self, type):
        if type in self.fundamental_types:
            return True
        return False

    def hasPrimitiveType(self, type):
        if (type in list(self.primitive_type_map.values()) or
                type in self.primitive_types):
            return True
        return False

    def primitiveType(self, type):
        try:
            return self.primitive_type_map[type]
        except KeyError:
            return None
            
    def hasAbstractType(self, type):
        if (type in self.abstract_type_map):
            return True
        return False

    def abstractType(self, type):
        try:
            return self.abstract_type_map[type]
        except KeyError:
            return None
            
    def hasSubstitutionGroup(self, type):
        if (type in self.substitutionGroup_map):
            return True
        return False

    def substitutionGroup(self, type):
        try:
            return self.substitutionGroup_map[type]
        except KeyError:
            return None
            
    def getSubstitutionTypes(self):
        return list(self.substitutionGroup_map.keys())
            
    def isPrimitiveBase(self, type):
        if type in self.primitive_base_types:
            return True
        else:
            return False

    def hasType(self, type):
        if type in self.type_map:
            return True
        return False

    def type(self, type):
        try:
            return self.type_map[type]
        except KeyError:
            return None

    def index_signature(self, name, max_occurs, level, dummy=False):
        sig = {
            'type': name,
            }

        if name[:2].isupper():
            sig['argname'] = "%sIndex" % name
        else:
            sig['argname'] = "%s%sIndex" % (name[:1].lower(), name[1:])

        return sig

    def index_string(self, signature, dummy=False):
        if dummy is False:
            return "%s %s" % (signature['argtype'], signature['argname'])
        else:
            return "%s /* %s */" % (signature['argtype'], signature['argname'])

    def index_argname(self, signature, dummy=False):
        return signature['argname']


class Java(Language):
    def __init__(self, namespace, templatepath):
        super(Java, self).__init__(namespace, templatepath)

        self.package_separator = '.'

        self.base_class = "Object"

        self.primitive_type_map[namespace + 'boolean'] = 'Boolean'
        self.primitive_type_map[namespace + 'string'] = 'String'
        self.primitive_type_map[namespace + 'integer'] = 'Integer'
        self.primitive_type_map[namespace + 'int'] = 'Integer'
        self.primitive_type_map[namespace + 'long'] = 'Long'
        self.primitive_type_map[namespace + 'float'] = 'Double'
        self.primitive_type_map[namespace + 'double'] = 'Double'
        self.primitive_type_map[namespace + 'anyURI'] = 'String'
        self.primitive_type_map[namespace + 'hexBinary'] = 'String'
        self.primitive_type_map['base64Binary'] = 'byte[]'
        self.primitive_type_map['Map'] = 'List<MapPair>'

        self.model_type_map['Map'] = None
        self.model_type_map['M'] = None
        self.model_type_map['K'] = None
        self.model_type_map['V'] = None

        self.model_unit_map['UnitsLength'] = 'Length'
        self.model_unit_map['UnitsPressure'] = 'Pressure'
        self.model_unit_map['UnitsAngle'] = 'Angle'
        self.model_unit_map['UnitsTemperature'] = 'Temperature'
        self.model_unit_map['UnitsElectricPotential'] = 'ElectricPotential'
        self.model_unit_map['UnitsPower'] = 'Power'
        self.model_unit_map['UnitsFrequency'] = 'Frequency'

        self.model_unit_default['UnitsLength'] = 'UNITS.METRE'
        self.model_unit_default['UnitsTime'] = 'UNITS.SECOND'
        self.model_unit_default['UnitsPressure'] = 'UNITS.PASCAL'
        self.model_unit_default['UnitsAngle'] = 'UNITS.RADIAN'
        self.model_unit_default['UnitsTemperature'] = 'UNITS.KELVIN'
        self.model_unit_default['UnitsElectricPotential'] = 'UNITS.VOLT'
        self.model_unit_default['UnitsPower'] = 'UNITS.WATT'
        self.model_unit_default['UnitsFrequency'] = 'UNITS.HERTZ'

        self.type_map = copy.deepcopy(self.primitive_type_map)
        self._initTypeMap()
        self.type_map['MIMEtype'] = 'String'

        self.name = "Java"
        self.template_dir = "templates/java"
        self.source_suffix = ".java"
        self.converter_name = "MetadataConverter"
        self.converter_dir = "src/main/java/ode/xml/meta"

        self.odexml_model_package = "ode.xml.model"
        self.odexml_model_enums_package = "ode.xml.model.enums"
        self.odexml_model_odexml_model_enum_handlers_package = \
            "ode.xml.model.enums.handlers"
        self.metadata_package = "ode.xml.meta"
        self.odexml_metadata_package = "ode.xml.meta"

        self.units_implementation_is = "ode"
        self.units_package = "ode.units"
        self.units_implementation_imports = \
            "import ode.units.quantity.*;\nimport ode.units.*;"
        self.model_unit_map['UnitsTime'] = 'Time'

    def getDefaultModelBaseClass(self):
        return "AbstractODEModelObject"

    def typeToUnitsType(self, unitType):
        return self.model_unit_map[unitType]

    def typeToDefault(self, unitType):
        return self.model_unit_default[unitType]

    def index_signature(self, name, max_occurs, level, dummy=False):
        """Makes a Java method signature dictionary from an index name."""

        sig = super(Java, self).index_signature(name, max_occurs, level, dummy)
        sig['argtype'] = 'int'

        return sig


def create(language, namespace, templatepath):
    """
    Create a language by name.
    """

    lang = None

    if language == "Java":
        lang = Java(namespace, templatepath)
    else:
        raise ModelProcessingError(
            "Invalid language: %s" % language)

    return lang