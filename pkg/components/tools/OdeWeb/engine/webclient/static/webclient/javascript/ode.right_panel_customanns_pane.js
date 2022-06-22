var CustomAnnsPane = function CustomAnnsPane($element, opts) {

    var $header = $element.children('h1'),
        $body = $element.children('div'),
        $custom_annotations = $("#custom_annotations"),
        objects = opts.selected;

    var tmplText = $('#customanns_template').html();
    var customannsTempl = _.template(tmplText);


    var initEvents = (function initEvents() {

        $header.on('click', function(){
            $header.toggleClass('closed');
            $body.slideToggle();

            var expanded = !$header.hasClass('closed');
            ODE.setPaneExpanded('others', expanded);

            if (expanded && $custom_annotations.is(":empty")) {
                this.render();
            }
        }.bind(this));
    }).bind(this);


    // display xml in a new window      
    $body.on( "click", ".show_xml", function(event) {
        var xml = $(event.target).next().html();
        var newWindow=window.open('','','height=500,width=500,scrollbars=yes, top=50, left=100');
        newWindow.document.write(xml);
        newWindow.document.close();
        return false;
    });


    this.render = function render() {

        if ($custom_annotations.is(":visible")) {

            if ($custom_annotations.is(":empty")) {
                $custom_annotations.html("Loading other annotations...");
            }

            var request = objects.map(function(o){
                return o.replace("-", "=");
            });
            request = request.join("&");

            $.getJSON(WEBCLIENT.URLS.webindex + "api/annotations/?type=custom&" + request, function(data){

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

                    // AddedBy IDs for filtering
                    ann.addedBy = [ann.link.owner.id];
                    // convert 'class' to 'type' E.g. XmlAnnotationI to Xml
                    ann.type = ann.class.replace('AnnotationI', '');
                    var attrs = ['textValue', 'timeValue', 'termValue', 'longValue', 'doubleValue', 'boolValue'];
                    attrs.forEach(function(a){
                        if (ann[a] !== undefined){
                            ann.value = ann[a];
                        }
                    });
                    if (objects.length > 1) {
                        ann.parent = {
                            'class': ann.link.parent.class.slice(0, -1), // slice parent class 'ProjectI' > 'Project'
                            'id': ann.link.parent.id
                        };
                    }
                    return ann;
                });

                // Show most recent annotations at the top
                anns.sort(function(a, b) {
                    return a.date < b.date;
                });

                // Update html...
                var html = "";
                if (anns.length > 0) {
                    html = customannsTempl({'anns': anns,
                                          'static': WEBCLIENT.URLS.static_webclient,
                                          'webindex': WEBCLIENT.URLS.webindex});
                }
                $custom_annotations.html(html);

                // Finish up...
                ODE.filterAnnotationsAddedBy();
                $(".tooltip", $custom_annotations).tooltip_init();
            });
        }
    };


    initEvents();

    if (ODE.getPaneExpanded('others')) {
        $header.toggleClass('closed');
        $body.show();
    }

    this.render();
};