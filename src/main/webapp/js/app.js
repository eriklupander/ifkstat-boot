'use strict';

var ifkstatApp = angular.module('ifkstatApp', [
    'ngRoute',
	'ui.bootstrap',
    'ngTable',
    'ifkstatControllers'
]);
ifkstatApp.config(['$routeProvider', '$httpProvider',
    function($routeProvider, $httpProvider) {
        $routeProvider.
            when('/admin', {
                templateUrl: 'partials/admin.html',
                controller: 'AdminCtrl'
            }).
            when('/players', {
                templateUrl: 'partials/player-list.html',
                controller: 'PlayerListCtrl'
            }).
            when('/players/:id', {
                templateUrl: 'partials/player-detail.html',
                controller: 'PlayerDetailCtrl'
            }).
            when('/players/:id/tournaments/:tournamentId/games', {
                templateUrl: 'partials/games-list.html',
                controller: 'GamesListCtrl'
            }).
            when('/players/:id/tournaments/:tournamentId/games/:scoredGoal', {
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
            }).when('/login', {
                templateUrl : 'login.html',
                controller : 'navigation'
            }).when('/createUser', {
                templateUrl : 'admin/createuser.html',
                controller : 'navigation'
            }).
            otherwise({
                redirectTo: '/players'
            });

        $httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
        $httpProvider.defaults.withCredentials = true;
    }]);