#!/usr/bin/env python
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

"""
Simple command-line searching. Similar to the hql plugin.
"""

from builtins import str
import sys
import time

from Ice import OperationNotExistException
from ode.cli import CLI
from ode.plugins.hql import HqlControl
from ode.rtypes import robject

HELP = """Search for object ids by string.

Examples:

  ode search Image "my-text"
  ode search Image "with wildcard*"
  ode search Project "with wildcard*"

Examples (admin-only):

  ode search --index Image:1
  ode search --index Well:5

"""


class SearchControl(HqlControl):

    def _configure(self, parser):
        parser.add_argument(
            "--index", action="store_true", default=False,
            help="Index an object as a administrator")
        parser.add_argument(
            "--no-parse",
            action="store_true",
            help="Pass the search string directly to Lucene with no parsing")
        parser.add_argument(
            "--field", nargs="*",
            default=(),
            help=("Fields which should be searched "
                  "(e.g. name, description, annotation)"))
        parser.add_argument(
            "--from",
            dest="_from",
            metavar="YYYY-MM-DD",
            type=self.date,
            help="Start date for limiting searches (YYYY-MM-DD)")
        parser.add_argument(
            "--to",
            dest="_to",
            metavar="YYYY-MM-DD",
            type=self.date,
            help="End date for limiting searches (YYYY-MM-DD)")
        parser.add_argument(
            "--date-type",
            default="import",
            choices=("acquisitionDate", "import"),
            help=("Which field to use for --from/--to "
                  "(default: acquisitionDate)"))
        parser.add_argument(
            "type",
            help="Object type to search for, e.g. 'Image' or 'Well'")
        HqlControl._configure(self, parser)
        parser.set_defaults(func=self.search)

    def date(self, user_string):
        try:
            t = time.strptime(user_string, "%Y-%m-%d")
            return time.strftime("%Y%m%d", t)
        except Exception as e:
            self.ctx.dbg(str(e))
            raise

    def search(self, args):
        c = self.ctx.conn(args)

        import ode
        import ode.all

        if args.index:
            if not self.ctx.get_event_context().isAdmin:
                self.ctx.die(432, "Only admin can index object")

            try:
                parts = args.type.split(":")
                kls = parts[0].strip()
                kls = getattr(ode.model, kls)
                kls = kls.ice_staticId()
                of = c.getCommunicator().findObjectFactory(kls)
                obj = of.create(kls)
                id = int(parts[1].strip())
                obj.setId(ode.rtypes.rlong(id))
            except Exception as e:
                self.ctx.dbg(e)
                self.ctx.die(432, "Bad object: %s" % args.type)

            c.sf.getUpdateService().indexObject(obj)

        else:
            group = None
            if args.admin:
                group = "-1"
            ctx = c.getContext(group)
            search = c.sf.createSearchService()
            try:
                try:
                    # Matching ODEGateway.search()
                    search.setAllowLeadingWildcard(True)
                    search.setCaseSensitive(False)
                    search.onlyType(args.type)

                    if args.no_parse:
                        if args._from or args._to or args.field:
                            self.ctx.err("Ignoring from/to/fields")
                        search.byFullText(args.query)
                    else:
                        try:
                            if args.date_type == "import":
                                args.date_type = "details.creationEvent.time"
                            search.byLuceneQueryBuilder(
                                ",".join(args.field),
                                args._from, args._to, args.date_type,
                                args.query, ctx)
                        except OperationNotExistException:
                            self.ctx.err(
                                "Server does not support byLuceneQueryBuilder")
                            search.byFullText(args.query)

                    if not search.hasNext(ctx):
                        self.ctx.die(433, "No results found.")

                    self.ctx.set("search.results", [])
                    while search.hasNext(ctx):
                        results = search.results(ctx)
                        self.ctx.get("search.results").extend(results)
                        results = [[x] for x in results]
                        if not args.ids_only:
                            results = [[robject(x[0])] for x in results]
                        self.display(results,
                                     style=args.style,
                                     idsonly=args.ids_only)
                except ode.ApiUsageException as aue:
                    self.ctx.die(434, aue.message)

            finally:
                search.close()


try:
    register("search", SearchControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("search", SearchControl, HELP)
        cli.invoke(sys.argv[1:])