var openlp = angular.module('openlp', ['ngResource']);

openlp
    .service('AnalyzeService', ['$resource', function($resource) {
            return $resource('/openlp', null, {
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
