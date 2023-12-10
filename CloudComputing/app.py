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

cred = credentials.Certificate('C:\\Users\\Lenovo\\AppData\\Local\\Programs\\degreen-project-capstone-firebase-adminsdk-k7s32-dab5bf5bc6.json')
firebase_admin.initialize_app(cred, {
    'databaseURL': 'https://degreen-project-capstone-default-rtdb.asia-southeast1.firebasedatabase.app/'  # Ganti dengan URL database Firebase Anda
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

@app.route("/")
def index():
    return jsonify({
        "status": {
            "code": 200,
            "message": "Welcome to DeGreen",
        },
        "data": None
    }), 200

# API TYPES OF PLANTS
@app.route('/plants', methods=['GET'])
def get_all_plants():
    plants_ref = db.reference('jenis_tanaman')
    all_plants_data = plants_ref.get()
    
    if all_plants_data:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all plants",
            },
            "data": all_plants_data
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "No plants found"
            }
        }), 404

@app.route('/plants/<string:plant_id>', methods=['GET'])
def plant_detail(plant_id):
    plant_ref = db.reference('jenis_tanaman').child(plant_id)
    plant_data = plant_ref.get()
    
    data_requested = request.args.get('data_requested')  
    if plant_data:
        if data_requested in ['deskripsi_tanaman', 'id_tanaman', 'nama', 'url_gambar', 'url_produk']:
            response_data = plant_data.get(data_requested)
            return jsonify({ 
                "status": {
                    "code": 200,
                    "message": f"Success get {data_requested} based on ID"
                },
                "data": response_data
            }), 200
        elif data_requested == 'detail':
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get detail based on ID"
                },
                "data": plant_data  
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid data_requested parameter"
                }
            }), 400
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Plant not found"
            }
        }), 404


@app.route('/plants/search', methods=['GET'])
def search_plants_by_keyword():
    keyword = request.args.get('keyword')

    if not keyword:
        return jsonify({'error': 'No keyword provided for search'})

    plants_ref = db.reference('jenis_tanaman')
    all_plants_data = plants_ref.get()

    if all_plants_data:
        matched_plants = {}
        for plant_id, plant_data in all_plants_data.items():
            plant_name = plant_data.get('nama', '').lower()
            if keyword.lower() in plant_name:
                matched_plants[plant_id] = plant_data

        if matched_plants:
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Found matching plants for the keyword '{keyword}'",
                },
                "data": matched_plants
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 404,
                    "message": f"No matching plants found for the keyword '{keyword}'"
                }
            }), 404
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "No plants found"
            }
        }), 404



# API SOIL TYPE
@app.route('/soil', methods=['GET'])
def get_all_soils():
    soils_ref = db.reference('tanah')
    all_soils_data = soils_ref.get()
    
    if all_soils_data:
        return jsonify({
            "status": {
                "code": 200,
                "message": "Success get all soils",
            },
            "data": all_soils_data
        }), 200
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "No soils found"
            }
        }), 404

@app.route('/soil/<string:soil_name>', methods=['GET'])
def soil_detail(soil_name):
    soil_ref = db.reference('tanah').child(soil_name)
    soil_data = soil_ref.get()
    
    data_requested = request.args.get('data_requested')
    
    if soil_data:
        if data_requested in ['deskripsi_tanah', 'jenis', 'nama_tanah', 'rekomendasi_bibit']:
            response_data = soil_data.get(data_requested)
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get {data_requested} for soil {soil_name}"
                },
                "data": response_data
            }), 200
        elif not data_requested:
            return jsonify({
                "status": {
                    "code": 200,
                    "message": f"Success get detail for soil {soil_name}"
                },
                "data": soil_data
            }), 200
        else:
            return jsonify({
                "status": {
                    "code": 400,
                    "message": "Invalid data_requested parameter"
                }
            }), 400
    else:
        return jsonify({
            "status": {
                "code": 404,
                "message": "Soil not found"
            }
        }), 404

# MAIN FUNCTION
@app.route('/upload', methods=['POST'])
def upload_image():
    uploads_dir = 'uploads'  # Ubah nama variabel sesuai dengan yang benar
    if not os.path.exists(uploads_dir):
        os.makedirs(uploads_dir)

    if 'image' not in request.files:
        return jsonify({'error': 'No image part'})

    image = request.files['image']

    if image.filename == '':
        return jsonify({'error': 'No selected image'})

    if image and allowed_image(image.filename):
        image_path = os.path.join(uploads_dir, secure_filename(image.filename))  # Gunakan variabel uploads_dir yang benar
        image.save(image_path)

        class_name, confidence_score = predict_image(image_path)

        # Simpan hasil prediksi ke Firebase Realtime Database
        firebase_ref.push({
            'image_name': image.filename,
            'class_name': class_name,
            'confidence_score': f"{float(confidence_score) * 100:.2f}" 
        })

        # Menambahkan informasi prediksi ke respons JSON
        return jsonify({
            'success': 'File uploaded and predictions saved to Firebase',
            'prediction': {
                'class_name': class_name,
                'confidence_score': f"{float(confidence_score) * 100:.2f}" 
            }
        })
    else:
        return jsonify({'error': 'Failed to upload file or file type not allowed'})

if __name__ == "__main__":
    app.run(debug=True, host="0.0.0.0", port=int(os.environ.get("PORT", 8080)))

