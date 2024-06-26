import requests
api_url = "http://localhost:8888/api/auth/signup"
number_of_users = 10000

for i in range(number_of_users):
    user_data = {
        "username": f"user_num{i+1}",
        "email": f"user_{i+1}@example.com",
        "password": "password123",
        "roles": [
                "user"
        ]
    }
    response = requests.post(api_url, json=user_data)
    print(response)
    if response.status_code == 200:
        print(f"User {i+1} created successfully")
    else:
        print(f"Failed to create user {i+1}: {response.text}")
