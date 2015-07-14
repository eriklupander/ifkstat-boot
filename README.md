ifkstat-boot
============

Rewrite of the ifkstat backend using Spring Boot, Spring-based REST instead of jax-rs, Spring security etc.


TODO 2014-10-18
* Add Spring Method caching. The data seldom changes, should be possible to optimize a lot of requests, especially those for
"all" data.
* Add a modal Ajax Loading Spinner
* Continue to add new stat views.
* Style the GUI, perhaps take a peek at the official ifkgoteborg.se colors, fonts etc.
* Implement automatic fetch of games. Either from a new Server API or start scraping ifkgoteborg.se