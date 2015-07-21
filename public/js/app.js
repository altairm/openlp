var openlp = angular.module('openlp', ['ngResource']);

openlp
    .service('AnalyzeService', ['$resource', function($resource) {
            return $resource('/openlp', null, {
            });
    }])
    .controller('MainController', ['AnalyzeService', function(AnalyzeService) {
        var that = this;
        this.openlpModel = {};
        this.payload = function() {
            console.log(that.openlpModel);
            AnalyzeService.save(that.openlpModel);
        };
    }]);
