SharedService
=============

An Android implementation of a Service shared between multiple Activities.  Uses a "sticky service" and then a ServiceManager class that is responsible for managing the life-cycle and binding for the Service. The ServiceManager class is put into a sub-class of Application so the Activities can access it as a scoped global.
