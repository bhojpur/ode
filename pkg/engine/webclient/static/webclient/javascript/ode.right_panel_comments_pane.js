var CommentsPane = function CommentsPane($element, opts) {

    var $header = $element.children('h1'),
        $body = $element.children('div'),
        $comments_container = $("#comments_container"),
        objects = opts.selected;
    var self = this;

    var tmplText = $('#comments_template').html();
    var commentsTempl = _.template(tmplText);


    var initEvents = (function initEvents() {

        $header.on('click', function(){
            $header.toggleClass('closed');
            $body.slideToggle();

            var expanded = !$header.hasClass('closed');
            ODE.setPaneExpanded('comments', expanded);

            if (expanded && $comments_container.is(":empty")) {
                this.render();
            }
        }.bind(this));
    }).bind(this);

    // Comment field - show/hide placeholder and submit button.
    $("#add_comment_wrapper label").inFieldLabels();
    $("#id_comment")
        .on('blur', function(event){
            setTimeout(function(){
                $("#add_comment_form input[type='submit']").hide();
            }, 200);    // Delay allows clicking on the submit button!
        })
        .on('focus', function(){
            $("#add_comment_form input[type='submit']").show();
        });

    // bind removeItem to various [-] buttons
    $("#comments_container").on("click", ".removeComment", function(event){
        var url = $(this).attr('url');
        var objId = objects.join("|");
        ODE.removeItem(event, ".ann_comment_wrapper", url, objId);
        return false;
    });

    // handle submit of Add Comment form
    $("#add_comment_form").ajaxForm({
        beforeSubmit: function(data, $form, options) {
            var textArea = $('#add_comment_form textarea');
            if (textArea.val().trim().length === 0) return false;
            // here we specify what objects are to be annotated
            objects.forEach(function(o){
                var dtypeId = o.split("-");
                data.push({"name": dtypeId[0], "value": dtypeId[1]});
            });
        },
        success: function(html) {
            $("#id_comment").val("");
            self.render();
        },
    });


    this.render = function render() {

        if ($comments_container.is(":visible")) {

            if ($comments_container.is(":empty")) {
                $comments_container.html("Loading comments...");
            }

            var request = objects.map(function(o){
                return o.replace("-", "=");
            });
            request = request.join("&");

            $.getJSON(WEBCLIENT.URLS.webindex + "api/annotations/?type=comment&" + request, function(data){


                // manipulate data...
                // make an object of eid: experimenter
                var experimenters = data.experimenters.reduce(function(prev, exp){
                    prev[exp.id + ""] = exp;
                    return prev;
                }, {});

                // Populate experimenters within anns
                var anns = data.annotations.map(function(ann){
                    ann.owner = experimenters[ann.owner.id];
                    if (ann.link && ann.link.owner) {
                        ann.link.owner = experimenters[ann.link.owner.id];
                    }
                    ann.addedBy = [ann.link.owner.id];
                    return ann;
                });

                // Show most recent comments at the top
                anns.sort(function(a, b) {
                    return a.date < b.date ? 1 : -1;
                });

                // Remove duplicates (same comment on multiple objects)
                anns = anns.filter(function(ann, idx){
                    // already sorted, so just compare with last item
                    return (idx === 0 || anns[idx - 1].id !== ann.id);
                });

                // Update html...
                var html = "";
                if (anns.length > 0) {
                    html = commentsTempl({'anns': anns,
                                          'static': WEBCLIENT.URLS.static_webclient,
                                          'webindex': WEBCLIENT.URLS.webindex});
                }
                $comments_container.html(html);

                // Finish up...
                ODE.linkify_element($( ".commentText" ));
                ODE.filterAnnotationsAddedBy();
                $(".tooltip", $comments_container).tooltip_init();
            });
        }
    };


    initEvents();

    if (ODE.getPaneExpanded('comments')) {
        $header.toggleClass('closed');
        $body.show();
    }

    this.render();
};