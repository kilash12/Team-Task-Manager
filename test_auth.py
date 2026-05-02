import requests
import re

session = requests.Session()

print("--- Testing Registration ---")
res = session.get('http://localhost:8081/register')
match = re.search(r'name="_csrf"\s+value="([^"]+)"', res.text)
csrf_token = match.group(1) if match else None
print(f"Register CSRF Token: {csrf_token}")

register_data = {
    'name': 'Test User',
    'email': 'testuser2@example.com',
    'phoneNumber': '1234567890',
    'password': 'password123',
    'about': 'Test User About',
    '_csrf': csrf_token
}

res2 = session.post('http://localhost:8081/do-register', data=register_data)
print(f"Register POST Status: {res2.status_code}")
print(f"Register POST URL: {res2.url}")

print("\n--- Testing Login ---")
res3 = session.get('http://localhost:8081/login')
match = re.search(r'name="_csrf"\s+value="([^"]+)"', res3.text)
csrf_token = match.group(1) if match else None
print(f"Login CSRF Token: {csrf_token}")

login_data = {
    'email': 'testuser2@example.com',
    'password': 'password123',
    '_csrf': csrf_token
}

res4 = session.post('http://localhost:8081/authenticate', data=login_data)
print(f"Login POST Status: {res4.status_code}")
print(f"Login POST URL: {res4.url}")

print("\n--- Testing Dashboard Access ---")
res5 = session.get('http://localhost:8081/user/dashboard')
print(f"Dashboard GET Status: {res5.status_code}")
print(f"Dashboard GET URL: {res5.url}")
