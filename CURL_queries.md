##CURL Queries

Get All

> curl -X GET \
  http://localhost:8080/topjava/rest/meals/ \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 0288c61b-280e-38d8-c2c8-d01db912ec0d'
  
 Delete one meal  


> curl -X DELETE \
  http://localhost:8080/topjava/rest/meals/100005 \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 8e12eb0e-4cd6-6cf0-25bf-459e78e96b0c'
  
  
  Get one meal
  
 > curl -X GET \
  http://localhost:8080/topjava/rest/meals/100005 \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 098de8ba-a49f-7b24-078e-002ba2e8ae1b'


Create new Meal

> curl -X POST \
  http://localhost:8080/topjava/rest/meals \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 98356bbc-6511-5717-0f38-48aa4896e850' \
  -d '{
        "dateTime": "2020-01-31T17:17:00",
        "description": "TestRESTmeal",
        "calories": 1111
    }'
	
	
Update meal

> curl -X PUT \
  http://localhost:8080/topjava/rest/meals/100007 \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 87be19ee-9c81-fdbc-85a5-e6eb5ae29e5e' \
  -d '{
    "id": 100007,
    "dateTime": [
        2020,
        1,
        31,
        13,
        0
    ],
    "description": "Обед_Updated_REST",
    "calories": 1111
}'
	
	
Get Filtered meals (GetBetween)

> curl -X GET \
  'http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=10%3A00%3A00&endTime=20%3A01%3A00' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: b933d4fa-8e1d-db3a-ea1d-76011395fedd'
