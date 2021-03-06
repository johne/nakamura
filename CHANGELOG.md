rSmart Nakamura Changelog
=========================

acad-1.0.2-M1-20111024
----------------------

* NOJIRA Correct version number for oracle 11g driver in contrib/oracle-jdbc-6 529552a
* correcting the host path for the Nakamura web start 5e0d9c0
* changing version numbers for the 1.0.2 release 7e45b12
* OAE-84 Rename unloader artifact to: com.rsmart.nakamura.uxloader a62d452
* updating version numbers for release 66473e6
* NOJIRA changing amazon provisioning so it can be done from Jenkins 4ec8f69
* KERN-2259 update solr version to 1.0.1 and consolidate in the base pom.xml 945663b
* KERN-2249 implement license plugin, add/update headers c373cf7
* KERN-2239 update basiclti test to expect an empty response 32d530c
* NOJIRA add newlines at the end of these scripts 020e319
* NOJIRA creating scripts that we'll use for automating our release pipeline 5e228c4
* KERN-2151 Dynamically create hybrid json config files. 4baf74e
* KERN-2151 Dynamically create hybrid json config files. 4743e94
* KERN-2151 Dynamically create hybrid json config files. 3a6b33f
* KERN-2151 Dynamically create hybrid json config files. 5c3f761
* NOJIRA switching to a datestamp version of sparsemap for stability in the release window of 1.0.1 a902f2a
* NOJIRA consolidating the commons.json version in the base pom be95a88
* NOJIRA use consistent version numbers for org.apache.sling.commons.json 5601d98
* KERN-2180 changing the test to reflect how activity feed permissions work 5867be4
* KERN-2235 - added saksi:pseudoGroup=true to create_pseudo_group() f62948a
* KERN-2230 for hybrid, add /s2site to the list of trusted exact paths 5cf31bc
* KERN-2205 - fixing test failures with wait_for_indexer, NPE with NPE guard and made int test script executable, fixing merge conflicts ce29dd0
* KERN-2163 added unit tests for LiteAllActivitiesResultProcessor f8cd9e0
* KERN-2205-squash - add code to prevent infinite loop in recursion through pseudo-groups, correcting misshandling of updateIntervalMinutes, moving full_group_creator to lib/sling for use in integration test, fixing several constants references to use UserConstants a757530
* KERN-2179 clean up error handling. Allow visibility to underlying exception when necessary. 596f1a3
* KERN-2179 create page indexing utility which works in conjunction with PoolContentResourceTypeHandler 9fd4059
* KERN-2151 remove redundant .json files 5100d27
* KERN-2151 re-added needed statics, added ability to configure list e52b03f
* Removed unneeded 'static' from variable definitions d09d062
* KERN-2151 fixed code review issues, made everything configurable c4fbeb4
* KERN-2221 Remove the Solr index record for the deleted user or group, not for the user doing the deletion ca8b979
* KERN-2151 add servlet to generate json for cle tool basiclti 5be43d7
* KERN-2226 Update Nakamura version on deployment of new code to existing server 5be5690
* KERN-2225 Add additional mime types for preview_processor to processor, and prevent preview_processor from trying to process audio/mpeg mime type. 034d4ba
* change the version number of the solr bundle back to 1.0 b704a5d
* updating the version file to 1.1-SNAPSHOT c6daa27
* switched version numbers to 1.1-SNAPSHOT f27b940
* NOJIRA binding to sparsemapcontent 1.1-SNAPSHOT and centralizing the version number 198876c
* NOJIRA Updated bundles to use 1.1-SNAPSHOT of sparse 1fb4c5c

oae-v1.0.0-M4-20111006
----------------------

* NO CHANGES

oae-v1.0.0-M3-20111004
----------------------

* KERN-2151 Dynamically create hybrid json config files. 1a247b0
* KERN-2151 Dynamically create hybrid json config files. (cherry picked from commit 4baf74efbfc0534ce4b774ff0d3db33ccdf2317c) 6986f4e
* KERN-2151 Dynamically create hybrid json config files. (cherry picked from commit 4743e945894cc3f602e211462984c1c4912dd42c) 48f76fe
* KERN-2151 Dynamically create hybrid json config files. (cherry picked from commit 3a6b33f08eb34036306a5817bae0c3e52958bfe3) 608e6fd
* KERN-2151 Dynamically create hybrid json config files. (cherry picked from commit 5c3f761b70fea98ba50996e0fc900efcac3f8773) b757521
* KERN-2151 remove redundant .json files (cherry picked from commit 5100d2750427506261fb9bd16b1a034a59679b69) cc18bd1
* KERN-2151 re-added needed statics, added ability to configure list (cherry picked from commit e52b03facc83f112778071528a8b9f12a6e5fda5) af36249
* KERN-2151 fixed code review issues, made everything configurable (cherry picked from commit c4fbeb4dde9703fa8dc50901bb95f251eb1424c7) fd41bf9
* KERN-2151 add servlet to generate json for cle tool basiclti (cherry picked from commit 5be43d7afd7fa3d7d0053f6d8c75a9fb1e3c4365) 64280bb
* OAE-33 replaced context-type of CourseSection with course; and user role of Learner with Student 7662fbc
* OAE-47 now using non-Administrative sessions for pulling user contact and group information 58d2cab
* OAE-49 Change artifactId for executable jar to be com.rsmart.academic.app e5095d1
* OAE-3, OAE-29 1c265e0
* OAE-3, OAE-29 22ee8a0
* OAE-29 cfcd350

oae-v1.0.0-M2-20110927
----------------------

* NOJIRA configure LTI client for DEV 97433a4
* OAE-23 14f2be3
* OAE-23 new query file and property provider for a user's contacts b0bae54

oae-v1.0.0-M1-20110921
----------------------

* OAE-3 ae0777b
* OAE-8 Enable MySQL Connector by default cf4fc06
* OAE-3 cc87cf3
* KERN-2221 Remove the Solr index record for the deleted user or group, not for the user doing the deletion (cherry picked from commit ca8b979d34362c853dcec0d6c6f2c4ddd33eaac9) 540e5f9
* KERN-2226 Update Nakamura version on deployment of new code to existing server (cherry picked from commit 5be56904ce987877506db815639b3fce6ae42d09) 01317de
* KERN-2225 Add additional mime types for preview_processor to processor, and prevent preview_processor from trying to process audio/mpeg mime type. (cherry picked from commit 034d4ba90510cca2740cbb483fe579ca0a5e15cc) 0a795f1
* KERN-2199 Include /dav in reference hybrid apache httpd configuration (cherry picked from commit b957c4d0cd810b3737a33a024b68fb967fdbc409) 0957d17
* KERN-2200 Upgrade mysql-connector-java to latest version 5.1.17 (cherry picked from commit c73772aaf902ed0d17c9e3c23b8e882141de0444) fc8b8df
* Inverted registration of LiteAuthorizablePostProcessors to avoid race conditions where dependencies load in the wrong order. (cherry picked from commit 475aaaccf5765aa2dea9c0da1d8191e3695ca81d) 36505ee
* Added missing reference that was preventing custom APP to be registered, may need to invert this. (cherry picked from commit 30ab4458cf1d8159f17aa0c26d7e0e373c522416) 00555ab


