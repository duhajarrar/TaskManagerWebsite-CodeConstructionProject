import requests

api_url = "http://localhost:8888/api/auth/signin"
number_of_users = 100
number_of_tasks_per_user = 10000
status = ["To Do", "In Progress", "Done"]
priorityLevel = ["High", "Medium", "Low"]
achievedPercentage = [5, 10, 20, 30, 40, 50, 60, 70, 80, 90]
completed = [True, False]
for i in range(number_of_users):
    user_data = {
        "username": f"user_num{i+1}",
        "password": "password123",
    }
    response = requests.post(api_url, json=user_data)
    print(response)
    token = response.json()["accessToken"]
    print(response.json()["accessToken"])

    if response.status_code == 200:
        print(f"User {i+1} sign in successfully")
    else:
        print(f"Failed to sign in user {i+1}: {response.text}")

    create_api_url = "http://localhost:8888/api/task/create"
    for j in range(number_of_tasks_per_user):
        user_data = {
            "taskName": f"user_{i+1}_task_{j+1}",
            "tag": "Work",
            "description": "task description",
            "priorityLevel": f"{priorityLevel[j%3]}",
            "preRequisites": [],
            "status": f"{status[j%3]}",
            "dueDate": f"2023-0{j%9+1}-0{j%9+1}",
            "category": "Reports",
            "reminderDate": f"2023-0{j%9+1}-0{j%3+1}",
            "completed": completed[j % 2],
            "achievedPercentage": achievedPercentage[j % 10],
            "assignedUser": f"user_{i+1}",
        }
        print(user_data)
        response = requests.post(
            create_api_url, json=user_data, headers={"Authorization": f"Bearer {token}"}
        )
        print(response.text)

        if response.status_code == 201:
            print(f"Task {i+1} created successfully")
        else:
            print(f"Failed to create task {i+1}: {response.text}")
