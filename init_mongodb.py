from pymongo import MongoClient

# Connect to MongoDB server
client = MongoClient('mongodb://localhost:27017/')

# Create a new database
database_name = 'taskManager'
db = client[database_name]

# Create a collection in the database
collection_name = 'tasks'
collection = db.create_collection(collection_name)

collection_name = 'users'
collection = db.create_collection(collection_name)

collection_name = 'refreshToken'
collection = db.create_collection(collection_name)

collection_name = 'roles'
collection = db.create_collection(collection_name)

# Insert a document into the collection
document = {"name": "ROLE_USER"}
result = collection.insert_one(document)
document = {"name": "ROLE_ADMIN"}
result = collection.insert_one(document)
document = {"name": "ROLE_MODERATOR"}
result = collection.insert_one(document)

# Close the connection
client.close()
