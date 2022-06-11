var RatingsPane = function RatingsPane($element, opts) {

    var $header = $element.children('h1'),
        $body = $element.children('div'),
        $rating_annotations = $("#rating_annotations"),
        objects = opts.selected,
        canAnnotate = opts.canAnnotate,
        self = this;

    var request = objects.map(function(o){
        return o.replace("-", "=");
    });
    request = request.join("&");

    var tmplText = $('#ratings_template').html();
    var ratingsTempl = _.template(tmplText);


    var initEvents = (function initEvents() {

        $header.on('click', function(){
            $header.toggleClass('closed');
            $body.slideToggle();

            var expanded = !$header.hasClass('closed');
            ODE.setPaneExpanded('ratings', expanded);

            if (expanded && $rating_annotations.is(":empty")) {
                this.render();
            }
        }.bind(this));
    }).bind(this);


    $("#rating_annotations.canAnnotate").on("click", ".myRating img", function(event){
        var $rating = $(this),
            clickX = event.pageX - $rating.offset().left;
        var r = (clickX/ $rating.width()) * 5;
        r = parseInt(Math.ceil(r), 10);
        setRating(r, $rating);
    });

    $("#rating_annotations.canAnnotate").on("click", ".removeRating", function(event){
        var $liMyRating = $(this).parent(),
            $ratingimg = $liMyRating.find('img');
        setRating(0, $ratingimg);
    });

    var setRating = function(rating, $rating) {
        // update rating
        if ($rating) {
            var rating_src = WEBCLIENT.URLS.static_webclient + "image/rating" + rating + ".png";
            $rating.attr('src', rating_src);
        }
        // update rating annotation
        var rating_url = WEBCLIENT.URLS.webindex + "annotate_rating/?" + request;
        rating_url += "&rating=" + rating;
        $.post(rating_url, function(data) {
            // update summary
            self.render();
        });
    };


    var isClientMapAnn = function(ann) {
        return ann.ns === ODE.constants.metadata.NSCLIENTMAPANNOTATION;
    };
    var isMyClientMapAnn = function(ann) {
        return isClientMapAnn(ann) && ann.owner.id == WEBCLIENT.USER.id;
    };


    this.render = function render() {

        if ($rating_annotations.is(":visible")) {

            if ($rating_annotations.is(":empty")) {
                $rating_annotations.html("Loading ratings...");
            }

            $.getJSON(WEBCLIENT.URLS.webindex + "api/annotations/?type=rating&" + request, function(data){

                var anns = data.annotations;
                var sum = anns.reduce(function(prev, ann){
                    return prev + ann.longValue;
                }, 0);
                var myRatings = anns.filter(function(ann){
                    return ann.owner.id == WEBCLIENT.USER.id;
                });
                var average = Math.round(sum/anns.length);

                // Update html...
                var html = ratingsTempl({'anns': anns,
                                         'canAnnotate': canAnnotate,
                                         'average': average,
                                         'count': anns.length,
                                         'static': WEBCLIENT.URLS.static_webclient});
                $rating_annotations.html(html);

                // Finish up...
                ODE.filterAnnotationsAddedBy();
                $(".tooltip", $rating_annotations).tooltip_init();
            });
        }
    };


    initEvents();

    if (ODE.getPaneExpanded('ratings')) {
        $header.toggleClass('closed');
        $body.show();
    }

    this.render();
};