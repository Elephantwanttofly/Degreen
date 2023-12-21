# **Cloud Computing**
<br>

# **Soil Classification API**
This API provides information about various soil types, their descriptions, recommended plants, and a prediction feature for soil type based on uploaded images. It is developed using Flask, Firebase, and a pre-trained machine learning model.

# **Tech Stack**
Flask, Cloud Run, Realtime Database, Firebase Storage.

# **Firebase Configuration**
The application uses Firebase Realtime Database to store predictions. Make sure to replace the Firebase credentials and database URL in app.py with your own.

# **Model Details**
The soil classification model is based on soil_classification_model2.h5. The labels used by the model are stored in labels.txt.

# **Documentation APIs**
<br>
For details regarding the endpoints, please refer to the provided documentation [Here](https://documenter.getpostman.com/view/30136639/2s9Ykq8gJj).
GET index
https://degreen-apps-polsmieylq-as.a.run.app
<br>
This endpoint provides a simple welcome message indicating the successful fetching of the API. It serves as a starting point to check if the API is operational.

GET All Soils
https://degreen-apps-polsmieylq-as.a.run.app/soil
<br>
Endpoint
GET /soil
Description : Retrieve information about all valid soil types. This endpoint provides a list of soils, including their IDs and additional details.

GET Soil by ID
https://degreen-apps-polsmieylq-as.a.run.app/soil/001
<br>
Endpoint
GET /soil/{id_tanah}
Description :Retrieve detailed information about a specific soil type identified by its ID (id_tanah). You can specify the type of data requested, such as recommendations for planting, soil description, and more.

Request
Method: GET
Query Parameters
<ol>
  <li> data_requested (optional): Specify the type of data to be requested (e.g., rekomendasi_bibit, deskripsi_tanah, jenis, url_tanah). </li>
  <li> plant_id (optional): Specify the plant ID for more detailed information. </li>
  <li> get_detail (optional): Specify the detail to be retrieved </li>
</ol>


GET Soil Recommendation
https://degreen-apps-polsmieylq-as.a.run.app/soil/001/rekomendasi_bibit
<br>
Endpoint
GET /soil/{soil_id}/rekomendasi_bibit
Description : Retrieve recommended plants for a specific soil type. This endpoint provides information about recommended plants, including their IDs and details.

POST Upload Image for Prediction
https://degreen-apps-polsmieylq-as.a.run.app/upload
<br>
Endpoint
POST /upload
Description : Upload an image for soil type prediction. The API utilizes a pre-trained deep learning model to analyze the image and predict the soil type. The predicted results, including the class name and confidence score, are then stored in Firebase Realtime Database.

Request 
<li>Method: POST</li>
<li>Body: Form-data with key image and the image file.</li>
Body : formdata
