
Enviroment setup
-----------------
1. Install JDK 11
2. Install MongoDB community version
3. Create DB Name as 'aaa'
4. Setup maven
5. Make sure you have 2 projects
   I.  ihcd-eureka-naming-server
   II. ihcd-zuul-api-gateway
   
6. do the following commands on both the projects, as a result it will create target folder on both the projects
   >mvn clean install
7. goto target folder of the both the projects and run the snapshot jars
    > java -jar ihcd-eureka-naming-server-0.0.1-SNAPSHOT.jar
    > java -jar ihcd-zuul-api-gateway-0.0.1-SNAPSHOT.jar
   > 
6. Create 2 users having one user with 'ADMIN' & another user with 'MANAGER' roles by using postman 
   POST - http://localhost:8765/api/users
   Request payload
   {
   "username":"manager",
   "password":"manager",
   "firstname":"John",
   "lastname":"manager",
   "email":"manager@gmail.com",
   "mobile":"testmobile",
   "isQRAuthEnabled": "false",
   "isQRSetupdone": "false",
   "emailverifyenabled": "false",    
   "qrSecretcode": "",
   "isQRCodeverified": "false",
   "roles": [
   {"role":"MANAGER"},        
   {"role":"USER"}
   ],
   "addresses": [
   {
   "addresstype":"Permanant",
   "country":"USA",
   "state":"MN"
   },
   {
   "addresstype":"Mailling",
   "country":"USA",
   "state":"MN"
   }        
   ]
   }

6. Test ADMIN user
   1. Login ADMIN user by using presignin API
   2. Try to access ADMIN access enabled API - GET  http://localhost:8765/admin/users
      Expected Response : 200 along with registered all users as json format
   2. Try to access other than ADMIN access enabled API - GET  http://localhost:8765/management/reports
      Expected Response : 403 - Fobidden

7. Test MANAGER user
   1. Login MANAGER user by using presignin API
   2. Try to access MANAGER access enabled API - GET  http://localhost:8765/management/reports
      Expected Response : 200 along with registered all users as json format
   2. Try to access other than ADMIN access enabled API - GET  http://localhost:8765/admin/users
      Expected Response : 403 - Fobidden      

      

      
   
    