db = db.getSiblingDB('dbank-assignment');

db.createCollection('branches');
db.createCollection('customers');

var branchesData = cat('/docker-entrypoint-initdb.d/branches.json');
var branches = JSON.parse(branchesData);
db.branches.insertMany(branches);
print("Branches data inserted successfully!");
var customersData = cat('/docker-entrypoint-initdb.d/customers.json');
var customers = JSON.parse(customersData);
db.customers.insertMany(customers);
print("Customers data inserted successfully!");

db.branches.createIndex({ "branchCode": 1 }, { unique: true });
db.customers.createIndex({ "customerNumber": 1 }, { unique: true });
db.customers.createIndex({ "branchCode": 1 });

print("All indexes created successfully!"); 