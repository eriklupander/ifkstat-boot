'use strict';

var phonecatControllers = angular.module('phonecatControllers', []);

//var phonecatApp = angular.module('phonecatApp', []);

phonecatApp.controller('PhoneListCtrl', ['$scope', '$http', function($scope, $http) {
    $scope.ordered_columns = [];
    $scope.all_columns = [{
        "key" : "name",
        "title": "Namn",
        "type": "string"
    }, {
        "key" : "games",
        "title": "Antal matcher",
        "type": "number"
    }, {
        "key" : "goals",
        "title": "Antal mål",
        "type": "number"
    },{
        "key" : "firstGame",
        "title": "Första match",
        "type": "string"

    }, {
        "key" : "lastGame",
        "title": "Sista match",
        "type": "string"
    }];

    $scope.gap = 5;

    $scope.groupedItems = [];
    $scope.itemsPerPage = 25;
    $scope.pagedItems = [];
    $scope.currentPage = 0;


    // calculate page in place
    $scope.groupToPages = function () {
        $scope.pagedItems = [];

        for (var i = 0; i < $scope.data.length; i++) {
            if (i % $scope.itemsPerPage === 0) {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)] = [ $scope.data[i] ];
            } else {
                $scope.pagedItems[Math.floor(i / $scope.itemsPerPage)].push($scope.data[i]);
            }
        }
    };

    $scope.range = function (size,start, end) {
        var ret = [];
        console.log(size,start, end);

        if (size < end) {
            end = size;
            start = size-$scope.gap;
        }
        for (var i = start; i < end; i++) {
            ret.push(i);
        }
        console.log(ret);
        return ret;
    };

    $scope.prevPage = function () {
        if ($scope.currentPage > 0) {
            $scope.currentPage--;
        }
    };

    $scope.nextPage = function () {
        if ($scope.currentPage < $scope.pagedItems.length - 1) {
            $scope.currentPage++;
        }
    };

    $scope.setPage = function () {
        $scope.currentPage = this.n;
    };


    $http({method: 'GET', url: '/rest/view/players/summaries'}).
        success(function(data, status, headers, config) {
            $scope.data = data;
            $scope.currentPage = 0;
            // now group by pages
            $scope.groupToPages();
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });

    //  $scope.ordered_columns.push($scope.all_columns);
    for (var i = 0; i < $scope.all_columns.length; i++) {
        var column = $scope.all_columns[i];
        $scope.ordered_columns.push(column);
    }

}]);

phonecatControllers.controller('PhoneDetailCtrl', ['$scope', '$http', '$routeParams',
    function($scope, $http, $routeParams) {
		$scope.p = {};
	
		$scope.playerId = $routeParams.id;
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId}).
        success(function(data, status, headers, config) {
            $scope.p = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });
		
		
		$scope.ps = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/stats'}).
        success(function(data, status, headers, config) {
            $scope.ps = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });
		
		$scope.pps = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/positions'}).
        success(function(data, status, headers, config) {
            $scope.pps = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });
		
		$scope.rs = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/resultstats'}).
        success(function(data, status, headers, config) {
            $scope.rs = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        })
		
		$scope.rse = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/resultstats/full'}).
        success(function(data, status, headers, config) {
            $scope.rse = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        })
		
		$scope.ses = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/gamespertournaments'}).
        success(function(data, status, headers, config) {
            $scope.ses = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        })
		
		
		
//        $scope.phone = Phone.get({phoneId: $routeParams.phoneId}, function(phone) {
//            $scope.mainImageUrl = phone.images[0];
//        });
//        $scope.setImage = function(imageUrl) {
//            $scope.mainImageUrl = imageUrl;
//        }
    }]);