// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

#include <map>
#include <string>
#include <ode/model/PermissionsI.h>
#include <ode/fixture.h>

using namespace std;

TEST(ClientTest, UnconfiguredClient )
{
  int argc = 1;
  std::string host("--ode.host=localhost");
  char* argv[] = {&host[0], 0};
  ode::client_ptr c = new ode::client(argc, argv);
}

TEST(ClientTest, ClientWithInitializationData )
{
  int argc = 0;
  char** argv = {0};
  Ice::InitializationData id;
  id.properties = Ice::createProperties();
  id.properties->setProperty("ode.host","localhost");
  ode::client_ptr c = new ode::client(argc,argv,id);
}

TEST(ClientTest, ClientWithInitializationData2 )
{
  int argc = 2;
  const char* argv[] = {"program", "--ode.host=localhost",0};
  Ice::StringSeq args = Ice::argsToStringSeq(argc, const_cast<char**>(argv));
  Ice::InitializationData id;
  id.properties = Ice::createProperties(argc, const_cast<char**>(argv));
  id.properties->parseCommandLineOptions("ode", args);
  ode::client_ptr c = new ode::client(id);
  std::string s = c->getProperty("ode.host");
  ASSERT_EQ("localhost", s);
}

TEST(ClientTest, BlockSizeDefault)
{
  int argc = 0;
  char* argv[] = {0};
  ode::client_ptr c = new ode::client(argc, argv);
  ASSERT_EQ(5000000, c->getDefaultBlockSize());
}

TEST(ClientTest, BlockSize1MB)
{

  int argc = 1;
  std::string blocksize("--ode.block_size=1000000");
  char* argv[] = {&blocksize[0], 0};
  ode::client_ptr c = new ode::client(argc, argv);
  ASSERT_EQ(1000000, c->getDefaultBlockSize());
}

TEST(ClientTest, testCreateFromMap)
{
    map<string, string> props;
    props["ode.host"] = "localhost";

    ode::client_ptr client = new ode::client(props, false);
    std::string s = client->getProperty("ode.host");
    ASSERT_EQ("localhost", s);
}
