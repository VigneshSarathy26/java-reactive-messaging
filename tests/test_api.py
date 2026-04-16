import pytest
import requests
import uuid

BASE_URL = "http://localhost:80"  # Nginx port

def test_health_check():
    response = requests.get(f"{BASE_URL}/health")
    assert response.status_code == 200
    assert response.text == "OK"

def test_get_all_products():
    response = requests.get(f"{BASE_URL}/api/v1/products")
    assert response.status_code == 200
    products = response.json()
    assert len(products) >= 2
    assert any(p['name'] == 'Reactive Microservice Book' for p in products)

def test_create_and_get_product():
    new_product = {
        "name": "Testing Product",
        "description": "Created by Pytest",
        "price": 19.99,
        "stock": 10
    }
    
    # Create product
    create_resp = requests.post(f"{BASE_URL}/api/v1/products", json=new_product)
    assert create_resp.status_code == 201
    created_id = create_resp.json()['id']
    
    # Get created product
    get_resp = requests.get(f"{BASE_URL}/api/v1/products/{created_id}")
    assert get_resp.status_code == 200
    assert get_resp.json()['name'] == "Testing Product"

@pytest.mark.asyncio
async def test_api_via_playwright(page):
    # Testing that the API endpoints are reachable via browser-like interaction
    await page.goto(f"{BASE_URL}/api/v1/products")
    content = await page.content()
    assert "Reactive Microservice Book" in content
