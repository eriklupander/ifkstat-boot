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
            when('/clubs/stats', {
                templateUrl: 'partials/clubs-list.html',
                controller: 'ClubsListCtrl'
            }).
            when('/clubs/:clubId/games', {
                templateUrl: 'partials/games-list.html',
                controller: 'GamesListCtrl'
            }).
            when('/tournaments', {
                templateUrl: 'partials/tournaments-list.html',
                controller: 'TournamentsListCtrl'
            }).
            when('/tournaments/:id/seasons', {
                templateUrl: 'partials/tournamentseasons-list.html',
                controller: 'TournamentSeasonsListCtrl'
            }).
            when('/tournamentseasons/:tournamentSeasonId/games', {
                templateUrl: 'partials/games-list.html',
                controller: 'GamesListCtrl'
            }).
            otherwise({
                redirectTo: '/players'
            });
    }]);