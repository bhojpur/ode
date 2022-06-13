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

import os.path
import sys
import logging
from ode import ode
import engine.config
import engine.clients
import tempfile
import re
import json
import pytz
import random
import string
from builtins import str as text

from extras import portalocker
from engine.util.concurrency import get_event
from engine.utils import sort_properties_to_tuple
from engine.connector import Server

logger = logging.getLogger(__name__)

# LOGS
# NEVER DEPLOY a site into production with DEBUG turned on.
# Debuging mode.
# A boolean that turns on/off debug mode.
# handler404 and handler500 works only when False
if "ODE_HOME" in os.environ:
    logger.warn("ODE_HOME usage is ignored in Bhojpur ODE web")

ODEDIR = os.environ.get("ODEDIR")
if not ODEDIR:
    raise Exception("ERROR: ODEDIR not set")

# Logging
LOGDIR = os.path.join(ODEDIR, "var", "log").replace("\\", "/")

if not os.path.isdir(LOGDIR):
    try:
        os.makedirs(LOGDIR)
    except Exception:
        exctype, value = sys.exc_info()[:2]
        raise exctype(value)

# DEBUG: Never deploy a site into production with DEBUG turned on.
# Logging levels: logging.DEBUG, logging.INFO, logging.WARNING, logging.ERROR
# logging.CRITICAL
# FORMAT: 2010-01-01 00:00:00,000 INFO  [engine.webadmin.webadmin_utils]
# (proc.1308 ) getGuestConnection:20 Open connection is not available

STANDARD_LOGFORMAT = (
    "%(asctime)s %(levelname)5.5s [%(name)40.40s]"
    " (proc.%(process)5.5d) %(funcName)s():%(lineno)d %(message)s"
)

FULL_REQUEST_LOGFORMAT = (
    "%(asctime)s %(levelname)5.5s [%(name)40.40s]"
    " (proc.%(process)5.5d) %(funcName)s():%(lineno)d"
    " HTTP %(status_code)d %(request)s"
)

LOGGING_CLASS = "extras.cloghandler.ConcurrentRotatingFileHandler"
LOGSIZE = 500000000

LOGGING = {
    "version": 1,
    "disable_existing_loggers": False,
    "formatters": {
        "standard": {"format": STANDARD_LOGFORMAT},
        "full_request": {"format": FULL_REQUEST_LOGFORMAT},
    },
    "filters": {
        "require_debug_false": {
            "()": "django.utils.log.RequireDebugFalse",
        },
        "require_debug_true": {
            "()": "django.utils.log.RequireDebugTrue",
        },
    },
    "handlers": {
        "default": {
            "level": "DEBUG",
            "class": LOGGING_CLASS,
            "filename": os.path.join(LOGDIR, "ODEweb.log").replace("\\", "/"),
            "maxBytes": LOGSIZE,
            "backupCount": 10,
            "formatter": "standard",
        },
        "request_handler": {
            "level": "DEBUG",
            "class": LOGGING_CLASS,
            "filename": os.path.join(LOGDIR, "ODEweb.log").replace("\\", "/"),
            "maxBytes": LOGSIZE,
            "backupCount": 10,
            "filters": ["require_debug_false"],
            "formatter": "full_request",
        },
        "console": {
            "level": "INFO",
            "filters": ["require_debug_true"],
            "class": "logging.StreamHandler",
            "formatter": "standard",
        },
        "mail_admins": {
            "level": "ERROR",
            "filters": ["require_debug_false"],
            "class": "django.utils.log.AdminEmailHandler",
        },
    },
    "loggers": {
        "django.request": {  # Stop SQL debug from logging to main logger
            "handlers": ["default", "request_handler", "mail_admins"],
            "level": "DEBUG",
            "propagate": False,
        },
        "django": {"handlers": ["console"], "level": "DEBUG", "propagate": True},
        "": {"handlers": ["default"], "level": "DEBUG", "propagate": True},
    },
}

CONFIG_XML = os.path.join(ODEDIR, "etc", "grid", "config.xml")
count = 10
event = get_event("websettings")

while True:
    try:
        CUSTOM_SETTINGS = dict()
        if os.path.exists(CONFIG_XML):
            CONFIG_XML = engine.config.ConfigXml(CONFIG_XML, read_only=True)
            CUSTOM_SETTINGS = CONFIG_XML.as_map()
            CONFIG_XML.close()
        break
    except portalocker.LockException:
        # logger.error("Exception while loading configuration retrying...",
        # exc_info=True)
        exctype, value = sys.exc_info()[:2]
        count -= 1
        if not count:
            raise exctype(value)
        else:
            event.wait(1)  # Wait a total of 10 seconds
    except Exception:
        # logger.error("Exception while loading configuration...",
        # exc_info=True)
        exctype, value = sys.exc_info()[:2]
        raise exctype(value)

del event
del count
del get_event

WSGI = "wsgi"
WSGITCP = "wsgi-tcp"
WSGI_TYPES = (WSGI, WSGITCP)
DEVELOPMENT = "development"
DEFAULT_SERVER_TYPE = WSGITCP
ALL_SERVER_TYPES = (WSGI, WSGITCP, DEVELOPMENT)

DEFAULT_SESSION_ENGINE = "engine.filesessionstore"
SESSION_ENGINE_VALUES = (
    "engine.filesessionstore",
    "django.contrib.sessions.backends.db",
    "django.contrib.sessions.backends.file",
    "django.contrib.sessions.backends.cache",
    "django.contrib.sessions.backends.cached_db",
)

def parse_boolean(s):
    s = s.strip().lower()
    if s in ("true", "1", "t"):
        return True
    return False

def parse_paths(s):
    return [os.path.normpath(path) for path in json.loads(s)]

def check_server_type(s):
    if s not in ALL_SERVER_TYPES:
        raise ValueError(
            "Unknown server type: %s. Valid values are: %s" % (s, ALL_SERVER_TYPES)
        )
    return s


def check_session_engine(s):
    if s not in SESSION_ENGINE_VALUES:
        raise ValueError(
            "Unknown session engine: %s. Valid values are: %s"
            % (s, SESSION_ENGINE_VALUES)
        )
    return s

def identity(x):
    return x

def check_timezone(s):
    """
    Checks that string is a valid time-zone. If not, raise Exception
    """
    pytz.timezone(s)
    return s

def str_slash(s):
    if s is not None:
        s = str(s)
        if s and not s.endswith("/"):
            s += "/"
    return s

class LeaveUnset(Exception):
    pass

def leave_none_unset(s):
    if s is None:
        raise LeaveUnset()
    return s

def leave_none_unset_int(s):
    s = leave_none_unset(s)
    if s is not None:
        return int(s)

CUSTOM_HOST = CUSTOM_SETTINGS.get("Ice.Default.Host", "localhost")
CUSTOM_HOST = CUSTOM_SETTINGS.get("engine.master.host", CUSTOM_HOST)
# DO NOT EDIT!
INTERNAL_SETTINGS_MAPPING = {
    "engine.qa.feedback": ["FEEDBACK_URL", "http://qa.bhojpur.net", str, None],
    "engine.web.upgrades.url": ["UPGRADES_URL", None, leave_none_unset, None],
    "engine.web.check_version": ["CHECK_VERSION", "true", parse_boolean, None],
    # Allowed hosts:
    "engine.web.allowed_hosts": ["ALLOWED_HOSTS", '["*"]', json.loads, None],
    # Do not show WARNING (1_8.W001): The standalone TEMPLATE_* settings
    # were deprecated in Django 1.8 and the TEMPLATES dictionary takes
    # precedence. You must put the values of the following settings
    # into your default TEMPLATES dict:
    # TEMPLATE_DIRS, TEMPLATE_CONTEXT_PROCESSORS.
    "engine.web.system_checks": [
        "SILENCED_SYSTEM_CHECKS",
        '["1_8.W001"]',
        json.loads,
        None,
    ],
    # Internal email notification for engine.web.admins,
    # loaded from config.xml directly
    "engine.mail.from": [
        "SERVER_EMAIL",
        None,
        identity,
        (
            "The email address that error messages come from, such as those"
            " sent to :property:`engine.web.admins`.  Requires EMAIL properties"
            " below."
        ),
    ],
    "engine.mail.host": [
        "EMAIL_HOST",
        None,
        identity,
        "The SMTP server host to use for sending email.",
    ],
    "engine.mail.password": [
        "EMAIL_HOST_PASSWORD",
        None,
        identity,
        "Password to use for the SMTP server.",
    ],
    "engine.mail.username": [
        "EMAIL_HOST_USER",
        None,
        identity,
        "Username to use for the SMTP server.",
    ],
    "engine.mail.port": ["EMAIL_PORT", 25, identity, "Port to use for the SMTP server."],
    "engine.web.admins.email_subject_prefix": [
        "EMAIL_SUBJECT_PREFIX",
        "[ODE.web - admin notification]",
        str,
        "Subject-line prefix for email messages",
    ],
    "engine.mail.smtp.starttls.enable": [
        "EMAIL_USE_TLS",
        "false",
        parse_boolean,
        (
            "Whether to use a TLS (secure) connection when talking to the SMTP"
            " server."
        ),
    ],
}

CUSTOM_SETTINGS_MAPPINGS = {
    # Deployment configuration
    "engine.web.debug": [
        "DEBUG",
        "false",
        parse_boolean,
        (
            "A boolean that turns on/off debug mode. "
            "Use debug mode only in development, not in production, as it logs "
            "sensitive and confidential information in plaintext."
        ),
    ],
    "engine.web.secret_key": [
        "SECRET_KEY",
        None,
        leave_none_unset,
        ("A boolean that sets SECRET_KEY for a particular Django " "installation."),
    ],
    "engine.web.admins": [
        "ADMINS",
        "[]",
        json.loads,
        (
            "A list of people who get code error notifications whenever the "
            "application identifies a broken link or raises an unhandled "
            "exception that results in an internal server error. This gives "
            "the administrators immediate notification of any errors, "
            "see :doc:`/sysadmins/mail`. "
            'Example:``\'[["Full Name", "email address"]]\'``.'
        ),
    ],
    "engine.web.application_server": [
        "APPLICATION_SERVER",
        DEFAULT_SERVER_TYPE,
        check_server_type,
        (
            "ODE.web is configured to run in Gunicorn as a generic WSGI (TCP)"
            "application by default. Available options: ``wsgi-tcp`` "
            "(Gunicorn, default), ``wsgi`` (Advanced users only, e.g. manual "
            "Apache configuration with ``mod_wsgi``)."
        ),
    ],
    "engine.web.application_server.host": [
        "APPLICATION_SERVER_HOST",
        "127.0.0.1",
        str,
        "The front-end webserver e.g. NGINX can be set up to run on a "
        "different host from ODE.web. The property ensures that ODE.web "
        "is accessible on an external IP. It requires copying all the "
        "ODE.web static files to the separate NGINX server.",
    ],
    "engine.web.application_server.port": [
        "APPLICATION_SERVER_PORT",
        4080,
        int,
        "Upstream application port",
    ],
    "engine.web.application_server.max_requests": [
        "APPLICATION_SERVER_MAX_REQUESTS",
        0,
        int,
        ("The maximum number of requests a worker will process before " "restarting."),
    ],
    "engine.web.middleware": [
        "MIDDLEWARE_CLASSES_LIST",
        (
            "["
            '{"index": 1, '
            '"class": "django.middleware.common.BrokenLinkEmailsMiddleware"},'
            '{"index": 2, '
            '"class": "django.middleware.common.CommonMiddleware"},'
            '{"index": 3, '
            '"class": "django.contrib.sessions.middleware.SessionMiddleware"},'
            '{"index": 4, '
            '"class": "django.middleware.csrf.CsrfViewMiddleware"},'
            '{"index": 5, '
            '"class": "django.contrib.messages.middleware.MessageMiddleware"},'
            '{"index": 6, '
            '"class": "django.middleware.clickjacking.XFrameOptionsMiddleware"}'
            "]"
        ),
        json.loads,
        (
            "Warning: Only system administrators should use this feature. "
            "List of Django middleware classes in the form "
            '[{"class": "class.name", "index": FLOAT}]. '
            "See :djangodoc:`Django middleware <topics/http/middleware/>`."
            " Classes will be ordered by increasing index"
        ),
    ],
    "engine.web.prefix": [
        "FORCE_SCRIPT_NAME",
        None,
        leave_none_unset,
        (
            "Used as the value of the SCRIPT_NAME environment variable in any"
            " HTTP request."
        ),
    ],
    "engine.web.use_x_forwarded_host": [
        "USE_X_FORWARDED_HOST",
        "false",
        parse_boolean,
        (
            "Specifies whether to use the X-Forwarded-Host header in preference "
            "to the Host header. This should only be enabled if a proxy which "
            "sets this header is in use."
        ),
    ],
    "engine.web.static_url": [
        "STATIC_URL",
        "/static/",
        str_slash,
        (
            "URL to use when referring to static files. Example: ``'/static/'``"
            " or ``'http://static.example.com/'``. Used as the base path for"
            " asset  definitions (the Media class) and the staticfiles app. It"
            " must end in a slash if set to a non-empty value."
        ),
    ],
    "engine.web.static_root": [
        "STATIC_ROOT",
        os.path.join(ODEDIR, "var", "static"),
        os.path.normpath,
        (
            "The absolute path to the directory where collectstatic will"
            " collect static files for deployment. If the staticfiles contrib"
            " app is enabled (default) the collectstatic management command"
            " will collect static files into this directory."
        ),
    ],
    "engine.web.session_engine": [
        "SESSION_ENGINE",
        DEFAULT_SESSION_ENGINE,
        check_session_engine,
        (
            "Controls where Django stores session data. See :djangodoc:"
            "`Configuring the session engine for more details <ref/settings"
            "/#session-engine>`."
        ),
    ],
    "engine.web.session_expire_at_browser_close": [
        "SESSION_EXPIRE_AT_BROWSER_CLOSE",
        "true",
        parse_boolean,
        (
            "A boolean that determines whether to expire the session when the "
            "user closes their browser. See :djangodoc:`Django Browser-length "
            "sessions vs. persistent sessions documentation <topics/http/"
            "sessions/#browser-length-vs-persistent-sessions>` for more "
            "details."
        ),
    ],
    "engine.web.caches": [
        "CACHES",
        ('{"default": {"BACKEND":' ' "django.core.cache.backends.dummy.DummyCache"}}'),
        json.loads,
        (
            "ODE.web offers alternative session backends to automatically"
            " delete stale data using the cache session store backend, see "
            ":djangodoc:`Django cached session documentation <topics/http/"
            "sessions/#using-cached-sessions>` for more details."
        ),
    ],
    "engine.web.secure": [
        "SECURE",
        "false",
        parse_boolean,
        ("Force all backend ODE.server connections to use SSL."),
    ],
    "engine.web.session_cookie_age": [
        "SESSION_COOKIE_AGE",
        86400,
        int,
        "The age of session cookies, in seconds.",
    ],
    "engine.web.session_cookie_domain": [
        "SESSION_COOKIE_DOMAIN",
        None,
        leave_none_unset,
        "The domain to use for session cookies",
    ],
    "engine.web.session_cookie_name": [
        "SESSION_COOKIE_NAME",
        None,
        leave_none_unset,
        "The name to use for session cookies",
    ],
    "engine.web.session_cookie_path": [
        "SESSION_COOKIE_PATH",
        None,
        leave_none_unset,
        "The path to use for session cookies",
    ],
    "engine.web.session_cookie_secure": [
        "SESSION_COOKIE_SECURE",
        "false",
        parse_boolean,
        (
            "Restrict session cookies to HTTPS only, you are strongly "
            "recommended to set this to ``true`` in production."
        ),
    ],
    "engine.web.csrf_cookie_secure": [
        "CSRF_COOKIE_SECURE",
        "false",
        parse_boolean,
        (
            "Restrict CSRF cookies to HTTPS only, you are strongly "
            "recommended to set this to ``true`` in production."
        ),
    ],
    "engine.web.csrf_cookie_httponly": [
        "CSRF_COOKIE_HTTPONLY",
        "false",
        parse_boolean,
        (
            "Prevent CSRF cookie from being accessed in JavaScript. "
            "Currently disabled as it breaks background JavaScript POSTs in "
            "ODE.web."
        ),
    ],
    "engine.web.logdir": ["LOGDIR", LOGDIR, str, "A path to the custom log directory."],
    "engine.web.secure_proxy_ssl_header": [
        "SECURE_PROXY_SSL_HEADER",
        "[]",
        json.loads,
        (
            "A tuple representing a HTTP header/value combination that "
            "signifies a request is secure. Example "
            '``\'["HTTP_X_FORWARDED_PROTO_ODE_WEB", "https"]\'``. '
            "For more details see :djangodoc:`secure proxy ssl header <ref/"
            "settings/#secure-proxy-ssl-header>`."
        ),
    ],
    "engine.web.wsgi_args": [
        "WSGI_ARGS",
        None,
        leave_none_unset,
        (
            "A string representing Gunicorn additional arguments. "
            "Check Gunicorn Documentation "
            "https://docs.gunicorn.org/en/latest/settings.html"
        ),
    ],
    "engine.web.wsgi_workers": [
        "WSGI_WORKERS",
        5,
        int,
        (
            "The number of worker processes for handling requests. "
            "Check Gunicorn Documentation "
            "https://docs.gunicorn.org/en/stable/settings.html#workers"
        ),
    ],
    "engine.web.wsgi_timeout": [
        "WSGI_TIMEOUT",
        60,
        int,
        (
            "Workers silent for more than this many seconds are killed "
            "and restarted. Check Gunicorn Documentation "
            "https://docs.gunicorn.org/en/stable/settings.html#timeout"
        ),
    ],
    # Public user
    "engine.web.public.enabled": [
        "PUBLIC_ENABLED",
        "false",
        parse_boolean,
        "Enable and disable the ODE.web public user functionality.",
    ],
    "engine.web.public.url_filter": [
        "PUBLIC_URL_FILTER",
        r"(?#This regular expression matches nothing)a^",
        re.compile,
        (
            "Set a regular expression that matches URLs the public user is "
            "allowed to access. If this is not set, no URLs will be "
            "publicly available."
        ),
    ],
    "engine.web.public.get_only": [
        "PUBLIC_GET_ONLY",
        "true",
        parse_boolean,
        "Restrict public users to GET requests only",
    ],
    "engine.web.public.server_id": [
        "PUBLIC_SERVER_ID",
        1,
        int,
        "Server to authenticate against.",
    ],
    "engine.web.public.user": [
        "PUBLIC_USER",
        None,
        leave_none_unset,
        "Username to use during authentication.",
    ],
    "engine.web.public.password": [
        "PUBLIC_PASSWORD",
        None,
        leave_none_unset,
        "Password to use during authentication.",
    ],
    "engine.web.public.cache.enabled": [
        "PUBLIC_CACHE_ENABLED",
        "false",
        parse_boolean,
        None,
    ],
    "engine.web.public.cache.key": [
        "PUBLIC_CACHE_KEY",
        "engine.web.public.cache.key",
        str,
        None,
    ],
    "engine.web.public.cache.timeout": ["PUBLIC_CACHE_TIMEOUT", 60 * 60 * 24, int, None],
    # Social media integration
    "engine.web.sharing.twitter": [
        "SHARING_TWITTER",
        "{}",
        json.loads,
        (
            "Dictionary of `server-name: @twitter-site-username`, where "
            "server-name matches a name from `engine.web.server_list`. "
            'For example: ``\'{"ode": "@bhojpur"}\'``'
        ),
    ],
    "engine.web.sharing.opengraph": [
        "SHARING_OPENGRAPH",
        "{}",
        json.loads,
        (
            "Dictionary of `server-name: site-name`, where "
            "server-name matches a name from `engine.web.server_list`. "
            'For example: ``\'{"ode": "Bhojpur ODE"}\'``'
        ),
    ],
    # Application configuration
    "engine.web.server_list": [
        "SERVER_LIST",
        '[["%s", 4064, "ode"]]' % CUSTOM_HOST,
        json.loads,
        "A list of servers the Web client can connect to.",
    ],
    "engine.web.ping_interval": [
        "PING_INTERVAL",
        60000,
        int,
        "Timeout interval between ping invocations in seconds",
    ],
    "engine.web.chunk_size": [
        "CHUNK_SIZE",
        1048576,
        int,
        "Size, in bytes, of the “chunk”",
    ],
    "engine.web.webgateway_cache": ["WEBGATEWAY_CACHE", None, leave_none_unset, None],
    "engine.web.maximum_multifile_download_size": [
        "MAXIMUM_MULTIFILE_DOWNLOAD_ZIP_SIZE",
        1024**3,
        int,
        "Prevent multiple files with total aggregate size greater than this "
        "value in bytes from being downloaded as a zip archive.",
    ],
    "engine.web.max_table_download_rows": [
        "MAX_TABLE_DOWNLOAD_ROWS",
        10000,
        int,
        "Prevent download of ODE.tables exceeding this number of rows "
        "in a single request.",
    ],
    # VIEWER
    "engine.web.viewer.view": [
        "VIEWER_VIEW",
        "engine.webclient.views.image_viewer",
        str,
        (
            "Django view which handles display of, or redirection to, the "
            "desired full image viewer."
        ),
    ],
    # OPEN WITH
    "engine.web.open_with": [
        "OPEN_WITH",
        (
            '[["Image viewer", "webgateway", {"supported_objects": ["image"],'
            '"script_url": "webclient/javascript/ode.openwith_viewer.js"}]]'
        ),
        json.loads,
        (
            "A list of viewers that can be used to display selected Images "
            "or other objects. Each viewer is defined as "
            '``["Name", "url", options]``. Url is reverse(url). '
            "Selected objects are added to the url as ?image=:1&image=2"
            "Objects supported must be specified in options with "
            'e.g. ``{"supported_objects":["images"]}`` '
            "to enable viewer for one or more images."
        ),
    ],
    # PIPELINE 1.3.20
    # Pipeline is an asset packaging library for Django, providing both CSS
    # and JavaScript concatenation and compression, built-in JavaScript
    # template support, and optional data-URI image and font embedding.
    "engine.web.pipeline_js_compressor": [
        "PIPELINE_JS_COMPRESSOR",
        None,
        identity,
        (
            "Compressor class to be applied to JavaScript files. If empty or "
            "None, JavaScript files won't be compressed."
        ),
    ],
    "engine.web.pipeline_css_compressor": [
        "PIPELINE_CSS_COMPRESSOR",
        None,
        identity,
        (
            "Compressor class to be applied to CSS files. If empty or None,"
            " CSS files won't be compressed."
        ),
    ],
    "engine.web.pipeline_staticfile_storage": [
        "STATICFILES_STORAGE",
        "pipeline.storage.PipelineStorage",
        str,
        (
            "The file storage engine to use when collecting static files with"
            " the collectstatic management command. See `the documentation "
            "<https://django-pipeline.readthedocs.org/en/latest/storages.html>`_"
            " for more details."
        ),
    ],
    # Customisation
    "engine.web.login_logo": [
        "LOGIN_LOGO",
        None,
        leave_none_unset,
        (
            "Customize webclient login page with your own logo. Logo images "
            "should ideally be 150 pixels high or less and will appear above "
            "the ODE logo. You will need to host the image somewhere else "
            "and link to it with the ODE logo."
        ),
    ],
    "engine.web.login_view": [
        "LOGIN_VIEW",
        "weblogin",
        str,
        (
            "The Django view name used for login. Use this to provide an "
            "alternative login workflow."
        ),
    ],
    "engine.web.login_incorrect_credentials_text": [
        "LOGIN_INCORRECT_CREDENTIALS_TEXT",
        "Connection not available, please check your user name and password.",
        str,
        (
            "The error message shown to users who enter an incorrect username "
            "or password."
        ),
    ],
    "engine.web.top_logo": [
        "TOP_LOGO",
        "",
        str,
        (
            "Customize the webclient top bar logo. The recommended image height "
            "is 23 pixels and it must be hosted outside of ODE.web."
        ),
    ],
    "engine.web.top_logo_link": [
        "TOP_LOGO_LINK",
        "",
        str,
        ("The target location of the webclient top logo, default unlinked."),
    ],
    "engine.web.user_dropdown": [
        "USER_DROPDOWN",
        "true",
        parse_boolean,
        (
            "Whether or not to include a user dropdown in the base template."
            " Particularly useful when used in combination with the engine.web"
            " public user where logging in may not make sense."
        ),
    ],
    "engine.web.feedback.comment.enabled": [
        "FEEDBACK_COMMENT_ENABLED",
        "true",
        parse_boolean,
        (
            "Enable the feedback form for comments. "
            "These comments are sent to the URL in ``engine.qa.feedback`` "
            "(Bhojpur ODE team by default)."
        ),
    ],
    "engine.web.feedback.error.enabled": [
        "FEEDBACK_ERROR_ENABLED",
        "true",
        parse_boolean,
        (
            "Enable the feedback form for errors. "
            "These errors are sent to the URL in ``engine.qa.feedback`` "
            "(Bhojpur ODE team by default)."
        ),
    ],
    "engine.web.show_forgot_password": [
        "SHOW_FORGOT_PASSWORD",
        "true",
        parse_boolean,
        (
            "Allows to hide 'Forgot password' from the login view"
            " - useful for LDAP/ActiveDir installations"
        ),
    ],
    "engine.web.favicon_url": [
        "FAVICON_URL",
        "webgateway/img/favicon.ico",
        str,
        ("Favicon URL, specifies the path relative to django's static file dirs."),
    ],
    "engine.web.staticfile_dirs": [
        "STATICFILES_DIRS",
        "[]",
        json.loads,
        (
            "Defines the additional locations the staticfiles app will traverse"
            " if the FileSystemFinder finder is enabled, e.g. if you use the"
            " collectstatic or findstatic management command or use the static"
            " file serving view."
        ),
    ],
    "engine.web.template_dirs": [
        "TEMPLATE_DIRS",
        "[]",
        json.loads,
        (
            "List of locations of the template source files, in search order. "
            "Note that these paths should use Unix-style forward slashes."
        ),
    ],
    "engine.web.index_template": [
        "INDEX_TEMPLATE",
        None,
        identity,
        (
            "Define template used as an index page ``http://your_host/ode/``."
            "If None user is automatically redirected to the login page."
            "For example use 'webclient/index.html'. "
        ),
    ],
    "engine.web.base_include_template": [
        "BASE_INCLUDE_TEMPLATE",
        None,
        identity,
        ("Template to be included in every page, at the end of the <body>"),
    ],
    "engine.web.login_redirect": [
        "LOGIN_REDIRECT",
        "{}",
        json.loads,
        (
            "Redirect to the given location after logging in. It only supports "
            "arguments for :djangodoc:`Django reverse function"
            " <ref/urlresolvers/#reverse>`. "
            'For example: ``\'{"redirect": ["webindex"], "viewname":'
            ' "load_template", "args":["userdata"], "query_string":'
            ' {"experimenter": -1}}\'``'
        ),
    ],
    "engine.web.redirect_allowed_hosts": [
        "REDIRECT_ALLOWED_HOSTS",
        "[]",
        json.loads,
        (
            "If you wish to allow redirects to an external site, "
            "the domains must be listed here. "
            'For example ["bhojpur.net"].'
        ),
    ],
    "engine.web.login.show_client_downloads": [
        "SHOW_CLIENT_DOWNLOADS",
        "true",
        parse_boolean,
        ("Whether to link to official client downloads on the login page"),
    ],
    "engine.web.login.client_downloads_base": [
        "CLIENT_DOWNLOAD_GITHUB_REPO",
        "ode/ode-insight",
        str,
        ("GitHub repository containing the Desktop client downloads"),
    ],
    "engine.web.apps": [
        "ADDITIONAL_APPS",
        "[]",
        json.loads,
        (
            "Add additional Django applications. For example, see"
            " :doc:`/developers/Web/CreateApp`"
        ),
    ],
    "engine.web.root_application": [
        "ODEWEB_ROOT_APPLICATION",
        "",
        str,
        (
            "Override the root application label that handles ``/``. "
            "**Warning** you must ensure the application's URLs do not conflict "
            "with other applications. "
            "ode-gallery is an example of an application that can be used for "
            "this (set to ``gallery``)"
        ),
    ],
    "engine.web.databases": ["DATABASES", "{}", json.loads, None],
    "engine.web.page_size": [
        "PAGE",
        200,
        int,
        (
            "Number of images displayed within a dataset or 'orphaned'"
            " container to prevent from loading them all at once."
        ),
    ],
    "engine.web.thumbnails_batch": [
        "THUMBNAILS_BATCH",
        50,
        int,
        (
            "Number of thumbnails retrieved to prevent from loading them"
            " all at once. Make sure the size is not too big, otherwise"
            " you may exceed limit request line, see"
            " https://docs.gunicorn.org/en/latest/settings.html"
            "?highlight=limit_request_line"
        ),
    ],
    "engine.web.search.default_user": [
        "SEARCH_DEFAULT_USER",
        0,
        int,
        ("ID of user to pre-select in search form."),
    ],
    "engine.web.search.default_group": [
        "SEARCH_DEFAULT_GROUP",
        0,
        int,
        ("ID of group to pre-select in search form."),
    ],
    "engine.web.ui.top_links": [
        "TOP_LINKS",
        (
            "["
            '["Data", "webindex", {"title": "Browse Data via Projects, Tags'
            ' etc"}],'
            '["History", "history", {"title": "History"}],'
            '["Help", "https://help.bhojpur.net/",'
            '{"title":"Open ODE user guide in a new tab", "target":"new"}]'
            "]"
        ),
        json.loads,
        (
            "Add links to the top header: links are ``['Link Text', "
            "'link|lookup_view', options]``, where the url is reverse('link'), "
            "simply 'link' (for external urls) or lookup_view is a detailed "
            'dictionary {"viewname": "str", "args": [], "query_string": '
            '{"param": "value" }], '
            'e.g. ``\'["Webtest", "webtest_index"] or ["Homepage",'
            ' "http://...", {"title": "Homepage", "target": "new"}'
            ' ] or ["Repository", {"viewname": "webindex", '
            '"query_string": {"experimenter": -1}}, '
            '{"title": "Repo"}]\'``'
        ),
    ],
    "engine.web.ui.metadata_panes": [
        "METADATA_PANES",
        (
            "["
            '{"name": "tag", "label": "Tags", "index": 1},'
            '{"name": "map", "label": "Key-Value Pairs", "index": 2},'
            '{"name": "table", "label": "Tables", "index": 3},'
            '{"name": "file", "label": "Attachments", "index": 4},'
            '{"name": "comment", "label": "Comments", "index": 5},'
            '{"name": "rating", "label": "Ratings", "index": 6},'
            '{"name": "other", "label": "Others", "index": 7}'
            "]"
        ),
        json.loads,
        (
            "Manage Metadata pane accordion. This functionality is limited to"
            " the existing sections."
        ),
    ],
    "engine.web.ui.right_plugins": [
        "RIGHT_PLUGINS",
        (
            '[["Acquisition",'
            ' "webclient/data/includes/right_plugin.acquisition.js.html",'
            ' "metadata_tab"],'
            # '["ROIs", "webtest/webclient_plugins/right_plugin.rois.js.html",
            # "image_roi_tab"],'
            '["Preview", "webclient/data/includes/right_plugin.preview.js.html"'
            ', "preview_tab"]]'
        ),
        json.loads,
        (
            "Add plugins to the right-hand panel. "
            "Plugins are ``['Label', 'include.js', 'div_id']``. "
            "The javascript loads data into ``$('#div_id')``."
        ),
    ],
    "engine.web.ui.center_plugins": [
        "CENTER_PLUGINS",
        (
            "["
            # '["Split View",
            # "webtest/webclient_plugins/center_plugin.splitview.js.html",
            # "split_view_panel"],'
            "]"
        ),
        json.loads,
        (
            "Add plugins to the center panels. Plugins are "
            "``['Channel overlay',"
            " 'webtest/webclient_plugins/center_plugin.overlay.js.html',"
            " 'channel_overlay_panel']``. "
            "The javascript loads data into ``$('#div_id')``."
        ),
    ],
    "engine.web.plate_layout": [
        "PLATE_LAYOUT",
        "expand",
        str,
        (
            "If 'shrink', the plate will not display rows and columns "
            "before the first Well, or after the last Well. "
            "If 'trim', the plate will only show Wells "
            "from A1 to the last Well. "
            "If 'expand' (default), the plate will expand from A1 to a "
            "multiple of 12 columns x 8 rows after the last Well."
        ),
    ],
    # CORS
    "engine.web.cors_origin_whitelist": [
        "CORS_ORIGIN_WHITELIST",
        "[]",
        json.loads,
        (
            "A list of origin hostnames that are authorized to make cross-site "
            "HTTP requests. "
            "Used by the django-cors-headers app as described at "
            "https://github.com/ottoyiu/django-cors-headers"
        ),
    ],
    "engine.web.cors_origin_allow_all": [
        "CORS_ORIGIN_ALLOW_ALL",
        "false",
        parse_boolean,
        (
            "If True, cors_origin_whitelist will not be used and all origins "
            "will be authorized to make cross-site HTTP requests."
        ),
    ],
    "engine.web.html_meta_referrer": [
        "HTML_META_REFERRER",
        "origin-when-crossorigin",
        str,
        (
            "Default content for the HTML Meta referrer tag. "
            "See https://www.w3.org/TR/referrer-policy/#referrer-policies for "
            "allowed values and https://caniuse.com/referrer-policy for "
            "browser compatibility. "
            "Warning: Internet Explorer 11 does not support the default value "
            'of this setting, you may want to change this to "origin" after '
            "reviewing the linked documentation."
        ),
    ],
    "engine.web.x_frame_options": [
        "X_FRAME_OPTIONS",
        "SAMEORIGIN",
        str,
        "Whether to allow ODE.web to be loaded in a frame.",
    ],
    "engine.web.time_zone": [
        "TIME_ZONE",
        "Europe/London",
        check_timezone,
        (
            "Time zone for this installation. Choices can be found in the "
            "``TZ database name`` column of: "
            "https://en.wikipedia.org/wiki/List_of_tz_database_time_zones "
            'Default ``"Europe/London"``'
        ),
    ],
    "engine.web.django_additional_settings": [
        "DJANGO_ADDITIONAL_SETTINGS",
        "[]",
        json.loads,
        (
            "Additional Django settings as list of key-value tuples. "
            "Use this to set or override Django settings that aren't managed by "
            'ODE.web. e.g. ``["CUSTOM_KEY", "CUSTOM_VALUE"]``'
        ),
    ],
    "engine.web.nginx_server_extra_config": [
        "NGINX_SERVER_EXTRA_CONFIG",
        "[]",
        json.loads,
        (
            "Extra configuration lines to add to the Nginx server block. "
            "Lines will be joined with \\n. "
            "Remember to terminate lines with; when necessary."
        ),
    ],
}

DEPRECATED_SETTINGS_MAPPINGS = {
    # Deprecated settings, description should indicate the replacement.
    "engine.web.force_script_name": [
        "FORCE_SCRIPT_NAME",
        None,
        leave_none_unset,
        ("Use engine.web.prefix instead."),
    ],
    "engine.web.server_email": [
        "SERVER_EMAIL",
        None,
        identity,
        ("Use engine.mail.from instead."),
    ],
    "engine.web.email_host": [
        "EMAIL_HOST",
        None,
        identity,
        ("Use engine.mail.host instead."),
    ],
    "engine.web.email_host_password": [
        "EMAIL_HOST_PASSWORD",
        None,
        identity,
        ("Use engine.mail.password instead."),
    ],
    "engine.web.email_host_user": [
        "EMAIL_HOST_USER",
        None,
        identity,
        ("Use engine.mail.username instead."),
    ],
    "engine.web.email_port": [
        "EMAIL_PORT",
        None,
        identity,
        ("Use engine.mail.port instead."),
    ],
    "engine.web.email_subject_prefix": [
        "EMAIL_SUBJECT_PREFIX",
        "[ODE.web]",
        str,
        ("Default email subject is no longer configurable."),
    ],
    "engine.web.email_use_tls": [
        "EMAIL_USE_TLS",
        "false",
        parse_boolean,
        ("Use engine.mail.smtp.* instead to set up" " javax.mail.Session properties."),
    ],
    "engine.web.plate_download.enabled": [
        "PLATE_DOWNLOAD_ENABLED",
        "false",
        parse_boolean,
        ("Use engine.policy.binary_access instead to restrict download."),
    ],
    "engine.web.viewer.initial_zoom_level": [
        "VIEWER_INITIAL_ZOOM_LEVEL",
        None,
        leave_none_unset_int,
        ("Use engine.client.viewer.initial_zoom_level instead."),
    ],
    "engine.web.send_broken_link_emails": [
        "SEND_BROKEN_LINK_EMAILS",
        "false",
        parse_boolean,
        (
            "Replaced by django.middleware.common.BrokenLinkEmailsMiddleware."
            "To get notification set :property:`engine.web.admins` property."
        ),
    ],
}

del CUSTOM_HOST


def check_worker_class(c):
    if c == "gevent":
        try:
            import gevent  # NOQA
        except ImportError:
            raise ImportError(
                "You are using async workers based "
                "on Greenlets via Gevent. Install gevent"
            )
    return str(c)


def check_threading(t):
    t = int(t)
    if t > 1:
        try:
            import concurrent.futures  # NOQA
        except ImportError:
            raise ImportError(
                "You are using sync workers with " "multiple threads. Install futures"
            )
    return t


# DEVELOPMENT_SETTINGS_MAPPINGS - WARNING: For each setting developer MUST open
# a ticket that needs to be resolved before a release either by moving the
# setting to CUSTOM_SETTINGS_MAPPINGS or by removing the setting at all.
DEVELOPMENT_SETTINGS_MAPPINGS = {
    "engine.web.wsgi_worker_class": [
        "WSGI_WORKER_CLASS",
        "sync",
        check_worker_class,
        (
            "The default ODE.web uses sync workers to handle most “normal” "
            "types of workloads. Check Gunicorn Design Documentation "
            "https://docs.gunicorn.org/en/stable/design.html"
        ),
    ],
    "engine.web.wsgi_worker_connections": [
        "WSGI_WORKER_CONNECTIONS",
        1000,
        int,
        (
            "(ASYNC WORKERS only) The maximum number of simultaneous clients. "
            "Check Gunicorn Documentation https://docs.gunicorn.org"
            "/en/stable/settings.html#worker-connections"
        ),
    ],
    "engine.web.wsgi_threads": [
        "WSGI_THREADS",
        1,
        check_threading,
        (
            "(SYNC WORKERS only) The number of worker threads for handling "
            "requests. Check Gunicorn Documentation "
            "https://docs.gunicorn.org/en/stable/settings.html#threads"
        ),
    ],
}

def map_deprecated_settings(settings):
    m = {}
    for key, values in settings.items():
        try:
            global_name = values[0]
            m[global_name] = (CUSTOM_SETTINGS[key], key)
            if len(values) < 5:
                # Not using default (see process_custom_settings)
                values.append(False)
        except KeyError:
            if len(values) < 5:
                values.append(True)
    return m

def process_custom_settings(
    module, settings="CUSTOM_SETTINGS_MAPPINGS", deprecated=None
):
    logging.info("Processing custom settings for module %s" % module.__name__)

    if deprecated:
        deprecated_map = map_deprecated_settings(getattr(module, deprecated, {}))
    else:
        deprecated_map = {}

    for key, values in getattr(module, settings, {}).items():
        # Django may import settings.py more than once:
        # In that case, the custom settings have already been processed.
        if len(values) == 5:
            continue

        global_name, default_value, mapping, description = values

        try:
            global_value = CUSTOM_SETTINGS[key]
            values.append(False)
        except KeyError:
            global_value = default_value
            values.append(True)

        try:
            using_default = values[-1]
            if global_name in deprecated_map:
                dep_value, dep_key = deprecated_map[global_name]
                if using_default:
                    logging.warning("Setting %s is deprecated, use %s", dep_key, key)
                    global_value = dep_value
                else:
                    logging.error(
                        "%s and its deprecated key %s are both set, using %s",
                        key,
                        dep_key,
                        key,
                    )
            setattr(module, global_name, mapping(global_value))
        except ValueError as e:
            raise ValueError(
                "Invalid %s (%s = %r). %s. %s"
                % (global_name, key, global_value, e.args[0], description)
            )
        except ImportError as e:
            raise ImportError(
                "ImportError: %s. %s (%s = %r).\n%s"
                % (e.message, global_name, key, global_value, description)
            )
        except LeaveUnset:
            pass

process_custom_settings(sys.modules[__name__], "INTERNAL_SETTINGS_MAPPING")
process_custom_settings(
    sys.modules[__name__], "CUSTOM_SETTINGS_MAPPINGS", "DEPRECATED_SETTINGS_MAPPINGS"
)
process_custom_settings(sys.modules[__name__], "DEVELOPMENT_SETTINGS_MAPPINGS")

if not DEBUG:  # from CUSTOM_SETTINGS_MAPPINGS  # noqa
    LOGGING["loggers"]["django.request"]["level"] = "INFO"
    LOGGING["loggers"]["django"]["level"] = "INFO"
    LOGGING["loggers"][""]["level"] = "INFO"

def report_settings(module):
    from django.views.debug import SafeExceptionReporterFilter

    setting_filter = SafeExceptionReporterFilter()
    custom_settings_mappings = getattr(module, "CUSTOM_SETTINGS_MAPPINGS", {})
    for key in sorted(custom_settings_mappings):
        values = custom_settings_mappings[key]
        global_name, default_value, mapping, description, using_default = values
        source = using_default and "default" or key
        global_value = getattr(module, global_name, None)
        if global_name.isupper():
            logger.debug(
                "%s = %r (source:%s)",
                global_name,
                setting_filter.cleanse_setting(global_name, global_value),
                source,
            )

    deprecated_settings = getattr(module, "DEPRECATED_SETTINGS_MAPPINGS", {})
    for key in sorted(deprecated_settings):
        values = deprecated_settings[key]
        global_name, default_value, mapping, description, using_default = values
        global_value = getattr(module, global_name, None)
        if global_name.isupper() and not using_default:
            logger.debug(
                "%s = %r (deprecated:%s, %s)",
                global_name,
                setting_filter.cleanse_setting(global_name, global_value),
                key,
                description,
            )

report_settings(sys.modules[__name__])

SITE_ID = 1

FIRST_DAY_OF_WEEK = 0  # 0-Monday, ... 6-Sunday

# LANGUAGE_CODE: A string representing the language code for this
# installation. This should be in standard language format. For example, U.S.
# English is "en-us".
LANGUAGE_CODE = "en-gb"

# SECRET_KEY: A secret key for this particular Django installation. Used to
# provide a seed in secret-key hashing algorithms. Set this to a random string,
# the longer, the better. Make this unique, and don't share it with anybody.
try:
    SECRET_KEY
except NameError:
    secret_path = os.path.join(ODEDIR, "var", "django_secret_key").replace("\\", "/")
    if not os.path.isfile(secret_path):
        try:
            secret_key = "".join(
                [
                    random.SystemRandom().choice(
                        "{0}{1}{2}".format(
                            string.ascii_letters, string.digits, string.punctuation
                        )
                    )
                    for i in range(50)
                ]
            )
            with os.fdopen(
                os.open(secret_path, os.O_WRONLY | os.O_CREAT, 0o600), "w"
            ) as secret_file:
                secret_file.write(secret_key)
        except IOError:
            raise IOError(
                "Please create a %s file with random characters"
                " to generate your secret key!" % secret_path
            )
    try:
        with open(secret_path, "r") as secret_file:
            SECRET_KEY = secret_file.read().strip()
    except IOError:
        raise IOError("Could not find secret key in %s!" % secret_path)

# USE_I18N: A boolean that specifies whether Django's internationalization
# system should be enabled.
# This provides an easy way to turn it off, for performance. If this is set to
# False, Django will make some optimizations so as not to load the
# internationalization machinery.
USE_I18N = True

# ROOT_URLCONF: A string representing the full Python import path to your root
# URLconf.
# For example: "mydjangoapps.urls". Can be overridden on a per-request basis
# by setting the attribute urlconf on the incoming HttpRequest object.
ROOT_URLCONF = "engine.urls"

# STATICFILES_FINDERS: The list of finder backends that know how to find
# static files in various locations. The default will find files stored in the
# STATICFILES_DIRS setting (using
# django.contrib.staticfiles.finders.FileSystemFinder) and in a static
# subdirectory of each app (using
# django.contrib.staticfiles.finders.AppDirectoriesFinder)
STATICFILES_FINDERS = (
    "django.contrib.staticfiles.finders.FileSystemFinder",
    "django.contrib.staticfiles.finders.AppDirectoriesFinder",
    "pipeline.finders.PipelineFinder",
)

# STATICFILES_DIRS: This setting defines the additional locations the
# staticfiles app will traverse if the FileSystemFinder finder is enabled,
# e.g. if you use the collectstatic or findstatic management command or use
# the static file serving view.
# from CUSTOM_SETTINGS_MAPPINGS
# STATICFILES_DIRS += (("webapp/custom_static", path/to/statics),)  # noqa

# TEMPLATES: A list containing the settings for all template engines
# to be used with Django. Each item of the list is a dictionary containing
# the options for an individual engine.
TEMPLATES = [
    {
        "BACKEND": "django.template.backends.django.DjangoTemplates",
        "DIRS": TEMPLATE_DIRS,  # noqa
        "APP_DIRS": True,
        "OPTIONS": {
            "builtins": ["engine.webgateway.templatetags.defaulttags"],
            "debug": DEBUG,  # noqa
            "context_processors": [
                # Insert your TEMPLATE_CONTEXT_PROCESSORS here or use this
                # list if you haven't customized them:
                "django.contrib.auth.context_processors.auth",
                "django.template.context_processors.debug",
                "django.template.context_processors.i18n",
                "django.template.context_processors.media",
                "django.template.context_processors.static",
                "django.template.context_processors.tz",
                "django.contrib.messages.context_processors.messages",
                "engine.custom_context_processor.url_suffix",
                "engine.custom_context_processor.base_include_template",
            ],
        },
    },
]

# INSTALLED_APPS: A tuple of strings designating all applications that are
# enabled in this Django installation. Each string should be a full Python
# path to a Python package that contains a Django application, as created by
# django-admin.py startapp.
INSTALLED_APPS = (
    "django.contrib.staticfiles",
    "django.contrib.auth",
    "django.contrib.contenttypes",
    "django.contrib.sessions",
    "django.contrib.sites",
)

# ADDITONAL_APPS: We import any settings.py from apps. This allows them to
# modify settings.
# We're also processing any CUSTOM_SETTINGS_MAPPINGS defined there.
for app in ADDITIONAL_APPS:  # from CUSTOM_SETTINGS_MAPPINGS  # noqa
    # Previously the app was added to INSTALLED_APPS as 'engine.app', which
    # then required the app to reside within or be symlinked from within
    # engine, instead of just having to be somewhere on the python path.
    # To allow apps to just be on the path, but keep it backwards compatible,
    # try to import as engine.app, if it works, keep that in INSTALLED_APPS,
    # otherwise add it to INSTALLED_APPS just with its own name.
    try:
        __import__("engine.%s" % app)
        INSTALLED_APPS += ("engine.%s" % app,)
    except ImportError:
        INSTALLED_APPS += (app,)
    try:
        logger.debug("Attempting to import additional app settings for app: %s" % app)
        module = __import__("%s.settings" % app)
        process_custom_settings(module.settings)
        report_settings(module.settings)
    except ImportError:
        logger.debug("Couldn't import settings from app: %s" % app)

INSTALLED_APPS += (
    "engine.feedback",
    "engine.webadmin",
    "engine.webclient",
    "engine.webgateway",
    "engine.webredirect",
    "engine.api",
    "pipeline",
)

logger.debug("INSTALLED_APPS=%s" % [INSTALLED_APPS])

PIPELINE = {
    "STYLESHEETS": {
        "webgateway_viewer": {
            "source_filenames": (
                "webgateway/css/reset.css",
                "webgateway/css/ode.body.css",
                "webclient/css/dusty.css",
                "webgateway/css/ode.viewport.css",
                "webgateway/css/ode.toolbar.css",
                "webgateway/css/ode.gs_slider.css",
                "webgateway/css/base.css",
                "webgateway/css/ode.snippet_header_logo.css",
                "webgateway/css/ode.postit.css",
                "3rdparty/farbtastic-1.2/farbtastic.css",
                "webgateway/css/ode.colorbtn.css",
                "3rdparty/JQuerySpinBtn-1.3a/JQuerySpinBtn.css",
                "3rdparty/jquery-ui-1.10.4/themes/base/jquery-ui.all.css",
                "webgateway/css/ode_image.css",
                "3rdparty/panojs-2.0.0/panojs.css",
            ),
            "output_filename": "engine.viewer.min.css",
        },
    },
    "CSS_COMPRESSOR": "pipeline.compressors.NoopCompressor",
    "JS_COMPRESSOR": "pipeline.compressors.NoopCompressor",
    "JAVASCRIPT": {
        "webgateway_viewer": {
            "source_filenames": (
                "3rdparty/jquery-1.11.1.js",
                "3rdparty/jquery-migrate-1.2.1.js",
                "3rdparty/jquery-ui-1.10.4/js/jquery-ui.1.10.4.js",
                "webgateway/js/ode.popup.js",
                "3rdparty/aop-1.3.js",
                "3rdparty/raphael-2.1.0/raphael.js",
                "3rdparty/raphael-2.1.0/scale.raphael.js",
                "3rdparty/panojs-2.0.0/utils.js",
                "3rdparty/panojs-2.0.0/PanoJS.js",
                "3rdparty/panojs-2.0.0/controls.js",
                "3rdparty/panojs-2.0.0/pyramid_Bisque.js",
                "3rdparty/panojs-2.0.0/pyramid_imgcnv.js",
                "3rdparty/panojs-2.0.0/pyramid_Zoomify.js",
                "3rdparty/panojs-2.0.0/control_thumbnail.js",
                "3rdparty/panojs-2.0.0/control_info.js",
                "3rdparty/panojs-2.0.0/control_svg.js",
                "3rdparty/panojs-2.0.0/control_roi.js",
                "3rdparty/panojs-2.0.0/control_scalebar.js",
                "3rdparty/hammer-2.0.2/hammer.min.js",
                "webgateway/js/ode.gs_utils.js",
                "webgateway/js/ode.viewportImage.js",
                "webgateway/js/ode.gs_slider.js",
                "webgateway/js/ode.viewport.js",
                "webgateway/js/ode_image.js",
                "webgateway/js/ode.roidisplay.js",
                "webgateway/js/ode.scalebardisplay.js",
                "webgateway/js/ode.smartdialog.js",
                "webgateway/js/ode.roiutils.js",
                "3rdparty/JQuerySpinBtn-1.3a/JQuerySpinBtn.js",
                "webgateway/js/ode.colorbtn.js",
                "webgateway/js/ode.postit.js",
                "3rdparty/jquery.selectboxes-2.2.6.js",
                "3rdparty/farbtastic-1.2/farbtastic.js",
                "3rdparty/jquery.mousewheel-3.0.6.js",
            ),
            "output_filename": "engine.viewer.min.js",
        }
    },
}

# Prevent scripting attacks from obtaining session cookie
SESSION_COOKIE_HTTPONLY = True

CSRF_FAILURE_VIEW = "engine.feedback.views.csrf_failure"

# Configuration for django-cors-headers app
# See https://github.com/ottoyiu/django-cors-headers
# Configration of allowed origins is handled by custom settings above
CORS_ALLOW_CREDENTIALS = True
# Needed for Django <1.9 since CSRF_TRUSTED_ORIGINS not supported
CORS_REPLACE_HTTPS_REFERER = True

# FEEDBACK - DO NOT MODIFY!
# FEEDBACK_URL: Is now configurable for testing purpuse only. Used in
# feedback.sendfeedback.SendFeedback class in order to submit errors or
# comment messages to http://qa.bhojpur.net.
# FEEDBACK_APP: 6 = ODE.web
FEEDBACK_APP = 6

# IGNORABLE_404_STARTS:
# Default: ('/cgi-bin/', '/_vti_bin', '/_vti_inf')
# IGNORABLE_404_ENDS:
# Default: ('mail.pl', 'mailform.pl', 'mail.cgi', 'mailform.cgi',
# 'favicon.ico', '.php')

# SESSION_FILE_PATH: If you're using file-based session storage, this sets the
# directory in which Django will store session data. When the default value
# (None) is used, Django will use the standard temporary directory for the
# system.
SESSION_FILE_PATH = tempfile.gettempdir()

# FILE_UPLOAD_TEMP_DIR: The directory to store data temporarily while
# uploading files.
FILE_UPLOAD_TEMP_DIR = tempfile.gettempdir()

# # FILE_UPLOAD_MAX_MEMORY_SIZE: The maximum size (in bytes) that an upload
# will be before it gets streamed to the file system.
FILE_UPLOAD_MAX_MEMORY_SIZE = 2621440  # default 2621440 (i.e. 2.5 MB).

# DEFAULT_IMG: Used in
# webclient.webclient_gateway.OdeWebGateway.defaultThumbnail in order to
# load default image while thumbnail can't be retrieved from the server.
DEFAULT_IMG = os.path.join(
    os.path.dirname(__file__),
    "webgateway",
    "static",
    "webgateway",
    "img",
    "image128.png",
).replace("\\", "/")

# # DEFAULT_USER: Used in
# webclient.webclient_gateway.OdeWebGateway.getExperimenterDefaultPhoto in
# order to load default avatar while experimenter photo can't be retrieved
# from the server.
DEFAULT_USER = os.path.join(
    os.path.dirname(__file__),
    "webgateway",
    "static",
    "webgateway",
    "img",
    "personal32.png",
).replace("\\", "/")

# MANAGERS: A tuple in the same format as ADMINS that specifies who should get
# broken-link notifications when
# SEND_BROKEN_LINK_EMAILS=True.
MANAGERS = ADMINS  # from CUSTOM_SETTINGS_MAPPINGS  # noqa

# https://docs.djangoproject.com/en/1.6/releases/1.6/#default-session-serialization-switched-to-json
# JSON serializer, which is now the default, cannot handle
# engine.connector.Connector object
SESSION_SERIALIZER = "django.contrib.sessions.serializers.PickleSerializer"

# Load custom settings from etc/grid/config.xml
# Tue  2 Nov 2010 11:03:18 GMT -- ticket:3228
# MIDDLEWARE: A tuple of middleware classes to use.
MIDDLEWARE = sort_properties_to_tuple(MIDDLEWARE_CLASSES_LIST)  # noqa

for k, v in DJANGO_ADDITIONAL_SETTINGS:  # noqa
    setattr(sys.modules[__name__], k, v)


# Load server list and freeze
def load_server_list():
    for s in SERVER_LIST:  # from CUSTOM_SETTINGS_MAPPINGS  # noqa
        server = (len(s) > 2) and text(s[2]) or None
        Server(host=text(s[0]), port=int(s[1]), server=server)
    Server.freeze()


load_server_list()