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

""" A view functions is simply a Python function that takes a Web request and
returns a Web response. This response can be the HTML contents of a Web page,
or a redirect, or the 404 and 500 error, or an XML document, or an image...
or anything."""

import sys
import datetime
import traceback
import logging

from django.conf import settings
from django.http import HttpResponse, HttpResponseRedirect, JsonResponse
from django.http import HttpResponseServerError, HttpResponseNotFound
from django.views.defaults import page_not_found
from django.urls import reverse
from django.shortcuts import render

from django.views.debug import get_exception_reporter_filter
from django.utils.encoding import force_text

from engine.feedback.sendfeedback import SendFeedback
from engine.feedback.forms import ErrorForm, CommentForm

logger = logging.getLogger(__name__)

def get_user_agent(request):
    user_agent = ""
    try:
        user_agent = request.META["HTTP_USER_AGENT"]
    except Exception:
        pass
    return user_agent

###############################################################################
def send_feedback(request):
    if not settings.FEEDBACK_ERROR_ENABLED:
        return HttpResponseRedirect(reverse("feedback_disabled"))

    error = None
    form = ErrorForm(data=request.POST.copy())
    if form.is_valid():
        error = form.cleaned_data["error"]
        comment = form.cleaned_data["comment"]
        email = form.cleaned_data["email"]
        try:
            sf = SendFeedback(settings.FEEDBACK_URL)
            sf.send_feedback(
                error=error,
                comment=comment,
                email=email,
                user_agent=get_user_agent(request),
            )
        except Exception as e:
            logger.error("handler500: Feedback could not be sent")
            logger.error(traceback.format_exc())
            error = (
                "Feedback could not be sent. Please contact" " administrator. %s" % e
            )
            fileObj = open(
                ("%s/error500-%s.html" % (settings.LOGDIR, datetime.datetime.now())),
                "w",
            )
            try:
                try:
                    fileObj.write(request.POST["error"])
                except Exception:
                    logger.error("handler500: Error could not be saved.")
                    logger.error(traceback.format_exc())
            finally:
                fileObj.close()
        else:
            if request.is_ajax():
                return HttpResponse(
                    "<h1>Thanks for your feedback</h1><p>You may need to"
                    " refresh your browser to recover from the error</p>"
                )
            return HttpResponseRedirect(reverse("fthanks"))

    context = {"form": form, "error": error}
    return render(request, "500.html", context)


def send_comment(request):
    if not settings.FEEDBACK_COMMENT_ENABLED:
        return HttpResponseRedirect(reverse("feedback_disabled"))

    error = None
    form = CommentForm()

    if request.method == "POST":
        form = CommentForm(data=request.POST.copy())
        if form.is_valid():
            comment = form.cleaned_data["comment"]
            email = form.cleaned_data["email"]
            try:
                sf = SendFeedback(settings.FEEDBACK_URL)
                sf.send_feedback(
                    comment=comment, email=email, user_agent=get_user_agent(request)
                )
            except Exception:
                logger.error("handler500: Feedback could not be sent")
                logger.error(traceback.format_exc())
                error = "Feedback could not be sent." " Please contact administrator."
            else:
                return HttpResponseRedirect(reverse("fthanks"))

    context = {"form": form, "error": error}
    return render(request, "comment.html", context)

##############################################################################
# handlers

def csrf_failure(request, reason=""):
    """
    Always return Json response
    since this is accepted by browser and API users
    """
    error = (
        "CSRF Error. You need to include valid CSRF tokens for any"
        " POST, PUT, PATCH or DELETE operations."
        " You have to include CSRF token in the POST data or"
        " add the token to the HTTP header."
    )
    return JsonResponse({"message": error}, status=403)

def handler500(request):
    """
    Custom error handling.
    Catches errors that are not handled elsewhere.
    NB: This only gets used by Django if engine.web.debug False (production use)
    If debug is True, Django returns it's own debug error page
    """
    logger.error("handler500: Server error")

    as_string = "\n".join(traceback.format_exception(*sys.exc_info()))
    logger.error(as_string)

    try:
        error_filter = get_exception_reporter_filter(request)
        try:
            request_repr = "\n{}".format(
                force_text(error_filter.get_request_repr(request))
            )
        except Exception:
            request_repr = error_filter.get_request_repr(request)
    except Exception:
        try:
            request_repr = repr(request)
        except Exception:
            request_repr = "Request unavailable"

    error500 = "%s\n%s" % (as_string, request_repr)

    # If AJAX, return JUST the error message (not within html page)
    if request.is_ajax():
        return HttpResponseServerError(error500)

    if settings.FEEDBACK_ERROR_ENABLED:
        form = ErrorForm(initial={"error": error500})
        context = {"form": form}
        template = "500.html"
    else:
        context = {"error500": error500}
        template = "500-nosubmit.html"
    return render(request, template, context, status=500)

def handler404(request, exception=None):
    logger.warning(
        "Not Found: %s" % request.path, extra={"status_code": 404, "request": request}
    )
    if request.is_ajax():
        return HttpResponseNotFound()

    return page_not_found(request, "404.html")

def handlerInternalError(request, error):
    """
    This is mostly used in an "object not found" situation,
    So there is no feedback form - simply display "not found" message.
    If the call was AJAX, we return the message in a 404 response.
    Otherwise return an html page, with 404 response.
    """
    logger.warning(
        "Object Not Found: %s" % request.path,
        extra={"status_code": 404, "request": request},
    )

    if request.is_ajax():
        return HttpResponseNotFound(error)

    context = {"error": error}
    return render(request, "error.html", context, status=404)