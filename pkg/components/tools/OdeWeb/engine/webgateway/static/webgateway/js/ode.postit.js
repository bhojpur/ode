/**
 * Post-it plugin for JQuery.
 *
 * Called on a container, creates a post-it like thingy
 */

 var aop_handler = function (meth) {
    return function () {
      if (this.get(0) && typeof this.get(0)[meth] == 'function') {
          return this.get(0)[meth]();
      }
        return this;
    };
  };
  
  jQuery.aop.before( {target: jQuery, method: 'show'}, aop_handler('postit_open_handler'));
  jQuery.aop.after( {target: jQuery.fn, method: 'hide'}, aop_handler('postit_close_handler'));
  
  $.fn.postit = function(cfg) {
    this.sdialog();
    return this.each(function(){
      /* The basic setup */
      var self = jQuery(this);
      this.postit_open_handler = function () {
        self.trigger('opening');
          return this;
      };
      this.postit_close_handler = function () {
        self.trigger('closed');
          return this;
      };
      /* Some extra details on the dragbar */
      var dragbar = self.find('h1:first');
      dragbar.on('dblclick', function(e) { self.toggleClass('collapsed'); });
  
      if (dragbar.get(0).addEventListener) {
        // Respond to mouse wheel in Firefox
        dragbar.get(0).addEventListener('DOMMouseScroll', function(e) { 
        if (e.detail > 0)
          self.removeClass('collapsed');
        else if (e.detail < 0)
          self.addClass('collapsed');
        e.preventDefault();
        }, false);
      }
  
      self
        .append('<div class="postit-toggle-btn">')
        .append('<div class="postit-close-btn">')
        .append('<div class="postit-resize-bar">')
        .addClass('postit')
        .removeClass('sdialog');
      /* The buttons */
      self.find('.postit-toggle-btn').on('click', function(e) { self.toggleClass('collapsed'); });
      self.find('.postit-close-btn').on('click', function(e) { self.hide(); });
      self.find('.sdialog-content').removeClass('sdialog-content').addClass('postit-content');
      if (cfg && !cfg.noResize) {
        var target;
        if (cfg && cfg.resizeTarget) {
          target = $(cfg.resizeTarget, self);
            target.resizable({minHeight: 200, minWidth: 200, containment: 'document'});
        } else {
          target = self;
            target.resizable({minHeight: 40, minWidth: 40});
        }
      }
  
      /* We're done, make it draggable */
      var dropEvent = function (e) {
        var viewport = {width: $('body').width(), height: $('body').height()};
        var boundaries = self.position();
        boundaries.right = boundaries.left + self.width();
        boundaries.bottom = boundaries.top + self.height();
        if (boundaries.left < 0) {
          self.css('left', 0);
        } else if (boundaries.right > viewport.width) {
          self.css('left', viewport.width - self.width());
        }
        if (boundaries.top < 0) {
          self.css('top', 0);
        } else if (boundaries.bottom > viewport.height) {
          var top = Math.max(viewport.height - self.height(), 0);
          self.css('top', top);
        }
      };
        self.draggable({handle: 'h1:first', scroll: false, containment: 'document'});
      self.find('h1:first').on('mouseup', dropEvent)
    });
  }