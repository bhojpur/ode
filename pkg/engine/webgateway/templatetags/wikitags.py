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

import re

from django.template import Library
from django.templatetags.static import PrefixNode

register = Library()

@register.filter
def wikify(value):
    if value is not None:

        urlier = (
            r"(http[s]?://|localhost|ftp://|ftps://)"
            r"(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\)\|,]|"
            r"(?:%[0-9a-fA-F][0-9a-fA-F]))+"
        )
        wikifier = re.compile(r"\b(%s)\b" % urlier, re.IGNORECASE)
        value = wikifier.sub(r'<a href="\1" target="_blank">\1</a>', value)

        return value

    return value

# happy                :) :-)
# wink                 ;) ;-)
# big smile            :-D :D
# tongue sticking out  :-P :P :-p :p
# surprised / o, no    :-o :O
# sad                  :( :-(
# very sad             :-(( :((
# embarrassed          :">
# wearing sunglasses   B-)
# kiss                 =* :-* :*
# confused             :-/ :-\
# angry                X-( x-( X( x(

@register.filter
def sharewikify(value):
    if value is not None:

        WIKI_WORD = (
            r"http[s]?://(?:[a-zA-Z]|[0-9]|[$-_@.&+]|[!*\(\)\|,]|"
            "(?:%[0-9a-fA-F][0-9a-fA-F]))+"
        )
        wikifier = re.compile(r"\b(%s)\b" % WIKI_WORD)
        value = wikifier.sub(r'<a href="\1" target="_blank">\1</a>', value)

        # happy :) :-)
        emot1 = re.compile(r"\:[\-]?\)", re.VERBOSE)
        value = emot1.sub(
            r'<img src="%simage/emots/tinymce_smiley-smile18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # wink ;) ;-)
        emot11 = re.compile(r"\;[\-]?\)", re.VERBOSE)
        value = emot11.sub(
            r'<img src="%simage/emots/tinymce_smiley-wink18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # very sad :-(( :((
        emot22 = re.compile(r"\:[\-]?\(\(", re.VERBOSE)
        value = emot22.sub(
            r'<img src="%simage/emots/tinymce_smiley-cry18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # sad :( :-(
        emot2 = re.compile(r"\:[\-]?\(", re.VERBOSE)
        value = emot2.sub(
            r'<img src="%simage/emots/tinymce_smiley-frown18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # kiss =* :-* :*
        emot3 = re.compile(r"[=\:][\-]?\*", re.VERBOSE)
        value = emot3.sub(
            r'<img src="%simage/emots/tinymce_smiley-kiss18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # big smile :-D :D
        emot4 = re.compile(r"\:[\-]?[dD]", re.VERBOSE)
        value = emot4.sub(
            r'<img src="%simage/emots/tinymce_smiley-laughing18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # tongue sticking out :-P :P :-p :p
        emot5 = re.compile(r"\:[\-]?[pP]", re.VERBOSE)
        value = emot5.sub(
            r'<img src="%simage/emots/tinymce_smiley-tongue-out18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # surprised / o, no :-o :O
        emot6 = re.compile(r"\:[\-]?[oO]", re.VERBOSE)
        value = emot6.sub(
            r'<img src="%simage/emots/tinymce_smiley-surprised18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # embarrassed :">
        emot7 = re.compile(r"\:\"\>", re.VERBOSE)
        value = emot7.sub(
            r'<img src="%simage/emots/tinymce_smiley-embarassed18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # wearing sunglasses B-)
        emot8 = re.compile(r"B\-\)", re.VERBOSE)
        value = emot8.sub(
            r'<img src="%simage/emots/tinymce_smiley-cool18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # confused :-/ :-\
        emot9 = re.compile(r"\:\-[\\/]", re.VERBOSE)
        value = emot9.sub(
            r'<img src="%simage/emots/tinymce_smiley-undecided18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # angry X-( x-( X( x(
        emot9 = re.compile(r"[xX][\-]?\(", re.VERBOSE)
        value = emot9.sub(
            r'<img src="%simage/emots/tinymce_smiley-yell18.gif" />'
            % PrefixNode.handle_simple("STATIC_URL"),
            value,
        )

        # TODO: Beta 4.1
        # quote = re.compile(r'\[quote\](.*)\[/quote\]', re.DOTALL)
        # value = quote.sub(r'<p class="quote">\1</p>', value)

    return value

# happy                :) :-)
# wink                 ;) ;-)
# big smile            :-D :D
# tongue sticking out  :-P :P :-p :p
# surprised / o, no    :-o :O
# sad                  :( :-(
# very sad             :-(( :((
# embarrassed          :">
# wearing sunglasses   B-)
# kiss                 =* :-* :*
# confused             :-/ :-\
# angry                X-( x-( X( x(