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

"""Test ODE.scripts usage in the webclient."""

from engine.testlib import IWebTest
from engine.testlib import get, post, get_json
import time
import pytest
import json

from django.core.urlresolvers import reverse

class TestScripts(IWebTest):
    """Test ODE.scripts usage in the webclient."""

    # Values that are included in the script below
    default_param_values = {
        'Greeting': 'Hello',
        'Do_Work': False,
        'Row_Count': 12,
        'Names': ['A', 'B'],
        'Channels': [1, 2, 3, 4]
    }

    def upload_script(self):
        """Upload script and return script ID."""
        root_client = self.new_client(system=True)
        scriptService = root_client.sf.getScriptService()
        uuid = self.uuid()

        script = """
import ode
from ode.rtypes import rstring, rlong, wrap
import ode.scripts as scripts
if __name__ == '__main__':
    client = scripts.client(
        'HelloWorld.py', 'Hello World example script',
        scripts.String('Greeting', default='Hello'),
        scripts.Bool('Do_Work', default=False),
        scripts.Int('Row_Count', default=12),
        scripts.List('Names', default=['A', 'B']),
        scripts.List('Channels',
                     default=[1L, 2L, 3L, 4L]).ofType(rlong(0))
    )
    params = client.getInputs(unwrap=True)
    for name, value in params.items():
        client.setOutput(name, wrap(value))
    client.setOutput('Message', wrap("Script Completed"))
    """

        script_id = scriptService.uploadOfficialScript(
            "/test/web/script%s.py" % uuid, script)
        assert script_id is not None
        return script_id

    def test_script_ui_defaults(self):
        """Test script UI html page includes default values."""
        script_id = self.upload_script()
        script_ui_url = reverse('script_ui', kwargs={'scriptId': script_id})
        rsp = get(self.django_client, script_ui_url)
        html = rsp.content
        defaults = self.default_param_values
        expected_values = [
            defaults['Greeting'],
            str(defaults['Row_Count']),
            ','.join([str(c) for c in defaults['Channels']]),
            ','.join(defaults['Names'])
        ]
        for v in expected_values:
            assert ('value="%s"' % v) in html

    @pytest.mark.parametrize("inputs", [{},
                                        {'Greeting': 'Hello World',
                                         'Do_Work': True,
                                         'Row_Count': 6,
                                         'Names': ['One', 'Two', 'Three'],
                                         'Channels': [1, 2]},
                                        {'Names': ['Single'],
                                         'Channels': ['not_a_number']}])
    def test_script_inputs_outputs(self, inputs):
        """Test that inputs and outputs are passed to and from script."""
        script_id = self.upload_script()
        script_run_url = reverse('script_run', kwargs={'scriptId': script_id})

        data = inputs.copy()
        # Lists are submitted as comma-delimited strings
        if data.get('Names'):
            data['Names'] = ','.join(data['Names'])
            data['Channels'] = ','.join([str(c) for c in data['Channels']])
        rsp = post(self.django_client, script_run_url, data)
        rsp = json.loads(rsp.content)
        job_id = rsp['jobId']

        defaults = self.default_param_values.copy()

        # Any inputs we have will replace default values
        defaults.update(inputs)

        # Non numbers will get removed from Long list
        if defaults.get('Channels') == ['not_a_number']:
            defaults['Channels'] = []

        # Ping Activities until done...
        activities_url = reverse('activities_json')
        data = get_json(self.django_client, activities_url)

        # Keep polling activities until no jobs in progress
        while data['inprogress'] > 0:
            time.sleep(0.5)
            data = get_json(self.django_client, activities_url)

        # individual activities/jobs are returned as dicts within json data
        for k, o in data.items():
            # find dict of results from the script job
            if job_id in k:
                assert o['status'] == 'finished'
                assert o['job_name'] == 'HelloWorld'
                assert o['Message'] == 'Script Completed'
                # All inputs should be passed to outputs
                assert o['results'] == defaults
