import os
import numpy as np

from flask import Flask, jsonify, request
import firebase_admin
from firebase_admin import credentials, db
import random
from PIL import Image
from tensorflow.keras.models import load_model
from werkzeug.utils import secure_filename

app = Flask(__name__)
app.config['ALLOWED_EXTENSIONS'] = set(['png', 'jpg', 'jpeg'])
app.config['UPLOAD_FOLDER'] = 'db.prediksi'
app.config['MODEL_FILE'] = 'soil_classification_model2.h5'
app.config['LABELS_FILE'] = 'labels.txt'

cred = credentials.Certificate('degreen-project-capstone-firebase-adminsdk-k7s32-dab5bf5bc6.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://degreen-project-capstone-default-rtdb.asia-southeast1.firebasedatabase.app/'
})
firebase_ref = db.reference('/predictions')

def allowed_image(filename):
    return '.' in filename and filename.rsplit('.', 1)[1].lower() in app.config['ALLOWED_EXTENSIONS']


model = load_model(app.config['MODEL_FILE'], compile=False)
with open(app.config['LABELS_FILE'], 'r') as file:
    labels = file.read().splitlines()

def predict_image(image):
    img = Image.open(image).convert("RGB")
    img = img.resize((224, 224))
    img_array = np.asarray(img)
    img_array = np.expand_dims(img_array, axis=0)
    normalized_image_array = (img_array.astype(np.float32) / 255.0)
    predictions = model.predict(normalized_image_array)
    index = np.argmax(predictions)
    class_name = labels[index]
    confidence_score = predictions[0][index]
    return class_name, confidence_score

valid_ids = ['001', '002', '003', '004', '005']
valid_id = ['T01P', 'T02D', 'T03M', 'T04J', 'T05S', 'T06B', 'T07K', 'T08T', 'T09T', 'T10B', 'T11S', 'T12A',
                   'T13J', 'T14K', 'T15W', 'T16B', 'T17K', 'T18J', 'T19K', 'T20C', 'T21N', 'T22K', 'T23R', 'T24A',
                   'T25L', 'T26K', 'T27C', 'T28S', 'T29K', 'T30S', 'T31A', 'T32P', 'T33D', 'T34J', 'T35B', 'T36K',
                   'T37U', 'T38B', 'T39M', 'T40K', 'T41M', 'T42J', 'T43J', 'T44M', 'T45S', 'T46C', 'T47J', 'T48P',
                   'T49B', 'T50K']

@app.route("/")
def index():
    return jsonify({
        "status": {
            "code": 200,
            "message": "success fetching api",
        },
        "data": None
    }), 200

#API SOIL TYPE
@app.route('/soil', methods=['GET'])
def get_soil_list():
    soil_list = []

    for soil_id in valid_ids:
        soil_ref = db.reference('tanah').child(soil_id)
        soil_detail = soil_ref.get()

        rekomendasi_bibit = soil_detail.get('rekomendasi_bibit')
        if rekomendasi_bibit:
            rekomendasi_list = [{"id_tanaman": key, **value} for key, value in rekomendasi_bibit.items()]

            url_tanah = soil_detail.get('url_tanah')  

            soil_data = {
                "id": soil_id,
                "deskripsi_tanah": soil_detail.get("deskripsi_tanah"),
                "jenis": soil_detail.get("jenis"),
                "rekomendasi_bibit": rekomendasi_list,
                "url_tanah": url_tanah
            }

            soil_list.append(soil_data)

    if soil_list:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get soil list",
            },
            "data": soil_list
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soils not found",
            }
        }), 404

@app.route('/soil/<string:soil_id>', methods=['GET'])
def get_soil_by_id(soil_id):
    if soil_id in valid_ids:
        soil_ref = db.reference('tanah').child(soil_id)
        soil_detail = soil_ref.get()

        rekomendasi_bibit = soil_detail.get('rekomendasi_bibit')
        if rekomendasi_bibit:
            rekomendasi_list = [{"id_tanaman": key, **value} for key, value in rekomendasi_bibit.items()]

            # Mendapatkan URL tanah dari database Firebase
            url_tanah = soil_detail.get('url_tanah')  
            
            soil_data = {
                "id": soil_id,
                "deskripsi_tanah": soil_detail.get("deskripsi_tanah"),
                "jenis": soil_detail.get("jenis"),
                "rekomendasi_bibit": rekomendasi_list,
                "url_tanah": url_tanah 
            }

            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get soil {soil_id}",
                },
                "data": soil_data
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": f"No rekomendasi bibit found for soil {soil_id}"
                }
            }), 404
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soil ID not found"
            }
        }), 404

@app.route('/soil/<string:soil_id>/rekomendasi_bibit', methods=['GET'])
def get_soil_recommendation(soil_id=None):
    if soil_id and soil_id in valid_ids: 
        soil_ref = db.reference('tanah').child(soil_id)
        soil_data = soil_ref.get()

        rekomendasi_bibit = soil_data.get('rekomendasi_bibit', {}) 

        data = []
        for plant_id, plant_data in rekomendasi_bibit.items():
            plant_data['id_tanaman'] = plant_id
            data.append(plant_data)

        if data: 
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get rekomendasi bibit for soil {soil_id}",
                },
                "data": data
            }), 200
        else: 
            return jsonify({
                "status": {
                    "code": 404,
                    "message": f"No rekomendasi bibit found for soil {soil_id}"
                }
            }), 404
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soil ID not found or invalid"
            }
        }), 404

# MAIN FUNCTION
@app.route('/upload', methods=['POST'])
def upload_image():
    uploads_dir = 'uploads' 
    if not os.path.exists(uploads_dir):
        os.makedirs(uploads_dir)

    if 'image' not in request.files:
        return jsonify({
        "status": {
            "code": 400,
            "message": "Bad request: Please input an image"
        }
    }), 400

    image = request.files['image']

    if image.filename == '':
        return jsonify({
        "status": {
            "code": 400,
            "message": "Bad request: No selected image"
        }
    }), 400

    if image and allowed_image(image.filename):
        image_path = os.path.join(uploads_dir, secure_filename(image.filename)) 
        image.save(image_path)

        class_name, confidence_score = predict_image(image_path)

        confidence_percentage = "{:.2%}".format(confidence_score)

        firebase_ref.push({
            'image_name': image.filename,
            'class_name': class_name,
            'confidence_score': confidence_percentage 
        })

        return jsonify({
            'success': 'File uploaded and predictions saved to Firebase',
            'prediction': {
                'class_name': class_name,
                'confidence_score': confidence_percentage 
            }
        })
    else:
        return jsonify({
            "status": {
                "code": 400,
                "message": "Failed to upload file or file type not allowed"
            }
        }), 400

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))
