'use strict';

var phonecatApp = angular.module('phonecatApp', [
    'ngRoute',
	'ui.bootstrap',
    'ngTable',
    'ifkstatControllers'
]);
phonecatApp.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider.
            when('/players', {
                templateUrl: 'partials/player-list.html',
                controller: 'PlayerListCtrl'
            }).
            when('/players/:id', {
                templateUrl: 'partials/player-detail.html',
                controller: 'PhoneDetailCtrl'
            }).
            when('/players/:id/tournaments/:tournamentId/games', {
                templateUrl: 'partials/games-list.html',
                controller: 'GamesListCtrl'
            }).
            when('/games', {
                templateUrl: 'partials/games-list.html',
                controller: 'GamesListCtrl'
            }).
            when('/games/:id', {
                templateUrl: 'partials/game-detail.html',
                controller: 'GameDetailCtrl'
            }).
            otherwise({
                redirectTo: '/players'
            });
    }]);