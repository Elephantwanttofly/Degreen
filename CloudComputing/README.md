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
GET index
https://degreen-apps-polsmieylq-as.a.run.app
This endpoint provides a simple welcome message indicating the successful fetching of the API. It serves as a starting point to check if the API is operational.

GET All Soils
https://degreen-apps-polsmieylq-as.a.run.app/soil
Endpoint
GET /soil
Description : Retrieve information about all valid soil types. This endpoint provides a list of soils, including their IDs and additional details.

