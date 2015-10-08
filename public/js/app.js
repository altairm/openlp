var openlp = angular.module('openlp', ['ngResource']);

openlp
    .service('AnalyzeService', ['$resource', function($resource) {
            return $resource('/api/openlp', null, {
            });
    }])
    .controller('MainController', ['AnalyzeService', function(AnalyzeService) {
        var that = this;
        this.openlpModel = {};
        this.payloadModel = [];
        this.isLoading = false;
        this.payload = function() {
            that.isLoading = true;
            AnalyzeService.save(that.openlpModel,
                function(payload){
                    if (payload.$resolved && payload.list) {
                        that.isLoading = false;
                        that.payloadModel = payload.list;
                    }
                },
                function(error) {
                    that.isLoading = false;
                    that.payloadModel = ['ERROR'];
                }
            );
        };
    }]);


var phantom = angular.module('phantom', ['ngResource']);

phantom
    .service('PhantomService', ['$resource', function($resource) {
            return $resource('/api/phantom', null, {
            });
    }])
    .controller('PhantomController', ['PhantomService', function(PhantomService) {
            var that = this;
            this.phantomModel = {};
            this.payloadModel = [];
            this.totalTime = "";
            this.isLoading = false;
            this.payload = function() {
                that.isLoading = true;
                PhantomService.save(that.phantomModel,
                    function(payload){
                        if (payload.$resolved && payload.list) {
                            that.isLoading = false;
                            that.payloadModel = payload.list;
                            that.totalTime = payload.time;
                        }
                    },
                    function(error) {
                        that.isLoading = false;
                        that.payloadModel = ['ERROR'];
                    }
                );
            };
        }]);