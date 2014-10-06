'use strict';

var ifkstatControllers = angular.module('ifkstatControllers', []);

//var phonecatApp = angular.module('phonecatApp', []);

ifkstatControllers.controller('PlayerListCtrl', ['$scope', '$http','$filter', 'ngTableParams', function($scope, $http, $filter, ngTableParams) {

    $http({method: 'GET', url: '/rest/view/players/summaries'}).
        success(function(data, status, headers, config) {
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 25,
                filter: {
                    name: ''
                }
            }, {
                total: data.length, // length of data
                getData: function($defer, params) {
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(data, params.orderBy()) :
                        data;
                    var sliced = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                    params.total(data.length);
                    $defer.resolve(sliced);
                }
            });
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });

}]);


ifkstatControllers.controller('ClubsListCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

        $http({method: 'GET', url: '/rest/view/clubs/stats'}).
            success(function(data, status, headers, config) {
                $scope.clubsList = new ngTableParams({
                    page: 1,
                    count: 25
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var sliced = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(sliced);
                    }
                });
            });
    }]
);


ifkstatControllers.controller('GamesListCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {
        $scope.playerId = $routeParams.id;
        $scope.tournamentId = $routeParams.tournamentId;
        $scope.clubId = $routeParams.clubId;
        if($scope.playerId && $scope.tournamentId) {
            var myUrl = '/rest/view/players/' + $scope.playerId + '/tournaments/' + $scope.tournamentId + '/games';
        } else if($scope.clubId) {
            var myUrl = '/rest/view/clubs/' + $scope.clubId + '/games';
        } else {
            var myUrl = '/rest/view/games';
        }


        $http({method: 'GET', url: myUrl}).
            success(function(data, status, headers, config) {
                $scope.gamesList = new ngTableParams({
                    page: 1,
                    count: 25,
                    filter: {
                        name: ''
                    }
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var sliced = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(sliced);
                    }
                });
            })
        if($scope.playerId) {
            $http({method: 'GET', url: '/rest/view/players/' + $scope.playerId}).
                success(function(data, status, headers, config) {
                    $scope.playerName = data.name;
                })
        }
        updateNavBarByElementId('nav-games');
    }]);

ifkstatControllers.controller('GameDetailCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {
        $scope.gameId = $routeParams.id;
        $http({method: 'GET', url: '/rest/view/games/' + $scope.gameId}).
            success(function(data, status, headers, config) {
                $scope.g = data;
            })

        $http({method: 'GET', url: '/rest/view/games/' + $scope.gameId + '/participants'}).
            success(function(data, status, headers, config) {
                $scope.participants = [];
                for(var i = 0; i < data.length; i++) {
                    if(data[i].participationType == 'STARTER' || data[i].participationType == 'SUBSTITUTE_OUT' ) {
                        var position = {
                            'positionalCss' : lineup.getPositionOffset(data[i].formationPosition.position),
                            'name': data[i].player.name,
                            'playerId' : data[i].player.id
                        }
                        $scope.participants.push(position);
                    }
                }

            })

        $http({method: 'GET', url: '/rest/view/games/' + $scope.gameId + '/events'}).
            success(function(data, status, headers, config) {

                $scope.gameEventsTable = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    groupBy: 'eventName',
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        var events = [];
                        for(var i = 0; i < slicedData.length; i++) {
                            var eventName =  slicedData[i].eventType == 'GOAL' ? 'Mål' : (slicedData[i].eventType == 'SUBSTITUTION_IN' ? 'Inbytt' : 'Utbytt');
                            var playerId = slicedData[i].player.id;
                            var playerName = slicedData[i].player.name;
                            var gameMinute = slicedData[i].gameMinute != null ?  slicedData[i].gameMinute : '--';

                            var row = {
                                'eventName' : eventName,
                                'playerId' :  playerId,
                                'playerName' : playerName,
                                'gameMinute' : gameMinute
                            }

                            events.push(row);
                        }
                        $defer.resolve(events);
                    }
                });
            })

        $http({method: 'GET', url: '/rest/view/games/' + $scope.gameId + '/notes'}).
            success(function(data, status, headers, config) {
                $scope.notes = data;
            });
    }]);


ifkstatControllers.controller('PhoneDetailCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

		$scope.p = {};
	
		$scope.playerId = $routeParams.id;
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId}).
        success(function(data, status, headers, config) {
                $scope.playerName = data.name;
            $scope.p = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });
		
		
		$scope.ps = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/stats'}).
        success(function(data, status, headers, config) {
            $scope.ps = data.averagesPerTournament;
            $scope.totals = data.totals;



            $scope.tableParams = new ngTableParams({
                page: 1,            // show first page
                count: 10,           // count per page,
                filter: {
                    name: ''       // initial filter
                }
            }, {
                total: $scope.ps.length, // length of data
                getData: function($defer, params) {
                    var orderedData = params.sorting() ?
                        $filter('orderBy')($scope.ps, params.orderBy()) :
                        $scope.ps;
                    $scope.ps = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                    params.total(orderedData.length);
                    $defer.resolve($scope.ps);
                }
            });
        }).
        error(function(data, status, headers, config) {
            alert("Error:"  + data);
        });


		
		$scope.pps = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/positions'}).
        success(function(data, status, headers, config) {
                $scope.positionsParams = new ngTableParams({
                    page: 1,            // show first page
                    count: 10,           // count per page,
                    filter: {
                        name: ''       // initial filter
                    }
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(slicedData);
                    }
                });
        });
		
		$scope.rs = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/resultstats'}).
        success(function(data, status, headers, config) {
                $scope.resultsParams = new ngTableParams({
                    page: 1,            // show first page
                    count: 10,           // count per page,
                    filter: {
                        name: ''       // initial filter
                    }
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(slicedData);
                    }
                });
        });
		
		$scope.rse = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/resultstats/full'}).
        success(function(data, status, headers, config) {
                $scope.extendedResultsParams = new ngTableParams({
                    page: 1,
                    count: 10
                }, {
                    groupBy: 'tournament',
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(slicedData);
                    }
                });

        });
		
		$scope.ses = {};
		$http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/gamespertournaments'}).
        success(function(data, status, headers, config) {
                $scope.seasonsParams = new ngTableParams({
                    page: 1,
                    count: 10,
                    filter: {
                        name: ''
                    }
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(slicedData);
                    }
                });
        });

        $scope.ses = {};
        $http({method: 'GET', url: '/rest/view/players/' + $scope.playerId + '/playedwith'}).
            success(function(data, status, headers, config) {
                $scope.playedWithParams = new ngTableParams({
                    page: 1,
                    count: 10,
                    filter: {
                        name: ''
                    }
                }, {
                    total: data.length, // length of data
                    getData: function($defer, params) {
                        var orderedData = params.sorting() ?
                            $filter('orderBy')(data, params.orderBy()) :
                            data;
                        var slicedData = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                        params.total(data.length);
                        $defer.resolve(slicedData);
                    }
                });
            });

        updateNavBarByElementId('nav-players');
    }]);