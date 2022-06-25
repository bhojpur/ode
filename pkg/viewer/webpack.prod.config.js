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

const path = require('path');
const {AureliaPlugin} = require('aurelia-webpack-plugin');
const ProvidePlugin = require('webpack/lib/ProvidePlugin');

module.exports = {
  entry: {
    main: './src/main',
    deps: ['d3', 'file-saver', 'text-encoding']
  },
  output: {
    path: path.join(__dirname, 'build'),
    filename: "[name].js"
  },
  optimization: {
    splitChunks: {
      cacheGroups: {
        commons: {
          test: /[\\/]node_modules[\\/]/,
          name: 'vendors',
          chunks: 'all'
        }
      }
    },
  },
  performance: {
    hints: false
  },
  plugins: [
    new AureliaPlugin({
        aureliaApp: undefined,
        aureliaConfig: "basic",
        features: {svg: false}
    }),
    new ProvidePlugin({
      Promise: 'bluebird',
      $: 'jquery',
      jQuery: 'jquery',
      'window.jQuery': 'jquery'
    })],
  resolve: {
      extensions: [".js"],
      modules: ["src", "libs", "node_modules"]
  },
  module: {
    rules: [
        { test: /\.js$/, loader: 'babel-loader'},
        { test: /[\/\\]node_modules[\/\\]bluebird[\/\\].+\.js$/, loader: 'expose-loader?Promise' },
        { test: require.resolve('jquery'), loader: 'expose-loader?$!expose-loader?jQuery' },
        { test: /\.(png|gif|jpg|jpeg)$/, loader: 'file-loader?name=css/images/[name].[ext]' },
        { test: /\.(woff|woff2)$/, loader: 'file-loader?name=css/fonts/[name].[ext]' },
        { test: /\.css?$/, loader: 'file-loader?name=css/[name].[ext]' },
        { test: /\.html$/, loader: 'html-loader' }
    ]
  }
};