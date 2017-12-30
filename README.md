# mtlshipback

Google project name: mtlglobalshipback

The process being implemented:

![alt text](https://www.websequencediagrams.com/cgi-bin/cdraw?lz=dGl0bGUgTVRMIE9yZGVyIFByb2Nlc3MKCkN1c3RvbWVyLT4rTVRMOiAACAggUHVyY2hhc2UAKgcoMSkKTVRMLT4rVmVuZG9yOiBPdQATEjIpCgAZBi0tPgBKBUV4LW1pbGwgZGF0ZSAoMwAWCT4tAGkFAEYGIGludm9pY2UgKDQAXgdGb3J3YXJkZXI6IFJlc2VydmUgY29udGFpbmVyICg1AIEEBy0AgTUIOiBJADoINikKCg&s=patent)

## Compiling

### Deploying

    gradle appengineDeploy

### Deploying to Heroku

    git push heroku master
    
### Cleaning up database

    
    
## Status

### 1 Customer Purchase Order Creation

* Can create customers
* Can look at customer detail
* Can get a list of customer purchase orders
* Can create a new purchase order
* Can create individual items

**TO DO**:

* Purchase order calculations
* Get item defaults from design entity
* Initiate cancel workflow
* Edit item
* Delete item

### 2 Our Purchase Order Creation

* Can create vendors
* Can look at vendor detail
* Can get a list of our purchase orders for vendor
* Can create new purchase order
* Can assign individual item to purchase order

**TO DO**:
* Purchase order calculations
* Remove item from purchase order
* Edit item
* The cancel process
* Finish the orange color stuff

### 3 Update The Ex-Mill Date

* Reports missing ex-date in orange
* Edit item added

**TO DO**

* Check to make sure ex-mill date > invoice date in both the front end and back end

### 4 Add Functionality To Add A Vendor Invoice

Major work done

**To Do**

### 5 Shipping Data Creation

**To Do**

### 6 Our Invoice Creation

**To Do**

### 7 Reports

**To Do**

## Process Code

    title MTL Order Process
    
    Customer->+MTL: Customer Purchase Order (1)
    MTL->+Vendor: Our Purchase Order (2)
    Vendor-->MTL: Ex-mill date (3)
    Vendor->-MTL: Vendor invoice (4)
    MTL->Forwarder: Reserve container (5)
    MTL->-Customer: Invoice (6)
    
https://www.websequencediagrams.com/