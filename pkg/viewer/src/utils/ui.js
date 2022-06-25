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

import {noView} from 'aurelia-framework';

import Misc from './misc';
import {IMAGE_VIEWER_RESIZE} from '../events/events';

/**
 * A utility class with various static helper methods for the ui
 */
@noView
export default class Ui {
    /**
     * Binds panel resize handlers
     *
     * @param {object} eventbus the eventbus for publishing
     * @static
     */
    static bindResizeHandlers(eventbus) {
        if (typeof eventbus !== 'object' ||
            typeof eventbus.publish !== 'function') return;

        $('.col-splitter').mousedown((e) => {
            e.preventDefault();
            let leftSplit = $(e.currentTarget).hasClass("left-split");

            $(document).mousemove((e) => {
                e.preventDefault();

                let el = leftSplit ? $('.thumbnail-panel') : $('.right-hand-panel');
                if (el.width() === 0) return false;

                let x = leftSplit ? e.pageX - el.offset().left :
                    $(window).width() - e.pageX;
                let frameWidth = $(".frame").width();
                let minWidth = leftSplit ? 25 : 50;
                let maxWidth = parseInt(el.css("max-width"));
                let tolerance =  $(window).width() -
                        (leftSplit ? $('.right-hand-panel').width() :
                            $('.thumbnail-panel').width())-200;
                if (maxWidth > tolerance)
                    maxWidth = tolerance;
                let rightBound = leftSplit ?
                    ($(window).width() - frameWidth) : $(window).width();

                if (x > minWidth && x < maxWidth && e.pageX < rightBound) {
                    el.width(x);
                    if (leftSplit)
                        $('.frame').css(
                            {"margin-left": '' + (-x-5) + 'px',
                             "padding-left": '' + (x+5) + 'px'});
                    else
                        $('.frame').css(
                            {"margin-right": '' + (-x-5) + 'px',
                             "padding-right": '' + (x+5) + 'px'});
                }
                eventbus.publish(IMAGE_VIEWER_RESIZE,
                    {config_id: -1, is_dragging: true});
            });

            $(document).mouseup((e) => {
                $(document).unbind('mousemove');
                $(document).unbind('mouseup');
                eventbus.publish(IMAGE_VIEWER_RESIZE,
                    {config_id: -1, is_dragging: false});
            });
        });
    }

    /**
     * Unbinds panel resize handlers
     *
     * @static
     */
    static unbindResizeHandlers() {
        $('.col-splitter').unbind("mousedown mouseup");
    }

    /**
     * Unbinds panel collapse handlers
     *
     * @static
     */
    static unbindCollapseHandlers() {
        $('.collapse-left').unbind("mousedown click");
        $('.collapse-right').unbind("mousedown click");
    }

    /**
     * Binds panel collapse handlers
     *
     * @param {object} eventbus the eventbus for publishing
     * @param {string} uri_prefix a uri prefix for resource requests
     * @static
     */
    static bindCollapseHandlers(eventbus, uri_prefix) {
        if (typeof uri_prefix !== 'string') uri_prefix = '';
        $('.collapse-left, .collapse-right').mousedown(
            (e) => {e.preventDefault(); e.stopPropagation();});

        $('.collapse-left, .collapse-right').click((e) => {
            let leftSplit =
                $(e.currentTarget).hasClass("collapse-left");
            let el = leftSplit ? $('.thumbnail-panel') : $('.right-hand-panel');

            let minWidth = leftSplit ? 20 : 50;
            let oldWidth = parseInt(el.attr("old-width"));
            let width = el.width();

            let tolerance =  $(window).width() -
                    (leftSplit ? $('.right-hand-panel').width() :
                        $('.thumbnail-panel').width())-200;

            let newWidth = width === 0 ?
                (isNaN(oldWidth) || oldWidth <= 0 ? minWidth : oldWidth) : 0;

            // we do not allow opening sidebars if there is not enough room
            // we try to reduce it to above set minWidth but if that is
            // not good enough, we don't open it
            if (newWidth !== 0 && newWidth > tolerance) {
                if (minWidth > tolerance) return;
                newWidth = tolerance;
            }
            el.attr("old-width", width);

            let url =
                uri_prefix === '' ?
                    Misc.pruneUrlToLastDash($(e.currentTarget).attr("src")) :
                    uri_prefix + "/css/images";
            $(e.currentTarget).attr(
                "src", url + "/collapse-" +
                (leftSplit && newWidth === 0 ||
                    !leftSplit && newWidth !== 0 ? "right" : "left") + ".png");
            el.width(newWidth);
            el.css('flex', '0 0 ' + newWidth + 'px');
            if (newWidth === 0)
                $(e.currentTarget).parent().css("cursor", "default");
            else
                $(e.currentTarget).parent().css("cursor", "ew-resize");
            if (leftSplit)
                $('.frame').css(
                    {"margin-left": '' + (-newWidth-5) + 'px',
                     "padding-left": '' + (newWidth+5) + 'px'});
            else
                $('.frame').css(
                    {"margin-right": '' + (-newWidth-5) + 'px',
                     "padding-right": '' + (newWidth+5) + 'px'});
            eventbus.publish(IMAGE_VIEWER_RESIZE, {config_id: -1});
        });
    }

    /**
     * Registers side bar resize and collapse handlers.
     * It's a convenience method for doing both handlers at the same time-slider
     * as well as unbinding beforehand
     *
     * @param {object} eventbus the eventbus for publishing
     * @param {string} uri_prefix a uri prefix for resource requests
     * @static
     */
    static registerSidePanelHandlers(eventbus, uri_prefix) {
        this.unbindResizeHandlers();
        this.unbindCollapseHandlers();
        this.bindResizeHandlers(eventbus);
        this.bindCollapseHandlers(eventbus, uri_prefix);
    }

    /**
     * Adjusts the sidebars in case of window resize
     *
     * @static
     */
    static adjustSideBarsOnWindowResize() {
        // if we get too small we'll collaps the thumbnail sidebar
        let sideBar = $('.thumbnail-panel');
        let otherSideBar = $('.right-hand-panel');
        let width = sideBar.width();
        let toleranceWidth = 0, origin=null;
        if (width > 0) {
            toleranceWidth = $(window).width() - otherSideBar.width() - 200;
            origin = $('.collapse-left');
            if (width > toleranceWidth)
                origin.trigger('click', {currentTarget: origin});
        }
        // if we get even smaller, we'll either resize the right hand panel or
        // collapse it as well
        sideBar = $('.right-hand-panel');
        width = sideBar.width();
        toleranceWidth = $(window).width() - 200;
        origin = $('.collapse-right');
        if (width > toleranceWidth) {
            if (toleranceWidth < 50)
                origin.trigger('click', {currentTarget: origin});
            else {
                 $('.right-hand-panel').width(toleranceWidth);
                $('.frame').css(
                    {"margin-right": '' + (-toleranceWidth-5) + 'px',
                     "padding-right": '' + (toleranceWidth+15) + 'px'});
            }
        }
    }

    /**
     * Shows a bootstrap modal message box
     *
     * @param {string} message the title of the dialog
     * @param {string} button_text a button with the given text will be shown
     *                             if a non-empty string is provided
     * @static
     */
    static showModalMessage(message, button_text) {
        if (typeof message !== 'string') message = '';
        if (typeof button_text === 'string')
            button_text = button_text.replace(/\s/g, "")
        else button_text = '';

        let modal = $('.modal-message');
        if (modal.length === 0) return;

        let footer = modal.find('.modal-footer');
        if (button_text) {
            footer.find('button').html(button_text);
            footer.show();
        } else footer.hide();

        modal.find('.modal-body').html(message);
        modal.modal();
    }

    /**
     * Hides the bootstrap modal message box
     *
     * @static
     */
    static hideModalMessage() {
        let modal = $('.modal-message');
        if (modal.length === 0) return;
        modal.find('.modal-body').html("");
        modal.modal('hide');
    }

    /**
     * Shows a bootstrap modal confirmation dialog
     *
     * @param {string} title the title of the dialog
     * @param {string} content goes into the body of the dialog
     * @param {function} yes a handler that's executed on clicking yes
     * @param {function} no an optional handler that's executed on clicking no
     * @param {boolean} can_close if true we allow closing by esc key and X click
     * @static
     */
     static showConfirmationDialog(
         title='', content='', yes = null, no = null, can_close = true) {
         if (typeof title !== 'string') title = 'Confirmation';
         if (typeof content !== 'string') content = '';
         if (typeof yes !== 'function') yes = null;
         if (typeof no !== 'function') no = null;
         if (typeof can_close !== 'boolean') can_close = true;

         let dialog = $('.confirmation-dialog');
         if (dialog.length === 0) return;

         if (can_close) dialog.find('.modal-header .close').show();
         else dialog.find('.modal-header .close').hide();

         dialog.find('.modal-title').html(title);
         dialog.find('.modal-body').html(content);

         // set up handlers and reset routine on close
         let handlers = [".yes", ".no"];
         handlers.map((h) => {
             dialog.find(h).off();
             dialog.find(h).on('click', () => {
                 dialog.modal("hide");
                 if (h === '.yes' && yes) yes();
                 if (h === '.no' && no) no();
             });
         });

         dialog.modal();
     }

      /**
       * Scrolls a container with a scrollbar in order to
       * put one of its children into the visible part of the container
       * if it cannot be seen already.
       *
       * @param {string} id the id of the child to scroll to
       * @param {string} container the selector for the container element
       * @param {number} header_height optional height of a header
       * @static
       */
      static scrollContainer(id, container, header_height) {
          if (typeof id !== 'string' || typeof container !== 'string') return;

          let el = document.getElementById(id);
          if (el === null) return;

          if (typeof header_height !== 'number' || header_height < 0)
            header_height = 0;
          let contEl = $(container);
          let scrollTop = contEl.scrollTop();
          let scrollBottom = scrollTop + contEl.outerHeight();
          let elTop = el.offsetTop - header_height;
          let elBottom = elTop + el.offsetHeight;
          if (elTop > scrollTop && elBottom < scrollBottom) return;

          let pos =
              elTop - parseInt((contEl.outerHeight() - el.offsetHeight) / 2);
          contEl.scrollTop(pos < 0 ? elTop : pos);
      }

      /**
       * Measures the browser's scrollbar width
       *
       * @static
       */
      static measureScrollbarWidth() {
          let outer = document.createElement("div");
          outer.style.visibility = "hidden";
          outer.style.width = "100px";
          document.body.appendChild(outer);

          let widthNoScroll = outer.offsetWidth;
          outer.style.overflow = "scroll";
          let inner = document.createElement("div");
          inner.style.width = "100%";
          outer.appendChild(inner);
          let widthWithScroll = inner.offsetWidth;
          outer.parentNode.removeChild(outer);

          return widthNoScroll - widthWithScroll;
    }

    /**
     * A helper function to register a list of keyhandlers via the context
     * @param {Context} context a reference to the Context instance
     * @param {Array.<Object>} handler_info array of objects providing key code and handler
     * @param {string} group the group info for registration, default: global
     * @param {Object} scope the scope for the handler function, default: null
     * @static
     */
    static registerKeyHandlers(context, handler_info, group='global', scope=null) {
        handler_info.map(
            (action) =>
                context.addKeyListener(
                    action.key,
                    (event) => {
                        let command =
                            Misc.isApple() ? 'metaKey' : 'ctrlKey';
                            if (context.selected_config === null ||
                                (group !== 'global' &&
                                context.selected_tab !== group) ||
                                ((typeof action.ctrl !== 'boolean' ||
                                  action.ctrl) && !event[command])) return true;
                            try {
                                action.func.apply(scope, action.args);
                            } catch(err) {
                                console.error("Key Handler failed: " + err);
                            }
                            return false;
                }, group, action.ctrl));
    }

    /**
     * Returns the prefix for the full screen api functionality
     * @return {string|null} the prefix or null if no support
     * @static
     */
    static getFullScreenApiPrefix() {
        try {
            let ret = null;

            if (document.body.webkitRequestFullscreen) ret = 'webkit'
            else if (document.body.mozRequestFullScreen) ret = 'moz'
            else if (document.body.msRequestFullscreen) ret = 'ms'
            else if (document.body.requestFullscreen) ret = "";

            // no support
            if (ret === null) return null;

            // now check the corresponding enabled flag
            if (document[ret + 'FullScreenEnabled'] ||
                document[ret + 'FullscreenEnabled']) return ret
        } catch(ignored){}
        return null;
    }
}