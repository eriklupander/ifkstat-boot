ifkstat-boot
============

Rewrite of the ifkstat backend using Spring Boot, Spring-based REST instead of jax-rs, Spring security etc.

** Setup **
* MySQL only supported db at the moment
* Create user 'ifkstat' with DBA or similar privileges
* If applicable, change db.url, db.password property in /config/application.properties file
* Change other passwords for default users in /config/application.properties if doing this for production.
* Create schema 'ifkstat' with latin1 collation
* Manually create DB tables for 'users' and 'authorities'. They are populated on first startup.

    create table users (    username varchar(50) not null primary key,    password varchar(255) not null,    enabled boolean not null) engine = InnoDb;
    create table authorities (    username varchar(50) not null,    authority varchar(50) not null,    foreign key (username) references users (username),    unique index authorities_idx_1 (username, authority))

* Login as superadmin. Click Admin tab. Click "Init core data"
* To bulk import, copy the generated .txt file with game data into the textarea and press 'Bulk import'. Takes a few to many minutes.

=======
Automated provisioning

# Provisionering av resurser för ifkstat
> ansible-playbook -i hosts_demo provision.yml

# Driftsättning av ifk
> ansible-playbook -i hosts_demo deploy.yml



TODO 2015-08-01
* Add notes import to GUI
* Add a tiny bit of styling to the admin GUI
* Check if ROLE of user can be fetched OK and used in GUI - e.g. only show admin GUI for admin/superadmin
* Many "clickables" left to do in tables.
* Add REST endpoint for Trivia controllers. Note: Use in-memory cached data for trivia game data.

* Add Spring Method caching. The data seldom changes, should be possible to optimize a lot of requests, especially those for
"all" data.
* Add a modal Ajax Loading Spinner
* Continue to add new stat views.
* Style the GUI, perhaps take a peek at the official ifkgoteborg.se colors, fonts etc.
* Implement automatic fetch of games. Either from a new Server API or start scraping ifkgoteborg.se
