'use strict';

var ifkstatControllers = angular.module('ifkstatControllers', []);


ifkstatControllers.controller('AdminCtrl', ['$scope', '$http', function($scope, $http) {

    $scope.bulkData = "";

    $scope.bulkUpload = function(data) {
        var req = {
            method: 'POST',
            url: '/rest/superadmin/games',
            headers: {
                'Content-Type': 'text/plain',
                'Accepts' : 'text/plain'
            },
            data: data
        }
        $http(req).
            success(function(data, status, headers, config) {
                console.log("Bulk upload OK");
                $scope.uploadSuccessful = true;
            }).
            error(function(data, status, headers, config) {
                alert("Error:"  + data);
                $scope.uploadSuccessful = false;
            });
    }


    $scope.init = function() {
        var req = {
            method: 'POST',
            url: '/rest/superadmin/init',
            headers: {
                'Content-Type': 'text/plain',
                'Accepts' : 'text/plain'
            },
            data: {  }
        }
        $http(req).
            success(function(data, status, headers, config) {
                console.log("INIT OK");
                $scope.initSuccessful = true;
            }).
            error(function(data, status, headers, config) {
                alert("Error:"  + data);
                $scope.initSuccessful = false;
            });
    }


}]);


ifkstatControllers.controller('PlayerListCtrl', ['$scope', '$http','$filter', 'ngTableParams', function($scope, $http, $filter, ngTableParams) {

    $http({method: 'GET', url: '/rest/view/players/summaries'}).
        success(function(data, status, headers, config) {
            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 25,
                filter: {
                    name: ''
                },
                sorting: {
                    name: 'asc'     // initial sorting
                }
            }, {
                total: data.length, // length of data
                getData: function($defer, params) {

                    // use build-in angular filter
                    var filteredData = params.filter() ?
                        $filter('filter')(data, params.filter()) :
                        data;
                    var orderedData = params.sorting() ?
                        $filter('orderBy')(filteredData, params.orderBy()) :
                        filteredData;


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

ifkstatControllers.controller('navigation', function($rootScope, $scope, $http, $location) {

    var authenticate = function(credentials, callback) {

        var headers = credentials ? {authorization : "Basic "
            + btoa(credentials.username + ":" + credentials.password)
        } : {};

        $http.get('user', {headers : headers}).success(function(data) {
            if (data.name) {
                $rootScope.authenticated = true;
            } else {
                $rootScope.authenticated = false;
            }
            callback && callback();
        }).error(function() {
                $rootScope.authenticated = false;
                callback && callback();
            });

    }

    authenticate();
    $scope.credentials = {};
    $scope.login = function() {
        authenticate($scope.credentials, function() {
            if ($rootScope.authenticated) {
                $location.path("/");
                $scope.error = false;
            } else {
                $location.path("/login");
                $scope.error = true;
            }
        });
    };

    $scope.logout = function() {
        $http.post('logout', {}).success(function() {
            $rootScope.authenticated = false;
            $location.path("/");
        }).error(function(data) {
                $rootScope.authenticated = false;
                $location.path("/");
            });
    };

    $http({method: 'GET', url: '/rest/view/countries'}).
        success(function(data, status, headers, config) {
            $rootScope.countries = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error getting countries:"  + status);
        });

    $http({method: 'GET', url: '/rest/view/positiontypes'}).
        success(function(data, status, headers, config) {
            $rootScope.positiontypes = data;
        }).
        error(function(data, status, headers, config) {
            alert("Error getting positiontypes:"  + status);
        });
});

ifkstatControllers.controller('ClubsListCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

        $scope.handle = null;

        $scope.searchTerm = '';
        $scope.clubSearch = function() {
            if ($scope.searchTerm.length > 0) {
                if ($scope.handle != null) {
                    clearTimeout($scope.handle);
                }
                $scope.handle = setTimeout(execClubSearch, 500);
            } else {
                execClubSearch();
            }
        }


        var execClubSearch = function() {
            $scope.handle = null;
            $http({method: 'GET', url: '/rest/view/clubs/stats'}).
                success(function(data, status, headers, config) {
                    $scope.clubsList = new ngTableParams({
                        page: 1,
                        count: 25,
                        filter: {
                            clubName: $scope.searchTerm
                        },
                        sorting: {
                            clubName: 'asc'     // initial sorting
                        }
                    }, {
                        total: data.length, // length of data
                        getData: function($defer, params) {
                            var filteredData = params.filter() ?
                                $filter('filter')(data, params.filter()) :
                                data;
                            var orderedData = params.sorting() ?
                                $filter('orderBy')(filteredData, params.orderBy()) :
                                filteredData;
                            var sliced = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count());
                            params.total(data.length);
                            $defer.resolve(sliced);
                        }
                    });
                });

        };
        execClubSearch();
    }]
);


ifkstatControllers.controller('TournamentsListCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

        $http({method: 'GET', url: '/rest/view/tournaments'}).
            success(function(data, status, headers, config) {
                $scope.tournamentsParams = new ngTableParams({
                    page: 1,
                    count: 25,
                    sorting : {
                        'name' : 'asc'
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
            });
    }]
);

ifkstatControllers.controller('TournamentSeasonsListCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

        $http({method: 'GET', url: '/rest/view/tournaments/' + $routeParams.id + '/seasons'}).
            success(function(data, status, headers, config) {
                $scope.tournamentseasonsParams = new ngTableParams({
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
        $scope.tournamentSeasonId = $routeParams.tournamentSeasonId;
        $scope.scoredGoal = $routeParams.scoredGoal;

        if($scope.playerId && $scope.tournamentId) {

            if(!$scope.scoredGoal) {
                var myUrl = '/rest/view/players/' + $scope.playerId + '/tournaments/' + $scope.tournamentId + '/games';
            } else {
                var myUrl = '/rest/view/players/' + $scope.playerId + '/tournaments/' + $scope.tournamentId + '/games/goals';
            }
            $http({method: 'GET', url: '/rest/view/tournaments/' + $scope.tournamentId}).
                success(function(data, status, headers, config) {
                    $scope.tournamentName = data[0].name;
                });

        } else if($scope.clubId) {
            var myUrl = '/rest/view/clubs/' + $scope.clubId + '/games';
            $http({method: 'GET', url: '/rest/view/clubs/' + $scope.clubId}).
                success(function(data, status, headers, config) {
                    $scope.vsClub = data.name;
                });
        } else if($scope.tournamentSeasonId) {
            var myUrl = '/rest/view/tournamentseasons/' +$scope.tournamentSeasonId+ '/games';
            $http({method: 'GET', url: '/rest/view/tournamentseasons/' + $scope.tournamentSeasonId}).
                success(function(data, status, headers, config) {
                    $scope.tournamentSeasonName = data.tournament.name + ', ' + data.seasonName;
                });
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
                    },
                    sorting: {
                        dateOfGame: 'desc'     // initial sorting
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


ifkstatControllers.controller('PlayerDetailCtrl', ['$scope', '$http', '$routeParams', '$filter', 'ngTableParams',
    function($scope, $http, $routeParams, $filter, ngTableParams) {

		$scope.p = {};
        $scope.edit = false;
        $scope.toggleEdit = function() {
            $scope.edit = !$scope.edit;
        };
        $scope.update = function(p) {

            $scope.toggleEdit();
            $http.put('/rest/admin/players/' + $scope.playerId, p).
                success(function(data, status, headers, config) {
                    console.log("Save OK");
                    $scope.p = data; // Update the client-side model with the returned object.
                }).
                error(function(data, status, headers, config) {
                    alert("Error:"  + data);
                });
        }
	
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
                },
                sorting: {
                    tournamentName : 'asc'
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
                    },
                    sorting: {
                        games : 'desc'
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

                        orderedData = $.map(orderedData, function(row) {
                            if (row.participationType == 'STARTER') row.participationType = 'Startelvan';
                            if (row.participationType == 'SUBSTITUTE') row.participationType = 'Inhoppare';
                            if (row.participationType == 'NO_PART') row.participationType = 'Deltog ej';
                            return row;
                        });

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
                    count: 10,
                    sorting: {
                        tournament : 'asc',
                        season : 'desc'
                    }

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
                    },
                    sorting : {
                        tournamentName : 'asc',
                        seasonName : 'desc'
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
                    },
                    sorting: {
                        gamesWithPlayer: 'desc'
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