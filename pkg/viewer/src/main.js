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

import { bootstrap } from 'aurelia-bootstrapper';
import {EventAggregator} from 'aurelia-event-aggregator';
import Context from './app/context';
import Index from './app/index';
import Misc from './utils/misc';
import {URI_PREFIX, PLUGIN_NAME, WINDOWS_1252} from './utils/constants';
import * as Bluebird from 'bluebird';

// #if process.env.NODE_ENV
require('../node_modules/bootstrap/dist/css/bootstrap.min.css');
require('../node_modules/jquery-ui/themes/base/theme.css');
require('../node_modules/jquery-ui/themes/base/spinner.css');
require('../node_modules/jquery-ui/themes/base/slider.css');
require('../node_modules/spectrum-colorpicker/spectrum.css');
require('../css/ol3-viewer.css');
require('../css/app.css');
// #endif

// global scope settings
Bluebird.config({ warnings: { wForgottenReturn: false } });
window['encoding-indexes'] = {"windows-1252": WINDOWS_1252};

let req = window.INITIAL_REQUEST_PARAMS || {};

/**
 * Viewer bootstrap function
 * @function
 * @param {Object} aurelia the aurelia instance
 */
bootstrap(function(aurelia) {
    aurelia.use.basicConfiguration();
    let ctx = new Context(aurelia.container.get(EventAggregator), req);
    aurelia.container.registerInstance(Context,ctx);

    aurelia.start().then(
        () => aurelia.setRoot(PLATFORM.moduleName('app/index'), document.body));
});