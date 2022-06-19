/*
 * Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

#ifndef ODE_COLLECTIONS_ICE
#define ODE_COLLECTIONS_ICE

#include <ode/ModelF.ice>
#include <ode/SystemF.ice>
#include <ode/RTypes.ice>
#include <Ice/BuiltinSequences.ice>

/*
 * Defines various sequences and dictionaries used throughout the
 * Bhojpur ODE API. Defining all of these in one central location
 * increases reuse and keeps the library sizes as small as possible.
 *
 * Some collections cannot be defined here since some types are not
 * yet defined.
 */
module ode {

    module api {

        // Forward definition (used in sequences)

        dictionary<string, ode::model::Annotation> SearchMetadata;

        //
        // Primitive Lists
        //

        ["java:type:java.util.ArrayList<String>:java.util.List<String>"]
            sequence<string> StringSet;

        ["java:type:java.util.ArrayList<Long>:java.util.List<Long>"]
            sequence<long> LongList;

        ["java:type:java.util.ArrayList<Integer>:java.util.List<Integer>"]
            sequence<int> IntegerList;

        //
        // Object lists
        //

        ["java:type:java.util.ArrayList"]
            sequence<SearchMetadata> SearchMetadataList;

        ["java:type:java.util.ArrayList<ode.model.Experimenter>:java.util.List<ode.model.Experimenter>"]
            sequence<ode::model::Experimenter> ExperimenterList;

        ["java:type:java.util.ArrayList<ode.model.ExperimenterGroup>:java.util.List<ode.model.ExperimenterGroup>"]
            sequence<ode::model::ExperimenterGroup> ExperimenterGroupList;

        ["java:type:java.util.ArrayList<ode.model.Event>:java.util.List<ode.model.Event>"]
            sequence<ode::model::Event> EventList;

        ["java:type:java.util.ArrayList<ode.model.EventLog>:java.util.List<ode.model.EventLog>"]
            sequence<ode::model::EventLog> EventLogList;

        ["java:type:java.util.ArrayList<ode.model.Annotation>:java.util.List<ode.model.Annotation>"]
            sequence<ode::model::Annotation> AnnotationList;

        ["java:type:java.util.ArrayList<ode.model.Session>:java.util.List<ode.model.Session>"]
            sequence<ode::model::Session> SessionList;

        ["java:type:java.util.ArrayList<ode.model.IObject>:java.util.List<ode.model.IObject>"]
            sequence<ode::model::IObject> IObjectList;

        ["java:type:java.util.ArrayList<ode.model.Project>:java.util.List<ode.model.Project>"]
            sequence<ode::model::Project> ProjectList;

        ["java:type:java.util.ArrayList<ode.model.Dataset>:java.util.List<ode.model.Dataset>"]
            sequence<ode::model::Dataset> DatasetList;

        ["java:type:java.util.ArrayList<ode.model.Image>:java.util.List<ode.model.Image>"]
            sequence<ode::model::Image> ImageList;

        ["java:type:java.util.ArrayList<ode.model.LogicalChannel>:java.util.List<ode.model.LogicalChannel>"]
            sequence<ode::model::LogicalChannel> LogicalChannelList;

        ["java:type:java.util.ArrayList<ode.model.OriginalFile>:java.util.List<ode.model.OriginalFile>"]
            sequence<ode::model::OriginalFile> OriginalFileList;

        ["java:type:java.util.ArrayList<ode.model.Pixels>:java.util.List<ode.model.Pixels>"]
            sequence<ode::model::Pixels> PixelsList;

        ["java:type:java.util.ArrayList<ode.model.PixelsType>:java.util.List<ode.model.PixelsType>"]
            sequence<ode::model::PixelsType> PixelsTypeList;

        ["java:type:java.util.ArrayList<ode.model.Roi>:java.util.List<ode.model.Roi>"]
            sequence<ode::model::Roi> RoiList;

        ["java:type:java.util.ArrayList<ode.model.ScriptJob>:java.util.List<ode.model.ScriptJob>"]
            sequence<ode::model::ScriptJob> ScriptJobList;

        ["java:type:java.util.ArrayList<ode.model.Shape>:java.util.List<ode.model.Shape>"]
            sequence<ode::model::Shape> ShapeList;

        ["java:type:java.util.ArrayList<ode.model.ChecksumAlgorithm>:java.util.List<ode.model.ChecksumAlgorithm>"]
            sequence<ode::model::ChecksumAlgorithm> ChecksumAlgorithmList;

        ["java:type:java.util.ArrayList<ode.model.NamedValue>:java.util.List<ode.model.NamedValue>"]
        sequence<ode::model::NamedValue> NamedValueList;

        ["java:type:java.util.ArrayList<ode.sys.EventContext>:java.util.List<ode.sys.EventContext>"]
        sequence<ode::sys::EventContext> EventContextList;

        // Arrays

        sequence<bool> BoolArray;
        sequence<byte> ByteArray;
        sequence<short> ShortArray;
        sequence<int> IntegerArray;
        sequence<long> LongArray;
        sequence<float> FloatArray;
        sequence<double> DoubleArray;
        sequence<string> StringArray;
        sequence<ByteArray> ByteArrayArray;
        sequence<ShortArray> ShortArrayArray;
        sequence<IntegerArray> IntegerArrayArray;
        sequence<IntegerArrayArray> IntegerArrayArrayArray;
        sequence<LongArray> LongArrayArray;
        sequence<FloatArray> FloatArrayArray;
        sequence<FloatArrayArray> FloatArrayArrayArray;
        sequence<DoubleArray> DoubleArrayArray;
        sequence<DoubleArrayArray> DoubleArrayArrayArray;
        sequence<StringArray> StringArrayArray;
        sequence<RTypeDict> RTypeDictArray;

        // Dictionaries

        dictionary<long,   string>                     LongStringMap;
        dictionary<long,   int>                        LongIntMap;
        dictionary<long,   ByteArray>                  LongByteArrayMap;
        dictionary<long,   ode::model::Pixels>         LongPixelsMap;
        dictionary<int,    string>                     IntStringMap;
        dictionary<int,    IntegerArray>               IntegerIntegerArrayMap;
        dictionary<int,    DoubleArray>                IntegerDoubleArrayMap;
        dictionary<string, ode::RType>                 StringRTypeMap;
        dictionary<string, ode::model::Experimenter>   UserMap;
        dictionary<string, ode::model::OriginalFile>   OriginalFileMap;
        dictionary<string, string>                     StringStringMap;
        dictionary<string, ode::RString>               StringRStringMap;
        dictionary<string, StringArray>                StringStringArrayMap;
        dictionary<string, long>                       StringLongMap;
        dictionary<string, int>                        StringIntMap;

        // if using to store owner and group ID, use first=owner, second=group
        struct LongPair {
          long first;
          long second;
        };

        dictionary<LongPair, long>                     LongPairLongMap;
        dictionary<LongPair, int>                      LongPairIntMap;
        dictionary<LongPair, StringLongMap>            LongPairToStringLongMap;
        dictionary<LongPair, StringIntMap>             LongPairToStringIntMap;

        // Multimaps (dictionaries with sequence values)

        dictionary<string, Ice::LongSeq>               IdListMap;
        dictionary<string, LongList>                   StringLongListMap;
        dictionary<bool,   LongList>                   BooleanLongListMap;
        dictionary<bool,   ode::sys::LongList>         BooleanIdListMap;
        dictionary<string, IObjectList>                IObjectListMap;
        dictionary<long,   IObjectList>                LongIObjectListMap;
        dictionary<string, ShapeList>                  StringShapeListMap;
        dictionary<long,   ShapeList>                  LongShapeListMap;
        dictionary<int,    ShapeList>                  IntShapeListMap;
        dictionary<long,   AnnotationList>             LongAnnotationListMap;
        dictionary<long,   BooleanLongListMap>         IdBooleanLongListMapMap;

    };

};

#endif