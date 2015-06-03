========
Get Boot
========

A simple application that queries the github API and makes links to the latest release of boot, the best damned little build tool ever.

Installation
============
How you deploy chiefly depends on your target. For most situations, you can just install boot, and then run:

.. code-block:: shell-session
   
   $ ./get-latest
   
By default, it runs on port 3000. You can optinally pass a port as the first command line option:

.. code-block:: shell-session
   
   $ ./get-latest 9999
   
   
You can also use the included :code:`build.boot` file to compile a standalone jar or war file:

.. code-block:: shell-session
   
   $ boot build-war
   $ boot standalone
   
The files will be output to the :code:`target` directory.
   
The war file will be called :code:`getboot.war` and can be deployed in the usual way to tomcat or elastic beanstalk.

The jar file's name will depend on the particular version of the application. For the current release, :code:`1.0.0`, the
jar file is named :code:`latest-boot-1.0.0.jar`.

can be run with :code:`java -jar`, and can also take a port as the first CLI parameter:

.. code-block:: shell-session
   
   $ java -jar target/latest-boot-1.0.0.jar 9999
   

